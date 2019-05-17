package metodosCruce;

import base.AlgoritmoGenetico;
import base.Arbol;
import base.Cromosoma;

public class AlgoritmoCruce {
	public static void cruceBase(Cromosoma[] pob, int tamPob, double probCruce) {
		Cromosoma hijo1, hijo2;
		int selCruce[] = new int[tamPob]; //seleccionados para reproducir
		int nSelCruce = 0; //contador seleccionados
		double prob;

		//Se eligen los individuos a cruzar
		for (int i = 0; i < tamPob; i++) {
			prob = Math.random(); //se generan tam_pob números aleatorios en [0 1)
			//se eligen los individuos de las posiciones i si prob < probCruce
			if (prob < probCruce){
				selCruce[nSelCruce] = i;
				nSelCruce++;
			}
		}

		// el numero de seleccionados se hace par
		if ((nSelCruce % 2) == 1)
			nSelCruce--;

		// se cruzan los individuos elegidos en un punto al azar. Todos por el mismo punto
		for (int i = 0; i < nSelCruce; i+=2) {
			hijo1 = new Cromosoma();
			hijo2 = new Cromosoma();

			cruce(pob[selCruce[i]], pob[selCruce[i+1]], hijo1, hijo2);

			// se evaluan
			hijo1.setFitness(hijo1.evaluaCromosoma());
			hijo2.setFitness(hijo2.evaluaCromosoma());

			// los nuevos individuos sustituyen a sus progenitores
			if (hijo1.getFitness() > pob[selCruce[i]].getFitness()
					|| (hijo1.getFitness() == pob[selCruce[i]].getFitness()
					&& hijo1.getArbol().getAltura() <= pob[selCruce[i]].getArbol().getAltura())) {
				pob[selCruce[i]] = hijo1;
				AlgoritmoGenetico.numCruces++;
			}
			if (hijo2.getFitness() > pob[selCruce[i+1]].getFitness()
					|| (hijo2.getFitness() == pob[selCruce[i+1]].getFitness()
					&& hijo2.getArbol().getAltura() <= pob[selCruce[i+1]].getArbol().getAltura())) {
				pob[selCruce[i+1]] = hijo2;
				AlgoritmoGenetico.numCruces++;
			}
		}
	}

	private static void cruce(Cromosoma padre1, Cromosoma padre2, Cromosoma hijo1, Cromosoma hijo2) {
		Arbol subarbol1 = new Arbol(), subarbol2 = new Arbol();
		int nodo_cruceP1, nodo_cruceP2;
		// copia los padres en los hijos
		hijo1.copiaCromosoma(padre1);
		hijo2.copiaCromosoma(padre2);

		// elige un nodo aleatorio
		nodo_cruceP1 = (int) (Math.random()*(padre1.getArbol().getNum_nodos())+1);
		nodo_cruceP2 = (int) (Math.random()*(padre2.getArbol().getNum_nodos())+1);

		// copia el subarbol a partir del nodo seleccionado de cada padre
		subarbol1.copiaArbol(padre1.getArbol().buscarNodo(nodo_cruceP1));
		subarbol2.copiaArbol(padre2.getArbol().buscarNodo(nodo_cruceP2));

		// sustituir en los hijos los subarboles
		hijo1.getArbol().sustituirSubarbol(nodo_cruceP1, subarbol2);
		hijo2.getArbol().sustituirSubarbol(nodo_cruceP2, subarbol1);
	}
}
