package endless.blue.oneroom;

import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

public class Enemy extends Mob {
	
	public Enemy(BufferedImage sprite, Clip hurtSound) {
		this.im = sprite;
		this.hurtSound = hurtSound;
		this.x = (int)(Math.random()*Room.WIDTH);
		this.y = (int)(Math.random()*Room.HEIGHT);
	}
	
	@Override
	public void physicsTick(Room room, Mob player) {
		super.physicsTick(room, player);
		wander(room);
		
		if (this.distanceSquaredTo(player)<1.0f) player.harm(1);
	}
	
	public void wander(Room room) {
		if (!nudgeForward(room)) {
			this.facing = Cardinal.values()[(int)(Math.random()*4)];
		}
	}
}
