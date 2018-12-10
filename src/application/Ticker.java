package application;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Ticker extends AnimationTimer {
	
	public enum State {
		TITLE, SELECT, FIELD
	}
	
	private State state = State.TITLE;
	private State lastState = State.SELECT;
	
	private Canvas canvas;
	private MouseListener mouse;
	private Holder current;
	
	public Ticker(Canvas canvas, MouseListener mouse) {
		this.canvas = canvas;
		this.mouse = mouse;
	}

	@Override
	public void handle(long now) {
		
		//System.out.println("tick");
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		boolean isPrimaryClicked = mouse.isPrimaryClicked();
		Point2D primaryPressPosition = null;
		Point2D primaryReleasePosition = null;
		if (isPrimaryClicked) {
			Point2D[] primaryClickInfo = mouse.retrievePrimaryClickInfo();
			primaryPressPosition = primaryClickInfo[0];
			primaryReleasePosition = primaryClickInfo[1];
		}
		
		
		if (state == State.TITLE) {
			if (lastState != state) {
				current = new TitleScreen(this);
				System.out.println("DEBUG : Construct TitleScreen");
			}
		}
		
		else if (state == State.SELECT) {
			if (lastState != state) {
				current = new SelectScreen(this);
				System.out.println("DEBUG : Construct SelectScreen");
			}
		}
		
		else if (state == State.FIELD) {
			
		}
		
		current.hover(mouse.getHoverPosition());
		if (isPrimaryClicked) current.click(primaryPressPosition, primaryReleasePosition);
		current.tick(now, gc);
		
		lastState = state;
		
	}
	
	public void setState(State state) {
		this.state = state;
	}

}
