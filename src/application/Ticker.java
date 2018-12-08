package application;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Ticker extends AnimationTimer {
	
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
		
		currentField.setHover(mouse.getHoverPosition());
		if (mouse.isPrimaryClicked()) {
			Point2D[] clickInfo = mouse.retrievePrimaryClickInfo();
			currentField.setPrimaryClick(clickInfo[0], clickInfo[1]);
		}
		
	}

}
