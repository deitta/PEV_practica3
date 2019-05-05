package tablero;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import base.Arbol;
import tablero.Hormiga.TDireccion;

public class Tablero {
	private TEstado tablero[][];
	public static enum TEstado {
		NADA, HAYCOMIDA, HABIACOMIDA, CAMINADA;
		
		public String toString() {
			switch(this) {
			case CAMINADA:
				return "-";
			case HABIACOMIDA:
				return "x";
			case HAYCOMIDA:
				return "#";
			case NADA:
				return " ";
			default:
				return "\nERROR\n";
			}
		}
	}
	Hormiga hor;

	public Tablero() {
		setTablero(new TEstado[32][32]);
		restauraTablero();
	}
	
	public void recorreTablero(Arbol arbol) {
		switch(arbol.getDato()) {
		case AVANZA:
			hor.avanza();
			if (tablero[hor.ejeY][hor.ejeX] == TEstado.HAYCOMIDA || tablero[hor.ejeY][hor.ejeX] == TEstado.HABIACOMIDA) tablero[hor.ejeY][hor.ejeX] = TEstado.HABIACOMIDA;
			else tablero[hor.ejeY][hor.ejeX] = TEstado.CAMINADA;
			break;
		case GIRA_DERECHA:
			hor.giraDch();
			break;
		case GIRA_IZQUIERDA:
			hor.giraIzq();
			break;
		case SIC:
			Hormiga horAux = new Hormiga(hor.ejeX, hor.ejeY, hor.dir);
			horAux.avanza();
			if (tablero[horAux.ejeY][horAux.ejeX] == TEstado.HAYCOMIDA)
				recorreTablero(arbol.getHi());
			else recorreTablero(arbol.getHd());
			break;
		case PROGN2:
			recorreTablero(arbol.getHi());
			recorreTablero(arbol.getHd());
			break;
		case PROGN3:
			recorreTablero(arbol.getHi());
			recorreTablero(arbol.getHc());
			recorreTablero(arbol.getHd());
			break;
		}
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
				case '0': getTablero()[f][c] = TEstado.values()[0]; break;
				case '#': getTablero()[f][c] = TEstado.values()[1]; break;
				case '@': {
					getTablero()[f][c] = TEstado.values()[2];
					hor = new Hormiga(f, c, TDireccion.ESTE);
					break;
				}
				default: System.out.println("ERROR LECTURA ARCHIVO");break;
				}
				
				casilla = (char) fr.read();
				c = (c+1)%32;
				if (c == 0) f++;
			}
			getTablero()[31][31] = TEstado.values()[0];
			
			fr.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public String toString() {
		String s = "";
		
		for (int f = 0; f < 32; f++) {
			for (int c = 0; c < 32; c++) {
				s += getTablero()[f][c].toString();
				if (c < 31) s += " ";
				else s += "\n";
			}
		}
		
		return s;
	}
	
	// para depurar
	public static void main(String args[]) {
		Tablero t = new Tablero();
		Arbol a = new Arbol(400);
		a.creaArbol(a, 2, 9);
		System.out.println("Arbol: " + a.toString());
		System.out.println();
		System.out.println("Tablero inicial:\n" + t.toString());
		System.out.println();
		
		t.recorreTablero(a);
		System.out.println("Tablero final:\n" + t.toString());	
	}

	public TEstado[][] getTablero() {
		return tablero;
	}

	public void setTablero(TEstado tablero[][]) {
		this.tablero = tablero;
	}
}
