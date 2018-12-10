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
		select = new RetrievalButton(new Point2D(640, 300), 500, 50);
		instruction = new RetrievalButton(new Point2D(490, 450), 220, 50);
		credit = new RetrievalButton(new Point2D(790, 450), 220, 50);
	}

	@Override
	public void tick(long now, GraphicsContext gc) {
		select.tick(now, gc);
		instruction.tick(now, gc);
		credit.tick(now, gc);
		isInstruction = instruction.retrieveClickStatus();
		isCredit = credit.retrieveClickStatus();
		if (isInstruction) {
			System.out.println("DEBUG : Draw Instruction");
			//gc.drawImage(instruction);
		}
		else if (isCredit) {
			System.out.println("DEBUG : Draw Credt");
			//gc.drawImage(credit);
		}
		else if (select.retrieveClickStatus()) {
			System.out.println("DEBUG : Select");
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
