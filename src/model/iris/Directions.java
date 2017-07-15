package model.iris;

public enum Directions {
		D60(1,1),D110(1,-1),D240(-1,-1),D300(-1,1);
		
		private int x;
		private int y;
		Directions(int x, int y){
			this.x=x;
			this.y=y;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
}
