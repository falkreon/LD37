package endless.blue.oneroom.enemy;

import endless.blue.oneroom.Room;

public class OrbEnemy extends Enemy {
	public static final float TAU = (float)(2*Math.PI);
	float theta = 0f;
	float dtheta = 0.01f;
	
	public OrbEnemy() {
		super(Enemies.IMAGE_ORB_POSITIVE, Enemies.SOUND_ROOMBA_HURT);
		dtheta = 0.01f;
		
		if (Math.random()<0.5f) {
			curFrame = Enemies.IMAGE_ORB_NEGATIVE;
			dtheta = -0.01f;
		}
		
		this.points = 25;
	}

	@Override
	public void wander(Room room) {
		theta+= dtheta;
		if (theta>=TAU) theta-=TAU;
		if (theta<0) theta+=TAU;
		
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
