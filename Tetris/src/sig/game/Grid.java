package sig.game;

import java.util.Arrays;

public class Grid {
	
	final int GRID_WIDTH = 10;
	final int GRID_HEIGHT = 20;
	
	Block[][] grid = new Block[GRID_WIDTH][GRID_HEIGHT];

	Grid() {
		for (int i=0;i<GRID_WIDTH;i++) {
			for (int j=0;j<GRID_HEIGHT;j++) {
				grid[i][j] = new Block(false,Color.BLACK);
			}
		}
	}
}
