package metodosSeleccion;

import base.Cromosoma;

public class SeleccionRuleta implements AlgoritmoSeleccion {

	public void seleccion(Cromosoma[] pob, Cromosoma[] nuevaPob, int tamPob) {
		int selSuper[] = new int[tamPob];
		int posSuper;
		double prob;
		
		for (int i = 0; i < tamPob; i++){
			prob = Math.random();
			posSuper = 0;
			while ((posSuper < pob.length-1) && (prob > pob[posSuper].getPuntAcu())) posSuper++;
			selSuper[i] = posSuper;
		}
		
		for(int i = 0; i < tamPob; i++) {
			nuevaPob[i].copiaCromosoma(pob[selSuper[i]]);
		}
	}
	
}
