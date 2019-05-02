package tablero;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import base.Arbol;

public class Tablero {
	TEstado tablero[][];
	static enum TEstado {
		NADA, HAYCOMIDA, HORMIGA, HABIACOMIDA, CAMINADA;
	}
	Hormiga hor;
	
	String estado; //0->nada, #->sihaycomida, @->hormiga
	

	public Tablero() {
		tablero = new TEstado[32][32];
		restauraTablero();
	}
	
	public void recorreTablero(Arbol arbol) {
		
	}

	public void restauraTablero() {
		File archivo = new File ("SantaFe.txt");
		FileReader fr;
		try {
			fr = new FileReader (archivo);
			int f = 0, c = 0; // f -> fila, c -> columna
			char casilla = (char) fr.read();

			while(fr.read() != -1) {
				switch(casilla) {
				case '0': tablero[c][f] = TEstado.values()[0]; break;
				case '#': tablero[c][f] = TEstado.values()[1]; break;
				case '@': {
					tablero[c][f] = TEstado.values()[2];
					hor = new Hormiga(f, c, 1);
					break;
				}
				default: System.out.println("ERROR LECTURA ARCHIVO");break;
				}
				
				casilla = (char) fr.read();
				f = (f+1)%32;
				if (f == 0) c++;
			}
			tablero[31][31] = TEstado.values()[0];
			
			fr.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public String toString() {
		String s = "";
		
		for (int i = 0; i < 32; i++) {
			for (int j = 0; j < 32; j++) {
				s += tablero[i][j].name();
				if (j < 31) s += " ";
				else s += "\n";
			}
		}
		
		return s;
	}
	
	// para depurar
	public static void main(String args[]) {
		Tablero t = new Tablero();
		System.out.println(t.toString());
	}
}
