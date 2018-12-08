package application;

import javafx.geometry.Point2D;

public interface MouseInteractable {
	
	boolean hover(Point2D hoverPosition);
	
	boolean click(Point2D pressPosition, Point2D releasePosition);
	
	void unclick(); // must be call when click return false

}
