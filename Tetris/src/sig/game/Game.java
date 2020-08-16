package sig.game;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Game {
	public static Frame[] piecePool;
	public static Player p;
	public static int[] levelDelay = new int[] {
			30,27,25,23,22,20,18,16,14,12,
			10,9,8,7,6,5,4,3,2,1
	}; 
	
	public static int linesCleared = 0;
	
	public static int level = 0;
	public static int tickDelay = 60;
	public static int rotation = 0; //0-3
	public static Grid gameGrid = new Grid();
	
	public static int keyDelay = 0;
	public final static int defaultKeyDelay = 4;

	public static int rotateDelay = 20;
	public static int rotateTimer = rotateDelay;
	
	public static GameState state = GameState.PLAYING;
	
	public static JFrame frame;
	public static GameCanvas canvas;
	
	public static int lastKey = -1;
	
	public static HashMap<Integer,Key> keyMap = new HashMap<>();
	
	final public static Frame LPiece = new Frame(
			new Shape(
					"XOXX"
				+	"XOXX"
				+	"XOOX"
				+	"XXXX"),
			new Shape(
					"XXXX"
				+	"XOOO"
				+	"XOXX"
				+	"XXXX"),
			new Shape(
					"XOOX"
				+	"XXOX"
				+	"XXOX"
				+	"XXXX"),
			new Shape(
					"XXXX"
				+	"XXOX"
				+	"OOOX"
				+	"XXXX")
			,Color.ORANGE);
	final public static Frame JPiece = new Frame(
			new Shape(
					"XXOX"
				+	"XXOX"
				+	"XOOX"
				+	"XXXX"),
			new Shape(
					"XOXX"
				+	"XOOO"
				+	"XXXX"
				+	"XXXX"),
			new Shape(
					"XOOX"
				+	"XOXX"
				+	"XOXX"
				+	"XXXX"),
			new Shape(
					"OOOX"
				+	"XXOX"
				+	"XXXX"
				+	"XXXX")
			,Color.BLUE);
	final public static Frame SPiece = new Frame(
			new Shape(
					"XOXX"
				+	"XOOX"
				+	"XXOX"
				+	"XXXX"),
			new Shape(
					"XOOX"
				+	"OOXX"
				+	"XXXX"
				+	"XXXX"),
			new Shape(
					"XOXX"
				+	"XOOX"
				+	"XXOX"
				+	"XXXX"),
			new Shape(
					"XOOX"
				+	"OOXX"
				+	"XXXX"
				+	"XXXX")
			,Color.RED);
	final public static Frame ZPiece = new Frame(
			new Shape(
					"XXOX"
				+	"XOOX"
				+	"XOXX"
				+	"XXXX"),
			new Shape(
					"OOXX"
				+	"XOOX"
				+	"XXXX"
				+	"XXXX"),
			new Shape(
					"XXOX"
				+	"XOOX"
				+	"XOXX"
				+	"XXXX"),
			new Shape(
					"OOXX"
				+	"XOOX"
				+	"XXXX"
				+	"XXXX")
			,Color.GREEN);
	final public static Frame TPiece = new Frame(
			new Shape(
					"OOOX"
				+	"XOXX"
				+	"XXXX"
				+	"XXXX"),
			new Shape(
					"XOXX"
				+	"OOXX"
				+	"XOXX"
				+	"XXXX"),
			new Shape(
					"XOXX"
				+	"OOOX"
				+	"XXXX"
				+	"XXXX"),
			new Shape(
					"XOXX"
				+	"XOOX"
				+	"XOXX"
				+	"XXXX")
			,Color.PURPLE);
	final public static Frame OPiece = new Frame(
			new Shape(
					"XOOX"
				+	"XOOX"
				+	"XXXX"
				+	"XXXX"),
			new Shape(
					"XOOX"
				+	"XOOX"
				+	"XXXX"
				+	"XXXX"),
			new Shape(
					"XOOX"
				+	"XOOX"
				+	"XXXX"
				+	"XXXX"),
			new Shape(
					"XOOX"
				+	"XOOX"
				+	"XXXX"
				+	"XXXX")
			,Color.YELLOW);
	final public static Frame IPiece = new Frame(
			new Shape(
					"XXOX"
				+	"XXOX"
				+	"XXOX"
				+	"XXOX"),
			new Shape(
					"OOOO"
				+	"XXXX"
				+	"XXXX"
				+	"XXXX"),
			new Shape(
					"XXOX"
				+	"XXOX"
				+	"XXOX"
				+	"XXOX"),
			new Shape(
					"OOOO"
				+	"XXXX"
				+	"XXXX"
				+	"XXXX")
			,Color.AQUA);
	
	public static void main(String[] args) {
		
		piecePool = new Frame[] {LPiece,JPiece,SPiece,ZPiece,TPiece,OPiece,IPiece};
		
		p = new Player();
		
		frame = new JFrame();
		canvas = new GameCanvas();
		frame.addKeyListener(canvas);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(1024, 576);
		frame.setTitle("My Tetris Game");
		frame.add(canvas);
		frame.setResizable(false);
		frame.setVisible(true);
		
		TimerTask stepTask = new TimerTask() {

			@Override
			public void run() {
				step();
			}
			
		};
		Timer t = new Timer();
		
		t.schedule(stepTask, 0, (long)Math.floor(1000/60f));
	}
	
	static void step() {
		tickDelay--;
		//rotateTimer--;
		if (state!=GameState.LOSE) {
			if (tickDelay<=0) {
				moveBlock();
				tickDelay=levelDelay[level];
			}
			/*if (rotateTimer<=0) {
				//p.rotateCounterClockwise();
				p.moveBlockLeft();
				rotateTimer=rotateDelay;
			}*/
			HandleKeys();
		}
		DrawBoard();
	}

	private static void HandleKeys() {
			if (AreKeysHeldDown(KeyEvent.VK_LEFT,KeyEvent.VK_A)) {
				Game.p.moveBlockLeft();
				UpdateKeyDelay(KeyEvent.VK_LEFT,KeyEvent.VK_A);
			}
			if (AreKeysHeldDown(KeyEvent.VK_RIGHT,KeyEvent.VK_D)) {
				Game.p.moveBlockRight();
				UpdateKeyDelay(KeyEvent.VK_RIGHT,KeyEvent.VK_D);
			}
			if (AreKeysHeldDown(KeyEvent.VK_S,KeyEvent.VK_DOWN)) {
				Game.tickDelay=1;
				UpdateKeyDelay(KeyEvent.VK_S,KeyEvent.VK_DOWN);
			}
	}

	private static void UpdateKeyDelay(int...keys) {
		for (int i=0;i<keys.length;i++) {
			if (keyMap.containsKey(keys[i])) {
				keyMap.get(keys[i]).setTimer(Game.defaultKeyDelay);
			}
		}
	}

	private static boolean AreKeysHeldDown(Integer...keys) {
		for (int i=0;i<keys.length;i++) {
			if (keyMap.containsKey(keys[i])&&keyMap.get(keys[i]).pressed
					&&keyMap.get(keys[i]).timer<=0) {
				return true;
			} else {
				if (keyMap.containsKey(keys[i])) {
					keyMap.get(keys[i]).setTimer(keyMap.get(keys[i]).timer-1);
				}
			}
		}
		return false;
	}

	private static void DrawBoard() {
		canvas.repaint();
		//DrawConsoleBoard();
	}

	private static void DrawConsoleBoard() {
		ClearScreen();
		Point[] checkPoints = p.GetPlayerBlocksInGrid(p.getCurrentShape(),p.pos);
		for (int y=19;y>=0;y--) {
			for (int x=0;x<10;x++) {
				boolean plotted=false;
				for (Point point : checkPoints) {
					if (point.x==x && point.y==y) {
						plotted=true;
						System.out.print('P');
						break;
					}
				}
				if (!plotted) {
					System.out.print(gameGrid.grid[x][y]);
				}
			}
			System.out.println();
		}
	}

	private static void ClearScreen() {
		for (int i=0;i<20;i++) {
			System.out.println("");
		}
	}

	private static void moveBlock() {
		//Check all squares that the tetrimino will occupy.
		//If any squares come in contact with an active square, this block will "snap"
		//And all those squares become active on the grid.
		//Then, update the current piece for the player to the next piece, randomize the next piece, and set the player position back to 5,20.
		
		///System.out.println(tetShape.shape);
		/*for (int i=0;i<tetShape.shapeStr.length();i++) {
			if (tetShape.shapeStr.charAt(i)=='O') {
				checkPoints[pointsInserted++] = new Point((i%4)-2,-(i/4));
			}
		}*/
		
		Point[] checkPoints = p.GetPlayerBlocksInGrid(p.getCurrentShape(),p.pos);
		
		boolean isOccupied = isOccupied(checkPoints,new Point(0,-1));
		
		if (isOccupied) {
			SnapPieceThatCollided(checkPoints);
			ClearLinesThatHaveFullRows();
		} else {
			MovePlayerBlockDownByOne();
		}
	}

	private static void ClearLinesThatHaveFullRows() {
		int deletionRows=0;
		for (int y=0;y<gameGrid.grid[0].length;y++) {
			//System.out.println("Called: "+y);
			deletionRows = CheckAndClearFullRows(deletionRows, y);
		}
		linesCleared+=deletionRows;
		if (linesCleared/10>level && level<19) {
			level=linesCleared/10;
		}
	}

	private static int CheckAndClearFullRows(int deletionRows, int y) {
		boolean isAllActive=true;
		for (int x=0;x<gameGrid.grid.length;x++) {
			//System.out.print(gameGrid.grid[x][y]);
			if (!gameGrid.grid[x][y].active) {
				isAllActive=false;
				break;
			}
		}
		//System.out.println();
		if (isAllActive) {
			//Take every block above this row, and shift it all down by one.
			//x is the current row we are on.
			//Move every row down, if we find a row that is also a match, increment the amount of rows we move down by 1.
			deletionRows++;
		} else {
			for (int x=0;x<gameGrid.grid.length;x++) {
				ShiftBlockDownByNumberOfDeletionRows(deletionRows, y, x);
			}
		}
		return deletionRows;
	}

	private static void ShiftBlockDownByNumberOfDeletionRows(int deletionRows, int y, int x) {
		if (deletionRows>0) {
			gameGrid.grid[x][y-deletionRows].active=gameGrid.grid[x][y].active;
			gameGrid.grid[x][y-deletionRows].color=gameGrid.grid[x][y].color;
			gameGrid.grid[x][y].active=false;
			gameGrid.grid[x][y].color=Color.BLACK;
		}
	}

	private static void MovePlayerBlockDownByOne() {
		p.pos.translate(0, -1);
	}

	private static void SnapPieceThatCollided(Point[] checkPoints) {
		for (Point point : checkPoints) {
			//If any point in the grid is outside, this is our lose condition.
			if (point.y>=20) {
				state = GameState.LOSE;
			} else {
				gameGrid.grid[point.x][point.y].active=true;
				gameGrid.grid[point.x][point.y].color=p.getCurrentShape().col;
			}
		}
		p.ShuffleNextPiece();
	}

	static boolean isOccupied(Point[] checkPoints) {
		return isOccupied(checkPoints,new Point(0,0));
	}
	static boolean isOccupied(Point[] checkPoints,Point offset) {
		boolean isOccupied = false;
		for (int i=0;i<checkPoints.length;i++) {
			Point point = checkPoints[i];
			//point.translate(p.pos.x,p.pos.y);
			//System.out.println((point.x+offset.x)+"/"+(point.y+offset.y));
			if (point.y+offset.y<0 ||  
					point.y+offset.y>=gameGrid.grid[0].length||  
					point.x+offset.x>=gameGrid.grid.length||  
					point.x+offset.x<0
					|| gameGrid.grid[point.x+offset.x][point.y+offset.y].active) {
				isOccupied=true;
				break;
			}
			//System.out.println("Pass");
		}
		return isOccupied;
	}
}
