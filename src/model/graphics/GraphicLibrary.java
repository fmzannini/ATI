
package model.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GraphicLibrary {
	public static BufferedImage generateCircle(double radius){
		int diameter=(int)Math.ceil(2*radius);
		BufferedImage bi=new BufferedImage(diameter, diameter, BufferedImage.TYPE_BYTE_BINARY);
		Graphics graphics=bi.getGraphics();
		graphics.drawOval(0,0, diameter-1, diameter-1);
		return bi;
	}
	public static BufferedImage generateSquare(double length){
		int size=(int) length;
		BufferedImage bi=new BufferedImage(size, size, BufferedImage.TYPE_BYTE_BINARY);
		Graphics graphics=bi.getGraphics();
		graphics.drawRect(0, 0, size-1, size-1);
		return bi;
	}
}
