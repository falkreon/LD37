package endless.blue.oneroom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

import javax.sound.sampled.Clip;

import endless.blue.oneroom.enemy.EliteRoomba;
import endless.blue.oneroom.enemy.Enemy;
import endless.blue.oneroom.enemy.OrbEnemy;
import endless.blue.oneroom.enemy.Roomba;

public class FieldScreen implements Screen {
	
	private Clip music;
	
	private BufferedImage lightMap = new BufferedImage(Room.WIDTH, Room.HEIGHT, BufferedImage.TYPE_INT_ARGB);
	
	private Clip robotHurt;
	
	private BufferedImage healthpack;
	private Clip healthpackCollect;
	
	private BufferedImage letter;
	private BufferedImage book;
	private BufferedImage turnin;
	private Clip fitness;
	
	private Room curRoom = new Room();
	
	private BufferedImage[] particles = null;
	
	private ArrayList<Mob> mobs = new ArrayList<>();
	
	private int asplodeTimer = 0;
	
	private Mob robot = new Mob();
	Light robotLight = new Light((int)robot.x,(int)robot.y,3);
	
	Color bg = Color.BLACK; //new Color(100,100,200);
	
	private int roomThreshold = 10;
	
	@Override
	public void onActivate() {
		BufferedImage[] robotFrames = ResourceBroker.diceImage(ResourceBroker.loadImage("image/robot.png"), 16, 23);
		SpriteSet robotSprite = new SpriteSet(robotFrames);
		
		robot.curSprite = robotSprite;
		
		healthpack = ResourceBroker.loadImage("image/healthpack.png");
		healthpackCollect = ResourceBroker.loadSound("sound/collect_healthpack.wav");
		
		letter = ResourceBroker.loadImage("image/letter.png");
		book   = ResourceBroker.loadImage("image/book.png");
		turnin = ResourceBroker.loadImage("image/turnin.png");
		fitness = ResourceBroker.loadSound("sound/fitness.wav");
		
		FitnessObjective goal = new FitnessObjective(letter,book,turnin,fitness);
		Display.curObjective = goal;
		mobs.add(goal);
		
		curRoom = RoomTemplates.constructFromTemplate(RoomTemplates.test1);
		
		particles = ResourceBroker.diceImage(ResourceBroker.loadImage("image/particles.png"), 8, 8);
		
		//roombaHurt = ResourceBroker.loadSound("sound/hurt_roomba.wav");
		robotHurt = ResourceBroker.loadSound("sound/hurt_robot.wav");
		robot.attackSound = ResourceBroker.loadSound("sound/attack_robot.wav");
		
		robotLight.radius = 8;
		curRoom.addLight(robotLight);
		mobs.add(robot);
		for(int i=0; i<4; i++) {
			mobs.add(new Roomba());
		}
		
		relight();
		
		music = ResourceBroker.loadMusic("sound/3.mp3");
		music.loop(Clip.LOOP_CONTINUOUSLY);
		//if (music!=null) music.start(); //TODO: RE-ENABLE THIS TO RESTART MUSIC BEFORE RELEASE
		
		robot.hurtSound = robotHurt;
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
		for(Mob m : mobs) {
			m.paint(g);
		}
		//robot.paint(g);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(lightMap, 0, 0, Room.WIDTH*16, Room.HEIGHT*16, Display.OBSERVER);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		
		g.setColor(Color.WHITE);
		g.drawRect(1, 1, 41, 5);
		g.setColor(Color.RED);
		g.fillRect(2, 2, (int)robot.health*2, 4);
		
		g.setColor(Color.WHITE);
		//g.drawString("Particles: "+Display.numParticles(), 43, 10);
		
		g.drawString("Fitness:",80,10);
		if (Display.scrambleScore<Display.score) {
			g.setColor(Color.GREEN);
		}
		
		g.drawString(""+Display.scrambleScore, 140, 10);
		
		g.setColor(Color.YELLOW);
		g.drawString(Display.tip, 200, 10);
		
	}

	private int lightStep = 0;
	
	@Override
	public void onStep() {
		
		
		if (robot.health()>0) {
			if (Keyboard.isPressed("left")) {
				robot.curSprite.setFacing(Cardinal.WEST);
				//robot.curFrame = robotLeft;
				robot.facing = Cardinal.WEST;
				robot.nudgeLeft(curRoom);
			}
			if (Keyboard.isPressed("right")) {
				robot.curSprite.setFacing(Cardinal.EAST);
				//robot.curFrame = robotRight;
				robot.facing = Cardinal.EAST;
				robot.nudgeRight(curRoom);
			}
			
			if (Keyboard.isPressed("up")) {
				robot.curSprite.setFacing(Cardinal.NORTH);
				//robot.curFrame = robotNorth;
				robot.facing = Cardinal.NORTH;
				robot.nudgeUp(curRoom);
			}
			
			if (Keyboard.isPressed("down")) {
				robot.curSprite.setFacing(Cardinal.SOUTH);
				//robot.curFrame = robotSouth;
				robot.facing = Cardinal.SOUTH;
				robot.nudgeDown(curRoom);
			}
			
			if (Keyboard.isPressed("action")) {
				//Attack!
				spawnMobAttack(robot,1, 2.0f, 0.4f, 16);
				robot.melee(mobs);
			}
		}
		
		lightStep--;
		if (lightStep<=0) {
			robotLight.x = robot.x;
			robotLight.y = robot.y;
			relight();
			lightStep = 5;
			
			if (Display.scrambleScore<Display.score) {
				long scrambleSpeed = (Display.score-Display.scrambleScore) / 32L;
				if (scrambleSpeed<1) scrambleSpeed = 1;
				Display.scrambleScore+= scrambleSpeed;
			}
		}
		
		ArrayList<Mob> toRemove = new ArrayList<>();
		for(Mob m : mobs) {
			m.physicsTick(curRoom, robot);
			
			if (m==robot) continue;
			if (m.health()<=0) {
				if (m instanceof Enemy) Display.score += ((Enemy)m).points;
				for(int i=0; i<20; i++) {
					m.facing = Cardinal.WEST;
					spawnMobAttack(m,0,2,6.0f,20);
					m.facing = Cardinal.EAST;
					spawnMobAttack(m,0,2,6.0f,20);
				}
				toRemove.add(m);
			}	
		}
		for(Mob m : toRemove) {
			if ((int)(Math.random()*10) == 0) spawnHealthpack(m); //10% spawn health back
		}
		mobs.removeAll(toRemove);
		
		if (robot.health()==0) {
			if (asplodeTimer>0) asplodeTimer--;
			else {
				//asplode = true;
				mobs.remove(robot);
				for(int i=0; i<20; i++) {
					robot.facing = Cardinal.WEST;
					spawnMobAttack(robot,2,2,6.0f,20);
					robot.facing = Cardinal.EAST;
					spawnMobAttack(robot,2,2,6.0f,20);
				}
				asplodeTimer = 20;
			}
		}
		
		if (mobs.size()<roomThreshold) {
			if ((int)(Math.random()*300) == 0) {
				
				ArrayList<Callable<Mob>> spawnTable = new ArrayList<>();
				spawnTable.add(()->new Roomba());
				//if (Display.score > 5000)
					spawnTable.add(()->new OrbEnemy());
				//if (Display.score > 10000)
					spawnTable.add(()->new EliteRoomba());
					//spawnTable.add(Enemies::createEliteRoomba);
				
				int spawnItem = (int)(Math.random()*spawnTable.size());
				try {
					Mob toSpawn = spawnTable.get(spawnItem).call();
					mobs.add(toSpawn);
				} catch (Exception ex) {}
				
				//NOTE: Letters only spawn when there's room in the level! You MUST kill SOME enemies!
				if ((int)(Math.random()*5) == 0 && Display.curObjective==null) {
					Display.curObjective = new FitnessObjective(letter, book, turnin, fitness);
					mobs.add(Display.curObjective);
					Display.tip = "Collect the envelope of unreadable Chinese symbols!";
				}
				
				//if ((int)(Math.random()*5) == 0) {
				//	mobs.add(Enemies.createRoomba());
				//} else {
				//	mobs.add(new Enemy(roomba, roombaHurt));
				//}
			}
		}
	}

	private Random particleRandom = new Random();
	
	public void spawnMobSmoke(Mob m, int kind) {
		int particle = (int)(Math.random()*4) + kind*4;
		
		Display.addParticle(new Particle(
				m.x*16 + 8, m.y*16 + 2,
				(float)(Math.random()*1.0f - 0.5f), (float)(Math.random()*1.0f - 0.5f) - 3f,
				0f, 0.05f,
				particles[particle]
				));
	}
	
	public void spawnMobAttack(Mob m, int kind, float speed, float spread, int fuse) {
		int particle = (int)(Math.random()*4) + kind*4;
		
		float baseVX = m.facing.xofs()*speed;
		float baseVY = m.facing.yofs()*speed;
		
		baseVX += particleRandom.nextGaussian()*spread;
		baseVY += particleRandom.nextGaussian()*spread;
		
		Particle result = new Particle(
				m.x*16 + 8, m.y*16 + 2,
				baseVX, baseVY,
				0f, 0f,
				particles[particle]
				);
		result.fuse = fuse;
		Display.addParticle(result);
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
	
	public void spawnHealthpack(Mob m) {
		HealthPack pack = new HealthPack(healthpack, healthpackCollect);
		pack.x = m.x;
		pack.y = m.y;
		mobs.add(pack);
	}
}
