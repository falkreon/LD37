package endless.blue.oneroom;

public enum Cardinal {
	NORTH( 0,-1),
	EAST ( 1, 0),
	SOUTH( 0, 1),
	WEST (-1, 0);
	
	private final int xofs;
	private final int yofs;
	
	Cardinal(int xofs, int yofs) {
		this.xofs = xofs;
		this.yofs = yofs;
	}
	
	public int xofs() { return xofs; }
	public int yofs() { return yofs; }
	public Cardinal cw() {
		switch(this) {
		case NORTH: return EAST;
		case EAST: return SOUTH;
		case SOUTH: return WEST;
		case WEST: default: return NORTH;
		}
	}
	
	public Cardinal ccw() {
		switch(this) {
		case NORTH: return WEST;
		case EAST: default: return NORTH;
		case SOUTH: return EAST;
		case WEST: return SOUTH;
		}
	}
}
