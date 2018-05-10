package pacman.entries.pacman;

import java.util.ArrayList;

import com.fuzzylite.Engine;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Ramp;
import com.fuzzylite.term.Triangle;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Ghost;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class GenController extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	Engine engine = null;
	public GenController(Gene gen) {
		
		
		
		engine = new Engine();
		engine.setName("Fuzzy-Pacman");

		InputVariable input = new InputVariable();
		input.setEnabled(true);
		input.setName("DistanceToEnemy");
		input.setRange(0.000, 1.000);
		input.addTerm(new Ramp("NEAR", gen.getChromosomeElement(0),gen.getChromosomeElement(1) ));
		input.addTerm(new Triangle("MEDIUM",gen.getChromosomeElement(2),gen.getChromosomeElement(3),gen.getChromosomeElement(4)));
		input.addTerm(new Ramp("FAR", gen.getChromosomeElement(5), gen.getChromosomeElement(6)));
		engine.addInputVariable(input);
		
		input = new InputVariable();
		input.setEnabled(true);
		input.setName("DistanceToVictim");
		input.setRange(0.000, 1.000);
		input.addTerm(new Ramp("NEAR",gen.getChromosomeElement(7),gen.getChromosomeElement(8) ));
		input.addTerm(new Triangle("MEDIUM",gen.getChromosomeElement(9),gen.getChromosomeElement(10),gen.getChromosomeElement(11)));
		input.addTerm(new Ramp("FAR", gen.getChromosomeElement(12), gen.getChromosomeElement(13)));
		engine.addInputVariable(input);
		
		input = new InputVariable();
		input.setEnabled(true);
		input.setName("DistanceToPowerPill");
		input.setRange(0.000, 1.000);
		input.addTerm(new Ramp("NEAR", gen.getChromosomeElement(14),gen.getChromosomeElement(15)));
		input.addTerm(new Triangle("MEDIUM",gen.getChromosomeElement(16),gen.getChromosomeElement(17),gen.getChromosomeElement(18)));
		input.addTerm(new Ramp("FAR", gen.getChromosomeElement(19), gen.getChromosomeElement(20)));
		engine.addInputVariable(input);
		
		input = new InputVariable();
		input.setEnabled(true);				
		input.setName("VictimTime");
		input.setRange(0.000, 1.000);				
		input.addTerm(new Ramp("LOW", gen.getChromosomeElement(21),gen.getChromosomeElement(22)));
		input.addTerm(new Triangle("MEDIUM",gen.getChromosomeElement(23),gen.getChromosomeElement(24),gen.getChromosomeElement(25)));
		input.addTerm(new Ramp("HIGH", gen.getChromosomeElement(26), gen.getChromosomeElement(27)));
		engine.addInputVariable(input);
		
		OutputVariable action = new OutputVariable();
		action.setEnabled(true);
		action.setName("action");
		action.setRange(0.000, 1.000);
		action.fuzzyOutput().setAccumulation(new Maximum());
		action.setDefuzzifier(new Centroid(200));
		action.setDefaultValue(Double.NaN);
		action.setLockValidOutput(false);
		action.setLockOutputRange(false);
		
		
		action.addTerm(new Ramp("EAT", gen.getChromosomeElement(28),gen.getChromosomeElement(29)));
		action.addTerm(new Triangle("PEAT", gen.getChromosomeElement(30),gen.getChromosomeElement(31),gen.getChromosomeElement(32)));
		action.addTerm(new Ramp("KILL",gen.getChromosomeElement(33),gen.getChromosomeElement(34)));
		action.addTerm(new Triangle("RUN",gen.getChromosomeElement(35), gen.getChromosomeElement(36),gen.getChromosomeElement(37)));
		engine.addOutputVariable(action);
		
		
		
		RuleBlock rules = new RuleBlock();
		rules.setEnabled(true);
		rules.setName("");
		rules.setConjunction(new Minimum());
		rules.setDisjunction(new Maximum());
		rules.setActivation(new Minimum());
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is NEAR and DistanceToPowerPill is NEAR then action is PEAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is NEAR and DistanceToPowerPill is MEDIUM then action is PEAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is NEAR and DistanceToPowerPill is FAR then action is RUN", engine));
		
		
		
		
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is NEAR and VictimTime is LOW and DistanceToPowerPill is NEAR then action is PEAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is NEAR and VictimTime is LOW and DistanceToPowerPill is FAR then action is RUN", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is NEAR and VictimTime is LOW and DistanceToPowerPill is MEDIUM then action is RUN", engine));
		
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is NEAR and VictimTime is MEDIUM then action is KILL", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is NEAR and VictimTime is HIGH then action is KILL", engine));
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is MEDIUM and VictimTime is LOW and DistanceToPowerPill is NEAR then action is PEAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is MEDIUM and VictimTime is LOW and DistanceToPowerPill is MEDIUM then action is EAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is MEDIUM and VictimTime is LOW and DistanceToPowerPill is FAR then action is EAT", engine));
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is MEDIUM and VictimTime is MEDIUM then action is EAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is MEDIUM and VictimTime is HIGH then action is EAT", engine));
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is FAR and VictimTime is LOW then action is EAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is FAR and VictimTime is MEDIUM then action is EAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is FAR and VictimTime is HIGH then action is EAT", engine));
		
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is FAR and DistanceToVictim  is NEAR and VictimTime is LOW then action is RUN", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is FAR and DistanceToVictim  is NEAR and VictimTime is MEDIUM then action is KILL", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is FAR and DistanceToVictim  is NEAR and VictimTime is HIGH then action is KILL", engine));
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is FAR and DistanceToVictim  is MEDIUM and VictimTime is LOW then action is EAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is FAR and DistanceToVictim  is MEDIUM and VictimTime is MEDIUM then action is EAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is FAR and DistanceToVictim  is MEDIUM and VictimTime is HIGH then action is KILL", engine));
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is FAR and DistanceToVictim  is FAR and VictimTime is LOW then action is EAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is FAR and DistanceToVictim  is FAR and VictimTime is MEDIUM then action is EAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is FAR and DistanceToVictim  is FAR and VictimTime is HIGH then action is EAT", engine));
		
		
		
		engine.addRuleBlock(rules);		
		
	}
	public MOVE getMove(Game game, long timeDue) 
	{
		
		int current=game.getPacmanCurrentNodeIndex();

		
		
		
		double EatMove;
		int distanceVictim = Integer.MAX_VALUE;
		GHOST victim = null;
		int distanceEnemy = Integer.MAX_VALUE;
		GHOST enemy = null;
	
		String strategy="";
		
		for(GHOST ghost : GHOST.values())
		{
			
			int distance = game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost));
			
			//Si es comestible es victma
			if(game.getGhostEdibleTime(ghost) > 0) {
				if(distance > 0 && distance < distanceVictim)
				{
					distanceVictim=distance;
					victim = ghost;
				}
			}
			//Sino es Enemigo
			else {
				if(distance > 0 && distance < distanceEnemy)
				{
					distanceEnemy=distance;
					enemy = ghost;
				}
				
			}
		}
		
		if(enemy == null) {
			distanceEnemy = 150;
			
		}
		
		
		
		
		int[] powerPills=game.getPowerPillIndices();		
		
		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		
		for(int i=0;i<powerPills.length;i++)			//check with power pills are available
			if(game.isPowerPillStillAvailable(i))
				targets.add(powerPills[i]);				
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
			targetsArray[i]=targets.get(i);
		
		//return the next direction once the closest target has been identified
		int closestPP = game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH);
		
		if(closestPP > 0)
			engine.setInputValue("DistanceToPowerPill", DataTuple.normalizeDistanceStatic((int) game.getDistance(current, closestPP, DM.PATH)));
		
		else
			engine.setInputValue("DistanceToPowerPill",1);
		
		engine.setInputValue("DistanceToEnemy", DataTuple.normalizeDistanceStatic(distanceEnemy));
		
		
		if(victim != null) {
			engine.setInputValue("VictimTime", game.getGhostEdibleTime(victim));
			engine.setInputValue("DistanceToVictim", DataTuple.normalizeDistanceStatic(distanceVictim));
			
		}
		else {
			engine.setInputValue("VictimTime", 0);
			engine.setInputValue("DistanceToVictim", 1);
			
		}
		
		engine.process();
		OutputVariable output= engine.getOutputVariable("action");
		
		
		strategy=getStrategy(output);
		if(strategy.equals("EAT"))
			{
			myMove=MoveToClosestPill(game);
			}
		else if(strategy.equals("RUN"))
			{
			if(enemy != null) {				
				
				myMove=game.getApproximateNextMoveAwayFromTarget(current,game.getGhostCurrentNodeIndex(enemy),game.getPacmanLastMoveMade(), DM.PATH);
				
			}
			else
				myMove=MOVE.NEUTRAL;
			}
		else if (strategy.equals("KIL"))
			{
				if(victim != null)
					myMove=MoveToClosestGhost(game,victim);
				else
					myMove=MOVE.NEUTRAL;
			}
		else if (strategy.equals("PEA"))
			{
			if(closestPP == -1)
				myMove = MOVE.NEUTRAL;
			else
				myMove= game.getNextMoveTowardsTarget(current, closestPP, DM.PATH);
			}	
		else 
			myMove=MOVE.NEUTRAL;
		//System.out.println(output.fuzzyOutput());
		//System.out.println(getStrategy(output));
		
		//System.out.println();

		return myMove;
	}
	
	
	
	
	
	public MOVE MoveToClosestGhost(Game game,GHOST ghost)
	{
		int current=game.getPacmanCurrentNodeIndex();
		return game.getNextMoveTowardsTarget(current,game.getGhostCurrentNodeIndex(ghost) , DM.PATH);
	}
	
	public MOVE MoveToClosestPill(Game game)
	{
		int current=game.getPacmanCurrentNodeIndex();
		int[] pills=game.getPillIndices();
		//int[] powerPills=game.getPowerPillIndices();		
		
		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		
		for(int i=0;i<pills.length;i++)					//check which pills are available			
			if(game.isPillStillAvailable(i))
				targets.add(pills[i]);
		/*
		for(int i=0;i<powerPills.length;i++)			//check with power pills are available
			if(game.isPowerPillStillAvailable(i))
				targets.add(powerPills[i]);				
		*/
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
			targetsArray[i]=targets.get(i);
		
		//return the next direction once the closest target has been identified
		return game.getNextMoveTowardsTarget(current,game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH),DM.PATH);
	}
	public String getStrategy(OutputVariable output)
	{
		String strategy="",strategyAux;
		strategyAux=output.fuzzyOutput().toString();
		try {
		Character.toString(strategyAux.charAt(89));
		}catch (StringIndexOutOfBoundsException e)
		{
			//System.out.println("Nope");
			return strategy;
		}
		for(int i=0;i<3;i++)
		{
			strategy+=strategyAux.charAt(i+89);
		}
		//System.out.println(strategy);
		return strategy;
	}
}