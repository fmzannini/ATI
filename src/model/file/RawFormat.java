package model.file;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class RawFormat {
	
	private File file;
	private int width;
	private int height;

	public RawFormat(File file, int width, int height) {
		this.file = file;
		this.width = width;
		this.height = height;
	}
	
	public BufferedImage read() throws IOException{
		FileInputStream fis=new FileInputStream(file);
		byte[] buffer=new byte[width];
		BufferedImage ans=new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster=ans.getRaster();
		
		for(int i=0;i<height;i++){
			fis.read(buffer);
			for(int j=0;j<width;j++){
				raster.setSample(j, i, 0, buffer[j]);
			}
		}
		fis.close();
		return ans;
	}
	
	public void write(BufferedImage img) throws IOException{
		FileOutputStream fos=new FileOutputStream(file);
		Raster raster=img.getData();
		
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				int pixel=raster.getSample(j, i, 0);
				fos.write(pixel);
			}
		}
		
		fos.close();
	}
	

}
