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
}
