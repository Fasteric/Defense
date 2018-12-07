package application;

import java.util.ArrayList;
import java.util.Collections;

public class Wave {
	
	private Field field;
	
	private ArrayList<Enemy> enemies;
	
	private int callTime;
	private int maxSpawnTime;
	
	
	public Wave(ArrayList<Enemy> enemies) {
		Collections.sort(enemies);
		this.enemies = enemies;
		this.maxSpawnTime = enemies.get(enemies.size() - 1).getSpawnTime();
	}
	
	public void call(long now) {
		for (Enemy enemy : enemies) {
			field.addEnemy(enemy);
		}
	}
	
}
