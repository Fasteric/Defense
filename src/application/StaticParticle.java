package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class StaticParticle implements Renderable {
	
	private Image[] frames;
	private int frameCount;
	
	private Field field;
	private Point2D position;
	private double width;
	private double height;
	private int frameHold;
	
	private int lifeTime = 0;
	
	public StaticParticle(Field field, Image[] frames, Point2D position2, double width, double height, int frameHold) {
		this.frames = frames;
		this.field = field;
		this.position = position2;
		this.width = width;
		this.height = height;
		this.frameHold = frameHold;
		
		frameCount = frames.length;
	}
	

	@Override
	public void tick(long now, GraphicsContext gc) {
		
		int frameIndex = lifeTime / frameHold;
		
		if (frameIndex >= frameCount) {
			// remove itself
			field.removeRender(this);
			return;
		}
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height / 2;
		
		gc.drawImage(frames[frameIndex], drawX, drawY, width, height);
		
		lifeTime++;
		
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
