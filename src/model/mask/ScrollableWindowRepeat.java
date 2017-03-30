package model.mask;

import java.awt.Point;

import model.image.ImageGray;

public class ScrollableWindowRepeat implements ScrollableWindow {

	private ImageGray img;
	private int widthWindow;
	private int heightWindow;

	private Point[] displacements;
	private Point middleWindow;

	private ImageGray result;

	private Point center;

	// Se asume tamaño cuadrado impar
	public ScrollableWindowRepeat(ImageGray img, int widthWindow, int heightWindow) {
		this.img = img;
		this.widthWindow = widthWindow;
		this.heightWindow = heightWindow;
		generateDisplacements();

		this.result = new ImageGray(img.showImage(), false);
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

	@Override
	public int getWidthWindow() {
		return this.widthWindow;
	}

	@Override
	public int getHeightWindow() {
		return this.heightWindow;
	}

	@Override
	public Point getMiddlePoint() {
		return middleWindow;
	}

	@Override
	public Point[] getDisplacements() {
		return displacements;
	}

	@Override
	public boolean hasNext() {
		if (center != null && center.x >= img.getWidth() - 1 && center.y >= img.getHeight() - 1)
			return false;
		return true;
	}

	@Override
	public double[][] nextRegion() {
		double[][] region=new double[this.widthWindow][this.heightWindow];
		
		if(center == null)
			center=new Point(0,0);
		else{
			if(center.x>=img.getWidth()-1){
				center=new Point(0,center.y+1);
			}else{
				center=new Point(center.x+1,center.y);				
			}
		}

		for (Point displacement : this.displacements) {
			int x = center.x + displacement.x;
			int y = center.y + displacement.y;

			if (x < 0) {
				x = Math.abs(x);
			} else if (x >= img.getWidth()) {
				x = x - (x - (img.getWidth() - 1));
			}
			if (y < 0) {
				y = Math.abs(y);
			} else if (y >= img.getHeight()) {
				y = y - (y - (img.getHeight() - 1));
			}

			region[this.middleWindow.x + displacement.x][this.middleWindow.y + displacement.y] = img.getPixel(x, y);
		}

		return region;
	}

	@Override
	public void updateCurrentCenter(double value) {
		this.result.setPixel(center, value);
	}

	@Override
	public ImageGray getResult() {
		return result;
	}

}
