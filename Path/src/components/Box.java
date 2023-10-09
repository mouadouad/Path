package components;

import java.io.Serializable;
import main.MainActivity;

/**
 * This is the class that creates the cells of the grid, it does not inherit from the JavaFX Button class but each object is linked to one
 */

public class Box implements Serializable{
	
	private static final long serialVersionUID = 2L;
	
	static String[] Colors = {"red", "blue", "green", "yellow"};
	
	private boolean isPawn = false;
	private Pawn pawn = null;
	private int x,y;
	private boolean isBarrierLeft = false; // defines if the neighbors of the box are accessible or not, if true , that means that there is a barrier							
	private boolean isBarrierRight = false; // defines if the neighbors of the box are accessible or not, if true , that means that there is a barrier
	private boolean isBarrierTop = false; // defines if the neighbors of the box are accessible or not, if true , that means that there is a barrier
	private boolean isBarrierBot = false; // defines if the neighbors of the box are accessible or not, if true , that means that there is a barrier
	
	private boolean canClick = false;
	
	public Box(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * It's the method that is called when the corresponding button of the box is clicked
	 * It checks if the boxes in general can be clicked, and then if this specific box can be clicked
	 * It then sets its pawn variable to the corresponding turn and updates also the pawn object by setting his box
	 * Checks if the pawn arrived to his target row or column
	 * It then calls the reset method in the Plateau class
	 */
	public void onClickBox() {
		Plateau pl = MainActivity.pl;
		
		if(getCanClick() && pl.canClickBox()) {
			Pawn pawn = pl.getPawn(pl.getTurn());
			setPawn(pawn,true);
			pawn.getBox().setPawn(null,false);
			pawn.setBox(getThisBox());
			
			if(pawn.getTargetRow() == getx() || pawn.getTargetColumn() == gety()) { //check if arrived to the target row or column
				pl.setGameOver(true);
				pl.setWinner(pawn);
			}
			
			pl.reset();
		}
		
	}
	
	public Box getThisBox() {
		return this;
	}
	
	public int getx(){
		return x;
	}
	
	public int gety(){
		return y;
	}

	public boolean isBarrierLeft() {
		return isBarrierLeft;
	}

	public void setLeft(Boolean isBarrier) {
		this.isBarrierLeft = isBarrier;
	}

	public boolean isBarrierRight() {
		return isBarrierRight;
	}

	public void setRight(Boolean isBarrier) {
		this.isBarrierRight = isBarrier;
	}

	public boolean isBarrierTop() {
		return isBarrierTop;
	}

	public void setTop(Boolean isBarrier) {
		this.isBarrierTop = isBarrier;
	}

	public boolean isBarrierBot() {
		return isBarrierBot;
	}

	public void setBot(Boolean isBarrier) {
		this.isBarrierBot = isBarrier;
	}
	
	public boolean isPawn() {
		return isPawn;
	}

	public void setPawn(Pawn pawn, boolean isPawn) {
		this.isPawn = isPawn;
		this.pawn = pawn;
	}

	public Pawn getPawn() {
		return pawn;
	}
	
	public boolean getCanClick() {
		return canClick;
	}
	
	/**
	 * it sets the canClick boolean and changes the color of the button that it gets from the grid using the getNodeByRowColumnInex method in the
	 * MainActivity accordingly
	 * @param canClick
	 */
	public void setCanClick(boolean canClick) {
		this.canClick = canClick;
		if(canClick) {
			MainActivity.getNodeByRowColumnIndex(2*x + 1, 2*y + 1).setStyle("-fx-background-color: "+Colors[MainActivity.pl.getTurn()]+";");
		}else {
			MainActivity.getNodeByRowColumnIndex(2*x + 1, 2*y + 1).setStyle("-fx-background-color: gray;");
		}
		
	}
	
}
