package model.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.image.ImageGray;

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

	public static void main(String[] args) throws IOException {
		File fpgm=new File(System.getProperty("user.dir")+"/bell.pgm");
		File fppm=new File(System.getProperty("user.dir")+"/Zlatoust.ppm");
		File fbmpColor=new File(System.getProperty("user.dir")+"/boy.bmp");
		File fbmpGray=new File(System.getProperty("user.dir")+"/lena_gray.bmp");
		File fraw=new File(System.getProperty("user.dir")+"/GIRL.RAW");
		int width=389;
		int height=164;
		File fpng=new File(System.getProperty("user.dir")+"/monarch.png");
		
		
		BufferedImage bi=new ImageFileManager(fpgm).readImagePGM();
		testSave(new File(System.getProperty("user.dir")+"/fpgm.png"),bi);

		bi=new ImageFileManager(fppm).readImagePPM();
		testSave(new File(System.getProperty("user.dir")+"/fppm.png"),bi);

		bi=new ImageFileManager(fbmpColor).readImageBMP();
		testSave(new File(System.getProperty("user.dir")+"/fbmpColor.png"),bi);

		bi=new ImageFileManager(fbmpGray).readImageBMP();
		testSave(new File(System.getProperty("user.dir")+"/fbmpGray.png"),bi);

		bi=new ImageFileManager(fraw).readImageRAW(width,height);
		testSave(new File(System.getProperty("user.dir")+"/fraw.png"),bi);

		bi=new ImageFileManager(fpng).readImage();
		File f;
		f=new File(System.getProperty("user.dir")+"/fpng1.pgm");
		new ImageFileManager(f).writeImagePGM(new ImageGray(bi,true).showImage());

		f=new File(System.getProperty("user.dir")+"/fpng2.ppm");
		new ImageFileManager(f).writeImagePPM(bi);

		f=new File(System.getProperty("user.dir")+"/fpng3.bmp");
		new ImageFileManager(f).writeImageBMP(bi);

		f=new File(System.getProperty("user.dir")+"/fpng4.raw");
		new ImageFileManager(f).writeImageRAW(bi);

		bi=new ImageFileManager(f).readImageRAW(bi.getWidth(),bi.getHeight());
		f=new File(System.getProperty("user.dir")+"/fpngraw5.png");
		new ImageFileManager(f).writeImage(bi,"png");

	}
	
	public static void testSave(File f,BufferedImage bi) throws IOException{
		System.out.println("f:"+f.getName()+"  "+bi.getWidth());
		new ImageFileManager(f).writeImage(bi, "png");
	}
	public static void testSave2(File f,BufferedImage bi) throws IOException{
		System.out.println("f:"+f.getName()+"  "+bi.getWidth());
		new ImageFileManager(f).writeImage(bi, "png");
	}

}
