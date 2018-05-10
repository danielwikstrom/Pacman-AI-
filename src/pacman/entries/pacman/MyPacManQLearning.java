package pacman.entries.pacman;

import java.util.ArrayList;

import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.entries.pacman.QAction.Strategy;
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
public class MyPacManQLearning extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	
	public int cd = 0;
	public int totalReward=0;
	public QLearning qLearn;
	private QState currentState=null;
	private QState previousState=null;
	private QAction action=null;
	private double reward=0.0;
	private boolean isLearning=true;
	private GHOST currentFleeingFrom;
	
	public MyPacManQLearning() {
	
	qLearn= new QLearning();
	
	
	}
	public MOVE getMove(Game game, long timeDue) 
	{
		/*
		if(cd>0 && this.currentFleeingFrom!=null)
		{
			cd--;
			myMove=game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(this.currentFleeingFrom), DM.PATH);
			return myMove;
		}
		*/
		
		int current=game.getPacmanCurrentNodeIndex();
		if(isLearning)
		{
			
			double reward;
			currentState = new QState(game);
			if(previousState==null)
			{
				previousState=currentState;
				action=qLearn.chooseAction(currentState);
				myMove=action.getNextMoveFromAction(game);
				
				return myMove;
			}
			reward=currentState.obtainReward(game);
			qLearn.calculateQsa(reward, action, previousState, currentState);
			action=qLearn.chooseAction(currentState);
			myMove=action.getNextMoveFromAction(game);
			//qLearn.printTuple(currentState);
			//System.out.println(action.getStrategy());
			/*
			if(action.getStrategy()==Strategy.RUN)
			{
				this.currentFleeingFrom=action.getGhostRunningFrom();
				cd=4;
			}
			*/
			previousState=currentState;
			return myMove;
		}
		else
			currentState = new QState(game);
			action=qLearn.chooseAction(currentState);
			myMove=action.getNextMoveFromAction(game);
			System.out.println(action.getStrategy());
			/*
			if(action.getStrategy()==Strategy.RUN)
			{
				this.currentFleeingFrom=action.getGhostRunningFrom();
				cd=4;
			}
			*/
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
	public int getClosestPowerPill(Game game)
	{
		int current=game.getPacmanCurrentNodeIndex();
		int[] powerPills=game.getPowerPillIndices();		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		for(int i=0;i<powerPills.length;i++)			//check wich power pills are available
			if(game.isPowerPillStillAvailable(i))
				targets.add(powerPills[i]);				
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		for(int i=0;i<targetsArray.length;i++)
			targetsArray[i]=targets.get(i);
		
		//return the next direction once the closest target has been identified
		int closestPP = game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH);
		return closestPP;
	}
	
	public void notLearning()
	{
		this.isLearning=false;
		qLearn.isNotLearning();
		
	}
	
}