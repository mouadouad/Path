package components;

import java.io.Serializable;

/**
 * This is the class that creates the list of borders of the game
 * It is created twice, for horizontal and vertical borders as static variables in the MainActivity
 */

public class BorderList implements Serializable{
	
	private static final long serialVersionUID = 5L;
	
	private Border[][] list;
	private int rows, col;
	
	/**
	 * Its the constructor of the BorderList class it takes in as parameters the number of rows and columns of the two dimensional table of Borders
	 * and a character to set which rotation, vertical or horizontal
	 * It then creates the table accordingly
	 * @param rows
	 * @param col
	 * @param rotation
	 */
	public BorderList(int rows, int col, char rotation) {
		
		this.rows = rows;
		this.col = col;
		
		list = new Border[rows][col];
		
		for(int i = 0; i < rows; i++) {
			for (int j=0; j<col; j++) {
				Border border = new Border(i, j, rotation);
				
				//Check if the border is an outer border and then set it as a barrier
				
				if(rotation == 'V' && (j == 0|| j == col-1)) {
					border.setBarrier();
				}
				if(rotation != 'V' && (i == 0|| i == rows-1)) {
					border.setBarrier();
				}
				
				list[i][j] = border;
				
			}
		}
		
	}
	
	public Border getBorder(int x, int y) {
		return list[x][y];
	}
	
	public Border[][] getList(){
		return list;
	}
	
	public int getrows() {
		return rows;
	}
	
	public int getcol() {
		return col;
	}

}
