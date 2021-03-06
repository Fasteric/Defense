package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ThrownPotion extends Projectile {
	
	private static Image potion;
	private static Image splash[];

	private static double width = 9;
	private static double height = 13;
	private static double particleSize = 100;
	
	static {
		potion = ImageLoader.potion;
		splash = ImageLoader.splash;
	}
	
	private static double verticalTrajectory = 20;
	private static int maxLifeTime = 45;
	
	public ThrownPotion(Field field, Point2D position, Point2D destination) {
		super(field, position, destination, verticalTrajectory, maxLifeTime);
	}
	

	@Override
	protected void hit() {
		field.pushDamage(new Damage(Damage.POTION, field, position));
		StaticParticle sp = new StaticParticle(field, splash, position, particleSize, particleSize, 2);
		field.addRender(sp);
	}
	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height / 2;
		
		gc.drawImage(potion, drawX, drawY, width, height);
	}

	
	@Override
	public double getRenderPriority() {
		return position.getY() + 10 * (maxLifeTime - lifeTime);
	}

	@Override
	public int compareTo(Renderable other) {
		return Double.compare(getRenderPriority(), other.getRenderPriority());
	}

}
