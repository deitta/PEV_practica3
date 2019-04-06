package metodosSeleccion;

import base.Cromosoma;

public class SeleccionRestos implements AlgoritmoSeleccion {

	public void seleccion(Cromosoma[] pob, Cromosoma[] nuevaPob, int tamPob) {
		int tamNuevaPob = 0, faltantes;		
		for (int i = 0; i < tamPob; i++){
			for (int j = 0; j < (int) (tamPob*pob[i].getPunt()); j++){
				nuevaPob[tamNuevaPob].copiaCromosoma(pob[i]);
				tamNuevaPob++;
			}
		}

		faltantes = tamPob-tamNuevaPob;

		if(faltantes > 0){
			Cromosoma[] pobFaltantes = new Cromosoma[tamPob];
			
			for (int i = 0; i < faltantes; i++)
				pobFaltantes[i] = new Cromosoma();
			
			AlgoritmoSeleccion algoSeleccion = FactoriaSeleccion.getAlgoritmoDeSeleccion("Ruleta", 0);
			algoSeleccion.seleccion(pob, pobFaltantes, faltantes);
			
			for (int i = 0; i < faltantes; i++){
				nuevaPob[tamNuevaPob].copiaCromosoma(pobFaltantes[i]);
				tamNuevaPob++;
			}
		}
	}
}
