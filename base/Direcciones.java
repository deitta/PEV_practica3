package base;

public class Direcciones {
	private static enum dir {NORTE, ESTE, SUR, OESTE};
	
	public static int gira(int dir, int giro) { // giro=-1 -> izq, giro=1 -> dch
		return (dir+giro+4)%4;
	} 
}
