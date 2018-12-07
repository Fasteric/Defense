package application;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ArtilleryTower extends Tower {
	
	private static Image[] idle;
	private static Image[] hover;
	
	private static Image[][] firingAnimation;
	private static int firingAnimationLength = 18;
	
	static {
		// load image
	}
	
	private static double width;
	private static double height;
	
	private static int prefiringDelay = 60;
	private static int postfiringDelay = 120;
	
	private static double radiusX = 100;
	private static double radiusY = 70;
	
	
	/*** Constructor ***/

	public ArtilleryTower(Field field, Point2D position, int direction) {
		super(field, position, direction, prefiringDelay, postfiringDelay, radiusX, radiusY);
	}


	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		
		if (isSearching) {
			gc.drawImage(idle[direction], drawX, drawY);
		}
		
		if (isPrefiring || isPostfiring) {
			int frame = (lifeCycle + prefiringDelay) / 10;
			gc.drawImage(firingAnimation[direction][frame], drawX, drawY);
		}
		
	}


	@Override
	protected void fire() {
		
	}


	@Override
	public boolean hover(Point2D hoverPosition) {
		return false;
	}


	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		return false;
	}


	@Override
	public boolean unclick() {
		return false;
	}

}
