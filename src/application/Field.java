package application;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Field {
	
	private static Image callDisable;
	private static Image callEnable;
	private static Image callHover;
	private static Point2D callButtonPosition = new Point2D(1180, 620);
	
	private static Image lightningDisable;
	private static Image lightningEnable;
	private static Image lightningHover;
	private static Point2D lightningButtonPosition = new Point2D(980, 620);
	
	static {
		// load image
	}
	
	
	private Image fieldImage;

	private ArrayDeque<Wave> storedWave = new ArrayDeque<Wave>();
	private ArrayList<Path> storedPath = new ArrayList<Path>();
	
	private ArrayDeque<Damage> pendingDamage = new ArrayDeque<>();

	private ArrayList<Renderable> renderableHolder = new ArrayList<>();
	private ArrayList<Enemy> enemyOnField = new ArrayList<>();
	private ArrayList<Projectile> projectileOnField = new ArrayList<>();
	private ArrayList<Tower> towerOnField = new ArrayList<>();

	private int life;
	private int money;
	
	private CooldownButton callButton;
	private CooldownButton lightningButton;
	
	private int lifeTime = 0;
	private int nextWaveTime = 300;
	
	private Point2D mouseHoverPosition;
	private Point2D mousePressPosition;
	private Point2D mouseReleasePosition;
	private boolean isPrimaryClicked = false;
	private boolean isSecondaryClicked = false;
	
	
	/*** Constructor ***/
	
	public Field(String path) throws Exception {
		
		fieldImage = new Image("res/stage/stage.png");
		
		readFile("bin/res/stage/stage.txt");
		/*
		callButton = new CooldownButton(this, callDisable, callEnable, callHover,
				callButtonPosition, 75, 75, 0, true);
		lightningButton = new CooldownButton(this, lightningDisable, lightningEnable, lightningHover, 
				lightningButtonPosition, 75, 75, 3600, false);
		*/
		// Constructor is not complete yet
		
		System.out.println("Field Constructed");
		
	}
	
	private void readFile(String path) throws Exception {
		File file = new File(path);
		Scanner sc = new Scanner(file);
		
		while (sc.hasNext()) {
			String mode = sc.next();
			
			switch (mode) {
				case "life" : {
					this.life = readInt(sc);
					break;
				}
				case "money" : {
					this.money = readInt(sc);
					break;
				}
				case "tower" : {
					readTower(sc.nextLine());
					break;
				}
				case "path" : {
					readPath(sc.nextLine());
					break;
				}
				case "wave" : {
					if (!sc.hasNextInt()) {
						throw new InvalidStageFormatException("expect int waveDuration");
					}
					int waveDuration = sc.nextInt();
					String waveInfo = "";
					String line = sc.nextLine();
					while (!line.equals("endwave")) {
						waveInfo += "\t" + line;
						line = sc.nextLine();
					}
					readWave(waveInfo, waveDuration);
					break;
				}
				default : {
					throw new InvalidStageFormatException("unknown prefix : " + mode);
				}
			}
			
		}
		
		sc.close();
	}
	
	private int readInt(Scanner sc) throws InvalidStageFormatException {
		if (!sc.hasNextInt()) {
			throw new InvalidStageFormatException("expect int");
		}
		return sc.nextInt();
	}
	
	private void readTower(String towersInfo) throws InvalidStageFormatException {
		Scanner sc = new Scanner(towersInfo);
		
		while (sc.hasNextInt()) {
			
			int direction = sc.nextInt();
			
			if (!sc.hasNextDouble()) {
				throw new InvalidStageFormatException("expect double for tower's x position");
			}
			double towerX = sc.nextDouble();
			
			if (!sc.hasNextDouble()) {
				throw new InvalidStageFormatException("expect double for tower's y position");
			}
			double towerY = sc.nextDouble();
			
			NullTower tower = new NullTower(this, new Point2D(towerX, towerY), direction);
			addTower(tower);
			
		}
		
		sc.close();
	}
	
	private void readPath(String pathInfo) throws InvalidStageFormatException {
		Scanner sc = new Scanner(pathInfo);
		ArrayList<Point2D> nodes = new ArrayList<>();
		
		while (sc.hasNextDouble()) {
			
			double nodeX = sc.nextDouble();
			
			if (!sc.hasNextDouble()) {
				throw new InvalidStageFormatException("expect double for node's y position");
			}
			double nodeY = sc.nextDouble();
			
			nodes.add(new Point2D(nodeX, nodeY));
			
		}
		
		if (nodes.size() < 2) {
			throw new InvalidStageFormatException("path must have at least 2 nodes");
		}
		Path path = new Path(nodes);
		storedPath.add(path);
		
		sc.close();
	}
	
	private void readWave(String waveInfo, int waveDuration) throws InvalidStageFormatException {
		Scanner sc = new Scanner(waveInfo);
		ArrayList<Enemy> enemiesInWave = new ArrayList<>();
		
		while (sc.hasNext()) {
			
			String type = sc.next();
			
			if (!sc.hasNextInt()) {
				throw new InvalidStageFormatException("expect int pathIndex");
			}
			int pathIndex = sc.nextInt();
			
			if (!sc.hasNextDouble()) {
				throw new InvalidStageFormatException("expect double pathShift");
			}
			double pathShift = sc.nextDouble();
			
			if (!sc.hasNextInt()) {
				throw new InvalidStageFormatException("expect int spawnTime");
			}
			int spawnTime = sc.nextInt();
			
			Enemy enemy = spawnEnemy(type, pathIndex, pathShift, spawnTime);
			enemiesInWave.add(enemy);
			
		}
		
		Wave wave = new Wave(this, enemiesInWave, waveDuration);
		storedWave.add(wave);
		
		sc.close();
	}
	
	private Enemy spawnEnemy(String type, int pathIndex, double pathShift, int spawnTime) 
			throws InvalidStageFormatException {
		switch (type) {
			case "Zombie" : {
				return new Zombie(this, storedPath.get(pathIndex), pathShift, spawnTime);
			}
			default : {
				throw new InvalidStageFormatException("unknown enemy type : " + type);
			}
		}
	}
	
	
	/*** Tick ***/

	public void tick(long now, GraphicsContext gc) {
		
		// retrieve mouse
		boolean isClickProcessed = false;
		for (int i = renderableHolder.size() - 1; i >= 0; i--) {
			Renderable render = renderableHolder.get(i);
			if (render instanceof MouseInteractable) {
				MouseInteractable mr = (MouseInteractable) render;
				mr.hover(mouseHoverPosition);
				if (!isClickProcessed && isPrimaryClicked) {
					boolean isProcessable = mr.click(mousePressPosition, mouseReleasePosition);
					if (!isProcessable) mr.unclick();
					else isClickProcessed = true; // allow only one object to process click
				}
				else if (isClickProcessed && isPrimaryClicked) {
					mr.unclick();
				}
			}
		}
		
		// release wave
		if (lifeTime >= nextWaveTime && !storedWave.isEmpty()) {
			System.out.println("DEBUG : Wave Call");
			nextWaveTime = lifeTime + storedWave.peek().getWaveDuration();
			storedWave.peek().call(now);
			storedWave.pop();
		}
		
		// process damage
		while (!pendingDamage.isEmpty()) {
			pendingDamage.pop().dealDamage();
		}
		
		// draw background
		gc.drawImage(fieldImage, 0, 0);
		

		// draw renderable
		Collections.sort(renderableHolder);
		for (int i = 0; i < renderableHolder.size(); i++) {
			renderableHolder.get(i).tick(now, gc);
		}
		
		
		// DEBUG
		if (isPrimaryClicked) {
			System.out.println("DEBUG : renderableHolder size : " + renderableHolder.size());
		}
		
		lifeTime++;
		isPrimaryClicked = false; // reset
		isSecondaryClicked = false;
		
	}
	
	
	/*** Utillity ***/
	
	public void addRender(Renderable render) {
		renderableHolder.add(render);
	}
	public void removeRender(Renderable render) {
		renderableHolder.remove(render);
	}
	
	public ArrayList<Enemy> getEnemyOnField() {
		return enemyOnField;
	}
	public void addEnemy(Enemy enemy) {
		enemyOnField.add(enemy);
		addRender(enemy);
	}
	public void removeEnemy(Enemy enemy) {
		enemyOnField.remove(enemy);
		removeRender(enemy);
	}
	
	public void addProjectile(Projectile projectile) {
		projectileOnField.add(projectile);
		addRender(projectile);
	}
	public void removeProjectile(Projectile projectile) {
		projectileOnField.remove(projectile);
		removeRender(projectile);
	}
	
	public void addTower(Tower tower) {
		towerOnField.add(tower);
		addRender(tower);
	}
	public void removeTower(Tower tower) {
		towerOnField.remove(tower);
		removeRender(tower);
	}
	
	public void pushDamage(Damage damage) {
		pendingDamage.push(damage);
	}
	
	
	public void invade(Enemy invader) {
		life--;
		if (life <= 0) {
			// trigger game over
		}
	}
	
	public void addMoney(int amount) {
		this.money += amount;
	}
	

	/*** Mouse ***/
	
	public void setHover(Point2D hoverPosition) {
		mouseHoverPosition = hoverPosition;
	}

	public void setPrimaryClick(Point2D pressPosition, Point2D releasePosition) {
		mousePressPosition = pressPosition;
		mouseReleasePosition = releasePosition;
		isPrimaryClicked = true;
	}
	
	public void setSecondaryClick() {
		isSecondaryClicked = true;
	}
	
}
