package p1;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;


public class Umbral {

	BufferedImage image;
	WritableRaster raster;
	
	public Umbral(BufferedImage entrada){
		
		image = entrada;
		raster = image.getRaster();
	}
	
	
	public BufferedImage binarizaImagen(){
		
		BufferedImage imagenSalida = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		WritableRaster salida = raster;
		int umbral = umbralAdaptativo();
		
		for(int x = 0; x < salida.getWidth(); x++){
			for(int y = 0; y < salida.getHeight(); y++){
				if(raster.getSample(x, y, 0) < umbral){
					salida.setSample(x, y, 0, 0);
				}else{
					salida.setSample(x, y, 0, 255);
				}
			}
		}

		imagenSalida.setData(salida);
		
		return imagenSalida;
	}
	
	public int umbralAdaptativo(){
	
		double media1, media2;
		double Told,Tnew;
		
		media1 = mediaEsquinas(raster);
		media2 = mediaImagen(raster);
		
		Told = 0;
		Tnew = (media1 + media2)/2;
		
		while(Tnew != Told){
			
			media1 = mediaInferior(raster,Tnew);
			media2 = mediaMayorOIgual(raster,Tnew);
			
			Told = Tnew;
			Tnew = (media1 + media2)/2;
		}
		
		return (int)Tnew;
	}
	
	public double mediaEsquinas(WritableRaster entrada){
		double res;
		int esquina1 = entrada.getSample(0, 0, 0); 
		int esquina2 = entrada.getSample(0, entrada.getHeight()-1, 0);
		int esquina3 = entrada.getSample(entrada.getWidth()-1, 0, 0);
		int esquina4 = entrada.getSample(entrada.getWidth()-1, entrada.getHeight()-1, 0);
		
		res = (esquina1 + esquina2 + esquina3 + esquina4)/4;
		return res;
	}
	
	public double mediaImagen(WritableRaster entrada){
		double res;
		int sum = 0;
		int num = 0;
		for(int x = 0; x < entrada.getWidth(); x++){
			for(int y = 0; y < entrada.getHeight(); y++){
				sum = sum + entrada.getSample(x, y, 0);
				num++;
			}
		}
		res = sum/num;
		return res;
	}
	
	public double mediaInferior(WritableRaster entrada, double T){
		double res;
		int sum = 0;
		int num = 0;
		for(int x = 0; x < entrada.getWidth(); x++){
			for(int y = 0; y < entrada.getHeight(); y++){
				
				if(entrada.getSample(x, y, 0) < T){
					sum = sum + entrada.getSample(x, y, 0);
					num++;
				}
				
			}
		}
		res = sum/num;
		return res;
	}
	
	public double mediaMayorOIgual(WritableRaster entrada, double T){
		double res;
		int sum = 0;
		int num = 0;
		for(int x = 0; x < entrada.getWidth(); x++){
			for(int y = 0; y < entrada.getHeight(); y++){
				
				if(entrada.getSample(x, y, 0) >= T){
					sum = sum + entrada.getSample(x, y, 0);
					num++;
				}
			}
		}
		res = sum/num;
		return res;
	}
	
	public BufferedImage invierte (BufferedImage imagen){
		BufferedImage imagenSalida = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
		WritableRaster rasterSalida = imagen.getRaster();
		for(int x = 0; x < rasterSalida.getWidth(); x++){
			for(int y = 0; y < rasterSalida.getHeight(); y++){
				if(rasterSalida.getSample(x, y, 0) == 0){
					rasterSalida.setSample(x, y, 0, 255);
				}else{
					rasterSalida.setSample(x, y, 0, 0);
				}
			}
			
		}
		imagenSalida.setData(rasterSalida);
		return imagenSalida;
	}
	
	
	
}
