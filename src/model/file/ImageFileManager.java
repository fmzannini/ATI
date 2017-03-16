package model.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageFileManager {

	private File file;
	
	public ImageFileManager(File file){
		this.file=file;
	}

	
	public BufferedImage readImage() throws IOException{
		return ImageIO.read(file);
	}
	
	public BufferedImage readImageBMP() throws IOException{
		return ImageIO.read(file);
	}
	public BufferedImage readImagePGM() throws IOException{
		return ImageIO.read(file);
	}
	public BufferedImage readImagePPM() throws IOException{
		return ImageIO.read(file);
	}
	public BufferedImage readImageRAW(int width, int height) throws IOException{
		RawFormat rawFormat=new RawFormat(file, width, height);
		return rawFormat.read();
	}
	
	
	public void writeImage(BufferedImage img, String format) throws IOException{
		ImageIO.write(img,format,file);
	}
	
	public void writeImageBMP(BufferedImage img) throws IOException{
		ImageIO.write(img,"bmp",file);
	}
	public void writeImagePGM(BufferedImage img) throws IOException{
		ImageIO.write(img,"pnm",file);
	}
	public void writeImagePPM(BufferedImage img) throws IOException{
		ImageIO.write(img,"pnm",file);
	}
	public void writeImageRAW(BufferedImage img) throws IOException{
		RawFormat rawFormat=new RawFormat(file, img.getWidth(), img.getHeight());
		rawFormat.write(img);
	}

	
}
