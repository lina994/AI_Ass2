public class HeuristicCalculator {

	public static double calculateF(int agentIndex, int agentPosition,int[] people, int numOfPeopleInCar,double time, double personWeight, boolean type) {
		double result;
		result=calculateHeuristic(agentIndex, agentPosition,people, numOfPeopleInCar,time, personWeight);
		if(!type)return result;
		return result+time;
	}
	
	public static double calculateGameF(StateNode node, double personWeight, boolean isAgent) {
		double result;
		result=calculateGameHeuristic(node, personWeight, isAgent);
		return result+node.time;
	}
	
	public static double calculateFullCooperativeF(StateNode node, double personWeight) {
		double result;
		result=calculateFullCooperativeHeuristic(node, personWeight);
		return result+node.time;
	}
	
	private static double calculateHeuristic(int agentIndex, int agentPosition,int[] people, int numOfPeopleInCar,double time, double personWeight) {
		double result = 0;
		for (int i = 0; i < people.length; i++) {
			if(people[i]>0) {
				double timeToEvacuate=calculateTime(agentPosition ,i, people, numOfPeopleInCar);
				if(timeToEvacuate+time>Main.deadline) 
					result+=people[i]*personWeight;
			}
		}
		if(numOfPeopleInCar>0) {
			DijkstraResult rs = DijkstraAlgorithm.dijkstra(agentPosition, numOfPeopleInCar, -1);
			if(rs.distance[rs.goal]+time>Main.deadline)
				result+=numOfPeopleInCar*personWeight;
		}
		for (int i = 0; i < Main.agents.length; i++) {
			if(i!=agentIndex ) {
				result+=Main.agents[i].peopleInCar*personWeight;
				result+=Main.agents[i].score*personWeight;
			}
		}
		return result;
	}
	
	private static double calculateGameHeuristic(StateNode node, double personWeight, boolean isAgent) {
		double result = 0;
		int position;
		int peopleInCar;
		
		if(isAgent) {
			position=node.agentPosition;
			peopleInCar=node.peopleInAgentCar;
			result+=node.peopleInOpponentCar*personWeight;
			result+=node.opponentScore*personWeight;
		}
		else {
			position=node.opponentPosition;
			peopleInCar=node.peopleInOpponentCar;
			result+=node.peopleInAgentCar*personWeight;
			result+=node.agentScore*personWeight;
		}
			
		for (int i = 0; i < node.peopleArr.length; i++) {
			if(node.peopleArr[i]>0) {
				double timeToEvacuate=calculateTime(position ,i, node.peopleArr, peopleInCar);
				if(timeToEvacuate+node.time>Main.deadline) 
					result+=node.peopleArr[i]*personWeight;
			}
		}
		if(peopleInCar>0) {
			DijkstraResult rs = DijkstraAlgorithm.dijkstra(position, peopleInCar, -1);
			if(rs.distance[rs.goal]+node.time>Main.deadline)
				result+=peopleInCar*personWeight;
		}
			
		return result;
	}
	
	//for GameFullyCooperativeAgent
	private static double calculateFullCooperativeHeuristic(StateNode node, double personWeight) {
		double result = 0;
		for (int i = 0; i < node.peopleArr.length; i++) {
			if(node.peopleArr[i]>0) {
				double timeToEvacuateByAgent=calculateTime(node.agentPosition ,i, node.peopleArr, node.peopleInAgentCar);
				double timeToEvacuateByOpponent=calculateTime(node.opponentPosition ,i, node.peopleArr, node.peopleInOpponentCar);
				if(timeToEvacuateByAgent+node.time>Main.deadline && timeToEvacuateByOpponent+node.time>Main.deadline) 
					result+=node.peopleArr[i]*personWeight;
			}
		}
		if(node.peopleInAgentCar>0) {
			DijkstraResult rs = DijkstraAlgorithm.dijkstra(node.agentPosition, node.peopleInAgentCar, -1);
			if(rs.distance[rs.goal]+node.time>Main.deadline)
				result+=node.peopleInAgentCar*personWeight;
		}
		if(node.peopleInOpponentCar>0) {
			DijkstraResult rs = DijkstraAlgorithm.dijkstra(node.opponentPosition, node.peopleInOpponentCar, -1);
			if(rs.distance[rs.goal]+node.time>Main.deadline)
				result+=node.peopleInOpponentCar*personWeight;
		}
		return result;
	}
	
	private static double calculateTime(int agentPosition ,int vertex, int[] people, int numOfPeopleInCar){
		double result = 0;
		DijkstraResult rs = DijkstraAlgorithm.dijkstra(agentPosition, numOfPeopleInCar, vertex);
		result += rs.distance[vertex];
		rs = DijkstraAlgorithm.dijkstra(vertex, (numOfPeopleInCar+people[vertex]), -1);
		result += rs.distance[rs.goal];
		return result;
	}
	
}

