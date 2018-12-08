package application;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class MouseListener implements EventHandler<MouseEvent> {
	
	// store only latest hoverPosition and valid click
	
	private Point2D hoverPosition;

	private Point2D tempPressPosition;
	private Point2D pressPosition;
	private Point2D releasePosition;
	private boolean isPrimaryPressed = false;
	private boolean isPrimaryClicked = false;
	
	private boolean isSecondaryPressed = false;
	private boolean isSecondaryClicked = false;

	@Override
	public void handle(MouseEvent event) {
		
		hoverPosition = new Point2D(event.getX(), event.getY());
		
		if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			if (event.isPrimaryButtonDown() && !isPrimaryPressed) {
				tempPressPosition = hoverPosition;
				isPrimaryPressed = true;
			}
			if (event.isSecondaryButtonDown()) {
				isSecondaryPressed = true;
			}
		}
		else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			if (!event.isPrimaryButtonDown() && isPrimaryPressed) {
				pressPosition = tempPressPosition;
				releasePosition = hoverPosition;
				isPrimaryPressed = false;
				isPrimaryClicked = true;
				//System.out.println("DEBUG : primaryClicked");
			}
			if (!event.isSecondaryButtonDown() && isSecondaryPressed) {
				isSecondaryPressed = false;
				isSecondaryClicked = true;
				//System.out.println("DEBUG : secondaryClicked");
			}
		}
		
	}
	
	public Point2D getHoverPosition() {
		return hoverPosition;
	}
	
	public boolean isPrimaryClicked() {
		return isPrimaryClicked;
	}
	public Point2D[] retrievePrimaryClickInfo() { // clickInfo reset themself! it can only be retrieved once
		if (isPrimaryClicked = false) return null;
		Point2D[] clickInfo = {pressPosition, releasePosition};
		isPrimaryClicked = false;
		return clickInfo;
	}
	
	public boolean retrieveSecondaryClickInfo() {
		boolean temp = isSecondaryClicked;
		isSecondaryClicked = false;
		return temp;
	}

}
