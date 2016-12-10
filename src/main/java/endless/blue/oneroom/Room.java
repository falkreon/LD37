package endless.blue.oneroom;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * I realize that having this class betrays my intent to make multiple rooms.
 * 
 * #DealWithIt
 */
public class Room {
	public static final float PIXEL = 1/16f;
	private static final BufferedImage EMPTY_TILE = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
	
	public static final int WIDTH = 40;
	public static final int HEIGHT = 22;
	
	private BlockType[] tileIdentity = new BlockType[WIDTH*HEIGHT];
	private int[] tileImage = new int[WIDTH*HEIGHT];
	
	private final ArrayList<Image> tileset = new ArrayList<>();
	
	public BlockType getTile(int x, int y) {
		if (x<0 || x>=WIDTH || y<0 || y>=HEIGHT) return BlockType.OUTSIDE_OF_LEVEL;
		BlockType result = tileIdentity[WIDTH*y + x];
		return (result==null) ? BlockType.OUTSIDE_OF_LEVEL : result;
	}
	
	public Image getTileImage(int x, int y) {
		if (tileset.isEmpty()) {
			JOptionPane.showMessageDialog(null, "A fatal error has occurred: No tileset.");
			System.exit(-1);
		}
		if (x<0 || x>=WIDTH || y<0 || y>=HEIGHT) return tileset.get(0);
		int image = tileImage[WIDTH*y + x];
		if (tileset.size()<=image) return tileset.get(0);
		return tileset.get(image);
	}

	public void clearTiles() {
		tileset.clear();
		tileset.add(EMPTY_TILE);
	}
	
	public void addTile(Image tile) {
		tileset.add(tile);
	}
	
	public void setBlock(int x, int y, BlockType id, int image) {
		if (x<0 || x>=WIDTH || y<0 || y>=HEIGHT) return;
		tileIdentity[WIDTH*y+x] = id;
		tileImage[WIDTH*y+x] = image;
	}
}
