package metodosSeleccion;

import base.Cromosoma;

public class SeleccionTorneo implements AlgoritmoSeleccion {
	int nParticipantes;

	public SeleccionTorneo(int nParticipantes) {
		this.nParticipantes = nParticipantes;
	}

	public void seleccion(Cromosoma[] pob, Cromosoma[] nuevaPob, int tamPob) {
		int participante; //individuo seleccionado para participar el torneo
		int posMejor;
		Cromosoma mejor = new Cromosoma();
		
		for(int i = 0; i < tamPob; i++){
			posMejor = ((int) (Math.random() * tamPob));
			mejor = pob[posMejor];
			for (int j = 1; j < nParticipantes; j++){
				participante = ((int) (Math.random() * tamPob)); // si el azar lo quiere puede haber varios participantes iguales
				if (pob[participante].getAdptacion() > pob[posMejor].getAdptacion()){
					posMejor = participante;
					mejor = pob[participante];
				}
			}
			nuevaPob[i].copiaCromosoma(mejor);
		}
	}
	
	

}
