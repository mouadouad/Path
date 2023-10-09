package components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;
import main.MainActivity;

/**
 * This is the class that creates the borders of the grid, it does not inherit from the JavaFX Button class but each object is linked to one
 */

public class Border implements Serializable{
	
	private static final long serialVersionUID = 4L;
	
	private int x, y;
	private boolean isBarrier, isVertical, isHorizontal;
	private int whichNeighbour = 0;  //It's an integer that tells which neighbor it is making the barrier with, it represents the index in the neighbors list
	
	public Border(int x, int y, char rotation) {
		
		/*
		 * Its the constructor of the Border class
		 * It takes as parameters the coordinates of the border in the BorderList and its rotation (vertical or horizontal)*/
		this.x = x;
		this.y = y;
		isBarrier = false;
		
		if(rotation == 'V') {
			isVertical = true;
			isHorizontal = false;
		}else {
			isVertical = false;
			isHorizontal = true;
		}
			
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isVertical() {
		return isVertical;
	}
	
	public boolean isHorizontal() {
		return isHorizontal;
	}
	
	public boolean isBarrier() {
		return isBarrier;
	}
	
	public void setBarrier() {	
		isBarrier = true;
	}
	
	/**
	 * It's the onMouseMoved handler of the corresponding border
	 * It checks if can click borders in general and if the border is not a barrier
	 * It takes the neighbors of the border using the Neighbors method
	 * It takes then the coordinates of the mouse and sees if the upper or lower(right or left if horizontal) part of the border is hovered
	 * and make the whichNeighbor integer accordingly 
	 * It changes then the color of the border and its neighbor if the barrier created doesn't block the way
	 * @param event
	 */
	public void onMouseMoved(MouseEvent event) {

		 if(MainActivity.pl.canClickBorder()&&!isBarrier()) {
	    	  double percentage;
	    	  if(isVertical()) {
	    		  percentage = event.getY()/50;
	    	  }else {
	    		  percentage = event.getX()/50; 
	    	  }
	    	  
	    	  List<Border> neighbours = Neighbours();
	    	  
	    	  if(percentage>0.5 && neighbours.size()>1) {
	    		  whichNeighbour = 1;
	    		  neighbours.get(0).originalColor(); //set the color of the other neighbor to original
	    	  }
	    	  
	    	  if(percentage<=0.5 && neighbours.size()>0) {
	    		  whichNeighbour = 0;
	    		  if(neighbours.size()>1) {
	    			  neighbours.get(1).originalColor(); //set the color of the other neighbor to original
	    		  } 
	    	  }
	    	  
	    	  if(neighbours.size()>0) { //check if the border has neighbors or not
	    		  if (doesntBlockWay(neighbours.get(whichNeighbour))) {
		    		  changeColor();
		    		  neighbours.get(whichNeighbour).changeColor();
	    		  }else {
	    			  originalColor();
	    		  }
	    	  } 
   	  }
	}
	
	/**
	 * Its the onMouseClicked handler of the corresponding border
	 * It checks if can click borders in general and if the border is not a barrier
	 * It takes the neighbors of the border using the Neighbors method
	 * It takes the neighbor concerned with the whichNeighbor integer
	 * It checks if the barrier doesn't block the way
	 * It then calls the reset method in the Plateau class
	 */
	public void onMouseClicked() {
		Plateau pl = MainActivity.pl;
		
		if(pl.canClickBorder()&&!isBarrier()) {
			
			List<Border> neighbours = Neighbours();
			if (neighbours.size() > 0) { //check if the border has neighbors or not
				Border neighbour = neighbours.get(whichNeighbour);
				
				if(doesntBlockWay(neighbour)) {
				
					//change the color of the border and its neighbor and set them both as barriers (isBarrier boolean)
					neighbour.setBarrier();
					neighbour.changeColor();
					setBarrier();
					changeColor();
					
					//set that the border is clicked and change the boxes concerned with the borderClicked method in the Plateau class
					pl.borderClicked(getX(), getY(), isVertical(),true);
					pl.borderClicked(neighbour.getX(), neighbour.getY(), neighbour.isVertical(),true);
					
					//increment the number of barriers
					pl.setNumberBarriere(pl.getNumberBarriere() + 1);
					
					pl.reset();
				}
				
			}
    	  }
	}
	
	/**
	 *  It's the onMouseExited handler of the corresponding border
	 *  It checks if can click borders in general and if the border is not a barrier
	 *  Checks if it has neighbors and then set both the border and its neighbor to original color
	 */
	public void onMouseExited() {
		
		if(MainActivity.pl.canClickBorder()&&!isBarrier()) {
  		  if(Neighbours().size()>0) {
  			  originalColor();
		      Neighbours().get(whichNeighbour).originalColor();
  		  }
	        
  	  }
		
	}
	
	/**
	 * It returns a boolean that says if a barrier is added in this border, will all the pawns have a way to achieve target
	 * It takes in parameter the neighbor border which is selected with it, because each barrier is composed of two borders
	 * It takes the Plateau pl static variable from the MainActivity and sets the two borders as already clicked just to do calculations
	 * and then set them back as not clicked at the end
	 * It tests then all the pawns and see if each one still has a way to achieve the target row or column
	 * It uses the existsWay method defined in the Plateau class
	 * @param neighbour
	 */
	private boolean doesntBlockWay(Border neighbour) {
		
		Plateau pl = MainActivity.pl;
		
		pl.borderClicked(getX(), getY(), isVertical(),true);
		pl.borderClicked(neighbour.getX(), neighbour.getY(), neighbour.isVertical(),true);
		
		boolean doesntBlockWay = true;
		
		for(Pawn p : pl.getPawns()) {
  		  if(!pl.existsWay(p)) {
  			  doesntBlockWay = false;
  			  break;
  		  }
  	  }
		
		pl.borderClicked(getX(), getY(), isVertical(),false);
		pl.borderClicked(neighbour.getX(), neighbour.getY(), neighbour.isVertical(),false);	
		
		return doesntBlockWay;
	}
	
	/**
	 * Change the color of the border to black as if it is selected
	 * It is done by getting the corresponding node from the static grid in the MainActivity using the row and column variables*/
	public void changeColor() {

		int row = isVertical()? x*2 + 1: x*2;
		int column = isVertical()? y*2: y*2 + 1;
		
		MainActivity.getNodeByRowColumnIndex(row, column).setStyle("-fx-background-color: black;");
	}
	
	/**
	 * Change the color of the border to blue as it is its original color
	 * It is done by getting the corresponding node from the static grid in the MainActivity using the row and column variables*/
	public void originalColor() {
		
		int row = isVertical()? x*2 + 1: x*2;
		int column = isVertical()? y*2: y*2 + 1;
		
		MainActivity.getNodeByRowColumnIndex(row, column).setStyle("-fx-background-color: #6d88b3;");
	}
	
	/**
	 * It returns a list of Borders that represent the neighbors this border can make a barrier with*/
	public List<Border> Neighbours(){
		
		List<Border> neighbours = new ArrayList<Border>();

		//choosing what list to consider form the MainActivity static variables (vertical_borders, horizontal_borders) according to rotation
		BorderList list = isVertical() ? MainActivity.vertical_borders : MainActivity.horizontal_borders;
		
			if(isVertical()) {
				if(x>0) { //check if it is a border that is at the end
					if(!list.getBorder(x-1, y).isBarrier()) { //check if the neighbor border is a barrier or not
						neighbours.add(list.getBorder(x-1, y));
					}
				}
				
				if(x<list.getrows()-1) { //check if it is a border that is at the end
					if(!list.getBorder(x+1, y).isBarrier()) { //check if the neighbor border is a barrier or not
						neighbours.add(list.getBorder(x+1, y));
					}
				}
				
			}else {
				if(y>0) { //check if it is a border that is at the end
					if(!list.getBorder(x, y-1).isBarrier()) { //check if the neighbor border is a barrier or not
						neighbours.add(list.getBorder(x, y-1));
					}
				}
				
				if(y<list.getcol()-1) { //check if it is a border that is at the end
					if(!list.getBorder(x, y+1).isBarrier()) { //check if the neighbor border is a barrier or not
						neighbours.add(list.getBorder(x, y+1));
					}
				}
			}
		
		return neighbours;
	}
}
