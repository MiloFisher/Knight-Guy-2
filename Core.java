import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.JFrame;  
import javax.swing.ImageIcon;   
import java.io.*;
import javax.sound.sampled.*;
import static java.lang.System.*;
 
public class Core extends JFrame  
{ 
	boolean drawPath = false;
	boolean showKillList = false;
	
	Clip[] music = new Clip[1];
	ArrayList<Integer> enemyKillList = new ArrayList<Integer>();
	boolean musicPlay = true;
	boolean gameOver = false;
	int gameMode = 0;
	int openlength = 0;
	int closedlength = 0;
	boolean regenerate = false;
	boolean newLevel = false;
	int[] preset = new int[9];
	ArrayList<Node> open = new ArrayList<Node>();
	ArrayList<Node> closed = new ArrayList<Node>();
	ArrayList<Node> fastestPath = new ArrayList<Node>();
	ArrayList<Lootbag> lootbags = new ArrayList<Lootbag>();
	Projectile[] projectiles = new Projectile[50];
	int projectileCounter = 0;
	int x = 488;//don't change this
	int y = 313;//don't change this

	//stats- the lower the faster
	int moveSpeed = 5;
	int attackSpeed = 75;
	int blockSpeed = 100;
	int health = 100;//out of 100
	int lives = 5;//out of whatever you want the max to be
	
	//location
	int ex = 188 + 200;
	int ey = -262 + 450 - (25*36);
	int xmod = 40; //40 is middle 
	int ymod = 76; //40 is middle
	
	int rand = 0; 
	int difficulty;
	boolean newGame = false;
	int level = 1;
	boolean canSee = false;
	int armor = 0;
	int stacks = 0;
	int stackValue = 0;
	int cooldown = 0;
	int dt = 0;
	boolean enemyInEnemy;
	int damaged = 0;
	int deathTimer = 3;
	int[][] terrain = new int[82][82];
	ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	int presetAmount = 3;
	int xsect = 0;
	int ysect = 0;
	int[] checkpointData = {x,y,ex,ey,xmod,ymod};
	boolean fInteract = false;
	boolean damageOn = false;
	String[][] mapLayout = new String[9][9];
	boolean[][] moveLayout = new boolean[82][82];
	Object [][] objects = new Object[82][82];
	ArrayList<String> mapOptions = new ArrayList<String>();
	int direction = 0;
	boolean inInventory = false;
	boolean gameFreeze = false;
	boolean isMoving = false;
	Item[] inventoryList = new Item[30];
	Item[] chestInventoryList = new Item[1000];//first chest is phase 0, so on, and each chest is 0-19 + 20 * phase 
	String[] savedNames = new String[1000];
	int[] selector = {0,1};
	int[] selected = new int[2];
	String[] filler = new String[30];
	boolean isSelected = false;
	boolean isAttacking = false;
	boolean isBlocking = false;
	boolean inMenu = false;
	boolean inOptions = false;
	boolean inControls = false;
	int menuSelector = 0;
	boolean isSaving = false;
	boolean deleteSet = false;
	boolean inChest = false;
	int shifter = 0;
	int chestCounter = 0;
	boolean canMove;
	
	ImageIcon ii; 
	//graphics: can name them and just put them here
	Image hotbar=Toolkit.getDefaultToolkit().getImage("src//Images//Hotbar Template.png"); 
	Image inventory=Toolkit.getDefaultToolkit().getImage("src//Images//Inventory Template.png"); 
	Image hudbackground=Toolkit.getDefaultToolkit().getImage("src//Images//HUD Background.png"); 
	Image background=Toolkit.getDefaultToolkit().getImage("src//Images//Background.jpg"); 
	Image roomTemplate=Toolkit.getDefaultToolkit().getImage("src//Images//Room Template 1.png"); 
	Image blankTemplate=Toolkit.getDefaultToolkit().getImage("src//Images//Blank Tile Template.png");
	Image vPathTemplate=Toolkit.getDefaultToolkit().getImage("src//Images//Vertical Path Template 1.png"); 
	Image hPathTemplate=Toolkit.getDefaultToolkit().getImage("src//Images//Horizontal Path Template 1.png");
	Image luTurnTemplate=Toolkit.getDefaultToolkit().getImage("src//Images//Left Up Turn Template 1.png"); 
	Image ldTurnTemplate=Toolkit.getDefaultToolkit().getImage("src//Images//Left Down Turn Template 1.png");
	Image ruTurnTemplate=Toolkit.getDefaultToolkit().getImage("src//Images//Right Up Turn Template 1.png"); 
	Image rdTurnTemplate=Toolkit.getDefaultToolkit().getImage("src//Images//Right Down Turn Template 1.png");
	Image man1=Toolkit.getDefaultToolkit().getImage("src//Images//Body Template A.png"); 
	Image boots1=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template A.png"); 
	Image chestplate1=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template A.png");
	Image gloves1=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template A.png"); 
	Image helmet1=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template A.png"); 
	Image body2=Toolkit.getDefaultToolkit().getImage("src//Images//Body Template B.png"); 
	Image boots2=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template B.png"); 
	Image chestplate2=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template B.png");
	Image gloves2=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template B.png"); 
	Image helmet2=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template B.png"); 
	Image body3=Toolkit.getDefaultToolkit().getImage("src//Images//Body Template C.png"); 
	Image boots3=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template C.png"); 
	Image chestplate3=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template C.png");
	Image gloves3=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template C.png"); 
	Image helmet3=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template C.png"); 
	Image body4=Toolkit.getDefaultToolkit().getImage("src//Images//Body Template D.png"); 
	Image boots4=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template D.png"); 
	Image chestplate4=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template D.png");
	Image gloves4=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template D.png"); 
	Image helmet4=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template D.png"); 
	Image helmetLogo=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Logo Template.png");
	Image chestplateLogo=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Logo Template.png"); 
	Image glovesLogo=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Logo Template.png"); 
	Image bootsLogo=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Logo Template.png"); 
	Image selectorCursor=Toolkit.getDefaultToolkit().getImage("src//Images//Selector Cursor.png"); 
	Image selectedIcon=Toolkit.getDefaultToolkit().getImage("src//Images//Selected Icon.png"); 
	Image inventoryIcon=Toolkit.getDefaultToolkit().getImage("src//Images//Inventory Icons Template.png");
	Image emptyImage=Toolkit.getDefaultToolkit().getImage("src//Images//Empty Template.png"); 
	Image shieldLogo=Toolkit.getDefaultToolkit().getImage("src//Images//Shield Logo Template.png");
	Image potionLogo=Toolkit.getDefaultToolkit().getImage("src//Images//Potion Logo Template.png");
	Image shieldImage1a=Toolkit.getDefaultToolkit().getImage("src//Images//Shield Template  A 1.png"); 
	Image shieldImage1b=Toolkit.getDefaultToolkit().getImage("src//Images//Shield Template A 2.png");
	Image shieldImage2a=Toolkit.getDefaultToolkit().getImage("src//Images//Shield Template B 1.png"); 
	Image shieldImage2b=Toolkit.getDefaultToolkit().getImage("src//Images//Shield Template B 2.png");
	Image shieldImage3a=Toolkit.getDefaultToolkit().getImage("src//Images//Shield Template C 1.png"); 
	Image shieldImage3b=Toolkit.getDefaultToolkit().getImage("src//Images//Shield Template C 2.png");
	Image shieldImage4a=Toolkit.getDefaultToolkit().getImage("src//Images//Shield Template D 1.png"); 
	Image shieldImage4b=Toolkit.getDefaultToolkit().getImage("src//Images//Shield Template D 2.png");
	Image heartImage=Toolkit.getDefaultToolkit().getImage("src//Images//Heart Template.png");
	Image armorBarOff1=Toolkit.getDefaultToolkit().getImage("src//Images//Armor Bar (off) 1 Template.png");
	Image armorBarOff2=Toolkit.getDefaultToolkit().getImage("src//Images//Armor Bar (off) Template.png");
	Image armorBarOn=Toolkit.getDefaultToolkit().getImage("src//Images//Armor Bar (on) Template.png");
	Image menuImage=Toolkit.getDefaultToolkit().getImage("src//Images//Menu Template.png"); 
	Image menuImage2=Toolkit.getDefaultToolkit().getImage("src//Images//Menu Template 2.png");
	Image menuSelectorImage=Toolkit.getDefaultToolkit().getImage("src//Images//Menu Selector Template.png");
	Image savedImage=Toolkit.getDefaultToolkit().getImage("src//Images//Saved Template.png");
	Image deleteIcon=Toolkit.getDefaultToolkit().getImage("src//Images//Delete Icon.png");
	Image spikesOn=Toolkit.getDefaultToolkit().getImage("src//Images//Spikes (on).png");
	Image spikesOff=Toolkit.getDefaultToolkit().getImage("src//Images//Spikes (off).png");
	Image damageTickScreen=Toolkit.getDefaultToolkit().getImage("src//Images//Damage Tick.png");
	Image pit=Toolkit.getDefaultToolkit().getImage("src//Images//Pit Template.png");
	Image crate=Toolkit.getDefaultToolkit().getImage("src//Images//Crate (thin).png");
	Image chest=Toolkit.getDefaultToolkit().getImage("src//Images//Chest.png");
	Image fInteraction=Toolkit.getDefaultToolkit().getImage("src//Images//F Interaction.png");
	Image chestInventory=Toolkit.getDefaultToolkit().getImage("src//Images//Chest Inventory Template.png");
	Image wallImage=Toolkit.getDefaultToolkit().getImage("src//Images//Wall.png");
	Image vdoor=Toolkit.getDefaultToolkit().getImage("src//Images//Vertical Door.png");
	Image hdoor=Toolkit.getDefaultToolkit().getImage("src//Images//Horizontal Door.png");
	Image checkpointOff=Toolkit.getDefaultToolkit().getImage("src//Images//Checkpoint (off).png");
	Image checkpointOn=Toolkit.getDefaultToolkit().getImage("src//Images//Checkpoint (on).png");
	Image keyLogo=Toolkit.getDefaultToolkit().getImage("src//Images//Key Logo.png");
	Image lootbag=Toolkit.getDefaultToolkit().getImage("src//Images//Lootbag.png");
	Image zombie1=Toolkit.getDefaultToolkit().getImage("src//Images//Zombie Template A.png");
	Image zombie2=Toolkit.getDefaultToolkit().getImage("src//Images//Zombie Template B.png");
	Image zombie3=Toolkit.getDefaultToolkit().getImage("src//Images//Zombie Template C.png");
	Image zombie4=Toolkit.getDefaultToolkit().getImage("src//Images//Zombie Template D.png");
	Image dzombie1=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Zombie Template A.png");
	Image dzombie2=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Zombie Template B.png");
	Image dzombie3=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Zombie Template C.png");
	Image dzombie4=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Zombie Template D.png");
	Image skeleton1=Toolkit.getDefaultToolkit().getImage("src//Images//Skeleton Template A.png");
	Image skeleton2=Toolkit.getDefaultToolkit().getImage("src//Images//Skeleton Template B.png");
	Image skeleton3=Toolkit.getDefaultToolkit().getImage("src//Images//Skeleton Template C.png");
	Image skeleton4=Toolkit.getDefaultToolkit().getImage("src//Images//Skeleton Template D.png");
	Image dskeleton1=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Skeleton Template A.png");
	Image dskeleton2=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Skeleton Template B.png");
	Image dskeleton3=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Skeleton Template C.png");
	Image dskeleton4=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Skeleton Template D.png");
	Image warden1=Toolkit.getDefaultToolkit().getImage("src//Images//Warden Template A.png");
	Image warden2=Toolkit.getDefaultToolkit().getImage("src//Images//Warden Template B.png");
	Image warden3=Toolkit.getDefaultToolkit().getImage("src//Images//Warden Template C.png");
	Image warden4=Toolkit.getDefaultToolkit().getImage("src//Images//Warden Template D.png");
	Image dwarden1=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Warden Template A.png");
	Image dwarden2=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Warden Template B.png");
	Image dwarden3=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Warden Template C.png");
	Image dwarden4=Toolkit.getDefaultToolkit().getImage("src//Images//Damaged Warden Template D.png");
	Image arrow1=Toolkit.getDefaultToolkit().getImage("src//Images//Arrow A.png");
	Image arrow2=Toolkit.getDefaultToolkit().getImage("src//Images//Arrow B.png");
	Image arrow3=Toolkit.getDefaultToolkit().getImage("src//Images//Arrow C.png");
	Image arrow4=Toolkit.getDefaultToolkit().getImage("src//Images//Arrow D.png");
	Image bowA1=Toolkit.getDefaultToolkit().getImage("src//Images//Bow A 1.png");
	Image bowB1=Toolkit.getDefaultToolkit().getImage("src//Images//Bow B 1.png");
	Image bowC1=Toolkit.getDefaultToolkit().getImage("src//Images//Bow C 1.png");
	Image bowD1=Toolkit.getDefaultToolkit().getImage("src//Images//Bow D 1.png");
	Image bowA2=Toolkit.getDefaultToolkit().getImage("src//Images//Bow A 2.png");
	Image bowB2=Toolkit.getDefaultToolkit().getImage("src//Images//Bow B 2.png");
	Image bowC2=Toolkit.getDefaultToolkit().getImage("src//Images//Bow C 2.png");
	Image bowD2=Toolkit.getDefaultToolkit().getImage("src//Images//Bow D 2.png");
	Image bowA3=Toolkit.getDefaultToolkit().getImage("src//Images//Bow A 3.png");
	Image bowB3=Toolkit.getDefaultToolkit().getImage("src//Images//Bow B 3.png");
	Image bowC3=Toolkit.getDefaultToolkit().getImage("src//Images//Bow C 3.png");
	Image bowD3=Toolkit.getDefaultToolkit().getImage("src//Images//Bow D 3.png");
	Image path1=Toolkit.getDefaultToolkit().getImage("src//Images//path1.png");
	Image path2=Toolkit.getDefaultToolkit().getImage("src//Images//path2.png");
	Image path3=Toolkit.getDefaultToolkit().getImage("src//Images//path3.png");
	Image shooter1=Toolkit.getDefaultToolkit().getImage("src//Images//Shooter 1.png");
	Image shooter2=Toolkit.getDefaultToolkit().getImage("src//Images//Shooter 2.png");
	Image shooter3=Toolkit.getDefaultToolkit().getImage("src//Images//Shooter 3.png");
	Image shooter4=Toolkit.getDefaultToolkit().getImage("src//Images//Shooter 4.png");
	Image levelChanger=Toolkit.getDefaultToolkit().getImage("src//Images//LevelChanger.png");
	Image controls=Toolkit.getDefaultToolkit().getImage("src//Images//Controls1.png");
	Image gameOverScreen=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over.png");
	Image gameOverEasy=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over Easy.png");
	Image gameOverNormal=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over Normal.png");
	Image gameOverHard=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over Hard.png");
	Image gameOver1=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over 1.png");
	Image gameOver2=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over 2.png");
	Image gameOver3=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over 3.png");
	Image gameOver4=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over 4.png");
	Image gameOver5=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over 5.png");
	Image gameOver6=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over 6.png");
	Image gameOver7=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over 7.png");
	Image gameOver8=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over 8.png");
	Image gameOver9=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over 9.png");
	Image gameOver0=Toolkit.getDefaultToolkit().getImage("src//Images//Game Over 0.png");
	Image livesImage=Toolkit.getDefaultToolkit().getImage("src//Images//Life.png");
	
	Image swordLogo0=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Logo Template.png"); 
	Image swordImage01=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template A.png"); 
	Image swordImage02=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template B.png");
	Image swordImage03=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template C.png"); 
	Image swordImage04=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template D.png");
	Image swordLogo1=Toolkit.getDefaultToolkit().getImage("src//Images//Broken Sword Logo Template.png"); 
	Image swordImage11=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 1 A.png"); 
	Image swordImage12=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 1 B.png");
	Image swordImage13=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 1 C.png"); 
	Image swordImage14=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 1 D.png");
	Image swordLogo2=Toolkit.getDefaultToolkit().getImage("src//Images//Common Sword Logo Template.png"); 
	Image swordImage21=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 2 A.png"); 
	Image swordImage22=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 2 B.png");
	Image swordImage23=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 2 C.png"); 
	Image swordImage24=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 2 D.png");
	Image swordLogo3=Toolkit.getDefaultToolkit().getImage("src//Images//Superior Sword Logo Template.png"); 
	Image swordImage31=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 3 A.png"); 
	Image swordImage32=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 3 B.png");
	Image swordImage33=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 3 C.png"); 
	Image swordImage34=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 3 D.png");
	Image swordLogo4=Toolkit.getDefaultToolkit().getImage("src//Images//Mythical Sword Logo Template.png"); 
	Image swordImage41=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 4 A.png"); 
	Image swordImage42=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 4 B.png");
	Image swordImage43=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 4 C.png"); 
	Image swordImage44=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 4 D.png");
	Image swordLogo5=Toolkit.getDefaultToolkit().getImage("src//Images//Hellbreaker Sword Logo Template.png"); 
	Image swordImage51=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 5 A.png"); 
	Image swordImage52=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 5 B.png");
	Image swordImage53=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 5 C.png"); 
	Image swordImage54=Toolkit.getDefaultToolkit().getImage("src//Images//Sword Template 5 D.png");
	
	Image helmetLogo1=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Logo Template 1.png");
	Image chestplateLogo1=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Logo Template 1.png"); 
	Image glovesLogo1=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Logo Template 1.png"); 
	Image bootsLogo1=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Logo Template 1.png");
	Image boots11=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 1 A.png"); 
	Image chestplate11=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 1 A.png");
	Image gloves11=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 1 A.png"); 
	Image helmet11=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 1 A.png"); 
	Image boots12=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 1 B.png"); 
	Image chestplate12=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 1 B.png");
	Image gloves12=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 1 B.png"); 
	Image helmet12=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 1 B.png"); 
	Image boots13=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 1 C.png"); 
	Image chestplate13=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 1 C.png");
	Image gloves13=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 1 C.png"); 
	Image helmet13=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 1 C.png"); 
	Image boots14=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 1 D.png"); 
	Image chestplate14=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 1 D.png");
	Image gloves14=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 1 D.png"); 
	Image helmet14=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 1 D.png"); 
	
	Image helmetLogo2=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Logo Template 2.png");
	Image chestplateLogo2=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Logo Template 2.png"); 
	Image glovesLogo2=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Logo Template 2.png"); 
	Image bootsLogo2=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Logo Template 2.png");
	Image boots21=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 2 A.png"); 
	Image chestplate21=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 2 A.png");
	Image gloves21=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 2 A.png"); 
	Image helmet21=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 2 A.png"); 
	Image boots22=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 2 B.png"); 
	Image chestplate22=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 2 B.png");
	Image gloves22=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 2 B.png"); 
	Image helmet22=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 2 B.png"); 
	Image boots23=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 2 C.png"); 
	Image chestplate23=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 2 C.png");
	Image gloves23=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 2 C.png"); 
	Image helmet23=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 2 C.png"); 
	Image boots24=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 2 D.png"); 
	Image chestplate24=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 2 D.png");
	Image gloves24=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 2 D.png"); 
	Image helmet24=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 2 D.png"); 
	
	Image helmetLogo3=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Logo Template 3.png");
	Image chestplateLogo3=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Logo Template 3.png"); 
	Image glovesLogo3=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Logo Template 3.png"); 
	Image bootsLogo3=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Logo Template 3.png");
	Image boots31=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 3 A.png"); 
	Image chestplate31=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 3 A.png");
	Image gloves31=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 3 A.png"); 
	Image helmet31=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 3 A.png"); 
	Image boots32=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 3 B.png"); 
	Image chestplate32=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 3 B.png");
	Image gloves32=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 3 B.png"); 
	Image helmet32=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 3 B.png"); 
	Image boots33=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 3 C.png"); 
	Image chestplate33=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 3 C.png");
	Image gloves33=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 3 C.png"); 
	Image helmet33=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 3 C.png"); 
	Image boots34=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 3 D.png"); 
	Image chestplate34=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 3 D.png");
	Image gloves34=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 3 D.png"); 
	Image helmet34=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 3 D.png"); 
	
	Image helmetLogo4=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Logo Template 4.png");
	Image chestplateLogo4=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Logo Template 4.png"); 
	Image glovesLogo4=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Logo Template 4.png"); 
	Image bootsLogo4=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Logo Template 4.png");
	Image boots41=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 4 A.png"); 
	Image chestplate41=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 4 A.png");
	Image gloves41=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 4 A.png"); 
	Image helmet41=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 4 A.png"); 
	Image boots42=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 4 B.png"); 
	Image chestplate42=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 4 B.png");
	Image gloves42=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 4 B.png"); 
	Image helmet42=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 4 B.png"); 
	Image boots43=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 4 C.png"); 
	Image chestplate43=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 4 C.png");
	Image gloves43=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 4 C.png"); 
	Image helmet43=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 4 C.png"); 
	Image boots44=Toolkit.getDefaultToolkit().getImage("src//Images//Boots Template 4 D.png"); 
	Image chestplate44=Toolkit.getDefaultToolkit().getImage("src//Images//Chestplate Template 4 D.png");
	Image gloves44=Toolkit.getDefaultToolkit().getImage("src//Images//Gloves Template 4 D.png"); 
	Image helmet44=Toolkit.getDefaultToolkit().getImage("src//Images//Helmet Template 4 D.png"); 
	
	
	Item holder = new Item("","empty",emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	Item empty = new Item("","empty",emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	Object space = new Object("space",0,emptyImage,emptyImage,0);
	
	Item head1 = new Item("helmet","Fine Helmet",helmetLogo,helmet1,helmet2,helmet3,helmet4,emptyImage,emptyImage,emptyImage,emptyImage, 2,0,1);
	Item chest1 = new Item("chestplate","Fine Chestplate",chestplateLogo,chestplate1,chestplate2,chestplate3,chestplate4,emptyImage,emptyImage,emptyImage,emptyImage, 2,0,1);
	Item hands1 = new Item("gloves","Fine Gloves",glovesLogo,gloves1,gloves2,gloves3,gloves4,emptyImage,emptyImage,emptyImage,emptyImage, 2,0,1);
	Item feet1 = new Item("boots","Fine Boots",bootsLogo,boots1,boots2,boots3,boots4,emptyImage,emptyImage,emptyImage,emptyImage, 2,0,1);
	
	Item head2 = new Item("helmet","Common Helmet",helmetLogo1,helmet11,helmet12,helmet13,helmet14,emptyImage,emptyImage,emptyImage,emptyImage, 1,0,1);
	Item chest2 = new Item("chestplate","Common Chestplate",chestplateLogo1,chestplate11,chestplate12,chestplate13,chestplate14,emptyImage,emptyImage,emptyImage,emptyImage, 1,0,1);
	Item hands2 = new Item("gloves","Common Gloves",glovesLogo1,gloves11,gloves12,gloves13,gloves14,emptyImage,emptyImage,emptyImage,emptyImage, 1,0,1);
	Item feet2 = new Item("boots","Common Boots",bootsLogo1,boots11,boots12,boots13,boots14,emptyImage,emptyImage,emptyImage,emptyImage, 1,0,1);
	
	Item head3 = new Item("helmet","Mythical Helmet",helmetLogo2,helmet21,helmet22,helmet23,helmet24,emptyImage,emptyImage,emptyImage,emptyImage, 4,0,1);
	Item chest3 = new Item("chestplate","Mythical Chestplate",chestplateLogo2,chestplate21,chestplate22,chestplate23,chestplate24,emptyImage,emptyImage,emptyImage,emptyImage, 4,0,1);
	Item hands3 = new Item("gloves","Mythical Gloves",glovesLogo2,gloves21,gloves22,gloves23,gloves24,emptyImage,emptyImage,emptyImage,emptyImage, 4,0,1);
	Item feet3 = new Item("boots","Mythical Boots",bootsLogo2,boots21,boots22,boots23,boots24,emptyImage,emptyImage,emptyImage,emptyImage, 4,0,1);
	
	Item head4 = new Item("helmet","Hellbreaker Helmet",helmetLogo3,helmet31,helmet32,helmet33,helmet34,emptyImage,emptyImage,emptyImage,emptyImage, 5,0,1);
	Item chest4 = new Item("chestplate","Hellbreaker Chestplate",chestplateLogo3,chestplate31,chestplate32,chestplate33,chestplate34,emptyImage,emptyImage,emptyImage,emptyImage, 5,0,1);
	Item hands4 = new Item("gloves","Hellbreaker Gloves",glovesLogo3,gloves31,gloves32,gloves33,gloves34,emptyImage,emptyImage,emptyImage,emptyImage, 5,0,1);
	Item feet4 = new Item("boots","Hellbreaker Boots",bootsLogo3,boots31,boots32,boots33,boots34,emptyImage,emptyImage,emptyImage,emptyImage, 5,0,1);
	
	Item head5 = new Item("helmet","Superior Helmet",helmetLogo4,helmet41,helmet42,helmet43,helmet44,emptyImage,emptyImage,emptyImage,emptyImage, 3,0,1);
	Item chest5 = new Item("chestplate","Superior Chestplate",chestplateLogo4,chestplate41,chestplate42,chestplate43,chestplate44,emptyImage,emptyImage,emptyImage,emptyImage, 3,0,1);
	Item hands5 = new Item("gloves","Superior Gloves",glovesLogo4,gloves41,gloves42,gloves43,gloves44,emptyImage,emptyImage,emptyImage,emptyImage, 3,0,1);
	Item feet5 = new Item("boots","Superior Boots",bootsLogo4,boots41,boots42,boots43,boots44,emptyImage,emptyImage,emptyImage,emptyImage, 3,0,1);
	
	Item brokenSword = new Item("weapon","Broken Sword",swordLogo1,swordImage11,swordImage12,swordImage13,swordImage14,emptyImage,emptyImage,emptyImage,emptyImage,0, 5,1);
	Item commonSword = new Item("weapon","Common Sword",swordLogo2,swordImage21,swordImage22,swordImage23,swordImage24,emptyImage,emptyImage,emptyImage,emptyImage,0, 7,1);
	Item fineSword = new Item("weapon","Fine Sword",swordLogo0,swordImage01,swordImage02,swordImage03,swordImage04,emptyImage,emptyImage,emptyImage,emptyImage,0, 9,1);
	Item superiorSword = new Item("weapon","Superior Sword",swordLogo3,swordImage31,swordImage32,swordImage33,swordImage34,emptyImage,emptyImage,emptyImage,emptyImage,0, 11,1);
	Item mythicalSword = new Item("weapon","Mythical Sword",swordLogo4,swordImage41,swordImage42,swordImage43,swordImage44,emptyImage,emptyImage,emptyImage,emptyImage,0, 13,1);
	Item hellbreakerSword = new Item("weapon","Hellbreaker Sword",swordLogo5,swordImage51,swordImage52,swordImage53,swordImage54,emptyImage,emptyImage,emptyImage,emptyImage,0, 15,1);
	Item shield1 = new Item("shield","Common Shield",shieldLogo,shieldImage1a,shieldImage2a,shieldImage3a,shieldImage4a,shieldImage1b,shieldImage2b,shieldImage3b,shieldImage4b, 3,0,1);
	Item potion1 = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	Item potion2 = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	Item potion3 = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	Item potion4 = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	Item key1 = new Item("item","Key",keyLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	
	Object SPIKETRAP = new Object("spikeTrap",0,spikesOff,spikesOn,0);
	Object PIT = new Object("pit",0,pit,emptyImage,0);
	Object CRATE = new Object("crate",0,crate,emptyImage,0);
	Object CHEST = new Object("chest",0,chest,emptyImage,0);
	Object WALL = new Object("wall",0,wallImage,emptyImage,0);
	Object CHECKPOINT = new Object("checkpoint",0,checkpointOff,checkpointOn,0);
	Object LEVELCHANGER = new Object("levelChanger",0,levelChanger,emptyImage,0);
	Object VDOOR = new Object("door",0,vdoor,hdoor,0);
	Object HDOOR = new Object("door",0,hdoor,vdoor,0);
	Object UPSHOOTER = new Object("shooter",0,shooter1,shooter1,0);
	Object RIGHTSHOOTER = new Object("shooter",1,shooter2,shooter2,1);
	Object DOWNSHOOTER = new Object("shooter",2,shooter3,shooter3,2);
	Object LEFTSHOOTER = new Object("shooter",3,shooter4,shooter4,3);
	Object ZOMBIE = new Object("zombie",0,emptyImage,emptyImage,0);
	Object WARDEN = new Object("warden",0,emptyImage,emptyImage,0);
	Object SKELETON = new Object("skeleton",0,emptyImage,emptyImage,0);
	
	Node blankNode = new Node(0,0,0,0,0,0);
	
	//Enemy zombie = new Enemy(36,40,"Zombie", "melee",20,25,0,blankNode,0,0,false,0,false);
	//Enemy warden = new Enemy(36,39,"Warden", "melee",20,25,0,blankNode,0,0,false,0,false);
	//Enemy skeleton = new Enemy(45,40,"Skeleton", "ranged",20,25,0,blankNode,0,0,false,0,false);
	DrawPanel drawPanel = new DrawPanel();
	
public Core()  
{  
	//*start* methods
    pack();  
    music();
    regeneration();
    
    //initializeInventory();
    //initializeObjects();
    //generateLayout(0);
    //loadData();
    //objectTime();
    //gameTime();
    //enemyMove();
    for(int i = 0; i < projectiles.length; i ++)
	{
		projectiles[i] = new Projectile("empty",0,0,0,0,0,false);
	}
	for(int row = 0; row < objects.length; row++)
	{
		for(int col = 0; col < objects[0].length; col++)
		{
			objects[row][col] = space;
		}
	}
	add(drawPanel);
	
	Action wAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(gameFreeze == false && isMoving == false)
        	{
        		new Thread()
                {
                    @Override public void run()
                    {		
                    	canMove = true;
                    	direction = 0;
                    	if(ymod - 1 >= 0 && objects[ymod - 1][xmod].Type() != "crate" && objects[ymod - 1][xmod].Type() != "chest" && objects[ymod - 1][xmod].Type() != "wall" && (objects[ymod - 1][xmod].Type() != "door" || objects[ymod - 1][xmod].getPhase() != 0) && objects[ymod - 1][xmod].Type() != "shooter")
                    	{
	                    	isMoving = true;
	                    	ymod--;
	                    	for(int i = 0; i < enemyList.size(); i++)
	                    	{
	                    		if(enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() == xmod)
	                    		{
	                    			canMove = false;
	                    		}
	                    	}
	                    	if(moveLayout[ymod][xmod] == true && canMove == true)
	         	            {
	                    		playSound("walk1");
			                    for(int i = 0; i < 25; i++)
			                    {
			                    	try
			                    	{
			                    		ey += 1;
			            	            drawPanel.repaint();
			            	            Thread.sleep(moveSpeed);
			                   		}
			                    	catch(InterruptedException e) {}
			                    }
	         	            }
	                    	else
	                    	{
	                    		ymod++;
	                    	}
	                    	isMoving = false;
                    	}
                    }
                }.start();
        	}
        	drawPanel.repaint();
        }
    };
    Action dAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(gameFreeze == false && isMoving == false)
        	{
        		new Thread()
                {
                    @Override public void run()
                    {
                    	canMove = true;
                    	direction = 1;
                    	if(xmod + 1 < 81 && objects[ymod][xmod + 1].Type() != "crate" && objects[ymod][xmod + 1].Type() != "chest" && objects[ymod][xmod + 1].Type() != "wall" && (objects[ymod][xmod + 1].Type() != "door" || objects[ymod][xmod + 1].getPhase() != 0) && objects[ymod][xmod + 1].Type() != "shooter")
                    	{
	                    	isMoving = true;
	                    	xmod++;
	                    	for(int i = 0; i < enemyList.size(); i++)
	                    	{
	                    		if(enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() == xmod)
	                    		{
	                    			canMove = false;
	                    		}
	                    	}
	                    	if(moveLayout[ymod][xmod] == true && canMove == true)
	         	            {
	                    		playSound("walk1");
			                    for(int i = 0; i < 25; i++)
			                    {
			                    	try
			                    	{
			                    		ex -= 1;
			            	            drawPanel.repaint();
			            	            Thread.sleep(moveSpeed);
			                   		}
			                    	catch(InterruptedException e) {}
			                    }
	         	            }
	                    	else
	                    	{
	                    		xmod--;
	                    	}
	                    	isMoving = false;
                    	}
                    }
                }.start();       	
        	}
        	drawPanel.repaint();
        }
    };
    Action aAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(gameFreeze == false && isMoving == false)
        	{
        		new Thread()
                {
                    @Override public void run()
                    {
                    	canMove = true;
                    	direction = 3;
                    	if(xmod - 1 >= 0 && objects[ymod][xmod - 1].Type() != "crate" && objects[ymod][xmod - 1].Type() != "chest" && objects[ymod][xmod - 1].Type() != "wall" && (objects[ymod][xmod - 1].Type() != "door" || objects[ymod][xmod - 1].getPhase() != 0) && objects[ymod][xmod - 1].Type() != "shooter")
                    	{
	                    	isMoving = true;
	                    	xmod--;
	                    	for(int i = 0; i < enemyList.size(); i++)
	                    	{
	                    		if(enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() == xmod)
	                    		{
	                    			canMove = false;
	                    		}
	                    	}
	                    	if(moveLayout[ymod][xmod] == true && canMove == true)
	         	            {
	                    		playSound("walk1");
			                    for(int i = 0; i < 25; i++)
			                    {
			                    	try
			                    	{
			                    		ex += 1;
			            	            drawPanel.repaint();
			            	            Thread.sleep(moveSpeed);
			                   		}
			                    	catch(InterruptedException e) {}
			                    }
	         	            }
	                    	else
	                    	{
	                    		xmod++;
	                    	}
	                    	isMoving = false;
                    	}
                    }
                }.start();
        	}
        	drawPanel.repaint();
        }
    };
    Action sAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(gameFreeze == false && isMoving == false)
        	{
        		new Thread()
                {
                    @Override public void run()
                    {
                    	canMove = true;
                    	direction = 2;
                    	if(ymod + 1 < 81 && objects[ymod + 1][xmod].Type() != "crate" && objects[ymod + 1][xmod].Type() != "chest"  && objects[ymod + 1][xmod].Type() != "wall" && (objects[ymod + 1][xmod].Type() != "door" || objects[ymod + 1][xmod].getPhase() != 0) && objects[ymod + 1][xmod].Type() != "shooter")
                    	{
	                    	isMoving = true;
	                    	ymod++;
	                    	for(int i = 0; i < enemyList.size(); i++)
	                    	{
	                    		if(enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() == xmod)
	                    		{
	                    			canMove = false;
	                    		}
	                    	}
	                    	if(moveLayout[ymod][xmod] == true && canMove == true)
	         	            {
	                    		playSound("walk1");
			                    for(int i = 0; i < 25; i++)
			                    {
			                    	try
			                    	{
			                    		ey -= 1;
			            	            drawPanel.repaint();
			            	            Thread.sleep(moveSpeed);
			                   		}
			                    	catch(InterruptedException e) {}
			                    }
	         	            }
	                    	else
	                    	{
	                    		ymod--;
	                    	}
	                    	isMoving = false;
                    	}
                    }
                }.start();
        	}
        	drawPanel.repaint();
        }
    };
    Action eAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(health > 0)
        	{
        		playSound("chestClose");
	            if(inInventory == false && inMenu == false)
	            {
	            	inInventory = true;
	            	gameFreeze = true;
	            	selector[1] = 1;
	            }
	            else
	            {
	            	inInventory = false;
	            	gameFreeze = false;
	            	isSelected = false;
	            	inChest = false;
	            	shifter = 0;
	            }
        	}
            
            drawPanel.repaint();
        }
    };
    Action upAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(inInventory == true || inMenu == true)
        		playSound("click1");
        	if(inInventory == true)
        	{
        		if(selector[0] > 0)
        		{
        			selector[0] -= 1;
        		}
        		deleteSet = false;
	            drawPanel.repaint();
        	}
        	if(!inControls)
        	{
	        	if(inOptions == true)
	        	{
	        		if(inMenu == true && menuSelector > 0)
		        	{
		        		menuSelector--;
		        	}
		        	else if(inMenu == true){
		        		menuSelector = 2;
		        	}
	        	}
	        	else
	        	{
		        	if(inMenu == true && menuSelector > 0)
		        	{
		        		menuSelector--;
		        	}
		        	else if(inMenu == true){
		        		menuSelector = 3;
		        	}
	        	}
        	}
        	drawPanel.repaint();
        }
    };
    Action rightAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(inInventory == true)
        		playSound("click1");
        	if(inInventory == true)
        	{
        		if(selector[1] < 5)
        		{
        			selector[1] += 1;
        		}
        		else if(selector[1] < 10 && inChest == true && selector[0] < 4)
        		{
        			selector[1] += 1;
        		}      		
        		deleteSet = false;
	            drawPanel.repaint();
        	}
        }
    };
    Action leftAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(inInventory == true)
        		playSound("click1");
        	if(inInventory == true)
        	{
        		if(selector[1] > 0)
        		{
        			selector[1] -= 1;
        		}
        		deleteSet = false;
	            drawPanel.repaint();
        	}
        }
    };
    Action downAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(inInventory == true || inMenu == true)
        		playSound("click1");
        	if(inInventory == true)
        	{
        		if(selector[0] < 4)
        		{
        			selector[0] += 1;
        			if(selector[1] > 5 && selector[0] == 4)
        			{
        				selector[0] -= 1;
        			}
        		}
        		deleteSet = false;
	            drawPanel.repaint();
        	}
        	if(!inControls)
        	{
	        	if(inOptions == true)
	        	{
	        		if(inMenu == true && menuSelector < 2)
	            	{
	            		menuSelector++;
	            	}else if(inMenu == true){
	            		menuSelector = 0;
	            	}
	        	}
	        	else
	        	{
	        		if(inMenu == true && menuSelector < 3)
	            	{
	            		menuSelector++;
	            	}else if(inMenu == true){
	            		menuSelector = 0;
	            	}
	        	}
        	}
        	drawPanel.repaint();
        }
    };
    Action kAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	new Thread()
            {
                @Override public void run()
                {
                	damaged = 0;
                	if(isAttacking == false && isBlocking == false && gameFreeze == false)
     	            {
                		playSound("slash");
                		for(int i = 0; i < enemyList.size(); i++)
                		{
                			if((enemyList.get(i).getRow() + 1 == ymod && enemyList.get(i).getCol() == xmod && direction == 0) || (enemyList.get(i).getRow() - 1 == ymod && enemyList.get(i).getCol() == xmod && direction == 2) || (enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() - 1 == xmod && direction == 1) || (enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() + 1 == xmod && direction == 3))
                			{
                				playSound("hiss");
                				enemyList.get(i).setHealth(enemyList.get(i).getHealth() - inventoryList[4].Damage());
                				if(enemyList.size() > 0 && damaged == 0)
	                    		{
                					damaged = i;
                					displayEnemyDamage(damaged);
	                    			//enemyList.get(damaged).setDamaged(true);
	                    		}
                				enemyDeathCheck(i);
                			}
                		}
                		if(objects[ymod - 1][xmod].Type().equals("crate") && direction == 0)
                		{
		        			objects[ymod - 1][xmod] = space;
		        			playSound("crate");
                		}
                		else if(objects[ymod][xmod + 1].Type().equals("crate") && direction == 1)
                		{
		        			objects[ymod][xmod + 1] = space;
		        			playSound("crate");
                		}
                		else if(objects[ymod + 1][xmod].Type().equals("crate") && direction == 2)
                		{
		        			objects[ymod + 1][xmod] = space;
		        			playSound("crate");
                		}
                		else if(objects[ymod][xmod - 1].Type().equals("crate") && direction == 3)
                		{
		        			objects[ymod][xmod - 1] = space;
		        			playSound("crate");
                		}
                		//isAttacking = true;// vvv
	                    for(int i = 0; i < 5; i++)
	                    {
	                    	try
	                    	{
	                    		isAttacking = true;
	                    		if(i == 4 && isAttacking == true)
	                    		{
	                    			isAttacking = false;
	                    			if(enemyList.size() > 0)
	                        		{
	                        			//enemyList.get(damaged).setDamaged(false);
	                        		}
	                    		}
	            	            drawPanel.repaint();
	            	            Thread.sleep(attackSpeed);
	                   		}
	                    	catch(InterruptedException e) {}
	                    }
     	            }
                }
            }.start();
            damaged = 0;
            drawPanel.repaint();
        }
    };
    Action lAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	new Thread()
            {
                @Override public void run()
                {
                	if(isBlocking == false && isAttacking == false && gameFreeze == false)
     	            {
                		playSound("shield2");
	                    for(int i = 0; i < 5; i++)
	                    {
	                    	try
	                    	{
	                    		isBlocking = true;
	                    		if(i == 4)
	                    		{
	                    			isBlocking = false;
	                    		}
	            	            drawPanel.repaint();
	            	            Thread.sleep(blockSpeed);
	                   		}
	                    	catch(InterruptedException e) {}
	                    }
     	            }
                }
            }.start();
        }
    };
    Action uAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(gameFreeze == false)
        	{
        		if(inventoryList[6].Name().equals("Health Potion"))
        		{
        			playSound("drink2");
        			if(health < 100)
        			{
        				health += 25;
        				if(health > 100)
        				{
        					health = 100;
        				}
        			}
        			inventoryList[6].setStack(inventoryList[6].Stack() - 1);
        			if(inventoryList[6].Stack() == 0)
        			{
        				inventoryList[6] = empty;
        			}
        		}
        		if(inventoryList[6].Name().equals("Key"))
        		{
        			if(direction == 0)
        			{
        				if(objects[ymod - 1][xmod].Type().equals("door") && objects[ymod - 1][xmod].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod - 1][xmod].setPhase(1);
        					inventoryList[6].setStack(inventoryList[6].Stack() - 1);
                			if(inventoryList[6].Stack() == 0)
                			{
                				inventoryList[6] = empty;
                			}
        				}
        			}else if(direction == 1)
        			{
        				if(objects[ymod][xmod + 1].Type().equals("door") && objects[ymod][xmod + 1].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod][xmod + 1].setPhase(2);
        					inventoryList[6].setStack(inventoryList[6].Stack() - 1);
                			if(inventoryList[6].Stack() == 0)
                			{
                				inventoryList[6] = empty;
                			}
        				}
        			}else if(direction == 2)
        			{
        				if(objects[ymod + 1][xmod].Type().equals("door") && objects[ymod + 1][xmod].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod + 1][xmod].setPhase(3);
        					inventoryList[6].setStack(inventoryList[6].Stack() - 1);
                			if(inventoryList[6].Stack() == 0)
                			{
                				inventoryList[6] = empty;
                			}
        				}
        			}else if(direction == 3)
        			{
        				if(objects[ymod][xmod - 1].Type().equals("door") && objects[ymod][xmod - 1].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod][xmod - 1].setPhase(4);
        					inventoryList[6].setStack(inventoryList[6].Stack() - 1);
                			if(inventoryList[6].Stack() == 0)
                			{
                				inventoryList[6] = empty;
                			}
        				}
        			}
        		}
	            drawPanel.repaint();
        	}
        }
    };
    Action iAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(gameFreeze == false)
        	{
        		if(inventoryList[7].Name().equals("Health Potion"))
        		{
        			playSound("drink2");
        			if(health < 100)
        			{
        				health += 25;
        				if(health > 100)
        				{
        					health = 100;
        				}
        			}
        			inventoryList[7].setStack(inventoryList[7].Stack() - 1);
        			if(inventoryList[7].Stack() == 0)
        			{
        				inventoryList[7] = empty;
        			}
        		}
        		if(inventoryList[7].Name().equals("Key"))
        		{
        			if(direction == 0)
        			{
        				if(objects[ymod - 1][xmod].Type().equals("door") && objects[ymod - 1][xmod].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod - 1][xmod].setPhase(1);
        					inventoryList[7].setStack(inventoryList[7].Stack() - 1);
                			if(inventoryList[7].Stack() == 0)
                			{
                				inventoryList[7] = empty;
                			}
        				}
        			}else if(direction == 1)
        			{
        				if(objects[ymod][xmod + 1].Type().equals("door") && objects[ymod][xmod + 1].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod][xmod + 1].setPhase(2);
        					inventoryList[7].setStack(inventoryList[7].Stack() - 1);
                			if(inventoryList[7].Stack() == 0)
                			{
                				inventoryList[7] = empty;
                			}
        				}
        			}else if(direction == 2)
        			{
        				if(objects[ymod + 1][xmod].Type().equals("door") && objects[ymod + 1][xmod].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod + 1][xmod].setPhase(3);
        					inventoryList[7].setStack(inventoryList[7].Stack() - 1);
                			if(inventoryList[7].Stack() == 0)
                			{
                				inventoryList[7] = empty;
                			}
        				}
        			}else if(direction == 3)
        			{
        				if(objects[ymod][xmod - 1].Type().equals("door") && objects[ymod][xmod - 1].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod][xmod - 1].setPhase(4);
        					inventoryList[7].setStack(inventoryList[7].Stack() - 1);
                			if(inventoryList[7].Stack() == 0)
                			{
                				inventoryList[7] = empty;
                			}
        				}
        			}
        		}
	            drawPanel.repaint();
        	}
        }
    };
    Action oAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(gameFreeze == false)
        	{
        		if(inventoryList[8].Name().equals("Health Potion"))
        		{
        			playSound("drink2");
        			if(health < 100)
        			{
        				health += 25;
        				if(health > 100)
        				{
        					health = 100;
        				}
        			}
        			inventoryList[8].setStack(inventoryList[8].Stack() - 1);
        			if(inventoryList[8].Stack() == 0)
        			{
        				inventoryList[8] = empty;
        			}
        		}
        		if(inventoryList[8].Name().equals("Key"))
        		{
        			if(direction == 0)
        			{
        				if(objects[ymod - 1][xmod].Type().equals("door") && objects[ymod - 1][xmod].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod - 1][xmod].setPhase(1);
        					inventoryList[8].setStack(inventoryList[8].Stack() - 1);
                			if(inventoryList[8].Stack() == 0)
                			{
                				inventoryList[8] = empty;
                			}
        				}
        			}else if(direction == 1)
        			{
        				if(objects[ymod][xmod + 1].Type().equals("door") && objects[ymod][xmod + 1].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod][xmod + 1].setPhase(2);
        					inventoryList[8].setStack(inventoryList[8].Stack() - 1);
                			if(inventoryList[8].Stack() == 0)
                			{
                				inventoryList[8] = empty;
                			}
        				}
        			}else if(direction == 2)
        			{
        				if(objects[ymod + 1][xmod].Type().equals("door") && objects[ymod + 1][xmod].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod + 1][xmod].setPhase(3);
        					inventoryList[8].setStack(inventoryList[8].Stack() - 1);
                			if(inventoryList[8].Stack() == 0)
                			{
                				inventoryList[8] = empty;
                			}
        				}
        			}else if(direction == 3)
        			{
        				if(objects[ymod][xmod - 1].Type().equals("door") && objects[ymod][xmod - 1].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod][xmod - 1].setPhase(4);
        					inventoryList[8].setStack(inventoryList[8].Stack() - 1);
                			if(inventoryList[8].Stack() == 0)
                			{
                				inventoryList[8] = empty;
                			}
        				}
        			}
        		}
	            drawPanel.repaint();
        	}
        }
    };
    Action pAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(gameFreeze == false)
        	{
        		if(inventoryList[9].Name().equals("Health Potion"))
        		{
        			playSound("drink2");
        			if(health < 100)
        			{
        				health += 25;
        				if(health > 100)
        				{
        					health = 100;
        				}
        			}
        			inventoryList[9].setStack(inventoryList[9].Stack() - 1);
        			if(inventoryList[9].Stack() == 0)
        			{
        				inventoryList[9] = empty;
        			}
        		}
        		if(inventoryList[9].Name().equals("Key"))
        		{
        			if(direction == 0)
        			{
        				if(objects[ymod - 1][xmod].Type().equals("door") && objects[ymod - 1][xmod].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod - 1][xmod].setPhase(1);
        					inventoryList[9].setStack(inventoryList[9].Stack() - 1);
                			if(inventoryList[9].Stack() == 0)
                			{
                				inventoryList[9] = empty;
                			}
        				}
        			}else if(direction == 1)
        			{
        				if(objects[ymod][xmod + 1].Type().equals("door") && objects[ymod][xmod + 1].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod][xmod + 1].setPhase(2);
        					inventoryList[9].setStack(inventoryList[9].Stack() - 1);
                			if(inventoryList[9].Stack() == 0)
                			{
                				inventoryList[9] = empty;
                			}
        				}
        			}else if(direction == 2)
        			{
        				if(objects[ymod + 1][xmod].Type().equals("door") && objects[ymod + 1][xmod].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod + 1][xmod].setPhase(3);
        					inventoryList[9].setStack(inventoryList[9].Stack() - 1);
                			if(inventoryList[9].Stack() == 0)
                			{
                				inventoryList[9] = empty;
                			}
        				}
        			}else if(direction == 3)
        			{
        				if(objects[ymod][xmod - 1].Type().equals("door") && objects[ymod][xmod - 1].getPhase() == 0)
        				{
        					playSound("unlock3");
        					objects[ymod][xmod - 1].setPhase(4);
        					inventoryList[9].setStack(inventoryList[9].Stack() - 1);
                			if(inventoryList[9].Stack() == 0)
                			{
                				inventoryList[9] = empty;
                			}
        				}
        			}
        		}
	            drawPanel.repaint();
        	}
        }
    };
    Action spaceAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	playSound("click1");
        	if(inInventory == true)
        	{
	        	if(isSelected == false)
	        	{
		        	if((inventoryList[0].Name().equals("") && selector[0] == 0 && selector[1] == 0) || (inventoryList[1].Name().equals("") && selector[0] == 1 && selector[1] == 0)|| (inventoryList[2].Name().equals("") && selector[0] == 2 && selector[1] == 0)|| (inventoryList[3].Name().equals("") && selector[0] == 3 && selector[1] == 0))
		        	{}
		        	else
		        	{
			       		isSelected = true;
				 		selected[0] = selector[0];
				 		selected[1] = selector[1];
		        	}
	        	}
	        	else
	        	{
	        		if(selector[0] < 4 && selector[1] > 0 && selected[0] < 4 && selected[1] > 0)
	        		{
	        			if(selector[1] < 6)
	        			{
	        				if(selected[1] < 6)
	        				{
			        			if(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Name().equals(inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name()) && inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") && inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item"))
			        			{
			        				if(selector[0] == selected[0] && selector[1] == selected[1])
			        				{}
			        				else
			        				{
			        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].setStack(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Stack() + inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Stack());
				        				inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = empty;
			        				}
			        			}
			        			holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
			        			inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
			        			inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
	        				}
	        				else
	        				{
	        					if(direction == 0)
	        					{
	        						if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name().equals(inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item") && inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Stack());
					        				inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = empty;
				        				}
				        			}
				        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
				        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
				        			inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
	        					}
	        					else if(direction == 1)
	        					{
	        						if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name().equals(inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item") && inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Stack());
					        				inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = empty;
				        				}
				        			}
				        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
				        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
				        			inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
	        					}
	        					else if(direction == 2)
	        					{
	        						if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name().equals(inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item") && inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Stack());
					        				inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = empty;
				        				}
				        			}
				        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
				        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
				        			inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
	        					}
	        					else if(direction == 3)
	        					{
	        						if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name().equals(inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item") && inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Stack());
					        				inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = empty;
				        				}
				        			}
				        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
				        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
				        			inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
	        					}
	        				}
	        			}
	        			else
	        			{
	        				if(direction == 0)
	        				{
	        					if(selected[1] < 6)
	        					{
	        						if(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name()) && inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].setStack(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack());
					        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = empty;
				        				}
				        			}
		        					holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
		        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
		        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = holder;
	        					}
	        					else
	        					{
		        					if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack());
					        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = empty;
				        				}
				        			}
		        					holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
		        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
		        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = holder;
	        					}
	        				}
	        				else if(direction == 1)
	        				{
	        					if(selected[1] < 6)
	        					{
	        						if(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name()) && inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].setStack(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack());
					        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = empty;
				        				}
				        			}
		        					holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
		        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
		        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = holder;
	        					}
	        					else
	        					{
		        					if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack());
					        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = empty;
				        				}
				        			}
		        					holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
		        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
		        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = holder;
	        					}
	        				}
	        				else if(direction == 2)
	        				{
	        					if(selected[1] < 6)
	        					{
	        						if(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name()) && inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].setStack(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack());
					        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = empty;
				        				}
				        			}
		        					holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
		        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
		        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = holder;
	        					}
	        					else
	        					{
		        					if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack());
					        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = empty;
				        				}
				        			}
		        					holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
		        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
		        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = holder;
	        					}
	        				}
	        				else if(direction == 3)
	        				{
	        					if(selected[1] < 6)
	        					{
	        						if(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name()) && inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].setStack(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack());
					        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = empty;
				        				}
				        			}
		        					holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
		        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
		        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = holder;
	        					}
	        					else
	        					{
		        					if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item"))
				        			{
				        				if(selector[0] == selected[0] && selector[1] == selected[1])
				        				{}
				        				else
				        				{
				        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack());
					        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = empty;
				        				}
				        			}
		        					holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
		        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
		        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = holder;
	        					}
	        				}
	        			}
	        			holder = empty;
			        	isSelected = false;
	        		}
	        		if(selector[0] == selected[0] && selector[1] == selected[1])
	        		{
			        	isSelected = false;
	        		}  
	        		if(selector[1] < 6)
	        		{
	        			if(selected[1] < 6)
	        			{
		        			if(selector[0] < 4 && selector [1] > 0 && selected[0] == 0 && selected[1] == 0 && ((inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("") || inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("helmet"))))
			        		{
		        				holder = inventoryList[0];
			        			inventoryList[0] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
					        	inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
					        	holder = empty;
					        	isSelected = false;
		        			}
		        			if(selector[0] == 0 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("helmet") || inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("")))
			        		{
				        		holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
						        inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[0];
						        inventoryList[0] = holder;
						        holder = empty;
						        isSelected = false;
			        		}
		        			if(selector[0] < 4 && selector [1] > 0 && selected[0] == 1 && selected[1] == 0 && (inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("") || inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("chestplate")))
			        		{
				        		holder = inventoryList[1];
				        		inventoryList[1] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
						        inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
						        holder = empty;
						        isSelected = false;
			        		}
		        			if(selector[0] == 1 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("chestplate") || inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("")))
			        		{
				        		holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
						        inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[1];
						        inventoryList[1] = holder;
						        holder = empty;
						        isSelected = false;
			        		}
		        			if(selector[0] < 4 && selector [1] > 0 && selected[0] == 2 && selected[1] == 0 && (inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("") || inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("gloves")))
			        		{
				        		holder = inventoryList[2];
				        		inventoryList[2] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
						        inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
						        holder = empty;
						        isSelected = false;
			        		}
		        			if(selector[0] == 2 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("gloves") || inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("")))
			        		{
				        		holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
						       	inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[2];
						        inventoryList[2] = holder;
						        holder = empty;
						        isSelected = false;
			        		}
		        			if(selector[0] < 4 && selector [1] > 0 && selected[0] == 3 && selected[1] == 0 && (inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("") || inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("boots")))
			        		{
				        		holder = inventoryList[3];
				        		inventoryList[3] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
						        inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
						        holder = empty;
						        isSelected = false;
			        		}
		        			if(selector[0] == 3 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("boots") || inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("")))
			        		{
				        		holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
						        inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[3];
						        inventoryList[3] = holder;
						        holder = empty;
						       	isSelected = false;
			        		}
		        			if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 0 && (inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("") || inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("weapon")))
			        		{
				        		holder = inventoryList[4];
				        		inventoryList[4] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
						        inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
						        holder = empty;
						        isSelected = false;
			        		}
		        			if(selector[0] == 4 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("weapon") || inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("")))
			        		{//inventory to weapon spot
				        		holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
						       	inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[4];
						        inventoryList[4] = holder;
						        holder = empty;
						        isSelected = false;
			        		}
		        			if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 1 && (inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("") || inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("shield")))
			        		{//shield spot to inventory
				        		holder = inventoryList[5];
				        		inventoryList[5] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
						        inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
						        holder = empty;
						       	isSelected = false;
			        		}
		        			if(selector[0] == 4 && selector [1] == 1 && selected[0] < 4 && selected[1] > 0 && (inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("shield") || inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("")))
			        		{//inventory to shield spot
				        		holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
						        inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[5];
						        inventoryList[5] = holder;
						        holder = empty;
						        isSelected = false;
			        		}
	        			}
	        			else
	        			{
	        				if(direction == 0)
			        		{
			        			if(selector[0] == 0 && selector[1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("helmet") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[0];
							        inventoryList[0] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 1 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("chestplate") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[1];
							        inventoryList[1] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 2 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("gloves") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[2];
							        inventoryList[2] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 3 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("boots") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[3];
							        inventoryList[3] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 4 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("weapon") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[4];
							        inventoryList[4] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 4 && selector [1] == 1 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("shield") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[5];
							        inventoryList[5] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        		}
	        				else if(direction == 1)
			        		{
			        			if(selector[0] == 0 && selector[1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("helmet") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[0];
							        inventoryList[0] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 1 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("chestplate") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[1];
							        inventoryList[1] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 2 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("gloves") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[2];
							        inventoryList[2] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 3 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("boots") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[3];
							        inventoryList[3] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 4 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("weapon") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[4];
							        inventoryList[4] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 4 && selector [1] == 1 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("shield") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[5];
							        inventoryList[5] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        		}
	        				else if(direction == 2)
			        		{
			        			if(selector[0] == 0 && selector[1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("helmet") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[0];
							        inventoryList[0] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 1 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("chestplate") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[1];
							        inventoryList[1] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 2 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("gloves") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[2];
							        inventoryList[2] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 3 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("boots") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[3];
							        inventoryList[3] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 4 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("weapon") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[4];
							        inventoryList[4] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 4 && selector [1] == 1 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("shield") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[5];
							        inventoryList[5] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        		}
	        				else if(direction == 3)
			        		{
			        			if(selector[0] == 0 && selector[1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("helmet") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[0];
							        inventoryList[0] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 1 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("chestplate") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[1];
							        inventoryList[1] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 2 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("gloves") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[2];
							        inventoryList[2] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 3 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("boots") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[3];
							        inventoryList[3] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 4 && selector [1] == 0 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("weapon") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[4];
							        inventoryList[4] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        			if(selector[0] == 4 && selector [1] == 1 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("shield") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("")))
			        			{
			        				holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
			        				chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[5];
							        inventoryList[5] = holder;
							        holder = empty;
							        isSelected = false;
			        			}
			        		}
	        			}
	        		}
	        		else
	        		{
		        		if(direction == 0)
		        		{
		        			if(selector[0] < 4 && selector [1] > 0 && selected[1] == 0 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("") || (selected[0] == 0 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("helmet")) || (selected[0] == 1 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("chestplate"))|| (selected[0] == 2 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("gloves"))|| (selected[0] == 3 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("boots")) || (selected[0] == 4 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("weapon"))))
		        			{
		        				holder = inventoryList[selected[0]];
				        		inventoryList[selected[0]] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
				        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = holder;
						        holder = empty;
						        isSelected = false;
		        			}
		        			if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 1 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("shield")))
		        			{
		        				holder = inventoryList[5];
				        		inventoryList[5] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
				        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = holder;
						        holder = empty;
						        isSelected = false;
		        			}
	        			}
		        		else if(direction == 1)
		        		{
		        			if(selector[0] < 4 && selector [1] > 0 && selected[1] == 0 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("") || (selected[0] == 0 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("helmet")) || (selected[0] == 1 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("chestplate"))|| (selected[0] == 2 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("gloves"))|| (selected[0] == 3 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("boots"))|| (selected[0] == 4 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("weapon"))))
		        			{
		        				holder = inventoryList[selected[0]];
				        		inventoryList[selected[0]] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
				        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = holder;
						        holder = empty;
						        isSelected = false;
		        			}
		        			if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 1 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("shield")))
		        			{
		        				holder = inventoryList[5];
				        		inventoryList[5] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
				        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = holder;
						        holder = empty;
						        isSelected = false;
		        			}
	        			}
		        		else if(direction == 2)
		        		{
		        			if(selector[0] < 4 && selector [1] > 0 && selected[1] == 0 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("") || (selected[0] == 0 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("helmet")) || (selected[0] == 1 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("chestplate"))|| (selected[0] == 2 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("gloves"))|| (selected[0] == 3 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("boots"))|| (selected[0] == 4 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("weapon"))))
		        			{
		        				holder = inventoryList[selected[0]];
				        		inventoryList[selected[0]] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
				        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = holder;
						        holder = empty;
						        isSelected = false;
		        			}
		        			if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 1 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("shield")))
		        			{
		        				holder = inventoryList[5];
				        		inventoryList[5] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
				        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = holder;
						        holder = empty;
						        isSelected = false;
		        			}
	        			}
		        		else if(direction == 3)
		        		{
		        			if(selector[0] < 4 && selector [1] > 0 && selected[1] == 0 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("") || (selected[0] == 0 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("helmet")) || (selected[0] == 1 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("chestplate"))|| (selected[0] == 2 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("gloves"))|| (selected[0] == 3 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("boots"))|| (selected[0] == 4 && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("weapon"))))
		        			{
		        				holder = inventoryList[selected[0]];
				        		inventoryList[selected[0]] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
				        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = holder;
						        holder = empty;
						        isSelected = false;
		        			}
		        			if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 1 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("shield")))
		        			{
		        				holder = inventoryList[5];
				        		inventoryList[5] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
				        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = holder;
						        holder = empty;
						        isSelected = false;
		        			}
	        			}
	        		}
	        		
	        		//u i o p
	        		if(selector[1] < 6)
	        		{
	        			if(selected[1] < 6)
	        			{
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 2 && (inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("") || inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item")))
	    	        		{//u to inventory
	    	        			if(inventoryList[6].Name().equals(inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name()) && inventoryList[6].Type().equals("item") && inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[6].setStack(inventoryList[6].Stack() + inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Stack());
	    		        				inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = empty;
	    	        				}
	    	        			}	
	    		        		holder = inventoryList[6];
	    		        		inventoryList[6] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
	    				        inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
	    				        holder = empty;
	    				        isSelected = false;
	    	        		}
	        				if(selector[0] == 4 && selector [1] == 2 && selected[0] < 4 && selected[1] > 0 && (inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") || inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("")))
	    	        		{//inventory to u
	    	        			if(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Name().equals(inventoryList[6].Name()) && inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") && inventoryList[6].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].setStack(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Stack() + inventoryList[6].Stack());
	    		        				inventoryList[6] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
	    			        	inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[6];
	    			        	inventoryList[6] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	    	        		if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 3 && (inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("") || inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item")))
	    	        		{//i to inventory
	    	        			if(inventoryList[7].Name().equals(inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name()) && inventoryList[7].Type().equals("item") && inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[7].setStack(inventoryList[7].Stack() + inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Stack());
	    		        				inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[7];
	    	        			inventoryList[7] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
	    			        	inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	    	        		if(selector[0] == 4 && selector [1] == 3 && selected[0] < 4 && selected[1] > 0 && (inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") || inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("")))
	    	        		{//inventory to i
	    	        			if(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Name().equals(inventoryList[7].Name()) && inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") && inventoryList[7].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].setStack(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Stack() + inventoryList[7].Stack());
	    		        				inventoryList[7] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
	    			        	inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[7];
	    			        	inventoryList[7] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	    	        		if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 4 && (inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("") || inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item")))
	    	        		{//o to inventory
	    	        			if(inventoryList[8].Name().equals(inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name()) && inventoryList[8].Type().equals("item") && inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[8].setStack(inventoryList[8].Stack() + inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Stack());
	    		        				inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[8];
	    	        			inventoryList[8] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
	    			        	inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	    	        		if(selector[0] == 4 && selector [1] == 4 && selected[0] < 4 && selected[1] > 0 && (inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") || inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("")))
	    	        		{//inventory to o
	    	        			if(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Name().equals(inventoryList[8].Name()) && inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") && inventoryList[8].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].setStack(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Stack() + inventoryList[8].Stack());
	    		        				inventoryList[8] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
	    			        	inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[8];
	    			        	inventoryList[8] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	    	        		if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 5 && (inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("") || inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item")))
	    	        		{//p to inventory
	    	        			if(inventoryList[9].Name().equals(inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name()) && inventoryList[9].Type().equals("item") && inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[9].setStack(inventoryList[9].Stack() + inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Stack());
	    		        				inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[9];
	    	        			inventoryList[9] = inventoryList[(5 * selector[0]) + selector[1] - 1 + 10];
	    			        	inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	    	        		if(selector[0] == 4 && selector [1] == 5 && selected[0] < 4 && selected[1] > 0 && (inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") || inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("")))
	    	        		{//inventory to p
	    	        			if(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Name().equals(inventoryList[9].Name()) && inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Type().equals("item") && inventoryList[9].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].setStack(inventoryList[(5 * selected[0]) + selected[1] - 1 + 10].Stack() + inventoryList[9].Stack());
	    		        				inventoryList[9] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[(5 * selected[0]) + selected[1] - 1 + 10];
	    			        	inventoryList[(5 * selected[0]) + selected[1] - 1 + 10] = inventoryList[9];
	    			        	inventoryList[9] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	    	        		if(selector[0] == 4 && (selector[1] == 2 || selector[1] == 3 || selector[1] == 4 || selector[1] == 5) && selected[0] == 4 && (selected[1] == 2 || selected[1] == 3 || selected[1] == 4 || selected[1] == 5))
	    	        		{//u to i to o to p
	    	        			if(inventoryList[4 + selected[1]].Name().equals(inventoryList[4 + selector[1]].Name()) && inventoryList[4 + selected[1]].Type().equals("item") && inventoryList[4 + selector[1]].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[4 + selected[1]].setStack(inventoryList[4 + selected[1]].Stack() + inventoryList[4 + selector[1]].Stack());
	    		        				inventoryList[4 + selector[1]] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[4 + selected[1]];
	    	        			inventoryList[4 + selected[1]] = inventoryList[4 + selector[1]];
	    			        	inventoryList[4 + selector[1]] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        			}
	        			else
	        			{
	        				if(direction == 0)
	        				{
	        					if(selector[0] == 4 && selector [1] == 2 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("")))
		    	        		{//inventory to u
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name().equals(inventoryList[6].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item") && inventoryList[6].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + inventoryList[6].Stack());
		    		        				inventoryList[6] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[6];
		    			        	inventoryList[6] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 3 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("")))
		    	        		{//inventory to i
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name().equals(inventoryList[7].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item") && inventoryList[7].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + inventoryList[7].Stack());
		    		        				inventoryList[7] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[7];
		    			        	inventoryList[7] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 4 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("")))
		    	        		{//inventory to o
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name().equals(inventoryList[8].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item") && inventoryList[8].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + inventoryList[8].Stack());
		    		        				inventoryList[8] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[8];
		    			        	inventoryList[8] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 5 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("")))
		    	        		{//inventory to p
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name().equals(inventoryList[9].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item") && inventoryList[9].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + inventoryList[9].Stack());
		    		        				inventoryList[9] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = inventoryList[9];
		    			        	inventoryList[9] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        				}
	        				if(direction == 1)
	        				{
	        					if(selector[0] == 4 && selector [1] == 2 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("")))
		    	        		{//inventory to u
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name().equals(inventoryList[6].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item") && inventoryList[6].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + inventoryList[6].Stack());
		    		        				inventoryList[6] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[6];
		    			        	inventoryList[6] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 3 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("")))
		    	        		{//inventory to i
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name().equals(inventoryList[7].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item") && inventoryList[7].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + inventoryList[7].Stack());
		    		        				inventoryList[7] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[7];
		    			        	inventoryList[7] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 4 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("")))
		    	        		{//inventory to o
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name().equals(inventoryList[8].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item") && inventoryList[8].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + inventoryList[8].Stack());
		    		        				inventoryList[8] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[8];
		    			        	inventoryList[8] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 5 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("")))
		    	        		{//inventory to p
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name().equals(inventoryList[9].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item") && inventoryList[9].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + inventoryList[9].Stack());
		    		        				inventoryList[9] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = inventoryList[9];
		    			        	inventoryList[9] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        				}
	        				if(direction == 2)
	        				{
	        					if(selector[0] == 4 && selector [1] == 2 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("")))
		    	        		{//inventory to u
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name().equals(inventoryList[6].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item") && inventoryList[6].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + inventoryList[6].Stack());
		    		        				inventoryList[6] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[6];
		    			        	inventoryList[6] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 3 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("")))
		    	        		{//inventory to i
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name().equals(inventoryList[7].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item") && inventoryList[7].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + inventoryList[7].Stack());
		    		        				inventoryList[7] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[7];
		    			        	inventoryList[7] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 4 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("")))
		    	        		{//inventory to o
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name().equals(inventoryList[8].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item") && inventoryList[8].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + inventoryList[8].Stack());
		    		        				inventoryList[8] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[8];
		    			        	inventoryList[8] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 5 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("")))
		    	        		{//inventory to p
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name().equals(inventoryList[9].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item") && inventoryList[9].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + inventoryList[9].Stack());
		    		        				inventoryList[9] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = inventoryList[9];
		    			        	inventoryList[9] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        				}
	        				if(direction == 3)
	        				{
	        					if(selector[0] == 4 && selector [1] == 2 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("")))
		    	        		{//inventory to u
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name().equals(inventoryList[6].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item") && inventoryList[6].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + inventoryList[6].Stack());
		    		        				inventoryList[6] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[6];
		    			        	inventoryList[6] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 3 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("")))
		    	        		{//inventory to i
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name().equals(inventoryList[7].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item") && inventoryList[7].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + inventoryList[7].Stack());
		    		        				inventoryList[7] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[7];
		    			        	inventoryList[7] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 4 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("")))
		    	        		{//inventory to o
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name().equals(inventoryList[8].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item") && inventoryList[8].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + inventoryList[8].Stack());
		    		        				inventoryList[8] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[8];
		    			        	inventoryList[8] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        					if(selector[0] == 4 && selector [1] == 5 && selected[0] < 4 && selected[1] > 0 && (chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item") || chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("")))
		    	        		{//inventory to p
		    	        			if(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name().equals(inventoryList[9].Name()) && chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item") && inventoryList[9].Type().equals("item"))
		    	        			{
		    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
		    	        				{}
		    	        				else
		    	        				{
		    	        					chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].setStack(chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + inventoryList[9].Stack());
		    		        				inventoryList[9] = empty;
		    	        				}
		    	        			}	
		    	        			holder = chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
		    	        			chestInventoryList[(5 * selected[0]) + selected[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = inventoryList[9];
		    			        	inventoryList[9] = holder;
		    			        	holder = empty;
		    			        	isSelected = false;
		    	        		}
	        				}
	        			}
	        		}
	        		else
	        		{
	        			if(direction == 0)
	        			{
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 2 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item")))
	    	        		{//u to inventory
	    	        			if(inventoryList[6].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name()) && inventoryList[6].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[6].setStack(inventoryList[6].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = empty;
	    	        				}
	    	        			}	
	    		        		holder = inventoryList[6];
	    		        		inventoryList[6] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
	    		        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = holder;
	    				        holder = empty;
	    				        isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 3 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item")))
	    	        		{//i to inventory
	    	        			if(inventoryList[7].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name()) && inventoryList[7].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[7].setStack(inventoryList[7].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[7];
	    	        			inventoryList[7] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 4 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item")))
	    	        		{//o to inventory
	    	        			if(inventoryList[8].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name()) && inventoryList[8].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[8].setStack(inventoryList[8].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[8];
	    	        			inventoryList[8] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 5 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item")))
	    	        		{//p to inventory
	    	        			if(inventoryList[9].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Name()) && inventoryList[7].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[9].setStack(inventoryList[9].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[9];
	    	        			inventoryList[9] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        			}
	        			if(direction == 1)
	        			{
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 2 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item")))
	    	        		{//u to inventory
	    	        			if(inventoryList[6].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name()) && inventoryList[6].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[6].setStack(inventoryList[6].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = empty;
	    	        				}
	    	        			}	
	    		        		holder = inventoryList[6];
	    		        		inventoryList[6] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
	    		        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = holder;
	    				        holder = empty;
	    				        isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 3 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item")))
	    	        		{//i to inventory
	    	        			if(inventoryList[7].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name()) && inventoryList[7].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[7].setStack(inventoryList[7].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[7];
	    	        			inventoryList[7] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 4 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item")))
	    	        		{//o to inventory
	    	        			if(inventoryList[8].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name()) && inventoryList[8].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[8].setStack(inventoryList[8].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[8];
	    	        			inventoryList[8] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 5 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item")))
	    	        		{//p to inventory
	    	        			if(inventoryList[9].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Name()) && inventoryList[7].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[9].setStack(inventoryList[9].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[9];
	    	        			inventoryList[9] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        			}
	        			if(direction == 2)
	        			{
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 2 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item")))
	    	        		{//u to inventory
	    	        			if(inventoryList[6].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name()) && inventoryList[6].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[6].setStack(inventoryList[6].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = empty;
	    	        				}
	    	        			}	
	    		        		holder = inventoryList[6];
	    		        		inventoryList[6] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
	    		        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = holder;
	    				        holder = empty;
	    				        isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 3 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item")))
	    	        		{//i to inventory
	    	        			if(inventoryList[7].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name()) && inventoryList[7].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[7].setStack(inventoryList[7].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[7];
	    	        			inventoryList[7] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 4 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item")))
	    	        		{//o to inventory
	    	        			if(inventoryList[8].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name()) && inventoryList[8].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[8].setStack(inventoryList[8].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[8];
	    	        			inventoryList[8] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 5 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item")))
	    	        		{//p to inventory
	    	        			if(inventoryList[9].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Name()) && inventoryList[7].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[9].setStack(inventoryList[9].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[9];
	    	        			inventoryList[9] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        			}
	        			if(direction == 3)
	        			{
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 2 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item")))
	    	        		{//u to inventory
	    	        			if(inventoryList[6].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name()) && inventoryList[6].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[6].setStack(inventoryList[6].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = empty;
	    	        				}
	    	        			}	
	    		        		holder = inventoryList[6];
	    		        		inventoryList[6] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
	    		        		chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = holder;
	    				        holder = empty;
	    				        isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 3 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item")))
	    	        		{//i to inventory
	    	        			if(inventoryList[7].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name()) && inventoryList[7].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[7].setStack(inventoryList[7].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[7];
	    	        			inventoryList[7] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 4 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item")))
	    	        		{//o to inventory
	    	        			if(inventoryList[8].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name()) && inventoryList[8].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[8].setStack(inventoryList[8].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[8];
	    	        			inventoryList[8] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        				if(selector[0] < 4 && selector [1] > 0 && selected[0] == 4 && selected[1] == 5 && (chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("") || chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item")))
	    	        		{//p to inventory
	    	        			if(inventoryList[9].Name().equals(chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Name()) && inventoryList[7].Type().equals("item") && chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Type().equals("item"))
	    	        			{
	    	        				if(selector[0] == selected[0] && selector[1] == selected[1])
	    	        				{}
	    	        				else
	    	        				{
	    	        					inventoryList[9].setStack(inventoryList[9].Stack() + chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack());
	    	        					chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = empty;
	    	        				}
	    	        			}	
	    	        			holder = inventoryList[9];
	    	        			inventoryList[9] = chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()];
	    	        			chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = holder;
	    			        	holder = empty;
	    			        	isSelected = false;
	    	        		}
	        			}
	        		}
	        	}
	            drawPanel.repaint();
        	}
        }
    };
    Action escapeAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	playSound("chestClose");
            if(inMenu == false)
            {
            	inMenu = true;
            	gameFreeze = true;
            	inInventory = false;
            	isSelected = false;
            	inChest = false;
            	shifter = 0;
            	fInteract = false;
            }
            else
            {
            	if(inControls == true)
            	{
	            	inControls = false;
	            	menuSelector = 0;
            	}
            	else
            	{
            		if(inOptions == true)
	            	{
	            		inOptions = false;
	            		menuSelector = 0;
	            	}
	            	else
	            	{
	            		inMenu = false;
	                	gameFreeze = false;
	            	}
            	}
            }
            
            drawPanel.repaint();
        }
    };
    Action enterAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(gameOver == true)
        	{
        		try
       			{
       			    String file;
       				if(gameMode == 0)
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
        		EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        Menu menu = new Menu();
                        menu.recoverDifficulty();
                    }
                });
        		musicPlay = false;
        		music[0].close();
                setVisible(false);
                dispose(); 
        	}
        	if(inMenu == true)
        		playSound("click1");
            if(inMenu == true && !inControls)
            {
            	if(inOptions == true)
            	{
            		if(menuSelector == 0 && isSaving == false)
	            	{
	            		inControls = true;
	            	}
	            	if(menuSelector == 1)
	            	{
	            		//open journal
	            	}
	            	if(menuSelector == 2)
	            	{
	            		if(musicPlay == true)
		            	{
	            			musicPlay = false;
		            		music[0].close();
		            	}
		            	else
		            	{
		            		musicPlay = true;
		            		music();
		            	}
	            	}
            	}
            	else
            	{
	            	if(menuSelector == 0 && isSaving == false)
	            	{
	            		savePlayerData();
	            	}
	            	if(menuSelector == 1)
	            	{
	            		menuSelector = 0;
	            		inOptions = true;
	            	}
	            	if(menuSelector == 2)
	            	{
	            		//open store
	            	}
	            	if(menuSelector == 3)
	            	{
	            		savePlayerData();
	            		EventQueue.invokeLater(new Runnable()
	                    {
	                        public void run()
	                        {
	                            Menu menu = new Menu();
	                            menu.recoverDifficulty();
	                        }
	                    });
	            		musicPlay = false;
	            		music[0].close();
	                    setVisible(false);
	                    dispose(); 
	            	}
            	}
            }
            drawPanel.repaint();
        }
    };
    Action xAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(inInventory == true)
        	{
        		if(deleteSet == false)
        		{
        			deleteSet = true;
        		}
        		else if(selector[0] < 4 && selector[1] > 0)
        		{
        			if(selector[1] < 6)
        			{
        				inventoryList[(5 * selector[0]) + selector[1] - 1 + 10] = empty;
        			}
        			else if(direction == 0)
        			{
        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod - 1][xmod].getPhase()] = empty;
        			}
        			else if(direction == 1)
        			{
        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod + 1].getPhase()] = empty;
        			}
        			else if(direction == 2)
        			{
        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod + 1][xmod].getPhase()] = empty;
        			}
        			else if(direction == 3)
        			{
        				chestInventoryList[(5 * selector[0]) + selector[1] - 6 + 20 * objects[ymod][xmod - 1].getPhase()] = empty;
        			}
        			deleteSet = false;
        		}
	            drawPanel.repaint();
        	}
        }
    };
    Action fAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
        	if(inInventory == false && inMenu == false && ((objects[ymod - 1][xmod].Type() == "chest"  && direction == 0) || (objects[ymod][xmod + 1].Type() == "chest"  && direction == 1) || (objects[ymod + 1][xmod].Type() == "chest"  && direction == 2) || (objects[ymod][xmod - 1].Type() == "chest"  && direction == 3)))
            {
        		playSound("chestClose");
            	inInventory = true;
            	inChest = true;
            	gameFreeze = true;
            	shifter = 175;
            	fInteract = false;
            	for(int row = 0; row < objects.length; row++)
            	{
            		for(int col = 0; col < objects[0].length; col++)
            		{
            			if(objects[row][col].Type().equals("chest"))
            			{
            				if((row == ymod - 1 && col == xmod && direction == 0) || (row == ymod && col == xmod + 1 && direction == 1) || (row == ymod + 1 && col == xmod && direction == 2) || (row == ymod && col == xmod - 1 && direction == 3))
            				{
            					objects[row][col].setPhase(chestCounter);
            				}
            				else
            				{
            					chestCounter++;
            				}
            			}
            		}
            	}
            	chestCounter = 0;
            }
            
            drawPanel.repaint();
        }
    };
    InputMap inputMap = drawPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = drawPanel.getActionMap();

    inputMap.put(KeyStroke.getKeyStroke("W"), "wAction");
    actionMap.put("wAction", wAction);
    inputMap.put(KeyStroke.getKeyStroke("D"), "dAction");
    actionMap.put("dAction", dAction);
    inputMap.put(KeyStroke.getKeyStroke("A"), "aAction");
    actionMap.put("aAction", aAction);
    inputMap.put(KeyStroke.getKeyStroke("S"), "sAction");
    actionMap.put("sAction", sAction);
    inputMap.put(KeyStroke.getKeyStroke("E"), "eAction");
    actionMap.put("eAction", eAction);
    inputMap.put(KeyStroke.getKeyStroke("UP"), "upAction");
    actionMap.put("upAction", upAction);
    inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
    actionMap.put("rightAction", rightAction);
    inputMap.put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
    actionMap.put("leftAction", leftAction);
    inputMap.put(KeyStroke.getKeyStroke("DOWN"), "downAction");
    actionMap.put("downAction", downAction);
    inputMap.put(KeyStroke.getKeyStroke("K"), "kAction");
    actionMap.put("kAction", kAction);
    inputMap.put(KeyStroke.getKeyStroke("L"), "lAction");
    actionMap.put("lAction", lAction);
    inputMap.put(KeyStroke.getKeyStroke("U"), "uAction");
    actionMap.put("uAction", uAction);
    inputMap.put(KeyStroke.getKeyStroke("I"), "iAction");
    actionMap.put("iAction", iAction);
    inputMap.put(KeyStroke.getKeyStroke("O"), "oAction");
    actionMap.put("oAction", oAction);
    inputMap.put(KeyStroke.getKeyStroke("P"), "pAction");
    actionMap.put("pAction", pAction);
    inputMap.put(KeyStroke.getKeyStroke("SPACE"), "spaceAction");
    actionMap.put("spaceAction", spaceAction);
    inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "escapeAction");
    actionMap.put("escapeAction", escapeAction);
    inputMap.put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
    actionMap.put("enterAction", enterAction);
    inputMap.put(KeyStroke.getKeyStroke("X"), "xAction");
    actionMap.put("xAction", xAction);
    inputMap.put(KeyStroke.getKeyStroke("F"), "fAction");
    actionMap.put("fAction", fAction);

	 //Create an ImageIcon object  
	ii=new ImageIcon(inventory);
	ii=new ImageIcon(hotbar);
	 ii=new ImageIcon(roomTemplate);  
	 ii=new ImageIcon(vPathTemplate);
	 ii=new ImageIcon(hPathTemplate);
	 ii=new ImageIcon(luTurnTemplate);
	 ii=new ImageIcon(ldTurnTemplate);
	 ii=new ImageIcon(ruTurnTemplate);
	 ii=new ImageIcon(rdTurnTemplate);
	 ii=new ImageIcon(man1); 
	 ii=new ImageIcon(boots1); 
	 ii=new ImageIcon(chestplate1); 
	 ii=new ImageIcon(gloves1); 
	 ii=new ImageIcon(helmet1); 
	 ii=new ImageIcon(body2); 
	 ii=new ImageIcon(boots2); 
	 ii=new ImageIcon(chestplate2); 
	 ii=new ImageIcon(gloves2); 
	 ii=new ImageIcon(helmet2); 
	 ii=new ImageIcon(body3); 
	 ii=new ImageIcon(boots3); 
	 ii=new ImageIcon(chestplate3); 
	 ii=new ImageIcon(gloves3); 
	 ii=new ImageIcon(helmet3); 
	 ii=new ImageIcon(body4); 
	 ii=new ImageIcon(boots4); 
	 ii=new ImageIcon(chestplate4); 
	 ii=new ImageIcon(gloves4); 
	 ii=new ImageIcon(helmet4); 
	 ii=new ImageIcon(helmetLogo);
	 ii=new ImageIcon(chestplateLogo);
	 ii=new ImageIcon(glovesLogo);
	 ii=new ImageIcon(bootsLogo);
	 ii=new ImageIcon(hudbackground);
	 ii=new ImageIcon(selectorCursor);
	 ii=new ImageIcon(selectedIcon);
	 ii=new ImageIcon(inventoryIcon);
	 ii=new ImageIcon(shieldLogo);
	 ii=new ImageIcon(potionLogo);
	 ii=new ImageIcon(shieldImage1a);
	 ii=new ImageIcon(shieldImage1b);
	 ii=new ImageIcon(shieldImage2a);
	 ii=new ImageIcon(shieldImage2b);
	 ii=new ImageIcon(shieldImage3a);
	 ii=new ImageIcon(shieldImage3b);
	 ii=new ImageIcon(shieldImage4a);
	 ii=new ImageIcon(shieldImage4b);
	 ii=new ImageIcon(heartImage);
	 ii=new ImageIcon(armorBarOff1);
	 ii=new ImageIcon(armorBarOff2);
	 ii=new ImageIcon(armorBarOn);
	 ii=new ImageIcon(menuImage);
	 ii=new ImageIcon(menuImage2);
	 ii=new ImageIcon(menuSelectorImage);
	 ii=new ImageIcon(savedImage);
	 ii=new ImageIcon(deleteIcon);
	 ii=new ImageIcon(spikesOn);
	 ii=new ImageIcon(spikesOff);
	 ii=new ImageIcon(damageTickScreen);
	 ii=new ImageIcon(pit);
	 ii=new ImageIcon(crate);
	 ii=new ImageIcon(chest);
	 ii=new ImageIcon(fInteraction);
	 ii=new ImageIcon(chestInventory);
	 ii=new ImageIcon(helmetLogo1);
	 ii=new ImageIcon(helmetLogo2);
	 ii=new ImageIcon(helmetLogo3);
	 ii=new ImageIcon(chestplateLogo1);
	 ii=new ImageIcon(chestplateLogo2);
	 ii=new ImageIcon(chestplateLogo3);
	 ii=new ImageIcon(glovesLogo1);
	 ii=new ImageIcon(glovesLogo2);
	 ii=new ImageIcon(glovesLogo3);
	 ii=new ImageIcon(bootsLogo1);
	 ii=new ImageIcon(bootsLogo2);
	 ii=new ImageIcon(bootsLogo3);
	 ii=new ImageIcon(boots11);
	 ii=new ImageIcon(boots12);
	 ii=new ImageIcon(boots13);
	 ii=new ImageIcon(boots14);
	 ii=new ImageIcon(boots21);
	 ii=new ImageIcon(boots22);
	 ii=new ImageIcon(boots23);
	 ii=new ImageIcon(boots24);
	 ii=new ImageIcon(boots31);
	 ii=new ImageIcon(boots32);
	 ii=new ImageIcon(boots33);
	 ii=new ImageIcon(boots34);
	 ii=new ImageIcon(boots41);
	 ii=new ImageIcon(boots42);
	 ii=new ImageIcon(boots43);
	 ii=new ImageIcon(boots44);
	 ii=new ImageIcon(helmet11);
	 ii=new ImageIcon(helmet12);
	 ii=new ImageIcon(helmet13);
	 ii=new ImageIcon(helmet14);
	 ii=new ImageIcon(helmet21);
	 ii=new ImageIcon(helmet22);
	 ii=new ImageIcon(helmet23);
	 ii=new ImageIcon(helmet24);
	 ii=new ImageIcon(helmet31);
	 ii=new ImageIcon(helmet32);
	 ii=new ImageIcon(helmet33);
	 ii=new ImageIcon(helmet34);
	 ii=new ImageIcon(helmet41);
	 ii=new ImageIcon(helmet42);
	 ii=new ImageIcon(helmet43);
	 ii=new ImageIcon(helmet44);
	 ii=new ImageIcon(chestplate11);
	 ii=new ImageIcon(chestplate12);
	 ii=new ImageIcon(chestplate13);
	 ii=new ImageIcon(chestplate14);
	 ii=new ImageIcon(chestplate21);
	 ii=new ImageIcon(chestplate22);
	 ii=new ImageIcon(chestplate23);
	 ii=new ImageIcon(chestplate24);
	 ii=new ImageIcon(chestplate31);
	 ii=new ImageIcon(chestplate32);
	 ii=new ImageIcon(chestplate33);
	 ii=new ImageIcon(chestplate34);
	 ii=new ImageIcon(chestplate41);
	 ii=new ImageIcon(chestplate42);
	 ii=new ImageIcon(chestplate43);
	 ii=new ImageIcon(chestplate44);
	 ii=new ImageIcon(gloves11);
	 ii=new ImageIcon(gloves12);
	 ii=new ImageIcon(gloves13);
	 ii=new ImageIcon(gloves14);
	 ii=new ImageIcon(gloves21);
	 ii=new ImageIcon(gloves22);
	 ii=new ImageIcon(gloves23);
	 ii=new ImageIcon(gloves24);
	 ii=new ImageIcon(gloves31);
	 ii=new ImageIcon(gloves32);
	 ii=new ImageIcon(gloves33);
	 ii=new ImageIcon(gloves34);
	 ii=new ImageIcon(gloves41);
	 ii=new ImageIcon(gloves42);
	 ii=new ImageIcon(gloves43);
	 ii=new ImageIcon(gloves44);
	 ii=new ImageIcon(wallImage);
	 ii=new ImageIcon(vdoor);
	 ii=new ImageIcon(hdoor);
	 ii=new ImageIcon(checkpointOff);
	 ii=new ImageIcon(checkpointOn);
	 ii=new ImageIcon(keyLogo);
	 ii=new ImageIcon(zombie1);
	 ii=new ImageIcon(zombie2);
	 ii=new ImageIcon(zombie3);
	 ii=new ImageIcon(zombie4);
	 ii=new ImageIcon(dzombie1);
	 ii=new ImageIcon(dzombie2);
	 ii=new ImageIcon(dzombie3);
	 ii=new ImageIcon(dzombie4);
	 ii=new ImageIcon(skeleton1);
	 ii=new ImageIcon(skeleton2);
	 ii=new ImageIcon(skeleton3);
	 ii=new ImageIcon(skeleton4);
	 ii=new ImageIcon(dskeleton1);
	 ii=new ImageIcon(dskeleton2);
	 ii=new ImageIcon(dskeleton3);
	 ii=new ImageIcon(dskeleton4);
	 ii=new ImageIcon(path1);
	 ii=new ImageIcon(path2);
	 ii=new ImageIcon(path3);
	 ii=new ImageIcon(arrow1);
	 ii=new ImageIcon(arrow2);
	 ii=new ImageIcon(arrow3);
	 ii=new ImageIcon(arrow4);
	 ii=new ImageIcon(bowA1);
	 ii=new ImageIcon(bowB1);
	 ii=new ImageIcon(bowC1);
	 ii=new ImageIcon(bowD1);
	 ii=new ImageIcon(bowA2);
	 ii=new ImageIcon(bowB2);
	 ii=new ImageIcon(bowC2);
	 ii=new ImageIcon(bowD2);
	 ii=new ImageIcon(bowA3);
	 ii=new ImageIcon(bowB3);
	 ii=new ImageIcon(bowC3);
	 ii=new ImageIcon(bowD3);
	 ii=new ImageIcon(shooter1);
	 ii=new ImageIcon(shooter2);
	 ii=new ImageIcon(shooter3);
	 ii=new ImageIcon(shooter4);
	 ii=new ImageIcon(lootbag);
	 ii=new ImageIcon(levelChanger);
	 ii=new ImageIcon(swordLogo0);
	 ii=new ImageIcon(swordImage01);
	 ii=new ImageIcon(swordImage02);
	 ii=new ImageIcon(swordImage03);
	 ii=new ImageIcon(swordImage04);
	 ii=new ImageIcon(swordLogo1);
	 ii=new ImageIcon(swordImage11);
	 ii=new ImageIcon(swordImage12);
	 ii=new ImageIcon(swordImage13);
	 ii=new ImageIcon(swordImage14);
	 ii=new ImageIcon(swordLogo2);
	 ii=new ImageIcon(swordImage21);
	 ii=new ImageIcon(swordImage22);
	 ii=new ImageIcon(swordImage23);
	 ii=new ImageIcon(swordImage24);
	 ii=new ImageIcon(swordLogo3);
	 ii=new ImageIcon(swordImage31);
	 ii=new ImageIcon(swordImage32);
	 ii=new ImageIcon(swordImage33);
	 ii=new ImageIcon(swordImage34);
	 ii=new ImageIcon(swordLogo4);
	 ii=new ImageIcon(swordImage41);
	 ii=new ImageIcon(swordImage42);
	 ii=new ImageIcon(swordImage43);
	 ii=new ImageIcon(swordImage44);
	 ii=new ImageIcon(swordLogo5);
	 ii=new ImageIcon(swordImage51);
	 ii=new ImageIcon(swordImage52);
	 ii=new ImageIcon(swordImage53);
	 ii=new ImageIcon(swordImage54);
	 ii=new ImageIcon(controls);
	 ii=new ImageIcon(gameOverScreen);
	 ii=new ImageIcon(gameOverEasy);
	 ii=new ImageIcon(gameOverNormal);
	 ii=new ImageIcon(gameOverHard);
	 ii=new ImageIcon(gameOver1);
	 ii=new ImageIcon(gameOver2);
	 ii=new ImageIcon(gameOver3);
	 ii=new ImageIcon(gameOver4);
	 ii=new ImageIcon(gameOver5);
	 ii=new ImageIcon(gameOver6);
	 ii=new ImageIcon(gameOver7);
	 ii=new ImageIcon(gameOver8);
	 ii=new ImageIcon(gameOver9);
	 ii=new ImageIcon(gameOver0);
	 ii=new ImageIcon(livesImage);
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

public void convertItems()
{
	for(int i = 0; i < chestInventoryList.length/20; i++)
	{
		for(int j = 0; j < 20; j++)
		{
			if(chestInventoryList[(i * 20) + j].Name().equals("Health Potion"))
				chestInventoryList[(i * 20) + j] = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
		}
	}
}

public void resetAllPhases()
{
	
	for(int row = 0; row < objects.length; row++)
	{
		for(int col = 0; col < objects[0].length; col++)
		{
			
			objects[row][col].setPhase(objects[row][col].getVersion());
		}
	}
}

public void resetPhases()
{
	for(int row = 0; row < objects.length; row++)
	{
		for(int col = 0; col < objects[0].length; col++)
		{
			if(objects[row][col].Type().equals("crate"));
			{
				objects[row][col].setPhase(0);
			}
		}
	}
}

public void cullEnemies()
{
	for(int i = 0; i < enemyList.size(); i++)
	{
		for(int j = 0; j < enemyKillList.size(); j++)
		{
			if(enemyList.get(i).getId() == enemyKillList.get(j))
			{
				enemyList.remove(i);
				i--;
			}
		}
	}
}

public void populateChests()
{
	//Item[] chestInventoryList = new Item[1000]; first chest is phase 0, so on, and each chest is 0-19 + 20 * phase 
	int totalChests = 0;
	for(int row=0;row<objects.length;row++)
	{
		for(int col=0;col<objects[0].length;col++)
		{
			if(objects[row][col].Type().equals("chest"))
				totalChests++;
		}
	}
	for(int i = 0; i < 1000;i++)
	{
		chestInventoryList[i] = empty;
	}
	
	int[] slots = new int[3];
	Item[] options = new Item[100];
	for(int i = 0; i < 40; i++)
		options[i] = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	if(level == 1 || level == 2)
	{
		for(int i = 40; i < 48; i++)
			options[i] = head2;
		for(int i = 48; i < 56; i++)
			options[i] = chest2;
		for(int i = 56; i < 64; i++)
			options[i] = hands2;
		for(int i = 64; i < 72; i++)
			options[i] = feet2;
		for(int i = 72; i < 80; i++)
			options[i] = commonSword;
		for(int i = 80; i < 84; i++)
			options[i] = head1;
		for(int i = 84; i < 88; i++)
			options[i] = chest1;
		for(int i = 88; i < 92; i++)
			options[i] = hands1;
		for(int i = 92; i < 96; i++)
			options[i] = feet1;
		for(int i = 96; i < 100; i++)
			options[i] = fineSword;
	}
	else if(level == 3 || level == 4)
	{
		for(int i = 40; i < 48; i++)
			options[i] = head1;
		for(int i = 48; i < 56; i++)
			options[i] = chest1;
		for(int i = 56; i < 64; i++)
			options[i] = hands1;
		for(int i = 64; i < 72; i++)
			options[i] = feet1;
		for(int i = 72; i < 80; i++)
			options[i] = fineSword;
		for(int i = 80; i < 84; i++)
			options[i] = head5;
		for(int i = 84; i < 88; i++)
			options[i] = chest5;
		for(int i = 88; i < 92; i++)
			options[i] = hands5;
		for(int i = 92; i < 96; i++)
			options[i] = feet5;
		for(int i = 96; i < 100; i++)
			options[i] = superiorSword;
	}
	else if(level == 5 || level == 6)
	{
		for(int i = 40; i < 48; i++)
			options[i] = head5;
		for(int i = 48; i < 56; i++)
			options[i] = chest5;
		for(int i = 56; i < 64; i++)
			options[i] = hands5;
		for(int i = 64; i < 72; i++)
			options[i] = feet5;
		for(int i = 72; i < 80; i++)
			options[i] = superiorSword;
		for(int i = 80; i < 84; i++)
			options[i] = head3;
		for(int i = 84; i < 88; i++)
			options[i] = chest3;
		for(int i = 88; i < 92; i++)
			options[i] = hands3;
		for(int i = 92; i < 96; i++)
			options[i] = feet3;
		for(int i = 96; i < 100; i++)
			options[i] = mythicalSword;
	}
	else if(level >= 7)
	{
		for(int i = 40; i < 48; i++)
			options[i] = head3;
		for(int i = 48; i < 56; i++)
			options[i] = chest3;
		for(int i = 56; i < 64; i++)
			options[i] = hands3;
		for(int i = 64; i < 72; i++)
			options[i] = feet3;
		for(int i = 72; i < 80; i++)
			options[i] = mythicalSword;
		for(int i = 80; i < 84; i++)
			options[i] = head4;
		for(int i = 84; i < 88; i++)
			options[i] = chest4;
		for(int i = 88; i < 92; i++)
			options[i] = hands4;
		for(int i = 92; i < 96; i++)
			options[i] = feet4;
		for(int i = 96; i < 100; i++)
			options[i] = hellbreakerSword;
	}
	
	for(int i = 0; i < totalChests; i++)
	{
		do{
			slots[0] = (int)Math.floor(Math.random()*20);
			slots[1] = (int)Math.floor(Math.random()*20);
			slots[2] = (int)Math.floor(Math.random()*20);
		}while(slots[0] == slots[1] || slots[0] == slots[2] || slots[1] == slots[2]);
		
		chestInventoryList[i * 20 + slots[0]] = options[(int)Math.floor(Math.random()*options.length)];
		chestInventoryList[i * 20 + slots[1]] = options[(int)Math.floor(Math.random()*options.length)];
		chestInventoryList[i * 20 + slots[2]] = options[(int)Math.floor(Math.random()*options.length)];
	}
	convertItems();
	/*chestInventoryList[40] = head2;
	chestInventoryList[41] = chest2;
	chestInventoryList[42] = hands2;
	chestInventoryList[43] = feet2;
	chestInventoryList[44] = head3;
	chestInventoryList[45] = chest3;
	chestInventoryList[46] = hands3;
	chestInventoryList[47] = feet3;
	chestInventoryList[48] = head4;
	chestInventoryList[49] = chest4;
	chestInventoryList[50] = hands4;
	chestInventoryList[51] = feet4;
	chestInventoryList[52] = head5;
	chestInventoryList[53] = chest5;
	chestInventoryList[54] = hands5;
	chestInventoryList[55] = feet5;
	inventoryList[0] = head1;
	inventoryList[1] = chest1;
	inventoryList[2] = hands1;
	inventoryList[3] = feet1;
	
	inventoryList[14] = commonSword;
	inventoryList[15] = fineSword;
	inventoryList[16] = superiorSword;
	inventoryList[17] = mythicalSword;
	inventoryList[18] = hellbreakerSword;
	
	inventoryList[6] = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	inventoryList[7] = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	inventoryList[8] = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	inventoryList[9] = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	inventoryList[10] = new Item("item","Key",keyLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	inventoryList[11] = new Item("item","Key",keyLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	inventoryList[12] = new Item("item","Key",keyLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	inventoryList[13] = new Item("item","Key",keyLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
	*/
}

public void setDifficulty(int d)
{
	this.difficulty = d;
}

public void setLevel(int l)
{
	this.level = l;
}

public void setLocation()
{
	//location
		ex = 188 + 200;
		ey = -262 + 450 - (25*36);
		xmod = 40; //40 is middle 
		ymod = 76; //40 is middle
		newLevel = true;
}

public void regeneration()
{
		new Thread()
		{
			@Override public void run()
			{
				while(true)
				{
					try
					{
						if(regenerate == true && gameFreeze == false)
							health++;
						if(health > 100)
							health = 100;
						Thread.sleep(500);
					} catch(InterruptedException e) {}
				}
			}
		}.start();
}

public void music()
{
		new Thread()
		{
			@Override public void run()
			{
				try 
				{
					int counter = 6840;
					while(musicPlay)
					{
						if(musicPlay == true)
						{
							if(counter == 6840)
							{
								counter = 0;
								Clip clip = AudioSystem.getClip();
								music[0] = clip;
								music[0].open(AudioSystem.getAudioInputStream(new File("src//Sounds//knight_guy_game_mix_1.wav")));
								music[0].addLineListener(new LineListener(){
								    public void update(LineEvent e){
								        if(e.getType() == LineEvent.Type.STOP){
								            e.getLine().close();
								        }
								    }
								});
								music[0].start();
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
			}
		}.start();
}

public void playSound(String file)
{
	try 
	{
		Clip clip = AudioSystem.getClip();
		clip.open(AudioSystem.getAudioInputStream(new File("src//Sounds//" + file + ".wav")));
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

public void displayEnemyDamage(int iterator)
{
	enemyList.get(iterator).setDamaged(true);
	new Thread()
	{
		@Override public void run()
		{
			for(int i = 0; i < 2; i++)
			{
				try
				{
					if(i == 1 && enemyList.size() > iterator)
					{
						enemyList.get(iterator).setDamaged(false);	
					}
					Thread.sleep(250);
				}
				catch (InterruptedException e) {}
			}
		}
	}.start();
}

public void initializeInventory()
{
	
	//fill inventory
	for(int i = 0; i < inventoryList.length; i++)
	{
		inventoryList[i] = empty;
	}
	for(int i = 0; i < chestInventoryList.length; i++)
	{
		chestInventoryList[i] = empty;
	}
	inventoryList[4] = brokenSword;
	inventoryList[5] = shield1;
}

public void initializeObjects()
{
	
	objects[37][40] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
	objects[40][43] = SPIKETRAP;
	objects[43][40] = SPIKETRAP;
	objects[40][37] = SPIKETRAP;
	objects[38][40] = PIT;
	objects[40][42] = PIT;
	objects[42][40] = PIT;
	objects[40][38] = new Object("checkpoint",0,checkpointOff,checkpointOn,0);
	objects[38][41] = CRATE;
	objects[41][42] = CRATE;
	objects[42][41] = CRATE;
	objects[41][38] = CRATE;
	objects[37][39] = CHEST;
	objects[39][43] = CHEST;
	objects[43][39] = CHEST;
	objects[39][37] = CHEST;
	objects[46][55] = WALL;
	objects[46][56] = WALL;
	objects[46][57] = WALL;
	objects[46][58] = WALL;
	objects[45][55] = VDOOR;
	objects[45][58] = WALL;
	objects[44][55] = WALL;
	objects[44][56] = WALL;
	objects[44][57] = HDOOR;
	objects[44][58] = WALL;
	objects[35][40] = DOWNSHOOTER;
	objects[45][40] = UPSHOOTER;
	objects[40][35] = RIGHTSHOOTER;
	objects[40][45] = LEFTSHOOTER;
	objects[70][40] = LEVELCHANGER;
	objects[76][40] = new Object("checkpoint",0,checkpointOff,checkpointOn,0);
	objects[47][55] = new Object("checkpoint",0,checkpointOff,checkpointOn,0);
	enemyList.add(new Enemy(36,40,"Zombie", "melee",30 + (20 * difficulty),25 + (5 * difficulty),0,blankNode,0,0,false,0,false,1));
	enemyList.add(new Enemy(36,39,"Warden", "melee",30 + (20 * difficulty),25 + (5 * difficulty),0,blankNode,0,0,false,0,false,2));
	enemyList.add(new Enemy(45,40,"Skeleton", "ranged",30 + (20 * difficulty),25 + (5 * difficulty),0,blankNode,0,0,false,0,false,3));
}

public void getTerrain()
{
	for(int row = 0; row < 82; row++)
	{
		for(int col = 0; col < 82; col++)
		{
			//if(moveLayout[row][col] == false || objects[row][col] != space)// || objects[row][col] != CHECKPOINT || (objects[row][col] != HDOOR && objects[row][col].getPhase() !> 0)) )
			//{
				//terrain[row][col] = 1;
			//}
			//else
			//{
				//terrain[row][col] = 0;
			//}
			
			if(moveLayout[row][col] == true && (objects[row][col] == space || objects[row][col].Type().equals("checkpoint") || objects[row][col].Type().equals("spikeTrap")))
			{
				terrain[row][col] = 0;
			}
			else
			{
				terrain[row][col] = 1;
			}
			if((objects[row][col].Type().equals("door") && objects[row][col].getPhase() != 0))
			{
				terrain[row][col] = 0;
			}
		}
	}
	for(int i = 0; i < enemyList.size(); i++)
	{
		if((enemyList.get(i).getRow() - 1 == ymod && enemyList.get(i).getCol() == xmod) || (enemyList.get(i).getRow() + 1 == ymod && enemyList.get(i).getCol() == xmod) || (enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() - 1 == xmod) || (enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() + 1 == xmod) || (enemyList.get(i).getName().equals("Skeleton") && enemyList.get(i).getHasLineOfSight() == true))
		{
			terrain[enemyList.get(i).getRow()][enemyList.get(i).getCol()] = 1;
		}
	}
}

public int calculateCost(int row, int col, int parentTotal)
{
	int cost = Math.abs(row - ymod) + Math.abs(col - xmod) + parentTotal;
	return cost;
}

public void playerDamage(int amount)
{
	health -= amount - armor;
	deathCheck();
	try 
	{
		Clip clip = AudioSystem.getClip();
		clip.open(AudioSystem.getAudioInputStream(new File("src//Sounds//playerHurt.wav")));
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
	new Thread()
	{
		@Override public void run()
		{
			if(health > 0)
			{
				damageOn = true;
				for(int i = 0; i < 15; i++)
				{
					try
					{
						if(i == 14)
						{
							damageOn = false;
						}
						Thread.sleep(10);
					}
					catch(InterruptedException e)
					{}
				}
			}
		}
	}.start();
}

public void enemyPathfind(Enemy enemy, int index)
{
	getTerrain();
	for(int i = 0; i < open.size(); i++)
	{
		open.remove(i);
		i--;
	}
	for(int i = 0; i < closed.size(); i++)
	{
		closed.remove(i);
		i--;
	}
	for(int i = 0; i < fastestPath.size(); i++)
	{
		fastestPath.remove(i);
		i--;
	}
	int lowest;
	int pathChecks = 0;
	boolean addAbove = true;
	boolean addRight = true;
	boolean addBelow = true;
	boolean addLeft = true;
	
	open.add(new Node(enemy.getRow(),enemy.getCol(),0,enemy.getRow(),enemy.getCol(),0));
	
	do
	{
		lowest = 0;
		for(int i = 0; i < open.size(); i++)
		{
			if(open.get(i).getCost() <= open.get(lowest).getCost())
			{
				lowest = i;
			}
		}
		if(open.size() > 0)
		{
			if(terrain[open.get(lowest).getRow() - 1][open.get(lowest).getCol()] == 0 && addAbove == true)
			{
				//above
				open.add(new Node(open.get(lowest).getRow() - 1,open.get(lowest).getCol(),calculateCost(open.get(lowest).getRow() - 1,open.get(lowest).getCol(),open.get(lowest).getParentTotal() + 1),open.get(lowest).getRow(),open.get(lowest).getCol(),open.get(lowest).getParentTotal() + 1));
			}
			if(terrain[open.get(lowest).getRow()][open.get(lowest).getCol() + 1] == 0 && addRight == true)
			{
				//right
				open.add(new Node(open.get(lowest).getRow(),open.get(lowest).getCol() + 1,calculateCost(open.get(lowest).getRow(),open.get(lowest).getCol() + 1,open.get(lowest).getParentTotal() + 1),open.get(lowest).getRow(),open.get(lowest).getCol(),open.get(lowest).getParentTotal() + 1));
			}
			if(terrain[open.get(lowest).getRow() + 1][open.get(lowest).getCol()] == 0 && addBelow == true)
			{
				//below
				open.add(new Node(open.get(lowest).getRow() + 1,open.get(lowest).getCol(),calculateCost(open.get(lowest).getRow() + 1,open.get(lowest).getCol(),open.get(lowest).getParentTotal() + 1),open.get(lowest).getRow(),open.get(lowest).getCol(),open.get(lowest).getParentTotal() + 1));	
			}
			if(terrain[open.get(lowest).getRow()][open.get(lowest).getCol() - 1] == 0 && addLeft == true)
			{
				//left
				open.add(new Node(open.get(lowest).getRow(),open.get(lowest).getCol() - 1,calculateCost(open.get(lowest).getRow(),open.get(lowest).getCol() - 1,open.get(lowest).getParentTotal() + 1),open.get(lowest).getRow(),open.get(lowest).getCol(),open.get(lowest).getParentTotal() + 1));
			}
		
			closed.add(open.get(lowest));
			for(int i = 0; i < closed.size(); i++)
			{
				for(int j = 0; j < open.size(); j++)
				{
					if(closed.get(i).getRow() == open.get(j).getRow() && closed.get(i).getCol() == open.get(j).getCol())
					{
						open.remove(j);
						j--;
					}
				}
			}
		}
		pathChecks++;
		addAbove = true;
		addBelow = true;
		addRight = true;
		addLeft = true;
		openlength = open.size();
		closedlength = closed.size();
	}
	while(pathChecks < 81 && !(closed.get(closed.size()-1).getRow() == ymod && closed.get(closed.size()-1).getCol() == xmod));//!((closed.get(closed.size() - 1).getRow() - 1 == ymod && closed.get(closed.size() - 1).getCol() == xmod) || (closed.get(closed.size() - 1).getRow() == ymod && closed.get(closed.size() - 1).getCol() + 1 == xmod) || (closed.get(closed.size() - 1).getRow() + 1 == ymod && closed.get(closed.size() - 1).getCol() == xmod) || (closed.get(closed.size() - 1).getRow() == ymod && closed.get(closed.size() - 1).getCol() - 1 == xmod) || pathChecks < 100));
	
	
	if(closed.get(closed.size()-1).getRow() == ymod && closed.get(closed.size()-1).getCol() == xmod)//(closed.get(closed.size() - 1).getRow() - 1 == ymod && closed.get(closed.size() - 1).getCol() == xmod) || (closed.get(closed.size() - 1).getRow() == ymod && closed.get(closed.size() - 1).getCol() + 1 == xmod) || (closed.get(closed.size() - 1).getRow() + 1 == ymod && closed.get(closed.size() - 1).getCol() == xmod) || (closed.get(closed.size() - 1).getRow() == ymod && closed.get(closed.size() - 1).getCol() - 1 == xmod))
	{
		fastestPath.add(closed.get(closed.size() - 1));
		//while(!(fastestPath.get(fastestPath.size() - 1).getRow() == enemy.getRow() && fastestPath.get(fastestPath.size() - 1).getCol() == enemy.getCol()))
		for(int k = 0; k < fastestPath.get(0).getParentTotal(); k++)
		{
			for(int i = 0; i < closed.size(); i++)
			{
				if(closed.get(i).getRow() == fastestPath.get(fastestPath.size() - 1).getParentRow() && closed.get(i).getCol() == fastestPath.get(fastestPath.size() - 1).getParentCol())
				{
					fastestPath.add(closed.get(i));
				}
			}
		}
		drawPanel.repaint();
	}
	if(health > 0 && fastestPath.size() >= 2)
	{
		enemyList.get(index).setTargetNode(fastestPath.get(fastestPath.size() - 2));
	}
	if(enemyList.size() > 0)
		enemyList.get(index).setHasLineOfSight(true);
	for(int i = fastestPath.size() - 1; i >= 0; i--)
	{
		if(fastestPath.get(fastestPath.size() - 1).getRow() == fastestPath.get(i).getRow())
		{}
		else
		{
			enemyList.get(index).setHasLineOfSight(false);
		}
	}
	if(enemyList.get(index).getHasLineOfSight() == false)
	{
		enemyList.get(index).setHasLineOfSight(true);
		for(int i = fastestPath.size() - 1; i >= 0; i--)
		{
			if(fastestPath.get(fastestPath.size() - 1).getCol() == fastestPath.get(i).getCol())
			{}
			else
			{
				enemyList.get(index).setHasLineOfSight(false);
			}
		}
	}
	
	if(pathChecks == 81)
		enemyList.get(index).setTargetNode(null);
}
   
public void enemyDamage(int enemyDirection, int index)
{
		new Thread()
	    {
	        @Override public void run()
	        {   	
	        	if(enemyList.get(index).getType().equals("melee"))
	        	{
	        		playSound("zombieAttack2");
		        	for(int i = 0; i < 5; i++)
		            {
		        		try
		                {
		        			if(enemyList.size() > 0)
		        			{
			        			if(enemyDirection == 0)
			        			{
			        				enemyList.get(index).setYShift(-10);
			        			}
			        			if(enemyDirection == 1)
			        			{
			        				enemyList.get(index).setXShift(10);
			        			}
			        			if(enemyDirection == 2)
			        			{
			        				enemyList.get(index).setYShift(10);
			        			}
			        			if(enemyDirection == 3)
			        			{
			        				enemyList.get(index).setXShift(-10);
			        			}
			        			if(i == 2 && gameFreeze == false && health > 0)
			        			{
			        				if(!(isBlocking == true && (direction == enemyDirection + 2 || direction == enemyDirection - 2)))
			        					playerDamage(enemyList.get(index).getDamage());	
			        			}
		        			}
		        			drawPanel.repaint();
		            	    Thread.sleep(50);
		                }
		                catch(InterruptedException e) {}
		            }
		        	enemyList.get(index).setXShift(0);
		        	enemyList.get(index).setYShift(0);
		        	drawPanel.repaint();
		        }
	        	else if(enemyList.get(index).getType().equals("ranged"))
	        	{
	        		playSound("bow");
	        		for(int i = 0; i < 3; i++)
		            {
		        		try
		                {
		        			if(enemyList.size() > 0)
		        			{
		        				enemyList.get(index).setXShift(i);
			        			if(i == 2 && gameFreeze == false && health > 0)
			        			{
			        				fireProjectile(enemyList.get(index).getRow(),enemyList.get(index).getCol(),enemyDirection);
			        			}
		        			}
		        			drawPanel.repaint();
		            	    Thread.sleep(100);
		                }
		                catch(InterruptedException e) {}
		            }
		        	enemyList.get(index).setXShift(0);  
		        	drawPanel.repaint();
	        	}
	        }
	    }.start();
	
}

public void fireProjectile(int row, int col, int direction)
{
	if(projectileCounter == 49)
	{
		projectileCounter = 0;
	}
	int thisProjectile = projectileCounter++;
	projectiles[thisProjectile] = new Projectile("Arrow",row,col,0,0,direction,false);
	new Thread()
    {
        @Override public void run()
        {
        	int arrowMod = 0;
        	int distance = 0;
        	do// && !(projectiles[thisProjectile].getRow() == ymod && projectiles[thisProjectile].getCol() == xmod))
            {
        		getTerrain();
	        		try
	                {
	        			//if(gameFreeze == false)
	            		//{
		        			if(arrowMod == 0)
		        			{
			        			if(direction == 0)
									projectiles[thisProjectile].moveUp();
								else if(direction == 1)
									projectiles[thisProjectile].moveRight();
								else if(direction == 2)
									projectiles[thisProjectile].moveDown();
								else if(direction == 3)
									projectiles[thisProjectile].moveLeft();
			        			
			        			arrowMod = 25;
			        			if(projectiles[thisProjectile].getRow() >= 0 && projectiles[thisProjectile].getCol() >= 0)
			        			{
				        			if((Math.abs(ymod-projectiles[thisProjectile].getRow()) < 12 && Math.abs(xmod-projectiles[thisProjectile].getCol()) < 12) && !(terrain[projectiles[thisProjectile].getRow()][projectiles[thisProjectile].getCol()] == 0 || objects[projectiles[thisProjectile].getRow()][projectiles[thisProjectile].getCol()].Type().equals("pit")))
				            			playSound("arrow2");
			        			}
		        			}
		        			if(direction == 0)
								projectiles[thisProjectile].setRowMod(arrowMod);
							else if(direction == 1)
								projectiles[thisProjectile].setColMod(-arrowMod);
							else if(direction == 2)
								projectiles[thisProjectile].setRowMod(-arrowMod);
							else if(direction == 3)
								projectiles[thisProjectile].setColMod(arrowMod);
		        			arrowMod--;
		        			distance++;
		        			drawPanel.repaint();
		            	    Thread.sleep(5);
	            		//}
	                }
	                catch(InterruptedException e) {}
        		if(projectiles[thisProjectile].getRow() < 0)
        			projectiles[thisProjectile].setRow(0);
        		if(projectiles[thisProjectile].getCol() < 0)
        			projectiles[thisProjectile].setCol(0);
            }
        	while(distance < 5000 && (terrain[projectiles[thisProjectile].getRow()][projectiles[thisProjectile].getCol()] == 0 || objects[projectiles[thisProjectile].getRow()][projectiles[thisProjectile].getCol()].Type().equals("pit")));    
        	projectiles[thisProjectile].setStuck(true);
        	//playSound("arrow2");
        }
    }.start();
}

public void enemyMove()
{
	new Thread()
    {
        @Override public void run()
        {   	
        	while(true)
            {
        		try
                {
        			if(gameFreeze == false && health > 0)
        			{
	        			for(int i = 0; i < enemyList.size(); i++)
	        			{
	        				enemyInEnemy = false;
		        				if(Math.abs(enemyList.get(i).getRow() - ymod) < 12 && Math.abs(enemyList.get(i).getCol() - xmod) < 12 && enemyList.get(i).getTargetNode() != null)
		        				{
			        				if(enemyList.get(i).getTargetNode().getRow() < enemyList.get(i).getRow())
			        				{
			        					if(enemyList.get(i).getDirection() == 0)
			        					{
			        						enemyList.get(i).setRow(enemyList.get(i).getRow() - 1);
			        						for(int j = 0; j < enemyList.size(); j++)
				        					{
				        						if(enemyList.get(i).getDirection() == 0 && j != i && enemyList.get(i).getRow() == enemyList.get(j).getRow() && enemyList.get(i).getCol() == enemyList.get(j).getCol())
				        						{
				        							enemyInEnemy = true;
				        						}
				        					}
				        					if(((objects[enemyList.get(i).getRow()][enemyList.get(i).getCol()].Type().equals("spikeTrap") && objects[enemyList.get(i).getRow()][enemyList.get(i).getCol()].getPhase() == 1) || (enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() == xmod) || enemyInEnemy == true || (enemyList.get(i).getType().equals("ranged") && enemyList.get(i).getHasLineOfSight() == true)) && enemyList.get(i).getDirection() == 0)
					        				{
				        						//if(enemyList.get(i).getDirection() == 0)
				        							enemyList.get(i).setRow(enemyList.get(i).getRow() + 1);
				        						//else
				        							//enemyList.get(i).setRow(enemyList.get(i).getRow() - 1);
				        						if(enemyList.get(i).getType().equals("melee"))
				        						{
						        					if((enemyList.get(i).getAttackNum() == 0 && enemyList.get(i).getRow() - 1 == ymod && enemyList.get(i).getCol() == xmod))
						        					{
						        						enemyDamage(enemyList.get(i).getDirection(),i);
						        						enemyList.get(i).setAttackNum(1);
						        					}
						        					else
						        					{
						        						enemyList.get(i).setAttackNum(0);
						        					}		
				        						}
				        						else if(enemyList.get(i).getType().equals("ranged"))
				        						{
				        							if(enemyList.get(i).getAttackNum() == 0 && enemyList.get(i).getType().equals("ranged") && enemyList.get(i).getHasLineOfSight() == true && enemyList.get(i).getDirection() == 0)
				        							{
						        						enemyDamage(enemyList.get(i).getDirection(),i);
						        						enemyList.get(i).setAttackNum(1);
						        					}
						        					else if(enemyList.get(i).getAttackNum() == 1)
						        					{
						        						enemyList.get(i).setAttackNum(2);
						        					}
						        					else
						        					{
						        						enemyList.get(i).setAttackNum(0);
						        					}
				        						}
					        				}
				        					else
				        					{
				        						playSound("enemyStep");
				        					}
			        					}
			        					else
			        					{
			        						enemyList.get(i).setDirection(0);
			        					}
			        				}else if(enemyList.get(i).getTargetNode().getRow() > enemyList.get(i).getRow())
			        				{
			        					if(enemyList.get(i).getDirection() == 2)
			        					{
			        						enemyList.get(i).setRow(enemyList.get(i).getRow() + 1);
			        						for(int j = 0; j < enemyList.size(); j++)
				        					{
				        						if(enemyList.get(i).getDirection() == 2 && j != i && enemyList.get(i).getRow() == enemyList.get(j).getRow() && enemyList.get(i).getCol() == enemyList.get(j).getCol())
				        						{
				        							enemyInEnemy = true;
				        						}
				        					}
				        					if(((objects[enemyList.get(i).getRow()][enemyList.get(i).getCol()].Type().equals("spikeTrap") && objects[enemyList.get(i).getRow()][enemyList.get(i).getCol()].getPhase() == 1) || (enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() == xmod) || enemyInEnemy == true || (enemyList.get(i).getType().equals("ranged") && enemyList.get(i).getHasLineOfSight() == true)) && enemyList.get(i).getDirection() == 2)
					        				{
				        						//if(enemyList.get(i).getDirection() == 2)
				        							enemyList.get(i).setRow(enemyList.get(i).getRow() - 1);
				        						//else
				        							//enemyList.get(i).setRow(enemyList.get(i).getRow() + 1);
					        					if(enemyList.get(i).getType().equals("melee"))
				        						{
						        					if((enemyList.get(i).getAttackNum() == 0 && enemyList.get(i).getRow() + 1 == ymod && enemyList.get(i).getCol() == xmod))
						        					{
						        						enemyDamage(enemyList.get(i).getDirection(),i);
						        						enemyList.get(i).setAttackNum(1);
						        					}
						        					else
						        					{
						        						enemyList.get(i).setAttackNum(0);
						        					}		
				        						}
				        						else if(enemyList.get(i).getType().equals("ranged"))
				        						{
				        							if(enemyList.get(i).getAttackNum() == 0 && enemyList.get(i).getType().equals("ranged") && enemyList.get(i).getHasLineOfSight() == true && enemyList.get(i).getDirection() == 2)
				        							{
						        						enemyDamage(enemyList.get(i).getDirection(),i);
						        						enemyList.get(i).setAttackNum(1);
						        					}
						        					else if(enemyList.get(i).getAttackNum() == 1)
						        					{
						        						enemyList.get(i).setAttackNum(2);
						        					}
						        					else
						        					{
						        						enemyList.get(i).setAttackNum(0);
						        					}
				        						}
					        				}
				        					else
				        					{
				        						playSound("enemyStep");
				        					}
			        					}
			        					else
			        					{
			        						enemyList.get(i).setDirection(2);
			        					}
			        				}else if(enemyList.get(i).getTargetNode().getCol() < enemyList.get(i).getCol())
			        				{
			        					if(enemyList.get(i).getDirection() == 3)
			        					{
			        						enemyList.get(i).setCol(enemyList.get(i).getCol() - 1);
			        						for(int j = 0; j < enemyList.size(); j++)
				        					{
				        						if(enemyList.get(i).getDirection() == 3 && j != i && enemyList.get(i).getRow() == enemyList.get(j).getRow() && enemyList.get(i).getCol() == enemyList.get(j).getCol())
				        						{
				        							enemyInEnemy = true;
				        						}
				        					}
				        					if(((objects[enemyList.get(i).getRow()][enemyList.get(i).getCol()].Type().equals("spikeTrap") && objects[enemyList.get(i).getRow()][enemyList.get(i).getCol()].getPhase() == 1) || (enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() == xmod) || enemyInEnemy == true || (enemyList.get(i).getType().equals("ranged") && enemyList.get(i).getHasLineOfSight() == true)) && enemyList.get(i).getDirection() == 3)
					        				{
				        						//if(enemyList.get(i).getDirection() == 3)
				        							enemyList.get(i).setCol(enemyList.get(i).getCol() + 1);
				        						//else
				        							//enemyList.get(i).setDirection(3);	
					        					if(enemyList.get(i).getType().equals("melee"))
				        						{
						        					if((enemyList.get(i).getAttackNum() == 0 && enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() - 1 == xmod))
						        					{
						        						enemyDamage(enemyList.get(i).getDirection(),i);
						        						enemyList.get(i).setAttackNum(1);
						        					}
						        					else
						        					{
						        						enemyList.get(i).setAttackNum(0);
						        					}		
				        						}
				        						else if(enemyList.get(i).getType().equals("ranged"))
				        						{
				        							if(enemyList.get(i).getAttackNum() == 0 && enemyList.get(i).getType().equals("ranged") && enemyList.get(i).getHasLineOfSight() == true && enemyList.get(i).getDirection() == 3)
				        							{
						        						enemyDamage(enemyList.get(i).getDirection(),i);
						        						enemyList.get(i).setAttackNum(1);
						        					}
						        					else if(enemyList.get(i).getAttackNum() == 1)
						        					{
						        						enemyList.get(i).setAttackNum(2);
						        					}
						        					else
						        					{
						        						enemyList.get(i).setAttackNum(0);
						        					}
				        						}
					        				}
				        					else
				        					{
				        						playSound("enemyStep");
				        					}
			        					}
			        					else
			        					{
			        						enemyList.get(i).setDirection(3);
			        					}
			        				}else if(enemyList.get(i).getTargetNode().getCol() > enemyList.get(i).getCol())
			        				{
			        					if(enemyList.get(i).getDirection() == 1)
			        					{
			        						enemyList.get(i).setCol(enemyList.get(i).getCol() + 1);
			        						for(int j = 0; j < enemyList.size(); j++)
				        					{
				        						if(enemyList.get(i).getDirection() == 1 && j != i && enemyList.get(i).getRow() == enemyList.get(j).getRow() && enemyList.get(i).getCol() == enemyList.get(j).getCol())
				        						{
				        							enemyInEnemy = true;
				        						}
				        					}
				        					if(((objects[enemyList.get(i).getRow()][enemyList.get(i).getCol()].Type().equals("spikeTrap") && objects[enemyList.get(i).getRow()][enemyList.get(i).getCol()].getPhase() == 1) || (enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() == xmod) || enemyInEnemy == true || (enemyList.get(i).getType().equals("ranged") && enemyList.get(i).getHasLineOfSight() == true)) && enemyList.get(i).getDirection() == 1)
					        				{
				        						//if(enemyList.get(i).getDirection() == 1)
				        							enemyList.get(i).setCol(enemyList.get(i).getCol() - 1);
				        						//else
				        							//enemyList.get(i).setDirection(1);
					        					if(enemyList.get(i).getType().equals("melee"))
				        						{
						        					if((enemyList.get(i).getAttackNum() == 0 && enemyList.get(i).getRow() == ymod && enemyList.get(i).getCol() + 1 == xmod))
						        					{
						        						enemyDamage(enemyList.get(i).getDirection(),i);
						        						enemyList.get(i).setAttackNum(1);
						        					}
						        					else
						        					{
						        						enemyList.get(i).setAttackNum(0);
						        					}		
				        						}
				        						else if(enemyList.get(i).getType().equals("ranged"))
				        						{
				        							if(enemyList.get(i).getAttackNum() == 0 && enemyList.get(i).getType().equals("ranged") && enemyList.get(i).getHasLineOfSight() == true && enemyList.get(i).getDirection() == 1)
				        							{
						        						enemyDamage(enemyList.get(i).getDirection(),i);
						        						enemyList.get(i).setAttackNum(1);
						        					}
						        					else if(enemyList.get(i).getAttackNum() == 1)
						        					{
						        						enemyList.get(i).setAttackNum(2);
						        					}
						        					else
						        					{
						        						enemyList.get(i).setAttackNum(0);
						        					}
				        						}		
					        				}
				        					else
				        					{
				        						playSound("enemyStep");
				        					}
			        					}
			        					else
			        					{
			        						enemyList.get(i).setDirection(1);
			        					}
			        				}
		        				}
	        				drawPanel.repaint();
	        			}
        			}
        			Thread.sleep(500);
                }
                catch(InterruptedException e) {}
            }

        }
    }.start();
}

public void checkpoint()
{
	checkpointData[0] = ex;
	checkpointData[1] = ey;
	checkpointData[2] = xmod;
	checkpointData[3] = ymod;
	savePlayerData();
}

public void objectTime()
{
	new Thread()
    {
        @Override public void run()
        {
        	boolean spikeNoisePlayed = false;
        	boolean bowNoisePlayed = false;
        	while(true)
            {
        		try
                {
        			if(gameFreeze == false)
        			{
	        			//spikes cycling
        				spikeNoisePlayed = false;
        				bowNoisePlayed = false;
	                    for(int row = 0; row < objects.length; row++)
	                    {
	                    	for(int col = 0; col < objects[0].length; col++)
	                    	{
	                    		if(objects[row][col].Type().equals("spikeTrap"))
	                    		{
	                    			if(Math.abs(ymod-row) < 12 && Math.abs(xmod-col) < 12)
	                    			{
	                    				if(!spikeNoisePlayed)
	                    				{
	                    					playSound("spikes");
	                    					spikeNoisePlayed = true;
	                    				}
	                    			}	
		                    		if(objects[row][col].getPhase() == 0)
		                    		{
		                    			objects[row][col].setPhase(1);
		                    		}
		                    		else
		                    		{
		                    			objects[row][col].setPhase(0);
		                    		}
	                    		}
	                    		if(objects[row][col].Type().equals("shooter"))
	                    		{
	                    			if(Math.abs(ymod-row) < 24 && Math.abs(xmod-col) < 24)
	                    			{
	                    				if(!bowNoisePlayed)
	                    				{
	                    					playSound("bow");
	                    					bowNoisePlayed = true;
	                    				}
	                    				fireProjectile(row,col,objects[row][col].getPhase());
	                    			}
	                    			//fireProjectile(row,col,objects[row][col].getPhase());
	                    		}
	                    	}
	                    }
	            	    drawPanel.repaint();
        			}
            	    Thread.sleep(2000);
                }
                catch(InterruptedException e) {}
            }

        }
    }.start();
}

public void gameTime()
{
	new Thread()
    {
        @Override public void run()
        {
        	while(true)
            {
        		try
                {
        			boolean pickupFail = false;
        			boolean stackFail = false;
        			if(gameFreeze == false)
        			{
        				if(lootbags.size() > 0)
        				{
	        				for(int i = 0; i < lootbags.size(); i++)
	        				{
	        					
	        					if(lootbags.get(i).getRow() == ymod && lootbags.get(i).getCol() == xmod)
	        					{
	        						for(int j = 0; j < lootbags.get(i).getItems().length; j++)
	        						{
	        							for(int g = 6; g < inventoryList.length; g++)
	        							{
	        								stackFail = false;
	        								if(inventoryList[g].Name().equals(lootbags.get(i).getItems()[j]))
	        								{
	        									playSound("loot");
	        									inventoryList[g].setStack(inventoryList[g].Stack() + 1);
	        									g = inventoryList.length;
	        								}
	        								else
	        								{
	        									stackFail = true;
	        								}
	        							}
	        							if(stackFail == true)
	        							{
	        								for(int g = 6; g < inventoryList.length; g++)
		        							{
		        								pickupFail = false;
		        								if(inventoryList[g] == empty)
			        							{
			        								playSound("loot");
			        								if(lootbags.get(i).getItems()[j].equals("Key"))
			        								{
			        									inventoryList[g] = new Item("item","Key",keyLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
			        									g = inventoryList.length;
			        								}
			        							}
		        								else
		        								{
		        									pickupFail = true;
		        								}
		        							}
	        							}
	        						}
	        						if(pickupFail == false)
	            					{
	            						lootbags.remove(i);
	            						i--;
	            					}
	        					}
	        					
	        				}
        				}
//        				if(lootbags.size() > 0)
//        				{
//	        				for(int i = 0; i < lootbags.size(); i++)
//	        				{
//	        					
//	        					if(lootbags.get(i).getRow() == ymod && lootbags.get(i).getCol() == xmod)
//	        					{
//	        						for(int j = 0; j < lootbags.get(i).getItems().length; j++)
//	        						{
//	        							
//	        							for(int g = 6; g < inventoryList.length; g++)
//	        							{
//	        								pickupfail = false;
//	        								if(inventoryList[g] == empty)
//	        								{
//	        									playSound("loot");
//	        									if(lootbags.get(i).getItems()[j].equals("Key"))
//	        									{
//	        										inventoryList[g] = new Item("item","Key",keyLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
//	        										g = inventoryList.length;
//	        									}
//	        								}
//	        								else if(inventoryList[g].Name().equals(lootbags.get(i).getItems()[j]))
//	        								{
//	        									playSound("loot");
//	        									inventoryList[g].setStack(inventoryList[g].Stack() + 1);
//	        									g = inventoryList.length;
//	        								}
//	        								else
//	        								{
//	        									pickupfail = true;
//	        								}
//	        							}
//	        						}
//	        						if(pickupfail == false)
//	            					{
//	            						lootbags.remove(i);
//	            						i--;
//	            					}
//	        					}
//	        					
//	        				}
//        				}
		        		if(objects[ymod][xmod].Type().equals("spikeTrap") && objects[ymod][xmod].getPhase() == 1)
		        		{
		        			if(cooldown > 75)
		        			{
			        			playerDamage(40+5*difficulty);
			        			cooldown = 0;
		        			}
		        		}
		        		else
		        		{
		        			cooldown = 75;
		        		}
		        		if(objects[ymod][xmod].Type().equals("pit"))
		        		{
		        			playerDamage(100 + armor);
		        		}
		        		if(objects[ymod][xmod].Type().equals("levelChanger") && objects[ymod][xmod].getPhase() == 0)
		        		{
		        			level++;
	                        newLevel = true;
	                        if(newLevel)
	                		{
	                			for(int i = 0; i < enemyList.size();i++)
	                			{
	                				enemyList.remove(i);
	                				i--;
	                			}
	                		}
	                        savePlayerData();
	                        objects[ymod][xmod].setPhase(1);
	                		EventQueue.invokeLater(new Runnable()
	                        {
	                            public void run()
	                            {
	                            	Core core = new Core(); 
	                            	core.setDifficulty(difficulty);
	                            	//core.setLevel(level);
	    	                        core.initializeInventory();
	    	                        if(gameMode == 0)
	    	                        	core.initializeObjects();
	    	                        int[] presets = new int[9];
	         						for(int i = 0; i < presets.length; i++)
	         						{
	         							presets[i] = (int)Math.floor(Math.random() * presetAmount);
	         						}
	    	                        core.generateLayout(gameMode,presets);
	    	                        core.loadPlayerData();
	    	                        core.setLevel(level);
	    	                        core.setLocation();
	    	                        core.objectTime();
	    	                        core.gameTime();
	    	                        core.enemyMove();
	    	                        core.populateChests();
	    	                        core.resetKillList();
	    	                        core.cullEnemies(); 
	    	                        core.resetAllPhases();
	                            }
	                        });
	                		newLevel = false;
	                		musicPlay = false;
	                		music[0].close();
	                        setVisible(false);
	                        dispose(); 
		        		}
		        		if(objects[ymod][xmod].Type().equals("checkpoint"))
		        			regenerate = true;
		        		else
		        			regenerate = false;
		            	if(objects[ymod][xmod].Type().equals("checkpoint") && objects[ymod][xmod].getPhase() == 0 && isMoving == false)
		        		{
		            		playSound("checkpoint");
		        			objects[ymod][xmod].setPhase(1);
		        			checkpoint();
		        		}
//		        		if(objects[ymod - 1][xmod].Type().equals("crate") && direction == 0 && isAttacking == true)
//		        			objects[ymod - 1][xmod] = space;
//		        		if(objects[ymod][xmod + 1].Type().equals("crate") && direction == 1 && isAttacking == true)
//		        			objects[ymod][xmod + 1] = space;
//		        		if(objects[ymod + 1][xmod].Type().equals("crate") && direction == 2 && isAttacking == true)
//		        			objects[ymod + 1][xmod] = space;
//		        		if(objects[ymod][xmod - 1].Type().equals("crate") && direction == 3 && isAttacking == true)
//		        			objects[ymod][xmod - 1] = space;
		        		
		        		if(objects[ymod - 1][xmod].Type().equals("chest") && direction == 0 && inChest == false && inMenu == false)
		        			fInteract = true;
		        		else if(objects[ymod][xmod + 1].Type().equals("chest") && direction == 1 && inChest == false && inMenu == false)
		        			fInteract = true;
		        		else if(objects[ymod + 1][xmod].Type().equals("chest") && direction == 2 && inChest == false && inMenu == false)
		        			fInteract = true;
		        		else if(objects[ymod][xmod - 1].Type().equals("chest") && direction == 3 && inChest == false && inMenu == false)
		        			fInteract = true;
		        		else
		        			fInteract = false;
        			}
	        		cooldown++;
	        		for(int i = 0; i < enemyList.size(); i++)
	    			{
	    				if((enemyList.get(i).getRow() - 1 != ymod && enemyList.get(i).getCol() != xmod) || (enemyList.get(i).getRow() + 1 != ymod && enemyList.get(i).getCol() != xmod) || (enemyList.get(i).getRow()!= ymod && enemyList.get(i).getCol() - 1 != xmod) || (enemyList.get(i).getRow() != ymod && enemyList.get(i).getCol() + 1 != xmod))
	    				{
	    					if(Math.abs(enemyList.get(i).getRow()-ymod) < 12 && Math.abs(enemyList.get(i).getCol()-xmod) < 12)
	    						enemyPathfind(enemyList.get(i),i);
	    				}
	    			}
	        		for(int i = 0; i < projectiles.length; i++)
	        		{
	        			if(projectiles[i].getRow() >= 0 && projectiles[i].getCol() >= 0)
	        			{
		        			if(projectiles[i].getRow() == ymod && projectiles[i].getCol() == xmod)
		        			{	        	
		        				if(!(((direction == 0 && projectiles[i].getDirection() == 2) || (direction == 1 && projectiles[i].getDirection() == 3) || (direction == 2 && projectiles[i].getDirection() == 0) || (direction == 3 && projectiles[i].getDirection() == 1)) && isBlocking == true))
		        					playerDamage(25 + 5*difficulty);
		        				projectiles[i] = new Projectile("empty",0,0,0,0,0,false);
		        			}
		        			if(objects[projectiles[i].getRow()][projectiles[i].getCol()] == space && projectiles[i].getStuck() == true)
		        			{
		        				projectiles[i] = new Projectile("empty",0,0,0,0,0,false);
		        			}	
	        			}
	        			else
	        			{
	        				projectiles[i] = new Projectile("empty",0,0,0,0,0,false);
	        			}
	        		}
	        		for(int i = 0; i < enemyList.size(); i++)
	        		{
	        			for(int j = 0; j < projectiles.length; j++)
	        			{
	        				if(enemyList.get(i).getRow() == projectiles[j].getRow() && enemyList.get(i).getCol() == projectiles[j].getCol())
	        				{
	        					projectiles[j] = new Projectile("empty",0,0,0,0,0,false);
	        				}
	        			}
	        		}
            	    drawPanel.repaint();
            	    Thread.sleep(10);
                }
                catch(InterruptedException e) {}
            }

        }
    }.start();
}

public void deathCheck()
{
	if(health <= 0 && gameFreeze == false)
	{
		lives--;
		gameFreeze = true;
		if(lives >= 0)
		{
			deathTimer = 3;
			drawPanel.repaint();
			new Thread()
		    {
		        @Override public void run()
		        {
		        	while(true)
		            {
		        		try
		                {	
	//		        		try
	//		        		{
	//		        			if(deathTimer == -1)
	//			        		{
	//		        				String file;
	//		        				if(gameMode == 0)
	//		        					file = "Player_Data.txt";
	//		        				else
	//		        					file = "Dungeon_Player_Data.txt";
	//		        				FileInputStream fi = new FileInputStream(new File(file));
	//		        				ObjectInputStream oi = new ObjectInputStream(fi);
	//		        				newGame = (boolean)oi.readObject();
	//		        				level = (int)oi.readObject();
	//		        				difficulty = (int)oi.readObject();
	//		        				ex = (int)oi.readObject();
	//		        				ey = (int)oi.readObject();
	//		        				xmod = (int)oi.readObject();
	//		        				ymod = (int)oi.readObject();
	//		        				ex = (int)oi.readObject();
	//		        				ey = (int)oi.readObject();
	//		        				xmod = (int)oi.readObject();
	//		        				ymod = (int)oi.readObject();
	//		        				health = 100;
	//		        				oi.close();
	//		        				fi.close();
	//		        				gameFreeze = false;
	//        						break;	
	//			        		}
	//		        		}
	//		        		catch (FileNotFoundException e) {}
	//		        		catch (IOException e) {}
	//		        		catch (ClassNotFoundException e) {}
			        		if(deathTimer == 0)
			        		{
			        			savePlayerData();
			        			int[] preset = new int[9];
		 						try
		 						{
		 							String file1;
		 							if(gameMode == 0)
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
	//	 						for(int i = 0; i < enemyList.size(); i++)
	//	 						{
	//	 							enemyList.remove(i);
	//	 							i--;
	//	 						}
		 						setDifficulty(difficulty);
		 						setLevel(level);
		 						generateLayout(gameMode,preset);
		 						loadPlayerData();	
		 						setDifficulty(difficulty);
		 						cullEnemies();
		 						//resetPhases();
		 						health = 100;
		 						gameFreeze = false;
		 						break;
			        		}
		        			Thread.sleep(1000);
		                }
		                catch(InterruptedException e) {}
		        		deathTimer--;        			
		            }
		        }
		    }.start();
		}
		else
		{
			gameOver = true;
		}
	}
	drawPanel.repaint();
}

public void resetKillList()
{
	for(int i = 0; i < enemyKillList.size(); i++)
	{
		enemyKillList.remove(i);
		i--;
	}
}

public void enemyDeathCheck(int index)
{
	if(enemyList.get(index).getHealth() <= 0)
	{
		if(enemyList.get(index).getName().equals("Warden"))
		{
			String[] items = {"Key"};
			lootbags.add(new Lootbag(enemyList.get(index).getRow(),enemyList.get(index).getCol(),items));
			enemyKillList.add(enemyList.get(index).getId());
		}
		enemyList.remove(index);
	}
}

public void savePlayerData()
{
	try
	{
		String file;
		if(gameMode == 0)
			file = "Player_Data.txt";
		else
			file = "Dungeon_Player_Data.txt";
		FileOutputStream f = new FileOutputStream(new File(file));
		ObjectOutputStream o = new ObjectOutputStream(f);
		o.writeObject(newGame);
		o.writeObject(level);
		o.writeObject(difficulty);
		o.writeObject(checkpointData[0]);
		o.writeObject(checkpointData[1]);
		o.writeObject(checkpointData[2]);
		o.writeObject(checkpointData[3]);
		o.writeObject(checkpointData[0]);
		o.writeObject(checkpointData[1]);
		o.writeObject(checkpointData[2]);
		o.writeObject(checkpointData[3]);
		o.writeObject(lives);
		//o.writeObject(direction);
		//o.writeObject(health);
		for(int i = 0; i < inventoryList.length; i++)
		{
			o.writeObject(inventoryList[i].Name());
			if(inventoryList[i].Type().equals("item"))
			{
				o.writeObject(inventoryList[i].Stack());
			}
		}
		for(int i = 0; i < chestInventoryList.length; i++)
		{
			o.writeObject(chestInventoryList[i].Name());
			if(chestInventoryList[i].Type().equals("item"))
			{
				o.writeObject(chestInventoryList[i].Stack());
			}
		}
		for(int row = 0; row < objects.length; row++)
		{
			for(int col = 0; col < objects[0].length; col++)
			{
				o.writeObject(objects[row][col].getPhase());
			}
		}
		o.writeObject(enemyKillList.size());
		for(int i = 0; i < enemyKillList.size(); i++)
		{
			o.writeObject(enemyKillList.get(i));
		}
		//o.writeObject(69);
		o.close();
		f.close();
	}
	catch (IOException e) {}
	new Thread()
    {
        @Override public void run()
        {
        	isSaving = true;
            for(int i = 0; i < 10; i++)
            {
                try
                {
        	        drawPanel.repaint();
        	        Thread.sleep(100);
               	}
               	catch(InterruptedException e) {}
            }
        	isSaving = false;
        	drawPanel.repaint();
        }
    }.start();
}

public void loadPlayerData()
{
	try
	{
		String file;
		if(gameMode == 0)
			file = "Player_Data.txt";
		else
			file = "Dungeon_Player_Data.txt";
		FileInputStream fi = new FileInputStream(new File(file));
		ObjectInputStream oi = new ObjectInputStream(fi);
		newGame = (boolean)oi.readObject();
		level = (int)oi.readObject();
		difficulty = (int)oi.readObject();
		ex = (int)oi.readObject();
		ey = (int)oi.readObject();
		xmod = (int)oi.readObject();
		ymod = (int)oi.readObject();
		checkpointData[0] = (int)oi.readObject();
		checkpointData[1] = (int)oi.readObject();
		checkpointData[2] = (int)oi.readObject();
		checkpointData[3] = (int)oi.readObject();
		lives = (int)oi.readObject();
		//direction = (int)oi.readObject();
		//health = (int)oi.readObject();
		for(int i = 0; i < inventoryList.length + stacks; i++)
		{
			savedNames[i] = (String) oi.readObject();
			if(savedNames[i].equals("Common Helmet")) {
				inventoryList[i+ stacks] = head2;
			}else if(savedNames[i].equals("Common Chestplate")) {
				inventoryList[i+ stacks] = chest2;
			}else if(savedNames[i].equals("Common Gloves")) {
				inventoryList[i+ stacks] = hands2;
			}else if(savedNames[i].equals("Common Boots")) {
				inventoryList[i+ stacks] = feet2;
			}else if(savedNames[i].equals("Broken Sword")) {
				inventoryList[i+ stacks] = new Item("weapon","Broken Sword",swordLogo1,swordImage11,swordImage12,swordImage13,swordImage14,emptyImage,emptyImage,emptyImage,emptyImage,0, 5,1);
			}else if(savedNames[i].equals("Common Sword")) {
				inventoryList[i+ stacks] = new Item("weapon","Common Sword",swordLogo2,swordImage21,swordImage22,swordImage23,swordImage24,emptyImage,emptyImage,emptyImage,emptyImage,0, 7,1);
			}else if(savedNames[i].equals("Fine Sword")) {
				inventoryList[i+ stacks] = new Item("weapon","Fine Sword",swordLogo0,swordImage01,swordImage02,swordImage03,swordImage04,emptyImage,emptyImage,emptyImage,emptyImage,0, 9,1);
			}else if(savedNames[i].equals("Superior Sword")) {
				inventoryList[i+ stacks] = new Item("weapon","Superior Sword",swordLogo3,swordImage31,swordImage32,swordImage33,swordImage34,emptyImage,emptyImage,emptyImage,emptyImage,0, 11,1);
			}else if(savedNames[i].equals("Mythical Sword")) {
				inventoryList[i+ stacks] = new Item("weapon","Mythical Sword",swordLogo4,swordImage41,swordImage42,swordImage43,swordImage44,emptyImage,emptyImage,emptyImage,emptyImage,0, 13,1);
			}else if(savedNames[i].equals("Hellbreaker Sword")) {
				inventoryList[i+ stacks] = new Item("weapon","Hellbreaker Sword",swordLogo5,swordImage51,swordImage52,swordImage53,swordImage54,emptyImage,emptyImage,emptyImage,emptyImage,0, 15,1);
			}else if(savedNames[i].equals("Common Shield")) {
				inventoryList[i+ stacks] = new Item("shield","Common Shield",shieldLogo,shieldImage1a,shieldImage2a,shieldImage3a,shieldImage4a,shieldImage1b,shieldImage2b,shieldImage3b,shieldImage4b, 3,0,1);
			}else if(savedNames[i].equals("Health Potion")) {
				stackValue = (int)oi.readObject();
				inventoryList[i+ stacks] = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,stackValue);
				if(stackValue > 1){  
					stacks++;
				}
			}else if(savedNames[i].equals("Key")) {
				stackValue = (int)oi.readObject();
				inventoryList[i+ stacks] = new Item("item","Key",keyLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,stackValue);
				if(stackValue > 1){  
					stacks++;
				}
			}else if(savedNames[i].equals("Fine Helmet")) {
				inventoryList[i+ stacks] = head1;
			}else if(savedNames[i].equals("Fine Chestplate")) {
				inventoryList[i+ stacks] = chest1;
			}else if(savedNames[i].equals("Fine Gloves")) {
				inventoryList[i+ stacks] = hands1;
			}else if(savedNames[i].equals("Fine Boots")) {
				inventoryList[i+ stacks] = feet1;
			}else if(savedNames[i].equals("Mythical Helmet")) {
				inventoryList[i+ stacks] = head3;
			}else if(savedNames[i].equals("Mythical Chestplate")) {
				inventoryList[i+ stacks] = chest3;
			}else if(savedNames[i].equals("Mythical Gloves")) {
				inventoryList[i+ stacks] = hands3;
			}else if(savedNames[i].equals("Mythical Boots")) {
				inventoryList[i+ stacks] = feet3;
			}else if(savedNames[i].equals("Hellbreaker Helmet")) {
				inventoryList[i+ stacks] = head4;
			}else if(savedNames[i].equals("Hellbreaker Chestplate")) {
				inventoryList[i+ stacks] = chest4;
			}else if(savedNames[i].equals("Hellbreaker Gloves")) {
				inventoryList[i+ stacks] = hands4;
			}else if(savedNames[i].equals("Hellbreaker Boots")) {
				inventoryList[i+ stacks] = feet4;
			}else if(savedNames[i].equals("Superior Helmet")) {
				inventoryList[i+ stacks] = head5;
			}else if(savedNames[i].equals("Superior Chestplate")) {
				inventoryList[i+ stacks] = chest5;
			}else if(savedNames[i].equals("Superior Gloves")) {
				inventoryList[i+ stacks] = hands5;
			}else if(savedNames[i].equals("Superior Boots")) {
				inventoryList[i+ stacks] = feet5;
			}
			else if(savedNames[i].equals("empty")) {
				inventoryList[i+ stacks] = empty;
			}
			stackValue = 0;
			stacks = 0;
		}
		for(int i = 0; i < chestInventoryList.length + stacks; i++)
		{
			savedNames[i] = (String) oi.readObject();
			if(savedNames[i].equals("Common Helmet")) {
				chestInventoryList[i+ stacks] = head2;
			}else if(savedNames[i].equals("Common Chestplate")) {
				chestInventoryList[i+ stacks] = chest2;
			}else if(savedNames[i].equals("Common Gloves")) {
				chestInventoryList[i+ stacks] = hands2;
			}else if(savedNames[i].equals("Common Boots")) {
				chestInventoryList[i+ stacks] = feet2;
			}else if(savedNames[i].equals("Broken Sword")) {
				chestInventoryList[i+ stacks] = new Item("weapon","Broken Sword",swordLogo1,swordImage11,swordImage12,swordImage13,swordImage14,emptyImage,emptyImage,emptyImage,emptyImage,0, 5,1);
			}else if(savedNames[i].equals("Common Sword")) {
				chestInventoryList[i+ stacks] = new Item("weapon","Common Sword",swordLogo2,swordImage21,swordImage22,swordImage23,swordImage24,emptyImage,emptyImage,emptyImage,emptyImage,0, 7,1);
			}else if(savedNames[i].equals("Fine Sword")) {
				chestInventoryList[i+ stacks] = new Item("weapon","Fine Sword",swordLogo0,swordImage01,swordImage02,swordImage03,swordImage04,emptyImage,emptyImage,emptyImage,emptyImage,0, 9,1);
			}else if(savedNames[i].equals("Superior Sword")) {
				chestInventoryList[i+ stacks] = new Item("weapon","Superior Sword",swordLogo3,swordImage31,swordImage32,swordImage33,swordImage34,emptyImage,emptyImage,emptyImage,emptyImage,0, 11,1);
			}else if(savedNames[i].equals("Mythical Sword")) {
				chestInventoryList[i+ stacks] = new Item("weapon","Mythical Sword",swordLogo4,swordImage41,swordImage42,swordImage43,swordImage44,emptyImage,emptyImage,emptyImage,emptyImage,0, 13,1);
			}else if(savedNames[i].equals("Hellbreaker Sword")) {
				chestInventoryList[i+ stacks] = new Item("weapon","Hellbreaker Sword",swordLogo5,swordImage51,swordImage52,swordImage53,swordImage54,emptyImage,emptyImage,emptyImage,emptyImage,0, 15,1);
			}else if(savedNames[i].equals("Common Shield")) {
				chestInventoryList[i+ stacks] = new Item("shield","Common Shield",shieldLogo,shieldImage1a,shieldImage2a,shieldImage3a,shieldImage4a,shieldImage1b,shieldImage2b,shieldImage3b,shieldImage4b, 3,0,1);
			}else if(savedNames[i].equals("Health Potion")) {
				stackValue = (int)oi.readObject();
				chestInventoryList[i+ stacks] = new Item("item","Health Potion",potionLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,stackValue);
				if(stackValue > 1){  
					stacks++;
				}
			}else if(savedNames[i].equals("Key")) {
				stackValue = (int)oi.readObject();
				chestInventoryList[i+ stacks] = new Item("item","Key",keyLogo,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,stackValue);
				if(stackValue > 1){  
					stacks++;
				}
			}else if(savedNames[i].equals("empty")){
				chestInventoryList[i+ stacks] = new Item("","empty",emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,emptyImage,0,0,1);
			}else if(savedNames[i].equals("Fine Helmet")) {
				chestInventoryList[i+ stacks] = head1;
			}else if(savedNames[i].equals("Fine Chestplate")) {
				chestInventoryList[i+ stacks] = chest1;
			}else if(savedNames[i].equals("Fine Gloves")) {
				chestInventoryList[i+ stacks] = hands1;
			}else if(savedNames[i].equals("Fine Boots")) {
				chestInventoryList[i+ stacks] = feet1;
			}else if(savedNames[i].equals("Mythical Helmet")) {
				chestInventoryList[i+ stacks] = head3;
			}else if(savedNames[i].equals("Mythical Chestplate")) {
				chestInventoryList[i+ stacks] = chest3;
			}else if(savedNames[i].equals("Mythical Gloves")) {
				chestInventoryList[i+ stacks] = hands3;
			}else if(savedNames[i].equals("Mythical Boots")) {
				chestInventoryList[i+ stacks] = feet3;
			}else if(savedNames[i].equals("Hellbreaker Helmet")) {
				chestInventoryList[i+ stacks] = head4;
			}else if(savedNames[i].equals("Hellbreaker Chestplate")) {
				chestInventoryList[i+ stacks] = chest4;
			}else if(savedNames[i].equals("Hellbreaker Gloves")) {
				chestInventoryList[i+ stacks] = hands4;
			}else if(savedNames[i].equals("Hellbreaker Boots")) {
				chestInventoryList[i+ stacks] = feet4;
			}else if(savedNames[i].equals("Superior Helmet")) {
				chestInventoryList[i+ stacks] = head5;
			}else if(savedNames[i].equals("Superior Chestplate")) {
				chestInventoryList[i+ stacks] = chest5;
			}else if(savedNames[i].equals("Superior Gloves")) {
				chestInventoryList[i+ stacks] = hands5;
			}else if(savedNames[i].equals("Superior Boots")) {
				chestInventoryList[i+ stacks] = feet5;
			}
			stackValue = 0;
			stacks = 0;
		}
		for(int row = 0; row < objects.length; row++)
		{
			for(int col = 0; col < objects[0].length; col++)
			{
				objects[row][col].setPhase((int) oi.readObject());
			}
		}
		int killSize = (int) oi.readObject();
		for(int i = 0; i <killSize; i++)
		{
			enemyKillList.add((Integer) oi.readObject());
		}
		oi.close();
		fi.close();
	}
	catch (FileNotFoundException e) {}
	catch (IOException e) {}
	catch (ClassNotFoundException e) {}
}

public void generateLayout(int gameType, int[] presets)
{
	gameMode = gameType;
	for(int i = 0; i < enemyList.size(); i++)
	{
		enemyList.remove(i);
		i--;
	}
//	Object SPIKETRAP = new Object("spikeTrap",0,spikesOff,spikesOn);
//	Object PIT = new Object("pit",0,pit,emptyImage);
//	Object CRATE = new Object("crate",0,crate,emptyImage);
//	Object CHEST = new Object("chest",0,chest,emptyImage);
//	Object WALL = new Object("wall",0,wallImage,emptyImage);
//	Object CHECKPOINT = new Object("checkpoint",0,checkpointOff,checkpointOn);
//	Object LEVELCHANGER = new Object("levelChanger",0,levelChanger,emptyImage);
//	Object VDOOR = new Object("door",0,vdoor,hdoor);
//	Object HDOOR = new Object("door",0,hdoor,vdoor);
//	Object UPSHOOTER = new Object("shooter",0,shooter1,shooter1);
//	Object RIGHTSHOOTER = new Object("shooter",1,shooter2,shooter2);
//	Object DOWNSHOOTER = new Object("shooter",2,shooter3,shooter3);
//	Object LEFTSHOOTER = new Object("shooter",3,shooter4,shooter4);
//	Object ZOMBIE = new Object("zombie",0,emptyImage,emptyImage);
//	Object WARDEN = new Object("warden",0,emptyImage,emptyImage);
//	Object SKELETON = new Object("skeleton",0,emptyImage,emptyImage);
	if(gameType == 0)
	{
		if(level == 1)
		{
			for(int row = 0; row < mapLayout.length; row++)
			{
				for(int col = 0; col < mapLayout[0].length; col++)
				{
					mapLayout[row][col] = "room";
				}
			}
		}
		
	}else if(gameType == 1)
	{
		xsect = 0;
		ysect = 0;
		if(presets[0] == 0)//top left
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "vertical path";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,1));
			
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		else if(presets[0] == 1)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "right down turn";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,4));
			
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		else if(presets[0] == 2)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "right down turn";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "horizontal path";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,7));
			
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		xsect = 1;
		ysect = 0;
		if(presets[1] == 0)//top middle
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			//enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,10));
			//objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		else if(presets[1] == 1)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "left down turn";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "left up turn";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "right down turn";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,13));
			objects[15 + 27 * ysect][13 + 27 * xsect] = CHEST;
		}
		else if(presets[1] == 2)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "left up turn";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,16));
			objects[15 + 27 * ysect][13 + 27 * xsect] = CHEST;
		}
		xsect = 2;
		ysect = 0;
		if(presets[2] == 0)//top right
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "vertical path";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,19));
			
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		else if(presets[2] == 1)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "left down turn";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,22));
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		else if(presets[2] == 2)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "horizontal path";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,25));
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		xsect = 0;
		ysect = 1;
		if(presets[3] == 0)//middle left
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,28));
			objects[15 + 27 * ysect][13 + 27 * xsect] = CHEST;
		}
		else if(presets[3] == 1)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "left up turn";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "right down turn";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,31));
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		else if(presets[3] == 2)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "right up turn";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,34));
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		xsect = 1;
		ysect = 1;
		if(presets[4] == 0)//middle middle
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "left up turn";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "vertical path";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			//enemyList.add(new Enemy(13 + 27 * ysect,19 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,37));
			//enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,38));

			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		else if(presets[4] == 1)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "vertical path";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "vertical path";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,40));
			objects[15 + 27 * ysect][13 + 27 * xsect] = CHEST;
		}
		else if(presets[4] == 2)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,43));
			objects[15 + 27 * ysect][13 + 27 * xsect] = CHEST;
		}
		xsect = 2;
		ysect = 1;
		if(presets[5] == 0)//middle right
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,46));
			objects[15 + 27 * ysect][13 + 27 * xsect] = CHEST;
		}
		else if(presets[5] == 1)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "left up turn";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "right down turn";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,49));
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		else if(presets[5] == 2)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "right up turn";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,52));
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		xsect = 0;
		ysect = 2;
		if(presets[6] == 0)//bottom left
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "vertical path";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			
			enemyList.add(new Enemy(4 + 27 * ysect,7 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,55));
			enemyList.add(new Enemy(22 + 27 * ysect,13 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,52));
			enemyList.add(new Enemy(10 + 27 * ysect,10 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,58));

			objects[7 + 27 * ysect][13 + 27 * xsect] = CHEST;
			objects[4 + 27 * ysect][9 + 27 * xsect] = new Object("door",0,vdoor,hdoor,0);
			objects[7 + 27 * ysect][3 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[7 + 27 * ysect][4 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[7 + 27 * ysect][5 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][3 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][4 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][5 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[11 + 27 * ysect][3 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[11 + 27 * ysect][4 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[11 + 27 * ysect][5 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[14 + 27 * ysect][3 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[14 + 27 * ysect][4 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[14 + 27 * ysect][5 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[15 + 27 * ysect][3 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[15 + 27 * ysect][4 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[15 + 27 * ysect][5 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[18 + 27 * ysect][3 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[18 + 27 * ysect][4 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[18 + 27 * ysect][5 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[20 + 27 * ysect][3 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[20 + 27 * ysect][4 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[20 + 27 * ysect][5 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][12 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[12 + 27 * ysect][12 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[14 + 27 * ysect][12 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[16 + 27 * ysect][12 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][14 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[12 + 27 * ysect][14 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[14 + 27 * ysect][14 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[16 + 27 * ysect][14 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][16 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[12 + 27 * ysect][16 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[14 + 27 * ysect][16 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[16 + 27 * ysect][16 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][18 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[12 + 27 * ysect][18 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[14 + 27 * ysect][18 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[16 + 27 * ysect][18 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[23 + 27 * ysect][3 + 27 * xsect] = UPSHOOTER;
			objects[23 + 27 * ysect][4 + 27 * xsect] = UPSHOOTER;
			objects[23 + 27 * ysect][5 + 27 * xsect] = UPSHOOTER;
			objects[16 + 27 * ysect][11 + 27 * xsect] = CRATE;
			objects[10 + 27 * ysect][13 + 27 * xsect] = CRATE;
			objects[16 + 27 * ysect][15 + 27 * xsect] = CRATE;
			objects[10 + 27 * ysect][17 + 27 * xsect] = CRATE;
			objects[16 + 27 * ysect][19 + 27 * xsect] = CRATE;
			objects[21 + 27 * ysect][20 + 27 * xsect] = CRATE;
			objects[22 + 27 * ysect][20 + 27 * xsect] = CRATE;
			objects[23 + 27 * ysect][20 + 27 * xsect] = CRATE;
			objects[6 + 27 * ysect][3 + 27 * xsect] = CRATE;
			objects[6 + 27 * ysect][4 + 27 * xsect] = CRATE;
			objects[6 + 27 * ysect][5 + 27 * xsect] = CRATE;
			objects[11 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][25 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][24 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][23 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][22 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][21 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][20 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][20 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][24 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][25 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[1 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[2 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[3 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[5 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[6 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[7 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[7 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[6 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[5 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[4 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[3 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[2 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[1 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][19 + 27 * xsect] = WALL;
			
		}
		else if(presets[6] == 1)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "right up turn";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,58));
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		else if(presets[6] == 2)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "right up turn";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "horizontal path";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,61));
			objects[13 + 27 * ysect][15 + 27 * xsect] = CHEST;
		}
		xsect = 1;
		ysect = 2;
		if(presets[7] == 0)//bottom middle
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "left up turn";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "vertical path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			
			enemyList.add(new Enemy(4 + 27 * ysect,13 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,64));
			enemyList.add(new Enemy(13 + 27 * ysect,22 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,65));
			enemyList.add(new Enemy(13 + 27 * ysect,4 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,66));

			objects[1 + 27 * ysect][13 + 27 * xsect] = CHEST;
			objects[3 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[4 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[5 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[3 + 27 * ysect][18 + 27 * xsect] = CRATE;
			objects[4 + 27 * ysect][18 + 27 * xsect] = CRATE;
			objects[5 + 27 * ysect][18 + 27 * xsect] = CRATE;
			//SPIKETRAP = new Object("spikeTrap",0,spikesOff,spikesOn);
			objects[1 + 27 * ysect][11 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[2 + 27 * ysect][11 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[3 + 27 * ysect][11 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[3 + 27 * ysect][12 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[3 + 27 * ysect][13 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[3 + 27 * ysect][14 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[3 + 27 * ysect][15 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[2 + 27 * ysect][15 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[1 + 27 * ysect][15 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[2 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[1 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[2 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[1 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[6 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[7 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[7 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[6 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[19 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[20 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[21 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[22 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[23 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[24 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[25 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[25 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[24 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[23 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[22 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[21 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[20 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[19 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][15 + 27 * xsect] = WALL;
		}
		else if(presets[7] == 1)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			
			enemyList.add(new Enemy(4 + 27 * ysect,4 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,67));
			enemyList.add(new Enemy(4 + 27 * ysect,22 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,68));
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,69));
			
			objects[4 + 27 * ysect][13 + 27 * xsect] = CHEST;
			objects[9 + 27 * ysect][4 + 27 * xsect] = new Object("door",0,hdoor,vdoor,0);
			objects[9 + 27 * ysect][13 + 27 * xsect] = new Object("door",0,hdoor,vdoor,0);
			objects[9 + 27 * ysect][22 + 27 * xsect] = new Object("door",0,hdoor,vdoor,0);
			objects[13 + 27 * ysect][8 + 27 * xsect] = new Object("door",0,vdoor,hdoor,0);
			objects[13 + 27 * ysect][18 + 27 * xsect] = new Object("door",0,vdoor,hdoor,0);
			objects[10 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[2 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[1 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[2 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[1 + 27 * ysect][17 + 27 * xsect] = WALL;
			
			objects[6 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[7 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[6 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[7 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][1 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][2 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][3 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][5 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][6 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][7 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][20 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][21 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][23 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][24 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][25 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][1 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][2 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][3 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][4 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][5 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][6 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][7 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][20 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][21 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][22 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][23 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][24 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][25 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[19 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[20 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[21 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[22 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[23 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[24 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[25 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[19 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[20 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[21 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[22 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[23 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[24 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[25 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][16 + 27 * xsect] = WALL;
		}
		else if(presets[7] == 2)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
		
			enemyList.add(new Enemy(13 + 27 * ysect,4 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2*(level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,73));
			enemyList.add(new Enemy(14 + 27 * ysect,13 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,67));
			enemyList.add(new Enemy(4 + 27 * ysect,22 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,70));
			
			objects[12 + 27 * ysect][9 + 27 * xsect] = CHEST;
			objects[14 + 27 * ysect][9 + 27 * xsect] = CHEST;
			//SPIKETRAP = new Object("spikeTrap",0,spikesOff,spikesOn);
			objects[3 + 27 * ysect][8 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[3 + 27 * ysect][10 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[3 + 27 * ysect][12 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[3 + 27 * ysect][14 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[3 + 27 * ysect][16 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[3 + 27 * ysect][18 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[4 + 27 * ysect][9 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[4 + 27 * ysect][11 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[4 + 27 * ysect][13 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[4 + 27 * ysect][15 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[4 + 27 * ysect][17 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[5 + 27 * ysect][8 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[5 + 27 * ysect][10 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[5 + 27 * ysect][12 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[5 + 27 * ysect][14 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[5 + 27 * ysect][16 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[5 + 27 * ysect][18 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[9 + 27 * ysect][3 + 27 * xsect] = CRATE;
			objects[9 + 27 * ysect][4 + 27 * xsect] = CRATE;
			objects[9 + 27 * ysect][5 + 27 * xsect] = CRATE;
			objects[12 + 27 * ysect][10 + 27 * xsect] = PIT;
			objects[12 + 27 * ysect][11 + 27 * xsect] = PIT;
			objects[12 + 27 * ysect][12 + 27 * xsect] = PIT;
			objects[12 + 27 * ysect][13 + 27 * xsect] = PIT;
			objects[12 + 27 * ysect][14 + 27 * xsect] = PIT;
			objects[12 + 27 * ysect][15 + 27 * xsect] = PIT;
			objects[12 + 27 * ysect][16 + 27 * xsect] = PIT;
			objects[12 + 27 * ysect][17 + 27 * xsect] = PIT;
			objects[14 + 27 * ysect][10 + 27 * xsect] = PIT;
			objects[14 + 27 * ysect][11 + 27 * xsect] = PIT;
			objects[14 + 27 * ysect][12 + 27 * xsect] = PIT;
			objects[14 + 27 * ysect][13 + 27 * xsect] = PIT;
			objects[14 + 27 * ysect][14 + 27 * xsect] = PIT;
			objects[14 + 27 * ysect][15 + 27 * xsect] = PIT;
			objects[14 + 27 * ysect][16 + 27 * xsect] = PIT;
			objects[14 + 27 * ysect][17 + 27 * xsect] = PIT;
			objects[10 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][1 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][2 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][6 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][7 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][1 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][2 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][6 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][7 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[20 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[19 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[18 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[19 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[20 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[21 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[22 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[23 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[24 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[25 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[26 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[25 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[24 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][20 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][24 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][25 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][19 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][20 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][21 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][22 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][23 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][24 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][25 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][26 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][18 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][8 + 27 * xsect] = new Object("door",0,vdoor,hdoor,0);
		}
		xsect = 2;
		ysect = 2;
		if(presets[8] == 0)//bottom right
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "vertical path";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			
			enemyList.add(new Enemy(11 + 27 * ysect,13 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2*(level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,73));
			enemyList.add(new Enemy(15 + 27 * ysect,13 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2*(level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,73));
			enemyList.add(new Enemy(22 + 27 * ysect,4 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,76));

			objects[18 + 27 * ysect][3 + 27 * xsect] = CHEST;
			objects[18 + 27 * ysect][5 + 27 * xsect] = CHEST;
			objects[17 + 27 * ysect][4 + 27 * xsect] = new Object("door",0,hdoor,vdoor,0);
			//DOWNSHOOTER = new Object("shooter",2,shooter3,shooter3);
			objects[3 + 27 * ysect][21 + 27 * xsect] = DOWNSHOOTER;
			objects[3 + 27 * ysect][22 + 27 * xsect] = DOWNSHOOTER;
			objects[3 + 27 * ysect][23 + 27 * xsect] = DOWNSHOOTER;
			//UPSHOOTER = new Object("shooter",0,shooter1,shooter1);
			objects[23 + 27 * ysect][21 + 27 * xsect] = UPSHOOTER;
			objects[23 + 27 * ysect][22 + 27 * xsect] = UPSHOOTER;
			objects[23 + 27 * ysect][23 + 27 * xsect] = UPSHOOTER;
			//SPIKETRAP = new Object("spikeTrap",0,spikesOff,spikesOn);
			objects[10 + 27 * ysect][10 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][11 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[11 + 27 * ysect][10 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[11 + 27 * ysect][11 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[15 + 27 * ysect][15 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[15 + 27 * ysect][16 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[16 + 27 * ysect][15 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[16 + 27 * ysect][16 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[15 + 27 * ysect][10 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[15 + 27 * ysect][11 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[16 + 27 * ysect][10 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[16 + 27 * ysect][11 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][15 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][16 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[11 + 27 * ysect][15 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[11 + 27 * ysect][16 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][12 + 27 * xsect] = CRATE;
			objects[10 + 27 * ysect][13 + 27 * xsect] = CRATE;
			objects[10 + 27 * ysect][14 + 27 * xsect] = CRATE;
			objects[12 + 27 * ysect][9 + 27 * xsect] = CRATE;
			objects[13 + 27 * ysect][9 + 27 * xsect] = CRATE;
			objects[14 + 27 * ysect][9 + 27 * xsect] = CRATE;
			objects[12 + 27 * ysect][16 + 27 * xsect] = CRATE;
			objects[13 + 27 * ysect][16 + 27 * xsect] = CRATE;
			objects[14 + 27 * ysect][16 + 27 * xsect] = CRATE;
			objects[16 + 27 * ysect][12 + 27 * xsect] = CRATE;
			objects[16 + 27 * ysect][13 + 27 * xsect] = CRATE;
			objects[16 + 27 * ysect][14 + 27 * xsect] = CRATE;
			objects[12 + 27 * ysect][21 + 27 * xsect] = CRATE;
			objects[12 + 27 * ysect][22 + 27 * xsect] = CRATE;
			objects[12 + 27 * ysect][23 + 27 * xsect] = CRATE;
			objects[14 + 27 * ysect][21 + 27 * xsect] = CRATE;
			objects[14 + 27 * ysect][22 + 27 * xsect] = CRATE;
			objects[14 + 27 * ysect][23 + 27 * xsect] = CRATE;
			objects[21 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[22 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[23 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[11 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][1 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][2 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][3 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][4 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][5 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][6 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][7 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[7 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[6 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[5 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[4 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[3 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[2 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[1 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][1 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][2 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][3 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][5 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][6 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][7 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[8 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[7 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[6 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[2 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[1 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[0 + 27 * ysect][15 + 27 * xsect] = WALL;
		}
		else if(presets[8] == 1)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "right down turn";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "left up turn";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "blank";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "horizontal path";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			
			enemyList.add(new Enemy(18 + 27 * ysect,3 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,76));
			enemyList.add(new Enemy(18 + 27 * ysect,5 + 27 * xsect,"Warden", "melee",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,76));
			enemyList.add(new Enemy(22 + 27 * ysect,22 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,79));
			
			objects[20 + 27 * ysect][4 + 27 * xsect] = CHEST;
			objects[9 + 27 * ysect][4 + 27 * xsect] = new Object("door",0,hdoor,vdoor,0);
			objects[17 + 27 * ysect][4 + 27 * xsect] = new Object("door",0,hdoor,vdoor,0);
			objects[12 + 27 * ysect][23 + 27 * xsect] = LEFTSHOOTER;
			objects[13 + 27 * ysect][23 + 27 * xsect] = LEFTSHOOTER;
			objects[14 + 27 * ysect][23 + 27 * xsect] = LEFTSHOOTER;
			objects[12 + 27 * ysect][10 + 27 * xsect] = PIT;
			objects[13 + 27 * ysect][10 + 27 * xsect] = PIT;
			objects[13 + 27 * ysect][12 + 27 * xsect] = PIT;
			objects[14 + 27 * ysect][12 + 27 * xsect] = PIT;
			objects[12 + 27 * ysect][14 + 27 * xsect] = PIT;
			objects[13 + 27 * ysect][14 + 27 * xsect] = PIT;
			objects[13 + 27 * ysect][16 + 27 * xsect] = PIT;
			objects[14 + 27 * ysect][16 + 27 * xsect] = PIT;
			objects[12 + 27 * ysect][18 + 27 * xsect] = PIT;
			objects[13 + 27 * ysect][18 + 27 * xsect] = PIT;
			objects[12 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[13 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[14 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[21 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[22 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[23 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[17 + 27 * ysect][21 + 27 * xsect] = CRATE;
			objects[17 + 27 * ysect][22 + 27 * xsect] = CRATE;
			objects[17 + 27 * ysect][23 + 27 * xsect] = CRATE;
			objects[11 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][1 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][2 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][3 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][5 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][6 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][7 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][8 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][7 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][6 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][5 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][3 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][2 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][1 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][0 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][4 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][4 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][4 + 27 * xsect] = WALL;
		}
		else if(presets[8] == 2)
		{
			mapLayout[0 + 3 * ysect][0 + 3 * xsect] = "blank";
			mapLayout[0 + 3 * ysect][1 + 3 * xsect] = "right up turn";
			mapLayout[0 + 3 * ysect][2 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][0 + 3 * xsect] = "left down turn";
			mapLayout[1 + 3 * ysect][1 + 3 * xsect] = "room";
			mapLayout[1 + 3 * ysect][2 + 3 * xsect] = "left up turn";
			mapLayout[2 + 3 * ysect][0 + 3 * xsect] = "right up turn";
			mapLayout[2 + 3 * ysect][1 + 3 * xsect] = "left up turn";
			mapLayout[2 + 3 * ysect][2 + 3 * xsect] = "blank";
			
			enemyList.add(new Enemy(4 + 27 * ysect,22 + 27 * xsect,"Skeleton", "ranged",30 + (20 * difficulty) + (8*(level-1)), (2 * (level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,79));
			enemyList.add(new Enemy(22 + 27 * ysect,4 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2*(level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,73));
			enemyList.add(new Enemy(13 + 27 * ysect,13 + 27 * xsect,"Zombie", "melee",30 + (20 * difficulty) + (8*(level-1)), (2*(level-1)) + 25 + (5 * difficulty),0,blankNode,0,0,false,0,false,73));
			
			objects[10 + 27 * ysect][16 + 27 * xsect] = CHEST;
			objects[16 + 27 * ysect][10 + 27 * xsect] = UPSHOOTER;
			objects[15 + 27 * ysect][11 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[14 + 27 * ysect][10 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[12 + 27 * ysect][10 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][10 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][12 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[10 + 27 * ysect][14 + 27 * xsect] = new Object("spikeTrap",0,spikesOff,spikesOn,0);
			objects[18 + 27 * ysect][3 + 27 * xsect] = CRATE;
			objects[18 + 27 * ysect][4 + 27 * xsect] = CRATE;
			objects[18 + 27 * ysect][5 + 27 * xsect] = CRATE;
			objects[21 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[22 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[23 + 27 * ysect][8 + 27 * xsect] = CRATE;
			objects[8 + 27 * ysect][21 + 27 * xsect] = CRATE;
			objects[8 + 27 * ysect][22 + 27 * xsect] = CRATE;
			objects[8 + 27 * ysect][23 + 27 * xsect] = CRATE;
			objects[3 + 27 * ysect][18 + 27 * xsect] = CRATE;
			objects[4 + 27 * ysect][18 + 27 * xsect] = CRATE;
			objects[5 + 27 * ysect][18 + 27 * xsect] = CRATE;
			objects[17 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][9 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][10 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[9 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[10 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[15 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][17 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][16 + 27 * xsect] = WALL;
			objects[17 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[16 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[14 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[13 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[12 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][11 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][12 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][13 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][14 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][15 + 27 * xsect] = WALL;
			objects[11 + 27 * ysect][16 + 27 * xsect] = WALL;
		}
	}//end of mapLayout generation
	//permanent objects
	objects[76][40] = CHECKPOINT;
	objects[40][40] = LEVELCHANGER;
	for(int row = 0; row < moveLayout.length; row++)
	{
		for(int col = 0; col < moveLayout[0].length; col++)
		{
			moveLayout[row][col] = false;
		}
	}
	for(int row = 0; row < mapLayout.length; row++)
	{
		for(int col = 0; col < mapLayout[0].length; col++)
		{
			if(mapLayout[row][col].equals("room"))
			{
				moveLayout[row * 9 + 0][col * 9 + 0] = true;
				moveLayout[row * 9 + 0][col * 9 + 1] = true;
				moveLayout[row * 9 + 0][col * 9 + 2] = true;
				moveLayout[row * 9 + 0][col * 9 + 3] = true;
				moveLayout[row * 9 + 0][col * 9 + 4] = true;
				moveLayout[row * 9 + 0][col * 9 + 5] = true;
				moveLayout[row * 9 + 0][col * 9 + 6] = true;
				moveLayout[row * 9 + 0][col * 9 + 7] = true;
				moveLayout[row * 9 + 0][col * 9 + 8] = true;
				moveLayout[row * 9 + 1][col * 9 + 0] = true;
				moveLayout[row * 9 + 1][col * 9 + 1] = true;
				moveLayout[row * 9 + 1][col * 9 + 2] = true;
				moveLayout[row * 9 + 1][col * 9 + 3] = true;
				moveLayout[row * 9 + 1][col * 9 + 4] = true;
				moveLayout[row * 9 + 1][col * 9 + 5] = true;
				moveLayout[row * 9 + 1][col * 9 + 6] = true;
				moveLayout[row * 9 + 1][col * 9 + 7] = true;
				moveLayout[row * 9 + 1][col * 9 + 8] = true;
				moveLayout[row * 9 + 2][col * 9 + 0] = true;
				moveLayout[row * 9 + 2][col * 9 + 1] = true;
				moveLayout[row * 9 + 2][col * 9 + 2] = true;
				moveLayout[row * 9 + 2][col * 9 + 3] = true;
				moveLayout[row * 9 + 2][col * 9 + 4] = true;
				moveLayout[row * 9 + 2][col * 9 + 5] = true;
				moveLayout[row * 9 + 2][col * 9 + 6] = true;
				moveLayout[row * 9 + 2][col * 9 + 7] = true;
				moveLayout[row * 9 + 2][col * 9 + 8] = true;
				moveLayout[row * 9 + 3][col * 9 + 0] = true;
				moveLayout[row * 9 + 3][col * 9 + 1] = true;
				moveLayout[row * 9 + 3][col * 9 + 2] = true;
				moveLayout[row * 9 + 3][col * 9 + 3] = true;
				moveLayout[row * 9 + 3][col * 9 + 4] = true;
				moveLayout[row * 9 + 3][col * 9 + 5] = true;
				moveLayout[row * 9 + 3][col * 9 + 6] = true;
				moveLayout[row * 9 + 3][col * 9 + 7] = true;
				moveLayout[row * 9 + 3][col * 9 + 8] = true;
				moveLayout[row * 9 + 4][col * 9 + 0] = true;
				moveLayout[row * 9 + 4][col * 9 + 1] = true;
				moveLayout[row * 9 + 4][col * 9 + 2] = true;
				moveLayout[row * 9 + 4][col * 9 + 3] = true;
				moveLayout[row * 9 + 4][col * 9 + 4] = true;
				moveLayout[row * 9 + 4][col * 9 + 5] = true;
				moveLayout[row * 9 + 4][col * 9 + 6] = true;
				moveLayout[row * 9 + 4][col * 9 + 7] = true;
				moveLayout[row * 9 + 4][col * 9 + 8] = true;
				moveLayout[row * 9 + 5][col * 9 + 0] = true;
				moveLayout[row * 9 + 5][col * 9 + 1] = true;
				moveLayout[row * 9 + 5][col * 9 + 2] = true;
				moveLayout[row * 9 + 5][col * 9 + 3] = true;
				moveLayout[row * 9 + 5][col * 9 + 4] = true;
				moveLayout[row * 9 + 5][col * 9 + 5] = true;
				moveLayout[row * 9 + 5][col * 9 + 6] = true;
				moveLayout[row * 9 + 5][col * 9 + 7] = true;
				moveLayout[row * 9 + 5][col * 9 + 8] = true;
				moveLayout[row * 9 + 6][col * 9 + 0] = true;
				moveLayout[row * 9 + 6][col * 9 + 1] = true;
				moveLayout[row * 9 + 6][col * 9 + 2] = true;
				moveLayout[row * 9 + 6][col * 9 + 3] = true;
				moveLayout[row * 9 + 6][col * 9 + 4] = true;
				moveLayout[row * 9 + 6][col * 9 + 5] = true;
				moveLayout[row * 9 + 6][col * 9 + 6] = true;
				moveLayout[row * 9 + 6][col * 9 + 7] = true;
				moveLayout[row * 9 + 6][col * 9 + 8] = true;
				moveLayout[row * 9 + 7][col * 9 + 0] = true;
				moveLayout[row * 9 + 7][col * 9 + 1] = true;
				moveLayout[row * 9 + 7][col * 9 + 2] = true;
				moveLayout[row * 9 + 7][col * 9 + 3] = true;
				moveLayout[row * 9 + 7][col * 9 + 4] = true;
				moveLayout[row * 9 + 7][col * 9 + 5] = true;
				moveLayout[row * 9 + 7][col * 9 + 6] = true;
				moveLayout[row * 9 + 7][col * 9 + 7] = true;
				moveLayout[row * 9 + 7][col * 9 + 8] = true;
				moveLayout[row * 9 + 8][col * 9 + 0] = true;
				moveLayout[row * 9 + 8][col * 9 + 1] = true;
				moveLayout[row * 9 + 8][col * 9 + 2] = true;
				moveLayout[row * 9 + 8][col * 9 + 3] = true;
				moveLayout[row * 9 + 8][col * 9 + 4] = true;
				moveLayout[row * 9 + 8][col * 9 + 5] = true;
				moveLayout[row * 9 + 8][col * 9 + 6] = true;
				moveLayout[row * 9 + 8][col * 9 + 7] = true;
				moveLayout[row * 9 + 8][col * 9 + 8] = true;
			}
			if(mapLayout[row][col].equals("vertical path"))
			{
				moveLayout[row * 9 + 0][col * 9 + 3] = true;
				moveLayout[row * 9 + 0][col * 9 + 4] = true;
				moveLayout[row * 9 + 0][col * 9 + 5] = true;
				moveLayout[row * 9 + 1][col * 9 + 3] = true;
				moveLayout[row * 9 + 1][col * 9 + 4] = true;
				moveLayout[row * 9 + 1][col * 9 + 5] = true;
				moveLayout[row * 9 + 2][col * 9 + 3] = true;
				moveLayout[row * 9 + 2][col * 9 + 4] = true;
				moveLayout[row * 9 + 2][col * 9 + 5] = true;
				moveLayout[row * 9 + 3][col * 9 + 3] = true;
				moveLayout[row * 9 + 3][col * 9 + 4] = true;
				moveLayout[row * 9 + 3][col * 9 + 5] = true;
				moveLayout[row * 9 + 4][col * 9 + 3] = true;
				moveLayout[row * 9 + 4][col * 9 + 4] = true;
				moveLayout[row * 9 + 4][col * 9 + 5] = true;
				moveLayout[row * 9 + 5][col * 9 + 3] = true;
				moveLayout[row * 9 + 5][col * 9 + 4] = true;
				moveLayout[row * 9 + 5][col * 9 + 5] = true;
				moveLayout[row * 9 + 6][col * 9 + 3] = true;
				moveLayout[row * 9 + 6][col * 9 + 4] = true;
				moveLayout[row * 9 + 6][col * 9 + 5] = true;
				moveLayout[row * 9 + 7][col * 9 + 3] = true;
				moveLayout[row * 9 + 7][col * 9 + 4] = true;
				moveLayout[row * 9 + 7][col * 9 + 5] = true;
				moveLayout[row * 9 + 8][col * 9 + 3] = true;
				moveLayout[row * 9 + 8][col * 9 + 4] = true;
				moveLayout[row * 9 + 8][col * 9 + 5] = true;
			}
			if(mapLayout[row][col].equals("horizontal path"))
			{
				moveLayout[row * 9 + 3][col * 9 + 0] = true;
				moveLayout[row * 9 + 3][col * 9 + 1] = true;
				moveLayout[row * 9 + 3][col * 9 + 2] = true;
				moveLayout[row * 9 + 3][col * 9 + 3] = true;
				moveLayout[row * 9 + 3][col * 9 + 4] = true;
				moveLayout[row * 9 + 3][col * 9 + 5] = true;
				moveLayout[row * 9 + 3][col * 9 + 6] = true;
				moveLayout[row * 9 + 3][col * 9 + 7] = true;
				moveLayout[row * 9 + 3][col * 9 + 8] = true;
				moveLayout[row * 9 + 4][col * 9 + 0] = true;
				moveLayout[row * 9 + 4][col * 9 + 1] = true;
				moveLayout[row * 9 + 4][col * 9 + 2] = true;
				moveLayout[row * 9 + 4][col * 9 + 3] = true;
				moveLayout[row * 9 + 4][col * 9 + 4] = true;
				moveLayout[row * 9 + 4][col * 9 + 5] = true;
				moveLayout[row * 9 + 4][col * 9 + 6] = true;
				moveLayout[row * 9 + 4][col * 9 + 7] = true;
				moveLayout[row * 9 + 4][col * 9 + 8] = true;
				moveLayout[row * 9 + 5][col * 9 + 0] = true;
				moveLayout[row * 9 + 5][col * 9 + 1] = true;
				moveLayout[row * 9 + 5][col * 9 + 2] = true;
				moveLayout[row * 9 + 5][col * 9 + 3] = true;
				moveLayout[row * 9 + 5][col * 9 + 4] = true;
				moveLayout[row * 9 + 5][col * 9 + 5] = true;
				moveLayout[row * 9 + 5][col * 9 + 6] = true;
				moveLayout[row * 9 + 5][col * 9 + 7] = true;
				moveLayout[row * 9 + 5][col * 9 + 8] = true;
			}
			if(mapLayout[row][col].equals("left up turn"))
			{
				moveLayout[row * 9 + 0][col * 9 + 3] = true;
				moveLayout[row * 9 + 0][col * 9 + 4] = true;
				moveLayout[row * 9 + 0][col * 9 + 5] = true;			
				moveLayout[row * 9 + 1][col * 9 + 3] = true;
				moveLayout[row * 9 + 1][col * 9 + 4] = true;
				moveLayout[row * 9 + 1][col * 9 + 5] = true;			
				moveLayout[row * 9 + 2][col * 9 + 3] = true;
				moveLayout[row * 9 + 2][col * 9 + 4] = true;
				moveLayout[row * 9 + 2][col * 9 + 5] = true;		
				moveLayout[row * 9 + 3][col * 9 + 0] = true;
				moveLayout[row * 9 + 3][col * 9 + 1] = true;
				moveLayout[row * 9 + 3][col * 9 + 2] = true;
				moveLayout[row * 9 + 3][col * 9 + 3] = true;
				moveLayout[row * 9 + 3][col * 9 + 4] = true;
				moveLayout[row * 9 + 3][col * 9 + 5] = true;
				moveLayout[row * 9 + 4][col * 9 + 0] = true;
				moveLayout[row * 9 + 4][col * 9 + 1] = true;
				moveLayout[row * 9 + 4][col * 9 + 2] = true;
				moveLayout[row * 9 + 4][col * 9 + 3] = true;
				moveLayout[row * 9 + 4][col * 9 + 4] = true;
				moveLayout[row * 9 + 4][col * 9 + 5] = true;
				moveLayout[row * 9 + 5][col * 9 + 0] = true;
				moveLayout[row * 9 + 5][col * 9 + 1] = true;
				moveLayout[row * 9 + 5][col * 9 + 2] = true;
				moveLayout[row * 9 + 5][col * 9 + 3] = true;
				moveLayout[row * 9 + 5][col * 9 + 4] = true;
				moveLayout[row * 9 + 5][col * 9 + 5] = true;
			}
			if(mapLayout[row][col].equals("left down turn"))
			{			
				moveLayout[row * 9 + 3][col * 9 + 0] = true;
				moveLayout[row * 9 + 3][col * 9 + 1] = true;
				moveLayout[row * 9 + 3][col * 9 + 2] = true;
				moveLayout[row * 9 + 3][col * 9 + 3] = true;
				moveLayout[row * 9 + 3][col * 9 + 4] = true;
				moveLayout[row * 9 + 3][col * 9 + 5] = true;
				moveLayout[row * 9 + 4][col * 9 + 0] = true;
				moveLayout[row * 9 + 4][col * 9 + 1] = true;
				moveLayout[row * 9 + 4][col * 9 + 2] = true;
				moveLayout[row * 9 + 4][col * 9 + 3] = true;
				moveLayout[row * 9 + 4][col * 9 + 4] = true;
				moveLayout[row * 9 + 4][col * 9 + 5] = true;
				moveLayout[row * 9 + 5][col * 9 + 0] = true;
				moveLayout[row * 9 + 5][col * 9 + 1] = true;
				moveLayout[row * 9 + 5][col * 9 + 2] = true;
				moveLayout[row * 9 + 5][col * 9 + 3] = true;
				moveLayout[row * 9 + 5][col * 9 + 4] = true;
				moveLayout[row * 9 + 5][col * 9 + 5] = true;			
				moveLayout[row * 9 + 6][col * 9 + 3] = true;
				moveLayout[row * 9 + 6][col * 9 + 4] = true;
				moveLayout[row * 9 + 6][col * 9 + 5] = true;				
				moveLayout[row * 9 + 7][col * 9 + 3] = true;
				moveLayout[row * 9 + 7][col * 9 + 4] = true;
				moveLayout[row * 9 + 7][col * 9 + 5] = true;				
				moveLayout[row * 9 + 8][col * 9 + 3] = true;
				moveLayout[row * 9 + 8][col * 9 + 4] = true;
				moveLayout[row * 9 + 8][col * 9 + 5] = true;
			}
			if(mapLayout[row][col].equals("right up turn"))
			{

				moveLayout[row * 9 + 0][col * 9 + 3] = true;
				moveLayout[row * 9 + 0][col * 9 + 4] = true;
				moveLayout[row * 9 + 0][col * 9 + 5] = true;
				moveLayout[row * 9 + 1][col * 9 + 3] = true;
				moveLayout[row * 9 + 1][col * 9 + 4] = true;
				moveLayout[row * 9 + 1][col * 9 + 5] = true;				
				moveLayout[row * 9 + 2][col * 9 + 3] = true;
				moveLayout[row * 9 + 2][col * 9 + 4] = true;
				moveLayout[row * 9 + 2][col * 9 + 5] = true;						
				moveLayout[row * 9 + 3][col * 9 + 3] = true;
				moveLayout[row * 9 + 3][col * 9 + 4] = true;
				moveLayout[row * 9 + 3][col * 9 + 5] = true;
				moveLayout[row * 9 + 3][col * 9 + 6] = true;
				moveLayout[row * 9 + 3][col * 9 + 7] = true;
				moveLayout[row * 9 + 3][col * 9 + 8] = true;			
				moveLayout[row * 9 + 4][col * 9 + 3] = true;
				moveLayout[row * 9 + 4][col * 9 + 4] = true;
				moveLayout[row * 9 + 4][col * 9 + 5] = true;
				moveLayout[row * 9 + 4][col * 9 + 6] = true;
				moveLayout[row * 9 + 4][col * 9 + 7] = true;
				moveLayout[row * 9 + 4][col * 9 + 8] = true;			
				moveLayout[row * 9 + 5][col * 9 + 3] = true;
				moveLayout[row * 9 + 5][col * 9 + 4] = true;
				moveLayout[row * 9 + 5][col * 9 + 5] = true;
				moveLayout[row * 9 + 5][col * 9 + 6] = true;
				moveLayout[row * 9 + 5][col * 9 + 7] = true;
				moveLayout[row * 9 + 5][col * 9 + 8] = true;
			}
			if(mapLayout[row][col].equals("right down turn"))
			{			
				moveLayout[row * 9 + 3][col * 9 + 3] = true;
				moveLayout[row * 9 + 3][col * 9 + 4] = true;
				moveLayout[row * 9 + 3][col * 9 + 5] = true;
				moveLayout[row * 9 + 3][col * 9 + 6] = true;
				moveLayout[row * 9 + 3][col * 9 + 7] = true;
				moveLayout[row * 9 + 3][col * 9 + 8] = true;			
				moveLayout[row * 9 + 4][col * 9 + 3] = true;
				moveLayout[row * 9 + 4][col * 9 + 4] = true;
				moveLayout[row * 9 + 4][col * 9 + 5] = true;
				moveLayout[row * 9 + 4][col * 9 + 6] = true;
				moveLayout[row * 9 + 4][col * 9 + 7] = true;
				moveLayout[row * 9 + 4][col * 9 + 8] = true;			
				moveLayout[row * 9 + 5][col * 9 + 3] = true;
				moveLayout[row * 9 + 5][col * 9 + 4] = true;
				moveLayout[row * 9 + 5][col * 9 + 5] = true;
				moveLayout[row * 9 + 5][col * 9 + 6] = true;
				moveLayout[row * 9 + 5][col * 9 + 7] = true;
				moveLayout[row * 9 + 5][col * 9 + 8] = true;			
				moveLayout[row * 9 + 6][col * 9 + 3] = true;
				moveLayout[row * 9 + 6][col * 9 + 4] = true;
				moveLayout[row * 9 + 6][col * 9 + 5] = true;		
				moveLayout[row * 9 + 7][col * 9 + 3] = true;
				moveLayout[row * 9 + 7][col * 9 + 4] = true;
				moveLayout[row * 9 + 7][col * 9 + 5] = true;		
				moveLayout[row * 9 + 8][col * 9 + 3] = true;
				moveLayout[row * 9 + 8][col * 9 + 4] = true;
				moveLayout[row * 9 + 8][col * 9 + 5] = true;	
			}	
			drawPanel.repaint();
		}
	}
	try
	{
		String file;
		if(gameMode == 0)
			file = "Saved_Data.txt";
		else
			file = "Dungeon_Saved_Data.txt";
		FileOutputStream f = new FileOutputStream(new File(file));
		ObjectOutputStream o = new ObjectOutputStream(f);
		for(int i = 0; i < 9; i++)
			o.writeObject(presets[i]);
		o.close();
		f.close();
	}
	catch (IOException e) {}
}

private class DrawPanel extends JPanel
{
	public void paint(Graphics g)  
	{  
		armor = inventoryList[0].Protection() + inventoryList[1].Protection() + inventoryList[2].Protection() + inventoryList[3].Protection() ;
		//This will draw drawImageIntoJFrame.jpg into JFrame  
		//put variable in location to move them in game (and size of the
		//image in pixels correlates to the grid in the jFrame)
		g.drawImage(background,-1350 + ex,-1350 + ey,null); 
		for(int row = 0; row < mapLayout.length; row++)
		{
			for(int col = 0; col < mapLayout[0].length; col++)
			{
				if(mapLayout[row][col].equals("room"))
				{
					g.drawImage(roomTemplate,(col - 4) * 225 + ex,(row - 4) * 225 + ey + 25,null); 
				}
				if(mapLayout[row][col].equals("horizontal path"))
				{
					g.drawImage(hPathTemplate,(col - 4) * 225 + ex,(row - 4) * 225 + ey + 25,null); 
				}
				if(mapLayout[row][col].equals("vertical path"))
				{
					g.drawImage(vPathTemplate,(col - 4) * 225 + ex,(row - 4) * 225 + ey + 25,null); 
				}
				if(mapLayout[row][col].equals("left up turn"))
				{
					g.drawImage(luTurnTemplate,(col - 4) * 225 + ex,(row - 4) * 225 + ey + 25,null); 
				}
				if(mapLayout[row][col].equals("left down turn"))
				{
					g.drawImage(ldTurnTemplate,(col - 4) * 225 + ex,(row - 4) * 225 + ey + 25,null); 
				}
				if(mapLayout[row][col].equals("right up turn"))
				{
					g.drawImage(ruTurnTemplate,(col - 4) * 225 + ex,(row - 4) * 225 + ey + 25,null); 
				}
				if(mapLayout[row][col].equals("right down turn"))
				{
					g.drawImage(rdTurnTemplate,(col - 4) * 225 + ex,(row - 4) * 225 + ey + 25,null); 
				}
				if(mapLayout[row][col].equals("blank"))
				{
					g.drawImage(blankTemplate,(col - 4) * 225 + ex,(row - 4) * 225 + ey + 25,null); 
				}
			}
		}
		//draw path
		if(drawPath == true)
			{
			for(int i = 0; i < open.size(); i++)
			{
				g.drawImage(path1,(open.get(i).getCol() - 36) * 25 + ex,(open.get(i).getRow() - 35) * 25 + ey,null);
				g.drawString(open.get(i).getCost() + " ",(open.get(i).getCol() - 36) * 25 + ex + 2,(open.get(i).getRow() - 35) * 25 + ey + 12);
			}
			for(int i = 0; i < closed.size(); i++)
			{
				g.drawImage(path2,(closed.get(i).getCol() - 36) * 25 + ex,(closed.get(i).getRow() - 35) * 25 + ey,null);
				g.drawString(closed.get(i).getCost() + " ",(closed.get(i).getCol() - 36) * 25 + ex + 2,(closed.get(i).getRow() - 35) * 25 + ey + 12);
			}
			for(int i = 0; i < fastestPath.size(); i++)
			{
				g.drawImage(path3,(fastestPath.get(i).getCol() - 36) * 25 + ex,(fastestPath.get(i).getRow() - 35) * 25 + ey,null);
			}
		}
		//draw objects
		for(int row = 0; row < objects.length; row++)
		{
			for(int col = 0; col < objects[0].length; col++)
			{
				if(objects[row][col].Type().equals("chest"))
				{
					g.drawImage(objects[row][col].Image1(),(col - 36) * 25 + ex,(row - 35) * 25 + ey,null);
				}
				else
				{
					if(objects[row][col].getPhase() == 0)
					{
						g.drawImage(objects[row][col].Image1(),(col - 36) * 25 + ex,(row - 35) * 25 + ey,null);
					}
					else
					{
						if(objects[row][col].Type().equals("door"))
						{
							if(objects[row][col].getPhase() == 1 || objects[row][col].getPhase() == 4)
							{
								g.drawImage(objects[row][col].Image2(),(col - 36) * 25 + ex - 9,(row - 35) * 25 + ey - 9,null);
							}else if(objects[row][col].getPhase() == 2 || objects[row][col].getPhase() == 3)
							{
								g.drawImage(objects[row][col].Image2(),(col - 36) * 25 + ex + 9,(row - 35) * 25 + ey + 9,null);
							}	
							
						}
						else
						{
							g.drawImage(objects[row][col].Image2(),(col - 36) * 25 + ex,(row - 35) * 25 + ey,null);
						}
					}
				}	
			}
		}
		
		//draw lootbags
		for(int i = 0; i < lootbags.size(); i++)
		{
			g.drawImage(lootbag,(lootbags.get(i).getCol() - 36) * 25 + ex,(lootbags.get(i).getRow() - 35) * 25 + ey,null);
		}
		
		//draw projectiles
		for(int i = 0; i < projectiles.length; i++)
		{
			if(projectiles[i].getName().equals("Arrow"))
			{
				if(projectiles[i].getDirection() == 0)
					g.drawImage(arrow1,(projectiles[i].getCol() - 36) * 25 + ex + projectiles[i].getColMod(),(projectiles[i].getRow() - 35) * 25 + ey + projectiles[i].getRowMod() - 10,null);
				if(projectiles[i].getDirection() == 1)
					g.drawImage(arrow2,(projectiles[i].getCol() - 36) * 25 + ex + projectiles[i].getColMod() + 10,(projectiles[i].getRow() - 35) * 25 + ey + projectiles[i].getRowMod(),null);
				if(projectiles[i].getDirection() == 2)
					g.drawImage(arrow3,(projectiles[i].getCol() - 36) * 25 + ex + projectiles[i].getColMod(),(projectiles[i].getRow() - 35) * 25 + ey + projectiles[i].getRowMod() + 10,null);
				if(projectiles[i].getDirection() == 3)
					g.drawImage(arrow4,(projectiles[i].getCol() - 36) * 25 + ex + projectiles[i].getColMod() - 10,(projectiles[i].getRow() - 35) * 25 + ey + projectiles[i].getRowMod(),null);
				
				//g.drawString(projectiles[i].getCol() + " " + projectiles[i].getRow(),(projectiles[i].getCol() - 36) * 25 + ex + projectiles[i].getColMod(),(projectiles[i].getRow() - 35) * 25 + ey + projectiles[i].getRowMod());
			}
		}
		
		//draw enemies
		for(int i = 0; i < enemyList.size(); i++)
		{
			if(enemyList.get(i).getHasLineOfSight() == true)
			{
				//g.drawString("true",(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey - 10);
			}
			else
			{
				//g.drawString("false",(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey - 10);
			}
			if(enemyList.get(i).getName().equals("Zombie"))//zombie
			{
				if(enemyList.get(i).getDirection() == 0)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(zombie1,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
					else
					{
						g.drawImage(dzombie1,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
				}else if(enemyList.get(i).getDirection() == 1)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(zombie2,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
					else
					{
						g.drawImage(dzombie2,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
				}else if(enemyList.get(i).getDirection() == 2)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(zombie3,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
					else
					{
						g.drawImage(dzombie3,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
				}else if(enemyList.get(i).getDirection() == 3)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(zombie4,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
					else
					{
						g.drawImage(dzombie4,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
				}
			}
			else if(enemyList.get(i).getName().equals("Skeleton"))//skeleton
			{
				if(enemyList.get(i).getDirection() == 0)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(skeleton1,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					}
					else
					{
						g.drawImage(dskeleton1,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					}
					if(enemyList.get(i).getXShift() == 0)
						g.drawImage(bowA1,(enemyList.get(i).getCol() - 36) * 25 + ex - 2,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					else if(enemyList.get(i).getXShift() == 1)
						g.drawImage(bowA2,(enemyList.get(i).getCol() - 36) * 25 + ex - 2,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					else if(enemyList.get(i).getXShift() == 2)
						g.drawImage(bowA3,(enemyList.get(i).getCol() - 36) * 25 + ex - 2,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
				}else if(enemyList.get(i).getDirection() == 1)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(skeleton2,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					}
					else
					{
						g.drawImage(dskeleton2,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					}
					if(enemyList.get(i).getXShift() == 0)
						g.drawImage(bowB1,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey - 2,null);
					else if(enemyList.get(i).getXShift() == 1)
						g.drawImage(bowB2,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey - 2,null);
					else if(enemyList.get(i).getXShift() == 2)
						g.drawImage(bowB3,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey - 2,null);
				}else if(enemyList.get(i).getDirection() == 2)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(skeleton3,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					}
					else
					{
						g.drawImage(dskeleton3,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					}
					if(enemyList.get(i).getXShift() == 0)
						g.drawImage(bowC1,(enemyList.get(i).getCol() - 36) * 25 + ex + 2,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					else if(enemyList.get(i).getXShift() == 1)
						g.drawImage(bowC2,(enemyList.get(i).getCol() - 36) * 25 + ex + 2,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					else if(enemyList.get(i).getXShift() == 2)
						g.drawImage(bowC3,(enemyList.get(i).getCol() - 36) * 25 + ex + 2,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
				}else if(enemyList.get(i).getDirection() == 3)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(skeleton4,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					}
					else
					{
						g.drawImage(dskeleton4,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey,null);
					}
					if(enemyList.get(i).getXShift() == 0)
						g.drawImage(bowD1,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey + 2,null);
					else if(enemyList.get(i).getXShift() == 1)
						g.drawImage(bowD2,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey + 2,null);
					else if(enemyList.get(i).getXShift() == 2)
						g.drawImage(bowD3,(enemyList.get(i).getCol() - 36) * 25 + ex,(enemyList.get(i).getRow() - 35) * 25 + ey + 2,null);
				}
			}else if(enemyList.get(i).getName().equals("Warden"))//warden
			{
				if(enemyList.get(i).getDirection() == 0)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(warden1,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
					else
					{
						g.drawImage(dwarden1,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
				}else if(enemyList.get(i).getDirection() == 1)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(warden2,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
					else
					{
						g.drawImage(dwarden2,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
				}else if(enemyList.get(i).getDirection() == 2)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(warden3,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
					else
					{
						g.drawImage(dwarden3,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
				}else if(enemyList.get(i).getDirection() == 3)
				{
					if(enemyList.get(i).getDamaged() == false)
					{
						g.drawImage(warden4,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
					else
					{
						g.drawImage(dwarden4,(enemyList.get(i).getCol() - 36) * 25 + ex + enemyList.get(i).getXShift(),(enemyList.get(i).getRow() - 35) * 25 + ey + enemyList.get(i).getYShift(),null);
					}
				}
			}
		}
		//draw player
		if(direction == 0)
		{
			g.drawImage(man1,x,y,null);
		}
		if(direction == 1)
		{
			g.drawImage(body2,x,y,null);
		}
		if(direction == 2)
		{
			g.drawImage(body3,x,y,null);
		}
		if(direction == 3)
		{
			g.drawImage(body4,x,y,null);
		}
		for(int i = 3; i >= 0; i--)
		{
			if(direction == 0)
			{
				g.drawImage(inventoryList[i].image1,x,y,null);
			}
			if(direction == 1)
			{
				g.drawImage(inventoryList[i].image2,x,y,null);
			}
			if(direction == 2)
			{
				g.drawImage(inventoryList[i].image3,x,y,null);
			}
			if(direction == 3)
			{
				g.drawImage(inventoryList[i].image4,x,y,null);
			}
		}
		
		if(direction == 0)
		{
			if(isAttacking == true)
			{
				g.drawImage(inventoryList[4].image1,x - 1,y - 7,null);
			}
			else
			{
				g.drawImage(inventoryList[4].image1,x - 1,y - 3,null);
			}
			if(isBlocking == true)
			{
				g.drawImage(inventoryList[5].image5,x,y,null);
			}
			else
			{
				g.drawImage(inventoryList[5].image1,x,y,null);
			}	
		}			
		if(direction == 1)
		{
			if(isAttacking == true)
			{
				g.drawImage(inventoryList[4].image2,x + 7,y - 1,null);
			}
			else
			{
				g.drawImage(inventoryList[4].image2,x + 3,y - 1,null);
			}
			if(isBlocking == true)
			{
				g.drawImage(inventoryList[5].image6,x,y,null);
			}
			else
			{
				g.drawImage(inventoryList[5].image2,x,y,null);
			}	
		}
		if(direction == 2)
		{
			if(isAttacking == true)
			{
				g.drawImage(inventoryList[4].image3,x + 1,y + 7,null);
			}
			else
			{
				g.drawImage(inventoryList[4].image3,x + 1,y + 3,null);
			}
			if(isBlocking == true)
			{
				g.drawImage(inventoryList[5].image7,x,y,null);
			}
			else
			{
				g.drawImage(inventoryList[5].image3,x,y,null);
			}	
		}
		if(direction == 3)
		{
			if(isAttacking == true)
			{
				g.drawImage(inventoryList[4].image4,x - 7,y + 1,null);
			}
			else
			{
				g.drawImage(inventoryList[4].image4,x - 3,y + 1,null);
			}
			if(isBlocking == true)
			{
				g.drawImage(inventoryList[5].image8,x,y,null);
			}
			else
			{
				g.drawImage(inventoryList[5].image4,x,y,null);
			}	
		}
			
		//damage tick
		if(damageOn == true)
		{
			g.drawImage(damageTickScreen,-1350 + ex,-1350 + ey,null);
		}
		
		g.drawImage(hudbackground,0,-20,null);
		
		//draw f interact bubble
		if(fInteract == true)
		{
			g.drawImage(fInteraction,425, 380, null);
		}
		
		//draw inventory
		if(inChest == true)
		{
			g.drawImage(chestInventory,340 + shifter,200,null);
		}
		if(inInventory == true)
		{
			g.drawImage(inventory,340 - shifter,200,null);
			//selector
			if(selector[0] < 4)
			{
				if(selector[1] > 0)
				{
					if(selector[1] < 6)
					{
						g.drawImage(selectorCursor,390 + selector[1] * 45 - shifter,220 + selector[0] * 45,null);
						if(deleteSet == true)
						{
							g.drawImage(deleteIcon,390 + selector[1] * 45 - shifter,220 + selector[0] * 45,null);
						}
					}
					else
					{
						g.drawImage(selectorCursor,390 + selector[1] * 45 - shifter + 35,220 + selector[0] * 45,null);
						if(deleteSet == true)
						{
							g.drawImage(deleteIcon,390 + selector[1] * 45 - shifter + 35,220 + selector[0] * 45,null);
						}
					}
				}
				else
				{
					g.drawImage(selectorCursor,345 + selector[1] * 45 - shifter,220 + selector[0] * 45,null);
				}
			}
			else
			{
				if(selector[1] > 1)
				{
					g.drawImage(selectorCursor,390 + selector[1] * 45 - shifter,240 + selector[0] * 45,null);
				}
				else
				{
					g.drawImage(selectorCursor,345 + selector[1] * 45 - shifter,240 + selector[0] * 45,null);
				}
			}
			//selected
			if(isSelected == true)
			{
				if(selected[0] < 4)
				{
					if(selected[1] > 0)
					{
						if(selected[1] < 6)
						{
							g.drawImage(selectedIcon,390 + selected[1] * 45 - shifter,220 + selected[0] * 45,null);
						}
						else
						{
							g.drawImage(selectedIcon,390 + selected[1] * 45 - shifter + 35,220 + selected[0] * 45,null);
						}
					}
					else
					{
						g.drawImage(selectedIcon,345 + selected[1] * 45 - shifter,220 + selected[0] * 45,null);
					}
				}
				else
				{
					if(selected[1] > 1)
					{
						g.drawImage(selectedIcon,390 + selected[1] * 45 - shifter,240 + selected[0] * 45,null);
					}
					else
					{
						g.drawImage(selectedIcon,345 + selected[1] * 45 - shifter,240 + selected[0] * 45,null);
					}
				}
			}
			if(inInventory == true)
			{
				g.drawImage(inventoryIcon,340 - shifter,200,null);
			}
			
			
			//draw items in inventory
			for(int row = 0; row < 4; row++)
			{
				for(int col = 0; col < 5; col++)
				{
					g.drawImage(inventoryList[(5 * row) + col + 10].iconImage(),435 + col * 45 - shifter,220 + row * 45,null);
					if(inChest == true)
					{
						if(direction == 0)
							g.drawImage(chestInventoryList[((5 * row) + col) + 20 * objects[ymod - 1][xmod].getPhase()].iconImage(),345 + col * 45 + shifter,220 + row * 45,null);
						else if(direction == 1)
							g.drawImage(chestInventoryList[((5 * row) + col) + 20 * objects[ymod][xmod + 1].getPhase()].iconImage(),345 + col * 45 + shifter,220 + row * 45,null);
						else if(direction == 2)
							g.drawImage(chestInventoryList[((5 * row) + col) + 20 * objects[ymod + 1][xmod].getPhase()].iconImage(),345 + col * 45 + shifter,220 + row * 45,null);
						else if(direction == 3)
							g.drawImage(chestInventoryList[((5 * row) + col) + 20 * objects[ymod][xmod - 1].getPhase()].iconImage(),345 + col * 45 + shifter,220 + row * 45,null);
					}
				}
			}
			for(int i = 0; i < 4; i++)
			{
				g.drawImage(inventoryList[i].iconImage(),345 + 0 * 45 - shifter,220 + i * 45,null);
			}
			for(int i = 0; i < 2; i++)
			{
				g.drawImage(inventoryList[i + 4].iconImage(),345 + i * 45 - shifter,239 + 4 * 45,null);
			}
			for(int i = 2; i < 6; i++)
			{
				g.drawImage(inventoryList[i + 4].iconImage(),390 + i * 45 - shifter,240 + 4 * 45,null);
			}

			//draw item texts
			if(selector[0] < 4)
			{
				if(selector[1] > 0)
				{			
					if(selector[1] < 6)
					{
						g.drawString(inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name(),390 + selector[1] * 45 - (inventoryList[(5 * selector[0]) + selector[1] - 1 + 10].Name().length()*4 - 23) - shifter,259 + selector[0] * 45);
					}
					else if(direction == 0)
					{
						g.drawString(chestInventoryList[((5 * selector[0]) + selector[1] - 6) + 20 * objects[ymod - 1][xmod].getPhase()].Name(),390 + selector[1] * 45 - (chestInventoryList[((5 * selector[0]) + selector[1] - 6) + 20 * objects[ymod - 1][xmod].getPhase()].Name().length()*4 - 23) - shifter + 35,259 + selector[0] * 45);
					}
					else if(direction == 1)
					{
						g.drawString(chestInventoryList[((5 * selector[0]) + selector[1] - 6) + 20 * objects[ymod][xmod + 1].getPhase()].Name(),390 + selector[1] * 45 - (chestInventoryList[((5 * selector[0]) + selector[1] - 6) + 20 * objects[ymod][xmod + 1].getPhase()].Name().length()*4 - 23) - shifter + 35,259 + selector[0] * 45);
					}
					else if(direction == 2)
					{
						g.drawString(chestInventoryList[((5 * selector[0]) + selector[1] - 6) + 20 * objects[ymod + 1][xmod].getPhase()].Name(),390 + selector[1] * 45 - (chestInventoryList[((5 * selector[0]) + selector[1] - 6) + 20 * objects[ymod + 1][xmod].getPhase()].Name().length()*4 - 23) - shifter + 35,259 + selector[0] * 45);
					}
					else if(direction == 3)
					{
						g.drawString(chestInventoryList[((5 * selector[0]) + selector[1] - 6) + 20 * objects[ymod][xmod - 1].getPhase()].Name(),390 + selector[1] * 45 - (chestInventoryList[((5 * selector[0]) + selector[1] - 6) + 20 * objects[ymod][xmod - 1].getPhase()].Name().length()*4 - 23) - shifter + 35,259 + selector[0] * 45);
					}
						
				}
				else
				{
					g.drawString(inventoryList[selector[0]].Name(),345 + selector[1] * 45 - (inventoryList[selector[0]].Name().length()*4 - 23) - shifter,259 + selector[0] * 45);
				}
			}
			else
			{
				if(selector[1] > 1)
				{
					g.drawString(inventoryList[4 + selector[1]].Name(),390 + selector[1] * 45 - (inventoryList[4 + selector[1]].Name().length()*4 - 23) - shifter,279 + selector[0] * 45);
				}
				else
				{
					g.drawString(inventoryList[4 + selector[1]].Name(),345 + selector[1] * 45 - (inventoryList[4 + selector[1]].Name().length()*4 - 23) - shifter,279 + selector[0] * 45);
				}
			}
			for(int i = 6; i < 30; i++)
			{//chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()]
				if(inventoryList[i].Stack() > 1 && i < 10)
				{
					g.drawString(inventoryList[i].Stack() + "",420 + (i-4) * 45 - shifter, 252 + 4 * 45);
				}
				else if(inventoryList[i].Stack() > 1 && i < 15)
				{
					g.drawString(inventoryList[i].Stack() + "",420 + (i-9) * 45 - shifter, 232 + 0);
				}
				else if(inventoryList[i].Stack() > 1 && i < 20)
				{
					g.drawString(inventoryList[i].Stack() + "",420 + (i-14) * 45 - shifter, 232 + 1 * 45);
				}
				else if(inventoryList[i].Stack() > 1 && i < 25)
				{
					g.drawString(inventoryList[i].Stack() + "",420 + (i-19) * 45 - shifter, 232 + 2 * 45);
				}
				else if(inventoryList[i].Stack() > 1 && i < 30)
				{
					g.drawString(inventoryList[i].Stack() + "",420 + (i-24) * 45 - shifter, 232 + 3 * 45);
				}
				if(direction == 0 && inChest == true)
				{
					if(chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() > 1 && i < 11)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + "",415 + (i-3) * 45, 232 + 0 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() > 1 && i < 16)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + "",415 + (i-8) * 45, 232 + 1 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() > 1 && i < 21)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + "",415 + (i-13) * 45, 232 + 2 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() > 1 && i < 26)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + "",415 + (i-18) * 45, 232 + 3 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() > 1 && i < 31)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod - 1][xmod].getPhase()].Stack() + "",415 + (i-23) * 45, 232 + 4 * 45);
					}
				}
				else if(direction == 1 && inChest == true)
				{
					if(chestInventoryList[i - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() > 1 && i < 11)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + "",415 + (i-3) * 45, 232 + 0 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() > 1 && i < 16)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + "",415 + (i-8) * 45, 232 + 1 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() > 1 && i < 21)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + "",415 + (i-13) * 45, 232 + 2 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() > 1 && i < 26)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + "",415 + (i-18) * 45, 232 + 3 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() > 1 && i < 31)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod][xmod + 1].getPhase()].Stack() + "",415 + (i-23) * 45, 232 + 4 * 45);
					}
				}
				else if(direction == 2 && inChest == true)
				{
					if(chestInventoryList[i - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() > 1 && i < 11)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + "",415 + (i-3) * 45, 232 + 0 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() > 1 && i < 16)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + "",415 + (i-8) * 45, 232 + 1 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() > 1 && i < 21)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + "",415 + (i-13) * 45, 232 + 2 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() > 1 && i < 26)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + "",415 + (i-18) * 45, 232 + 3 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() > 1 && i < 31)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod + 1][xmod].getPhase()].Stack() + "",415 + (i-23) * 45, 232 + 4 * 45);
					}
				}
				else if(direction == 3 && inChest == true)
				{
					if(chestInventoryList[i - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() > 1 && i < 11)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + "",415 + (i-3) * 45, 232 + 0 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() > 1 && i < 16)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + "",415 + (i-8) * 45, 232 + 1 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() > 1 && i < 21)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + "",415 + (i-13) * 45, 232 + 2 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() > 1 && i < 26)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + "",415 + (i-18) * 45, 232 + 3 * 45);
					}
					else if(chestInventoryList[i - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() > 1 && i < 31)
					{
						g.drawString(chestInventoryList[i - 6 + 20 * objects[ymod][xmod - 1].getPhase()].Stack() + "",415 + (i-23) * 45, 232 + 4 * 45);
					}
				}
			}
		}
		else
		{
			g.drawImage(hotbar,340,545,null);
			if(isAttacking == true && inventoryList[4].Type().equals("weapon"))
			{
				g.drawImage(selectedIcon,345 + 0 * 45,565,null);
			}
			if(isBlocking == true && inventoryList[5].Type().equals("shield"))
			{
				g.drawImage(selectedIcon,345 + 1 * 45,565,null);
			}
			for(int i = 0; i < 2; i++)
			{
				g.drawImage(inventoryList[i + 4].iconImage(),345 + i * 45,564,null);
			}
			for(int i = 2; i < 6; i++)
			{
				g.drawImage(inventoryList[i + 4].iconImage(),390 + i * 45,565,null);
			}
			//draw health
			
			//for(int i = 0; i < health; i++)
			//{
			//	g.drawImage(heartImage,335 + i * 24,510,null);
			//}
			g.fillRect(345, 518, 304, 24);
			g.setColor(Color.GRAY);
			g.fillRect(347, 520, 300, 20);
			g.setColor(Color.RED);
			g.fillRect(347, 520, health * 3, 20);
			g.setColor(Color.BLACK);
			g.drawString(health + "/100",482, 535);
			
			g.setColor(Color.MAGENTA);
			g.drawString("Level: " + level,250, 569);
			g.setColor(Color.RED);
			g.drawString("Lives:",675, 560);
			g.drawString("X " + lives,710, 592);
			g.drawImage(livesImage,670,565,null);
			
			if(showKillList)
			{
			g.setColor(Color.GREEN);
			g.drawString("Enemy Kill List Id's: ",670, 600);
			for(int i = 0; i < enemyKillList.size();i++)
				g.drawString(enemyKillList.get(i) + ", ",780 + i*20, 600);
			}
			g.setColor(Color.MAGENTA);
			if(difficulty == 0)
				g.drawString("Difficulty: Easy",250,589);
			if(difficulty == 1)
				g.drawString("Difficulty: Normal",240,589);
			if(difficulty == 2)
				g.drawString("Difficulty: Hard",250,589);
			g.setColor(Color.BLACK);
			for(int i = 0; i < 20; i++)
			{
				if(i%2 == 0)
				{
					g.drawImage(armorBarOn,330 + i * 6,485,null);
				}
				if(i < armor)
				{
					if(i%2 == 0)
					{
						g.drawImage(armorBarOff1,330 + i * 6,485,null);
					}
					else
					{
						g.drawImage(armorBarOff2,324 + i * 6,485,null);
					}
				}
			}
			for(int i = 6; i < 10; i++)
			{
				if(inventoryList[i].Stack() > 1)
				{
					g.drawString(inventoryList[i].Stack() + "",420 + (i-4) * 45, 577);
				}
			}
			if(health <= 0)
			{
				g.drawImage(background,-1350 + ex,-1350 + ey,null);
				g.setColor(Color.WHITE);
				g.drawString("You Died",x-5,y-2);
				g.drawString("Respawning in... " + deathTimer,x-25,y+12);
			}
			//menu
			if(inMenu == true)
			{
				if(inControls == true)
				{
					g.drawImage(controls,0,0,null);
				}
				else
				{
					if(inOptions == true)
					{
						g.drawImage(menuImage2,420,220,null);
						if(menuSelector == 0)
							g.drawImage(menuSelectorImage,440 + menuSelector,menuSelector * 51 + 296,null);
						else if(menuSelector == 1)
							g.drawImage(menuSelectorImage,455 + menuSelector,menuSelector * 51 + 298,null);
						else if(menuSelector == 2)
							g.drawImage(menuSelectorImage,430 + menuSelector,menuSelector * 51 + 296,null);
					}
					else
					{
						g.drawImage(menuImage,420,220,null);
						g.drawImage(menuSelectorImage,440 + menuSelector * 11,menuSelector * 41 + 286,null);
					}
				}
			}
			if(isSaving == true)
			{
				g.drawImage(savedImage,420,215,null);
			}
			
			if(gameOver == true)
			{
				//background
				g.drawImage(gameOverScreen,0,0,null);
				//difficulty
				if(difficulty == 0)
					g.drawImage(gameOverEasy,0,0,null);
				else if(difficulty == 1)
					g.drawImage(gameOverNormal,0,0,null);
				else
					g.drawImage(gameOverHard,0,0,null);
				//level
				char[] finalLevel = ("" + level).toCharArray();
				int gap = 17;
				for(int i = 0; i < finalLevel.length; i++)
				{
					if(finalLevel[i] == '1')
						g.drawImage(gameOver1,i*gap,0,null);
					else if(finalLevel[i] == '2')
						g.drawImage(gameOver2,i*gap,0,null);
					else if(finalLevel[i] == '3')
						g.drawImage(gameOver3,i*gap,0,null);
					else if(finalLevel[i] == '4')
						g.drawImage(gameOver4,i*gap,0,null);
					else if(finalLevel[i] == '5')
						g.drawImage(gameOver5,i*gap,0,null);
					else if(finalLevel[i] == '6')
						g.drawImage(gameOver6,i*gap,0,null);
					else if(finalLevel[i] == '7')
						g.drawImage(gameOver7,i*gap,0,null);
					else if(finalLevel[i] == '8')
						g.drawImage(gameOver8,i*gap,0,null);
					else if(finalLevel[i] == '9')
						g.drawImage(gameOver9,i*gap,0,null);
					else if(finalLevel[i] == '0')
						g.drawImage(gameOver0,i*gap,0,null);
				}
				
				
			}
		}
		
	}
}

public static void main(String[]args)  
{  
 Core miloMadeThisGame=new Core();  
}  
}  