package endless.blue.oneroom;

import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

public class HealthPack extends Mob {
	public HealthPack(BufferedImage sprite, Clip hurtSound) {
		this.curFrame = sprite;
		this.hurtSound = hurtSound;
		this.x = (int)(Math.random()*Room.WIDTH);
		this.y = (int)(Math.random()*Room.HEIGHT);
	}
	
	public boolean harm(float amount) {
		if (amount==200) {
			return super.harm(amount);
		} else return false;
	}

	@Override
	public void physicsTick(Room room, Mob player) {
		super.physicsTick(room, player);
		
		
		if (this.distanceSquaredTo(player)<1.5f && this.health > 0) {
			this.harm(200);
			player.health += 4;
			if (player.health > 20) player.health = 20;
		}
	}
}
