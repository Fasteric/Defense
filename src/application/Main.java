package application;
	
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class Main extends Application {
	
	Pane pane = new Pane();;
	
	Canvas canvas = new Canvas(1280, 720);
	
	@Override
	public void start(Stage primaryStage) {
		
		pane.getChildren().add(canvas);
		
		ArrayList<Point2D> nodes = new ArrayList<>();
		nodes.add(new Point2D(10, 10));
		nodes.add(new Point2D(200, 10));
		nodes.add(new Point2D(300, 70));
		nodes.add(new Point2D(350, 220));
		nodes.add(new Point2D(400, 400));
		nodes.add(new Point2D(550, 500));
		nodes.add(new Point2D(700, 400));
		nodes.add(new Point2D(1000, 100));
		nodes.add(new Point2D(1100, 10));
		nodes.add(new Point2D(1200, 10));
		Path path = new Path(nodes);
		Zombie zombie0 = new Zombie(path, -10, 0);
		Zombie zombie1 = new Zombie(path, 0, 0);
		Zombie zombie2 = new Zombie(path, 10, 0);
		
		AnimationTimer at = new AnimationTimer() {
			@Override
			public void handle(long now) {
				GraphicsContext gc = canvas.getGraphicsContext2D();
				//gc.clearRect(0, 0, 1280, 720);
				zombie0.tick(now, gc);
				zombie1.tick(now, gc);
				zombie2.tick(now, gc);
			}
		};
		at.start();
		
		Scene scene = new Scene(pane, 1280, 720);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
