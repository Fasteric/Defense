package application;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Ticker extends AnimationTimer {
	
	private enum State {
		MENU, FIELD
	}
	
	private State state = State.MENU;
	
	private Canvas canvas;
	private MouseListener mouse;
	private Field currentField;
	
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
		
		
		if (state == State.MENU) {
			if (isPrimaryClicked) { // trigger load field
				System.out.println("Load Field Triggered");
				try {
					currentField = new Field("File Path");
					state = State.FIELD;
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					boolean check = e instanceof InvalidStageFormatException;
					e.printStackTrace();
				}
			}
		}
		
		else if (state == State.FIELD) {
			currentField.hover(mouse.getHoverPosition());
			if (isPrimaryClicked) currentField.click(primaryPressPosition, primaryReleasePosition);
			currentField.tick(now, gc);
		}
		
	}

}
