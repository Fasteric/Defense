package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NullTower extends Tower {
	
	private static Image idle;
	private static Image hover;
	
	private static double width = 70;
	private static double height = 90;
	
	static {
		idle = new Image("res/tower/null.png", width, height, true, false);
		hover = idle;
	}
	
	private static int prefiringDelay = 2147483647;
	private static int postfiringDelay = 2147483647;
	
	private static double radiusX = 0;
	private static double radiusY = 0;
	
	private boolean isHovered;
	private UpgradeTooltip tooltip;
	
	
	public NullTower(Field field, Point2D position, int direction) {
		super(field, position, direction, prefiringDelay, postfiringDelay, radiusX, radiusY);
		tooltip = new UpgradeTooltip(field, this, position);
	}

	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height / 2;
		if (!isHovered) gc.drawImage(idle, drawX, drawY);
		else gc.drawImage(hover, drawX, drawY);
	}

	@Override
	protected void fire() {
		// nulltower won't fire
	}


	@Override
	public boolean hover(Point2D hoverPosition) {
		isHovered = isMouseInRange(hoverPosition);
		return isHovered;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		if (!isMouseInRange(pressPosition) || !isMouseInRange(releasePosition)) {
			return false;
		}
		field.addRender(tooltip); // tooltip will remove itself
		return true;
	}
	
	@Override
	public void unclick() {
		 // tooltip will remove itself
	}
	
	private boolean isMouseInRange(Point2D mousePosition) {
		double dx = mousePosition.getX() - position.getX();
		double dy = mousePosition.getY() - position.getY();
		return dx >= -width / 2 && dx <= width / 2 && dy >= -height && dy <= 0;
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
