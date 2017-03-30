package model.image;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class ImageColorHSV extends ImageColor {

	private static final int HUE_BAND = 0;
	private static final int SATURATION_BAND = 1;
	private static final int BRIGHTNESS_BAND = 2;

	public ImageColorHSV(int width, int height){
		super(width,height,HSV_QTY);
	}
	public ImageColorHSV(double[][][] image) {
		super(image,HSV_QTY);
	}

	@Override
	public ImageType getType() {
		return ImageType.IMAGE_HSV;
	}
	
	@Override
	public Image getRegion(Point origin, Point end) {
		return new ImageColorHSV(super.getRegionMatrix(origin, end));
	}

	@Override
	public BufferedImage showImage() {
		return this.passToRGB().showImage();
	}

	public ImageColorHSV getBand(int band){
		int width=this.getWidth();
		int height=this.getHeight();
		ImageColorHSV imgColor=new ImageColorHSV(width,height);
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				double[] pixelValue=new double[]{0,0,0};
				pixelValue[band]=this.getPixel(i, j)[band];
				imgColor.setPixel(i, j, pixelValue);
			}
		}
		return imgColor;
	}
	
	@Override
	public ImageGray getBandOnlyGray(int band) {
		ImageGray img = super.getBandOnlyGray(band);
		double[][] pixels=img.getImage();
		for(int i=0;i<img.getWidth();i++){
			for(int j=0;j<img.getHeight();j++){
				pixels[i][j]=pixels[i][j]*255;
			}
		}
		return img;
	}
	
	public ImageColorRGB passToRGB(){
		int width=this.getWidth();
		int height=this.getHeight();
		ImageColorRGB imgColor=new ImageColorRGB(width,height);
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				double[] pixelHSV=this.getPixel(i, j);
				int pixelRGB=Color.HSBtoRGB((float)pixelHSV[HUE_BAND], (float)pixelHSV[SATURATION_BAND], (float)pixelHSV[BRIGHTNESS_BAND]);
				Color pixelRGB_color=new Color(pixelRGB);

				double[] pixelRGB_double=new double[]{ (double)pixelRGB_color.getRed(),
						(double)pixelRGB_color.getGreen(), (double)pixelRGB_color.getBlue()
						};
				
				imgColor.setPixel(i, j, pixelRGB_double);
			}
		}
		return imgColor;
	}
	
	public Image copy() {
		double[][][] matrix = new double[this.getWidth()][this.getHeight()][3];
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				matrix[i][j][HUE_BAND] = this.getImage()[i][j][HUE_BAND];
				matrix[i][j][SATURATION_BAND] = this.getImage()[i][j][SATURATION_BAND];
				matrix[i][j][BRIGHTNESS_BAND] = this.getImage()[i][j][BRIGHTNESS_BAND];
			}
		}
		return new ImageColorRGB(matrix);
	}


}
