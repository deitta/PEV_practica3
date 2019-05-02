package tablero;

public class Hormiga {
	int ejeX;
	int ejeY;
	int dir;
	
	public Hormiga(int x, int y, int dir) {
		this.ejeX = x;
		this.ejeY = y;
		this.dir = dir;
	}

	public void gira(int giro) { // giro=-1 -> izq, giro=1 -> dch
		dir = (dir+giro+4)%4;
	} 
}
