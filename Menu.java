import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.JFrame;  
import javax.swing.ImageIcon;   
import java.io.*;
import javax.sound.sampled.*;
import static java.lang.System.*;
 
public class Menu extends JFrame  
{ 
	int select = 0;
	int screen = 0;
	int difficulty = 0;
	int level = 0;
	boolean newGame = false;
	boolean musicOn = true;
	Clip[] clips = new Clip[1];
	ImageIcon ii; 
	//graphics: can name them and just put them here
	Image menu=Toolkit.getDefaultToolkit().getImage("src//Images//Menu Screen Template 1.png"); 
	Image play=Toolkit.getDefaultToolkit().getImage("src//Images//Play.png"); 
	Image options=Toolkit.getDefaultToolkit().getImage("src//Images//Options.png"); 
	Image credits=Toolkit.getDefaultToolkit().getImage("src//Images//Credits.png"); 
	Image quit=Toolkit.getDefaultToolkit().getImage("src//Images//Quit.png"); 
	Image gameType=Toolkit.getDefaultToolkit().getImage("src//Images//Menu Screen Template 2.png");
	Image storyMode=Toolkit.getDefaultToolkit().getImage("src//Images//Story Mode.png");
	Image dungeonMode=Toolkit.getDefaultToolkit().getImage("src//Images//Dungeon Mode.png");
	Image optionsList=Toolkit.getDefaultToolkit().getImage("src//Images//Menu Screen Template 3.png");
	Image howToPlayText=Toolkit.getDefaultToolkit().getImage("src//Images//How To Play.png");
	Image difficultyText=Toolkit.getDefaultToolkit().getImage("src//Images//Difficulty.png");
	Image toggleMusicText=Toolkit.getDefaultToolkit().getImage("src//Images//Toggle Music.png");
	Image difficultyList=Toolkit.getDefaultToolkit().getImage("src//Images//Menu Screen Template 4.png");
	Image easy=Toolkit.getDefaultToolkit().getImage("src//Images//Easy.png");
	Image normal=Toolkit.getDefaultToolkit().getImage("src//Images//Normal.png");
	Image hard=Toolkit.getDefaultToolkit().getImage("src//Images//Hard.png");
	Image controls=Toolkit.getDefaultToolkit().getImage("src//Images//Controls1.png");
	
	DrawPanel drawPanel = new DrawPanel();
	
public Menu()  
{  
	//*start* methods
	music();
	recoverDifficulty();
    pack();
	add(drawPanel);
	
	
    Action upAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(screen != 4)
        		playClick();
        	select--;
        	if(screen == 0)
        	{
        		if(select < 0)
            		select = 3;
        	}
        	else if(screen == 1)
        	{
        		if(select < 0)
            		select = 1;
        	}
        	else if(screen == 2)
        	{
        		if(select < 0)
            		select = 2;
        	}
        	else if(screen == 3)
        	{
        		if(select < 0)
            		select = 2;
        	}
        	if(newGame)
        		newGame = false;
        	drawPanel.repaint();
        }
    };
    Action downAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(screen != 4)
        		playClick();
        	select++;
        	if(screen == 0)
        	{
        		if(select > 3)
            		select = 0;
        	}
        	else if(screen == 1)
        	{
        		if(select > 1)
            		select = 0;
        	}
        	else if(screen == 2)
        	{
        		if(select > 2)
            		select = 0;
        	}
        	else if(screen == 3)
        	{
        		if(select > 2)
            		select = 0;
        	}
        	if(newGame)
        		newGame = false;
        	drawPanel.repaint();
        }
    };
    Action enterAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(screen != 4)
        		playClick();
        	if(screen == 0)
        	{
	        	if(select == 0)
	        	{
	        		select = 0;
	        		screen = 1;
	        	}
	            if(select == 1)
	            {
	            	select = 0;
	            	screen = 2;
	            }
	            if(select == 2)
	            {
	            	//open credits
	            }
	            if(select == 3)
	            {
	            	musicOn = false;
	            	clips[0].close();
	            	setVisible(false);
	                dispose(); 
	                System.exit(0);
	            }
        	}
        	else if(screen == 1 && select == 1)//delete '&& select == 1' when you want to implement story mode
        	{
        		EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                    	//recoverDifficulty(); // if you want to make it so that they can't change their difficulty once its selected
                        Core core = new Core(); 
                        core.initializeInventory();
                        if(select == 0)
                        	core.initializeObjects();
                        try
         			   	{
                        	String file;
                        	if(select == 0)
                        		file = "Player_Data.txt";
                        	else
                        		file = "Dungeon_Player_Data.txt";
         					FileInputStream fi = new FileInputStream(new File(file));
         					ObjectInputStream oi = new ObjectInputStream(fi);
         					if((boolean)oi.readObject() == false)
         					{
         						int[] preset = new int[9];
         						try
         						{
         							String file1;
         							if(select == 0)
         								file1 = "Saved_Data.txt";
         							else
         								file1 = "Dungeon_Saved_Data.txt";
         							FileInputStream fi1 = new FileInputStream(new File(file1));
         							ObjectInputStream oi1 = new ObjectInputStream(fi1);
         							
         							for(int i = 0; i < 9; i++)
         								preset[i] = (int)oi1.readObject();
         							oi1.close();
         							fi1.close();
         						}
         						catch (FileNotFoundException e) {}
         						catch (IOException e) {}
         						catch (ClassNotFoundException e) {}
         						core.setDifficulty(difficulty);
         						core.setLevel(level);
         						core.generateLayout(select,preset);
         						core.loadPlayerData();	
         						//core.resetKillList();
         						core.setDifficulty(difficulty);
         						core.cullEnemies();
         						//core.resetPhases();
         					}
         					else
         					{
         						int[] presets = new int[9];
         						for(int i = 0; i < presets.length; i++)
         						{
         							presets[i] = (int)Math.floor(Math.random() * 3);
         						}
         						//===Test Only===
         						//presets[0] = 0;
         						//presets[1] = 0;
         						//presets[2] = 0;
         						//presets[3] = 0;
         						//presets[4] = 0;
         						//presets[5] = 0;
         						//presets[6] = 1;
         						//presets[7] = 0;
         						//presets[8] = 0;
         						//===============
         						core.setDifficulty(difficulty);
         						core.setLevel(1);
         						core.generateLayout(select,presets);
         						core.setLocation();
         						core.populateChests();
         					}
         					oi.close();
         					fi.close();
         				}
         				catch (FileNotFoundException e) {}
         				catch (IOException e) {}
         				catch (ClassNotFoundException e) {}
                        core.objectTime();
                        core.gameTime();
                        core.enemyMove();
                    }
                });
        		musicOn = false;
        		clips[0].close();
            	setVisible(false);
                dispose(); 
        	}
        	else if(screen == 2)
        	{
        		if(select == 0)
	        	{
	        		screen = 4;
	        	}
	            if(select == 1)
	            {
	            	select = 0;
	            	screen = 3;
	            }
	            if(select == 2)
	            {
	            	if(musicOn == true)
	            	{
	            		musicOn = false;
	            		clips[0].close();
	            	}
	            	else
	            	{
	            		musicOn = true;
	            		music();
	            	}
	            }
        	}
        	else if(screen == 3)
        	{
	        	if(select == 0)
	        	{
	        		difficulty = 0;
	        		select = 0;
	        		screen = 2;
	        	}
	            if(select == 1)
	            {
	            	difficulty = 1;
	        		select = 0;
	        		screen = 2;
	            }
	            if(select == 2)
	            {
	            	difficulty = 2;
	        		select = 0;
	        		screen = 2;
	            }
        	}
            drawPanel.repaint();
        }
    };
    Action escapeAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	playClick();
        	if(!newGame)
        	{
	        	if(screen == 1 || screen == 2)
	        	{
	        		select = 0;
	        		screen = 0;
	        	}
	        	else if(screen == 3 || screen == 4)
	        	{
	        		select = 0;
	        		screen = 2;
	        	}
        	}
        	else
        	{
        		newGame = false;
        	}
        	drawPanel.repaint();
        }
    };
    Action nAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(screen == 1)
        	{
        		playClick();
        		if(newGame == false)
        			newGame = true;
        		else
        		{
        		try
       			{
       			    String file;
       				if(select == 0)
       					file = "Player_Data.txt";
       				else
       					file = "Dungeon_Player_Data.txt";
       				FileOutputStream f = new FileOutputStream(new File(file));
       				ObjectOutputStream o = new ObjectOutputStream(f);
       				o.writeObject(true);
       				o.close();
       				f.close();
       			}
       			catch (IOException ex) {}
        			newGame = false;
        		}
        		level = 1;
        	}
        	drawPanel.repaint();
        }
    };
    InputMap inputMap = drawPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = drawPanel.getActionMap();

   
    inputMap.put(KeyStroke.getKeyStroke("UP"), "upAction");
    actionMap.put("upAction", upAction);
    inputMap.put(KeyStroke.getKeyStroke("DOWN"), "downAction");
    actionMap.put("downAction", downAction);
    inputMap.put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
    actionMap.put("enterAction", enterAction);
    inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "escapeAction");
    actionMap.put("escapeAction", escapeAction);
    inputMap.put(KeyStroke.getKeyStroke("N"), "nAction");
    actionMap.put("nAction", nAction);

	 //Create an ImageIcon object  
	ii=new ImageIcon(menu);
	ii=new ImageIcon(play);
	ii=new ImageIcon(options);
	ii=new ImageIcon(credits);
	ii=new ImageIcon(quit);
	ii=new ImageIcon(gameType);
	ii=new ImageIcon(storyMode);
	ii=new ImageIcon(dungeonMode);
	ii=new ImageIcon(optionsList);
	ii=new ImageIcon(howToPlayText);
	ii=new ImageIcon(difficultyText);
	ii=new ImageIcon(toggleMusicText);
	ii=new ImageIcon(difficultyList);
	ii=new ImageIcon(easy);
	ii=new ImageIcon(normal);
	ii=new ImageIcon(hard);
	ii=new ImageIcon(controls);
	 //set close operation for JFrame  
	 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	 //set JFrame size follow the image size  
	 //setSize(ii.getIconWidth(),ii.getIconHeight());  
	 setSize(1000,650);
	 //make so your JFrame can be resizable  
	 setResizable(false);  
	 //make JFrame visible. So we can see it.  
	 setVisible(true);  
}  

public void recoverDifficulty()
{
	try
	{
		String file= "Dungeon_Player_Data.txt";
		FileInputStream fi = new FileInputStream(new File(file));
		ObjectInputStream oi = new ObjectInputStream(fi);
		boolean disregard = (boolean)oi.readObject();
		level = (int)oi.readObject();
		difficulty = (int)oi.readObject();
		oi.close();
		fi.close();
	}
	catch (FileNotFoundException e) {}
	catch (IOException e) {}
	catch (ClassNotFoundException e) {}
	
}

public void music()
{
	
		//src//Sounds//knight_guy_menu_mix_1.wav
		new Thread()
		{
			@Override public void run()
			{
				
					//try
					//{
						try 
						{
							int counter = 2060;
							while(musicOn)
							{
								if(musicOn == true)
								{
									if(counter == 2060)
									{
										counter = 0;
										Clip clip = AudioSystem.getClip();
										clips[0] = clip;
										clips[0].open(AudioSystem.getAudioInputStream(new File("src//Sounds//knight_guy_menu_mix_1.wav")));
										clips[0].addLineListener(new LineListener(){
										    public void update(LineEvent e){
										        if(e.getType() == LineEvent.Type.STOP){
										            e.getLine().close();
										            
										        }
										    }
										});
										clips[0].start();
									}
									counter++;
								}
								else
								{
									break;
								}
								Thread.sleep(10);
							}
					    } 
						catch(Exception ex) {}
						
					//} catch(InterruptedException e) {}
				
			}
		}.start();
	
}

public void playClick()
{
	try 
	{
		Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(new File("src//Sounds//click1.wav")));
        clip.addLineListener(new LineListener(){
		    public void update(LineEvent e){
		        if(e.getType() == LineEvent.Type.STOP){
		            e.getLine().close();
		        }
		    }
		});
        clip.start();
    } 
	catch(Exception ex) {}
}

private class DrawPanel extends JPanel
{
	public void paint(Graphics g)  
	{  
		//This will draw drawImageIntoJFrame.jpg into JFrame  
		//put variable in location to move them in game (and size of the
		//image in pixels correlates to the grid in the jFrame)
		if(screen == 0)
		{
			g.drawImage(menu,-17 + 17,28 -28,null); 
			if(select == 0)
				g.drawImage(play,-21 + 327 + 17,28 -28 + 151,null); 
			
			if(select == 1)
				g.drawImage(options,-19 + 327 + 17,28 -28 + 151,null);
			
			if(select == 2)
				g.drawImage(credits,-17 + 327 + 17,28 -28 + 151,null); 
			
			if(select == 3)
				g.drawImage(quit,-22 + 327 + 17,28 -26 + 151,null); 
		}	
		else if(screen == 1)
		{
			g.drawImage(gameType,-17 + 17,28 -28,null); 
			if(select == 0)
			{
				g.drawImage(storyMode,-19 + 327 + 17,28 -62 + 151,null);
				g.setColor(Color.RED);
				g.drawString("<Currently Unavailable>",425 + 17,28 + 355);
//				if(!newGame)
//				{
//					try
//					{
//						FileInputStream fi = new FileInputStream(new File("Player_Data.txt"));
//						ObjectInputStream oi = new ObjectInputStream(fi);
//						if((boolean)oi.readObject() == true)
//						{
//							g.setColor(Color.GREEN);
//							g.drawString("<No saved data>",440,355);
//						}
//						else
//						{
//							g.setColor(Color.GREEN);
//							g.drawString("<Level " + (int)oi.readObject() + " Save>",445,355);
//						}
//						oi.close();
//						fi.close();
//					}
//					catch (FileNotFoundException e) {}
//					catch (IOException e) {}
//					catch (ClassNotFoundException e) {}
//				}
//				else
//				{
//					g.setColor(Color.RED);
//					g.drawString("<Erase current data?>",430,355);
//				}
			}
				 
			
			if(select == 1)
			{
				g.drawImage(dungeonMode,-14 + 327 + 17,28 + 62 + 151,null);
				if(!newGame)
				{
					try
					{
						FileInputStream fi = new FileInputStream(new File("Dungeon_Player_Data.txt"));
						ObjectInputStream oi = new ObjectInputStream(fi);
						if((boolean)oi.readObject() == true)
						{
							g.setColor(Color.GREEN);
							g.drawString("<No saved data>",440 + 17,28 + 479);
						}
						else
						{
							g.setColor(Color.GREEN);
							g.drawString("<Level " + (int)oi.readObject() + " Save>",445 + 17,28 + 479);
						}
						oi.close();
						fi.close();
					}
					catch (FileNotFoundException e) {}
					catch (IOException e) {}
					catch (ClassNotFoundException e) {}
				}
				else
				{
					g.setColor(Color.RED);
					g.drawString("<Erase current data?>",430 + 17,28 + 479);
				}
			}
		}
		else if(screen == 2)
		{
			g.drawImage(optionsList,-17+ 17,28 -28,null); 
			if(select == 0)
				g.drawImage(howToPlayText,-21 + 332 + 17,28 -28 + 177,null); 
			
			if(select == 1)
				g.drawImage(difficultyText,-19 + 322 + 17,28 -28 + 282,null);
			
			if(select == 2)
				g.drawImage(toggleMusicText,-17 + 324 + 17,28 -28 + 387,null); 
		}
		else if(screen == 3)
		{
			g.drawImage(difficultyList,-17 + 17,28 -28,null); 
			if(select == 0)
				g.drawImage(easy,-21 + 332 + 17,28 -28 + 177,null); 
			
			if(select == 1)
				g.drawImage(normal,-19 + 322 + 17,28 -28 + 282,null);
			
			if(select == 2)
				g.drawImage(hard,-17 + 324 + 17,28 -28 + 387,null); 
		}
		else if(screen == 4)
		{
			g.drawImage(controls,-17 + 17,28 -28,null);
		}
		if(screen != 4)
		{
		g.setColor(Color.MAGENTA);
			if(difficulty == 0)
				g.drawString("Difficulty: Easy",440 + 17,28 +581);
			if(difficulty == 1)
				g.drawString("Difficulty: Normal",440 + 17,28 + 581);
			if(difficulty == 2)
				g.drawString("Difficulty: Hard",440 + 17,28 + 581);
		}
	}
}
public static void main(String[]args)  
{  
 Menu miloMadeThisGame=new Menu();  
}  
}  