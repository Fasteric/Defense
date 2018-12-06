
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Test extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		Pane pane = new Pane();
		Canvas c = new Canvas(1280, 720);
		GraphicsContext gc = c.getGraphicsContext2D();
		gc.setFill(new Color(1, 0, 0, 1));
		gc.fillRect(10, 10, 100, 100);
		gc = c.getGraphicsContext2D();
		gc.setFill(new Color(0, 1, 0, 1));
		gc.fillRect(110, 110, 100, 100);
		pane.getChildren().add(c);
		
		Scene scene = new Scene(pane);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
