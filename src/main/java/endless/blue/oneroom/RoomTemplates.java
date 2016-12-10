package endless.blue.oneroom;

import java.awt.image.BufferedImage;

public class RoomTemplates {

	public static String tileMarkers = ".O#@*%$;"; //Indicates numeric order for indexing a tile into its tileset.
	//A number indicates a light with radius equal to its value
	
	public static String[] test1 = {
		"########################################",
		"#..........#................#..........#",
		"#..........#................#..........#",
		"#......................................#",
		"#....6.....#................#.....6....#",
		"#..........#................#..........#",
		"#..........########..########..........#",
		"#..........#................#..........#",
		"###.####OO##................##OO####.###",
		"#.....#..............5...........#.....#",
		"#......................................#",
		"#...................................4..#",
		"#.....#..........................#.....#",
		"###.###.....................########.###",
		"#...........................#..........#",
		"#...........................#..........#",
		"#...........8...............#..........#",
		"#...........................#..........#",
		"#...........................#.....6....#",
		"#...........................#..........#",
		"#...........................#..........#",
		"########################################"
	};

	
	public static Room constructFromTemplate(String[] template) {
		Room result = new Room();
		result.clearTiles();
		result.addTile(ResourceBroker.loadImage("image/floorTile.png"));
		result.addTile(ResourceBroker.loadImage("image/glassTile.png"));
		result.addTile(ResourceBroker.loadImage("image/wallTile.png"));
		result.addTile(ResourceBroker.loadImage("image/block.png"));
		
		for(int y=0; y<template.length; y++) {
			if (y>=Room.HEIGHT) break;
			for(int x=0; x<template[y].length(); x++) {
				BlockType blockType = BlockType.OPAQUE_WALL;
				char tile = template[y].charAt(x);
				int markerIndex = tileMarkers.indexOf(tile);
				if (markerIndex==-1) blockType = BlockType.OUTSIDE_OF_LEVEL;
				if (markerIndex==0) blockType = BlockType.WALKABLE;
				if (markerIndex==1) blockType = BlockType.TRANSPARENT_WALL;
				//blockType = BlockType.OPAQUE_WALL;
				if (markerIndex==-1) markerIndex = 0; //outside-level also gets set to an image of 0
				
				if (Character.isDigit(tile)) {
					markerIndex = 0;
					blockType = BlockType.WALKABLE;
					int radius = Integer.valueOf(""+tile, 10);
					//System.out.println("Light at "+x+","+y+" r="+radius);
					result.addLight(new Light(x,y,radius*1.5f));
				}
				
				result.setBlock(x, y, blockType, markerIndex+1);
			}
		}
		
		return result;
	}
}
