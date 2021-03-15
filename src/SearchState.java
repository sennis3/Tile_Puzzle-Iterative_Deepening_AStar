import java.util.ArrayList;
import java.util.HashSet;

public class SearchState {
	
	private int nodeCount;
	private int nodeSize;
	private int iterationNum;
	private int bound;
	private ArrayList<Integer> inputState;
	private ArrayList<Integer> goalState;
	private String goalPath;
	private boolean isManhattan;
	private boolean isGoalFound;
	//private HashSet<ArrayList<Integer>> exploredStates = new HashSet<ArrayList<Integer>>();
	//private ArrayList<Node> nodeQueue = new ArrayList<Node>();
	private ArrayList<Node> path = new ArrayList<Node>();
	private HashSet<ArrayList<Integer>> pathSet = new HashSet<ArrayList<Integer>>();
	private double startTime;
	private double endTime;
	private boolean skipMisplaced;
	
	//SearchState constructor
	public SearchState(ArrayList<Integer> inputState, ArrayList<Integer> goalState, int nodeSize) {
		this.inputState = inputState;
		this.goalState = goalState;
		this.nodeSize = nodeSize;
		
		nodeCount = 0;
		iterationNum = 0;
		//bound = 0;
		isManhattan = false;
		isGoalFound = false;
		
		pathSet.add(inputState);
	}
	
	public int getNodeCount() {
		return nodeCount;
	}
	
	public void setNodeCount(int newCount) {
		this.nodeCount = newCount;
	}
	
	public void incNodeCount() {
		nodeCount++;
	}
	
	public int getIterationNum() {
		return iterationNum;
	}
	
	public void incIterationNum() {
		iterationNum++;
	}
	
	public int getBound() {
		return bound;
	}
	
	public void setBound(int newBound) {
		this.bound = newBound;
	}
	
	public ArrayList<Integer> getInputState() {
		return inputState;
	}
	
	public ArrayList<Integer> getGoalState() {
		return goalState;
	}
	
	public boolean getIsManhattan() {
		return isManhattan;
	}
	
	public void setIsManhattan(boolean isManhattan) {
		this.isManhattan = isManhattan;
	}
	
	public boolean getIsGoalFound() {
		return isGoalFound;
	}
	
	public void setIsGoalFound(boolean isGoalFound) {
		this.isGoalFound = isGoalFound;
	}
	
	public String getGoalPath() {
		return goalPath;
	}
	
	public void setGoalPath(String goalPath) {
		this.goalPath = goalPath;
	}
	
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}
	
	/*public HashSet<ArrayList<Integer>> getExploredStates(){
		return exploredStates;
	}
	
	public ArrayList<Node> getNodeQueue() {
		return nodeQueue;
	}*/
	
	public ArrayList<Node> getPath() {
		return path;
	}
	
	public HashSet<ArrayList<Integer>> getPathSet() {
		return pathSet;
	}
	
	public boolean getSkipMisplaced() {
		return skipMisplaced;
	}
	
	public void setSkipMisplaced(boolean skipMisplaced) {
		this.skipMisplaced = skipMisplaced;
	}
	
	//Calculates time elapsed during search
	public double calcTimeTaken() {
		double timeTaken = (endTime - startTime) / 1000000000.00; //in seconds
		return timeTaken;
	}
	
	//Calculates memory used during last search iteration
	public int calcMemUsed() {
		int memUsed = nodeCount * nodeSize / 1000;
		return memUsed;
	}
	
	//Resets the values for the next heuristic search
	public void resetValues() {
		nodeCount = 0;
		iterationNum = 0;
		bound = 0;
		goalPath = "";
		isGoalFound = false;
		startTime = 0;
		endTime = 0;
		
		//nodeQueue.clear();
		//exploredStates.clear();
		//exploredStates.add(inputState);
		path.clear();
		pathSet.clear();
		pathSet.add(inputState);
	}
	
}
