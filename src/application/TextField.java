package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class TextField implements Renderable {
	
	private static int k = 16;
	
	private static Image font = ImageLoader.asciiWhite;
	
	private String displayText;
	private Point2D position;
	private double scale;
	
	
	public TextField(Point2D position, String displayText, double scale) {
		this.position = position;
		this.displayText = displayText;
		this.scale = scale;
	}
	
	public void setText(String displayText) {
		this.displayText = displayText;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public void setBlack() {
		font = ImageLoader.asciiBlack;
	}

	
	@Override
	public void tick(long now, GraphicsContext gc) {
		int length = displayText.length();
		double size = scale * 8;
		double dx = position.getX();
		double dy = position.getY();
		for (int i = 0; i < length; i++) {
			char c = displayText.charAt(i);
			int sx = c % 16 * 8;
			int sy = c / 16 * 8;
			gc.drawImage(font, sx * k, sy * k, 8 * k, 8 * k, dx, dy, size, size);
			dx += scale * 7;
		}
	}

	
	@Override
	public double getRenderPriority() {
		return 4000;
	}

	@Override
	public int compareTo(Renderable other) {
		return Double.compare(4000, other.getRenderPriority());
	}

}
