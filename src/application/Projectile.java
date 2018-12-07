package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Projectile {
	
	protected Field field;
	protected Point2D position;
	protected Point2D destination;
	protected double verticalTrajectory;
	protected int maxLifeTime;

	protected Point2D horizontalPosition;
	protected Point2D horizontalMomentum;
	
	protected int lifeTime = 0;
	
	
	public Projectile(Field field, Point2D position, Point2D destination, 
			double verticalTrajectory, int maxLifeTime) {
		this.field = field;
		this.position = position;
		this.destination = destination;
		this.verticalTrajectory = verticalTrajectory;
		this.maxLifeTime = maxLifeTime;
		
		horizontalPosition = position;
		Point2D different = PointOperations.different(position, destination);
		double distant = PointOperations.getSize(different);
		horizontalMomentum = PointOperations.scale(different, distant / maxLifeTime);
	}
	
	
	public void tick(long now, GraphicsContext gc) {
		logicUpdate(now);
		graphicUpdate(gc);
		lifeTime++;
	}
	
	protected void logicUpdate(long now) {
		horizontalPosition = PointOperations.add(horizontalPosition, horizontalMomentum);
		position = new Point2D(horizontalPosition.getX(), 
				horizontalPosition.getY() - calculateVerticalTrajectory()); // minus y is actually up
		if (lifeTime == 0) {
			hit();
			field.removeProjectile(this);
		}
	}
	
	protected double calculateVerticalTrajectory() {
		double t = (2 * lifeTime - maxLifeTime) / maxLifeTime;
		return verticalTrajectory * (1 - t * t);
	}
	
	protected abstract void graphicUpdate(GraphicsContext gc);
	
	protected abstract void hit();

}
