package base;

import metodosCruce.AlgoritmoCruce;
import metodosMutacion.AlgoritmoMutacion;
import metodosMutacion.FactoriaMutacion;
import metodosSeleccion.AlgoritmoSeleccion;
import metodosSeleccion.FactoriaSeleccion;

public class AlgoritmoGenetico {
	Cromosoma[] pob;
	Cromosoma elMejor;
	int tamPob;
	int numMaxGen; //# max de generaciones
	int posMejor;
	double probCruce; //si es menor cruza
	double probMutacion; // si es menor muta
	int hMax;
	static int mediaTamPob;

	String seleccion;
	String cruce;
	String mutacion;
	int participantes;
	
	double elitismo; // elitismo*tamPob = tamElite
	Cromosoma[] elite;


	// Para graficas
	double[] genMedia; //rojo
	double[] genMejor; //verde
	double[] mejorAbsoluto; //azul

	boolean contractividad;
	
	int generacionActual;

	public static int numMuts;
	public static int numCruces;
	int ejecucion = 0;

	public AlgoritmoGenetico() {
		tamPob = 100;
		numMaxGen = 300;
		posMejor = 0;
		probCruce = 0.6;
		probMutacion = 0.5;
		hMax = 4;
		seleccion = "Ranking";
		cruce = "PMX";
		mutacion = "Inicializacion";
		participantes = 3;
		elitismo = 0.03;
		contractividad = false;
		

		// en principio lo de abajo no es necesario pero si se quita da error por toString
		pob = new Cromosoma[tamPob];
		elite = new Cromosoma[(int) (tamPob*elitismo)];

		for (int i = 0; i < tamPob; i++)
			pob[i] = new Cromosoma();
		for (int i = 0; i < (int) (tamPob*elitismo); i++)
			elite[i] = new Cromosoma();
		elMejor = new Cromosoma();
	}

	public void inicializaPoblacion() {
		genMedia = new double[numMaxGen];
		genMejor = new double[numMaxGen];
		mejorAbsoluto = new double[numMaxGen];
		
		pob = new Cromosoma[tamPob];
		elite = new Cromosoma[(int) (tamPob*elitismo)];

		int nGrupos = (hMax - 1), tamGrupos = tamPob/nGrupos;
		
		for (int i = 0; i < nGrupos; i++) {
			for (int j = 0; j < Math.ceil(tamGrupos/2); j++) { // inicializacion creciente
				pob[j+tamGrupos*i] = new Cromosoma(0, i+2, hMax);
				mediaTamPob += pob[j+tamGrupos*i].getArbol().getNum_nodos();
			}
			for (int j = (int) Math.ceil(tamGrupos/2); j < tamGrupos; j++) { // inicializacion completa
				pob[j+tamGrupos*i] = new Cromosoma(i+2, i+2, hMax);
				mediaTamPob += pob[j+tamGrupos*i].getArbol().getNum_nodos();
			}
		}


		for (int i = tamGrupos*nGrupos; i < tamPob; i++)
			pob[i] = new Cromosoma(1, 5, hMax);

		for (int i = 0; i < (int) (tamPob*elitismo); i++)
			elite[i] = new Cromosoma();

		mediaTamPob = mediaTamPob / tamPob;

		for (int i = 0; i < tamPob ; i++) { 
			pob[i].fitness = pob[i].evaluaCromosoma();
		}
		
		
		elMejor = new Cromosoma();
		elMejor.copiaCromosoma(pob[0]);
	}

	public void evalua(){
		double puntAcu = 0; // puntuacion acumulada
		double sumaAptitud = 0; // suma de la aptitud

		sumaAptitud = pob[0].adaptacion;
		posMejor = 0;
		for (int i = 1; i < tamPob; i++){
			sumaAptitud += pob[i].adaptacion;
			if (pob[i].adaptacion > pob[posMejor].adaptacion){
				posMejor = i;
			}
		}

		for (int i = 0; i < tamPob; i++){
			pob[i].punt = pob[i].adaptacion / sumaAptitud;
			pob[i].puntAcu = pob[i].punt + puntAcu;
			puntAcu += pob[i].punt;
		}

		if(pob[posMejor].adaptacion > elMejor.adaptacion)
				elMejor.copiaCromosoma(pob[posMejor]);
	}

	private void adaptarMinimizacion(int tamElite){
		double fmax = pob[0].fitness;

		for (int i = 1; i < tamPob; i++)
			if (pob[i].fitness > fmax)
				fmax = pob[i].fitness;
		for (int i = 0; i < tamElite; i++)
			if (elite[i].fitness > fmax)
				fmax = elite[i].fitness;
		if (elMejor.fitness > fmax)
			fmax = elMejor.fitness;

		fmax *= 1.05; // margen para evitar suma adaptacion = 0

		// adapta la poblacion
		for (int i = 0; i < tamPob; i++)
			pob[i].adaptacion = fmax - pob[i].fitness;

		// adapta la elite
		for (int i = 0; i < tamElite; i++)
			elite[i].adaptacion = fmax - elite[i].fitness;

		// adapta el mejor
		elMejor.adaptacion = fmax - elMejor.fitness;
	}

	private void adaptarMaximizacion(int tamElite){
		double fmin = pob[0].fitness;

		for (int i = 1; i < tamPob; i++)
			if (pob[i].fitness < fmin)
				fmin = pob[i].fitness;
		for (int i = 0; i < tamElite; i++)
			if (elite[i].fitness < fmin)
				fmin = elite[i].fitness;
		if (elMejor.fitness < fmin)
			fmin = elMejor.fitness;

		fmin = Math.abs(fmin * 1.05); // margen para evitar suma adaptacion = 0
		if (fmin == 0)
			fmin = 1.05;
		// adapta la poblacion
		for (int i = 0; i < tamPob; i++)
			pob[i].adaptacion = pob[i].fitness + fmin;

		// adapta la elite
		for (int i = 0; i < tamElite; i++)
			elite[i].adaptacion = elite[i].fitness + fmin;

		// adapta el mejor
		elMejor.adaptacion = elMejor.fitness + fmin;
	}

	public void seleccion(){
		Cromosoma[] nuevaPob = new Cromosoma[tamPob];

		for (int i = 0; i < tamPob; i++)
			nuevaPob[i] = new Cromosoma();

		AlgoritmoSeleccion algoSeleccion = FactoriaSeleccion.getAlgoritmoDeSeleccion(seleccion, participantes);
		algoSeleccion.seleccion(pob, nuevaPob, tamPob);
		pob = nuevaPob;
	}

	public void cruce(){
		AlgoritmoCruce.cruceBase(pob, tamPob, probCruce);
	}

	public void mutacion(){
		AlgoritmoMutacion algoMutacion;
		algoMutacion = FactoriaMutacion.getAlgoritmoDeMutacion(mutacion);
		algoMutacion.mutacion(pob, probMutacion, tamPob);
	}

	public void separaElite(int tamElite){
		int posPeor = 0; // apunta al peor individuo dentro de la elite

		// coge a los primeros de la poblacion como a la elite
		// buscamos el peor dentro de la elite
		for (int i = 0; i < tamElite; i++){
			elite[i].copiaCromosoma(pob[i]);
			if (elite[i].adaptacion < elite[posPeor].adaptacion) posPeor = i;
		}

		// recorre la poblacion buscando a los mejores individuos
		for (int i = tamElite; i < tamPob; i++){
			// si encontramos a algun individuo mejor que el peor de la elite este ocupara su lugar y volvemos a buscar el peor dentro de la elite
			if (elite[posPeor].adaptacion < pob[i].adaptacion) {
				elite[posPeor].copiaCromosoma(pob[i]);
				for (int j = 0; j < tamElite; j++)
					if (elite[j].adaptacion < elite[posPeor].adaptacion) posPeor = j;
			}
		}
	}

	public void incluyeElite(int tamElite){
		// indices en pob de los peores de la poblacion
		int[] pobPeores = new int[tamElite];
		int posMejor = 0; // apunta al mejor individuo dentro de pobPeores

		// coge a los primeros de la poblacion como a los peores
		// y buscamos el mejor dentro de los peores
		for (int i = 0; i < tamElite; i++){
			pobPeores[i] = i;
			if (pob[pobPeores[i]].adaptacion > pob[pobPeores[posMejor]].adaptacion) posMejor = i;
		}

		// recorre la poblacion buscando a los peores individuos
		for (int i = tamElite; i < tamPob; i++){
			// si encontramos a algun individuo peor que el mejor de los individuosPeores este ocupara su lugar y volvemos a buscar el mejor dentro de los peores
			if (pob[pobPeores[posMejor]].adaptacion > pob[i].adaptacion) {
				pobPeores[posMejor] = i;
				for (int j = 0; j < tamElite; j++)
					if(pob[pobPeores[j]].adaptacion > pob[pobPeores[posMejor]].adaptacion) posMejor = j;
			}
		}

		// Sustituimos a los peores de la poblacion por la elite
		for (int i = 0; i < tamElite; i++)
			pob[pobPeores[i]].copiaCromosoma(elite[i]);
	}

	private void media(int generacion){
		int sumaAbs = 0;

		for (int i = 0; i < tamPob; i++)
			sumaAbs += pob[i].fitness;

		genMedia[generacion] = sumaAbs/tamPob;
	}

	private void mejor(int generacion){
		genMejor[generacion] = pob[posMejor].fitness;
	}

	private void mejorAbs(int generacion){
		if (generacion == 0) mejorAbsoluto[0] = pob[posMejor].fitness;
		else if (pob[0].isMaximizar()){
			if (mejorAbsoluto[generacion-1] < pob[posMejor].fitness)
				mejorAbsoluto[generacion] = pob[posMejor].fitness;
			else mejorAbsoluto[generacion] = mejorAbsoluto[generacion-1];
		} else{
			if (mejorAbsoluto[generacion-1] > pob[posMejor].fitness)
				mejorAbsoluto[generacion] = pob[posMejor].fitness;
			else mejorAbsoluto[generacion] = mejorAbsoluto[generacion-1];
		}
	}

	private void calculaMedia() {
		mediaTamPob = 0;
		for (int i = 0; i < tamPob; i++)
			mediaTamPob += pob[i].getArbol().getNum_nodos();
		mediaTamPob = mediaTamPob / tamPob;
	}
	
	public void AlgoritmoGeneticoFuncion(){
		int generacionesAtascado = 0;
		int tamElite = (int) (tamPob*elitismo);
		generacionActual = 0;
		mediaTamPob = 0; numMuts = 0; numCruces = 0;
		
		inicializaPoblacion();
		
		calculaMedia();

		if(pob[0].isMaximizar()) adaptarMaximizacion(tamElite);
		else adaptarMinimizacion(tamElite);

		evalua();
		
		while (generacionActual < numMaxGen && generacionesAtascado < numMaxGen) {
			if (tamElite > 0) separaElite(tamElite);

			seleccion();
			cruce();
			mutacion();
			
			calculaMedia();

			if(pob[0].isMaximizar()) adaptarMaximizacion(tamElite);
			else adaptarMinimizacion(tamElite);
			

			if (tamElite > 0) incluyeElite(tamElite);

			evalua();
			
			// para las graficas
			media(generacionActual);
			mejor(generacionActual);
			mejorAbs(generacionActual);

			if (contractividad && generacionActual > 0) {
				if(genMedia[generacionActual] < genMedia[generacionActual - 1]) {
					generacionActual++;
					generacionesAtascado = 0;
				}
				else generacionesAtascado++;
			} else generacionActual++;
		}
		System.out.println("Ejecucion " + ejecucion);
		System.out.println("Puntuacion: " + elMejor.getNumBocados());
		System.out.println("Nº de mutaciones: " + numMuts);
		System.out.println("Nº de cruces: " + numCruces);
		System.out.println("Altura: " + elMejor.getArbol().getAltura());
		System.out.println("Nº de nodos: " + elMejor.getArbol().getNum_nodos());
		System.out.println();
		ejecucion++;
	}


	//Getters and setters * * * * * * * * * * * * *

	public double[] getGenMedia() {
		return genMedia;
	}

	public double[] getGenMejor() {
		return genMejor;
	}

	public double[] getMejorAbsoluto() {
		return mejorAbsoluto;
	}

	public int getNumMaxGen() {
		return numMaxGen;
	}

	public void setNumMaxGen(int numMaxGen) {
		this.numMaxGen = numMaxGen;
	}

	public double getProbCruce() {
		return probCruce;
	}

	public void setProbCruce(double probCruce) {
		this.probCruce = probCruce;
	}

	public double getProbMutacion() {
		return probMutacion;
	}

	public void setProbMutacion(double probMutacion) {
		this.probMutacion = probMutacion;
	}

	public String getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(String seleccion) {
		this.seleccion = seleccion;
	}

	public String getCruce() {
		return cruce;
	}

	public void setCruce(String cruce) {
		this.cruce = cruce;
	}

	public String getMutacion() {
		return mutacion;
	}

	public void setMutacion(String mutacion) {
		this.mutacion = mutacion;
	}

	public int getParticipantes() {
		return participantes;
	}

	public void setParticipantes(int participantes) {
		this.participantes = participantes;
	}

	public int getTamPob() {
		return tamPob;
	}

	public void setTamPob(int tamPob) {
		this.tamPob = tamPob;
	}

	public double getElitismo() {
		return elitismo;
	}

	public void setElitismo(double elitismo) {
		this.elitismo = elitismo;
	}

	public Cromosoma getElMejor() {
		return elMejor;
	}

	public String getContractividad() {
		String sContractividad;
		if (contractividad) sContractividad = "True";
		else sContractividad = "False";
		return sContractividad;
	}

	public void setContractividad(String sContractividad) {
		if (sContractividad == "True") contractividad = true;
		else contractividad = false;
	}

	public int getGeneracionActual() {
		return generacionActual;
	}

	public int gethMax() {
		return hMax;
	}

	public void sethMax(int hMax) {
		this.hMax = hMax;
	}

	// Para depurar
	public String toString(){
		String agString = "Elite: ";
		for (int i = 0; i < elite.length - 1; i++)
			agString += elite[i].toString() + ", ";
		if (elite.length - 1 >= 0) agString += elite[elite.length - 1].toString();
		agString += "\nPoblacion:";

		for (int i = 0; i < pob.length - 1; i++)
			agString += pob[i].toString() + ", ";
		if (pob.length - 1 >= 0) agString += pob[pob.length - 1].toString() + "\nEl mejor: " + elMejor.toString();

		return agString;
	}
}
