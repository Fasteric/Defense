package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class RetrievalButton implements Renderable, MouseInteractable {
	
	private Point2D position;
	
	private static Image disable = ImageLoader.retrievalDisable;
	private static Image idle = ImageLoader.retrievalIdle;
	private static Image hover = ImageLoader.retrievalHover;
	
	private double width;
	private double height;
	
	private boolean isDisabled = false;
	private boolean isHover = false;
	private boolean isClicked = false;
	
	
	public RetrievalButton(Point2D position, double width, double height) {
		this.position = position;
		this.width = width;
		this.height = height;
	}
	
	public boolean isDisable() {
		return isDisabled;
	}
	public void disable() {
		this.isDisabled = true;
	}
	public void enable() {
		this.isDisabled = false;
	}
	
	public boolean retrieveClickStatus() { // someone must retrieve click status before tick
		boolean temp = isClicked;
		isClicked = false;
		return temp;
	}
	
	
	@Override
	public void tick(long now, GraphicsContext gc) {
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height / 2;
		
		if (isDisabled) gc.drawImage(disable, drawX, drawY, width, height);
		else if (isHover) gc.drawImage(hover, drawX, drawY, width, height);
		else gc.drawImage(idle, drawX, drawY, width, height);
		
		isHover = false;
		
	}

	
	@Override
	public boolean hover(Point2D hoverPosition) {
		if (!isMouseInRange(hoverPosition)) {
			isHover = false;
			return false;
		}
		if (!isDisabled) isHover = true;
		return true;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		if (!isMouseInRange(pressPosition) || !isMouseInRange(releasePosition)) return false;
		if (!isDisabled) isClicked = true;
		return true;
	}

	@Override
	public void unclick() {
		
	}
	
	private boolean isMouseInRange(Point2D mousePosition) {
		double dx = mousePosition.getX() - position.getX();
		double dy = mousePosition.getY() - position.getY();
		return dx >= -width / 2 && dx <= width / 2 && dy >= -height / 2 && dy <= height / 2;
	}
	

	@Override
	public double getRenderPriority() {
		return 3000;
	}

	@Override
	public int compareTo(Renderable other) {
		return Double.compare(3000, other.getRenderPriority());
	}

}
