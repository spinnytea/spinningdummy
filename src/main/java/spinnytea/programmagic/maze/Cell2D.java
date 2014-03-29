package spinnytea.programmagic.maze;

/**
 * <p/>
 * A square "room" on a 2D plane.
 * <p/>
 * This contains a door in all four directions in the same way a doubly-linked-list has a link in both directions.
 * <p/>
 * <b>XXX</b> This certainly isn't the best way to store all the information, but, I need to start somewhere. I'll improve after I get something working.<br>
 */
public class Cell2D
{
	public static enum Direction
	{
		NORTH,
		SOUTH,
		EAST,
		WEST
	}

	/** plus x - room to the east */
	public Cell2D east = null;
	/** minus x - room to the west */
	public Cell2D west = null;
	/** minus y - room to the north */
	public Cell2D north = null;
	/** plus y - room to the south */
	public Cell2D south = null;

	/** x position of the cell */
	public final int y;
	/** y position of the cell */
	public final int x;

	public Cell2D(int y, int x)
	{
		this.y = y;
		this.x = x;
	}

	/** @return true if this cell is connected to the maze */
	public boolean inTheMaze()
	{
		return east != null || west != null || north != null || south != null;
	}

	public Cell2D getRoom(Direction dir)
	{
		switch(dir)
		{
		case NORTH:
			return north;
		case SOUTH:
			return south;
		case EAST:
			return east;
		case WEST:
			return west;
		default:
			throw new UnsupportedOperationException("How did that direction get in there (" + dir + ")?");
		}
	}

	public void removeRoom(Direction dir)
	{
		switch(dir)
		{
		case NORTH:
			north.south = null;
			north = null;
			break;
		case SOUTH:
			south.north = null;
			south = null;
			break;
		case EAST:
			east.west = null;
			east = null;
			break;
		case WEST:
			west.east = null;
			west = null;
			break;
		default:
			throw new UnsupportedOperationException("How did that direction get in there? (" + dir + ")");
		}
	}

	/** doesn't let you set a room if it has already been set */
	public void setRoom(Direction dir, Cell2D cell)
	{
		switch(dir)
		{
		case NORTH:
			if(north != null)
				throw new IllegalArgumentException("North wall is already set.");
			if(cell.south != null)
				throw new IllegalArgumentException("South wall is already set.");
			north = cell;
			cell.south = this;
			break;
		case SOUTH:
			if(south != null)
				throw new IllegalArgumentException("South wall is already set.");
			if(cell.north != null)
				throw new IllegalArgumentException("North wall is already set.");
			south = cell;
			cell.north = this;
			break;
		case EAST:
			if(east != null)
				throw new IllegalArgumentException("East wall is already set.");
			if(cell.west != null)
				throw new IllegalArgumentException("West wall is already set.");
			east = cell;
			cell.west = this;
			break;
		case WEST:
			if(west != null)
				throw new IllegalArgumentException("West wall is already set.");
			if(cell.east != null)
				throw new IllegalArgumentException("East wall is already set.");
			west = cell;
			cell.east = this;
			break;
		default:
			throw new UnsupportedOperationException("How did that direction get in there? (" + dir + ")");
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null || getClass() != obj.getClass())
			return false;

		Cell2D cell2D = (Cell2D) obj;

		if(x != cell2D.x)
			return false;
		if(y != cell2D.y)
			return false;

		if((east == null) != (cell2D.east == null))
			return false;
		if((north == null) != (cell2D.north == null))
			return false;
		if((south == null) != (cell2D.south == null))
			return false;
		if((west == null) != (cell2D.west == null))
			return false;

		return true;
	}
}
