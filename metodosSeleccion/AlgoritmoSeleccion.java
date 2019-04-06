package metodosSeleccion;

import base.Cromosoma;

public interface AlgoritmoSeleccion {
	public void seleccion(Cromosoma[] pob, Cromosoma[] nuevaPob, int tamPob);
}
