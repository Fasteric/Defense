package application;

import java.util.ArrayList;
import java.util.Collections;

public class Wave {
	
	private Field field;
	
	private ArrayList<Zombie> enemies;
	
	private long callTime;
	private int maxSpawnTime;
	
	
	public Wave(ArrayList<Zombie> enemies) {
		Collections.sort(enemies);
		this.enemies = enemies;
		this.maxSpawnTime = enemies.get(enemies.size() - 1).getSpawnTime();
	}
	
	public void call(long now) {
		for (Zombie e : enemies) {
			e.call(now);
			field.addEnemy(e);
		}
	}
	
	public boolean isCompleted(long now) {
		return now > callTime + maxSpawnTime;
	}
	
}
