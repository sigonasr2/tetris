package sig.game;

public class Shape {
	/*Block[][] shape = new Block[][]{
		new Block[] {new Block(false),new Block(false),new Block(false),new Block(false),},
		new Block[] {new Block(false),new Block(false),new Block(false),new Block(false),},
		new Block[] {new Block(false),new Block(false),new Block(false),new Block(false),},
		new Block[] {new Block(false),new Block(false),new Block(false),new Block(false),},
	};*/
	Block[][] shape;
	Color col;
	Shape(String shapeStr) {
		Block[][] shapeArr = new Block[4][4];
		for (int i=0;i<shapeStr.length();i++) {
			shapeArr[i/4][i%4]= new Block(shapeStr.charAt(i)=='O');
		}
		shape = shapeArr;
		col = shapeArr[0][0].color;
	}
}
