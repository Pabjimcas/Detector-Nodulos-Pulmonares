package p1;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

import com.pearsoneduc.ip.op.StructElementException;


public class Prueba {

	public static void main(String[] args) throws IOException,
			StructElementException {

		BufferedImage image = Auxiliar.getImagen("Imagen TAC de Entrada");

		image = ImagenGris.rescale(image, 1F, 1F);

		Auxiliar.imprimeImagen(image);

		Umbral umbral = new Umbral(image);
		BufferedImage image2 = umbral.binarizaImagen();

		BufferedImage image5 = Auxiliar.segmenta(image2, umbral);

		BufferedImage salgo = Auxiliar.TopHatYEliminaFondo(image5, umbral, 7);

		BufferedImage pruebame = Auxiliar.aplicaMascaraImagen(salgo, 1F, 1f);

		BufferedImage gabor = Filtros.filtroGabor(9, 9, pruebame, 90, 1, 1, 1,0);

		BufferedImage nodulos = Auxiliar.obtieneNodulos(gabor, 1F, 1F);

		ImageIO.write(nodulos, "bmp", new File("nodulos.bmp"));

	}
}
