package application;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ArtilleryTower {
	
	private static Image[][] constructingAnimation;
	private static int constructingAnimationLength = 1;
	
	private static Image[][] firingAnimation;
	private static int firingAnimationLength = 10;
	
	private static Random random;
	
	static {
		// load image
		
		random = new Random(3);
	}
	
	private static double width;
	private static double height;
	
	private Point2D position;
	private int direction;
	
	private double radiusX;
	private double radiusY;
	
	private int maxConstructingFrameHold = 20;
	private int constructingFrameHold;
	private int constructingFrameIndex;
	private boolean isConstructed;
	private boolean isConstructing;
	
	private int maxFiringFrameHold = 5;
	private int firingFrameHold;
	private int firingFrameIndex;
	private boolean isFiring;
	
	private int triggeringFrame = 3;
	
	private int maxFiringCooldown = 180;
	private int firingCooldown;
	
	private Point2D primedTntDestination;
	
	
	public ArtilleryTower(Point2D position, int direction) {
		this.position = position;
		this.direction = direction;

		this.isConstructed = false;
		this.isConstructing = false;
		this.firingCooldown = 0;
	}
	
	
	public void tick(long now, GraphicsContext gc, ArrayList<Zombie> enemiesOnField) {
		
		if (firingCooldown > 0) {
			firingCooldown--;
			return;
		}
		
		if (!isConstructed) {
			construct(gc);
			return;
		}
		
		fire(gc, enemiesOnField);
		
	}
	
	private void construct(GraphicsContext gc) {
		if (!isConstructing) {
			isConstructing = true;
			constructingFrameIndex = 0;
			draw(gc, constructingAnimation[direction][0]);
			constructingFrameHold = maxConstructingFrameHold;
		}
		if (constructingFrameHold > 0) {
			constructingFrameHold--;
			return;
		}
		constructingFrameIndex++;
		if (constructingFrameIndex < constructingAnimationLength) {
			draw(gc, constructingAnimation[direction][constructingFrameIndex]);
			constructingFrameHold = maxConstructingFrameHold;
		}
		else {
			isConstructed = true;
			isConstructing = false;
		}
	}
	
	
	private void fire(GraphicsContext gc, ArrayList<Zombie> enemiesOnField) {
		
		if (!isFiring) {
			Zombie enemy = pickEnemyInRange(enemiesOnField);
			if (enemy != null) {
				locatePrimedTntDestination(enemy);
				draw(gc, firingAnimation[direction][0]);
			}
		}
		
		if (firingFrameHold > 0) {
			firingFrameHold--;
			return;
		}
		
		firingFrameIndex++;
		if (firingFrameIndex >= firingAnimationLength) {
			firingCooldown = maxFiringCooldown;
			isFiring = false;
		}
		draw(gc, firingAnimation[direction][firingFrameIndex]);
		firingFrameHold = maxFiringFrameHold;
		if (firingFrameIndex == triggeringFrame) {
			trigger();
		}
		
	}
	
	private void locatePrimedTntDestination(Zombie selectedEnemy) {
		Point2D enemyPosition = selectedEnemy.getPosition();
		Point2D enemyMomentum = selectedEnemy.getMomentum();
		enemyMomentum = PointOperations.scale(enemyMomentum, 100);
		primedTntDestination = PointOperations.add(enemyPosition, enemyMomentum);
		firingFrameIndex = 0;
		firingFrameHold = maxFiringFrameHold;
		isFiring = true;
	}
	
	private void trigger() {
		PrimedTnt primeTnt = new PrimedTnt(position, primedTntDestination);
		// someUpdateList.add(primedTnt);
	}
	
	
	private void draw(GraphicsContext gc, Image image) {
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		gc.drawImage(image, drawX, drawY);
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
