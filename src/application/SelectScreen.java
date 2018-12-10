package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class SelectScreen implements Holder {
	
	private Ticker ticker;

	private RetrievalButton play;
	private RetrievalButton back;
	
	public SelectScreen(Ticker ticker) {
		this.ticker = ticker;
		play = new RetrievalButton(new Point2D(640, 360), null, null, null, 500, 100);
		back = new RetrievalButton(new Point2D(640, 360), null, null, null, 500, 100);
	}

	@Override
	public void tick(long now, GraphicsContext gc) {

		if (play.retrieveClickStatus()) {
			ticker.setState(Ticker.State.FIELD);
		}
		if (back.retrieveClickStatus()) {
			ticker.setState(Ticker.State.TITLE);
		}
		
	}

	@Override
	public boolean hover(Point2D hoverPosition) {
		play.hover(hoverPosition);
		back.hover(hoverPosition);
		return true;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		play.click(pressPosition, releasePosition);
		back.click(pressPosition, releasePosition);
		return true;
	}

	@Override
	public void unclick() {
		
	}

}
