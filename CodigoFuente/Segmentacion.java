package p1;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class Segmentacion {

	BufferedImage imagenEntrada;
	BufferedImage imagenSalida;
	WritableRaster rasterEntrada;
	Map<Integer,Integer> mapa = new HashMap<Integer,Integer>();

	int width;
	int height;
    Map<Integer,Set<Integer>> regiones = new HashMap<Integer,Set<Integer>>();
	
	int arrayY[] = {0,1,1,1,0,-1,-1,-1};
	int arrayX[] = {1,1,0,-1,-1,-1,0,1};
	int[][] etiquetas;
	int contador = 1;
	int contador2 = 1;
	
	public Segmentacion (BufferedImage entrada){
		imagenEntrada = entrada;
		etiquetas = new int[entrada.getHeight()][entrada.getWidth()];
		for(int i  = 0; i < imagenEntrada.getHeight();i++){
			for(int j = 0; j < imagenEntrada.getWidth(); j++){
				etiquetas[i][j] = 0;
			}
		}
		width = imagenEntrada.getWidth();
		height = imagenEntrada.getHeight();
		rasterEntrada = imagenEntrada.getRaster();
	}
	
	
	public int[][] etiqueta(int[][] etiquetas2){
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int j = 0;
				int k = 0;
				if(rasterEntrada.getSample(x, y, 0) > 0){
					if(x > 0 && y > 0 && x < (width -1) && y < (height -1)){

						for(int i=0; i < 8; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas2[j][k] > 0){
								
								etiquetas2[x][y] = etiquetas2[j][k]; 
							}
						}
					}else if(x == 0 && y > 0 && y < (height -1)){
	
						for(int i = 0; i < 3; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas2[j][k] > 0){
								
								etiquetas2[x][y] = etiquetas2[j][k];
							}
						}
						for(int i = 6; i < 8; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas2[j][k] > 0){
								
								etiquetas2[x][y] = etiquetas2[j][k];
							}
						}
					}else if(y == 0 && x > 0 && x < (width -1)){
	
						for(int i = 0; i < 5; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas2[j][k] > 0){
								
								etiquetas2[x][y] = etiquetas2[j][k];
							}
						}
						
					}else if(x==0 && y == 0){

						for(int i = 0; i < 3; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas2[j][k] > 0){
								
								etiquetas2[x][y] = etiquetas2[j][k];
							}
						}
					}else if(x == (width-1) && y == 0){
	
						for(int i = 2; i < 5; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas2[j][k] > 0){
								
								etiquetas2[x][y] = etiquetas2[j][k];
							}
						}
						
					}else if(x == 0 && y == (height - 1)){

						for(int i = 6; i < 8; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas2[j][k] > 0){
								
								etiquetas2[x][y] = etiquetas2[j][k];
							}
						}
						if(etiquetas2[x + arrayX[0]][y]>0){
							etiquetas2[x][y] = etiquetas2[x + arrayX[0]][y];
						}
						
					}else if(x == width && y == height){
						for(int i = 3; i < 6; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas2[j][k] > 0){
								
								etiquetas2[x][y] = etiquetas2[j][k];
							}
					
						}
				    }else if(x == (width - 1) && y > 0 && y < (height-1)){
				    	for(int i = 2; i < 7; i++){
				    		j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas2[j][k] > 0){
								
								etiquetas2[x][y] = etiquetas2[j][k];
							}
				    	}
				    }else if(x > 0 && y == (height -1) && x < (width -1)){
				    	for(int i = 4; i < 8; i++){
				    		j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas2[j][k] > 0){
								
								etiquetas2[x][y] = etiquetas2[j][k];
							}
				    	}
				    	if(etiquetas2[x + arrayX[0]][y]>0){
				    		etiquetas2[x][y] = etiquetas2[x + arrayX[0]][y];
				    	}
				    }
					if(etiquetas2[x][y]==0){
						etiquetas2[x][y] = contador;
						contador++;
					}
			}
			}
		
	}
		return etiquetas2;
	}
	
	public Map<Integer,Set<Integer>> mapaRegiones(int[][]etiquetas6){
		Set<Integer> conj = new HashSet<Integer>();
		int j = 0;
		int k = 0;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(etiquetas6[x][y]>0){
					if(regiones.containsKey(etiquetas6[x][y])){
						conj = regiones.get(etiquetas6[x][y]);
					}else{
						conj = new HashSet<Integer>();	
					}
					if(x > 0 && y > 0 && x < (width -1) && y < (height -1)){

						for(int i=0; i < 8; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas6[j][k] > 0 && etiquetas6[j][k] != etiquetas6[x][y]){
								conj.add(etiquetas6[j][k]);
							}
						}
					}else if(x == 0 && y > 0 && y < (height -1)){
	
						for(int i = 0; i < 3; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas6[j][k] > 0 && etiquetas6[j][k] != etiquetas6[x][y]){
								
								conj.add(etiquetas6[j][k]);
							}
						}
						for(int i = 6; i < 8; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas6[j][k] > 0 && etiquetas6[j][k] != etiquetas6[x][y]){
								conj.add(etiquetas6[j][k]);
							}
						}
					}else if(y == 0 && x > 0 && x < (width -1)){
	
						for(int i = 0; i < 5; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas6[j][k] > 0 && etiquetas6[j][k] != etiquetas6[x][y]){
								conj.add(etiquetas6[j][k]);
							}
						}
						
					}else if(x==0 && y == 0){

						for(int i = 0; i < 3; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas6[j][k] > 0 && etiquetas6[j][k] != etiquetas6[x][y]){
								conj.add(etiquetas6[j][k]);
							}
						}
					}else if(x == (width-1) && y == 0){
	
						for(int i = 2; i < 5; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas6[j][k] > 0 && etiquetas6[j][k] != etiquetas6[x][y]){
								conj.add(etiquetas6[j][k]);
							}
						}
						
					}else if(x == 0 && y == (height - 1)){

						for(int i = 6; i < 8; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas6[j][k] > 0 && etiquetas6[j][k] != etiquetas6[x][y]){
								conj.add(etiquetas6[j][k]);
							}
						}
						if(etiquetas6[x + arrayX[0]][y]>0 && etiquetas6[x + arrayX[0]][y] != etiquetas6[x][y]){
							etiquetas6[x][y] = etiquetas6[x + arrayX[0]][y];
						}
						
					}else if(x == (width) && y == (height)){
						for(int i = 3; i < 6; i++){
							j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas6[j][k] > 0 && etiquetas6[j][k] != etiquetas6[x][y]){
								conj.add(etiquetas6[j][k]);
							}
						}
				    }else if(x == (width - 1) && y > 0 && y < (height-1)){
				    	for(int i = 2; i < 7; i++){
				    		j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas6[j][k] > 0 && etiquetas6[j][k] != etiquetas6[x][y]){
								conj.add(etiquetas6[j][k]);
							}
				    	}
				    }else if(x > 0 && y == (height -1) && x < (width -1)){
				    	for(int i = 4; i < 8; i++){
				    		j = x + arrayX[i];
							k = y + arrayY[i];
							if(etiquetas6[j][k] > 0 && etiquetas6[j][k] != etiquetas6[x][y]){
								conj.add(etiquetas6[j][k]);
							}
				    	}
				    	if(etiquetas6[x + arrayX[0]][y]>0 && etiquetas6[x + arrayX[0]][y] != etiquetas6[x][y]){
				    		etiquetas6[x][y] = etiquetas6[x + arrayX[0]][y];
				    	}
				    }
					regiones.put(etiquetas6[x][y], conj);
				}
			}
		}
		
		return regiones;
	}
	
	public int[][] reetiqueta(Map<Integer,Set<Integer>> mp, int[][] etiquetas2){
		Set<Integer> conj = new HashSet<Integer>();
		Map<Integer,Set<Integer>> mapa = new HashMap<Integer,Set<Integer>>();
		List<Set<Integer>> conjuntos = new ArrayList<Set<Integer>>();
		List<Map<Integer,Set<Integer>>> lista = new ArrayList<Map<Integer,Set<Integer>>>();
		Set<Integer> conj2 = new HashSet<Integer>();
		for(int i = 1; i <= mp.size();i++){
			conj = mp.get(i);
			if(!conj.isEmpty()){
				mapa = remapea(mp,i,conj);
				if(!conjuntos.contains(mapa.get(i))){
					conjuntos.add(mapa.get(i));
					lista.add(mapa);
				}
				
			}
		}
		for(int j = 0; j < lista.size();j++){
			List<Integer> keyList = new ArrayList<Integer>(lista.get(j).keySet());
			conj2 = lista.get(j).get(keyList.get(0));
			System.out.println(conj2);
			for(int y = 0; y < height ; y++){
				for(int x = 0; x < width; x++){
					
					if(conj2.contains(etiquetas2[x][y])){
						etiquetas2[x][y] = keyList.get(0);
					}
				}
			}
		}
		
		return etiquetas2;
	}
	
	
	
	public Map<Integer,Set<Integer>> remapea(Map<Integer,Set<Integer>> mp,int indice, Set<Integer> conj){
		Map<Integer,Set<Integer>> mapa = new HashMap<Integer,Set<Integer>>();
		Set<Integer> cerrados = new HashSet<Integer>();
		List<Integer> abiertos = new ArrayList<Integer>();
		int actual = 0;
		cerrados.addAll(conj);
		abiertos.addAll(conj);
		while(!abiertos.isEmpty()){
		
			actual = abiertos.get(abiertos.size()-1);
			abiertos.remove(abiertos.size()-1);
		
			if(!cerrados.containsAll(mp.get(actual))){
				abiertos.addAll(mp.get(actual));
				cerrados.addAll(mp.get(actual));
			}
		}
		mapa.put(indice, cerrados);
		return mapa;
	}
	
	
	public int[][] reenumera(int[][] etiquetas3){
		
		for(int y = 0; y < height ; y++){
			
			for(int x = 0; x < width; x++){
				
				if(etiquetas3[x][y] > 0){
					
					if(mapa.containsKey(etiquetas3[x][y])){
						etiquetas3[x][y] = mapa.get(etiquetas3[x][y]);
					}else{
						mapa.put(etiquetas3[x][y], contador2);
						etiquetas3[x][y] = contador2;
						contador2++;
					}
				}
			}
		}
		return etiquetas3;
	}
	
	public Map<Integer,Integer> calculaMapa(int[][] et){
		Map<Integer,Integer> mapa2 = new HashMap<Integer,Integer>();
		Integer val;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(et[x][y]>0){
					if(mapa2.containsKey(et[x][y])){
						val = mapa2.get(et[x][y]);
						val++;
						mapa2.put(et[x][y], val);
					}else{
						mapa2.put(et[x][y], 1);
					}
			}
		}
		
		}
		return mapa2;
	}
	public Integer indiceMaximo(Map<Integer,Integer> map){
		Integer maximo = 0;
		Integer indice = 0;
		Integer res = 0;
		Set<Integer> conj = map.keySet();
		List<Integer> indices = new ArrayList<Integer>(conj);
		for(int i = 0; i < indices.size(); i++){
			indice = indices.get(i);
			if(map.get(indice) > maximo){
				maximo = map.get(indice);
				res = indice;
			}
		}
		return res;
	}
	
	public BufferedImage muestraRegion(int i,int[][] etiquetas4){
		BufferedImage salida = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster rasterSalida = salida.getRaster();
		
		for(int y = 0; y <height; y++){
			for(int x = 0; x < width; x++){
				if(etiquetas4[x][y] == i){
					rasterSalida.setSample(x, y, 0, 255);
				}else{
					rasterSalida.setSample(x, y, 0, 0);
				}
			}
		}
		salida.setData(rasterSalida);
		return salida;
		
	}
	
	public int[][] eliminaEtiquetas(int indice, int [][] etiquetas3){
		
		for(int y = 0; y <height; y++){
			for(int x = 0; x < width; x++){
				if(etiquetas3[x][y] == indice){
					etiquetas3[x][y] = 0;
				}
			}
		}
		return etiquetas3;
	}
	
	
	public BufferedImage imagenEtiquetada(int[][] etiquetas5){
		BufferedImage salida = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster rasterSalida = salida.getRaster();
		
		for(int y = 0; y <height; y++){
			for(int x = 0; x < width; x++){
				if(etiquetas5[x][y] > 0){
					rasterSalida.setSample(x, y, 0, 255);
				}else{
					rasterSalida.setSample(x, y, 0, 0);
				}
			}
		}
		salida.setData(rasterSalida);
		return salida;
	}


}


