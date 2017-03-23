package model.graphics;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ImageTest {

	private static final int WIDTH = 200;
	private static final int HEIGHT = 200;
	
	private static final double LENGTH_SQUARE = 100;
	private static final double RADIUS_CIRCLE = 50;

	public BufferedImage generateSquare(){
		BufferedImage bi=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_BYTE_BINARY);
		GraphicLibrary graphics=new GraphicLibrary(bi.createGraphics());
		graphics.drawSquare(new Point(WIDTH/2,HEIGHT/2), LENGTH_SQUARE);
		BufferedImage bi2=passFromBinaryToGray(bi);
		return bi2;
	}
	
	public BufferedImage generateCircle(){
		BufferedImage bi=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_BYTE_BINARY);
		GraphicLibrary graphics=new GraphicLibrary(bi.createGraphics());
		
		graphics.drawCircle(new Point(WIDTH/2,HEIGHT/2), RADIUS_CIRCLE);
		BufferedImage bi2=passFromBinaryToGray(bi);
		return bi2;
	}
	
	
	private BufferedImage passFromBinaryToGray(BufferedImage bi){
		BufferedImage bi2=new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster newRaster=bi2.getRaster();
		Raster oldRaster=bi.getData();
		int size=bi.getWidth()*bi.getHeight();
		int[] samples=new int[size];
		samples=oldRaster.getPixels(0, 0, bi.getWidth(), bi.getHeight(), samples);
		for(int i=0;i<size;i++){
			samples[i]=samples[i]*255;
		}
		newRaster.setPixels(0, 0, bi.getWidth(), bi.getHeight(), samples);
		return bi2;
	}

}
