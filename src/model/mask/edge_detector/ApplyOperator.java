package model.mask.edge_detector;

import model.image.Image;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import model.mask.Mask;

public abstract class ApplyOperator {

	
	
	public Image apply(Image img){
		Image result=img;
		switch(img.getType()){
		case IMAGE_GRAY:
			result=apply((ImageGray)img);
			break;
		case IMAGE_RGB:
			ImageColorRGB imgColor=(ImageColorRGB)img;
			for(int i=0;i<ImageColorRGB.RGB_QTY;i++){
				ImageGray band=imgColor.getBandOnlyGray(i);
				band=apply(band);
				imgColor.setBand(band,i);
			}
			result=imgColor;
			break;
		}
		return result;
	}


	protected abstract ImageGray apply(ImageGray img);

}
