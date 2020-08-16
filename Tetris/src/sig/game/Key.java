package sig.game;

public class Key {
	boolean pressed;
	int timer;
	public Key(boolean pressed) {
		this.pressed=pressed;
		this.timer=0;
	}
	public Key updatePress(boolean pressed) {
		this.pressed=pressed;
		return this;
	}
	public Key setTimer(int timer) {
		this.timer=timer;
		return this;
	}
}
