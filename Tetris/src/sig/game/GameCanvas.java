package sig.game;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import sig.utils.DrawUtils;

public class GameCanvas extends JPanel implements KeyListener{
	public final static Font displayText = new Font("Consolas",Font.BOLD,24);
	
	public final static int WINDOW_WIDTH = 1024;
	public final static int WINDOW_HEIGHT = 576;
	
	public final static int BLOCK_SIZE = 24;
	public final static int FIELD_WIDTH = BLOCK_SIZE*10;
	public final static int FIELD_HEIGHT = BLOCK_SIZE*20;
	
	public final static int FIELD_STARTX = WINDOW_WIDTH/2-FIELD_WIDTH/2;
	public final static int FIELD_STARTY = WINDOW_HEIGHT/2+FIELD_HEIGHT/2;
	
	public final static int LINES_CLEARED_DISPLAYX = WINDOW_WIDTH/2+180;
	public final static int LINES_CLEARED_DISPLAYY = WINDOW_HEIGHT/2;
	
	public final static int LEVEL_DISPLAYX = WINDOW_WIDTH/2+180;
	public final static int LEVEL_DISPLAYY = LINES_CLEARED_DISPLAYY-64;
	
	public final static int NEXTPIECE_DISPLAYX = WINDOW_WIDTH/2+180;
	public final static int NEXTPIECE_DISPLAYY = LEVEL_DISPLAYY-128;
	
	public void paintComponent(Graphics g) {
		g.setColor(java.awt.Color.DARK_GRAY);
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		//System.out.println("Redrawn");
		Point[] checkPoints = Game.p.GetPlayerBlocksInGrid(Game.p.getCurrentShape(),Game.p.pos);
		for (int y=0;y<20;y++) {
			for (int x=0;x<10;x++) {
				boolean plotted=false;
				for (Point point : checkPoints) {
					if (point.x==x && point.y==y) {
						plotted=true;
						//System.out.print('P');
						g.setColor((Game.state==GameState.LOSE)
								?java.awt.Color.LIGHT_GRAY:
								Game.p.getCurrentShape().col.getColor());
						Point blockCoord=getBlockCoordinateToPixelCoordinate(point);
						g.fill3DRect(blockCoord.x, blockCoord.y, BLOCK_SIZE, BLOCK_SIZE, false);
						break;
					}
				}
				if (!plotted) {
					g.setColor((Game.state==GameState.LOSE&&Game.gameGrid.grid[x][y].active)
							?java.awt.Color.LIGHT_GRAY:Game.gameGrid.grid[x][y].color.getColor());
					Point blockCoord=getBlockCoordinateToPixelCoordinate(new Point(x,y));
					g.fill3DRect(blockCoord.x, blockCoord.y, BLOCK_SIZE, BLOCK_SIZE, false);
				}
			}
		}
		Point[] nextPoints = Game.p.GetPlayerBlocksInGrid(Game.p.getNextShape(),new Point(0,0));

		g.setColor(java.awt.Color.BLACK);
		g.fill3DRect(NEXTPIECE_DISPLAYX, NEXTPIECE_DISPLAYY, 104, 104, true);
		for (Point p : nextPoints) {
			g.setColor(Game.p.getNextShape().col.getColor());
			g.fill3DRect(p.x*BLOCK_SIZE+NEXTPIECE_DISPLAYX+52, p.y*BLOCK_SIZE+NEXTPIECE_DISPLAYY+52, BLOCK_SIZE, BLOCK_SIZE, false);
		}
		g.setColor(java.awt.Color.WHITE);
		g.setFont(displayText);
		g.drawString("NEXT",NEXTPIECE_DISPLAYX,NEXTPIECE_DISPLAYY);
		g.setColor(java.awt.Color.WHITE);
		g.setFont(displayText);
		g.drawString("LINES",LINES_CLEARED_DISPLAYX,LINES_CLEARED_DISPLAYY);
		g.drawString(Integer.toString(Game.linesCleared),LINES_CLEARED_DISPLAYX+6,LINES_CLEARED_DISPLAYY+24);
		g.drawString("LEVEL",LEVEL_DISPLAYX,LEVEL_DISPLAYY);
		g.drawString(Integer.toString(Game.level+1),LEVEL_DISPLAYX+6,LEVEL_DISPLAYY+24);
		if (Game.state==GameState.LOSE) {
			DrawUtils.drawCenteredText(g, g.getFont(),this, WINDOW_WIDTH/2, WINDOW_HEIGHT/2, java.awt.Color.WHITE, "Press R to Restart!");
		}
	}
	
	public Point getBlockCoordinateToPixelCoordinate(Point blockLoc) {
		return new Point(FIELD_STARTX+BLOCK_SIZE*blockLoc.x,FIELD_STARTY-BLOCK_SIZE*blockLoc.y);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		List<Integer> keys = Arrays.asList(
				KeyEvent.VK_LEFT,KeyEvent.VK_A,
				KeyEvent.VK_RIGHT,KeyEvent.VK_D,
				KeyEvent.VK_S,KeyEvent.VK_DOWN
				);
		Game.lastKey=e.getKeyCode();
		/*if ((Game.keyMap.containsKey(e.getKeyCode())&&!Game.keyMap.get(e.getKeyCode())&&keys.contains(e.getKeyCode())
				)||!Game.keyMap.containsKey(e.getKeyCode())) {
			System.out.println("Executing");
			Game.keyDelay=20;
		}*/
		if (Game.keyMap.containsKey(e.getKeyCode())) {
			Game.keyMap.put(e.getKeyCode(),Game.keyMap.get(e.getKeyCode()).updatePress(true));
		} else {
			Game.keyMap.put(e.getKeyCode(),new Key(true));
		}
		if (e.getKeyCode()==KeyEvent.VK_Z||e.getKeyCode()==KeyEvent.VK_COMMA) {
			Game.p.rotateClockwise();
		}
		if (e.getKeyCode()==KeyEvent.VK_X||e.getKeyCode()==KeyEvent.VK_PERIOD) {
			Game.p.rotateCounterClockwise();
		}
		if (e.getKeyCode()==KeyEvent.VK_R && Game.state==GameState.LOSE) {
			Game.p = new Player();
			Game.gameGrid = new Grid();
			Game.level=0;
			Game.linesCleared=0;
			Game.state=GameState.PLAYING;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Game.keyMap.put(e.getKeyCode(),Game.keyMap.get(e.getKeyCode()).updatePress(false));
	}
}
