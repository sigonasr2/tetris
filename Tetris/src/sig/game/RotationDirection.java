package sig.game;

public enum RotationDirection {
	CLOCKWISE(1),
	COUNTERCLOCKWISE(-1);
	
	int value;
	
	RotationDirection(int value) {
		this.value=value;
	}
}
