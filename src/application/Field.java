package application;

import java.util.ArrayList;

public class Field {
	
	public ArrayList<Zombie> enemiesOnField;
	public ArrayList<ArtilleryTower> towersOnField;
	public ArrayList<PrimedTnt> projectilesOnField;
	
	private ArrayList<Wave> waves;
	
	public void tick() {
		
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
	
}
