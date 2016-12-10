package endless.blue.oneroom;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javazoom.spi.mpeg.sampled.convert.DecodedMpegAudioInputStream;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

public class ResourceBroker {

	protected static Component observer = new javax.swing.JButton("WHY JAVA, WHY?");

	public static BufferedImage loadImage(String imageName) {
		try {
			BufferedImage im = ImageIO.read(new File(imageName));
			return im;
		} catch (Exception ex) {
			System.out.println("Cannot get file "+imageName);
			return new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		}
	}
	
	public static Clip loadSound(String imageName) {
		MpegAudioFileReader reader = new MpegAudioFileReader();
		
		
		
		try {
			Clip result = AudioSystem.getClip();
			
			AudioInputStream in = reader.getAudioInputStream(new File(imageName));
			DecodedMpegAudioInputStream dec = new DecodedMpegAudioInputStream(result.getFormat(), in);
			
			
			
			
			//AudioInputStream in = AudioSystem.getAudioInputStream(new File(imageName));
			result.open(dec);
			return result;
		} catch (Exception ex) {
			System.out.println("Couldn't open sound "+imageName+": "+ex.toString());
			return null;
		}
	}
	
	public static BufferedImage getFlipped(Image im) {
		BufferedImage ret = new BufferedImage(im.getWidth(observer),im.getHeight(observer),BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = ret.getGraphics();
		g.drawImage(im,im.getWidth(observer),0,-im.getWidth(observer),im.getHeight(observer),observer);
		g.dispose();
		return ret;
	}
	
	public static BufferedImage[] diceImage(Image im, int tileWidth, int tileHeight) {
		int tilesWide = im.getWidth(observer)/tileWidth;
		int tilesHigh = im.getHeight(observer)/tileHeight;
		int curTileNum = 0;
		BufferedImage[] result = new BufferedImage[tilesWide*tilesHigh];
		for(int y=0; y<tilesHigh; y++) {
			for(int x=0; x<tilesWide; x++) {
				BufferedImage curTile = new BufferedImage(tileWidth,tileHeight,BufferedImage.TYPE_4BYTE_ABGR);
				Graphics g = curTile.getGraphics();
				g.drawImage(im,-x*tileWidth,-y*tileHeight,im.getWidth(observer),im.getHeight(observer),observer);
				g.dispose();
				result[curTileNum]=curTile;
				curTileNum++;
			}
		}
		return result;
	}
	
}