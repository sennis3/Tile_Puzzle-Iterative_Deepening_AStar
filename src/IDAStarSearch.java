import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class IDAStarSearch {

	public static void main(String[] args) {
		
		//Set up and create searchState object
		ArrayList<Integer> goalState = getGoalState(); //Set up goal state
		Scanner in = new Scanner(System.in);
		ArrayList<Integer> inputState = getInputState(in); //Get initial state
		int nodeSize = Node.calculateNodeSize(); //Gets the estimated node size
		SearchState searchState = new SearchState(inputState, goalState, nodeSize);
		
		findSkipMisplaced(in, searchState); //Should misplaced tiles be skipped?
		in.close();
		
		if (!(searchState.getSkipMisplaced())) {
			//Run IDA* search with misplaced tiles heuristic
			searchState.setIsManhattan(false);
			System.out.println("Misplaced Tiles:");
			idaStarSearch(searchState);
			
			searchState.resetValues(); //resets values for manhattan distance
			System.out.println();
		}
		
		//Run IDA* search with Manhattan distance heuristic
		searchState.setIsManhattan(true);
		System.out.println("Manhattan Distance:");
		idaStarSearch(searchState);
		
		System.out.println();
		
	}
	
	//Performs IDA* search algorithm
	public static void idaStarSearch(SearchState searchState) {
		ArrayList<Integer> inputState = searchState.getInputState();
		ArrayList<Integer> goalState = searchState.getGoalState();
		ArrayList<Node> path = searchState.getPath();
		
		
		//Create initial node
		int heuristicCost = 0;
		if (searchState.getIsManhattan())
			heuristicCost = getManhattanDistance(inputState);
		else
			heuristicCost = getNumMisplacedTiles(inputState, goalState);
		Node initialNode = new Node(inputState, null, "None", 0, heuristicCost);

		searchState.setStartTime(System.nanoTime()); //Starts the timer
		
		path.add(initialNode);
		searchState.setBound(initialNode.getTotalCost());
		
		//Loops through iteration levels of IDA* until goal is found
		while (searchState.getIsGoalFound() == false) { //Times out after iteration level 20
			//searchState.setNodeCount(0);
			searchState.incIterationNum();
			System.out.println("Iteration #: " + searchState.getIterationNum());
			int t = recursiveSearch(searchState); //Kicks off the search and finds the next iteration bound
			searchState.setBound(t); //Sets the next bound
		}
		
		searchState.setEndTime(System.nanoTime()); //Stops the timer
		
		printResults(searchState);
	}
	
	//Recursive function to find the goal state using IDA*
	public static int recursiveSearch(SearchState searchState) {
		ArrayList<Node> path = searchState.getPath();
		HashSet<ArrayList<Integer>> pathSet = searchState.getPathSet();
		searchState.incNodeCount(); //increases node count
		
		Node n = path.get(path.size()-1); //last node in the path
		int f = n.getTotalCost(); //f = g + h
		if (f > searchState.getBound()) { //checks if total cost is outside current bound
			return f;
		}
		if (n.getState().equals(searchState.getGoalState())) { //checks if the current state is the goal state
			searchState.setIsGoalFound(true);
			searchState.setGoalPath(getGoalPath(n)); //gets the goal path
			return -1; //-1 is returned because the goal has been found
		}
		
		int min = -1; //-1 signifies it hasn't been set yet (replaces infinity from pseudocode)
		ArrayList<Node> queue = new ArrayList<Node>(); //ordered queue for child nodes
		getChildNodes(n, queue, searchState); //Gets possible child nodes and puts them into queue
		
		//Iterates through child nodes in order of best first
		for (int i = 0; i < queue.size(); i++) {
			//Push child node into path
			Node child = queue.get(i);
			ArrayList<Integer> childState = child.getState();
			path.add(child);
			pathSet.add(childState);
			
			//Recursive call to search function with child node
			int t = recursiveSearch(searchState);
			if (searchState.getIsGoalFound()) { //Check for goal found
				return -1;
			}
			if (t < min || min == -1) { //Check for new minimum estimated cost value
				min = t;
			}
			
			//Pop child node off of path
			path.remove(path.size()-1);
			pathSet.remove(childState);
		}
		
		return min;
	}
	
	public static void printResults(SearchState searchState) {
		//When goal is found
		if (searchState.getIsGoalFound()) {
			System.out.println("Goal Found!");
			//Print the moves made
			System.out.println("Moves: " + searchState.getGoalPath());
			//Print number of iteration levels
			System.out.println("Iterations: " + searchState.getIterationNum());
			//Print number of nodes
			System.out.println("Number of Nodes Expanded: " + searchState.getNodeCount());
			//Print memory taken
			System.out.println("Memory Used: " + searchState.calcMemUsed() + " kB");
			//Print time elapsed
			System.out.println("Time Taken: " + searchState.calcTimeTaken() + " seconds or " + (searchState.calcTimeTaken()*1000) + " milliseconds");
		}
	}
	
	//Finds the number of tiles in the wrong position compared to the goal state
	public static int getNumMisplacedTiles(ArrayList<Integer> currState, ArrayList<Integer> goalState) {
		int numMisplacedTiles = 0;
		
		for (int i = 0; i < 16; i++) {
			if (currState.get(i) != 0) { //skips over the empty space
				if (currState.get(i) != goalState.get(i)) //if current tile is in the wrong place
					numMisplacedTiles++;
			}
		}
		return numMisplacedTiles;
	}
	
	//Finds the sum of the total distances tiles are away from the goal state
	public static int getManhattanDistance(ArrayList<Integer> currState) {
		int manhattanDistance = 0;
		
		for (int i = 0; i < 16; i++) {
			int currTile = currState.get(i);
			if (currTile != 0) { //skips over the empty space
				int verticalDistance = calcVerticalDistance(currTile, i);
				int horizontalDistance = calcHorizontalDistance(currTile, i);
				manhattanDistance = manhattanDistance + verticalDistance + horizontalDistance;
			}
		}
		return manhattanDistance;
	}
	
	//Calculates a tile's vertical distance from its goal location
	public static int calcVerticalDistance(int tileNum, int tileLoc) {		
		 return Math.abs((tileNum-1)/4 - tileLoc/4);	
	}
	
	//Calculates a tile's horizontal distance from its goal location
	public static int calcHorizontalDistance(int tileNum, int tileLoc) {
		return Math.abs((tileNum-1)%4 - tileLoc%4);
	}
	
	//Gets all valid child nodes and adds them to the queue
	public static void getChildNodes(Node currNode, ArrayList<Node> queue, SearchState searchState) {
		HashSet<ArrayList<Integer>> pathSet = searchState.getPathSet();
		
		//Moving empty space up
		if (currNode.canMoveUp()) {
			ArrayList<Integer> upState = currNode.moveUp();
			if (!(pathSet.contains(upState))) { //Checks if its a repeated state
				Node upNode = createNewNode(upState, currNode, "U", searchState); //node created
				insertNodeSorted(queue, upNode); //node added to queue
			}
		}
		
		//Moving empty space down
		if (currNode.canMoveDown()) {
			ArrayList<Integer> downState = currNode.moveDown();
			if (!(pathSet.contains(downState))) { //Checks if its a repeated state
				Node downNode = createNewNode(downState, currNode, "D", searchState); //node created
				insertNodeSorted(queue, downNode); //node added to queue
			}
		}

		//Moving empty space left
		if (currNode.canMoveLeft()) {
			ArrayList<Integer> leftState = currNode.moveLeft();
			if (!(pathSet.contains(leftState))) { //Checks if its a repeated state
				Node leftNode = createNewNode(leftState, currNode, "L", searchState); //node created
				insertNodeSorted(queue, leftNode); //node added to queue
			}
		}
		
		//Moving empty space right
		if (currNode.canMoveRight()) {
			ArrayList<Integer> rightState = currNode.moveRight();
			if (!(pathSet.contains(rightState))) { //Checks if its a repeated state
				Node rightNode = createNewNode(rightState, currNode, "R", searchState); //node created
				insertNodeSorted(queue, rightNode); //node added to queue
			}
		}
	}
	
	//Creates a new Node object
	public static Node createNewNode(ArrayList<Integer> state, Node pNode, String pMove, SearchState searchState) {
		int heuristicCost = 0;
		//Gets cost depending on which heuristic is being used
		if (searchState.getIsManhattan()) {
			heuristicCost = getManhattanDistance(state);
		}
		else {
			heuristicCost = getNumMisplacedTiles(state, searchState.getGoalState());
		}
		
		Node newNode = new Node(state, pNode, pMove, pNode.getCostSoFar()+1, heuristicCost);
		return newNode;
	}
	
	//Node is inserted into the queue based on its total estimated cost
	public static void insertNodeSorted(ArrayList<Node> nodeQueue, Node n) {
		boolean isAdded = false;
		//Compares current node's cost with each node in the queue
		for (int i = 0; i < nodeQueue.size(); i++) {
			if (n.getTotalCost() < nodeQueue.get(i).getTotalCost()) {
				nodeQueue.add(i, n);
				isAdded = true;
				break;
			}
		}
		//Node is added to the end of the queue (if it has the worst cost)
		if (!isAdded) {
			nodeQueue.add(n);
		}
	}
	
	//Sets up the goal state and returns it
	public static ArrayList<Integer> getGoalState() {
		String goalString = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0";
		String[] tokens = goalString.split(" ");
		
		//Adds each goal state value into ArrayList
		ArrayList<Integer> goalState = new ArrayList<Integer>();
		for (int i = 0; i < tokens.length; i++) {
			goalState.add(Integer.parseInt(tokens[i]));
		}
		
		return goalState;
	}
	
	//Receives initial configuration state and returns it as ArrayList
	public static ArrayList<Integer> getInputState(Scanner in) {
		ArrayList<Integer> inputState = new ArrayList<Integer>();

		String userResponse;

		//userResponse = "1 0 3 4 5 2 6 8 9 10 7 11 13 14 15 12";

		//Receives user input from console
		//Scanner in = new Scanner(System.in);
		System.out.println("\nEnter list of 16 numbers as starting configuration (0 = empty space):");
		userResponse = in.nextLine();
		//in.close();
		
		//Checks if user entered any values
		if (userResponse.length() == 0) {
			System.out.print("Nothing entered - exiting...");
			System.exit(0);
		}
		
		//Checks if user entered correct number of values
		String[] tokensEntered = userResponse.split(" ");
		if (tokensEntered.length != 16) {
			System.out.print("There must be 16 entries! - exiting...");
			System.exit(0);
		}
		
		//Checks for any repeats in the values
		Set<String> setEntered = new HashSet<String>();
		for (int i=0; i<tokensEntered.length; i++)
		{
			setEntered.add(tokensEntered[i]); //Fills hashset
		}
		if (setEntered.size() != 16) //Checks if correct number of values are in hashset
		{
			System.out.print("Duplicate entries! - exiting...");
			System.exit(0);
		}
		
		//Checks if all values are integers in range
		for (int i = 0; i < tokensEntered.length; i++) {
			int numEntered=0;
			try {
				numEntered = Integer.parseInt(tokensEntered[i]); //Checks if they're integers
				
				if(numEntered < 0 || numEntered > 15) {  //Checks if they're in range
					System.out.println("Number out of range! - exiting...");
					System.exit(0);
				}
			}
			catch (NumberFormatException e){
				System.out.println("Invalid number entered! - exiting...");
				System.exit(0);
			}
			inputState.add(numEntered); //Adds value to initial state ArrayList
		}
		
		return inputState;
	}
	
	//Gets input determining whether to skip misplaced tiles heuristic
	public static void findSkipMisplaced(Scanner in, SearchState searchState) {
		String userResponse;

		//Receives user input from console
		System.out.println("Do you want to skip Misplaced Tiles? y/n");
		userResponse = in.nextLine();
		
		if (userResponse.equals("y"))
			searchState.setSkipMisplaced(true);
		else if (userResponse.equals("n"))
			searchState.setSkipMisplaced(false);
		else { //Only accepts 'y' or 'n'
			System.out.println("Invalid Response - exiting...");
			System.exit(0);
		}
		
		System.out.println();
	}
	
	//Parses back through the parent nodes to return the path to the goal
	public static String getGoalPath(Node goalNode) {
		Node currNode = goalNode;
		String goalPath = "";
		
		while (!(currNode.getPrevMove().equals("None"))) { //while it isn't the root node
			goalPath = currNode.getPrevMove() + " " + goalPath;
			currNode = currNode.getParentNode();
		}
		
		return goalPath;
	}
	
}
