public class Enemy 
{
	int row;
	int col;
	String name;
	String type;
	int health;
	int damage;
	int direction;
	Node targetNode;
	int xshift;
	int yshift;
	boolean damaged;
	int attackNum;
	boolean hasLineOfSight;
	int id;
	public Enemy(int row, int col, String name, String type, int health, int damage, int direction, Node targetNode, int xshift, int yshift, boolean damaged, int attackNum, boolean hasLineOfSight,int id)
	{
		this.row = row;
		this.col = col;
		this.name = name;
		this.type = type;
		this.health = health;
		this.damage = damage;
		this.direction = direction;
		this.targetNode = targetNode;
		this.xshift = xshift;
		this.yshift = yshift;
		this.damaged = damaged;
		this.attackNum = attackNum;
		this.hasLineOfSight = hasLineOfSight;
		this.id = id;
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
	public String getName()
	{
		return name;
	}
	public void setName(String type)
	{
		this.type = type;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String name)
	{
		this.name = name;
	}
	public int getHealth()
	{
		return health;
	}
	public void setHealth(int health)
	{
		this.health = health;
	}
	public int getDamage()
	{
		return damage;
	}
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
	public int getDirection()
	{
		return direction;
	}
	public void setDirection(int direction)
	{
		this.direction = direction;
	}
	public Node getTargetNode()
	{
		return targetNode;
	}
	public void setTargetNode(Node targetNode)
	{
		this.targetNode = targetNode;
	}
	public int getXShift()
	{
		return xshift;
	}
	public void setXShift(int xshift)
	{
		this.xshift = xshift;
	}
	public int getYShift()
	{
		return yshift;
	}
	public void setYShift(int yshift)
	{
		this.yshift = yshift;
	}
	public boolean getDamaged()
	{
		return damaged;
	}
	public void setDamaged(boolean damaged)
	{
		this.damaged = damaged;
	}
	public int getAttackNum()
	{
		return attackNum;
	}
	public void setAttackNum(int attackNum)
	{
		this.attackNum = attackNum;
	}
	public boolean getHasLineOfSight()
	{
		return hasLineOfSight;
	}
	public void setHasLineOfSight(boolean hasLineOfSight)
	{
		this.hasLineOfSight = hasLineOfSight;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
}
