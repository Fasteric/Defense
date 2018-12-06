package application;

import java.util.ArrayList;

public class Wave {
	
	private ArrayList<Zombie> wave;
	
	
	public Wave(ArrayList<Zombie> wave) {
		this.wave = wave;
	}
	
	public void call(long callingTime) {
		for (Zombie e : wave) {
			e.call(callingTime);
			// someList.add(e);
		}
	}
	
}
