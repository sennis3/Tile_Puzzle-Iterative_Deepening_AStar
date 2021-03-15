
import java.util.ArrayList;

public class Node {
	
	private int emptyIndex;
	private String prevMove;
	private ArrayList<Integer> currState = new ArrayList<Integer>();
	private Node parentNode;
	
	//Values for calculating costs
	private int costSoFar;
	private int heuristicCost;
	private int totalEstCost;
	
	
	//Constructor including parent information
	public Node(ArrayList<Integer> state, Node pNode, String pMove, int cost, int heuristic) {
		currState = state;	
		emptyIndex = findEmptyIndex();
		parentNode = pNode;
		prevMove = pMove;
		costSoFar = cost;
		heuristicCost = heuristic;
		totalEstCost = cost + heuristic;
		
	}
	
	//Tried my best to estimate the data sizes and summed them up
	public static int calculateNodeSize() {
		int objectHeaderSize = 8;
		int emptyIndexSize = 4;
		int prevMoveSize = 40; //Strings take 40 bytes for one character apparently
		int currStateSize = 8 + 16 * 16; //overhead + 16 integer objects
		int parentNodeSize = 4;
		int costSoFarSize = 4;
		int heuristicCostSize = 4;
		int totalEstCostSize = 4;
		return objectHeaderSize + emptyIndexSize + prevMoveSize + currStateSize + parentNodeSize + costSoFarSize + heuristicCostSize + totalEstCostSize;
	}
	
	public ArrayList<Integer> getState(){
		return currState;
	}
	
	public Node getParentNode() {
		return parentNode;
	}
	
	public void setParentNode(Node pNode) {
		parentNode = pNode;
	}
	
	public String getPrevMove() {
		return prevMove;
	}
	
	public void setPrevMove(String pMove) {
		prevMove = pMove;
	}
	
	public int getCostSoFar() {
		return costSoFar;
	}
	
	public int getTotalCost() {
		return totalEstCost;
	}
	
	//Finds the location of the empty space in the puzzle
	public int findEmptyIndex() {
		for (int i = 0; i < 16; i++) {
			if (currState.get(i) == 0) {
				return i;
			}
		}
		return -1; //Error: empty space not found
	}
	
	//Makes a deep copy of the current state ArrayList
	public ArrayList<Integer> cloneState(){
		ArrayList<Integer> clone = new ArrayList<Integer>();
		
		for (int i = 0; i < 16; i++) {
			clone.add(currState.get(i));
		}
		
		return clone;
	}
	
	//Checks if moving up is possible
	public boolean canMoveUp() {
		if(emptyIndex > 3)
			return true;
		else
			return false;
	}
	
	//Checks if moving down is possible
	public boolean canMoveDown() {
		if(emptyIndex < 12)
			return true;
		else
			return false;
	}
	
	//Checks if moving left is possible
	public boolean canMoveLeft() {
		if((emptyIndex % 4) == 0)
			return false;
		else
			return true;
	}
	
	//Checks if moving right is possible
	public boolean canMoveRight() {
		if(((emptyIndex + 1) % 4) == 0)
			return false;
		else
			return true;
	}
	
	//Finds state configuration when empty space is moved up
	public ArrayList<Integer> moveUp() {
		ArrayList<Integer> newState = cloneState();
		int numMoved = currState.get(emptyIndex - 4); //Finds tile being swapped
		
		//Swaps the tile and empty space
		newState.set(emptyIndex - 4, 0);
		newState.set(emptyIndex, numMoved);
		
		return newState;
	}
	
	//Finds state configuration when empty space is moved down
	public ArrayList<Integer> moveDown() {
		ArrayList<Integer> newState = cloneState();
		int numMoved = currState.get(emptyIndex + 4); //Finds tile being swapped
		
		//Swaps the tile and empty space
		newState.set(emptyIndex + 4, 0);
		newState.set(emptyIndex, numMoved);
		
		return newState;
	}
	
	//Finds state configuration when empty space is moved left
	public ArrayList<Integer> moveLeft() {
		ArrayList<Integer> newState = cloneState();
		int numMoved = currState.get(emptyIndex - 1); //Finds tile being swapped
		
		//Swaps the tile and empty space
		newState.set(emptyIndex - 1, 0);
		newState.set(emptyIndex, numMoved);
		
		return newState;
	}
	
	//Finds state configuration when empty space is moved right
	public ArrayList<Integer> moveRight() {
		ArrayList<Integer> newState = cloneState();
		int numMoved = currState.get(emptyIndex + 1); //Finds tile being swapped
		
		//Swaps the tile and empty space
		newState.set(emptyIndex + 1, 0);
		newState.set(emptyIndex, numMoved);
		
		return newState;
	}
	
}
