package metodosMutacion;

import base.Arbol;
import base.Cromosoma;
import base.Arbol.TNodo;

public class MutacionFuncion implements AlgoritmoMutacion {

	public void mutacion(Cromosoma[] pob, double probMutacion, int tamPob) {
		boolean mutado;
		double prob;
		int tMutar;

		for (int i = 0; i < tamPob; i++) {
			mutado = false;
			prob = Math.random();
			// mutan los genes con prob<probMutacion y con una raiz que no sea terminal
			if (prob < probMutacion && pob[i].getArbol().getDato().ordinal() > 2){
				Arbol a = new Arbol();
				TNodo n = TNodo.values()[(int) (Math.random()*3)+3]; // coge un nodo funcion
				do {
					tMutar = (int) (Math.random()*pob[i].getArbol().getNum_nodos()); //nodos totales a partir de 0dfv< 
					a = pob[i].getArbol().buscarNodo(tMutar);
					//este do_while lo hace si sale un nodo TERMINAL hasta encontrar un nodoFUNCION
				} while (a.getDato().ordinal() < 3);
				
				switch(n) {
				case PROGN2:
				case SIC:
					a.setDato(n);
					a.setHc(null);
					mutado = true;
					break;
				case PROGN3:
					if (a.getHc() == null && pob[i].getArbol().getPasos() < pob[i].getPasosMax()) {
						a.setDato(n);
						a.setHc(new Arbol());
						a.getHc().setDato(TNodo.values()[(int) (Math.random()*3)]); // añadea a hc un nodo terminal
						mutado = true;
					} else if (a.getHc() != null) {
						a.setDato(n);
						mutado = true;
					}
					break;
				default:
					System.out.println("ERROR: en la mutacion por funcion.");
					break;			
				}
			}
			if (mutado) pob[i].setFitness(pob[i].evaluaCromosoma());
		}

	}

}
