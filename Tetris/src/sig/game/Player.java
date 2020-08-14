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
	
	public void ShuffleNextPiece() {
		piece = nextPiece.clone();
		nextPiece = SelectRandomTetrimino();
		pos = new Point(5,20);
	}
	
	public Point[] GetPlayerBlocksInGrid(Shape s) {
		Point[] checkPoints = new Point[4];
		Shape tetShape = s;
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
	
	public Shape getCurrentShape() {
		return piece.shape[Game.rotation];
	}
	
	public void rotateClockwise() {
		//Get the new rotated piece.
		//See if the current piece position with the new rotation shape overlaps any active squares.
		//If they do, try to shift the piece one left, and compare again.
		//If they still do, try to shift the piece one right, and compare again.
		//If no piece position is free, we cannot rotate. Return.
		//Otherwise, all conditions have been met, and we can rotate the piece.
		Point[] checkPoints = GetPlayerBlocksInGrid(GetClockwiseRotatedCurrentPiece());
		RotateIfSpaceIsFree(checkPoints,RotationDirection.CLOCKWISE);
	}
	
	public void rotateCounterClockwise() {
		Point[] checkPoints = GetPlayerBlocksInGrid(GetCounterClockwiseRotatedCurrentPiece());
		RotateIfSpaceIsFree(checkPoints,RotationDirection.COUNTERCLOCKWISE);
	}
	
	public void moveBlockRight() {
		moveBlock(MoveDirection.RIGHT);
	}
	public void moveBlockLeft() {
		moveBlock(MoveDirection.LEFT);
	}
	
	
	
	
	
	
	
	private void moveBlock(MoveDirection dir) {
		Point[] checkPoints = GetPlayerBlocksInGrid(getCurrentShape());
		boolean isFree = true;
		for (Point p : checkPoints) {
			if (Game.isOccupied(checkPoints,new Point(dir.value,0))) {
				isFree=false;
				break;
			}
		}
		if (isFree) {
			pos.translate(dir.value, 0);
		}
	}

	private Frame SelectRandomTetrimino() {
		return Game.piecePool[(int)(Math.random()*Game.piecePool.length)];
	}

	private void RotateIfSpaceIsFree(Point[] checkPoints,RotationDirection dir) {
		int[] offset = new int[]{0,-1,1};
		int freeOffset = -1;
		for (int i=0;i<offset.length;i++) {
			if (!Game.isOccupied(checkPoints,new Point(offset[i],0))) {
				freeOffset = offset[i];
				break;
			}
		}
		if (freeOffset!=-1) {
			pos.translate(freeOffset, 0);
			Game.rotation=rotatePiece(Game.rotation,dir);
			//System.out.println("rotation: "+Game.rotation);
		}
	}

	private Shape GetClockwiseRotatedCurrentPiece() {
		return piece.shape[rotatePiece(Game.rotation,RotationDirection.CLOCKWISE)];
	}
	private Shape GetCounterClockwiseRotatedCurrentPiece() {
		return piece.shape[rotatePiece(Game.rotation,RotationDirection.COUNTERCLOCKWISE)];
	}
	
	private int rotatePiece(int currentRotation,RotationDirection direction) {
		currentRotation+=direction.value;
		if (currentRotation<0) {
			currentRotation=3;
		} else
		if (currentRotation>3) {
			currentRotation=0;
		}
		return currentRotation;
	}
}
