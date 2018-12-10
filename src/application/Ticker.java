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
	boolean stateChange = true;
	
	private Canvas canvas;
	private MouseListener mouse;
	private Holder current;
	
	public Ticker(Canvas canvas, MouseListener mouse) {
		ImageLoader.invoke();
		this.canvas = canvas;
		this.mouse = mouse;
	}

	@Override
	public void handle(long now) {
		
		//System.out.println(state);
		
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
			if (stateChange) {
				current = new TitleScreen(this);
				System.out.println("DEBUG : Construct TitleScreen");
				stateChange = false;
			}
		}
		
		else if (state == State.SELECT) {
			if (stateChange) {
				current = new SelectScreen(this);
				System.out.println("DEBUG : Construct SelectScreen");
				stateChange = false;
			}
		}
		
		else if (state == State.FIELD) {
			if (stateChange) {
				try {
					current = new Field(this, "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stateChange = false;
			}
		}
		
		current.hover(mouse.getHoverPosition());
		if (isPrimaryClicked) current.click(primaryPressPosition, primaryReleasePosition);
		current.tick(now, gc);
		
	}
	
	public void setState(State state) {
		System.out.println("DEBUG : set state");
		this.state = state;
		stateChange = true;
	}

}
