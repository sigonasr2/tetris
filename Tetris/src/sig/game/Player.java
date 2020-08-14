package sig.game;

import java.awt.Point;
import java.lang.reflect.Field;

import sig.utils.JavaUtils;
import sig.utils.ReflectUtils;

public class Player {
	Frame piece;
	Frame nextPiece;
	Point pos;
	
	Player() {
		piece = SelectRandomTetrimino();
		nextPiece = SelectRandomTetrimino();
		pos = new Point(5,20);
	}

	private Frame SelectRandomTetrimino() {
		return Game.piecePool[(int)(Math.random()*Game.piecePool.length)];
	}
	
	public void ShuffleNextPiece() {
		piece = nextPiece.clone();
		nextPiece = SelectRandomTetrimino();
		pos = new Point(5,20);
	}
	
	public Point[] GetPlayerBlocksInGrid() {
		Point[] checkPoints = new Point[4];
		Shape tetShape = piece.shape[Game.rotation];
		int pointsInserted=0;
		for (int x=0;x<tetShape.shape.length;x++) {
			for (int y=0;y<tetShape.shape[0].length;y++) {
				if (tetShape.shape[x][y].active) {
					Point point = new Point((x-2),-y);
					point.translate(pos.x,pos.y);
					checkPoints[pointsInserted++] = point;
				}
			}
		}
		return checkPoints;
	}
}
