package application;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class CooldownButton implements Renderable, MouseInteractable {
	
	private Image disable;
	private Image enable;
	private Image hover;
	
	private double width;
	private double height;
	
	private Field field;
	private Point2D position;
	private int maxCooldown;
	
	private int cooldown = 0;
	
	private boolean isPause = false;
	private boolean isEnable;
	private boolean isHover = false;
	private boolean isClicked = false;
	
	
	public CooldownButton(Field field, Image disable, Image enable, Image hover, Point2D position, 
			double width, double height, int maxCooldown, boolean isEnable) {
		this.field = field;
		this.position = position;
		
		this.disable = disable;
		this.enable = enable;
		this.hover = hover;
		
		width = enable.getWidth();
		height = enable.getHeight();
		
		this.maxCooldown = maxCooldown;
		
		this.isEnable = isEnable;
		if (isEnable) cooldown = maxCooldown;
		
	}
	
	
	@Override
	public void tick(long now, GraphicsContext gc) {
		
		double drawX = position.getX();
		double drawY = position.getY();
		
		if (isClicked) {
			isClicked = true;
			isEnable = false;
		}
		
		
		if (!isEnable) {
			if (isPause) return;
			gc.drawImage(disable, drawX, drawY);
			cooldown++;
			return;
		}
		
		if (!isHover) gc.drawImage(enable, drawX, drawY);
		else gc.drawImage(hover, drawX, drawY);
		
		if (cooldown < maxCooldown) {
			//isEnable = false; // failsafe
			//isHover = false;
			cooldown++;
		}
		else {
			isEnable = true;
		}
		
	}
	
	
	public void setMaxCooldown(int maxCooldown) {
		this.maxCooldown = maxCooldown;
		this.cooldown = 0;
	}
	
	public void pause() {
		isPause = true;
	}
	
	public boolean retrieveClick() {
		if (isClicked) {
			isClicked = false;
			return true;
		}
		return false;
	}
	
	
	@Override
	public boolean hover(Point2D hoverPosition) {
		if (!isEnable) return false;
		if (!isInRect(hoverPosition)) return false;
		isHover = true;
		return true;
	}
	
	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		if (!isEnable) return false;
		if (!isInRect(pressPosition) || !isInRect(releasePosition)) return false;
		isClicked = true;
		return true;
	}
	
	@Override
	public void unclick() {
		
	}
	
	private boolean isInRect(Point2D mouse) {
		boolean inRect = true;
		inRect &= mouse.getX() >= position.getX();
		inRect &= mouse.getX() - position.getX() <= width;
		inRect &= mouse.getY() >= position.getY();
		inRect &= mouse.getY() - position.getY() <= height;
		return inRect;
	}


	@Override
	public double getRenderPriority() {
		return 10000;
	}

	@Override
	public int compareTo(Renderable other) {
		return Double.compare(10000, other.getRenderPriority());
	}
	
}
