package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Field {
	
	public ArrayList<Zombie> enemiesOnField;
	public ArrayList<ArtilleryTower> towersOnField;
	public ArrayList<PrimedTnt> projectilesOnField;

	private ArrayList<Point2D> towerPosition = new ArrayList<Point2D>();
	
	private ArrayList<Path> storedPaths = new ArrayList<Path>();
	private ArrayList<Wave> storedWaves = new ArrayList<Wave>();
	
	private int currentWave;
	
	public int money = 500;
	public int health = 20;
	
	
	public Field(String file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				char mode = line.charAt(0);
				if (mode == 't') processTower(line);
				else if (mode == 'p') processPath(line);
				else if (mode == 'w') processWave(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		currentWave = 0;
	}
	
	private void processTower(String line) {
		LineProcesser lp = new LineProcesser(line, 1);
		while (!lp.isEnd()) {
			lp.skipUntil('(');
			if (lp.isEnd()) return;
			towerPosition.add(lp.findNextPoint2D());
		}
	}
	
	private void processPath(String line) {
		LineProcesser lp = new LineProcesser(line, 1);
		int pathIndex = lp.findNextInt();
		ArrayList<Point2D> pathPoints = new ArrayList<>();
		while (!lp.isEnd()) {
			lp.skipUntil('(');
			if (lp.isEnd());
			pathPoints.add(lp.findNextPoint2D());
		}
		Path path = new Path(pathPoints);
		storedPaths.add(path);
	}
	
	private void processWave(String line) {
		LineProcesser lp = new LineProcesser(line, 1);
		int waveIndex = lp.findNextInt();
		ArrayList<Zombie> enemies = new ArrayList<>();
		while (!lp.isEnd()) {
			lp.skipUntil('(');
			if (lp.isEnd()) break;
			String enemyType = lp.findNextWord();
			lp.skipUntil(',');
			int pathIndex = lp.findNextInt();
			lp.skipUntil(',');
			int pathShift = lp.findNextInt();
			lp.skipUntil(',');
			int spawnTime = lp.findNextInt();
			lp.skipUntil(')');
			Zombie enemy = spawnEnemy(enemyType, storedPaths.get(pathIndex), pathShift, spawnTime);
			enemies.add(enemy);
		}
		Wave wave = new Wave(enemies);
		storedWaves.add(wave);
	}
	
	private Zombie spawnEnemy(String enemyType, Path path, int pathShift, int spawnTime) {
		return new Zombie(this, path, pathShift, spawnTime);
	}
	
	
	public ArrayList<Zombie> getEnemiesOnField() {
		return enemiesOnField;
	}
	public void addEnemy(Zombie enemy) {
		enemiesOnField.add(enemy);
	}
	public void removeEnemy(Zombie enemy) {
		enemiesOnField.remove(enemy);
	}
	
	public ArrayList<ArtilleryTower> getTowersOnField() {
		return towersOnField;
	}
	public void addTower(ArtilleryTower tower) {
		towersOnField.add(tower);
	}
	public void removeTower(ArtilleryTower tower) {
		towersOnField.remove(tower);
	}
	
	public ArrayList<PrimedTnt> getProjectileOnField() {
		return projectilesOnField;
	}
	public void addProjectile(PrimedTnt projectile) {
		projectilesOnField.add(projectile);
	}
	public void removeProjectile(PrimedTnt projectile) {
		projectilesOnField.remove(projectile);
	}
	
	
	public void tick(long now, GraphicsContext gc) {
		for (Zombie enemy : enemiesOnField) {
			enemy.tick(now, gc);
		}
		for (ArtilleryTower tower : towersOnField) {
			tower.tick(now, gc);
		}
		for (PrimedTnt projectile : projectilesOnField) {
			projectile.tick(now, gc);
		}
	}
	
	public void callNextWave(long now) {
		storedWaves.get(currentWave).call(now);
	}
	
}
