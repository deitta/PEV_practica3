package metodosSeleccion;

import java.util.Collections;
import java.util.ArrayList;

import base.Cromosoma;
import base.OrdenCromosoma;

public class SeleccionTruncamiento implements AlgoritmoSeleccion {

	public void seleccion(Cromosoma[] pob, Cromosoma[] nuevaPob, int tamPob) {
		ArrayList<Cromosoma> pobOrdenada = new ArrayList<Cromosoma>();
		int trunc = (int) (Math.random()*40+10);
		
		for (int i = 0; i < tamPob; i++)
			pobOrdenada.add(pob[i]);
		
		Collections.sort(pobOrdenada, new OrdenCromosoma());
		
		int j = 0;
		// rellena nuevaPob con los trunc mejores individuos. Pone el mismo numero de copias de todos los individuos
		for (int i = 0; i < tamPob; i++){
			nuevaPob[i].copiaCromosoma(pobOrdenada.get(j));
			j = (j+1)%trunc;
		}
		
	}

}
