package metodosMutacion;

import base.AlgoritmoGenetico;
import base.Arbol;
import base.Arbol.TNodo;
import base.Cromosoma;

public class MutacionTerminal implements AlgoritmoMutacion {

	public void mutacion(Cromosoma[] pob, double probMutacion, int tamPob) {
		boolean mutado;
		double prob;
		int tMutar;

		for (int i = 0; i < tamPob; i++) {
			mutado = false;

			prob = Math.random();
			// mutan los genes con prob<probMutacion
			if (prob < probMutacion){
				tMutar = (int) (Math.random()*pob[i].getArbol().getPasos()+1);
				
				Arbol a = pob[i].getArbol().buscarHoja(tMutar);

				a.setDato(TNodo.values()[(a.getDato().ordinal()+1) % 3]); // sustituye por el siguiente terminal
				
				mutado = true;
			}

			if (mutado) {
				pob[i].setFitness(pob[i].evaluaCromosoma());
				AlgoritmoGenetico.numMuts++;
			}
		}
	}

}
