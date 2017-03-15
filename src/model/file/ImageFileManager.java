package model.file;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.image.ImageGray;

public class ImageFileManager {
/*FALTA AGREGAR LA LIBRERIA */
	public static void main(String[] args) throws IOException {
		
		final BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir")+"/TEST.PGM"));
		System.out.println(bi.getHeight());
		ImageGray img=new ImageGray(bi,false);
		
		ImageGray reg=img.getRegion(new Point(10,10),new Point(50,50));
		
		BufferedImage bireg=reg.showImage();
		System.out.println(bireg.getHeight());
		File file=new File(System.getProperty("user.dir")+"/img.pgm");

		ImageIO.write(bireg, "pgm", file);
		System.out.println(file.getAbsolutePath());
	}
}
