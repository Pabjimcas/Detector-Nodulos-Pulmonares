package p1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.pearsoneduc.ip.op.BinaryStructElement;
import com.pearsoneduc.ip.op.StructElementException;
import com.pearsoneduc.ip.op.StructElementTypes;

public class Auxiliar {

	
	public static BufferedImage getImagen(String ruta){
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(ruta));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	public static void imprimeImagen(BufferedImage image){
		try {
			ImageIO.write(image, "bmp", new File("image.bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage segmenta(BufferedImage image,Umbral umbral){
	
        Segmentacion segme = new Segmentacion(image);
		
		int[][] etiquetas = new int[image.getHeight()][image.getWidth()];
		
		for(int i  = 0; i < image.getHeight();i++){
			for(int j = 0; j < image.getWidth(); j++){
				etiquetas[i][j] = 0;
			}
		}
		
		int[][] etiq = segme.etiqueta(etiquetas);
		int[][] etiq2 = segme.etiqueta(etiq);
		int[][] etiq3 = segme.etiqueta(etiq2);
		int[][] etiq4 = segme.etiqueta(etiq3);
		int[][] etiq5 = segme.etiqueta(etiq4);
		int[][] etiq6 = segme.reenumera(etiq5);
		
		Map<Integer,Set<Integer>> map = segme.mapaRegiones(etiq6);
		int[][] etiq7 = segme.reetiqueta(map, etiq6);
		int[][] etiq8 = segme.reenumera(etiq7);
    	Map<Integer,Integer> map2 = segme.calculaMapa(etiq8);

		Integer max = segme.indiceMaximo(map2);
		System.out.println(max);
		BufferedImage image3 = segme.muestraRegion(max, etiq8);
		BufferedImage image4 = umbral.invierte(image3);
		BufferedImage image5 = eliminaFondo(image4);
		
		return image5;
	}
	
	public static BufferedImage eliminaFondo(BufferedImage image){
		Segmentacion segme = new Segmentacion(image);
        int[][] etiquetas = new int[image.getHeight()][image.getWidth()];
		
		for(int i  = 0; i < image.getHeight();i++){
			for(int j = 0; j < image.getWidth(); j++){
				etiquetas[i][j] = 0;
			}
		}
		
		int[][] etiq = segme.etiqueta(etiquetas);
		
		int[][] etiq1 = segme.reenumera(etiq);
		Map<Integer,Set<Integer>> map = segme.mapaRegiones(etiq1);
		int[][] etiq2 = segme.reetiqueta(map, etiq1);
		int[][] etiq3 = segme.reenumera(etiq2);
		int[][] etiq4 = segme.eliminaEtiquetas(etiq3[256][511], etiq3);
		int[][] etiq5 = segme.eliminaEtiquetas(etiq4[0][0], etiq4);
		
		BufferedImage image2 = segme.imagenEtiquetada(etiq5);
		
		return image2;
	}
	public static BufferedImage TopHatYEliminaFondo(BufferedImage image,Umbral umbral,int elem) throws StructElementException{
		BinaryStructElement structElement = new BinaryStructElement(StructElementTypes.DISK_7x7);
		
		if(elem == 5){
			structElement = new BinaryStructElement(StructElementTypes.DISK_5x5);
		}else if(elem == 7){
			structElement = new BinaryStructElement(StructElementTypes.DISK_7x7);
		}
		
		
		BufferedImage apertura = Filtros.apertura(image, structElement);
		BufferedImage imagenInversa = umbral.invierte(apertura);
		BufferedImage clausura = Filtros.clausura(imagenInversa, structElement);
		BufferedImage imagenInversa2 = umbral.invierte(clausura);
		
		BufferedImage res = eliminaFondo(imagenInversa2);
		
		return res;
	}
	public static BufferedImage aplicaMascaraImagen(BufferedImage image,float gain,float bias){
		BufferedImage nuevaImagen = Auxiliar.getImagen("image.bmp");
		
		nuevaImagen = ImagenGris.rescale(nuevaImagen, gain,bias);
		
		BufferedImage res =Filtros.aplicaMascara(nuevaImagen, image);
		return res;
	}
	public static BufferedImage obtieneNodulos(BufferedImage gabor,float gain,float bias) throws StructElementException{
		BufferedImage nuevaImagen = Auxiliar.getImagen("image.bmp");
		
		nuevaImagen = ImagenGris.rescale(nuevaImagen, gain,bias);
		
		Vector<BufferedImage> nodulos = Filtros.sacarNodulos(nuevaImagen, gabor);
		return nodulos.get(1);
	}
}
