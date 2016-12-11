package endless.blue.oneroom.enemy;

import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

import endless.blue.oneroom.Mob;
import endless.blue.oneroom.ResourceBroker;
import endless.blue.oneroom.SpriteSet;

public class Enemies {
	public static SpriteSet SPRITE_ROOMBA = new SpriteSet(
			ResourceBroker.diceImage(ResourceBroker.loadImage("image/roomba.png"), 16, 15)
			);
	
	public static BufferedImage IMAGE_ROOMBA_ELITE = ResourceBroker.loadImage("image/roomba_elite.png");
	
	public static BufferedImage IMAGE_ORB_POSITIVE    = ResourceBroker.loadImage("image/orb.png");
	public static BufferedImage IMAGE_ORB_NEGATIVE    = ResourceBroker.loadImage("image/orb_negative.png");
	
	public static Clip SOUND_ROOMBA_HURT  = ResourceBroker.loadSound("sound/hurt_roomba.wav");
}
