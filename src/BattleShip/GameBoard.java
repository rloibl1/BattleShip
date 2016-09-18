package BattleShip;
import java.util.ArrayList;;

public class GameBoard
{
	int rowCount = 10;
	int colCount = 10;
	
	final String LINE_END = System.getProperty("line.separator"); //***can remove this***
	
	ArrayList< ArrayList< Cell > > cells;
	ArrayList< Ship > myShips = new ArrayList<Ship>();
	
	public GameBoard( int rowCount, int colCount )
	{
		//create the 2D array of cells
		this.rowCount = rowCount;
		this.colCount = colCount;
		
		//Initialize rows
		this.cells = new ArrayList<ArrayList< Cell >>(rowCount);
		
		for(int i=0; i < colCount; i++ ){ //Size
			//Initialize columns
			ArrayList<Cell> column = new ArrayList< Cell >(colCount); //Capacity			
			for(int j=0; j < rowCount; j++){
				column.add(new Cell());				
			}
			this.cells.add(column);
		}		

	}
	
	public String draw()
	{
		StringBuilder sb = new StringBuilder();
		ArrayList< Cell > column;
		Cell c;
		
		//Upper border
		for(int i=0; i< this.colCount + 2; i++){
			sb.append("=");
		}
		sb.append(System.lineSeparator());
		
		//Game board cells and side borders 
		for (int i=0; i < this.cells.size(); i++){
			sb.append("|");
			column = this.cells.get(i);
			for(int j=0; j < column.size(); j++){
				c = column.get(j);
				sb = sb.append(c.draw());;
			}
			System.out.println("");
			sb.append("|");
			sb.append(System.lineSeparator());
		}
		
		//Lower border
		for(int i=0; i< this.colCount + 2; i++){
			sb.append("=");
		}
		sb.append(System.lineSeparator());
		return sb.toString();
	}
	
	//add in a ship if it fully 1) fits on the board and 2) doesn't collide w/
	//an existing ship.
	//Returns true on successful addition; false, otherwise
	public boolean addShip( Ship s , Position sternLocation, HEADING bowDirection )
	{
		int ship_length = s.getLength();		
		Cell c =  new Cell();
		
		//Determine heading for math operations
		//First check is if the ship will fit on the game board
		//Second check it if there are any other ships in the way
		
		//Subtract from rows
		if(bowDirection == HEADING.NORTH){
			if(sternLocation.y - ship_length >= 0){
				for(int i=0; i < ship_length; i++){
					c = cells.get(sternLocation.y - i).get(sternLocation.x);
					if(c.getShip() == null){ //Cell is empty
						//Add ship to the cell
						c.setShip(s); 
						cells.get(sternLocation.y - i).set(sternLocation.x, c);
					}
					else{return false;} //Ship already in cell					
				}
				//All cells allocated, ship successfully placed
				this.myShips.add(s);
				return true;
			}
			else{return false;} //No room for ship
		}
		//Add to rows
		else if(bowDirection == HEADING.SOUTH){
			if(sternLocation.y + ship_length <= this.rowCount){
				for(int i=0; i < ship_length; i++){
					c = cells.get(sternLocation.y + i).get(sternLocation.x);
					if(c.getShip() == null){ //Cell is empty
						//Add ship to the cell
						c.setShip(s); 
						cells.get(sternLocation.y + i).set(sternLocation.x, c);
					}
					else{return false;} //Ship already in cell
				}				
				//All cells allocated, ship successfully placed
				this.myShips.add(s);
				return true;
			}
			else{return false;} //No room for ship
		}
		//Subtract from columns
		else if(bowDirection == HEADING.WEST){
			if(sternLocation.x - ship_length >= 0){
				for(int i=0; i < ship_length; i++){
					c = cells.get(sternLocation.y).get(sternLocation.x - i);
					if(c.getShip() == null){ //Cell is empty
						//Add ship to the cell
						c.setShip(s);
						cells.get(sternLocation.y).set(sternLocation.x - i, c);
					}
					else{return false;} //Ship already in cell
				}
				//All cells allocated, ship successfully placed
				this.myShips.add(s);
				return true;
			}
			else{return false;} //No room for ship
		}
		//Add to columns
		else if(bowDirection == HEADING.EAST){
			if(sternLocation.x + ship_length <= this.colCount){
				for(int i=0; i < ship_length; i++){
					c = cells.get(sternLocation.y).get(sternLocation.x + i);
					if(c.getShip() == null){ //Cell is empty
						//Add ship to the cell
						c.setShip(s); 
						cells.get(sternLocation.y).set(sternLocation.x + i, c);
					}
					else{return false;} //Ship already in cell
				}
				//All cells allocated, ship successfully placed
				this.myShips.add(s);
				return true;
			}
			else{return false;} //No room for ship
		}
		else {return false;} //Heading error
	}

	//Returns A reference to a ship, if that ship was struck by a missle.
	//The returned ship can then be used to print the name of the ship which
	//was hit to the player who hit it.
	//Ensure you handle missiles that may fly off the grid
	public Ship fireMissle( Position coordinate )
	{
		Ship s = cells.get(coordinate.y).get(coordinate.x).ship;
		
		if(s != null){
			cells.get(coordinate.y).get(coordinate.x).hasBeenStruckByMissile(true);
			cells.get(coordinate.y).get(coordinate.x).ship = null;
		}
		
		return s;
	}
	
	//Here's a simple driver that should work without touching any of the code below this point
	public static void main( String [] args )
	{
		System.out.println( "Hello World" );
		GameBoard b = new GameBoard( 10, 10 );	
		System.out.println( b.draw() );
		
		Ship s = new Cruiser( "Cruiser" );
		if( b.addShip(s, new Position(3,6), HEADING.WEST ) )
			System.out.println( "Added " + s.getName() + "Location is " );
		else
			System.out.println( "Failed to add " + s.getName() );
		
		s = new Destroyer( "Vader" );
		if( b.addShip(s, new Position(3,5), HEADING.NORTH ) )
			System.out.println( "Added " + s.getName() + "Location is " );
		else
			System.out.println( "Failed to add " + s.getName() );
		
	
		System.out.println( b.draw() );
		
		b.fireMissle( new Position(3,5) );
		System.out.println( b.draw() );
		b.fireMissle( new Position(3,4) );
		System.out.println( b.draw() );
		b.fireMissle( new Position(3,3) );
		System.out.println( b.draw() );
		
		b.fireMissle( new Position(0,6) );
		b.fireMissle( new Position(1,6) );
		b.fireMissle( new Position(2,6) );
		b.fireMissle( new Position(3,6) );
		System.out.println( b.draw() );
		
		b.fireMissle( new Position(6,6) );
		System.out.println( b.draw() );
	}

}
