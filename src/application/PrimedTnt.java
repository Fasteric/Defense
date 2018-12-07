package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PrimedTnt extends Projectile {
	
	private static Image lit;
	private static Image unlit;
	
	static {
		// load image;
	}
	
	private static double width;
	private static double height;
	
	public PrimedTnt(Field field, Point2D position, Point2D destination) {
		super(field, position, destination, 100, 45);
	}

	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		if (lifeTime / 10 % 2 == 0) gc.drawImage(unlit, position.getX(), position.getY());
		else gc.drawImage(lit, position.getX(), position.getY());
	}

	@Override
	protected void hit() {
		field.pushDamage(new Damage(Damage.EXPLOSION, field, position));
	}

}
