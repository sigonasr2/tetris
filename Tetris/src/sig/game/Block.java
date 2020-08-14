package sig.game;

public class Block {
	boolean active = false;
	Color color = Color.BLUE;
	
	Block(boolean active) {
		this(active,Color.BLACK);
	}
	Block(boolean active,Color col) {
		this.active=active;
		this.color=col;
	}
	public String toString() {
		return active?"O":"-";
	}
}
