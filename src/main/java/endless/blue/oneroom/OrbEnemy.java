package endless.blue.oneroom;

import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

public class OrbEnemy extends Enemy {
	public static final float TAU = (float)(2*Math.PI);
	float theta = 0f;
	
	public OrbEnemy(BufferedImage sprite, Clip hurtSound) {
		super(sprite, hurtSound);
		this.points = 25;
	}

	@Override
	public void wander(Room room) {
		theta+= 0.01f;
		if (theta>=TAU) theta-=TAU;
		
		int dx = (int)(Math.cos(theta)*2.0f);
		int dy = (int)(Math.sin(theta)*-2.0f);
		
		for(int i=0; i<Math.abs(dx); i++) {
			if (dx>0) nudgeRight(room);
			else nudgeLeft(room);
		}
		
		for(int i=0; i<Math.abs(dy); i++) {
			if (dy>0) nudgeDown(room);
			else nudgeUp(room);
		}
	}
}
