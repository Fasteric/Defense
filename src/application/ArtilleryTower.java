package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ArtilleryTower extends Tower {
	
	private static Image[][] tower;
	
	private static double width = 70;
	private static double height = 90;
	
	private static Image saleDisable;
	private static Image saleIdle;
	private static Image saleHover;
	
	static {
		tower = ImageLoader.artilleryTower;
		saleDisable = ImageLoader.saleDisable;
		saleIdle = ImageLoader.saleIdle;
		saleHover = ImageLoader.saleHover;
	}
	
	public static final int COST = 150;
	
	private static int prefiringDelay = 120;
	private static int postfiringDelay = 120;

	private static double radius = 150;
	private static double radiusX = Math.sqrt(1.5) * radius;
	private static double radiusY = Math.sqrt(0.5) * radius;
	
	private static double projectileShift = 0;
	
	private boolean isHover = false;
	private RunnableButton saleButton;
	

	public ArtilleryTower(Field field, Point2D position, int direction) {
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
		System.out.println("DEBUG : Artillery.fire from to : " + position + " -> " + projectileDestination);
		Point2D projectileInitial = new Point2D(position.getX(), position.getY() - projectileShift);
		PrimedTnt primedTnt = new PrimedTnt(field, projectileInitial, projectileDestination);
		field.addProjectile(primedTnt);
	}
	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY();
		
		if (isSearching) {
			gc.drawImage(tower[direction][0], drawX, drawY, width, height);
		}
		
		if (isPrefiring || isPostfiring) {
			if (lifeCycle >= 0 && lifeCycle < 60) {
				gc.drawImage(tower[direction][1], drawX, drawY,width, height);
			}
			else {
				gc.drawImage(tower[direction][0], drawX, drawY, width, height);
			}
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
