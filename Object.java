import java.awt.*;
import java.io.Serializable;
public class Object implements Serializable
{
	private static final long serialVersionUID = 1L;
	String type;
	int phase;
	Image image1;
	Image image2;
	int version;
	Object(){};
	
	public Object(String type, int phase, Image image1, Image image2, int version)
	{
		this.type = type;
		this.phase = phase;
		this.image1 = image1;
		this.image2 = image2;
		this.version = version;
	}
	String Type()
	{
		return type;
	}
	int getPhase()
	{
		return phase;
	}
	void setPhase(int phase)
	{
		this.phase = phase;
	}
	Image Image1()
	{
		return image1;
	}
	Image Image2()
	{
		return image2;
	}
	int getVersion()
	{
		return version;
	}
	
}
