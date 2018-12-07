package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Zombie extends Enemy {
	
	private static double width;
	private static double height;
	
	private static Image[][] walkingAnimation;
	private static int walkingAnimationLength;
	private static int walkingHoldFrame;
	
	private static int maxHealth = 20;
	private static double speed = 10;
	private static int reward = 20;

	public Zombie(Field field, Path path, double pathShift, int spawnTime) {
		super(field, path, pathShift, maxHealth, speed, reward, spawnTime);
		
		walkingAnimationLength = 16;
		walkingAnimation = new Image[8][walkingAnimationLength];
		walkingHoldFrame = 10;
		
		width = 100;
		height = 100;
	}

	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		if (spawnTime > 0) return;
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		
		int walkFrameIndex = (int) lifeTime % walkingHoldFrame;
		gc.drawImage(walkingAnimation[direction][walkFrameIndex], drawX, drawY);
	
	}

	@Override
	public void damage(Damage damage, double amount) {

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