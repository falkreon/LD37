package endless.blue.oneroom;

import java.awt.BorderLayout;
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
		this.setMinimumSize(new Dimension(buf.getWidth(),buf.getHeight()));
	}
	
	public static class DisplayPanel extends JPanel {
		private final Display root;
		
		public DisplayPanel(Display root) {
			this.root = root;
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
			g.drawImage(root.buf, 0,0,this.getWidth(),this.getHeight(),this);
			
			this.repaint(10);
		}
	}
}
