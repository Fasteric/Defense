package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Field {
	
	private static Image fieldImage;
	private static Image waveCallEnable;
	private static Image waveCallDisable;
	
	static {
		//fieldImage = new Image("");
	}
	
	private ArrayList<Zombie> enemiesOnField;
	private ArrayList<Tower> towersOnField;
	private ArrayList<PrimedTnt> projectilesOnField;
	
	private ArrayList<Path> storedPaths = new ArrayList<Path>();
	private ArrayList<Wave> storedWaves = new ArrayList<Wave>();
	
	private int currentWave = 0;
	private boolean isNextWaveAvailable = true;
	private boolean isLightningAvailable;
	private boolean isLightningActive;
	private boolean isCleared = false;
	
	public int money = 500;
	public int health = 20;
	
	
	/*** Constructor ***/
	
	public Field(String file) throws InvalidStageFormatException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				char mode = line.charAt(0);
				if (mode == 't') setupTower(line);
				else if (mode == 'p') setupStoredPaths(line);
				else if (mode == 'w') setupStoredWaves(line);
				else {
					br.close();
					throw new InvalidStageFormatException("unknown mode");
				}
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (EndOfLineException e) {
			e.printStackTrace();
			throw new InvalidStageFormatException("expect something but found nothing");
		}
	}
	
	private void setupTower(String line) throws EndOfLineException {
		LineProcesser lp = new LineProcesser(line, 1);
		lp.skipUntilNumber();
		while (!lp.isEnd()) {
			int direction = lp.readNextInt();
			int positionX = lp.readNextInt();
			int positionY = lp.readNextInt();
			Point2D position = new Point2D(lp.readNextInt(), lp.readNextInt());
			towersOnField.add(new NullTower(this, position, direction));
			lp.skipUntilNumber();
		}
	}
	
	private void setupStoredPaths(String line) throws EndOfLineException {
		LineProcesser lp = new LineProcesser(line, 1);
		ArrayList<Point2D> nodes = new ArrayList<>();
		lp.skipUntilNumber();
		while (!lp.isEnd()) {
			int nodeX = lp.readNextInt();
			int nodeY = lp.readNextInt();
			Point2D node = new Point2D(nodeX, nodeY);
			nodes.add(node);
			lp.skipUntilNumber();
		}
		storedPaths.add(new Path(nodes));
	}
	
	private void setupStoredWaves(String line) throws EndOfLineException {
		LineProcesser lp = new LineProcesser(line, 1);
		ArrayList<Zombie> enemies = new ArrayList<>();
		lp.skipUntilNumber();
		while (!lp.isEnd()) {
			int spawnTime = lp.readNextInt();
			String type = lp.readNextWord();
			int pathIndex = lp.readNextInt();
			int pathShift = lp.readNextInt();
			Zombie enemy = createEnemy(type, storedPaths.get(pathIndex), pathShift, spawnTime);
			enemies.add(enemy);
		}
		storedWaves.add(new Wave(enemies));
	}
	
	private Zombie createEnemy(String enemyType, Path path, int pathShift, int spawnTime) {
		if (enemyType == "Zombie") {
			
		}
		return new Zombie(this, path, pathShift, spawnTime);
	}
	
	
	/*** Tick ***/
	
	public void tick(long now, GraphicsContext gc, MouseListener mouse) {
		
		// field
		
		gc.drawImage(fieldImage, 0, 0);
		
		if (storedWaves.get(currentWave).isCompleted(now)) {
			isNextWaveAvailable = true;
		}
		
		if (isNextWaveAvailable) gc.drawImage(waveCallEnable, 1180, 620);
		else gc.drawImage(waveCallDisable, 1180, 620);
		
		// mouse
		
		Point2D hoverPosition = mouse.getHoverPosition();
		interpretHover(hoverPosition);
		
		if (mouse.isClicked()) {
			Point2D[] clickInfo = mouse.getClickInfo();
			interpretClick(clickInfo[0], clickInfo[1]);
		}
		
		// update
		
		for (Zombie enemy : enemiesOnField) {
			enemy.tick(now, gc);
		}
		
		for (Tower tower : towersOnField) {
			tower.tick(now, gc);
		}
		
		for (PrimedTnt projectile : projectilesOnField) {
			projectile.tick(now, gc);
		}
		
	}
	
	public void interpretHover(Point2D hoverPosition) {
		
	}
	
	private void interpretClick(Point2D pressPosition, Point2D releasePosition) {
		
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
	
	public ArrayList<Tower> getTowersOnField() {
		return towersOnField;
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
	
	public void invaded(Zombie enemy) {
		health--;
	}
	
	
	private void callNextWave(long now) {
		if (isLastWave()) return; // failsafe
		currentWave++;
		storedWaves.get(currentWave).call(now);
		isNextWaveAvailable = false;
	}
	private boolean isLastWave() {
		return currentWave + 1 >= storedWaves.size();
	}
	
}
