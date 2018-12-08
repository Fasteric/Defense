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
	
	private static double verticalTrajectory = 125;
	private static int maxLifeTime = 60;
	
	public PrimedTnt(Field field, Point2D position, Point2D destination) {
		super(field, position, destination, verticalTrajectory, maxLifeTime);
	}
	

	@Override
	protected void hit() {
		field.pushDamage(new Damage(Damage.EXPLOSION, field, position));
	}
	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height / 2;
		
		if (lifeTime / 10 % 2 == 0) gc.drawImage(unlit, drawX, drawY);
		else gc.drawImage(lit, drawX, drawY);
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
