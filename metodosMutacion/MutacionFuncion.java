package metodosMutacion;

import base.Arbol;
import base.Cromosoma;
import base.Arbol.TNodo;

public class MutacionFuncion implements AlgoritmoMutacion {

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
				tMutar = (int) (Math.random()*pob[i].getArbol().getNum_nodos()); //nodos totalees a partir de 1
				Arbol a = new Arbol();
				a = pob[i].getArbol().buscarNodo(tMutar);

				//este if lo hace si sale un nodo TERMINAL hasta encontrar un nodoFUNCION
				if(a.getDato() != TNodo.SIC && a.getDato() != TNodo.PROGN2 && a.getDato() != TNodo.PROGN3) {
					tMutar = (int) (Math.random()*pob[i].getArbol().getNum_nodos()); //nodos totalees a partir de 1
					a = pob[i].getArbol().buscarNodo(tMutar);
				}
				
				if(a.getDato() == TNodo.SIC) a.setDato(TNodo.PROGN2); //tiene 2 arg igual que PROGN2
				else if (a.getDato() == TNodo.PROGN2) a.setDato(TNodo.SIC);//tiene 2 arg igual que SIC
				mutado = true;
				
				if(a.getDato() == TNodo.PROGN3) mutado = false; //no cambiaria porque no hay otra funcion con 3 param.
			}
			if (mutado) pob[i].setFitness(pob[i].evaluaCromosoma());
		}

	}
	
}
