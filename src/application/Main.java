package application;
	
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Main extends Application {
	
	private Pane pane = new Pane();;
	
	private Canvas canvas = new Canvas(1280, 720);
	
	@Override
	public void start(Stage primaryStage) {
		
		MouseEventListener mouse = new MouseEventListener();
		
		canvas.setOnMouseMoved(mouse);
		canvas.setOnMousePressed(mouse);
		canvas.setOnMouseReleased(mouse);
		
		pane.getChildren().add(canvas);
		
		AnimationTimer ticker = new Ticker(canvas);
		ticker.start();
		
		Scene scene = new Scene(pane, 1280, 720);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
