package model.image;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ImageColorRGB extends ImageColor {

	private static final int RED_BAND=0;
	private static final int GREEN_BAND=1;
	private static final int BLUE_BAND=2;

	
	public ImageColorRGB(BufferedImage image) {
		this(image.getWidth(),image.getHeight());
		int width=this.getWidth();
		int height=this.getHeight();
		Raster raster=image.getData();
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				double[] pixelValue=new double[RGB_QTY];
				for(int k=0;k<RGB_QTY;k++){
					pixelValue[k]=raster.getSampleDouble(i, j, k);
				}
				this.setPixel(i, j, pixelValue);
			}
		}

	}
	public ImageColorRGB(int width, int height){
		super(width,height,RGB_QTY);
	}
	public ImageColorRGB(double[][][] image) {
		super(image,RGB_QTY);
	}

	@Override
	public Image getRegion(Point origin, Point end) {
		return new ImageColorRGB(super.getRegionMatrix(origin, end));
	}

	public BufferedImage showImage(){
		int width=this.getWidth();
		int height=this.getHeight();
		BufferedImage bi=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		WritableRaster wr=bi.getRaster();
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				wr.setSample(i, j, RED_BAND, this.getPixel(i, j)[RED_BAND]);
				wr.setSample(i, j, GREEN_BAND, this.getPixel(i, j)[GREEN_BAND]);
				wr.setSample(i, j, BLUE_BAND, this.getPixel(i, j)[BLUE_BAND]);
			}
		}
		return bi;
	}

	public ImageColorRGB getBand(int band){
		int width=this.getWidth();
		int height=this.getHeight();
		ImageColorRGB imgColor=new ImageColorRGB(width,height);
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				double[] pixelValue=new double[]{0,0,0};
				pixelValue[band]=this.getPixel(i, j)[band];
				imgColor.setPixel(i, j, pixelValue);
			}
		}
		return imgColor;
	}
	
	public ImageColorHSV passToHSV(){
		int width=this.getWidth();
		int height=this.getHeight();
		ImageColorHSV imgColor=new ImageColorHSV(width,height);
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				double[] pixelRGB=this.getPixel(i, j);
				float[] pixelHSV=new float[HSV_QTY];
				Color.RGBtoHSB((int)pixelRGB[RED_BAND], (int)pixelRGB[GREEN_BAND], (int)pixelRGB[BLUE_BAND], pixelHSV);
				
				double[] pixelHSV_double=new double[HSV_QTY];
				for(int k=0;k<HSV_QTY;k++){
					pixelHSV_double[k]=(double)pixelHSV[k];
				}
				imgColor.setPixel(i, j, pixelHSV_double);
			}
		}
		return imgColor;
	}

	public BufferedImage getNegative() {
		BufferedImage bi=new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster wr=bi.getRaster();
		for(int i=0;i<this.getWidth();i++){
			for(int j=0;j<this.getHeight();j++){
				wr.setSample(i, j, RED_BAND, - this.getImage()[i][j][RED_BAND] + 255 -1);
				wr.setSample(i, j, GREEN_BAND, - this.getImage()[i][j][GREEN_BAND] + 255 -1);
				wr.setSample(i, j, BLUE_BAND, - this.getImage()[i][j][BLUE_BAND] + 255 -1);
			}
		}
		return bi;
	}

}
