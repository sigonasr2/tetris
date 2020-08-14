package sig.game;

import java.lang.reflect.Field;

import sig.utils.JavaUtils;
import sig.utils.ReflectUtils;

public class Frame {
	Shape[] shape;
	Color col;
	
	Frame(Shape s1,
			Shape s2,
			Shape s3,
			Shape s4,
			Color col) {
		shape = new Shape[]{s1,s2,s3,s4};
		this.col=col;
	}
	
	public Frame clone() {
		Frame newpos = new Frame(shape[0],shape[1],shape[2],shape[3],col);
		for (Field f : this.getClass().getDeclaredFields()) {
			if (ReflectUtils.isCloneable(f)) {
				try {
					f.set(newpos, f.get(this));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return newpos;
	}
}
