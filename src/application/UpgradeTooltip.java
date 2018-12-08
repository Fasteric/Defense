package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class UpgradeTooltip implements Renderable, MouseInteractable {
	
	private static Image[] tooltip;
	
	static {
		// load image
	}
	
	private static double width = 150;
	private static double height = 150;
	
	private Field field;
	private NullTower owner;
	private Point2D position;
	
	private int hoverStatus = 0;
	
	
	public UpgradeTooltip(Field field, NullTower owner, Point2D position) {
		this.field = field;
		this.owner = owner;
		this.position = position;
	}
	

	@Override
	public void tick(long now, GraphicsContext gc) {
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		gc.drawImage(tooltip[hoverStatus], drawX, drawY);
	}
	

	@Override
	public boolean hover(Point2D hoverPosition) {
		
		hoverStatus = 0;
		
		double dx = hoverPosition.getX() - position.getX();
		double dy = hoverPosition.getY() - position.getY();
		
		if (!isMouseInRange(dx, dy)) return false;
		
		hoverStatus = mouseOn(dy);
		
		return true;
		
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		
		double px = pressPosition.getX() - position.getX();
		double py = pressPosition.getY() - position.getY();
		double rx = releasePosition.getX() - position.getX();
		double ry = releasePosition.getY() - position.getY();
		
		if (!isMouseInRange(px, py) || !isMouseInRange(rx, ry)) return false;
		
		int sp = mouseOn(py);
		int sr = mouseOn(ry);
		if (sp == sr) replaceNullTower(sp);
		return false;
		
	}
	
	@Override
	public void unclick() {
		field.removeRender(this); // hopefully no memory leak
	}
	
	private boolean isMouseInRange(double dx, double dy) {
		if (dx < -width / 2 || dx > width / 2 || dy < -height || dy > 0) {
			return false;
		}
		return true;
	}
	
	private int mouseOn(double dy) {
		if (dy >= 0 * -height / 3 && dy <= 1 * -height / 3) return 1;
		if (dy > 1 * -height / 3 && dy <= 2 * -height / 3) return 2;
		if (dy > 2 * -height / 3 && dy <= 3 * -height / 3) return 3;
		return 0; // falisafe but not safe; should not happen;
	}
	
	private void replaceNullTower(int s) {
		
		Point2D ownerPosition = owner.getPosition();
		int ownerDirection = owner.getDirection();
		Tower replaceTower = null;
		
		switch (s) {
			case 1 : {
				replaceTower = new ArcheryTower(field, ownerPosition, ownerDirection);
				break;
			}
			case 2 : {
				replaceTower = new WitchTower(field, ownerPosition, ownerDirection);
				break;
			}
			case 3 : {
				replaceTower = new ArtilleryTower(field, ownerPosition, ownerDirection);
				break;
			}
		}
		
		field.removeTower(owner);
		field.addTower(replaceTower);
		
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
