package endless.blue.oneroom;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Mob {
	public float x = 1;
	public float y = 1;
	public BufferedImage im;
	
	public void paint(Graphics2D g) {
		if (im==null) return;
		
		int left = (int)(x*16) + 8 - (im.getWidth()/2);
		int top = (int)(y*16) + 15 - im.getHeight();
		
		g.drawImage(im, left, top, Display.OBSERVER);
	}
}
