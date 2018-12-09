package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Lightning implements Renderable {
	
	// Unused
	
	private static Image lightning[];
	private static int lightningLength = 6;
	
	static {
		// load image
	}
	
	private Field field;
	private Point2D position;
	
	private static int maxLifeTime = 120;
	private int lifeTime = 0;
	
	public Lightning(Field field, Point2D position) {
		this.field = field;
		this.position = position;
	}
	
	public void tick(long now, GraphicsContext gc) {
		
		if (lifeTime % 20 == 0) {
			field.pushDamage(new Damage(Damage.LIGHTNING, field, position));
		}
		
		gc.drawImage(lightning[lifeTime / 20], position.getX(), position.getY());
		
		if (lifeTime == maxLifeTime) {
			field.removeRender(this);
		}
		
		lifeTime++;
		
	}

	@Override
	public double getRenderPriority() {
		return position.getY() + 100;
	}

	@Override
	public int compareTo(Renderable other) {
		return Double.compare(getRenderPriority(), other.getRenderPriority());
	}

}
