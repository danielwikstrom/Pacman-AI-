package pacman.entries.pacman;
import java.util.ArrayList;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
public class GameInfo {
	
	public static GHOST getClosestGhost(Game game)
	{
		int distanceEnemy=150;
		GHOST enemy=null;
		for(GHOST ghost : GHOST.values())
			{
			
			int distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost));
				if(distance > 0 && distance < distanceEnemy)
				{
					distanceEnemy=distance;
					enemy=ghost;
				}
				
			}
		return enemy;
	}
		
	
	public static int getClosestPowerPill(Game game)
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
	
	
	public static int getClosestPill(Game game)
	{
		int current=game.getPacmanCurrentNodeIndex();
		int[] pills=game.getPillIndices();		
		int closestPillNode;
		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		
		for(int i=0;i<pills.length;i++)					//check which pills are available			
			if(game.isPillStillAvailable(i))
				targets.add(pills[i]);
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
			targetsArray[i]=targets.get(i);
		
		//return the next direction once the closest target has been identified
		closestPillNode=game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH);
		return closestPillNode;
	}
	
	public static boolean wasAnyGhostEaten(Game game)
	{
		for(GHOST ghost : GHOST.values())
		{
			if(game.wasGhostEaten(ghost))
				return true;
		}
		return false;
	}
}

