package application;

import java.util.ArrayDeque;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class MouseListener implements EventHandler<MouseEvent> {
	
	// store only latest hoverPosition and valid click
	
	private Point2D hoverPosition;

	private Point2D tempPressPosition;
	private Point2D pressPosition;
	private Point2D releasePosition;
	private boolean isPrimaryPressed = false;
	private boolean isClicked = false;

	@Override
	public void handle(MouseEvent event) {
		
		hoverPosition = new Point2D(event.getX(), event.getY());
		
		if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			if (event.isPrimaryButtonDown() && !isPrimaryPressed) {
				tempPressPosition = hoverPosition;
				isPrimaryPressed = true;
			}
		}
		else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			if (!event.isPrimaryButtonDown() && isPrimaryPressed) {
				pressPosition = tempPressPosition;
				releasePosition = hoverPosition;
				isPrimaryPressed = false;
				isClicked = true;
			}
		}
		
	}
	
	public Point2D getHoverPosition() {
		return hoverPosition;
	}
	
	public boolean isClicked() {
		return isClicked;
	}
	public Point2D[] getClickInfo() { // clickInfo reset themself! it can only be retrieved once
		if (isClicked = false) return null;
		Point2D[] clickInfo = {pressPosition, releasePosition};
		isClicked = false;
		return clickInfo;
	}

}
