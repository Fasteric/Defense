package application;

import java.util.ArrayList;
import java.util.Collections;

public class Wave {
	
	private Field field;
	
	private ArrayList<Enemy> enemies;
	
	private long callTime;
	private int maxSpawnTime;
	
	
	public Wave(ArrayList<Enemy> enemies) {
		Collections.sort(enemies);
		this.enemies = enemies;
		this.maxSpawnTime = enemies.get(enemies.size() - 1).getSpawnTime();
	}
	
	public void call(long now) {
		for (Enemy e : enemies) {
			e.call(now);
			field.addEnemy(e);
		}
	}
	
	public boolean isCompleted(long now) {
		return now > callTime + maxSpawnTime;
	}
	
}
