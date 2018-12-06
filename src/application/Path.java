package application;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class Path {
	
	private ArrayList<Point2D> nodes; // connected point
	private ArrayList<Point2D> edges; // length between connected point
	
	public Path(ArrayList<Point2D> nodes) {
		this.nodes = nodes;
		this.edges = new ArrayList<Point2D>();
		for (int i = 1; i < nodes.size(); i++) {
			Point2D edge = PointOperations.different(nodes.get(i - 1), nodes.get(i));
			edges.add(edge);
			if (i == 1 || i == nodes.size() - 1) {
				// push twice at first and last for 'getCoordinate' consistency
				// if nodes.size() == 6 : 01 01 12 23 34 45 45
				edges.add(edge);
			}
		}
	}
	
	public Point2D getNode(int index) {
		return nodes.get(index);
	}
	
	public Point2D getEdge(int index) {
		return edges.get(index);
	}
	
	public Point2D getCoordinate(int index, double shift) {
		double edge1Angle = PointOperations.getAngle(getEdge(index));
		double edge2Angle = PointOperations.getAngle(getEdge(index + 1));
		double shiftAngle = 90 + (edge1Angle + edge2Angle) / 2;
		Point2D shiftVector = new Point2D(1, 0); // shiftAngle already define base angle 90
		shiftVector = PointOperations.rotate(shiftVector, shiftAngle);
		shiftVector = PointOperations.scale(shiftVector, shift);
		return PointOperations.add(getNode(index), shiftVector);
	}
	
}
