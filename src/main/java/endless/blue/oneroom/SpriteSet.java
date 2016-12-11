package endless.blue.oneroom;

import java.awt.image.BufferedImage;

public class SpriteSet {
	private int step;
	private int delay;
	private int maxDelay = 1;
	private Cardinal facing = Cardinal.EAST;
	
	private BufferedImage[] northFrames;
	private BufferedImage[] eastFrames;
	private BufferedImage[] southFrames;
	private BufferedImage[] westFrames;
	
	public SpriteSet() {}
	
	public SpriteSet(BufferedImage... images) {
		if (images.length<4) {
			setStaticFrame(images[0]);
		} else {
			setStaticFrames(images[0], images[1], images[2], images[3]);
		}
	}
	
	public void step() {
		if (delay>0) delay--;
		if (delay<=0) {
			step++;
			
			fixFacing();
			
			delay = maxDelay;
		}
	}
	
	public void setStaticFrame(BufferedImage frame) {
		northFrames = new BufferedImage[]{ frame };
		eastFrames  = northFrames;
		southFrames = northFrames;
		westFrames  = northFrames;
	}
	
	public void setStaticFrames(BufferedImage north, BufferedImage east, BufferedImage south, BufferedImage west) {
		northFrames = new BufferedImage[]{ north };
		eastFrames  = new BufferedImage[]{ east  };
		southFrames = new BufferedImage[]{ south };
		westFrames  = new BufferedImage[]{ west  };
	}
	
	public void setFacing(Cardinal facing) {
		this.facing = facing;
		fixFacing();
	}
	
	public BufferedImage getFrame() {
		switch(facing) {
		case NORTH: default: return northFrames[step];
		case EAST: return eastFrames[step];
		case SOUTH: return southFrames[step];
		case WEST: return westFrames[step];
		}
	}
	
	private void fixFacing() {
		switch(facing) {
		case NORTH: default: if (step>=northFrames.length) step=0;
		case EAST: if (step>=eastFrames.length) step=0;
		case SOUTH: if (step>=southFrames.length) step=0;
		case WEST: if (step>=southFrames.length) step=0;
		}
	}
	
	public SpriteSet clone() {
		SpriteSet result = new SpriteSet();
		result.step = 0;
		result.delay = 0;
		result.maxDelay = this.delay;
		result.northFrames = this.northFrames;
		result.southFrames = this.southFrames;
		result.eastFrames = this.eastFrames;
		result.westFrames = this.westFrames;
		
		return result;
	}
}
