package metodosMutacion;

import base.Arbol;
import base.Cromosoma;
import base.Arbol.TNodo;

public class MutacionInicializacion implements AlgoritmoMutacion {

	@Override
	public void mutacion(Cromosoma[] pob, double probMutacion, int tamPob) {
		boolean mutado;
		double prob;
		int tMutar;

		for (int i = 0; i < tamPob; i++) {
			mutado = false;

			prob = Math.random();
			// mutan los genes con prob<probMutacion
			if (prob < probMutacion){
				tMutar = (int) ((Math.random()*pob[i].getArbol().getNum_nodos()) + 1); //de los nodos totales a partir de 1
				
				Arbol a = new Arbol();
				a = pob[i].getArbol().buscarNodo(tMutar);

				a.setDato(TNodo.values()[(int)(Math.random()*3)+3]);
				a.creaArbol(a, 3, 3);
				pob[i].getArbol().sustituirSubarbol(tMutar, a);;
				mutado = true;
			}

			if (mutado) pob[i].setFitness(pob[i].evaluaCromosoma());
		}
	}

}
