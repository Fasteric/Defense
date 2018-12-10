package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NullTower extends Tower {
	
	private static Image idle;
	private static Image hover;
	
	private static Image archeryDisable;
	private static Image archeryIdle;
	private static Image archeryHover;
	private static Image witchDisable;
	private static Image witchIdle;
	private static Image witchHover;
	private static Image artilleryDisable;
	private static Image artilleryIdle;
	private static Image artilleryHover;
	
	private static double width = 70;
	private static double height = 90;
	
	static {
		idle = ImageLoader.nullTower[0];
		hover = ImageLoader.nullTower[1];
		archeryDisable = ImageLoader.upgradeArcheryDisable;
		archeryIdle = ImageLoader.upgradeArcheryIdle;
		archeryHover = ImageLoader.upgradeArcheryHover;
		witchDisable = ImageLoader.upgradeWitchDisable;
		witchIdle = ImageLoader.upgradeWitchIdle;
		witchHover = ImageLoader.upgradeWitchHover;
		artilleryDisable = ImageLoader.upgradeArtilleryDisable;
		artilleryIdle = ImageLoader.upgradeArtilleryIdle;
		artilleryHover = ImageLoader.upgradeArtilleryHover;
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
		
		archeryButton = new RunnableButton(new Point2D(x - 48, y + 24), archeryDisable, archeryIdle, archeryHover, 48, 48);
		archeryButton.setOnClicked(() -> {
			ArcheryTower tower = new ArcheryTower(field, position, direction);
			field.addTower(tower);
			field.removeTower(this);
			field.removeMoney(ArcheryTower.COST);
			field.removeRender(archeryButton);
		});
		archeryButton.setOnUnclicked(() -> field.removeRender(archeryButton));
		archeryButton.setOnPreTick((now, gc) -> {
			if (field.getMoney() < ArcheryTower.COST) archeryButton.disable();
			else archeryButton.enable();
		});
		
		witchButton = new RunnableButton(new Point2D(x, y - 24), witchDisable, witchIdle, witchHover, 48, 48);
		witchButton.setOnClicked(() -> {
			WitchTower tower = new WitchTower(field, position, direction);
			field.addTower(tower);
			field.removeTower(this);
			field.removeMoney(WitchTower.COST);
			field.removeRender(witchButton);
		});
		witchButton.setOnUnclicked(() -> field.removeRender(witchButton));
		witchButton.setOnPreTick((now, gc) -> {
			if (field.getMoney() < WitchTower.COST) witchButton.disable();
			else witchButton.enable();
		});
		
		artilleryButton = new RunnableButton(new Point2D(x + 48, y + 24), artilleryDisable, artilleryIdle, artilleryHover, 48, 48);
		artilleryButton.setOnClicked(() -> {
			ArtilleryTower tower = new ArtilleryTower(field, position, direction);
			field.addTower(tower);
			field.removeTower(this);
			field.removeMoney(ArtilleryTower.COST);
			field.removeRender(artilleryButton);
		});
		artilleryButton.setOnUnclicked(() -> field.removeRender(artilleryButton));
		artilleryButton.setOnPreTick((now, gc) -> {
			if (field.getMoney() < ArtilleryTower.COST) artilleryButton.disable();
			else artilleryButton.enable();
		});
	}

	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		// anchored at top center
		double drawX = position.getX() - width / 2;
		double drawY = position.getY();
		if (!isHover) gc.drawImage(idle, drawX, drawY, width, height);
		else gc.drawImage(hover, drawX, drawY, width, height);
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
