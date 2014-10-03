package p1;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;


public class Histograma{
	
	BufferedImage image;
	WritableRaster raster;
	Integer numPixeles; 
	
	public Histograma(BufferedImage entrada){
		
		image = entrada;
		raster = image.getRaster();
		numPixeles = image.getHeight()*image.getWidth();
	}
	
	public int[] creaHistograma(){
		
		int histo[] =  new int[256];
		int luminancia = 0;
		//Inicializamos a cero el histograma
		for(int i = 0; i < 256; i++){
			histo[i] = 0;
		}
		
		for(int x = 0; x < image.getWidth(); x++){
			for(int y = 0; y < image.getHeight(); y++){
				luminancia = raster.getSample(x, y, 0);
				histo[luminancia]++;
				
			}
		}
		
		return histo;
	}
	
	public Integer getNumPixeles(){
		return numPixeles;
	}
}
		
		
	
	
