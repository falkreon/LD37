package endless.blue.oneroom;

import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

public class FitnessObjective extends Mob {
	public BufferedImage letter;
	public BufferedImage book;
	public BufferedImage turnin;
	
	public FitnessObjective(BufferedImage letter, BufferedImage book, BufferedImage turnin, Clip ding) {
		this.letter = letter;
		this.book = book;
		this.turnin = turnin;
		this.hurtSound = ding;
		this.im = letter;
		this.x = 8;
		this.y = Room.HEIGHT-2;
	}
	
	public boolean harm(float amount) {
		if (amount==200) return super.harm(amount);
		return false; //I AM INVINCIBLE! unless I get hit with the specific marker amount that signals that I've been "consumed"
	}

	@Override
	public void physicsTick(Room room, Mob player) {
		super.physicsTick(room, player);
		
		
		if (this.distanceSquaredTo(player)<1.5f && this.health > 0) {
			
			
			if (this.im==letter) {
				this.hurtSound.stop();
				this.hurtSound.setFramePosition(0);
				this.hurtSound.start();
				
				//BECOME BOOK!
				this.im = book;
				this.x = (int)(Math.random()*(Room.WIDTH-1))+0.5f;
				this.y = (int)(Math.random()*(Room.HEIGHT-4))+0.5f;
				Display.tip = "Collect the book to look up the response!";
				
			} else if (this.im==book) {
				this.hurtSound.stop();
				this.hurtSound.setFramePosition(0);
				this.hurtSound.start();
				
				//BECOME TURNIN!
				this.im = turnin;
				this.x = 21;
				this.y = Room.HEIGHT-2;
				Display.tip = "Go to the hatch to turn in your reply!";
			} else {
				//turnin
				Display.score += 1000;
				Display.curObjective = null;
				this.harm(200);
				Display.tip = "Use X to fire your welding beam at your ineffective competitors!";
			}
			
		}
	}
}
