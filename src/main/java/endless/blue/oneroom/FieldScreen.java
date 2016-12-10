package endless.blue.oneroom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;

public class FieldScreen implements Screen {
	
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
		if (Keyboard.isPressed("left")) {
			robot.im = robotLeft;
			robot.x -= 1/16f;
			
			if (Math.random()*4 < 1) {
				spawnMobParticle(robot);
				/*Display.addParticle(new Particle(
						robot.x*16, robot.y*16,
						(float)(Math.random()*1.0f - 0.5f), (float)(Math.random()*1.0f - 0.5f) - 3f,
						0f, 0.05f,
						blockImage
						));*/
			}
		}
		if (Keyboard.isPressed("right")) {
			robot.im = robotRight;
			robot.x += 1/16f;
			
			if (Math.random()*4 < 1) {
				spawnMobParticle(robot);
				/*Display.addParticle(new Particle(
						robot.x*16, robot.y*16,
						(float)(Math.random()*1.0f - 0.5f), (float)(Math.random()*1.0f - 0.5f) - 3f,
						0f, 0.05f,
						blockImage
						));*/
			}
		}
		
		if (Keyboard.isPressed("up")) {
			robot.im = robotLeft;
			robot.y -= 1/16f;
			
			if (Math.random()*4 < 1) {
				spawnMobParticle(robot);
			}
		}
		
		if (Keyboard.isPressed("down")) {
			robot.im = robotRight;
			robot.y += 1/16f;
			
			if (Math.random()*4 < 1) {
				spawnMobParticle(robot);
			}
		}
	}

	public void spawnMobParticle(Mob m) {
		Display.addParticle(new Particle(
				m.x*16, m.y*16,
				(float)(Math.random()*1.0f - 0.5f), (float)(Math.random()*1.0f - 0.5f) - 3f,
				0f, 0.05f,
				blockImage
				));
	}
	
}
