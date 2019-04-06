package metodosSeleccion;

import java.util.ArrayList;
import java.util.Collections;

import base.Cromosoma;
import base.OrdenCromosoma;

// conserva entre [0 6] copias del 15% de los mejores indi de pob (hasta 90% de nuevaPob) y el 5% de los peores. Lo restante lo rellena aleatoriamente
public class SeleccionPropia implements AlgoritmoSeleccion {

	public void seleccion(Cromosoma[] pob, Cromosoma[] nuevaPob, int tamPob) {
		ArrayList<Cromosoma> pobOrdenada = new ArrayList<Cromosoma>();
		double porcMejores = 0.15, porcPeores = 0.05;
		int numRepesMejores = (int) (Math.random()*6);
		int numMejores = ((int) (tamPob*porcMejores))*numRepesMejores, numPeores = (int) (tamPob*porcPeores);
		
		for (int i = 0; i < tamPob; i++)
			pobOrdenada.add(pob[i]);
		
		Collections.sort(pobOrdenada, new OrdenCromosoma());
		
		for (int i = 0; i < numMejores; i+=numRepesMejores) // Incluye los mejores individuos
			for (int j = 0; j < numRepesMejores; j++)
				nuevaPob[i+j].copiaCromosoma(pobOrdenada.get(i));
		for (int i = numMejores; i < numMejores+numPeores; i++) // Incluye los peores
			nuevaPob[i].copiaCromosoma(pobOrdenada.get(tamPob-(i-numMejores)));
		for (int i = numMejores+numPeores; i < tamPob; i++)
			nuevaPob[i].copiaCromosoma(pob[(int) (Math.random()*tamPob)]);
	}

}
