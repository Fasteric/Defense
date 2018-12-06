package application;

import javafx.geometry.Point2D;

public class PointOperations {
	
	/*** Point Operations ***/
	
	public static Point2D add(Point2D p1, Point2D p2) {
		return new Point2D(p1.getX() + p2.getX(), p1.getY() + p2.getY());
	}
	
	public static Point2D subtract(Point2D p1, Point2D p2) {
		return new Point2D(p1.getX() - p2.getX(), p1.getY() - p2.getY());
	}
	
	
	/*** Vector Operations ***/
	
	public static Point2D different(Point2D p1, Point2D p2) {
		return new Point2D(p2.getX() - p1.getX(), p2.getY() - p1.getY());
	}
	
	public static Point2D normalize(Point2D p) {
		double x = p.getX();
		double y = p.getY();
		double size = Math.sqrt(x * x + y * y);
		return new Point2D(x / size, y / size);
	}
	
	public static Point2D scale(Point2D p, double scale) {
		return new Point2D(p.getX() * scale, p.getY() * scale);
	}
	
	public static Point2D rotate(Point2D p, double angle) {
		double radians = Math.toRadians(angle);
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		double x = p.getX();
		double y = p.getY();
		double rotatedX = +cos * x + -sin * y;
		double rotatedY = +sin * x + +cos * y;
		return new Point2D(rotatedX, rotatedY);
	}
	
	public static double getSize(Point2D p) {
		double x = p.getX();
		double y = p.getY();
		return Math.sqrt(x * x + y * y);
	}
	
	public static double getAngle(Point2D p) {
		double radians = Math.atan2(p.getY(), p.getX());
		return Math.toDegrees(radians);
	}

}
