package model.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ImageColor implements Image{

	private static final int RED_BAND=0;
	private static final int GREEN_BAND=1;
	private static final int BLUE_BAND=2;
	
	
	private double[][][] image;
	private int width;
	private int height;
	
	public double[][][] getImage() {
		return image;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	public ImageColor(BufferedImage image) {
		this(image.getWidth(),image.getHeight());
		Raster raster=image.getData();
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				this.image[i][j][RED_BAND]=raster.getSampleDouble(i, j, RED_BAND);
				this.image[i][j][GREEN_BAND]=raster.getSampleDouble(i, j, GREEN_BAND);
				this.image[i][j][BLUE_BAND]=raster.getSampleDouble(i, j, BLUE_BAND);
			}
		}
	}

	public ImageColor(int width, int height) {
		this.image=new double[width][height][RGB_QTY];
		this.width = width;
		this.height = height;
	}

	
	public ImageColor(double[][][] image) {
		this.image = image;
		this.width = image.length;
		this.height = image[0].length;
	}
	
	public double[] getPixel(Point p){
		return this.getPixel(p.x, p.y);
	}	
	public double[] getPixel(int x, int y){
		return this.image[x][y].clone();
	}
	public void setPixel(Point p, double[] pixel){
		this.setPixel(p.x, p.y, pixel);
	}	
	public void setPixel(int x, int y, double[] pixel){
		this.image[x][y]=pixel;
	}
	
	
	public ImageColor getRegion(Point origin, Point end){
		int width=Math.abs(end.x-origin.x);
		int height=Math.abs(end.y-origin.y);
		double [][][] region=new double[width][height][RGB_QTY];
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				for(int k=0;k<RGB_QTY;k++){
					region[i][j][k]=this.image[origin.x+i][origin.y+j][k];
				}
			}
		}
		return new ImageColor(region);
	}
	
	public void setRegion(ImageColor region, Point origin){
		int width=region.getWidth();
		int height=region.getHeight();
		double[][][] pixels=region.getImage();
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				for(int k=0;k<RGB_QTY;k++){
					this.image[origin.x+i][origin.y+j][k]=pixels[i][j][k];
				}
			}
		}
	}
	
	public void overlapRegion(ImageColor region, Point origin){
		int width=region.getWidth();
		int height=region.getHeight();
		double[][][] pixels=region.getImage();
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				for(int k=0;k<RGB_QTY;k++){
					this.image[origin.x+i][origin.y+j][k] += pixels[i][j][k];
				}
			}
		}
	}
	public void overlapRegion(ImageGray region, Point origin){
		int width=region.getWidth();
		int height=region.getHeight();
		double[][] pixels=region.getImage();
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				for(int k=0;k<RGB_QTY;k++){
					this.image[origin.x+i][origin.y+j][k] += pixels[i][j];
				}
			}
		}
	}
	
	public BufferedImage showImage(){
		BufferedImage bi=new BufferedImage(this.width,this.height,BufferedImage.TYPE_INT_RGB);
		WritableRaster wr=bi.getRaster();
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				wr.setSample(i, j, RED_BAND, this.image[i][j][RED_BAND]);
				wr.setSample(i, j, GREEN_BAND, this.image[i][j][GREEN_BAND]);
				wr.setSample(i, j, BLUE_BAND, this.image[i][j][BLUE_BAND]);
			}
		}
		return bi;
	}
	
	
	public int getQtyPixels(){
		return width*height;
	}
	
	/*Devuelve el valor promedio para cada banda de color*/
	public double[] getMeanValuePixels(){
		double[] avg=new double[RGB_QTY];
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				for(int k=0;k<RGB_QTY;k++)
					avg[k]+=this.image[i][j][k];
			}
		}
		for(int k=0;k<RGB_QTY;k++)
			avg[k] /= this.getQtyPixels();
		
		return avg;
	}
	
	
}
