package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class Field {
	
	private static Image fieldImage;
	
	static {
		//fieldImage = new Image("");
	}
	
	private ArrayList<Zombie> enemiesOnField;
	private ArrayList<ArtilleryTower> towersOnField;
	private ArrayList<PrimedTnt> projectilesOnField;

	private ArrayList<Point2D> buildablePosition = new ArrayList<Point2D>();
	
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
				if (mode == 't') initBuildablePosition(line);
				else if (mode == 'p') initPath(line);
				else if (mode == 'w') initWave(line);
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
	
	private void initBuildablePosition(String line) {
		LineProcesser lp = new LineProcesser(line, 1);
		while (!lp.isEnd()) {
			lp.skipUntil('(');
			if (lp.isEnd()) return;
			buildablePosition.add(lp.findNextPoint2D());
		}
	}
	
	private void initPath(String line) {
		LineProcesser lp = new LineProcesser(line, 1);
		ArrayList<Point2D> pathPoints = new ArrayList<>();
		while (!lp.isEnd()) {
			lp.skipUntil('(');
			if (lp.isEnd());
			pathPoints.add(lp.findNextPoint2D());
		}
		Path path = new Path(pathPoints);
		storedPaths.add(path);
	}
	
	private void initWave(String line) {
		LineProcesser lp = new LineProcesser(line, 1);
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
	
	
	public void tick(long now, GraphicsContext gc, MouseEvent event) {
		
		gc.drawImage(fieldImage, 0, 0);
		
		mouseEventListener(now, event);
		
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
	
	
	public void mouseEventListener(long now, MouseEvent event) {
		Point2D mousePosition = new Point2D(event.getX(), event.getY());
		for (Point2D p : buildablePosition) {
			Point2D different = PointOperations.different(p, mousePosition);
			double distant = PointOperations.getSize(different);
			if (distant < 100) {
				// did hover
				//highlightbuild(p);
				break;
			}
		}
	}
	
	
	public void invaded(Zombie enemy) {
		health--;
	}
	
	public void callNextWave(long now) {
		if (currentWave > storedWaves.size()) {
			// endGame
			return;
		}
		storedWaves.get(currentWave).call(now);
		currentWave++;
	}
	
}
