package application;

public interface Renderable extends Tickable, Comparable<Renderable> {
	
	double getRenderPriority();
	
	int compareTo(Renderable other);

}
