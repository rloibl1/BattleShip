package BattleShip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Client
{
	final String NEWL = System.getProperty("line.separator");
	
	private String name = null;
	PrintWriter out = null;
	BufferedReader in = null;
	GameManager man = null;
	GameBoard board = new GameBoard(10,10);
	GameBoard targets = new GameBoard(10,10);
	
	Client( BufferedReader in, PrintWriter out, GameManager manager )
	{
		this.in = in;
		this.out = out;
		this.man = manager;
	}
	
	public void playGame() throws IOException
	{
		this.out.println( NEWL + NEWL + "   Missiles Away! Game has begun" );
		this.out.println( "   To Launch a missle at your enemy:" );
		this.out.println( "F 2 4" );
		this.out.println( "Fires a missile at coordinate x=2, y=4." );
		
		while(true) // put Code Here to process in game commands, after each command, print the target board and game board w/ updated state )
		{
			out.println( "------------------------" );
			out.println( "Target Board:" + this.targets.draw() );
			out.println( "Your Ships: " + this.board.draw() );
			out.println( "   Waiting for Next Command...\n\n" );
			out.flush();
			
			//Read next command
			processCommand();
			
			//Perform test here to see if we have won or lost
			
			//Win
			if(allEnemyShipsAreDestroyed()){
				break;
			}	
			//Loss
			if(allMyShipsAreDestroyed()){
				break;
			}			
		}
	}
	
	//Returns a bool, true iff all of this client's ships are destroyed
	boolean allMyShipsAreDestroyed()
	{
		if(board.myShips.size() != 0){
			return false;
		}
		else{
			return true;
		}
	}

	//Returns a bool, true iff all of the opponent's ships are destroyed
	boolean allEnemyShipsAreDestroyed()
	{
		if(targets.myShips.size() != 0){
			return false;
		}
		else{
			return true;
		}
	}

	//"F 2 4" = Fire command
	//"C Hello world, i am a chat message"
	//"D" - Redraw the latest game and target boards
	boolean processCommand() throws IOException
	{
		//Fire
		if(in.read() == 'F'){
			String [] s = new String[1];
			Integer i = in.read();
			s[0] = i.toString();
			i = in.read();
			s[1] = i.toString();
			
			if(processFireCmd(s)){
				return true;
			}
			else{
				return false;
			}
		}
		//Chat
		else if(in.read() == 'C'){
			String s  = in.readLine();
			
			if(processChatCmd(s)){
				return true;
			}
			else{
				return false;
			}
		}		
		else{
			//Invalid command
			throw new IOException(); //***This could be incorrect***
		}
	}
	
	//When a fire command is typed, this method parses the coordinates and launches a missle at the enemy
	boolean processFireCmd( String [] s )
	{
		//Do checking to make sure the fire command is valid here, i.e the missile isn't shooting off the board
		int x = Integer.parseInt(s[0]);
		int y = Integer.parseInt(s[1]);
		Position coordinate;
		
		if( x >= 0 && x <= board.colCount){ //***check if x is columns or y is columns***
			if(y >= 0 && y <= board.rowCount){
				coordinate = new Position(x,y);
				board.fireMissle(coordinate);
				return true; //Missile fired
			}
		}
		return false; //Invalid command params
	}
	
	//Send a message to the opponent
	boolean processChatCmd( String s )
	{
		if(s.length() <= 80){ //Enforce a 80 character chat limit
			//send the message, I assume it will have to be sent using a method in the gamemanager.java file
			return true;
		}
		else{
			return false; //Message over 80 characters
		}
	}
	
	GameBoard getGameBoard() { return this.board; }
	
	public void initPlayer() throws IOException
	{
		int num_ships = 0;
		boolean destroyer = false;
		boolean cruiser = false;
		int x;
		int y;
		int heading;
		String ship_name;
		Ship s;
		Position sternLocation;
		HEADING bowDirection;
		
		//Player name
		while(this.name == null){
			//How to check if the player has actually typed something?
			out.print("   Enter your name: " );
			this.name = in.readLine();	
		}
		
		
		//2.Print out instructions
		
		//Here's some nice instructions to show a client		
		//		out.println("   You will now place 2 ships. You may choose between either a Cruiser (C) " );
		//		out.println("   and Destroyer (D)...");
		//		out.println("   Enter Ship info. An example input looks like:");
		//		out.println("\nD 2 4 S USS MyBoat\n");
		//		out.println("   The above line creates a Destroyer with the stern located at x=2 (col)," );
		//		out.println("   y=4 (row) and the front of the ship will point to the SOUTH (valid" );
		//		out.println("   headings are N, E, S, and W.\n\n" );
		//		out.println("   the name of the ship will be \"USS MyBoat\"");
		//		out.println("Enter Ship 1 information:" );
		//		out.flush();
		
		//Get ship locations from the player for all 2 ships (or more than 2 if you're using more ships)
		
		while(num_ships < 2){
			//Cruiser
			if(in.read() == 'C' && cruiser == false){
				//Position
				x = in.read();
				y = in.read();
				sternLocation = new Position(x,y);
				
				//Heading
				heading = in.read();
				
				if(heading == 'N'){
					bowDirection = HEADING.NORTH;
				}
				else if(heading == 'S'){
					bowDirection = HEADING.SOUTH;
				}
				else if(heading == 'E'){
					bowDirection = HEADING.EAST;
				}
				else if(heading == 'W'){
					bowDirection = HEADING.WEST;
				}
				else{
					//Invalid heading
					return; //***Change this later***
				}
				
				//Ship name
				ship_name = in.readLine();
				s = new Cruiser(ship_name);
				
				//Add ship
				this.board.addShip(s, sternLocation, bowDirection);
				num_ships++;
			}
			//Destroyer
			else if(in.read() == 'D' && destroyer == false){
				//Position
				x = in.read();
				y = in.read();
				sternLocation = new Position(x,y);
				
				//Heading
				heading = in.read();
				
				if(heading == 'N'){
					bowDirection = HEADING.NORTH;
				}
				else if(heading == 'S'){
					bowDirection = HEADING.SOUTH;
				}
				else if(heading == 'E'){
					bowDirection = HEADING.EAST;
				}
				else if(heading == 'W'){
					bowDirection = HEADING.WEST;
				}
				else{
					//Invalid heading
					return; //***Change this later***
				}
				
				//Ship name
				ship_name = in.readLine();
				s = new Destroyer(ship_name);
				
				//Add ship
				this.board.addShip(s, sternLocation, bowDirection);
				num_ships++;
			}
		}
		
		//After all game state is inputed, draw the game board to the client
		this.board.draw();
		
		System.out.println( "Waiting for other player to finish their setup, then war will ensue!" );
	}
	
	String getName() { return this.name; }
	
	public static void main( String [] args )
	{
		
		
	}
}
