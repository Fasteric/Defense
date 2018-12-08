package application;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Ticker extends AnimationTimer {
	
	private enum State {
		MENU, LOADFIELD, FIELD
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
		Point2D pressPosition = null;
		Point2D releasePosition = null;
		if (isPrimaryClicked) {
			Point2D[] primaryClickInfo = mouse.retrievePrimaryClickInfo();
			pressPosition = primaryClickInfo[0];
			releasePosition = primaryClickInfo[1];
		}
		boolean isSecondaryClicked = mouse.retrieveSecondaryClickInfo();
		
		
		if (state == State.MENU) {
			if (isPrimaryClicked) { // trigger load field
				System.out.println("Load Field Triggered");
				try {
					currentField = new Field("");
					state = State.FIELD;
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		else if (state == State.FIELD) {
			currentField.setHover(mouse.getHoverPosition());
			if (isPrimaryClicked) currentField.setPrimaryClick(pressPosition, releasePosition);
			if (isSecondaryClicked) currentField.setSecondaryClick();
			currentField.tick(now, gc);
		}
		
	}

}
