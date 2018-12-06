package application;

import java.util.ArrayList;

public class Wave {
	
	private ArrayList<Zombie> enemies;
	
	
	public Wave(ArrayList<Zombie> enemies) {
		this.enemies = enemies;
	}
	
	public void addEnemy(Zombie enemy) {
		enemies.add(enemy);
	}
	
	public void call(long now, Field field) {
		for (Zombie e : enemies) {
			e.call(now);
			field.addEnemy(e);
		}
	}
	
}
