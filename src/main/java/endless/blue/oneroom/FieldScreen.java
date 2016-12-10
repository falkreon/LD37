package endless.blue.oneroom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;

public class FieldScreen implements Screen {
	private long lastRenderStart;
	private static final long targetRenderTicks = 10L;
	
	private BufferedImage blockImage;
	private BufferedImage robotRight;
	private BufferedImage robotLeft;
	
	private Room curRoom = new Room();
	
	private ArrayList<Mob> mobs = new ArrayList<>();
	
	private Mob robot = new Mob();
	
	private JButton observer = new JButton("Whatever.");
	Color bg = new Color(100,100,200);
	
	@Override
	public void onActivate() {
		blockImage = ResourceBroker.loadImage("image/block.png");
		robotRight = ResourceBroker.loadImage("image/robot_stand.png");
		robotLeft = ResourceBroker.getFlipped(robotRight);
		robot.im = robotRight;
		
		curRoom.addTile(blockImage);
	}

	@Override
	public void onDeactivate() {
		
	}

	@Override
	public void onPaint(Graphics2D g) {
		g.setColor(bg);
		g.fillRect(0, 0, 640, 360);
		g.drawImage(blockImage, 0, 16, observer);
		robot.paint(g);
	}

	@Override
	public void onStep() {
		long renderStart = System.currentTimeMillis();
		long renderTicks = renderStart-lastRenderStart;
		if (renderTicks < targetRenderTicks ) return;
		lastRenderStart = renderStart;
		
		if (Keyboard.isPressed("left")) {
			robot.im = robotLeft;
			robot.x -= 1/16f;
		}
		if (Keyboard.isPressed("right")) {
			robot.im = robotRight;
			robot.x += 1/16f;
		}
	}

	
	
}
