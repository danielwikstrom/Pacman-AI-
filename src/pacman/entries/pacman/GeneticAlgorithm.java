package pacman.entries.pacman;


import java.util.Collections;
import java.util.Comparator;
import java.util.Random;        // for generating random numbers
import java.util.concurrent.ThreadLocalRandom;

import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;

import java.util.ArrayList;     // arrayLists are more versatile than arrays


/**
 * Genetic Algorithm sample class <br/>
 * <b>The goal of this GA sample is to maximize the number of capital letters in a String</b> <br/>
 * compile using "javac GeneticAlgorithm.java" <br/>
 * test using "java GeneticAlgorithm" <br/>
 *
 * @author A.Liapis
 */

public class GeneticAlgorithm {
    // --- constants
    static int CHROMOSOME_SIZE=38;
    static int POPULATION_SIZE=100;
    static int MUTATION_RATE = 30;
    //Iteraciones para la media de puntos fitness
	static int INTERATIONS = 10;

    // --- variables:

    /**
     * The population contains an ArrayList of genes (the choice of arrayList over
     * a simple array is due to extra functionalities of the arrayList, such as sorting)
     */
    ArrayList<Gene> mPopulation;

    // --- functions:

    /**
     * Creates the starting population of Gene classes, whose chromosome contents are random
     * @param size: The size of the popultion is passed as an argument from the main class
     */
    public GeneticAlgorithm(int size){
        // initialize the arraylist and each gene's initial weights HERE
        mPopulation = new ArrayList<Gene>();
        for(int i = 0; i < size; i++){
            Gene entry = new Gene();
            entry.randomizeChromosome();
            mPopulation.add(entry);
        }
    }
    /**
     * For all members of the population, runs a heuristic that evaluates their fitness
     * based on their phenotype. The evaluation of this problem's phenotype is fairly simple,
     * and can be done in a straightforward manner. In other cases, such as agent
     * behavior, the phenotype may need to be used in a full simulation before getting
     * evaluated (e.g based on its performance)
     * @throws InterruptedException 
     */
    public void evaluateGeneration() throws InterruptedException{
    	
    	GenThread[] threads = new GenThread[mPopulation.size()];
        for(int i = 0; i < mPopulation.size(); i++){
        	
        	threads[i]= new GenThread(mPopulation.get(i));	
        	threads[i].start();	
        	
        }
        
        for(int i = 0; i < mPopulation.size(); i++){
        	/*Gene gen = mPopulation.get(i);
            // evaluation of the fitness function for each gene in the population goes HERE
        	gen.setFitness(exec.runQuietExperiment(new GenController(gen), new StarterGhosts(), INTERATIONS));*/
    
        		threads[i].join();		
        	
        	
        }
        
    }
    
    public void sort() {
    	
    	Collections.sort(mPopulation,new Comparator<Gene>() {
    		@Override public int compare(Gene a, Gene b) {
    			return Float.compare(b.mFitness,a.mFitness);
    		}
		});
    	
    }
    public void naturalSelection() {
    	
    	mPopulation.subList(POPULATION_SIZE-1,mPopulation.size()-1).clear();
    	
    	
    }
    /**
     * With each gene's fitness as a guide, chooses which genes should mate and produce offspring.
     * The offspring are added to the population, replacing the previous generation's Genes either
     * partially or completely. The population size, however, should always remain the same.
     * If you want to use mutation, this function is where any mutation chances are rolled and mutation takes place.
     */
    public void produceNextGeneration(){
        // use one of the offspring techniques suggested in class (also applying any mutations) HERE
    	int sexyTimes = 30;
    	int pool = 5;
    	ArrayList<Gene> newBorns = new ArrayList<Gene>();
    	for(int i = 0; i < sexyTimes; i++) {
    		
    		Gene[] aux = getParent(pool).reproduce(getParent(pool));
    		
    		if(ThreadLocalRandom.current().nextInt(100) < MUTATION_RATE)
    			aux[1].mutate();
    		if(ThreadLocalRandom.current().nextInt(100) < MUTATION_RATE)
    			aux[0].mutate();
    		newBorns.add(aux[0]);
    		newBorns.add(aux[1]);
    		
    	}
    	naturalSelection();
    	mPopulation.addAll(newBorns);
    	
    }
    
    
    public Gene getParent(int pool) {
    	
    	float bestFitnes = .0F;
    	Gene bestParent = null;
    	for(int i = 0; i < pool;i++) {
    		
    		int rand = ThreadLocalRandom.current().nextInt(mPopulation.size()-1);
    		if(mPopulation.get(rand).mFitness > bestFitnes) {
    			bestFitnes = mPopulation.get(rand).mFitness;
    			bestParent = mPopulation.get(rand);
    		}
    		
    	}
    	return bestParent;
    	
    }
    // accessors
    /**
     * @return the size of the population
     */
    public int size(){ return mPopulation.size(); }
    /**
     * Returns the Gene at position <b>index</b> of the mPopulation arrayList
     * @param index: the position in the population of the Gene we want to retrieve
     * @return the Gene at position <b>index</b> of the mPopulation arrayList
     */
    public Gene getGene(int index){ return mPopulation.get(index); }

    // Genetic Algorithm maxA testing method
    public static void main( String[] args ) throws InterruptedException{
    	System.out.println("Start");
        // Initializing the population (we chose 500 genes for the population,
        // but you can play with the population size to try different approaches)
        GeneticAlgorithm population = new GeneticAlgorithm(POPULATION_SIZE);
        int generationCount = 0;
        // For the sake of this sample, evolution goes on forever.
        // If you wish the evolution to halt (for instance, after a number of
        //   generations is reached or the maximum fitness has been achieved),
        //   this is the place to make any such checks
        while(true){
            // --- evaluate current generation:
            population.evaluateGeneration();
            // --- print results here:
            // we choose to print the average fitness,
            // as well as the maximum and minimum fitness
            // as part of our progress monitoring

            population.sort();
            
            String output = "Generation: " + generationCount;
            output += "\t MaxFitness: " + population.mPopulation.get(0).mFitness;
            System.out.println(output);
            
            double avgScore=0.0;
            for(int k=0;k<population.size();k++)
            {
            	avgScore+=population.mPopulation.get(k).mFitness;
            }
            avgScore=avgScore/population.size();
            System.out.println("Media= "+avgScore);
            // produce next generation:
            if(generationCount>=300 || avgScore>= 8000)
            {
            	for(int j=0;j<population.mPopulation.get(0).mChromosome.length;j++)
            	{
            		System.out.print(" "+ population.mPopulation.get(0).getChromosomeElement(j));
            	}
            	System.out.println();
            	break;
            }
            population.produceNextGeneration();
            generationCount++;
            avgScore=0.0;
        }
    }
};

