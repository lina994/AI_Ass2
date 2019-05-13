import java.util.Comparator;
import java.util.LinkedList;

public abstract class GameAgent extends Agent {

    StateNode head;
    Move nextMove;
    int numOfExpands; // for this round

    Comparator<StateNode> stateNodeComparatorMinToMax = new Comparator<StateNode>() {
        @Override
        public int compare(StateNode o1, StateNode o2) {
            if (o2.heuristic < o1.heuristic)
                return 1;
            if (o2.heuristic > o1.heuristic)
                return -1;
            return 0;
        }
    };

    Comparator<StateNode> stateNodeComparatorMaxToMin = new Comparator<StateNode>() {
        @Override
        public int compare(StateNode o1, StateNode o2) {
            if (o2.heuristic < o1.heuristic)
                return -1;
            if (o2.heuristic > o1.heuristic)
                return 1;
            return 0;
        }
    };

    public GameAgent(int agentTypes, int positions, int peopleInCar) {
        super(agentTypes, positions, peopleInCar);
    }

    /*
     * From the node with max depth choose the one that have min heuristic Check the
     * best node according to agent type
     */
    abstract double calcHeuristic(StateNode node);

    abstract void sortChildren(LinkedList<StateNode> children, StateNode parent);

    abstract void updateParentsScore(StateNode parent, StateNode child);

    abstract boolean cutTree(StateNode child, StateNode parent);

    public int calculateTree(int position) { // return next position
        int enemyIndex;

        if (Main.agents[0] == this)
            enemyIndex = 1;
        else
            enemyIndex = 0;

        int enemyPosition = Main.agents[enemyIndex].position;
        int enemyPeopleInCar = Main.agents[enemyIndex].peopleInCar;
        int enemyScore = Main.agents[enemyIndex].score;

        head = new StateNode(position, enemyPosition, Main.peopleToSave.clone(), peopleInCar, enemyPeopleInCar, score,
                enemyScore, Main.time, 0, /* heuristic */
                null, /* prev */
                null, /* next */
                new LinkedList<StateNode>(), /* children */
                0);
        head.heuristic = calcHeuristic(head);
        evaluateSubTree(head);
        return head.next.agentPosition;
    }

    /* calculate state of children */
    private StateNode evalNextNode(StateNode parent, int nextPosition, boolean isAgent) {
        int agentPosition = parent.agentPosition;
        int opponentPosition = parent.opponentPosition;
        int[] peopleArr = parent.peopleArr.clone();
        int peopleInAgentCar = parent.peopleInAgentCar;
        int peopleInOpponentCar = parent.peopleInOpponentCar;
        int agentScore = parent.agentScore;
        int opponentScore = parent.opponentScore;
        double time = parent.time;
        StateNode prev = parent;
        StateNode next = null;
        LinkedList<StateNode> childern = new LinkedList<StateNode>();
        int depth = parent.depth + 1;

        if (isAgent) {
            agentPosition = nextPosition;

            if (parent.agentPosition == nextPosition)
                time++;
            else {
                double vertexWeight = Main.vertexMatrix[parent.agentPosition][nextPosition]
                        + Main.vertexMatrix[nextPosition][parent.agentPosition];
                time += cTime(vertexWeight, parent.peopleInAgentCar, Main.k_parameter);
            }
            if (peopleArr[nextPosition] > 0) {
                peopleInAgentCar += peopleArr[nextPosition];
                peopleArr[nextPosition] = 0;
            }
            if (Main.shelters[nextPosition] && time <= Main.deadline) {
                agentScore += peopleInAgentCar;
                peopleInAgentCar = 0;
            }

        } else {
            opponentPosition = nextPosition;
            if (parent.opponentPosition == nextPosition)
                time++;
            else {
                double vertexWeight = Main.vertexMatrix[parent.opponentPosition][nextPosition]
                        + Main.vertexMatrix[nextPosition][parent.opponentPosition];
                time += cTime(vertexWeight, parent.peopleInOpponentCar, Main.k_parameter);
            }
            if (peopleArr[nextPosition] > 0) {
                peopleInOpponentCar += peopleArr[nextPosition];
                peopleArr[nextPosition] = 0;
            }
            if (Main.shelters[nextPosition] && time <= Main.deadline) {
                opponentScore += peopleInOpponentCar;
                peopleInOpponentCar = 0;
            }
        }

        StateNode res = new StateNode(agentPosition, opponentPosition, peopleArr, peopleInAgentCar, peopleInOpponentCar,
                agentScore, opponentScore, time, /* heuristic */0, prev, next, childern, depth);

        res.heuristic = calcHeuristic(res);
        return res;
    }

    // return sub tree result
    private void evaluateSubTree(StateNode parent) {
        StateNode temp;
        double timeForDeadline = Main.deadline - parent.time;

        if (timeForDeadline <= 0) { // local deadline in tree
            if (parent.prev != null)
                updateParentsScore(parent.prev, parent);
            return;
        }

        /*
         * if depth is even then calc agent move if depth is odd then calc opponent move
         */
        int position;
        if (parent.depth % 2 == 0)
            position = parent.agentPosition; // agent
        else
            position = parent.opponentPosition; // opponent

        /* start of add vertex */
        for (int i = 0; i < position; i++) {
            if (Main.vertexMatrix[i][position] > 0) {
                if (parent.depth % 2 == 0)
                    temp = evalNextNode(parent, i, true); // agent
                else
                    temp = evalNextNode(parent, i, false); // opponent

                parent.childern.add(temp);
            }
        }

        for (int i = position + 1; i < Main.vertexMatrix.length; i++) {
            if (Main.vertexMatrix[position][i] > 0) {
                if (parent.depth % 2 == 0)
                    temp = evalNextNode(parent, i, true); // agent
                else
                    temp = evalNextNode(parent, i, false); // opponent
                parent.childern.add(temp);
            }
        }

        // add no-op
        if (parent.depth % 2 == 0)
            temp = evalNextNode(parent, position, true); // agent
        else
            temp = evalNextNode(parent, position, false); // opponent
        parent.childern.add(temp);

        /* end of add vertex */

        sortChildren(parent.childern, parent);

        // start eval children
        for (StateNode n : parent.childern) {

            if (!cutTree(n, parent)) { // try early cut tree
                evaluateSubTree(n);

            }
        }
        if (parent.prev != null)
            updateParentsScore(parent.prev, parent);

        return;
    }

    @Override
    public double timeForNextAction() {
        nextMove = new Move(0, 0);
        nextMove.nextPosition = calculateTree(position);
        if (nextMove.nextPosition == position)
            nextMove.timeForAction = 1;
        else
            nextMove.timeForAction = calcW(nextMove.nextPosition, position, peopleInCar);
        return nextMove.timeForAction;
    }

    @Override
    public int doAction() {
        position = nextMove.nextPosition;
        peopleInCar += Main.peopleToSave[position];
        Main.peopleToSave[position] = 0;
        if (Main.shelters[position]) {
            score += peopleInCar;
            peopleInCar = 0;
        }
        return position;
    }

    public double cTime(double w, int p, double k) {
        return w * (p * k + 1);
    }

    private double calcW(int i, int j, int people) {
        double edge = Main.vertexMatrix[i][j] + Main.vertexMatrix[j][i];
        return edge * (1 + Main.k_parameter * people);
    }

}

class StateNode {
    int agentPosition; // current position
    int opponentPosition;
    int[] peopleArr; // people arr
    int peopleInAgentCar;
    int peopleInOpponentCar;
    int agentScore;
    int opponentScore;
    double time;
    double heuristic;
    StateNode prev;
    StateNode next;
    LinkedList<StateNode> childern;
    int depth;
    double oppHeuristic = 0;

    public StateNode(int agentPosition, int opponentPosition, int[] peopleArr, int peopleInAgentCar,
            int peopleInOpponentCar, int agentScore, int opponentScore, double time, double heuristic, StateNode prev,
            StateNode next, LinkedList<StateNode> children, int depth) {
        super();
        this.agentPosition = agentPosition;
        this.opponentPosition = opponentPosition;
        this.peopleArr = peopleArr;
        this.peopleInAgentCar = peopleInAgentCar;
        this.peopleInOpponentCar = peopleInOpponentCar;
        this.agentScore = agentScore;
        this.opponentScore = opponentScore;
        this.time = time;
        this.heuristic = heuristic;
        this.prev = prev;
        this.next = next;
        this.childern = children;
        this.depth = depth;
    }
}

class Move {
    double timeForAction;
    int nextPosition;

    public Move(double timeForAction, int nextPosition) {
        this.timeForAction = timeForAction;
        this.nextPosition = nextPosition;
    }
}

class State {
    int vertex;
    int[] peopleArr;
    int peopleInCar;
    double time;
    double heuristic;
    int prev;

    public State(int vertex, int[] peopleArr, int peopleInCar, double time, double heuristic, int prev) {
        super();
        this.vertex = vertex;
        this.peopleArr = peopleArr;
        this.peopleInCar = peopleInCar;
        this.time = time;
        this.heuristic = heuristic;
        this.prev = prev;
    }
}
