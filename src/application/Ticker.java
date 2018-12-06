package application;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Ticker extends AnimationTimer {
	
	private Canvas canvas;
	
	public Ticker(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void handle(long now) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
	}

}
