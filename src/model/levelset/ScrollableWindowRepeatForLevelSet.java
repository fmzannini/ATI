package model.levelset;

import java.awt.Point;

public class ScrollableWindowRepeatForLevelSet {
	
	private int widthWindow;
	private int heightWindow;

	private Point[] displacements;
	private Point middleWindow;


	// Se asume tamaño cuadrado impar
	public ScrollableWindowRepeatForLevelSet(int widthWindow, int heightWindow) {
		this.widthWindow = widthWindow;
		this.heightWindow = heightWindow;
		generateDisplacements();
	}

	private void generateDisplacements() {
		this.displacements = new Point[widthWindow * heightWindow];
		this.middleWindow = new Point((this.widthWindow - 1) / 2, (this.heightWindow - 1) / 2);
		int k = 0;
		for (int i = 0; i < this.widthWindow; i++) {
			for (int j = 0; j < this.heightWindow; j++) {
				this.displacements[k++] = new Point(i - this.middleWindow.x, j - this.middleWindow.y);
			}
		}
	}

	
	public int getWidthWindow() {
		return this.widthWindow;
	}

	
	public int getHeightWindow() {
		return this.heightWindow;
	}

	
	public Point getMiddlePoint() {
		return middleWindow;
	}

	
	public Point[] getDisplacements() {
		return displacements;
	}


	public double[][] nextRegion(int[][] matrix, Point center) {
		double[][] region=new double[this.widthWindow][this.heightWindow];
		
		for (Point displacement : this.displacements) {
			int x = center.x + displacement.x;
			int y = center.y + displacement.y;

			if (x < 0) {
				x = Math.abs(x);
			} else if (x >= matrix.length) {
				x = x - (x - (matrix.length - 1));
			}
			if (y < 0) {
				y = Math.abs(y);
			} else if (y >= matrix[0].length) {
				y = y - (y - (matrix[0].length - 1));
			}

			region[this.middleWindow.x + displacement.x][this.middleWindow.y + displacement.y] = matrix[x][y];
		}

		return region;
	}

}
