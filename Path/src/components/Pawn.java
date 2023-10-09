package components;

import java.io.Serializable;

/**
 * This is the class that creates the pawns of the grid, it does not inherit from the JavaFX Circle class but each object is linked to one
 */
 
public class Pawn implements Serializable{
	
	private static final long serialVersionUID = 3L;
	private int name;	//red pawn is 0 (top), blue pawn is 1(bottom), green pawn is 2(left) and yellow pawn is 3 (right)
	private Box box;   // the box on which is the pawn
	private int targetRow, targetColumn; //the row or column the pawn should arrive to. Set the other variable to -1
	
	/**
	 * It's The constructor of the class pawn
	 * It takes in the name of the pawn as an integer and the box on which the pawn is at the start
	 * It then sets the targetRow and the targetColumn accordingly */
	public Pawn(int name,Box box) {
		this.box = box;
		
		switch(name) {
			case 0:
				 this.name = name;
				 targetRow = 8;
				 targetColumn = -1;
				 break;
			case 1:
				 this.name = name;
				 targetRow = 0;
				 targetColumn = -1;
				 break;
			case 2:
				 this.name = name;
				 targetRow = -1;
				 targetColumn = 8;
				 break;
			case 3:
				 this.name = name;
				 targetRow = -1;
				 targetColumn = 0;
				 break;
			default:
				System.out.println("Error");
		}
	}
	
	public int getName() {
		return name;
	}
	
	public int getTargetRow() {
		return targetRow;
	}
	
	public int getTargetColumn() {
		return targetColumn;
	}
	
	public void setBox(Box newBox) {
		this.box = newBox;
	}
	
	public Box getBox() {
		return box;
	}
	
	
	
}
