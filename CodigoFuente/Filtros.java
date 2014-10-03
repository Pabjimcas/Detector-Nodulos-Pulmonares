package p1;


import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.util.Vector;


import com.pearsoneduc.ip.op.BinaryDilateOp;
import com.pearsoneduc.ip.op.BinaryErodeOp;
import com.pearsoneduc.ip.op.BinaryStructElement;
import com.pearsoneduc.ip.op.StructElementException;
import com.pearsoneduc.ip.op.StructElementTypes;

public class Filtros {

	
	
	public static BufferedImage apertura(BufferedImage entrada, BinaryStructElement structElement){
		BufferedImage imagenSalida1 = new BufferedImage(entrada.getWidth(), entrada.getHeight(), entrada.getType());
		BufferedImage imagenSalida2 = new BufferedImage(entrada.getWidth(), entrada.getHeight(), entrada.getType());
		BufferedImageOp erosion = new BinaryErodeOp(structElement);
		BufferedImageOp dilatacion = new BinaryDilateOp(structElement);
		
		imagenSalida1 = erosion.filter(entrada, imagenSalida1);
		imagenSalida2 = dilatacion.filter(imagenSalida1,imagenSalida2);
		return imagenSalida2;
	}
	public static BufferedImage clausura(BufferedImage entrada, BinaryStructElement structElement){
		BufferedImage imagenSalida1 = new BufferedImage(entrada.getWidth(), entrada.getHeight(), entrada.getType());
		BufferedImage imagenSalida2 = new BufferedImage(entrada.getWidth(), entrada.getHeight(), entrada.getType());
		BufferedImageOp erosion = new BinaryErodeOp(structElement);
		BufferedImageOp dilatacion = new BinaryDilateOp(structElement);
		
		imagenSalida1 = dilatacion.filter(entrada,imagenSalida1);
		imagenSalida2 = erosion.filter(imagenSalida1, imagenSalida2);
		return imagenSalida2;
	}
	public static BufferedImage aplicaMascara(BufferedImage entrada, BufferedImage mascara){
		BufferedImage imagenSalida = new BufferedImage(entrada.getWidth(), entrada.getHeight(), entrada.getType());
		WritableRaster rasterMascara = mascara.getRaster();
		WritableRaster rasterEntrada = entrada.getRaster();
		for(int x = 0; x < mascara.getWidth(); x++){
			for(int y = 0; y < mascara.getHeight(); y++){
				if(rasterMascara.getSample(x, y, 0) != 255){
					rasterEntrada.setSample(x, y, 0, 0);	
				}
			}
		}
		imagenSalida.setData(rasterEntrada);
		return imagenSalida;
	}
	
	public static BufferedImage filtroGabor(int xTam,int yTam,BufferedImage entrada,float orientacion,float varianza,float anchoBanda, float elipcidad, float desfase){
		
		float x1,y1;
		float[] arrayGabor = new float[xTam*yTam];
		float gaborReal;
		
		int contador = 0;
		for(int x = 0; x < xTam; x++){
			for(int y = 0; y < yTam; y++){
				x1 = (float) (x*Math.cos(orientacion) + y*Math.sin(orientacion));
				y1 = (float) (-x*Math.sin(orientacion) + y*Math.cos(orientacion));
				
				gaborReal = (float) ((Math.exp(-((Math.pow(x1, 2) + (Math.pow(elipcidad, 2)*Math.pow(y1, 2)))/(2*Math.pow(varianza, 2)))))*(Math.cos(((2*Math.PI*x1)/(anchoBanda)) + desfase)));
				
				arrayGabor[contador] = gaborReal;
				System.out.println(arrayGabor[contador]);
				contador++;
			}
		}
		Kernel mascara = new Kernel(xTam,yTam,arrayGabor);
		ConvolveOp conv = new ConvolveOp(mascara);
		BufferedImage imagenSalida = conv.filter(entrada, null);
		
		
		return imagenSalida;
	}
	
	public static Vector<BufferedImage> sacarNodulos(BufferedImage original,BufferedImage gabor) throws StructElementException{
		Vector<BufferedImage> resultado = new Vector<BufferedImage>();
		BufferedImage imagen = new BufferedImage(gabor.getWidth(), gabor.getHeight(), gabor.getType());
		BufferedImage imagen2 = new BufferedImage(gabor.getWidth(), gabor.getHeight(), gabor.getType());
		WritableRaster rastergabor = gabor.getRaster();
		Vector<Vector<Integer>> puntosACambiar = new Vector<Vector<Integer>>();
		int negros = 0;
	
		
		//Contamos los negros y guardo un vector con sus posiciones
		for(int i = 0; i<gabor.getHeight();i++){
			for(int j = 0; j<gabor.getWidth();j++){
				if(rastergabor.getSample(i, j, 0) == 0){
					negros++;
					Vector<Integer> punto = new Vector<Integer>(); 
					punto.add(i);punto.add(j);
					puntosACambiar.add(punto);
					
				}
			}
		}
		
		negros = negros/2;
		
		//Eliminamos la mitad de los negros
		for(int i = 0; i<gabor.getHeight();i++){
			for(int j = 0; j<gabor.getWidth();j++){
				if(rastergabor.getSample(i, j, 0) == 0){
					rastergabor.setSample(i, j, 0, 255);
					negros--;
					
				}
				if(negros < 0){
					break;
				}
			}
			if(negros < 0){
				break;
			}
		}
		
		imagen.setData(rastergabor);
		
		Umbral ultimoPaso = new Umbral(imagen);
		
		//Binarizamos la imagen
		
		BufferedImage imagenUmbralizada = ultimoPaso.binarizaImagen();
		rastergabor = imagenUmbralizada.getRaster();
		
		
		//Añadimos los negros que eliminamos
		
		
		for(Vector<Integer> punto: puntosACambiar){
			rastergabor.setSample(punto.get(0), punto.get(1), 0, 0);
		}
		imagenUmbralizada.setData(rastergabor);
		
		
		//Eliminamos pequeños desechos de la binarización con apertura
		
		BinaryStructElement elemento;
		elemento = new BinaryStructElement(1);
		imagenUmbralizada = Filtros.apertura(imagenUmbralizada, elemento);
		
		
		
		BufferedImage imagenUmbralizadaGrande = new BufferedImage(gabor.getWidth(), gabor.getHeight(), gabor.getType());
		BufferedImage imagenUmbralizadaMayor = new BufferedImage(gabor.getWidth(), gabor.getHeight(), gabor.getType());
	
		
		//Creamos objeto para dilatar
		
		BinaryStructElement structElement;
		
		structElement = new BinaryStructElement(StructElementTypes.DISK_5x5);

		
		BufferedImageOp dilatacion = new BinaryDilateOp(structElement);
		
		
		//Dilatamos
		
		dilatacion.filter(imagenUmbralizada, imagenUmbralizadaGrande);
		dilatacion.filter(imagenUmbralizadaGrande, imagenUmbralizadaMayor);
		
		//Señalamos
		
		WritableRaster rasterGrande = imagenUmbralizadaGrande.getRaster();
		WritableRaster rasterMayor = imagenUmbralizadaMayor.getRaster();
		
			
		
		
		for(int i = 0; i<gabor.getHeight();i++){
			for(int j = 0; j<gabor.getWidth();j++){
				if(rasterGrande.getSample(i, j, 0) == 255){
					rasterMayor.setSample(i, j, 0, 0);
				}
			}
		}
		imagenUmbralizadaMayor.setData(rasterMayor);
		
		WritableRaster rastergabor2 = Filtros.filtroGabor(9,9,original,90,1,1,1,0).getRaster();
		
		for(int i = 0; i<gabor.getHeight();i++){
			for(int j = 0; j<gabor.getWidth();j++){
				if(rasterMayor.getSample(i, j, 0) == 255){
					rastergabor2.setSample(i, j, 0, 0);
				}
			}
		}
		imagen2.setData(rastergabor2);
		
		 resultado.add(imagenUmbralizada);
		 resultado.add(imagen2);
		 
	
		 return resultado;
		
		}
	
	
}
