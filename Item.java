
import java.awt.*;
import java.io.Serializable;
public class Item implements Serializable
{
	private static final long serialVersionUID = 1L;
	String type;
	String name;
	Image iconImage;//custom serialization  
	Image image1;
	Image image2;
	Image image3;
	Image image4;
	Image image5;
	Image image6;
	Image image7;
	Image image8;
	int protection;
	int damage;
	int stack;
	
	public Item(){};
	
	public Item(String type, String name, Image iconImage, Image image1, Image image2, Image image3, Image image4, Image image5, Image image6, Image image7, Image image8, int protection, int damage, int stack)
	{
		this.type = type;
		this.name = name;
		this.iconImage = iconImage;
		this.image1 = image1;
		this.image2 = image2;
		this.image3 = image3;
		this.image4 = image4;
		this.image5 = image5;
		this.image6 = image6;
		this.image7 = image7;
		this.image8 = image8;
		this.protection = protection;
		this.damage = damage;
		this.stack = stack;
	}
	String Type()
	{
		return type;
	}
	String Name()
	{
		return name;
	}
	Image iconImage()
	{
		return iconImage;
	}
	Image Image1()
	{
		return image1;
	}
	Image Image2()
	{
		return image2;
	}
	Image Image3()
	{
		return image3;
	}
	Image Image4()
	{
		return image4;
	}
	Image Image5()
	{
		return image5;
	}
	Image Image6()
	{
		return image6;
	}
	Image Image7()
	{
		return image7;
	}
	Image Image8()
	{
		return image8;
	}
	int Protection()
	{
		return protection;
	}
	int Damage()
	{
		return damage;
	}
	int Stack()
	{
		return stack;
	}
	void setStack(int stacks)
	{
		this.stack = stacks;
	}
}
