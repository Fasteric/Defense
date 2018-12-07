package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class FieldButton {
	
	private Image disable;
	private Image enable;
	private Image hover;
	
	private Point2D position;
	
	private int maxCooldown;
	private int cooldown;
	private boolean isHover;
	private boolean isEnable;
	
	public FieldButton(Point2D position, Image disable, Image enable, Image hover, int cooldown) {
		this.maxCooldown = cooldown;
	}
	
	public void tick(long now, GraphicsContext gc) {
		if (cooldown > 0) {
			cooldown--;
			gc.drawImage(disable, position.getX(), position.getY());
			if (cooldown == 0) {
				isEnable = true;
			}
			return;
		}
		if (isEnable) {
			if (isHover) gc.drawImage(hover, position.getX(), position.getY());
			else gc.drawImage(enable, position.getX(), position.getY());
		}
		isHover = false;
	}
	
	public boolean hover() {
		if (false) return false; // check range
		isHover = true;
		return true;
	}
	
	public boolean click() {
		if (!isEnable) return false;
		if (false) return false; // check range
		isEnable = false;
		cooldown = maxCooldown;
		return true;
	}

}
