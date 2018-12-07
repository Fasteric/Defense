package application;

import javafx.geometry.Point2D;

public class Damage {
	
	
	
	private enum Type {
		ARROW(true, false, false), 
		EXPLOSION(true, false, false), 
		LIGHTNING(false, false, true);
		
		public boolean isPhysical;
		public boolean isMagical;
		public boolean isSpecial;
		
		Type(boolean isPhysical, boolean isMagical, boolean isSpecial) {
			this.isPhysical = isPhysical;
			this.isMagical = isMagical;
			this.isSpecial = isSpecial;
		}
	}
	
	public static final Type ARROW = Type.ARROW;
	public static final Type EXPLOSION = Type.EXPLOSION;
	public static final Type LIGHTNING = Type.LIGHTNING;
	
	private Type type;
	private Field field;
	private Point2D position;
	
	
	public Damage(Type type, Field field, Point2D position) {
		this.type = type;
		this.field = field;
		this.position = position;
	}
	
	public Type getType() {
		return type;
	}
	public boolean isPhysical() {
		return type.isPhysical;
	}
	public boolean isMagical() {
		return type.isMagical;
	}
	public boolean isSpecial() {
		return type.isSpecial;
	}
	

	public void process() {
		switch (type) {
		case ARROW: processArrowDamage(); break;
		case EXPLOSION: processExplosionDamage(); break;
		case LIGHTNING: processLightningDamage(); break;
		}
	}
	
	private void processArrowDamage() {
		double sqrRange = 100;
		double damage = 9;
		Enemy nearestEnemy = null;
		double leastSqrDistant = 0;
		
		for (Enemy enemy : field.getEnemiesOnField()) {
			double sqrDistant = calculateSquaredDistant(enemy);
			if (sqrDistant > sqrRange) continue;
			if (nearestEnemy == null || sqrDistant < leastSqrDistant) {
				nearestEnemy = enemy;
				leastSqrDistant = sqrDistant;
			}
		}
		
		nearestEnemy.damage(this, damage);
	}
	
	private void processExplosionDamage() {
		double sqrRange = 900;
		double maxDamage = 60;
		
		for (Enemy enemy : field.getEnemiesOnField()) {
			 double sqrDistant = calculateSquaredDistant(enemy);
			 if (sqrDistant > sqrRange) continue;
			 double damage = maxDamage * (1 - sqrDistant / 1200);
			 enemy.damage(this, damage);
		}
	}
	
	private void processLightningDamage() {
		double sqrRange = 10000;
		double damage = 12;
		
		for (Enemy enemy : field.getEnemiesOnField()) {
			 double sqrDistant = calculateSquaredDistant(enemy);
			 if (sqrDistant > sqrRange) continue;
			 enemy.damage(this, damage);
		}
	}
	
	
	private double calculateSquaredDistant(Enemy enemy) {
		Point2D enemyPosition = enemy.getPosition();
		Point2D different = PointOperations.different(position, enemyPosition);
		return PointOperations.getSquaredSize(different);
	}

}
