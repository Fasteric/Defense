package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Enemy implements Comparable<Enemy> {
	
	protected Field field;
	protected Point2D position;
	protected int direction;
	protected Path path;
	protected double pathShift;
	protected double maxHealth;
	protected double health;
	protected double speed;
	protected int reward;
	protected long spawnTime;
	protected long callingTime;
	
	protected int pathIndex = 0; // will be changed to 1 at 'updateDestination'
	protected Point2D momentum;
	protected int subpathTickRemaining = 0;
	
	protected boolean isCalled = false;
	protected boolean isSpawned = false;
	protected boolean isInvaded = false;
	
	protected long lifeTime = 0;
	
	
	/*** Constructor ***/
	
	public Enemy(Field field, Path path, double pathShift, 
			double maxHealth, double speed, int reward, long spawnTime) {
		this.field = field;
		this.path = path;
		this.pathShift = pathShift;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.speed = speed;
		this.reward = reward;
		this.spawnTime = spawnTime;
		
		position = path.getCoordinate(0, pathShift);
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
	}
	
	protected abstract void graphicUpdate(GraphicsContext gc);
	
	protected void logicUpdate(long now) {
		
		if (!isSpawned) {
			spawn(now);
			return;
		}
		
		if (health <= 0) {
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
		
		lifeTime++;
		
	}
	
	protected void spawn(long now) {
		if (!isCalled) return;
		if (callingTime + spawnTime < now) {
			isSpawned = true;
		}
	}
	
	protected void updateDestination() {
		Point2D destination = path.getCoordinate(pathIndex, pathShift);
		Point2D subpath = PointOperations.different(position, destination);
		double length = PointOperations.getSize(subpath);
		subpathTickRemaining = (int) (60 * length / speed);
		double distantPerTick = length / subpathTickRemaining;
		momentum = PointOperations.normalize(subpath);
		momentum = PointOperations.scale(momentum, distantPerTick);
	}
	
	protected void updateDirection() {
		double angle = PointOperations.getAngle(momentum);
		if (angle < 0) angle += 180;
		if (angle >= 360 - 22.5 || angle < 0 + 22.5) direction = 0;
		if (angle >= 45 - 22.5 && angle < 45 + 22.5) direction = 1;
		if (angle >= 90 - 22.5 && angle < 90 + 22.5) direction = 2;
		if (angle >= 135 - 22.5 && angle < 135 + 22.5) direction = 3;
		if (angle >= 180 - 22.5 && angle < 180 + 22.5) direction = 4;
		if (angle >= 225 - 22.5 && angle < 225 + 22.5) direction = 5;
		if (angle >= 270 - 22.5 && angle < 270 + 22.5) direction = 6;
		if (angle >= 315 - 22.5 && angle < 315 + 22.5) direction = 7;
	}
	
	protected void updatePosition() {
		position = PointOperations.add(position, momentum);
	}
	
	protected void invading() {
		field.invaded(this);
		despawn();
	}
	protected void dying() {
		field.addMoney(reward);
		despawn();
	}
	protected void despawn() {
		field.removeEnemy(this);
	}
	
	
	/*** Utility ***/
	
	public abstract void damage(Damage damage, double amount);
	
	public abstract boolean hover(Point2D hoverPosition);
	
	public abstract boolean click(Point2D pressPosition, Point2D releasePosition);
	
	public abstract boolean unclick();
	
	public void call(long callingTime) {
		this.callingTime = callingTime;
	}
	
	public int compareTo(Enemy another) {
		return Long.compare(spawnTime, another.spawnTime);
	}
	
	public long getSpawnTime() {
		return spawnTime;
	}

}
