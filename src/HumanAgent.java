import java.util.Scanner;

public class HumanAgent extends Agent {

	int usersDecition = -1;
	
	
    public HumanAgent(int agentTypes, int positions, int peopleInCar) {
        super(agentTypes, positions, peopleInCar);
    }
    
    
	@Override
	public double timeForNextAction() {
		
		double timeForAction = Double.POSITIVE_INFINITY;
		
		System.out.println("Which vertex you want to move to?");
		Scanner sc = Main.scan;
		int ans = sc.nextInt()-1;
		while (ans<0 | ans>=Main.vertexMatrix.length) {
			System.out.println("position of agent should be positive integer smaller than number of vertices. Try again");
			ans = sc.nextInt()-1;
		}
		usersDecition = ans; 
		if(usersDecition==position) 
			return 1; //no-op
		
		//calculate time = w(1+kp)
		double w = Main.vertexMatrix[Math.min(position,usersDecition)][Math.max(position,usersDecition)];
		while(w==0) {
			System.out.println("there is no edge between the vertices. Try again");
			ans = sc.nextInt()-1;
			w = Main.vertexMatrix[Math.min(position,ans)][Math.max(position,ans)];
		}
		usersDecition = ans;
		
		timeForAction = w*(1 + Main.k_parameter*peopleInCar);
		System.out.println("time for action is: " + timeForAction);
		return timeForAction;
	}

	@Override
	public int doAction() {
		// take people to car if needed
		peopleInCar += Main.peopleToSave[usersDecition];
		Main.peopleToSave[usersDecition]=0;
		
		// put people in shelter if needed
		if (Main.shelters[usersDecition]){
			score += peopleInCar;
			peopleInCar = 0;
		}
		
		//update position
		position = usersDecition;

		return position;
	}

}

