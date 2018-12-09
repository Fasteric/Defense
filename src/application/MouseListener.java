package application;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class MouseListener implements EventHandler<MouseEvent> {
	
	// store only latest hoverPosition and valid click
	
	private Point2D hoverPosition;

	private Point2D tempPrimaryPosition;
	private Point2D primaryPressPosition;
	private Point2D primaryReleasePosition;
	private boolean isPrimaryPressed = false;
	private boolean isPrimaryClicked = false;

	private Point2D tempSecondaryPosition;
	private Point2D secondaryPressPosition;
	private Point2D secondaryReleasePosition;
	private boolean isSecondaryPressed = false;
	private boolean isSecondaryClicked = false;

	@Override
	public void handle(MouseEvent event) {
		
		hoverPosition = new Point2D(event.getX(), event.getY());
		
		if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			if (event.isPrimaryButtonDown() && !isPrimaryPressed) {
				tempPrimaryPosition = hoverPosition;
				isPrimaryPressed = true;
			}
			if (event.isSecondaryButtonDown() && !isSecondaryPressed) {
				tempSecondaryPosition = hoverPosition;
				isSecondaryPressed = true;
			}
		}
		else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			if (!event.isPrimaryButtonDown() && isPrimaryPressed) {
				primaryPressPosition = tempPrimaryPosition;
				primaryReleasePosition = hoverPosition;
				isPrimaryPressed = false;
				isPrimaryClicked = true;
				//System.out.println("DEBUG : primaryClicked");
			}
			if (!event.isSecondaryButtonDown() && isSecondaryPressed) {
				secondaryPressPosition = tempSecondaryPosition;
				secondaryReleasePosition = hoverPosition;
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
		Point2D[] clickInfo = {primaryPressPosition, primaryReleasePosition};
		isPrimaryClicked = false;
		return clickInfo;
	}
	
	public boolean isSecondaryClicked() {
		return isSecondaryClicked;
	}
	public Point2D[] retrieveSecondaryClickInfo() {
		if (isSecondaryClicked = false) return null;
		Point2D[] clickInfo = {secondaryPressPosition, secondaryReleasePosition};
		isSecondaryClicked = false;
		return clickInfo;
	}

}
