package application;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Field implements Holder {
	
	private Ticker ticker;
	
	private static Image waveCallButtonDisable;
	private static Image waveCallButtonIdle;
	private static Image waveCallButtonHover;
	
	static {
		waveCallButtonDisable = ImageLoader.callDisable;
		waveCallButtonIdle = ImageLoader.callIdle;
		waveCallButtonHover = ImageLoader.callHover;
	}
	
	private Image fieldImage;
	private File fieldFile;
	
	private ArrayDeque<Wave> storedWave = new ArrayDeque<Wave>();
	private ArrayList<Path> storedPath = new ArrayList<Path>();
	
	private ArrayDeque<Damage> pendingDamage = new ArrayDeque<>();

	private ArrayList<Renderable> renderableHolder = new ArrayList<>();
	private ArrayList<Enemy> enemyOnField = new ArrayList<>();
	private ArrayList<Projectile> projectileOnField = new ArrayList<>();
	private ArrayList<Tower> towerOnField = new ArrayList<>();

	private int life;
	private int money;
	
	RunnableButton waveCallButton;
	private int lifeTime = 0;
	private int nextWaveTime = 0;
	private static int betweenWaveTime = 1200;
	
	private Point2D hoverPosition;
	private Point2D pressPosition;
	private Point2D releasePosition;
	private boolean isClicked = false;
	
	private boolean isStarted = false;
	
	private TextField lifeText = new TextField(new Point2D(60, 25), "", 2);
	private String lifeFormat = "Life %d";
	private TextField waveText = new TextField(new Point2D(60, 50), "", 2);
	private int waveSize;
	private String waveFormat;
	private TextField moneyText = new TextField(new Point2D(60, 75), "", 2);
	private TextField callText = new TextField(new Point2D(10, 690), " Wave call ", 2);
	private String callFormat = "Next wave %d";
	
	
	/*** Constructor ***/
	
	public Field(Ticker ticker, String path) throws Exception {
		
		this.ticker = ticker;
		
		path = "stage/0/";
		
		fieldImage = new Image(ClassLoader.getSystemResource(path + "stage.png").toString());
		String loadString = ClassLoader.getSystemResource(path + "stage.txt").toString();
		fieldFile = new File(loadString.replaceAll("file:/", ""));
		//getClass().getClassLoader().getResource("myFile.txt");
		Scanner sc = new Scanner(fieldFile);
		
		readFile(fieldFile);
		
		// wave call button
		waveCallButton = new RunnableButton(new Point2D(90, 650), 
				waveCallButtonDisable, waveCallButtonIdle, waveCallButtonHover, 48, 48);
		waveCallButton.setOnClicked(() -> {
			callWave();
			isStarted = true;
		});
		addRender(waveCallButton);
		
		//text
		waveSize = storedWave.size();
		waveFormat = "Wave %d/" + waveSize;
		
		System.out.println("Field Constructed");
		
	}
	
	private void readFile(File file) throws Exception {
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
		
		// process mouse event
		boolean isHoverProcessed = false;
		boolean isClickProcessed = false;
		for (int i = renderableHolder.size() - 1; i >= 0; i--) {
			Renderable render = renderableHolder.get(i);
			if (render instanceof MouseInteractable) {
				MouseInteractable mouse = (MouseInteractable) render;
				if (!isHoverProcessed) isHoverProcessed = mouse.hover(hoverPosition); // allow only one object to process hover
				if (isClicked) {
					if (!isClickProcessed) {
						isClickProcessed = mouse.click(pressPosition, releasePosition);
						if (isClickProcessed) continue; // allow only one object to process click
					}
					mouse.unclick();
				}
			}
		}
		
		// wave call and auto wave release
		if (!storedWave.isEmpty()) {
			int waveAvailability = lifeTime - nextWaveTime;
			if (waveAvailability >= betweenWaveTime) {
				callWave();
			}
			else if (waveAvailability >= 0 && waveAvailability < betweenWaveTime) {
				waveCallButton.enable();
			}
		}
		else {
			waveCallButton.disable();
		}
		
		// process pending damage
		while (!pendingDamage.isEmpty()) {
			pendingDamage.pop().dealDamage();
		}
		
		// draw ui
		gc.drawImage(fieldImage, 0, 0);
		gc.drawImage(ImageLoader.blankpane, 7, 600);
		gc.drawImage(ImageLoader.hangingSign, 20, -40);
		lifeText.setText(String.format(lifeFormat, life));
		lifeText.tick(now, gc);
		gc.drawImage(ImageLoader.iconHeart, 30, 25);
		waveText.setText(String.format(waveFormat, waveSize - storedWave.size()));
		waveText.tick(now, gc);
		gc.drawImage(ImageLoader.iconEnemy, 30, 50);
		moneyText.setText(Integer.toString(money));
		moneyText.tick(now, gc);
		gc.drawImage(ImageLoader.iconEmerald, 30, 75);
		if (storedWave.size() > 0) {
			if (isStarted) callText.setText(String.format(callFormat, (lifeTime - nextWaveTime - betweenWaveTime) / -60));
			callText.tick(now, gc);
		}
		

		// draw renderable
		Collections.sort(renderableHolder);
		for (int i = 0; i < renderableHolder.size(); i++) {
			renderableHolder.get(i).tick(now, gc);
		}
		
		
		// DEBUG
		if (isClicked) {
			System.out.println("DEBUG : renderableHolder size : " + renderableHolder.size());
		}
		
		if (isStarted) {
			lifeTime++;
		}
		isClicked = false; // reset
		
	}
	
	private void callWave() {
		System.out.println("DEBUG : Wave Call");
		nextWaveTime = lifeTime + storedWave.peek().getWaveDuration();
		storedWave.peek().call();
		storedWave.pop();
		waveCallButton.disable();
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
	
	
	public void invade(int cost) {
		life -= cost;
		if (life <= 0) {
			System.out.println("DEBUG : Field.invade : Game Over");
			// trigger game over
		}
	}

	
	public int getMoney() {
		return money;
	}
	public void addMoney(int amount) {
		money += amount;
	}
	public void removeMoney(int amount) {
		money -= amount;
	}
	

	/*** Mouse ***/
	
	@Override
	public boolean hover(Point2D hoverPosition) {
		this.hoverPosition = hoverPosition;
		return true;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		this.pressPosition = pressPosition;
		this.releasePosition = releasePosition;
		isClicked = true;
		return true;
	}

	@Override
	public void unclick() {
		isClicked = false;
	}
	
}
