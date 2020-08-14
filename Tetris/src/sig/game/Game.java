package sig.game;

import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
	public static Frame[] piecePool;
	public static Player p;
	public static int[] levelDelay = new int[] {
			30,27,25,23,22,20,18,16,14,12,
			10,9,8,7,6,5,4,3,2,1
	}; 
	public static int level = 0;
	public static int tickDelay = 60;
	public static int rotation = 0; //0-3
	public static Grid gameGrid = new Grid();
	
	public static void main(String[] args) {
		
		final Frame LPiece = new Frame(
				new Shape(
						"XOXX"
					+	"XOXX"
					+	"XOOX"
					+	"XXXX"),
				new Shape(
						"XXXX"
					+	"OOOO"
					+	"OXXX"
					+	"XXXX"),
				new Shape(
						"XOOX"
					+	"XXOX"
					+	"XXOX"
					+	"XXXX"),
				new Shape(
						"XXXX"
					+	"XXXO"
					+	"OOOO"
					+	"XXXX")
				,Color.ORANGE);
		final Frame JPiece = new Frame(
				new Shape(
						"XXOX"
					+	"XXOX"
					+	"XOOX"
					+	"XXXX"),
				new Shape(
						"OXXX"
					+	"OOOO"
					+	"XXXX"
					+	"XXXX"),
				new Shape(
						"XXOX"
					+	"XXOX"
					+	"XOOX"
					+	"XXXX"),
				new Shape(
						"OOOO"
					+	"XXXO"
					+	"XXXX"
					+	"XXXX")
				,Color.BLUE);
		final Frame SPiece = new Frame(
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
		final Frame ZPiece = new Frame(
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
		final Frame TPiece = new Frame(
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
		final Frame OPiece = new Frame(
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
		final Frame IPiece = new Frame(
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
		
		piecePool = new Frame[] {LPiece,JPiece,SPiece,ZPiece,TPiece,OPiece,IPiece};
		
		p = new Player();
		
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
		if (tickDelay<=0) {
			moveBlock();
			tickDelay=levelDelay[level];
		}
		ClearScreen();
		Point[] checkPoints = p.GetPlayerBlocksInGrid();
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
					System.out.print((gameGrid.grid[x][y].active)?'O':'-');
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
		
		Point[] checkPoints = p.GetPlayerBlocksInGrid();
		
		boolean isOccupied = false;
		for (int i=0;i<checkPoints.length;i++) {
			Point point = checkPoints[i];
			//point.translate(p.pos.x,p.pos.y);
			if (point.y-1<0 || gameGrid.grid[point.x][point.y-1].active) {
				isOccupied=true;
				break;
			}
		}
		
		if (isOccupied) {
			for (Point point : checkPoints) {
				gameGrid.grid[point.x][point.y].active=true;
			}
			p.ShuffleNextPiece();
		} else {
			p.pos.translate(0, -1);
		}
	}
}
