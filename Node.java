
public class Node 
{
	int row;
	int col;
	int cost;
	int parentRow;
	int parentCol;
	int parentTotal;
	public Node(int row, int col, int cost, int parentRow, int parentCol, int parentTotal)
	{
		this.row = row;
		this.col = col;
		this.cost = cost;
		this.parentRow = parentRow;
		this.parentCol = parentCol;
		this.parentTotal = parentTotal;
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
	public int getCost()
	{
		return cost;
	}
	public void setCost(int cost)
	{
		this.cost = cost;
	}
	public int getParentRow()
	{
		return parentRow;
	}
	public void setParentRow(int parentRow)
	{
		this.parentRow = parentRow;
	}
	public int getParentCol()
	{
		return parentCol;
	}
	public void setParentCol(int parentCol)
	{
		this.parentCol = parentCol;
	}
	public int getParentTotal()
	{
		return parentTotal;
	}
	public void setParentTotal(int parentTotal)
	{
		this.parentTotal = parentTotal;
	}
}
