import java.util.LinkedList;

public class GameFullyCooperativeAgent extends GameAgent {

    public GameFullyCooperativeAgent(int agentTypes, int positions, int peopleInCar) {
        super(agentTypes, positions, peopleInCar);
    }

    /* h=num of people that both agent can`t to evacuate */
    @Override
    double calcHeuristic(StateNode node) {
        return HeuristicCalculator.calculateFullCooperativeF(node, Main.mostHeavyEdge);
    }

    @Override
    void sortChildren(LinkedList<StateNode> children, StateNode parent) {
        parent.childern.sort(stateNodeComparatorMinToMax);
    }

    @Override
    void updateParentsScore(StateNode parent, StateNode child) {
        if (parent.agentScore + parent.opponentScore < child.agentScore + child.opponentScore || parent.next == null) {
            parent.agentScore = child.agentScore;
            parent.opponentScore = child.opponentScore;
            parent.next = child;
        }
    }

    @Override
    /* (totalPeople-score)*100>h */
    boolean cutTree(StateNode child, StateNode parent) {

        if (parent.next == null)
            return false;

        int peopleInDanger = 0;
        int wasSaved = parent.agentScore + parent.opponentScore - child.agentScore - child.opponentScore;
        int wasNotSaved;
        double bestH;

        for (int i = 0; i < parent.peopleArr.length; i++)
            peopleInDanger += parent.peopleArr[i];

        peopleInDanger += parent.peopleInAgentCar;
        peopleInDanger += parent.peopleInOpponentCar;

        wasNotSaved = peopleInDanger - wasSaved;
        bestH = wasNotSaved * Main.mostHeavyEdge;

        if (bestH <= child.heuristic)
            return true;
        return false;
    }

}
