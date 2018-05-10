package pacman.entries.pacman;

import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;

public class GenThread extends Thread {
	
	
	Gene myGen;
	static int INTERATIONS = 6;
	public GenThread(Gene myGen) {
		
		this.myGen  =myGen;
		
	}
	public void run() {
		
		Executor exec = new Executor();
		myGen.setFitness(exec.runQuietExperiment(new GenController(myGen), new StarterGhosts(), INTERATIONS));
		
	}
	
	
}
