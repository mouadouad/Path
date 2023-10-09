package components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import main.MainActivity;

/**
 * This is the class Plateau that contains most of the information about the game except borders
 * It is created only once in the MainActivity as a static variables as we have only one game going at time
 */

public class Plateau implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Box[][] plateau;
	private Pawn[] pawns;
	private boolean isGameOver;
	private int numberBarrier;
	private int numberPlayers;
	private int turn = 0;
	private Pawn winner;
	
	private boolean canClickBorder = false;
	private boolean canClickBox = false;
	
	/**
	 * It's the constructor of the Plateau class and contains most of the information about the game except borders
	 * It takes as parameters only the number of players*/
	public Plateau(int numberPlayers){

		this.plateau = new Box[9][9];
		isGameOver= false;
		numberBarrier = 0;
		this.numberPlayers = numberPlayers;
		//initialize the variables plateau and pawns
		initialise();
	}
	
	public Box[][] getPlateau() {
		return plateau;
	}
	
	public void setPlateau(Box[][] plateau) {
		this.plateau = plateau;
	}
	
	public Box getBox(int x,int y) {	
		return plateau[x][y];
	}
		
	public boolean isGameOver() {
		return isGameOver;
	}
	
	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}
	
	public void setWinner(Pawn winner) {
		this.winner = winner;
	}
	
	public Pawn getWinner() {
		return winner;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public int getNumberBarriere() {
		return numberBarrier;
	}
	
	public void setNumberBarriere(int numberBarrier) {
		this.numberBarrier = numberBarrier;
	}
	
	public int getNumberPlayers() {
		return numberPlayers;
	}
	
	public void setNumberPlayers(int numberPlayers) {
		this.numberPlayers = numberPlayers;
	}
	
	public Pawn[] getPawns() {
		return pawns;
	}
	
	public Pawn getPawn(int index) {
		return pawns[index];
	}
	
	public boolean canClickBorder() {
		return canClickBorder;
	}
	
	public void setCanClickBorder(boolean canClick) {
		this.canClickBorder = canClick;
	}
	
	public boolean canClickBox() {
		return canClickBox;
	}
	
	public void setCanClickBox(boolean canClick) {
		this.canClickBox = canClick;
	}
	
	/**
	 * It takes as parameters the x and y of the border that is considered and a boolean if it is vertical and a boolean to see if clicked or not
	 * it sets the two boxes that are around the considered border (using the isVertical variable) as clicked or not*/
	public void borderClicked(int x, int y, boolean isVertical,boolean clicked) {
		
		if(isVertical) {
			getBox(x,y-1).setRight(clicked);
			getBox(x,y).setLeft(clicked);
		}else {
			getBox(x,y).setTop(clicked);
			getBox(x-1,y).setBot(clicked);
		}
	}
	
	/**
	 * It returns the list of neighbors that a box can access
	 * (Most important method of the program as it does a lot of processing and calls itself recursively sometimes)
	 * @param box
	 * @param comesFrom
	 * @return List
	 */
	public List<Box> Neighbours(Box box, int comesFrom){
		
		//comesFrom is an integer that matters only when recursive (when neighborOfNeighbor) 
		//to not count the initial box or pawn as a neighbor again
		
		List<Box> neighbours = new ArrayList<Box>();
				
		if(!box.isBarrierTop() && comesFrom != 0){ //it checks if there isn't a barrier and that it doesn't come from the box in that direction
			Box neighbor = getBox(box.getx()-1, box.gety()); // get the box that is above it as a neighbor
			if (neighbor.isPawn()) { // check if the neighbor box does contain a pawn
				//all the conditions below check if comes from is -1, it means that the method is called for the first time not from itself
				if(neighbor.isBarrierTop() && comesFrom == -1) { //check if there is a barrier above the neighbor 
					//call the Neighbor method for this neighbor and set comesFrom accordingly
					//add the neighbors of neighbor to the main neighbors list
					for(Box neighborOfNeighbor: Neighbours(neighbor,1)) { 
						neighbours.add(neighborOfNeighbor);
					}
				}else if(getBox(neighbor.getx()-1, neighbor.gety()).isPawn() && comesFrom == -1) {
					//if there isn't a barrier then check the upper neighbor of neighbor if pawn
					//do the same as before
					for(Box neighborOfNeighbor: Neighbours(neighbor,1)) {
						neighbours.add(neighborOfNeighbor);
					}
				}else if(!getBox(neighbor.getx()-1, neighbor.gety()).isPawn() && comesFrom == -1) {
					//if the upper neighbor of neighbor isn't blocked by a barrier and isn't a pawn then add it to the neighbors list
					neighbours.add(getBox(neighbor.getx()-1, neighbor.gety()));
				}
				
			}else { // add the neighbor to the neighbors list if it doesn't contain a pawn
				neighbours.add(neighbor);
			}
		}
		
		//do the same for the rest with the correct modifications
		if(!box.isBarrierBot() && comesFrom != 1){
			Box neighbor = getBox(box.getx()+1, box.gety());
			if (neighbor.isPawn()) {
				if(neighbor.isBarrierBot() && comesFrom == -1) {
					for(Box neighborOfNeighbor: Neighbours(neighbor,0)) {
						neighbours.add(neighborOfNeighbor);
					}
				}else if(getBox(neighbor.getx()+1, neighbor.gety()).isPawn() && comesFrom == -1) {
					for(Box neighborOfNeighbor: Neighbours(neighbor,0)) {
						neighbours.add(neighborOfNeighbor);
					}
				}else if(!getBox(neighbor.getx()+1, neighbor.gety()).isPawn() && comesFrom == -1) {
					neighbours.add(getBox(neighbor.getx()+1, neighbor.gety()));
				}
				
			}else {
				neighbours.add(neighbor);
			}
		}
		if(!box.isBarrierLeft() && comesFrom != 2){
			Box neighbor = getBox(box.getx(), box.gety()-1);
			if (neighbor.isPawn()) {
				if(neighbor.isBarrierLeft() && comesFrom == -1) {
					for(Box neighborOfNeighbor: Neighbours(neighbor,3)) {
						neighbours.add(neighborOfNeighbor);
					}
				}else if(getBox(neighbor.getx(), neighbor.gety()-1).isPawn() && comesFrom == -1) {
					for(Box neighborOfNeighbor: Neighbours(neighbor,3)) {
						neighbours.add(neighborOfNeighbor);
					}
				}else if(!getBox(neighbor.getx(), neighbor.gety()-1).isPawn() && comesFrom == -1) {
					neighbours.add(getBox(neighbor.getx(), neighbor.gety()-1));
				}
				
			}else {
				neighbours.add(neighbor);
			}
		}
		if(!box.isBarrierRight() && comesFrom != 3){
			Box neighbor = getBox(box.getx(), box.gety()+1);
			if (neighbor.isPawn()) {
				if(neighbor.isBarrierRight() && comesFrom == -1) {
					for(Box neighborOfNeighbor: Neighbours(neighbor,2)) {
						neighbours.add(neighborOfNeighbor);
					}
				}else if(getBox(neighbor.getx(), neighbor.gety()+1).isPawn() && comesFrom == -1) {
					for(Box neighborOfNeighbor: Neighbours(neighbor,2)) {
						neighbours.add(neighborOfNeighbor);
					}
				}else if(!getBox(neighbor.getx(), neighbor.gety()+1).isPawn() && comesFrom == -1) {
					neighbours.add(getBox(neighbor.getx(), neighbor.gety()+1));
				}
				
			}else {
				neighbours.add(neighbor);
			}
		}
		
		return neighbours;
	}
	
	/**
	 * It is an intermediate if the Neighbors method is called with only with parameter (doesn't come recursively)
	 * calls then the Neighbors method with the second parameter set as -1
	 * @param box
	 */
	public List<Box> Neighbours(Box box){
		return Neighbours(box,-1);
	}
	
	/**
	 * It resets the game as a player just played
	 * It updates the player_turn label, the turn variable and sets move_btn and barrier_btn to not selected in the MainActivity
	 * It removes the boxes that you can click and draws the pawns to their new location
	 * Sets the canClickBorder and canClickBox booleans to false
	 * If the game is over change the player_turn_label to announce the name of the winner*/
	public void reset() {
		canClickBorder = false;
		canClickBox = false;
		
		setTurn((getTurn()+1)%getNumberPlayers());
		
		MainActivity.player_turn_label.setText("Player " + String.valueOf(getTurn()+1));
		MainActivity.set_background(MainActivity.barrier_btn, "barrier", false);
		MainActivity.set_background(MainActivity.move_btn, "move", false);
		
		removeClickableBoxes();
		MainActivity.drawPawns();
		
		if(isGameOver()) {
			MainActivity.player_turn_label.setText("Player " + String.valueOf(getWinner().getName()+1) + " Won!");
		}
		
	}
	
	/**
	 * It takes an integer turn as a parameter
	 * It sets the boxes that the corresponding pawn can click 
	 * the setCanClick method also changes their color
	 * It simply calls the Neighbors method for the box that the pawn is on, which returns a list of boxes */
	public void setClickableBoxes(int turn) {
		
		
		List<Box> neighbours = Neighbours(getPawn(turn).getBox());
		
		for(Box neighbor: neighbours) {
			neighbor.setCanClick(true);
		}
	}
	
	/**
	 * It iterates over all the boxes of the plateau and sets them as they cannot be clicked
	 * the setCanClick method also changes their color */
	public void removeClickableBoxes() {
		
		for(Box[] boxList: getPlateau()) {
			for(Box box: boxList) {
				box.setCanClick(false);
			}
		}
	}
	
	/**
	 * It takes a pawn as a parameter
	 * It returns a boolean that tells if there is a way for the pawn to achieve his target row or column
	 * It uses Breadth First Search algorithm, with the box, the pawn is on, as a starting point
	 * The algorithm is fairly easy to implement thanks to the Neighbors method that takes care of everything*/
	public boolean existsWay(Pawn pawn) {

		//BFS Breadth First Search
		List<Integer> marked = new ArrayList<>(); //using a list of integers rather than boxes for memory efficiency
		Queue<Box> queue = new LinkedList<>();
		queue.add(pawn.getBox());
		
		while(queue.size()>0) {
			Box V = queue.poll(); //take the first item of the queue
			if (!marked.contains(V.getx()*9 + V.gety())) { // check if marked (x*9 + y) is unique and represents the boxes
				
				if(pawn.getTargetRow() == V.getx() || pawn.getTargetColumn() == V.gety()) { //check if arrived to the target row or column
					return true;
				}
				
				marked.add(V.getx()*9 + V.gety()); //mark it
				
				for(Box W : Neighbours(V)) {
					if (!marked.contains(W.getx()*9 + W.gety())) { //check if not marked
						queue.add(W); //add neighbor to queue
					}
				}	
				
			}
		}
		
		return false;
	}
	
	/**
	 * It initializes the two dimensional table containing the boxes called plateau by creating the boxes objects
	 * It creates the pawn objects and add them to the pawns variable and tells the pawns which boxes they are on, and tells the boxes 
	 * which pawns they contain (if they have one)
	 * It sets the outer edges of the outer boxes of the grid as barriers */
	public void initialise() {

		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++) {
				plateau[i][j] = new Box(i,j);
				
				switch(j) {
				case 0:
					plateau[i][j].setLeft(true);
					break;
				case 8:
					plateau[i][j].setRight(true);
					break;
				}
				switch(i) {
				case 0:
					plateau[i][j].setTop(true);
					break;
				case 8:
					plateau[i][j].setBot(true);
					break;
				}
			}
		}

		if(this.getNumberPlayers()==2) {
			pawns = new Pawn[2];
			pawns[0] = new Pawn(0,plateau[0][4]);
			pawns[1] = new Pawn(1,plateau[8][4]);
			
		}else {
			pawns = new Pawn[4];
			pawns[0] = new Pawn(0, plateau[0][4]);
			pawns[1] = new Pawn(1, plateau[8][4]);
			pawns[2] = new Pawn(2, plateau[4][0]);
			pawns[3] = new Pawn(3, plateau[4][8]);
			
			plateau[4][0].setPawn(pawns[2], true);
			plateau[4][8].setPawn(pawns[3], true);
		}
		
		plateau[0][4].setPawn(pawns[0], true);
		plateau[8][4].setPawn(pawns[1], true);
		
			
	}
	
}