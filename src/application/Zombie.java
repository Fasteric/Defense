package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Zombie extends Enemy {
	
	private double width;
	private double height;
	
	private Image[][] walkingAnimation;
	private int walkingAnimationLength;
	private int walkingHoldFrame;

	public Zombie(Field field, Path path, double pathShift, double maxHealth, double speed, long spawnTime) {
		super(field, path, pathShift, maxHealth, speed, spawnTime);
		
		walkingAnimationLength = 16;
		walkingAnimation = new Image[8][walkingAnimationLength];
		walkingHoldFrame = 10;
		
		width = 100;
		height = 100;
	}

	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		if (!isSpawned) return;
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		
		int walkFrameIndex = (int) lifeTime % walkingHoldFrame;
		gc.drawImage(walkingAnimation[direction][walkFrameIndex], drawX, drawY);
	
	}

	@Override
	public boolean hover(Point2D hoverPosition) {
		return false;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		return false;
	}
	
	
	
}