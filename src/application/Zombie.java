package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Zombie extends Canvas implements Comparable<Zombie> {
	
	/*** Field ***/
	
	private static Image[] stand;
	
	private static Image[][] walkingAnimation;
	private static int walkingAnimationLength = 1;
	
	private static Blend hurt; // just hypothesis
	
	static {
		
		walkingAnimation = new Image[8][walkingAnimationLength];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < walkingAnimationLength; j++) {
				walkingAnimation[i][j] = new Image("elfmaker.png", 5, 5, true, true);
			}
		}
		
		double x = 0, y = 0, w = 0, h = 0;
		hurt = new Blend(BlendMode.DARKEN, null, new ColorInput(x, y, w, h, new Color(1/2d, 1/8d, 1/8d, 1/1d)));
		
	}
	
	private static double width;
	private static double height;
	
	private Field field;

	private Point2D position;
	private int direction;
	
	private Path path;
	private int pathIndex = 0;
	private double pathShift;
	
	private double maxHealth = 20;
	private double health = maxHealth;
	
	private double speed = 50;
	private Point2D subpathMomentum;
	private int subpathTickRemaining;
	private boolean isSubpathCompleted;
	
	private int walkingFrameHold = 20;
	private int walkingFrameRemaining;
	private int walkingFrameImageIndex;
	
	private long callingTime;
	private long spawnTime;
	private boolean isSpawned = false;
	
	private boolean isInvaded = false;
	
	private long last;
	
	
	/*** Constructor ***/
	
	public Zombie(Field field, Path path, double pathShift, long spawnTime) {
		this.field = field;
		
		this.path = path;
		this.pathIndex = 0;
		this.pathShift = pathShift;
		
		this.health = 20;
		
		this.spawnTime = spawnTime;
		this.isSpawned = false;
		
		this.isInvaded = false;
	}
	
	public Point2D getPosition() {
		return new Point2D(position.getX(), position.getY());
	}
	
	public Point2D getMomentum() {
		return new Point2D(subpathMomentum.getX(), subpathMomentum.getY());
	}
	
	
	/*** Tick ***/
	
	public void tick(long now, GraphicsContext gc) {
		
		if (isInvaded) return; // failsafe should not be call
		
		if (!isSpawned) {
			if (now < callingTime + spawnTime) return;
			else spawn();
		}
		
		if (isSubpathCompleted) {
			if (pathIndex >= path.size()) {
				invading();
				return;
			}
			else {
				updateDestination();
				updateDirection();
				isSubpathCompleted = false;
			}
		}
		
		updatePosition();
		
		commenceWalkingingSequence();
		
		draw(gc);
		
		last = now; // last line
		
	}
	
	private void spawn() {
		position = path.getCoordinate(0, pathShift);
		pathIndex = 0; // will be changed to 1 at 'updateDestination'
		walkingFrameRemaining = walkingFrameHold;
		walkingFrameImageIndex = 0;
		isSpawned = true;
		isSubpathCompleted = true;
	}
	
	private void updateDestination() {
		pathIndex++;
		Point2D destination = path.getCoordinate(pathIndex, pathShift);
		Point2D subpath = PointOperations.different(position, destination);
		double length = PointOperations.getSize(subpath);
		subpathTickRemaining = (int) (60 * length / speed);
		double distantPerFrame = length / subpathTickRemaining;
		subpathMomentum = PointOperations.normalize(subpath);
		subpathMomentum = PointOperations.scale(subpathMomentum, distantPerFrame);
	}
	
	private void updateDirection() {
		double angle = PointOperations.getAngle(subpathMomentum);
		if (angle < 0) angle += 180;
		if (angle >= 360 - 22.5 || angle < 0 + 22.5) direction = 0;
		if (angle >= 45 - 22.5 && angle < 45 + 22.5) direction = 1;
		if (angle >= 90 - 22.5 && angle < 90 + 22.5) direction = 2;
		if (angle >= 135 - 22.5 && angle < 135 + 22.5) direction = 3;
		if (angle >= 180 - 22.5 && angle < 180 + 22.5) direction = 4;
		if (angle >= 225 - 22.5 && angle < 225 + 22.5) direction = 5;
		if (angle >= 270 - 22.5 && angle < 270 + 22.5) direction = 6;
		if (angle >= 315 - 22.5 && angle < 315 + 22.5) direction = 7;
	}
	
	private void updatePosition() {
		position = PointOperations.add(position, subpathMomentum);
		subpathTickRemaining--;
		if (subpathTickRemaining <= 0) {
			isSubpathCompleted = true;
		}
	}
	
	private void commenceWalkingingSequence() {
		if (walkingFrameRemaining > 0) {
			walkingFrameRemaining--;
		}
		else {
			walkingFrameImageIndex = (walkingFrameImageIndex + 1) % walkingAnimationLength;
			walkingFrameRemaining = walkingFrameHold;
		}
	}
	
	private void invading() {
		field.invaded(this);
		field.removeEnemy(this);
		isInvaded = true;
	}
	
	
	private void draw(GraphicsContext gc) {
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		gc.drawImage(walkingAnimation[direction][walkingFrameImageIndex], drawX, drawY);
	}
	
	
	/*** Utility ***/
	
	public int compareTo(Zombie another) {
		return Long.compare(spawnTime, another.spawnTime);
	}
	
	public void call(long callingTime) {
		this.callingTime = callingTime;
	}
	
	public boolean isSpawned() {
		return isSpawned;
	}

}
