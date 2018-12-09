package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Enemy implements Renderable {
	
	protected Field field;
	protected Point2D position;
	protected int direction;
	protected Path path;
	protected double pathShift;
	protected double maxHealth;
	protected double health;
	protected double speed;
	protected int reward;
	protected int spawnTime;
	
	protected int pathIndex = 0;
	protected Point2D momentum;
	protected int subpathTickRemaining = 0;
	
	protected int lifeTime = 0;
	
	
	/*** Constructor ***/
	
	public Enemy(Field field, Path path, double pathShift, 
			double maxHealth, double speed, int reward, int spawnTime) {
		this.field = field;
		this.path = path;
		this.pathShift = pathShift;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.speed = speed;
		this.reward = reward;
		this.spawnTime = spawnTime;
		
		position = path.getCoordinate(0, pathShift);
		updateDestination();
	}
	
	public Point2D getPosition() {
		return new Point2D(position.getX(), position.getY());
	}
	
	public Point2D getMomentum() {
		return new Point2D(momentum.getX(), momentum.getY());
	}
	
	
	/*** Tick ***/
	
	public void tick(long now, GraphicsContext gc) {
		logicUpdate(now);
		graphicUpdate(gc);
		lifeTime++;
	}
	
	protected void logicUpdate(long now) {
		
		if (spawnTime > 0) {
			spawnTime--;
			return;
		}
		
		if (health <= 0) { // check here
			dying();
			return;
		}
		
		if (subpathTickRemaining == 0) {
			pathIndex++;
			
			if (pathIndex >= path.size()) {
				invading();
				return;
			}
			
			updateDestination();
			updateDirection();
		}
		
		updatePosition();
		
		subpathTickRemaining--;
		
	}
	
	protected void updateDestination() {
		Point2D destination = path.getCoordinate(pathIndex, pathShift);
		Point2D subpath = PointOperations.different(position, destination);
		double length = PointOperations.getSize(subpath);
		double angle = PointOperations.getAngle(subpath);
		double directionalSpeed = speed * isometricScale(angle);
		subpathTickRemaining = (int) (60 * length / directionalSpeed);
		double distantPerTick = length / subpathTickRemaining;
		momentum = PointOperations.normalize(subpath);
		momentum = PointOperations.scale(momentum, distantPerTick);
	}
	
	private double isometricScale(double angle) {
		double degrees = Math.toRadians(angle);
		double sin = Math.sin(degrees);
		double cos = Math.cos(degrees);
		return Math.sqrt(3d / 4) / Math.sqrt(1.5 * sin * sin + 0.5 * cos * cos);
	}
	
	protected void updateDirection() {
		double angle = PointOperations.getAngle(momentum);
		angle += 180;
		if (angle >= 360 - 22.5 || angle < 0 + 22.5) direction = 0;
		if (angle >= 45 - 22.5 && angle < 45 + 22.5) direction = 1;
		if (angle >= 90 - 22.5 && angle < 90 + 22.5) direction = 2;
		if (angle >= 135 - 22.5 && angle < 135 + 22.5) direction = 3;
		if (angle >= 180 - 22.5 && angle < 180 + 22.5) direction = 4;
		if (angle >= 225 - 22.5 && angle < 225 + 22.5) direction = 5;
		if (angle >= 270 - 22.5 && angle < 270 + 22.5) direction = 6;
		if (angle >= 315 - 22.5 && angle < 315 + 22.5) direction = 7;
		direction = (direction + 4) % 8; // workaround
	}
	
	protected void updatePosition() {
		position = PointOperations.add(position, momentum);
	}
	
	protected void invading() {
		System.out.println("DEBUG : Invading");
		field.invade(this);
		despawn();
	}
	protected void dying() {
		field.addMoney(reward);
		//generate smoke
		despawn();
	}
	protected void despawn() {
		field.removeEnemy(this);
	}
	
	
	protected abstract void graphicUpdate(GraphicsContext gc);
	
	public abstract void damage(Damage damage, double amount);
	
	
	public double getHealth() {
		return health;
	}
}
