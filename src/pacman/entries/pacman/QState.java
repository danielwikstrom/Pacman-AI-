package pacman.entries.pacman;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;


public class QState {
	

	
	public enum Distance
	{
		NEAR,MEDIUM,FAR
	}
	public enum Time
	{
		NONE,LOW, MEDIUM, HIGH
	}
	
	//distance to closest ghost
	private Distance distanceToGhost; 
	
	//closest PowerPill
	private Distance distanceToPowerPill;
	
	//closest normal pill
	private Distance distanceToClosestPill;
	
	//closest ghost edible time
	private Time edibleTime;
	
	public QState(Distance distanceToGhost,Distance distanceToPowerPill,Distance distanceToClosestPill,Time edibleTime)
	{
		this.distanceToGhost=distanceToGhost;
		this.distanceToPowerPill=distanceToPowerPill;
		this.distanceToClosestPill=distanceToClosestPill;
		this.edibleTime=edibleTime;
	}
	public QState(Game game)
	{
		
		int current=game.getPacmanCurrentNodeIndex();
		
		//distance to the closest ghost
		GHOST ghost=GameInfo.getClosestGhost(game);
		if(ghost!=null)
		{
			int ghostNode=game.getGhostCurrentNodeIndex(ghost);
			this.distanceToGhost=getDistance(game,current,ghostNode);
			
			//closest ghost edible time
			this.edibleTime=setEdibleTime(game,ghost);	
		}
		else 
			{
			this.distanceToGhost=Distance.FAR;
			this.edibleTime=Time.NONE;
			}
		//distance to the closest power pill
		int powerPillNode = GameInfo.getClosestPowerPill(game);
		if (powerPillNode >=0)
			this.distanceToPowerPill=getDistance(game,current,powerPillNode);
		else this.distanceToPowerPill=Distance.FAR;
		
		
		
		//distance to the closest pill
		int closestPillNode=GameInfo.getClosestPill(game);
		this.distanceToClosestPill=getDistance(game,current,closestPillNode);
	}
	
	public double obtainReward(Game game)
	{
		double reward=0;
		if(GameInfo.wasAnyGhostEaten(game))
			reward-=1;
		if(game.wasPillEaten())
			reward+=5;
		if(game.wasPowerPillEaten())
			reward-=1;
		if(game.wasPacManEaten())
			reward-=10;
		if(game.gameOver())
			reward-=10;
		return reward;
	}
	
	private Distance getDistance(Game game, int current,int target)
	{
		Distance dist;
		double distance=game.getDistance(current, target, DM.PATH);
		if(distance<40)
			dist=Distance.NEAR;
		else if(distance <100)
			dist=Distance.MEDIUM;
		else dist=Distance.FAR;
		
		return dist;
	}
	
	private Time setEdibleTime(Game game, GHOST ghost)
	{
		Time edibleTime;
		double time = game.getGhostEdibleTime(ghost);
		if(time==0)
			edibleTime=Time.NONE;
		else if(time < 40)
			edibleTime=Time.LOW;
		else if(time <120)
			edibleTime=Time.MEDIUM;
		else edibleTime=Time.HIGH;
		return edibleTime;
	}
	
	
	public String ToString()
	{
		String hash="";
		hash+= this.distanceToClosestPill.toString();
		hash+= this.distanceToGhost.toString();
		hash+= this.distanceToPowerPill.toString();
		hash+=this.edibleTime.toString();
		return hash;
	}
	@Override
	public int hashCode() {
		
		this.ToString().hashCode();
		return 0;
		
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if(!(obj instanceof QState))
			return false;
		QState state= (QState) obj;
		if(this.distanceToClosestPill == state.distanceToClosestPill && this.distanceToGhost==state.distanceToGhost
				&& this.distanceToPowerPill==state.distanceToPowerPill && this.edibleTime==state.edibleTime)
			return true;
		return false;
	}
	
	public void printState()
	{
		System.out.println(this.distanceToGhost + " " + this.distanceToPowerPill + " " 
				+ this.distanceToClosestPill + " " + this.edibleTime);
	}
}
