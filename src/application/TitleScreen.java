package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class TitleScreen implements Holder {
	
	private Ticker ticker;
	
	private RetrievalButton select;
	private RetrievalButton instruction;
	private RetrievalButton credit;
	
	private boolean isCredit;
	private boolean isInstruction;
	
	public TitleScreen (Ticker ticker) {
		this.ticker = ticker;
		select = new RetrievalButton(new Point2D(640, 300), null, null, null, 500, 100);
		instruction = new RetrievalButton(new Point2D(580, 450), null, null, null, 220, 100);
		credit = new RetrievalButton(new Point2D(700, 450), null, null, null, 220, 100);
	}

	@Override
	public void tick(long now, GraphicsContext gc) {
		select.tick(now, gc);
		instruction.tick(now, gc);
		credit.tick(now, gc);
		isInstruction = instruction.retrieveClickStatus();
		isCredit = credit.retrieveClickStatus();
		if (isInstruction) {
			//gc.drawImage(instruction);
		}
		else if (isCredit) {
			//gc.drawImage(credit);
		}
		else if (select.retrieveClickStatus()) {
			ticker.setState(Ticker.State.SELECT);
		}
	}

	@Override
	public boolean hover(Point2D hoverPosition) {
		select.hover(hoverPosition);
		instruction.hover(hoverPosition);
		credit.hover(hoverPosition);
		return true;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		if (isInstruction || isCredit) {
			isInstruction = false;
			isCredit = false;
		}
		else {
			select.click(pressPosition, releasePosition);
			instruction.click(pressPosition, releasePosition);
			credit.click(pressPosition, releasePosition);
		}
		return true;
	}

	@Override
	public void unclick() {
		
	}

}
