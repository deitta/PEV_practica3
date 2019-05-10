package base;

import tablero.Tablero;

public class Cromosoma {
	private Arbol arbol;

	protected double fitness; // aptitud
	protected double punt; // puntRelat = aptitud / sumaAptitud
	protected double puntAcu; // para seleccion

	protected double adaptacion;
	private boolean maximizar = true;
	//protected boolean elite;

	private final int pasosMax = 400;
	private int hMax; // profundidad maxima que puede tener el arbol para 400 hojas

	public Cromosoma(int profMin, int profMax, int hMax) {
		setArbol(new Arbol(pasosMax, 0));
		getArbol().creaArbol(getArbol(), profMin, profMax);
		this.hMax = hMax;
	}

	public Cromosoma() {}

	public double evaluaCromosoma(){
		Tablero t = new Tablero();
		fitness = 0;

		t.recorreTablero(getArbol());
		
		for (int i = 0; i < 32; i++){
			for (int j = 0; j < 32; j++) {
				if (t.getTablero()[i][j] == Tablero.TEstado.HABIACOMIDA) fitness++;
			}
		}

		return fitness;
	}

	public void copiaCromosoma(Cromosoma cromosoma) {
		setArbol(new Arbol());
		getArbol().copiaArbol(cromosoma.getArbol());

		fitness = cromosoma.fitness;
		punt = cromosoma.punt;
		puntAcu = cromosoma.puntAcu;

		adaptacion = cromosoma.adaptacion;
		hMax = cromosoma.hMax;
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

	public double getPunt() {
		return punt;
	}

	public double getAdptacion() {
		return adaptacion;
	}

	public Arbol getArbol() {
		return arbol;
	}

	public void setArbol(Arbol arbol) {
		this.arbol = arbol;
	}

	public int getPasosMax() {
		return pasosMax;
	}

	public int getHmax() {
		return hMax;
	}

	// Para la depuracion
	public String toString(){
		String s = "";
		if (getArbol() != null) s = getArbol().toString();
		return s;
	}
}
