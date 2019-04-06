package metodosMutacion;

import base.Cromosoma;

public interface AlgoritmoMutacion {
	public void mutacion(Cromosoma[] pob, double probMutacion, int tampPob);
}
