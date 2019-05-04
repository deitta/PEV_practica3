package tablero;

public class Hormiga {
	int ejeX;
	int ejeY;
	public static enum TDireccion {
		NORTE, ESTE, SUR, OESTE;
	}
	TDireccion dir;
	
	public Hormiga(int x, int y, TDireccion dir) {
		this.ejeX = x;
		this.ejeY = y;
		this.dir = dir;
	}
	
	public void avanza() {
		switch(dir) {
		case ESTE:
			ejeX = (ejeX+1)%32;
			break;
		case NORTE:
			ejeY = (ejeY-1+32)%32;
			break;
		case OESTE:
			ejeX = (ejeX-1+32)%32;
			break;
		case SUR:
			ejeY = (ejeY+1)%32;
			break;
		}
	}

	public void giraDch() {
		gira(1);
	}

	public void giraIzq() {
		gira(-1);
	}

	private void gira(int giro) { // giro=-1 -> izq, giro=1 -> dch
		dir = TDireccion.values()[(dir.ordinal()+giro+4)%4];
	}
}
