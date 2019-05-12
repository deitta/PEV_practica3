package metodosMutacion;

import base.AlgoritmoGenetico;
import base.Arbol;
import base.Cromosoma;

public class MutacionInicializacion implements AlgoritmoMutacion {

	public void mutacion(Cromosoma[] pob, double probMutacion, int tamPob) {
		boolean mutado;
		double prob;
		int nMutar;

		for (int i = 0; i < tamPob; i++) {
			mutado = false;
			prob = Math.random();
			// mutan los genes con prob<probMutacion
			if (prob < probMutacion){
				nMutar = (int) (Math.random()*pob[i].getArbol().getNum_nodos() + 1);
				int pasosMax = (int) (pob[i].getPasosMax() - pob[i].getArbol().getPasos() + pob[i].getArbol().buscarNodo(nMutar).getPasos());
				Arbol a = new Arbol(pasosMax, pob[i].getArbol().buscarNodo(nMutar).getProfundidad());
				
				a.creaArbol(a, 0, pob[i].getHmax()-a.getProfundidad()); // nose, si hago esto creo que no hace falta luego sustituirArbol
			
				pob[i].getArbol().sustituirSubarbol(nMutar, a);
				
				mutado = true;
			}

			if (mutado) {
				pob[i].setFitness(pob[i].evaluaCromosoma());
				AlgoritmoGenetico.numMuts++;
			}
		}
	}
}