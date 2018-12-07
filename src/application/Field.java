package application;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Field implements Renderable, MouseInteractable {
	
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
	
	private ArrayDeque<Damage> damageQueue = new ArrayDeque<>();

	private ArrayList<Renderable> renderableHolder = new ArrayList<>();
	private ArrayList<Enemy> enemyOnField;
	private ArrayList<Projectile> projectileOnField;
	private ArrayList<Tower> towerOnField;

	private int life;
	private int money;
	
	private CooldownButton callButton;
	private CooldownButton lightningButton;
	
	
	/*** Constructor ***/
	
	public Field(String filePath) throws Exception {
		
		readFile(filePath);
		
		callButton = new CooldownButton(this, callDisable, callEnable, callHover,
				callButtonPosition, 75, 75, 0, true);
		lightningButton = new CooldownButton(this, lightningDisable, lightningEnable, lightningHover, 
				lightningButtonPosition, 75, 75, 3600, false);
		
	}
	
	private void readFile(String filePath) throws Exception {
		File file = new File(filePath);
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
					String waveInfo = "";
					String line = sc.nextLine();
					while (line != "endwave") {
						waveInfo += "\t" + line;
						line = sc.nextLine();
					}
					readWave(waveInfo);
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
	
	private void readWave(String waveInfo) throws InvalidStageFormatException {
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

	@Override
	public void tick(long now, GraphicsContext gc) {
		
		
		
	}
	
	
	/*** Utillity ***/
	
	public void addRender(Renderable render) {
		renderableHolder.add(render);
		Collections.sort(renderableHolder);
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
		damageQueue.push(damage);
	}
	
	
	public void invade(Enemy invader) {
		life--;
		if (life <= 0) {
			// game over
		}
	}
	
	public void addMoney(int amount) {
		this.money += amount;
	}
	

	/*** Mouse ***/
	
	@Override
	public boolean hover(Point2D hoverPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unclick() {
		// TODO Auto-generated method stub
		return false;
	}

	
	/*** Compare ***/
	
	@Override
	public double getRenderPriority() {
		return 0;
	}

	@Override
	public int compareTo(Renderable other) {
		return Double.compare(0, other.getRenderPriority());
	}
	
}
