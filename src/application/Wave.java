package application;

import java.util.ArrayList;

public class Wave {
	
	private Field field;
	
	private ArrayList<Enemy> enemies;
	private int waveDuration;
	
	
	public Wave(Field field, ArrayList<Enemy> enemies, int waveDuration) {
		this.field = field;
		this.enemies = enemies;
		this.waveDuration = waveDuration;
	}
	
	
	public int call(long now) {
		for (Enemy enemy : enemies) {
			field.addEnemy(enemy);
		}
		return waveDuration;
	}
	
	public int getWaveDuration() {
		return waveDuration;
	}
	
}
