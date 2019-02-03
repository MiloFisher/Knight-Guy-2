public class Lootbag
{
	int row;
	int col;
	String[] items;
	public Lootbag(int row,int col, String[] items)
	{
		this.row = row;
		this.col = col;
		this.items = items;
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
	public String[] getItems()
	{
		return items;
	}
	public void setItems(String[] items)
	{
		this.items = items;
	}
}
