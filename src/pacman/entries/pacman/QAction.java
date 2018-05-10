package pacman.entries.pacman;

import java.util.Random;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Ghost;

public class QAction {
	
	public enum Strategy
	{
		EAT,PEAT,KILL,RUN
	}
	private Strategy strategy;
	
	//Value calculated from the Q(s,a) function
	private Double QValue;
	private GHOST currentGhost;
	public QAction(Strategy strategy, Double QValue)
	{
		this.strategy=strategy;
		this.QValue=QValue;
	}
	
	public MOVE getNextMoveFromAction(Game game)
	{
		MOVE myMove;
		int current=game.getPacmanCurrentNodeIndex();
		switch (strategy)
		{
		case EAT:
			myMove=game.getNextMoveTowardsTarget(current, GameInfo.getClosestPill(game), DM.PATH);
			//System.out.println("EAT");
			break;
		case PEAT:
			if(GameInfo.getClosestPowerPill(game)>=0)
				myMove=game.getNextMoveTowardsTarget(current, GameInfo.getClosestPowerPill(game), DM.PATH);
			else
				myMove=MOVE.NEUTRAL;
			//System.out.println("PEAT");
			break;
		case KILL:
			if(GameInfo.getClosestGhost(game)!=null)
			{
				myMove=game.getNextMoveTowardsTarget(current,game.getGhostCurrentNodeIndex(GameInfo.getClosestGhost(game)) , DM.PATH);
			}
			else
				myMove=MOVE.NEUTRAL;	
			//System.out.println("KILL");
			break;
		case RUN:
			if(GameInfo.getClosestGhost(game)!=null)
			{
				myMove=game.getNextMoveAwayFromTarget(current, game.getGhostCurrentNodeIndex(GameInfo.getClosestGhost(game)), DM.PATH);
				this.currentGhost=GameInfo.getClosestGhost(game);
				
			}
			else
				myMove=MOVE.NEUTRAL;
			//System.out.println("RUN");
			break;
		default:
			myMove=MOVE.NEUTRAL;
			break;
		}
		
		return myMove;
	}
	
	public double getValue()
	{
		return this.QValue;
	}
	public Strategy getStrategy()
	{
		return this.strategy;
	}
	
	public void setValue(Double newValue)
	{
		this.QValue=newValue;
	}
	
	public MOVE getRandomMode()
	{
		return null;
	}
	public GHOST getGhostRunningFrom()
	{
		return this.currentGhost;
	}
	
}
