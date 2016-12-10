package endless.blue.oneroom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.sound.sampled.Clip;

public class FieldScreen implements Screen {
	
	private Clip music;
	
	private BufferedImage lightMap = new BufferedImage(Room.WIDTH, Room.HEIGHT, BufferedImage.TYPE_INT_ARGB);
	
	private BufferedImage robotRight;
	private BufferedImage robotLeft;
	private BufferedImage robotNorth;
	private BufferedImage robotSouth;
	
	private Room curRoom = new Room();
	
	private BufferedImage[] particles = null;
	
	private ArrayList<Mob> mobs = new ArrayList<>();
	
	private Mob robot = new Mob();
	Light robotLight = new Light((int)robot.x,(int)robot.y,3);
	
	Color bg = new Color(100,100,200);
	
	@Override
	public void onActivate() {
		robotRight = ResourceBroker.loadImage("image/robot_stand.png");
		robotLeft = ResourceBroker.getFlipped(robotRight);
		robotNorth = ResourceBroker.loadImage("image/robot_north.png");
		robotSouth = ResourceBroker.loadImage("image/robot_south.png");
		robot.im = robotRight;
		
		curRoom = RoomTemplates.constructFromTemplate(RoomTemplates.test1);
		
		particles = ResourceBroker.diceImage(ResourceBroker.loadImage("image/particles.png"), 8, 8);
		
		robotLight.radius = 8;
		curRoom.addLight(robotLight);
		mobs.add(robot);
		
		relight();
		
		music = ResourceBroker.loadSound("sound/3.mp3");
		music.loop(Clip.LOOP_CONTINUOUSLY);
		if (music!=null) music.start();
	}

	@Override
	public void onDeactivate() {
		
	}

	@Override
	public void onPaint(Graphics2D g) {
		g.setColor(bg);
		g.fillRect(0, 0, 640, 360);
		
		for(int y=0; y<Room.HEIGHT; y++) {
			for (int x=0; x<Room.WIDTH; x++) {
				g.drawImage(curRoom.getTileImage(x, y), x*16, y*16, Display.OBSERVER);
			}
		}
		
		//g.drawImage(blockImage, 0, 16, observer);
		robot.paint(g);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(lightMap, 0, 0, Room.WIDTH*16, Room.HEIGHT*16, Display.OBSERVER);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
	}

	private int lightStep = 0;
	
	@Override
	public void onStep() {
		if (Keyboard.isPressed("left")) {
			robot.im = robotLeft;
			robot.facing = Cardinal.WEST;
			robot.nudgeLeft(curRoom);
		}
		if (Keyboard.isPressed("right")) {
			robot.im = robotRight;
			robot.facing = Cardinal.EAST;
			robot.nudgeRight(curRoom);
		}
		
		if (Keyboard.isPressed("up")) {
			robot.im = robotNorth;
			robot.facing = Cardinal.NORTH;
			robot.nudgeUp(curRoom);
		}
		
		if (Keyboard.isPressed("down")) {
			robot.im = robotSouth;
			robot.facing = Cardinal.SOUTH;
			robot.nudgeDown(curRoom);
		}
		
		if (Keyboard.isPressed("action")) {
			//Attack!
			spawnMobSmoke(robot,1);
		}
		
		lightStep--;
		if (lightStep<=0) {
			robotLight.x = robot.x;
			robotLight.y = robot.y;
			relight();
			lightStep = 5;
		}
	}

	public void spawnMobSmoke(Mob m, int kind) {
		int particle = (int)(Math.random()*4) + kind*4;
		
		Display.addParticle(new Particle(
				m.x*16 + 8, m.y*16 + 2,
				(float)(Math.random()*1.0f - 0.5f), (float)(Math.random()*1.0f - 0.5f) - 3f,
				0f, 0.05f,
				particles[particle]
				));
	}
	
	public int shadowTexel(int level) {
		return ((255-level) & 0xFF) << 24;
	}
	
	public int createGray(int level) {
		int lev = level & 0xFF;
		return (lev << 16) |
			   (lev <<  8) |
			   (lev <<  0);
	}
	
	public int createGray(int level, int intensity) {
		int lev = level & 0xFF;
		int i = 255 - intensity;
		return (  i << 24) |
			   (lev << 16) |
			   (lev <<  8) |
			   (lev <<  0);
	}
	
	public int createColor(int color, int intensity) {
		int i = 255 - intensity;
		return (color & 0x00FFFFFF) | ((i & 0xFF) << 24);
	}
	
	public void relight() {
		for(int y=0; y<Room.HEIGHT; y++) {
			for(int x=0; x<Room.WIDTH; x++) {
				lightMap.setRGB(x, y, shadowTexel(56));
			}
		}
		
		eachLight:
		for(Light light : curRoom.lights()) {
			int r = (int)Math.ceil(light.radius);
			int x1 = (int)light.x - r; if (x1<0) x1=0;
			int y1 = (int)light.y - r; if (y1<0) y1=0;
			int x2 = (int)light.x + r; if (x2>=Room.WIDTH) x2=Room.WIDTH-1;
			int y2 = (int)light.y + r; if (y2>=Room.HEIGHT) y2=Room.HEIGHT-1;
			
			for(int y=y1; y<=y2; y++) {
				for(int x=x1; x<=x2; x++) {
					float dist = checkRay(light.x, light.y, x, y);
					if (dist<=light.radius) {
						//Lit
						int intensity = (int)(light.intensity * (1-(dist/light.radius)) * 255.0f);
						
						int existing = (lightMap.getRGB(x, y) >> 24) & 0xFF;
						existing = 255-existing;
						int texel = Math.min(existing + intensity, 255);
						lightMap.setRGB(x, y, shadowTexel(texel));
					}
				}
			}
		}
	}
	
	public float checkRay(float x1, float y1, float x2, float y2) {
		//TODO: It may be desirable to remove the sqrts and just square the radius for the purposes of determining light falloff.
		
		int xdist = (int)Math.abs(x2-x1);
		int ydist = (int)Math.abs(y2-y1);
		float steps = Math.max(xdist, ydist);
		
		float xstep = (x2-x1) / steps;
		float ystep = (y2-y1) / steps;
		
		float x = x1+xstep;
		float y = y1+ystep;
		
		for(int i=0; i<steps; i++) {
			BlockType btype = curRoom.getTile((int)x, (int)y);
			if (btype==BlockType.OPAQUE_WALL) {
				return Float.MAX_VALUE;
			}
			
			x += xstep;
			y += ystep;
		}
		
		return (float)Math.sqrt(xdist*xdist + ydist*ydist);
	}
	
}
