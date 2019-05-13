import java.util.LinkedList;

public class AgentMinMax extends GameAgent {

    public AgentMinMax(int agentTypes, int positions, int peopleInCar) {
        super(agentTypes, positions, peopleInCar);
    }

    @Override
    double calcHeuristic(StateNode node) {
        double h1 = HeuristicCalculator.calculateGameF(node, Main.mostHeavyEdge, true);
        double h2 = HeuristicCalculator.calculateGameF(node, Main.mostHeavyEdge, false);
        return h1 - h2;
    }

    @Override
    void sortChildren(LinkedList<StateNode> children, StateNode parent) {
        if (parent.depth % 2 == 0) { // for agent
            parent.childern.sort(stateNodeComparatorMinToMax);
        } else { // for opponent
            parent.childern.sort(stateNodeComparatorMaxToMin);
        }
    }

    @Override
    void updateParentsScore(StateNode parent, StateNode child) {
        if (parent.next == null) {
            doUpdate(parent, child);
        } else if (parent.depth % 2 == 0) { // agent
            if ((child.agentScore - child.opponentScore) > (parent.agentScore - parent.opponentScore)) {
                doUpdate(parent, child);
            }
        } else { // opponent
            if ((child.opponentScore - child.agentScore) > (parent.opponentScore - parent.agentScore)) {
                doUpdate(parent, child);
            }
        }
    }

    private void doUpdate(StateNode parent, StateNode child) {
        parent.agentScore = child.agentScore;
        parent.opponentScore = child.opponentScore;
        parent.next = child;
    }

    boolean cutTree(StateNode child, StateNode parent) {

        if (parent.next == null)
            return false;

        if (parent.depth % 2 == 0) { // parent is agent, child is opponent
            if (parent.prev != null) {
                int pDiff = parent.agentScore - parent.opponentScore;
                int prevDiff = parent.prev.agentScore - parent.prev.opponentScore;
                if (pDiff > prevDiff)
                    return true;
            }
        } else { // parent is opponent, child is agent
            if (parent.prev != null) {
                int pDiff = parent.agentScore - parent.opponentScore;
                int prevDiff = parent.prev.agentScore - parent.prev.opponentScore;
                if (pDiff < prevDiff)
                    return true;
            }
        }
        return false;
    }

}
