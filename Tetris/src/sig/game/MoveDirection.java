package sig.game;

public enum MoveDirection {
	LEFT(-1),
	RIGHT(1);
	
	int value;
	
	MoveDirection(int value) {
		this.value=value;
	}
}
