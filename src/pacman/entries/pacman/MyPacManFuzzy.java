package pacman.entries.pacman;

import java.util.ArrayList;

import com.fuzzylite.Engine;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.AlgebraicProduct;
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

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacManFuzzy extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	Engine engine = null;
	public MyPacManFuzzy() {
		
		
		
		engine = new Engine();
		engine.setName("Fuzzy-Pacman");

		InputVariable input = new InputVariable();
		input.setEnabled(true);
		input.setName("DistanceToEnemy");
		input.setRange(0.000, 1.000);
		input.addTerm(new Ramp("NEAR", 0.33,0.000 ));
		input.addTerm(new Triangle("MEDIUM",0.33,0.500,0.66));
		input.addTerm(new Ramp("FAR", 0.66, 1));
		engine.addInputVariable(input);
		
		input = new InputVariable();
		input.setEnabled(true);
		input.setName("DistanceToVictim");
		input.setRange(0.000, 1.000);
		input.addTerm(new Ramp("NEAR", 0.33,0.000 ));
		input.addTerm(new Triangle("MEDIUM",0.100,0.500,0.750));
		input.addTerm(new Ramp("FAR", 0.700, 1));
		engine.addInputVariable(input);
		
		input = new InputVariable();
		input.setEnabled(true);
		input.setName("DistanceToPowerPill");
		input.setRange(0.000, 1.000);
		input.addTerm(new Ramp("NEAR", 0.33,0.000 ));
		input.addTerm(new Triangle("MEDIUM",0.100,0.500,0.750));
		input.addTerm(new Ramp("FAR", 0.700, 1));
		engine.addInputVariable(input);
		
		input = new InputVariable();
		input.setEnabled(true);				
		input.setName("VictimTime");
		input.setRange(0.000, 1.000);				
		input.addTerm(new Ramp("LOW", 0.300,0.000));
		input.addTerm(new Triangle("MEDIUM",0.250,0.500,0.750));
		input.addTerm(new Ramp("HIGH", 0.700, 1.000));
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
		
		
		action.addTerm(new Ramp("EAT", 0.330,0.000));
		action.addTerm(new Triangle("PEAT", 0.400, 0.500, 0.66));
		action.addTerm(new Ramp("KILL",0.66,1.000));
		action.addTerm(new Triangle("RUN",0.33, 0.40,0.55));
		engine.addOutputVariable(action);
		
		
		
		RuleBlock rules = new RuleBlock();
		rules.setEnabled(true);
		rules.setName("");
		rules.setConjunction(new AlgebraicProduct());
		rules.setDisjunction(new  Maximum());
		rules.setActivation(new Minimum());
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is NEAR and DistanceToPowerPill is NEAR then action is PEAT", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is NEAR and DistanceToPowerPill is MEDIUM then action is RUN", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is NEAR and DistanceToPowerPill is FAR then action is RUN", engine));
		
		
		
		
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is NEAR and VictimTime is LOW and DistanceToPowerPill is NEAR then action is RUN", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is NEAR and VictimTime is LOW and DistanceToPowerPill is FAR then action is RUN", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is NEAR and VictimTime is LOW and DistanceToPowerPill is MEDIUM then action is RUN", engine));
		
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is NEAR and VictimTime is MEDIUM then action is KILL", engine));
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is NEAR and VictimTime is HIGH then action is KILL", engine));
		
		rules.addRule(Rule.parse("if DistanceToEnemy  is MEDIUM and DistanceToVictim  is MEDIUM and VictimTime is LOW and DistanceToPowerPill is NEAR then action is EAT", engine));
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
			myMove= game.getNextMoveTowardsTarget(current, closestPP, DM.PATH);
			}	
		else 
			myMove=MOVE.NEUTRAL;

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