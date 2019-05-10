package metodosMutacion;

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
				
				Arbol a = new Arbol();
				a = pob[i].getArbol().buscarHoja(tMutar);

				a.setDato(TNodo.values()[(int) (Math.random()*3)]);
				
				mutado = true;
			}

			if (mutado) pob[i].setFitness(pob[i].evaluaCromosoma());
		}
	}

}
