package pacman.entries.pacman;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import pacman.entries.pacman.QAction.Strategy;
import pacman.entries.pacman.QState.Distance;
import pacman.entries.pacman.QState.Time;
import pacman.game.Game;
public class QLearning {
	private HashMap<QState,ArrayList<QAction>> QMap;
	private ArrayList<QAction> actions;
	private double initValue=999.0;
	private double gamma=0.9;
	private double alpha=0.001;
	private double minValueInit=-99999.0;
	//value used for the eGreedy policy
	private double epsilon=0.8;
	public QLearning()
	{
		
		initTable();
	}
	
	public void initTable()
	{
		
		QMap = new HashMap<QState,ArrayList<QAction>>();
		
		Distance[] distances= {Distance.NEAR,Distance.MEDIUM,Distance.FAR};
		Time[] times= {Time.NONE,Time.LOW,Time.MEDIUM,Time.HIGH};
		
		QState state;
		Distance ghostDist;
		Distance powerPillDist;
		Distance pillDist;
		Time edibleTime;
		System.out.println("generando tabla de estados y acciones");
		for(int i =0;i<distances.length;i++)
		{
			ghostDist=distances[i];
			for(int j =0;j<distances.length;j++)
			{
				powerPillDist=distances[j];
				for(int k =0;k<distances.length;k++)
				{
					pillDist=distances[k];
					for(int l=0;l<times.length;l++)
					{
						edibleTime=times[l];
						state = new QState(ghostDist,powerPillDist,pillDist,edibleTime);
						actions = new ArrayList<QAction>();
						actions.add(new QAction(Strategy.EAT,initValue));
						actions.add(new QAction(Strategy.PEAT,initValue));
						actions.add(new QAction(Strategy.KILL,initValue));
						actions.add(new QAction(Strategy.RUN,initValue));
						QMap.put(state, actions);
					}
				}
			}
		}
	}
	
	//The next action is chosen based on the policy e-greedy
	public QAction chooseAction(QState state)
	{
		Random rand= new Random();
		Double random=rand.nextDouble();
		ArrayList<QAction> posibleActions;
		posibleActions=QMap.get(state);
		QAction nextAction=null;
		
		if(random<=epsilon)
		{
			nextAction=getBestAction(posibleActions);
			return nextAction;
		}
		int randomAction= ThreadLocalRandom.current().nextInt(0,posibleActions.size());
		nextAction=posibleActions.get(randomAction);
		return nextAction;
		
	}
	
	public QAction getBestAction(ArrayList<QAction> posibleActions)
	{
		Double maxValue= minValueInit;
		QAction bestAction=null;
		for (int i=0; i<posibleActions.size();i++)
		{
			Double currentValue=posibleActions.get(i).getValue();
			if(currentValue>maxValue)
			{
				maxValue=currentValue;
				bestAction=posibleActions.get(i);
			}
			
		}
		return bestAction;
		
	}
	
	public void calculateQsa(double reward,QAction action,QState previousState,QState currentState)
	{
		ArrayList<QAction> actions= QMap.get(previousState);
		ArrayList<QAction> currentActions= QMap.get(currentState); 
		Double qsa=0.0;
		for(int i=0;i<actions.size();i++)
		{
			if(action.getStrategy()==actions.get(i).getStrategy())
			{
				//Q(s,a) formula
				qsa=actions.get(i).getValue() + alpha*(reward+(epsilon*getBestAction(currentActions).getValue()-actions.get(i).getValue()));
				actions.get(i).setValue(qsa);
				QMap.put(previousState, actions);
				
				return;
			}
		}
		
		
	}
	
	public void isNotLearning()
	{
		this.epsilon=1.0;
	}
	
	public void printTable()
	{
				System.out.println("-----------Tabla------------");
				for(int i =0;i<QMap.values().size();i++)
					for(int j=0; j<4;j++)
					System.out.println(QMap.values().iterator().next().get(j).getValue());
				System.out.println("-----------FinDeTabla------------");

	}
	
	public void printTuple(QState s)
	{
		s.printState();
		for (int i=0; i< QMap.get(s).size();i++)
			System.out.println("     "+ QMap.get(s).get(i).getValue());
		
	}
}
