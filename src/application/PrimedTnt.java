package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PrimedTnt extends Projectile {
	
	private static Image lit;
	private static Image unlit;
	private static Image[] explosion = new Image[24];
	
	private static double width = 19;
	private static double height = 22;
	
	private static double particleSize = 100;
	
	static {
		lit = new Image("res/explosion/lit.png", width, height, true, false);
		unlit = new Image("res/explosion/unlit.png", width, height, true, false);
		for (int i = 0; i < 24; i++) {
			explosion[i] = new Image("res/explosion/explosion (" + (i + 1) + ").png", particleSize, particleSize, true, false);
		}
	}
	
	private static double verticalTrajectory = 125;
	private static int maxLifeTime = 60;
	
	public PrimedTnt(Field field, Point2D position, Point2D destination) {
		super(field, position, destination, verticalTrajectory, maxLifeTime);
	}
	

	@Override
	protected void hit() {
		field.pushDamage(new Damage(Damage.EXPLOSION, field, position));
		StaticParticle sp = new StaticParticle(field, explosion, position, particleSize, particleSize, 2);
		field.addRender(sp);
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
		return position.getY() + 10 * (maxLifeTime - lifeTime);
	}

	@Override
	public int compareTo(Renderable other) {
		return Double.compare(getRenderPriority(), other.getRenderPriority());
	}

}
