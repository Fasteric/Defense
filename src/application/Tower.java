package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Tower {
	
	protected Field field;
	protected Point2D position;
	protected int direction;
	
	protected int lifeCycle;
	protected int prefiringDuration;
	protected int postfiringDuration;
	
	protected boolean isSearching = false;
	protected boolean isPrefiring = false;
	protected boolean isPostfiring = true;
	
	
	/*** Constructor ***/
	
	public Tower(Field field, Point2D position, int direction,
			int prefiringDuration, int postfiringDuration) {
		this.field = field;
		this.position = position;
		this.direction = direction;
		this.prefiringDuration = prefiringDuration;
		this.postfiringDuration = postfiringDuration;
		
		lifeCycle = 1;
	}
	
	public void tick(long now, GraphicsContext gc) {
		logicUpdate(now);
		graphicUpdate(gc);
	}
	
	protected void logicUpdate(long now) {
		
		if (isSearching) {
			search();
			return;
		}
		
		lifeCycle++;
		
	}
	
	protected void search() {
		//if (found) isSearching = false;
	}
	
	protected abstract void graphicUpdate(GraphicsContext gc);
	
	protected abstract void fire();
	
	
	/*** ***/
	
	public abstract boolean hover(Point2D hoverPosition);
	
	public abstract boolean click(Point2D pressPosition, Point2D releasePosition);

}
