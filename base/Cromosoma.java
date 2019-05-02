package base;

public class Cromosoma {
	protected Arbol arbol;

	protected double fitness; // aptitud
	protected double punt; // puntRelat = aptitud / sumaAptitud
	protected double puntAcu; // para seleccion

	protected double adaptacion;
	private boolean maximizar = true;
	//protected boolean elite;

	private final int pasosMax = 400;
	private final static int hMax = 9; // profundidad maxima que puede tener el arbol para 400 hojas

	public Cromosoma(int profMin, int profMax) {
		arbol = new Arbol(pasosMax);
		arbol.creaArbol(arbol, profMin, profMax);
	}

	public Cromosoma() {
		arbol = new Arbol(pasosMax);
	}

	public int evaluaCromosoma(){
		int fitness = 0, ciudad = 25;

		for (int i = 0; i < nGenes; i++){
			fitness += Ciudades.distanciaA(genes[i].ciudad, ciudad);
			ciudad = genes[i].ciudad;
		}

		return fitness;
	}

	public void copiaCromosoma(Cromosoma cromosoma) {
		//copia arbol
		as

		fitness = cromosoma.fitness;
		punt = cromosoma.punt;
		puntAcu = cromosoma.puntAcu;

		adaptacion = cromosoma.adaptacion;
	}

	public static int gira(int dir, int giro) { // giro=-1 -> izq, giro=1 -> dch
		return (dir+giro+4)%4;
	} 

// SET & GET

	public boolean isMaximizar(){
		return maximizar;
	}

	public double getFitness(){
		return this.fitness;
	}

	public void setFitness(double fitness){
		this.fitness = fitness;
	}

	public double getPuntAcu() {
		return puntAcu;
	}

	public static int gethMax() {
		return hMax;
	}

	public double getPunt() {
		return punt;
	}

	// Para la depuracion
	public String toString(){
		return arbol.toString();
	}

	public double getAdptacion() {
		return adaptacion;
	}
}
