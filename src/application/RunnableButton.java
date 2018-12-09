package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class RunnableButton implements Renderable, MouseInteractable {
	
	private Point2D position;
	
	private Image disable;
	private Image idle;
	private Image hover;
	
	private double width;
	private double height;
	
	private boolean isDisabled = false;
	private boolean isHover = false;
	
	private Runnable hoverEvent = () -> {};
	private Runnable clickEvent = () -> {}; // WTF notation
	private Runnable unclickEvent = () -> {};
	
	
	public RunnableButton(Point2D position, Image disable, Image idle, Image hover, double width, double height) {
		this.position = position;
		this.disable = disable;
		this.idle = idle;
		this.hover = hover;
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
	
	public void setOnHovered(Runnable hoverEvent) {
		this.hoverEvent = hoverEvent;
	}
	public void setOnClicked(Runnable clickEvent) {
		this.clickEvent = clickEvent;
	}
	public void setOnUnclicked(Runnable unclickEvent) {
		this.unclickEvent = unclickEvent;
	}
	
	
	@Override
	public void tick(long now, GraphicsContext gc) {
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height / 2;
		
		if (isDisabled) gc.drawImage(disable, drawX, drawY);
		else if (isHover) gc.drawImage(hover, drawX, drawY);
		else gc.drawImage(idle, drawX, drawY);
		
		isHover = false;
		
	}

	
	@Override
	public boolean hover(Point2D hoverPosition) {
		if (!isInRange(hoverPosition)) {
			isHover = false;
			return false;
		}
		if (!isDisabled) isHover = true;
		hoverEvent.run();
		return true;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		if (isDisabled) return false;
		if (!isInRange(pressPosition) || !isInRange(releasePosition)) return false;
		clickEvent.run();
		return true;
	}

	@Override
	public void unclick() {
		unclickEvent.run();
	}
	
	private boolean isInRange(Point2D mousePosition) {
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
