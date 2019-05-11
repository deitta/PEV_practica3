package metodosCruce;

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
			if (hijo1.getFitness() >= pob[selCruce[i]].getFitness())
				pob[selCruce[i]] = hijo1;
			if (hijo2.getFitness() >= pob[selCruce[i+1]].getFitness())
				pob[selCruce[i+1]] = hijo2;
		}
	}

	private static void cruce(Cromosoma padre1, Cromosoma padre2, Cromosoma hijo1, Cromosoma hijo2) {
		Arbol subarbol1 = new Arbol(), subarbol2 = new Arbol();
		int num_nodos, nodo_cruce;
		hijo1.setArbol(new Arbol());
		hijo2.setArbol(new Arbol());
		// copia los padres en los hijos
		hijo1.copiaCromosoma(padre1);
		hijo2.copiaCromosoma(padre2);

		// elige un nodo aleatorio
		num_nodos = Math.min(padre1.getArbol().getNum_nodos(), padre2.getArbol().getNum_nodos());
		nodo_cruce = (int) (Math.random()*num_nodos+1);

		// copia el subarbol a partir del nodo seleccionado de cada padre
		subarbol1.copiaArbol(padre1.getArbol().buscarNodo(nodo_cruce));
		subarbol2.copiaArbol(padre2.getArbol().buscarNodo(nodo_cruce));

		if (subarbol1.getAltura() <= padre2.getHmax()-subarbol2.getProfundidad() && subarbol2.getAltura() <= padre1.getHmax()-subarbol1.getProfundidad()) {
			// sustituir en los hijos los subarboles si respetan el numero de pasos
			hijo1.getArbol().sustituirSubarbol(nodo_cruce, subarbol2);
			hijo2.getArbol().sustituirSubarbol(nodo_cruce, subarbol1);
		}
	}
}
