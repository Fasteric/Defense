package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ArcheryTower extends Tower {
	
	private static Image[][] tower;
	
	private static double width = 70;
	private static double height = 90;
	
	private static Image saleDisable;
	private static Image saleIdle;
	private static Image saleHover;
	
	static {
		tower = ImageLoader.archeryTower;
		saleDisable = ImageLoader.saleDisable;
		saleIdle = ImageLoader.saleIdle;
		saleHover = ImageLoader.saleHover;
	}
	
	public static final int COST = 90;
	
	private static int prefiringDelay = 15;
	private static int postfiringDelay = 30;

	private static double radius = 200;
	private static double radiusX = Math.sqrt(1.5) * radius;
	private static double radiusY = Math.sqrt(0.5) * radius;
	
	private static double projectileShift = 0;
	
	private boolean isHover = false;
	private RunnableButton saleButton;
	

	public ArcheryTower(Field field, Point2D position, int direction) {
		super(field, position, direction, prefiringDelay, postfiringDelay, radiusX, radiusY);
		saleButton = new RunnableButton(new Point2D(position.getX(), position.getY() + 48), saleDisable, saleIdle, saleHover, 48, 48);
		saleButton.setOnClicked(() -> {
			NullTower tower = new NullTower(field, position, direction);
			field.addMoney(COST * 3 / 4);
			field.removeTower(this);
			field.addTower(tower);
			field.removeRender(saleButton);
		});
		saleButton.setOnUnclicked(() -> field.removeRender(saleButton));
	}

	
	@Override
	protected void fire() {
		Point2D projectileInitial = new Point2D(position.getX(), position.getY() - projectileShift);
		Arrow arrow = new Arrow(field, projectileInitial, projectileDestination);
		field.addProjectile(arrow);
	}
	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY();
		
		if (isSearching) {
			gc.drawImage(tower[direction][3], drawX, drawY, width, height);
			return;
		}
		
		if (isPrefiring) {
			if (lifeCycle >= -15 && lifeCycle < -10) {
				gc.drawImage(tower[direction][0], drawX, drawY, width, height);
			}
			if (lifeCycle >= -10 && lifeCycle < -5) {
				gc.drawImage(tower[direction][1], drawX, drawY, width, height);
			}
			if (lifeCycle >= -5 && lifeCycle < 0) {
				gc.drawImage(tower[direction][2], drawX, drawY, width, height);
			}
			return;
		}
		
		if (isPostfiring) {
			gc.drawImage(tower[direction][3], drawX, drawY, width, height);
		}
		
	}


	@Override
	public boolean hover(Point2D hoverPosition) {
		isHover = isMouseInRange(hoverPosition);
		return isHover;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		if (!isMouseInRange(pressPosition) || !isMouseInRange(releasePosition)) return false;
		field.addRender(saleButton);
		return true;
	}
	
	@Override
	public void unclick() {
		
	}
	
	private boolean isMouseInRange(Point2D mousePosition) {
		double dx = mousePosition.getX() - position.getX();
		double dy = mousePosition.getY() - position.getY();
		return dx >= -width / 2 && dx <= width / 2 && dy >= 0 && dy <= height; // special detection for NullTower
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
