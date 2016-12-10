package endless.blue.oneroom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JFrame {
	public static Screen screen;
	private BufferedImage buf = new BufferedImage(640,360, BufferedImage.TYPE_INT_ARGB);
	
	public Display(String title) {
		super(title);
		this.add(new DisplayPanel(this), BorderLayout.CENTER);
		this.setBackground(Color.BLACK);
		
		//Best effort to account for the window's frame and junk
		this.setMinimumSize(new Dimension(buf.getWidth()+this.getInsets().left+this.getInsets().right,buf.getHeight()+this.getInsets().top+this.getInsets().bottom));
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
			
			screen.onStep();
			
			Graphics2D bufGraphics = root.buf.createGraphics();
			screen.onPaint(bufGraphics);
			bufGraphics.dispose();
			
			//TODO: set the width and height of the painted image to the biggest they can be while preserving the aspect ratio
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
			
			this.repaint(10);
		}
	}
}
