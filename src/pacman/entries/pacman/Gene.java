package pacman.entries.pacman;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Gene {
    // --- variables:

    /**
     * Fitness evaluates to how "close" the current gene is to the
     * optimal solution (i.e. contains only 1s in its chromosome)
     * A gene with higher fitness value from another signifies that
     * it has more 1s in its chromosome, and is thus a better solution
     * While it is common that fitness is a floating point between 0..1
     * this is not necessary: the only constraint is that a better solution
     * must have a strictly higher fitness than a worse solution
     */
    protected float mFitness;
    /**
     * The chromosome contains only integers 0 or 1 (we choose to avoid
     * using a boolean type to make computations easier)
     */
    protected float mChromosome[];

    // --- functions:
    /**
     * Allocates memory for the mChromosome array and initializes any other data, such as fitness
     * We chose to use a constant variable as the chromosome size, but it can also be
     * passed as a variable in the constructor
     */
    Gene() {
        // allocating memory for the chromosome array
        mChromosome = new float[GeneticAlgorithm.CHROMOSOME_SIZE];
        // initializing fitness
        mFitness = 0.f;
    }

    /**
     * Randomizes the numbers on the mChromosome array to values 0 or 1
     */
    public void randomizeChromosome(){
        // code for randomization of initial weights goes HERE
    	
    	 for(int i = 0; i < mChromosome.length; i++){
             mChromosome[i] = (float) ThreadLocalRandom.current().nextDouble(0,1);
             }
    	
    	
    }

    /**
     * Creates a number of offspring by combining (using crossover) the current
     * Gene's chromosome with another Gene's chromosome.
     * Usually two parents will produce an equal amount of offpsring, although
     * in other reproduction strategies the number of offspring produced depends
     * on the fitness of the parents.
     * @param other: the other parent we want to create offpsring from
     * @return Array of Gene offspring (default length of array is 2).
     * These offspring will need to be added to the next generation.
     */
    public Gene[] reproduce(Gene other){
        
    	float alpha = .1f;
    	int numberOfDescendants=2;
    	Gene[] result = new Gene[numberOfDescendants];
    	Gene whiteParent = other;
    	Gene gipsyParent = this ;

    	if(getFitness() > other.getFitness()) {
    	whiteParent = this;
    	gipsyParent = other;

    	}


    	for(int i = 0; i < numberOfDescendants; i++) {
    	result[i]=new Gene();
    	int random=ThreadLocalRandom.current().nextInt(0,1);
    	for(int j=0;j<GeneticAlgorithm.CHROMOSOME_SIZE;j++)
    	{
    	if(random==0)
    	result[i].mChromosome[j] = whiteParent.mChromosome[j] + ((whiteParent.mChromosome[j] - gipsyParent.mChromosome[j])*alpha) ;
    	else
    	result[i].mChromosome[i] = gipsyParent.mChromosome[j] - ((whiteParent.mChromosome[j] - gipsyParent.mChromosome[j])*alpha) ;

    	}


    	}
        // initilization of offspring chromosome goes HERE
        
        
        return result;
    }

    /**
     * Mutates a gene using inversion, random mutation or other methods.
     * This function is called after the mutation chance is rolled.
     * Mutation can occur (depending on the designer's wishes) to a parent
     * before reproduction takes place, an offspring at the time it is created,
     * or (more often) on a gene which will not produce any offspring afterwards.
     */
    public void mutate(){
    	 
    	
    	int mutations = ThreadLocalRandom.current().nextInt(0,GeneticAlgorithm.CHROMOSOME_SIZE);
    	
    	for(int i = 0;i <mutations;i++) {
    		
    		//Sumamos desde -0.05 +0.05
    		int indx = ThreadLocalRandom.current().nextInt(0,GeneticAlgorithm.CHROMOSOME_SIZE);
    		//mChromosome[indx] = (float)(ThreadLocalRandom.current().nextDouble(-0.1, 0.1) + mChromosome[indx]) % (float)1 ;
    	
    		mChromosome[indx] = (float)ThreadLocalRandom.current().nextDouble(0,1);
    	}
    	
    			
    }
    /**
     * Sets the fitness, after it is evaluated in the GeneticAlgorithm class.
     * @param value: the fitness value to be set
     */
    public void setFitness(float value) { mFitness = value; }
    /**
     * @return the gene's fitness value
     */
    public float getFitness() { return mFitness; }
    /**
     * Returns the element at position <b>index</b> of the mChromosome array
     * @param index: the position on the array of the element we want to access
     * @return the value of the element we want to access (0 or 1)
     */
    public float getChromosomeElement(int index){ return mChromosome[index]; }

    /**
     * Sets a <b>value</b> to the element at position <b>index</b> of the mChromosome array
     * @param index: the position on the array of the element we want to access
     * @param value: the value we want to set at position <b>index</b> of the mChromosome array (0 or 1)
     */
    public void setChromosomeElement(int index, float value){ mChromosome[index]=value; }
    /**
     * Returns the size of the chromosome (as provided in the Gene constructor)
     * @return the size of the mChromosome array
     */
    public int getChromosomeSize() { return mChromosome.length; }
    /**
     * Corresponds the chromosome encoding to the phenotype, which is a representation
     * that can be read, tested and evaluated by the main program.
     * @return a String with a length equal to the chromosome size, composed of A's
     * at the positions where the chromosome is 1 and a's at the posiitons
     * where the chromosme is 0
     */
    public String getPhenotype() {
        // create an empty string
        String result="";
        for(int i = 0; i < mChromosome.length; i++){
            result += " "+mChromosome[i];
            }
        return result;
    }
    
    
}
