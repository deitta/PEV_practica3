package metodosMutacion;

import base.Arbol;
import base.Cromosoma;
import base.Arbol.TNodo;

public class MutacionFuncion implements AlgoritmoMutacion {

	public void mutacion(Cromosoma[] pob, double probMutacion, int tamPob) {
		boolean mutado;
		double prob;
		int fMutar;

		for (int i = 0; i < tamPob; i++) {
			mutado = false;
			prob = Math.random();

			if (prob < probMutacion && pob[i].getArbol().esFuncion()){
				int numFunciones = (int) (pob[i].getArbol().getNum_nodos() - pob[i].getArbol().getPasos());
				fMutar = (int) (Math.random()*numFunciones + 1);
				Arbol a = new Arbol();
				a = pob[i].getArbol().buscarFuncion(fMutar);

				if(a.getDato() == TNodo.SIC) {
					a.setDato(TNodo.PROGN2); //tiene 2 arg igual que PROGN2
					mutado = true;
				}
				else if (a.getDato() == TNodo.PROGN2) {
					a.setDato(TNodo.SIC);//tiene 2 arg igual que SIC
					mutado = true;
				}
			}
			if (mutado) pob[i].setFitness(pob[i].evaluaCromosoma());
		}
	}
}
