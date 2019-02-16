import java.util.Comparator;
import java.util.LinkedList;


public class GameSemiCooperativeAgent extends GameAgent {
	
    Comparator<StateNode> semiStateNodeComparatorMinToMax = new Comparator<StateNode>() {
        @Override
        public int compare(StateNode o1, StateNode o2) {
            if (o2.heuristic < o1.heuristic) return 1;
            if (o2.heuristic > o1.heuristic) return -1;
            if (o2.oppHeuristic < o1.oppHeuristic) return 1;
            if (o2.oppHeuristic > o1.oppHeuristic) return -1;
            return 0;
        }
    };
    
    Comparator<StateNode> semiStateNodeComparatorMinToMaxOppH = new Comparator<StateNode>() {
        @Override
        public int compare(StateNode o1, StateNode o2) {
            if (o2.oppHeuristic < o1.oppHeuristic) return 1;
            if (o2.oppHeuristic > o1.oppHeuristic) return -1;
            if (o2.heuristic < o1.heuristic) return 1;
            if (o2.heuristic > o1.heuristic) return -1;
            return 0;
        }
    };
	
	public GameSemiCooperativeAgent(int agentTypes, int positions, int peopleInCar) {
		super(agentTypes, positions, peopleInCar);
	}


	
	@Override
	double calcHeuristic(StateNode node) {
		double h1=HeuristicCalculator.calculateGameF(node,Main.mostHeavyEdge, true);
		node.oppHeuristic=HeuristicCalculator.calculateGameF(node,Main.mostHeavyEdge, false);
		
		return h1;
	}

	
	@Override
	void sortChildren(LinkedList<StateNode> children, StateNode parent) {
		
		if(parent.depth % 2 == 0)
			parent.childern.sort(semiStateNodeComparatorMinToMax);
		else
			parent.childern.sort(semiStateNodeComparatorMinToMaxOppH);

	}


	@Override
	void updateParentsScore(StateNode parent, StateNode child) {
		
		if(parent.depth % 2 == 0) { // agent
			if(child.agentScore>parent.agentScore || (child.agentScore==parent.agentScore && child.opponentScore>parent.opponentScore) || parent.next==null) {
				
				parent.agentScore = child.agentScore;
				parent.opponentScore = child.opponentScore;
				parent.next=child;
			}
		}		
	    else { // enemy
	    	if(child.opponentScore>parent.opponentScore || (child.opponentScore==parent.opponentScore && child.agentScore>parent.agentScore) || parent.next==null) {
			parent.agentScore = child.agentScore;
			parent.opponentScore = child.opponentScore;
			parent.next=child;
	    	}
	    }  
	}


	@Override
	boolean cutTree(StateNode child, StateNode parent) {

		if(parent.next==null)
			return false;
		
		double bestH;
		double secondH;
		
		if(parent.depth%2==0) {
			bestH = (Main.totalNumOfPeople-parent.agentScore)*Main.mostHeavyEdge;
			secondH = (Main.totalNumOfPeople-parent.opponentScore)*Main.mostHeavyEdge;
		}
		else {
			bestH = (Main.totalNumOfPeople-parent.opponentScore)*Main.mostHeavyEdge;
			secondH = (Main.totalNumOfPeople-parent.agentScore)*Main.mostHeavyEdge;
		}
		
		if(parent.depth%2==0) {
			if(bestH<child.heuristic) {
				return true; 
			}
			if(bestH==child.heuristic && secondH <= child.oppHeuristic) {
				return true; 
			}
		}
		else {
			if(bestH<child.oppHeuristic) {
				return true; 
			}
			if(bestH==child.oppHeuristic && secondH <= child.heuristic) {
				return true; 
			}
		}
		
		return false;
	}

}
