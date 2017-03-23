package model.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public abstract class ImageColor implements Image{
	
	private double[][][] image;
	private int width;
	private int height;
	private final int band_qty;

	public double[][][] getImage() {
		return image;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	public ImageColor(int width, int height, int band_qty) {
		this.image=new double[width][height][band_qty];
		this.width = width;
		this.height = height;
		this.band_qty=band_qty;
	}

	
	public ImageColor(double[][][] image, int band_qty) {
		this.image = image;
		this.width = image.length;
		this.height = image[0].length;
		this.band_qty=band_qty;
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
	
	
	protected double[][][] getRegionMatrix(Point origin, Point end){
		int width=Math.abs(end.x-origin.x);
		int height=Math.abs(end.y-origin.y);
		double [][][] region=new double[width][height][band_qty];
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				for(int k=0;k<band_qty;k++){
					region[i][j][k]=this.image[origin.x+i][origin.y+j][k];
				}
			}
		}
		return region;
	}
	
	public void setRegion(ImageColor region, Point origin){
		int width=region.getWidth();
		int height=region.getHeight();
		double[][][] pixels=region.getImage();
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				for(int k=0;k<band_qty;k++){
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
				for(int k=0;k<band_qty;k++){
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
				for(int k=0;k<band_qty;k++){
					this.image[origin.x+i][origin.y+j][k] += pixels[i][j];
				}
			}
		}
	}
	
	
	
	public int getQtyPixels(){
		return width*height;
	}
	
	/*Devuelve el valor promedio para cada banda de color*/
	public double[] getMeanValuePixels(){
		double[] avg=new double[band_qty];
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				for(int k=0;k<band_qty;k++)
					avg[k]+=this.image[i][j][k];
			}
		}
		for(int k=0;k<band_qty;k++)
			avg[k] /= this.getQtyPixels();
		
		return avg;
	}
	
	
	public ImageGray getBandOnlyGray(int band){
		ImageGray imgGray=new ImageGray(this.width,this.height);
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				imgGray.setPixel(i, j, image[i][j][band]);
			}
		}
		return imgGray;
	}
	
}
