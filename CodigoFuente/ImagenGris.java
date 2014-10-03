package p1;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import com.pearsoneduc.ip.op.EqualiseOp;
import com.pearsoneduc.ip.op.Histogram;
import com.pearsoneduc.ip.op.HistogramException;

public class ImagenGris {

	
	public static BufferedImage equaliza(BufferedImage image) throws HistogramException{
		Histogram histogram = new Histogram(image);
		EqualiseOp equalise = new EqualiseOp(histogram);
		BufferedImage equalisedImage = equalise.filter(image, null);
		return equalisedImage;
	}
	public static BufferedImage rescale(BufferedImage image,float gain, float bias){
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage rescaledImage = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster input = image.getRaster();
		WritableRaster output = rescaledImage.getRaster();
		
		for(int y = 0; y < h; y++){
			for(int x = 0; x < w; x++){
				output.setSample(x, y, 0, clamp(gain*input.getSample(x, y, 0) + bias));
			}
		}
		return rescaledImage;
	}
	
	public static int clamp(float value){
		return Math.min(Math.max(Math.round(value), 0), 255);
	}
}
