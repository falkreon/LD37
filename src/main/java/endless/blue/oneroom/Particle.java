package endless.blue.oneroom;

import java.awt.image.BufferedImage;

public strictfp class Particle {
	public float x = 0;
	public float y = 0;
	public float vx = 0;
	public float vy = 0;
	/** X acceleration */
	public float vvx = 0;
	/** Y acceleration */
	public float vvy = 0;
	public int fuse = 0;
	public BufferedImage image;
	
	public Particle(BufferedImage image) {
		this(320,90,0,0,0,0,image);
	}
	
	public Particle(float x, float y, BufferedImage image) {
		this(x,y,0,0,0,0,image);
	}
	
	public Particle(float x, float y, float vx, float vy, BufferedImage image) {
		this(x,y,vx,vy,0,0,image);
	}
	
	public Particle(float x, float y, float vx, float vy, float vvx, float vvy, BufferedImage image) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.vvx = vvx;
		this.vvy = vvy;
		this.image = image;
		
		this.fuse = 150;
	}
}
