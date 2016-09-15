package BattleShip;

public class Destroyer extends Ship
{
	public Destroyer( String name )
	{
		super(name);
	}
	
	public char drawShipStatusAtCell( boolean isDamaged )
	{
		return 'x';
	}
	
	public int getLength()
	{
		return 2;
	}
}
