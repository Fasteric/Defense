package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Field {
	
	private static Image fieldImage;
	
	private static Image waveCallButtonEnable;
	private static Image waveCallButtonDisable;
	private static Image waveCallButtonHover;
	
	private static Image lightningButtonEnable;
	private static Image lightningButtonDisable;
	private static Image lightningButtonHover
	;
	private static Image[] lightningCursorAnimation;
	private static int lightningCursorAnimationLength;
	
	static {
		//fieldImage = new Image("");
	}
	
	private ArrayList<Enemy> enemiesOnField;
	private ArrayList<Tower> towersOnField;
	private ArrayList<Projectile> projectilesOnField;
	
	private ArrayList<Path> storedPaths = new ArrayList<Path>();
	private ArrayList<Wave> storedWaves = new ArrayList<Wave>();
	
	private ArrayDeque<Damage> awaitingDamage = new ArrayDeque<>();
	
	private int currentWave = 0;
	private boolean isNextWaveAvailable = true;
	private Point2D waveCallButtonPosition = new Point2D(1180, 620);
	
	private int maxLightningCooldown = 3600;
	private int lightningCooldown;
	private boolean isLightningAvailable = true;
	private boolean isLightningActive = false;
	private Point2D lightningButtonPosition = new Point2D(980, 620);
	
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
		ArrayList<Enemy> enemies = new ArrayList<>();
		lp.skipUntilNumber();
		while (!lp.isEnd()) {
			int spawnTime = lp.readNextInt();
			String type = lp.readNextWord();
			int pathIndex = lp.readNextInt();
			int pathShift = lp.readNextInt();
			Enemy enemy = createEnemy(type, storedPaths.get(pathIndex), pathShift, spawnTime);
			enemies.add(enemy);
		}
		storedWaves.add(new Wave(enemies));
	}
	
	private Enemy createEnemy(String enemyType, Path path, int pathShift, int spawnTime) {
		if (enemyType == "Enemy") {
			
		}
		return null; // TODO
	}
	
	
	/*** Tick ***/
	
	public void tick(long now, GraphicsContext gc, MouseListener mouse) {
		
		if (isCleared) return; // failsafe
		
		// logic
		
		gc.drawImage(fieldImage, 0, 0);
		/*
		if (isNextWaveAvailable) gc.drawImage(waveCallButtonEnable, 1180, 620);
		else gc.drawImage(waveCallButtonDisable, 1180, 620);
		
		
		if (lightningCooldown > 0) {
			lightningCooldown--;
			if (lightningCooldown == 0) {
				isLightningAvailable = true;
			}
		}
		if (isLightningAvailable) gc.drawImage(lightningButtonEnable, 980, 620);
		else gc.drawImage(lightningButtonDisable, 980, 620);
		*/
		// mouse
		
		Point2D hoverPosition = mouse.getHoverPosition();
		interpretHover(gc, hoverPosition);
		
		if (mouse.getSecondaryClickInfo() && isLightningActive) {
			isLightningActive = false;
		}
		
		if (mouse.isPrimaryClicked()) {
			Point2D[] clickInfo = mouse.getPrimaryClickInfo();
			interpretClick(now, gc, clickInfo[0], clickInfo[1]);
		}
		
		// update
		
		for (Projectile projectile : projectilesOnField) {
			projectile.tick(now, gc);
		}
		
		while (!awaitingDamage.isEmpty()) {
			Damage damage = awaitingDamage.pop();
			damage.process();
		}
		
		for (Tower tower : towersOnField) {
			tower.tick(now, gc);
		}
		
		for (Enemy enemy : enemiesOnField) {
			enemy.tick(now, gc);
		}
		
		if (enemiesOnField.size() == 0 && isLastWave() && !isCleared) {
			commenceClear();
			isCleared = true;
		}
		
	}
	
	public void interpretHover(GraphicsContext gc, Point2D hoverPosition) {
		
		if (isNextWaveAvailable) {
			Point2D waveCallDifferent = PointOperations.different(hoverPosition, waveCallButtonPosition);
			if (PointOperations.getSize(waveCallDifferent) < 50) {
				gc.drawImage(waveCallButtonHover, 1180, 620);
				return;
			}
		}
		
		if (isLightningAvailable) {
			Point2D lightningDifferent = PointOperations.different(hoverPosition, lightningButtonPosition);
			if (PointOperations.getSize(lightningDifferent) < 50) {
				gc.drawImage(lightningButtonHover, 980, 620);
				return;
			}
		}
		
		for (Tower tower : towersOnField) {
			if (tower.hover(hoverPosition)) return;
		}
		
		for (Enemy enemy : enemiesOnField) {
			if (enemy.hover(hoverPosition)) return;
		}
		
	}
	
	private void interpretClick(long now, GraphicsContext gc, Point2D pressPosition, Point2D releasePosition) {
		
		if (isLightningActive) {
			commenceLightning(releasePosition);
			isLightningActive = false;
			return;
		}
		
		if (isNextWaveAvailable) {
			Point2D waveCallPress = PointOperations.different(pressPosition, waveCallButtonPosition);
			Point2D waveCallRelease = PointOperations.different(releasePosition, waveCallButtonPosition);
			if (PointOperations.getSize(waveCallPress) < 50 && PointOperations.getSize(waveCallRelease) < 50) {
				callNextWave(now);
				return;
			}
		}
		
		if (isLightningAvailable) {
			Point2D lightningPress = PointOperations.different(pressPosition, lightningButtonPosition);
			Point2D lightningRelease = PointOperations.different(releasePosition, lightningButtonPosition);
			if (PointOperations.getSize(lightningPress) < 50 && PointOperations.getSize(lightningRelease) < 50) {
				isLightningActive = true;
				return;
			}
		}
		
		for (Tower tower : towersOnField) {
			if (tower.click(pressPosition, releasePosition)) return;
		}
		
		for (Enemy enemy : enemiesOnField) {
			// TODO INCOMPLETE T_T
			if (true) return;
		}
		
	}
	
	
	public ArrayList<Enemy> getEnemiesOnField() {
		return enemiesOnField;
	}
	public void addEnemy(Enemy enemy) {
		enemiesOnField.add(enemy);
	}
	public void removeEnemy(Enemy enemy) {
		enemiesOnField.remove(enemy);
	}
	
	public ArrayList<Tower> getTowersOnField() {
		return towersOnField;
	}
	
	public ArrayList<Projectile> getProjectileOnField() {
		return projectilesOnField;
	}
	public void addProjectile(Projectile projectile) {
		projectilesOnField.add(projectile);
	}
	public void removeProjectile(Projectile projectile) {
		projectilesOnField.remove(projectile);
	}
	
	public void pushDamage(Damage damage) {
		awaitingDamage.push(damage);
	}
	
	public void addMoney(int reward) {
		money += reward;
	}
	public void invaded(Enemy enemy) {
		health--;
	}
	

	private boolean isLastWave() {
		return currentWave + 1 >= storedWaves.size();
	}
	private void callNextWave(long now) {
		if (isLastWave()) return; // failsafe
		currentWave++;
		storedWaves.get(currentWave).call(now);
		isNextWaveAvailable = false;
	}
	
	private void commenceLightning(Point2D position) {
		// TODO T_T
		lightningCooldown = maxLightningCooldown;
		isLightningAvailable = false;
	}
	
	private void commenceClear() {
		// TODO
	}
	
}
