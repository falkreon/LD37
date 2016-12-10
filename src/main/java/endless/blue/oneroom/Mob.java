package endless.blue.oneroom;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Mob {
	public float x = 1;
	public float y = 1;
	public BufferedImage im;
	public Cardinal facing;
	
	public void paint(Graphics2D g) {
		if (im==null) return;
		
		int left = (int)(x*16) + 8 - (im.getWidth()/2);
		int top = (int)(y*16) + 15 - im.getHeight();
		
		g.drawImage(im, left, top, Display.OBSERVER);
	}
	
	public void nudgeLeft(Room r) {
		if (this.x==(int)this.x) {
			BlockType type = r.getTile((int)x-1, (int)y);
			if (type==BlockType.WALKABLE) {
				this.x -= Room.PIXEL;
			}
		} else {
			this.x -= Room.PIXEL;
		}
	}
	
	public void nudgeRight(Room r) {
		if (this.x==(int)this.x) {
			BlockType type = r.getTile((int)x+1, (int)y);
			if (type==BlockType.WALKABLE) {
				this.x += Room.PIXEL;
			}
		} else {
			this.x += Room.PIXEL;
		}
	}
	
	public void nudgeUp(Room r) {
		if (this.y==(int)this.y) {
			BlockType type = r.getTile((int)x, (int)y-1);
			if (type==BlockType.WALKABLE) {
				this.y -= Room.PIXEL;
			}
		} else {
			this.y -= Room.PIXEL;
		}
	}
	
	public void nudgeDown(Room r) {
		if (this.y==(int)this.y) {
			BlockType type = r.getTile((int)x, (int)y+1);
			if (type==BlockType.WALKABLE) {
				this.y += Room.PIXEL;
			}
		} else {
			this.y += Room.PIXEL;
		}
	}
}
