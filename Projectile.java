
public class Projectile 
{
	String name;
	int row;
	int col;
	int rowMod;
	int colMod;
	int direction;
	boolean isStuck;
	public Projectile(String name, int row, int col, int rowMod, int colMod, int direction, boolean isStuck)
	{
		this.name = name;
		this.row = row;
		this.col = col;
		this.rowMod = rowMod;
		this.colMod = colMod;
		this.direction = direction;
		this.isStuck = isStuck;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getRow()
	{
		return row;
	}
	public void setRow(int row)
	{
		this.row = row;
	}
	public int getCol()
	{
		return col;
	}
	public void setCol(int col)
	{
		this.col = col;
	}
	public int getRowMod()
	{
		return rowMod;
	}
	public void setRowMod(int rowMod)
	{
		this.rowMod = rowMod;
	}
	public int getColMod()
	{
		return colMod;
	}
	public void setColMod(int colMod)
	{
		this.colMod = colMod;
	}
	public int getDirection()
	{
		return direction;
	}
	public void setDirection(int direction)
	{
		this.direction = direction;
	}
	public boolean getStuck()
	{
		return isStuck;
	}
	public void setStuck(boolean isStuck)
	{
		this.isStuck = isStuck;
	}
	public void moveUp()
	{
		this.row = this.row - 1;
	}
	public void moveDown()
	{
		this.row = this.row + 1;
	}
	public void moveLeft()
	{
		this.col = this.col - 1;
	}
	public void moveRight()
	{
		this.col = this.col + 1;
	}
}
