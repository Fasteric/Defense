package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		Pane pane = new Pane();
		Canvas canvas = new Canvas(1280, 720);
		MouseListener mouse = new MouseListener();
		
		canvas.setOnMouseMoved(mouse);
		canvas.setOnMousePressed(mouse);
		canvas.setOnMouseReleased(mouse);
		
		pane.getChildren().add(canvas);
		
		Scene scene = new Scene(pane, 1280, 720);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		AnimationTimer ticker = new Ticker(canvas, mouse);
		ticker.start();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
