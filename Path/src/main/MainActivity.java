package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import components.Border;
import components.BorderList;
import components.Box;
import components.Pawn;
import components.Plateau;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This is the Main Activity in which the program starts
 */

public class MainActivity extends Application {

	public static Label player_turn_label ;
	public static Button barrier_btn, move_btn, new_game_btn;
	static GridPane grid;
	public static Plateau pl;
	public static BorderList vertical_borders, horizontal_borders;
	
	static Circle[] circles;
	static Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
	
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {		
		launch(args);
	}

	/**
	 * It's the method that starts the JavaFX stage
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Path");
		
		//load the game 
		loadGame();
		
		//root container with two columns
		HBox root = new HBox(2);
		
		//create the grid for playground
		grid = new GridPane();
		createGridPane(pl,vertical_borders,horizontal_borders);
		HBox.setMargin(grid, new Insets(100,100,100,100));
		
		//add the pawns to the grid
		drawPawns();
		
		//container for buttons and text with 4 rows
		VBox container = createContainer(primaryStage);
		
		//adding containers in the root container
		root.getChildren().addAll(container, grid);
		root.setStyle("-fx-background-color: #46315c;");
		
		Scene scene = new Scene(root, 1080, 700);
		
		//save the game once the window is closed with setOnCloseRequest handler
		primaryStage.setOnCloseRequest(e -> saveGame());
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	/**
	 * Sets the static variables pl (which is the plateau that contains the boxes), vertical borders and horizontal orders
	 * It takes the data from the file myObjects.txt
	 * If the file is not found, it creates new objects for a game for 2 players*/
	private void loadGame() {
		try {
			FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			pl = (Plateau) oi.readObject();
			vertical_borders = (BorderList) oi.readObject();
			horizontal_borders = (BorderList) oi.readObject();

			oi.close();
			fi.close();
			
			System.out.println("it was a success loading");

		} catch (FileNotFoundException e) {
			pl =  new Plateau(2);
			vertical_borders = new BorderList(9, 10, 'V');
			horizontal_borders = new BorderList(10, 9, 'H');
			
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	/**
	 * Saves the pl, vertical_borders, and horizontal borders in a file called myObjects.txt
	 * It is called only when the game is closed*/
	private void saveGame() {
		
		try {
			FileOutputStream f = new FileOutputStream(new File("myObjects.txt"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(pl);
			o.writeObject(vertical_borders);
			o.writeObject(horizontal_borders);
			
			o.close();
			f.close();

			System.out.println("it was a success saving");

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		} 
	}

	/**
	 * It returns the corresponding node, given a row and a column, in the static GridPane variable grid
	 * It is used by both Box and Border classes 
	 * It is static and public*/
	public static Node getNodeByRowColumnIndex (final int row, final int column) {
		
	    Node result = null;
	    ObservableList<Node> childrens = grid.getChildren();

	    for (Node node : childrens) {
	        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
	            result = node;
	            break;
	        }
	    }

	    return result;
	}
	
	/**
	 * It makes the javaFX Circle objects to be displayed in the GridPane corresponding to the pawns
	 * It sets the static variable circles
	 * It is called only once at the beginning of each game*/
	public void makeCircles() {
		
		circles = new Circle[pl.getNumberPlayers()];
		for(int i = 0; i < pl.getNumberPlayers(); i++) {
			Circle pawn = new Circle();
			pawn.setCenterX(25);
			pawn.setCenterY(25);
			pawn.setRadius(20);
			pawn.setFill(colors[i]);
			circles[i] = pawn;
		}
	}
	
	/**
	 * It updates the static variable grid which is a GridPane
	 * It updates the new positions of the Circles which correspond to pawns in the grid
	 * It removes the circle and put it again in the right position corresponding to the pawn box*/
	public static void drawPawns() {
		
		for(Pawn p : pl.getPawns()){
			if(grid.getChildren().contains(circles[p.getName()])) {
				grid.getChildren().remove(circles[p.getName()]);
			}
			
			grid.add(circles[p.getName()], p.getBox().gety()*2+1, p.getBox().getx()*2+1);
			GridPane.setHalignment(circles[p.getName()], HPos.CENTER);
			
		}
	}
	
	/**
	 * It sets the static GridPane variable called grid
	 * It takes as parameters a Plateau object and 2 BorderList objects corresponding to vertical and horizontal
	 * It creates the button objects and adds them to the grid and add the EventHandlers who point at the corresponding method in either the 
	 * Box or Border classes
	 * The makeCircles method is called within this method, at the end of it*/	
	private void createGridPane(Plateau pl, BorderList vl, BorderList hl) {

        int size = pl.getPlateau().length;
        
        for (int row = 0; row < size; row++) {
        	
            for (int col = 0; col < size; col++) {
            	
            	Button cell = new Button();
            	Box myBox = pl.getBox(row, col);
            	
            	cell.setOnAction(e -> myBox.onClickBox());
            	cell.setStyle("-fx-background-color: gray;");
            	cell.setMinSize(50, 50);
            	cell.setMaxSize(50, 50);
            	
            	Button verticalBorder = new Button();
                Button horizontalBorder = new Button();
            	Border vertical_border = vl.getBorder(row, col);           
                Border horizontal_border = hl.getBorder(row, col);
                
                String Vcolor = vertical_border.isBarrier() ? "-fx-background-color: black;": "-fx-background-color: #6d88b3;";
                String Hcolor = horizontal_border.isBarrier() ? "-fx-background-color: black;": "-fx-background-color: #6d88b3;";
                
                verticalBorder.setStyle(Vcolor);
                horizontalBorder.setStyle(Hcolor);
                verticalBorder.setMinSize(5, 50);
                verticalBorder.setMaxSize(5, 50);
                horizontalBorder.setMinSize(50, 5);
                horizontalBorder.setMaxSize(50, 5);
                
                verticalBorder.setOnMouseMoved(e -> vertical_border.onMouseMoved(e));
                verticalBorder.setOnMouseClicked(e -> vertical_border.onMouseClicked());
                verticalBorder.setOnMouseExited(e -> vertical_border.onMouseExited());
                
                horizontalBorder.setOnMouseMoved(e -> horizontal_border.onMouseMoved(e));
                horizontalBorder.setOnMouseClicked(e -> horizontal_border.onMouseClicked());
                horizontalBorder.setOnMouseExited(e -> horizontal_border.onMouseExited());

                grid.add(cell, col*2+1, row*2+1);
                grid.add(horizontalBorder, col*2+1, row*2);
                grid.add(verticalBorder, col*2, row*2+1);
                
            }
            
            
            Button verticalBorder = new Button();
            Button horizontalBorder = new Button();
            Border outter_vertical_border = vl.getBorder(row, size);
            Border outter_horizontal_border = hl.getBorder(size, row);
            
            String Vcolor = outter_vertical_border.isBarrier() ? "-fx-background-color: black;": "-fx-background-color: #6d88b3;";
            String Hcolor = outter_horizontal_border.isBarrier() ? "-fx-background-color: black;": "-fx-background-color: #6d88b3;";
            
            verticalBorder.setStyle(Vcolor);
            horizontalBorder.setStyle(Hcolor);
            verticalBorder.setMinSize(5, 50);
            verticalBorder.setMaxSize(5, 50);
            horizontalBorder.setMinSize(50, 5);
            horizontalBorder.setMaxSize(50, 5);
            
            verticalBorder.setOnMouseMoved(e -> outter_vertical_border.onMouseMoved(e));
            verticalBorder.setOnMouseClicked(e -> outter_vertical_border.onMouseClicked());
            verticalBorder.setOnMouseExited(e -> outter_vertical_border.onMouseExited());
            
            horizontalBorder.setOnMouseMoved(e -> outter_horizontal_border.onMouseMoved(e));
            horizontalBorder.setOnMouseClicked(e -> outter_horizontal_border.onMouseClicked());
            horizontalBorder.setOnMouseExited(e -> outter_horizontal_border.onMouseExited());
            
            grid.add(horizontalBorder, row*2+1, size*2);
            grid.add(verticalBorder, size*2, row*2+1);
            
        }
        makeCircles();
    }
	
	/**
	 * It returns a VBox that contains the left part of the layout, including the player turn label and the buttons
	 * It takes the primaryStage as a parameter just to use it as parameter to call the newGameBtnHandler, as that class needs 
	 * the primary stage to make a window who's parent window is the primary stage*/
	private VBox createContainer(Stage primaryStage) {
		
		VBox container = new VBox(4);
		
		// label for indicating player turn
		player_turn_label = new Label();
		player_turn_label.setText("Player " + String.valueOf(pl.getTurn()+1));
		player_turn_label.setFont(new Font("Arial", 30));
		player_turn_label.setTextFill(Color.web("#0076a3"));
		player_turn_label.setStyle("-fx-font-weight: bold;");
		VBox.setMargin(player_turn_label, new Insets(5,0,100,50));
		
		//button for adding barrier
		barrier_btn = new Button();
		set_background(barrier_btn, "barrier", false);
		barrier_btn.setOnAction(new BarrierBtnHandler());
		
		//button for moving pawn
		move_btn = new Button();
		set_background(move_btn, "move", false);
		move_btn.setOnAction(new MoveBtnHandler());
		
		//button for a new game
		new_game_btn = new Button();
		set_background(new_game_btn, "new_game", false);
		VBox.setMargin(new_game_btn, new Insets(100,0,0,50));
		new_game_btn.setOnAction(new GameBtnHandler(primaryStage));
		
		//adding items in container	
		container.getChildren().addAll(player_turn_label, barrier_btn, move_btn, new_game_btn);
		
		return container;
	}
	
	/**
	  * Its a static method that takes in as parameters a Button, a String and a boolean
	  * It sets the size and the background of the button, with the image which name is taken from the string
	  * The boolean called selected is used to see if the button is selected for the case of the move_btn and the barrier_btn 
	  * if the button is selected change the size and margin and add S to the image name*/
	public static void set_background(Button btn, String image, Boolean selected) {
		 
		
		if (selected) {  
			VBox.setMargin(btn, new Insets(3,3,3,33));
			btn.setMinSize(239, 104);
			image = image + "S";
		}else {
			VBox.setMargin(btn, new Insets(20,20,20,50));
			btn.setMinSize(205, 70);
		}
		BackgroundImage backgroundImage = new BackgroundImage( new Image( MainActivity.class.getResource("/images/"+image+".png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		btn.setBackground(new Background(backgroundImage));
		
	}
	
	
	/**
	 * It's the barrier_btn onClick handler
	 * It checks if the game is not over and the number of barriers is below 20
	 * It changes the booleans CanClickBorder and CanClickBox accordingly
	 * It change the color of ClickableBoxes, if it wasn't the original color
	 * It sets the barrier_btn background as selected and the move_btn as not selected*/
	class BarrierBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			
			if(!pl.isGameOver() && pl.getNumberBarriere() < 20) {
				
				pl.setCanClickBorder(true);
				pl.setCanClickBox(false);
				
				pl.removeClickableBoxes();
				
				set_background(barrier_btn, "barrier", true);
				set_background(move_btn, "move", false);
			}
			
		}
		
	}
	
	/**
	 * It's the move_btn onClick handler
	 * It checks if the game is not over 
	 * It changes the booleans CanClickBorder and CanClickBox accordingly
	 * It change the color of ClickableBoxes according the color of the pawn using the Turn variable
	 * It sets the move_btn background as selected and the barrier_btn as not selected*/
	
	class MoveBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			
			if(!pl.isGameOver()) {
				
				pl.setCanClickBorder(false);
				pl.setCanClickBox(true);
				
				pl.setClickableBoxes(pl.getTurn());
				
				set_background(barrier_btn, "barrier", false);
				set_background(move_btn, "move", true);
				
			}
			
					
		}
		
	}
	
	/**
	 * It's the new game button onClick handler
	 */
	class GameBtnHandler implements EventHandler<ActionEvent> {
		
		private Stage primaryStage;
		
		/**
		 * It takes the primary Stage as a parameter, to be used as the parentWindow for the new window of the menu*/
		public GameBtnHandler(Stage primaryStage) {
			this.primaryStage = primaryStage;
		}
		
		/**
		 * It is called when a new game button is clicked
		 * It creates new objects for the new game
		 * It sets the static variables pl, vertical_borders and horizontal_borders with the new objects
		 * It clears the static variable grid and calls the CreateGridPane method to make the new grid 
		 * It sets the player_turn_label and sets the barrier_btn and the move_btn to not selected
		 * It calls the drawPawns method to draw the pawns
		 * It takes the number of players and the window as parameters
		 * The window which is the menu is then hidden*/
		public void buttonClicked(int numbPlayers, Stage window) {
			
			try {
				pl = new Plateau(numbPlayers);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			vertical_borders = new BorderList(9, 10, 'V');
			horizontal_borders = new BorderList(10, 9, 'H');
			
			grid.getChildren().clear();
			
			createGridPane(pl,vertical_borders,horizontal_borders);
			
			player_turn_label.setText("Player " + String.valueOf(1));
			set_background(barrier_btn, "barrier", false);
			set_background(move_btn, "move", false);
			
			drawPawns();
			
			window.hide();
		}
		
		/**
		 * It creates a new window which is the menu from which you can chose the type of the new game you want
		 * It contains a label and 2 buttons
		 * Its parent window is the primary stage taken as parameter in the construction of the class
		 * Both the buttons call the buttonClicked method, but with different parameters*/
		@Override
		public void handle(ActionEvent event) {
			
			//create the label newGame
			Label newGame = new Label("New Game :");
			newGame.setFont(new Font("Arial", 30));
			newGame.setTextFill(Color.web("#0076a3"));
			newGame.setStyle("-fx-font-weight: bold;");
			
			//create the 2 buttons to chose the type of the new game
			Button two_players = new Button();
			Button four_players = new Button();
			two_players.setMinSize(205, 70);
			four_players.setMinSize(205, 70);
			
			//set the background image of the buttons
			BackgroundImage backgroundImage = new BackgroundImage( new Image( MainActivity.class.getResource("/images/2players.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
			two_players.setBackground(new Background(backgroundImage));
			BackgroundImage backgroundImage1 = new BackgroundImage( new Image( MainActivity.class.getResource("/images/4players.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
			four_players.setBackground(new Background(backgroundImage1));

			//make the container of the window
            VBox secondaryLayout = new VBox(4);
			
			secondaryLayout.setSpacing(30);
			secondaryLayout.setAlignment(Pos.CENTER);
			
            secondaryLayout.getChildren().addAll(newGame, two_players, four_players);
            secondaryLayout.setStyle("-fx-background-color: #bccee6;");
            
            Scene secondScene = new Scene(secondaryLayout, 500, 700);

            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.setTitle("Menu");
            newWindow.setScene(secondScene);

            // Specifies the modality for new window.
            newWindow.initModality(Modality.WINDOW_MODAL);

            // Specifies the owner Window (parent) for new window
            newWindow.initOwner(primaryStage);

	        newWindow.show();
	        
	        //set the onAction Handlers for the 2 buttons
	        two_players.setOnAction(e -> buttonClicked(2, newWindow));
	        four_players.setOnAction(e -> buttonClicked(4, newWindow));
			
			
		}
		
	}
}
