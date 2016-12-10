package endless.blue.oneroom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JButton;

public class FieldScreen implements Screen {
	private Image blockImage;
	private JButton observer = new JButton("Whatever.");
	Color bg = new Color(100,100,200);
	
	@Override
	public void onActivate() {
		blockImage = ResourceBroker.loadImage("image/block.png");
	}

	@Override
	public void onDeactivate() {
		
	}

	@Override
	public void onPaint(Graphics2D g) {
		g.setColor(bg);
		g.fillRect(1, 1, 638, 358);
		g.drawImage(blockImage, 10, 10, observer);
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		
	}

}
