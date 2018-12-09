package application;

import javafx.geometry.Point2D;

public class Damage {
	
	private enum Type {
		ARROW(true, false, false), 
		EXPLOSION(true, false, false), 
		POTION(false, true, false),
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
	public static final Type POTION = Type.POTION;
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
	

	public void dealDamage() {
		switch (type) {
			case ARROW : processArrowDamage(); break;
			case EXPLOSION : processExplosionDamage(); break;
			case POTION : processPotionDamage(); break;
			case LIGHTNING : processLightningDamage(); break;
		}
	}
	
	private void processArrowDamage() {
		double sqrRange = 144;
		double damage = 4;
		
		Enemy nearestEnemy = null;
		double leastSqrDistant = 0;
		
		for (Enemy enemy : field.getEnemyOnField()) {
			double sqrDistant = calculateSquaredDistant(enemy);
			if (sqrDistant > sqrRange) continue;
			if (nearestEnemy == null || sqrDistant < leastSqrDistant) {
				nearestEnemy = enemy;
				leastSqrDistant = sqrDistant;
			}
		}
		
		if (nearestEnemy == null) return;
		nearestEnemy.damage(this, damage);
	}
	
	private void processPotionDamage() {
		double sqrRange = 900;
		double stepdown = 1600;
		double maxDamage = 8;
		
		for (Enemy enemy : field.getEnemyOnField()) {
			 double sqrDistant = calculateSquaredDistant(enemy);
			 if (sqrDistant > sqrRange) continue;
			 double damage = maxDamage * (1 - sqrDistant / stepdown);
			 enemy.damage(this, damage);
		}
	}
	
	private void processExplosionDamage() {
		double sqrRange = 2500;
		double stepdown = 3600;
		double maxDamage = 12;
		
		for (Enemy enemy : field.getEnemyOnField()) {
			 double sqrDistant = calculateSquaredDistant(enemy);
			 if (sqrDistant > sqrRange) continue;
			 double damage = maxDamage * (1 - sqrDistant / stepdown);
			 enemy.damage(this, damage);
		}
	}
	
	private void processLightningDamage() {
		double sqrRange = 10000;
		double damage = 30;
		
		for (Enemy enemy : field.getEnemyOnField()) {
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
