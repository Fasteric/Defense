package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Zombie extends Enemy {
	
	private static Image[][] walking = new Image[8][21];
	private static int walkingLength = 21;
	private static int walkingHold = 6;
	
	private static double width = 24;
	private static double height = 40;
	
	private static boolean needFlip(int direction) {
		if (direction == 0 || direction == 3 || direction == 5) return true;
		return false;
	}
	
	static {
		// load image
		for (int i = 0; i < 8; i++) {
			if (i == 0 || i == 4) walking[i] = ImageLoader.zombie4;
			if (i == 1 || i == 3) walking[i] = ImageLoader.zombie1;
			if (i == 2) walking[i] = ImageLoader.zombie2;
			if (i == 5 || i == 7) walking[i] = ImageLoader.zombie7;
			if (i == 6) walking[i] = ImageLoader.zombie6;
		}
	}
	
	private static int maxHealth = 50;
	private static double speed = 10;
	private static int cost = 1;
	private static int reward = 20;

	public Zombie(Field field, Path path, double pathShift, int spawnTime) {
		super(field, path, pathShift, maxHealth, speed, cost, reward, spawnTime);
	}

	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		if (lifeTime < spawnTime) return;
		
		double drawX = position.getX();
		double drawY = position.getY() - height;
		
		int walkFrameIndex = (int) lifeTime / walkingHold % walkingLength;
		
		if (needFlip(direction)) {
			drawX += width / 2;
			gc.drawImage(walking[direction][walkFrameIndex], 0, 0, 300, 500, drawX, drawY, -width, height);
		}
		else {
			drawX -= width / 2;
			gc.drawImage(walking[direction][walkFrameIndex], drawX, drawY, width, height);
		}
	
	}

	@Override
	public void damage(Damage damage, double amount) {
		health -= amount;
		System.out.println("DEBUG : Zombie.damage health : " + health);
	}

	
	@Override
	public double getRenderPriority() {
		return position.getY();
	}

	@Override
	public int compareTo(Renderable other) {
		return Double.compare(getRenderPriority(), other.getRenderPriority());
	}
	
}