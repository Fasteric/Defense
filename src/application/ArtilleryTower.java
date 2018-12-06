package application;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ArtilleryTower {
	
	private static Image[] tower;
	
	private static Image[][] firingAnimation;
	private static int firingAnimationLength = 10;
	
	private static Random random;
	
	static {
		// load image
		
		random = new Random(3);
	}
	
	private static double width;
	private static double height;
	
	private Field field;
	
	private Point2D position;
	private int direction;
	
	private double radiusX;
	private double radiusY;
	
	private boolean isConstructed;
	
	private int maxFiringFrameHold = 5;
	private int firingFrameHold;
	private int firingFrameIndex;
	private boolean isFiring;
	
	private int triggeringFrame = 3;
	
	private int maxFiringCooldown = 180;
	private int firingCooldown;
	
	private Point2D primedTntDestination;
	
	
	public ArtilleryTower(Field field, Point2D position, int direction) {
		this.field = field;
		
		this.position = position;
		this.direction = direction;
		
		this.isConstructed = false;
		
		this.firingCooldown = maxFiringCooldown;
		this.isFiring = false;
	}
	
	
	public void construct(GraphicsContext gc) {
		gc.drawImage(tower[direction], position.getX() - width / 2, position.getY() - height);
		isConstructed = true;
	}
	
	public void tick(long now, GraphicsContext gc) {
		
		if (!isConstructed) {
			construct(gc);
		}
		
		if (firingCooldown > 0) {
			firingCooldown--;
		}
		else {
			commenceFiringSequence(field);
		}
		
		draw(gc);
		
	}
	
	private void commenceFiringSequence(Field field) {
		
		if (firingCooldown > 0) {
			return; // should not be called
		}
		
		if (!isFiring) {
			Zombie enemy = pickEnemyInRange(field.getEnemiesOnField());
			if (enemy != null) {
				locatePrimedTntDestination(enemy);
				firingFrameIndex = 0;
				firingFrameHold = maxFiringFrameHold;
				isFiring = true;
			}
		}
		
		if (firingFrameHold > 0) {
			firingFrameHold--;
			return;
		}
		
		firingFrameIndex++;
		firingFrameHold = maxFiringFrameHold;
		
		if (firingFrameIndex == triggeringFrame) {
			fire(field);
		}

		if (firingFrameIndex >= firingAnimationLength) {
			firingCooldown = maxFiringCooldown;
			isFiring = false;
		}
		
	}
	
	private void locatePrimedTntDestination(Zombie selectedEnemy) {
		Point2D enemyPosition = selectedEnemy.getPosition();
		Point2D enemyMomentum = selectedEnemy.getMomentum();
		enemyMomentum = PointOperations.scale(enemyMomentum, 100);
		primedTntDestination = PointOperations.add(enemyPosition, enemyMomentum);
		firingFrameIndex = 0;
		firingFrameHold = maxFiringFrameHold;
	}
	
	private void fire(Field field) {
		PrimedTnt primedTnt = new PrimedTnt(field, position, primedTntDestination);
		field.addProjectile(primedTnt);
	}
	
	
	private void draw(GraphicsContext gc) {
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		
		if (!isFiring) {
			gc.drawImage(tower[direction], drawX, drawY);
		}
		else {
			gc.drawImage(firingAnimation[direction][firingFrameIndex], drawX, drawY);
		}
	}
	
	/*** Utilities ***/
	
	private Zombie pickEnemyInRange(ArrayList<Zombie> enemiesOnField) {
		ArrayList<Zombie> enemiesInRange = new ArrayList<>();
		for (Zombie e : enemiesOnField) {
			if (isEnemyInRange(e)) {
				enemiesInRange.add(e);
			}
		}
		if (enemiesInRange.size() == 0) return null;
		int enemyIndex = random.nextInt(enemiesInRange.size());
		return enemiesInRange.get(enemyIndex);
	}
	
	private boolean isEnemyInRange(Zombie enemy) {
		double dx = enemy.getPosition().getX() - position.getX() - width / 2;
		double dy = enemy.getPosition().getY() - position.getY() - height / 2;
		return (dx * dx) / (radiusX * radiusX) + (dy * dy) / (radiusY * radiusY) <= 1;
	}

}
