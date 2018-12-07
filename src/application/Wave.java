package application;

import java.util.ArrayList;
import java.util.Collections;

public class Wave {
	
	private Field field;
	
	private ArrayList<Enemy> enemies;
	
	
	public Wave(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	
	public void call(long now) {
		for (Enemy enemy : enemies) {
			field.addEnemy(enemy);
		}
	}
	
}
