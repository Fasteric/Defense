package application;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Ticker extends AnimationTimer {
	
	private Canvas canvas;
	private MouseListener mouse;
	
	public Ticker(Canvas canvas, MouseListener mouse) {
		this.canvas = canvas;
		this.mouse = mouse;
	}

	@Override
	public void handle(long now) {
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
	}

}
