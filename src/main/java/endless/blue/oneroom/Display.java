package endless.blue.oneroom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JFrame {
	public static ImageObserver OBSERVER;
	public static Screen screen;
	private BufferedImage buf = new BufferedImage(640,360, BufferedImage.TYPE_INT_ARGB);
	
	private long lastRenderStart;
	private static final long targetRenderTicks = 10L;
	
	private static Particle[] particles = new Particle[700];
	private static int maxParticle = 0; //Maximum particle index.
	
	public Display(String title) {
		super(title);
		OBSERVER = this;
		this.add(new DisplayPanel(this), BorderLayout.CENTER);
		this.setBackground(Color.BLACK);
		
		//Best effort to account for the window's frame and junk
		this.setMinimumSize(new Dimension(buf.getWidth()+this.getInsets().left+this.getInsets().right,buf.getHeight()+this.getInsets().top+this.getInsets().bottom+32));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addKeyListener(Keyboard.getInstance());
		
		
	}
	
	public static class DisplayPanel extends JPanel {
		private final Display root;
		
		public DisplayPanel(Display root) {
			this.root = root;
			this.setBackground(Color.BLACK);
		}
		
		@Override
		public void paint(Graphics g) {
			if (screen==null || !(g instanceof Graphics2D)) {
				super.paint(g);
				return;
			}
			
			long renderStart = System.currentTimeMillis();
			long renderTicks = renderStart-root.lastRenderStart;
			if (renderTicks >= targetRenderTicks ) {
				screen.onStep();
				root.moveParticles();
				root.lastRenderStart = renderStart;
			}
			
			Graphics2D bufGraphics = root.buf.createGraphics();
			screen.onPaint(bufGraphics);
			root.drawParticles(bufGraphics);
			bufGraphics.dispose();
			
			float bufAspect = (float)root.buf.getWidth()/(float)root.buf.getHeight();
			float selfAspect = (float)this.getWidth()/(float)this.getHeight();
			
			int targetWidth = root.buf.getWidth();
			int targetHeight = root.buf.getHeight();
			
			if (selfAspect>bufAspect) {
				//we're relatively wider than the buffer, so we need to pillarbox.
				//make the screen as tall as possible
				targetHeight = this.getHeight();
				targetWidth = (int)(targetHeight*bufAspect);
			} else {
				//we're relatively taller than the buffer, so we need to letterbox.
				//make the screen as wide as possible
				targetWidth = this.getWidth();
				targetHeight = (int)(targetWidth/bufAspect);
			}
			
			int left = (this.getWidth()-targetWidth)/2;
			int top = (this.getHeight()-targetHeight)/2;
			
			g.drawImage(root.buf, left,top,targetWidth,targetHeight,this);
			
			g.setColor(Color.BLACK);
			g.drawString("Particles: "+root.maxParticle, 4, 16);
			g.setColor(Color.WHITE);
			g.drawString("Particles: "+root.maxParticle, 5, 17);
			
			this.repaint(10);
		}
	}
	
	public void moveParticles() {
		for(int i=0; i<maxParticle; i++) {
			if (i==maxParticle-1 && particles[i]==null) maxParticle--; //mandatory
			
			Particle cur = particles[i];
			//skip dead particles
			if (cur==null) continue;
			
			//burn the fuse down
			if (cur.fuse>0) cur.fuse--;
			if (cur.fuse<=0) {
				particles[i]=null;
				continue;
			}
			
			//calculate trajectory
			cur.vx += cur.vvx;
			cur.x += cur.vx;
			
			cur.vy += cur.vvy;
			cur.y += cur.vy;
			
			//Compact left if possible. This will shift the whole stack left up to 1 entry per tick!
			if (i>0) {
				if (particles[i-1]==null) {
					particles[i-1] = particles[i];
					particles[i] = null;
					//if (i==maxParticle-1) maxParticle--; //optional
				}
			}
			
			
		}
	}
	
	public void drawParticles(Graphics2D g) {
		for(int i=0; i<maxParticle; i++) {
			Particle cur = particles[i];
			//skip dead particles
			if (cur==null || cur.fuse==0) continue;
			
			g.drawImage(cur.image, (int)cur.x-(cur.image.getWidth()/2), (int)cur.y-(cur.image.getHeight()/2), OBSERVER);
		}
	}
	
	public static void addParticle(Particle p) {
		if (maxParticle>=particles.length) return;
		particles[maxParticle] = p;
		maxParticle++;
	}
	
	
}
