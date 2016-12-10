package endless.blue.oneroom;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ResourceBroker {

	protected static Component observer = new javax.swing.JButton("WHY JAVA, WHY?");

	public static Image loadImage(String imageName) {
		try {
			File f = new File(imageName);
			BufferedImage im = ImageIO.read(new File(imageName));
			return im;
		} catch (Exception ex) {
			System.out.println("Cannot get file "+imageName);
			return new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		}
	}
	
	public static Image getFlipped(Image im) {
		BufferedImage ret = new BufferedImage(im.getWidth(observer),im.getHeight(observer),BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = ret.getGraphics();
		g.drawImage(im,im.getWidth(observer),0,-im.getWidth(observer),im.getHeight(observer),observer);
		g.dispose();
		return ret;
	}
	
	public static Image[] diceImage(Image im, int tileWidth, int tileHeight) {
		int tilesWide = im.getWidth(observer)/tileWidth;
		int tilesHigh = im.getHeight(observer)/tileHeight;
		int curTileNum = 0;
		Image[] result = new Image[tilesWide*tilesHigh];
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