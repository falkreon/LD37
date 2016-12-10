package endless.blue.oneroom;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.sound.sampled.Clip;

public class Mob {
	public static final int STANDARD_IFRAMES = 10;
	
	public float x = 1;
	public float y = 1;
	public float health = 20;
	public int overkill = 0;
	public int iframes = 0;
	public BufferedImage im;
	public Cardinal facing = Cardinal.EAST;
	
	public Clip hurtSound = null;
	public Clip attackSound = null;
	
	public void paint(Graphics2D g) {
		if (im==null) return;
		
		int left = (int)(x*16) + 8 - (im.getWidth()/2);
		int top = (int)(y*16) + 15 - im.getHeight();
		
		g.drawImage(im, left, top, Display.OBSERVER);
	}
	
	public float distanceSquaredTo(Mob m) {
		float xd = m.x-this.x;
		float yd = m.y-this.y;
		return xd*xd + yd*yd; //squaring removes any negatives before the addition
	}
	
	public float distanceSquaredTo(float x, float y) {
		float xd = x-this.x;
		float yd = y-this.y;
		return xd*xd + yd*yd;
	}
	
	public boolean nudgeForward(Room r) {
		switch(facing) {
		case NORTH: default: return nudgeUp(r);
		case EAST: return nudgeRight(r);
		case SOUTH: return nudgeDown(r);
		case WEST: return nudgeLeft(r);
		}
	}
	
	/** returns whether harm was done **/
	public boolean harm(float amt) {
		if (iframes>0) return false;
		
		if (hurtSound!=null) {
			hurtSound.stop();
			hurtSound.setFramePosition(0);
			hurtSound.start();
		}
		
		health -= amt;
		iframes = STANDARD_IFRAMES;
		if (health<0) {
			overkill = -(int)health;
			health = 0;
			//Fire off any death events?
		}
		return true;
	}
	
	public void melee(List<Mob> targets) {
		if (attackSound!=null) {
			attackSound.stop();
			attackSound.setFramePosition(0);
			attackSound.start();
		}
		
		float cx = x + facing.xofs();
		float cy = y + facing.yofs();
		float r2 = 1.5f;
		
		for(Mob target : targets) {
			if (this==target) continue; //Stop hitting yourself! (actual problem encountered)
			if (target.distanceSquaredTo(cx,cy)<r2) {
				target.harm(3);
			}
		}
	}
	
	public boolean nudgeLeft(Room r) {
		if (this.x==(int)this.x) {
			BlockType type = r.getTile((int)x-1, (int)y);
			if (type==BlockType.WALKABLE) {
				this.x -= Room.PIXEL;
			} else return false;
		} else {
			this.x -= Room.PIXEL;
		}
		return true;
	}
	
	public boolean nudgeRight(Room r) {
		if (this.x==(int)this.x) {
			BlockType type = r.getTile((int)x+1, (int)y);
			if (type==BlockType.WALKABLE) {
				this.x += Room.PIXEL;
			} else return false;
		} else {
			this.x += Room.PIXEL;
		}
		return true;
	}
	
	public boolean nudgeUp(Room r) {
		if (this.y==(int)this.y) {
			BlockType type = r.getTile((int)x, (int)y-1);
			if (type==BlockType.WALKABLE) {
				this.y -= Room.PIXEL;
			} else return false;
		} else {
			this.y -= Room.PIXEL;
		}
		return true;
	}
	
	public boolean nudgeDown(Room r) {
		if (this.y==(int)this.y) {
			BlockType type = r.getTile((int)x, (int)y+1);
			if (type==BlockType.WALKABLE) {
				this.y += Room.PIXEL;
			} else return false;
		} else {
			this.y += Room.PIXEL;
		}
		return true;
	}
	
	public void physicsTick(Room room, Mob player) {
		if (iframes>0) iframes--;
		
	}

	public int health() {
		return (int)health;
	}
}
