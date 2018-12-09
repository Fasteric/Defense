package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NullTower extends Tower {
	
	private static Image idle;
	private static Image hover;
	
	private static Image archeryIdle;
	private static Image archeryHover;
	private static Image witchIdle;
	private static Image witchHover;
	private static Image artilleryIdle;
	private static Image artilleryHover;
	
	private static double width = 70;
	private static double height = 90;
	
	static {
		idle = new Image("res/tower/null0.png", width, height, true, false);
		hover = new Image("res/tower/null1.png", width, height, true, false);
		archeryIdle = new Image("res/gui/upgrade_archery_idle.png");
		archeryHover = new Image("res/gui/upgrade_archery_hover.png");
		witchIdle = new Image("res/gui/upgrade_witch_idle.png");
		witchHover = new Image("res/gui/upgrade_witch_hover.png");
		artilleryIdle = new Image("res/gui/upgrade_artillery_idle.png");
		artilleryHover = new Image("res/gui/upgrade_artillery_hover.png");
	}
	
	private static int prefiringDelay = 2147483647;
	private static int postfiringDelay = 2147483647;
	
	private static double radiusX = 0;
	private static double radiusY = 0;
	
	private boolean isHover;
	
	private RunnableButton archeryButton;
	private RunnableButton witchButton;
	private RunnableButton artilleryButton;
	
	
	public NullTower(Field field, Point2D position, int direction) {
		super(field, position, direction, prefiringDelay, postfiringDelay, radiusX, radiusY);
		
		double x = position.getX();
		double y = position.getY();
		
		archeryButton = new RunnableButton(new Point2D(x - 48, y + 24), null, archeryIdle, archeryHover, 48, 48);
		archeryButton.setOnClicked(() -> {
			ArcheryTower tower = new ArcheryTower(field, position, direction);
			field.addTower(tower);
			field.removeTower(this);
			field.removeRender(archeryButton);
		});
		archeryButton.setOnUnclicked(() -> field.removeRender(archeryButton));
		
		witchButton = new RunnableButton(new Point2D(x, y - 24), null, witchIdle, witchHover, 48, 48);
		witchButton.setOnClicked(() -> {
			WitchTower tower = new WitchTower(field, position, direction);
			field.addTower(tower);
			field.removeTower(this);
			field.removeRender(witchButton);
		});
		witchButton.setOnUnclicked(() -> field.removeRender(witchButton));
		
		artilleryButton = new RunnableButton(new Point2D(x + 48, y + 24), null, artilleryIdle, artilleryHover, 48, 48);
		artilleryButton.setOnClicked(() -> {
			ArtilleryTower tower = new ArtilleryTower(field, position, direction);
			field.addTower(tower);
			field.removeTower(this);
			field.removeRender(artilleryButton);
		});
		artilleryButton.setOnUnclicked(() -> field.removeRender(artilleryButton));
	}

	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		// anchored at top center
		double drawX = position.getX() - width / 2;
		double drawY = position.getY();
		if (!isHover) gc.drawImage(idle, drawX, drawY);
		else gc.drawImage(hover, drawX, drawY);
	}

	@Override
	protected void fire() {
		// nulltower won't fire
	}


	@Override
	public boolean hover(Point2D hoverPosition) {
		isHover = isMouseInRange(hoverPosition);
		return isHover;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		if (!isMouseInRange(pressPosition) || !isMouseInRange(releasePosition)) {
			return false;
		}
		field.addRender(archeryButton);
		field.addRender(witchButton);
		field.addRender(artilleryButton);
		return true;
	}
	
	@Override
	public void unclick() {
		 // tooltip will remove itself
	}
	
	private boolean isMouseInRange(Point2D mousePosition) {
		double dx = mousePosition.getX() - position.getX();
		double dy = mousePosition.getY() - position.getY();
		return dx >= -width / 2 && dx <= width / 2 && dy >= height / 2 && dy <= height; // special detection for NullTower
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
