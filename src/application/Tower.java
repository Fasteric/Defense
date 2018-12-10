package application;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Tower implements Renderable, MouseInteractable {
	
	private static Random random = new Random(3);
	
	protected Field field;
	protected Point2D position;
	protected int direction;
	protected double radiusX;
	protected double radiusY;
	
	protected int lifeCycle;
	protected int prefiringDelay;
	protected int postfiringDelay;
	
	protected Point2D projectileDestination;
	
	protected boolean isSearching = false;
	protected boolean isPrefiring = false;
	protected boolean isPostfiring = true;
	
	
	/*** Constructor ***/
	
	public Tower(Field field, Point2D position, int direction,
			int prefiringDelay, int postfiringDelay, double radiusX, double radiusY) {
		this.field = field;
		this.position = position;
		this.direction = direction;
		this.prefiringDelay = prefiringDelay;
		this.postfiringDelay = postfiringDelay;
		this.radiusX = radiusX;
		this.radiusY = radiusY;
		
		lifeCycle = 1;
	}
	
	
	/*** Tick ***/
	
	public void tick(long now, GraphicsContext gc) {
		logicUpdate(now);
		graphicUpdate(gc);
		lifeCycle++;
	}
	
	protected void logicUpdate(long now) {
		
		if (isSearching) {
			boolean isFound = search();
			if (isFound) {
				isSearching = false;
				isPrefiring = true;
				lifeCycle = -prefiringDelay;
			}
			return;
		}
		
		if (lifeCycle == 0) {
			fire();
			isPrefiring = false;
			isPostfiring = true;
			return;
		}
		
		if (lifeCycle > postfiringDelay) {
			isPostfiring = false;
			isSearching = true;
			return;
		}
		
	}
	
	protected boolean search() {
		boolean isFound = false;
		Enemy lowestHealthEnemy = null;
		double lowestHealth = -1;
		ArrayList<Enemy> enemiesInRange = new ArrayList<>();
		for (Enemy enemy : field.getEnemyOnField()) {
			if (isInFiringRange(enemy.getPosition())) {
				enemiesInRange.add(enemy);
				if (!isFound) {
					lowestHealthEnemy = enemy;
					lowestHealth = enemy.getHealth();
					isFound = true;
				}
				else if (enemy.getHealth() < lowestHealth) {
					lowestHealthEnemy = enemy;
					lowestHealth = enemy.getHealth();
				}
			}
		}
		if (isFound) {
			Point2D scaledMomentum = PointOperations.scale(lowestHealthEnemy.getMomentum(), prefiringDelay);
			projectileDestination = PointOperations.add(lowestHealthEnemy.getPosition(), scaledMomentum);
		}
		return isFound;
	}
	
	protected boolean isInFiringRange(Point2D target) {
		Point2D different = PointOperations.different(position, target);
		double dx = different.getX();
		double dy = different.getY();
		return radiusY * radiusY * dx * dx + radiusX * radiusX * dy * dy < 
				radiusX * radiusX * radiusY * radiusY;
	}
	
	protected abstract void fire();
	
	protected abstract void graphicUpdate(GraphicsContext gc);
	
	
	public Point2D getPosition() {
		return position;
	}
	
	public int getDirection() {
		return direction;
	}

}
