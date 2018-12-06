package application;

import java.util.ArrayList;

public class Wave {
	
	private Field field;
	
	private ArrayList<Zombie> enemies;
	
	
	public Wave(ArrayList<Zombie> enemies) {
		this.enemies = enemies;
	}
	
	public void call(long now) {
		for (Zombie e : enemies) {
			e.call(now);
			field.addEnemy(e);
		}
	}
	
}
