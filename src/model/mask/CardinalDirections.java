package model.mask;

public enum CardinalDirections {
	NORTH(0,-1), SOUTH(0,1), EAST(1,0), WEST(-1,0);
	
	private int x;
	private int y;
	
	private CardinalDirections(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
