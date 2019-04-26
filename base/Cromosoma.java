package base;

public class Cromosoma {
	protected Arbol[] arbol;

	protected double fitness; // aptitud
	protected double punt; // puntRelat = aptitud / sumaAptitud
	protected double puntAcu; // para seleccion

	protected double adaptacion;
	private boolean maximizar = true;
	//protected boolean elite;

	private int pasos = 400;
	private int hMaxima = 10;
	private final String[] terminales = { "Avanza", "Derecha", "Izquierda" };
	private final String[] funciones = { "SiComida", "ProgN2", "ProgN3" };
	private int dir = 1;

	public Cromosoma() {
		inicializaCromosoma();
	}

	// ramped and half
	public void inicializaCromosoma() {
	}

	// creo que sobra
	public void insertar(int ciudad, int pos) {
		this.genes[pos].ciudad = ciudad;
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

		hMaxima = cromosoma.hMaxima;
		adaptacion = cromosoma.adaptacion;
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

	public int gethMaxima() {
		return hMaxima;
	}

	public void sethMaxima(int hMaxima) {
		this.hMaxima = hMaxima;
	}

	public double getPunt() {
		return punt;
	}

	// Para la depuracion
	public String toString(){
		String cromosoma = "(Adap: " + String.format("%.3f", adaptacion) + ", Fit: " + String.format("%.3f", fitness) + ")\n";
		for (int i = 0; i < nGenes - 1; i++){
			cromosoma += '[' + Integer.toString(i) + "] " + this.genes[i].toString() + ", ";
		}
		cromosoma += '[' + Integer.toString(nGenes - 1) + "] " + this.genes[nGenes - 1].toString();

		return cromosoma;
	}

	public double getAdptacion() {
		return adaptacion;
	}
}
