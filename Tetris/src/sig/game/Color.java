package sig.game;

public enum Color {
	BLUE(java.awt.Color.BLUE),
	AQUA(new java.awt.Color(196,196,255)),
	RED(java.awt.Color.RED),
	GREEN(java.awt.Color.GREEN),
	PURPLE(java.awt.Color.MAGENTA),
	YELLOW(java.awt.Color.YELLOW),
	ORANGE(java.awt.Color.ORANGE),
	BLACK(java.awt.Color.BLACK);
	
	java.awt.Color col;
	
	Color(java.awt.Color col) {
		this.col=col;
	}
	
	java.awt.Color getColor() {
		return col;
	}
}
