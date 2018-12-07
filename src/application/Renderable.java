package application;

import javafx.scene.canvas.GraphicsContext;

public interface Renderable extends Comparable<Renderable> {
	
	void tick(long now, GraphicsContext gc);
	
	double getRenderPriority();

}
