package metodosSeleccion;

import java.util.Collections;
import java.util.ArrayList;

import base.Cromosoma;
import base.OrdenCromosoma;

public class SeleccionRanking implements AlgoritmoSeleccion {

	public void seleccion(Cromosoma[] pob, Cromosoma[] nuevaPob, int tamPob) {
		int numOfParents;
		double[] fitnessSegments;
		ArrayList<Cromosoma> pobOrdenada = new ArrayList<Cromosoma>();

		for (int i = 0; i < tamPob; i++)
			pobOrdenada.add(pob[i]);
		
		Collections.sort(pobOrdenada, new OrdenCromosoma());

		nuevaPob[0].copiaCromosoma(pobOrdenada.get(0)); nuevaPob[1].copiaCromosoma(pobOrdenada.get(1));
		numOfParents = 2;
		fitnessSegments = pobRanking(tamPob);
		double entireSegment = fitnessSegments[fitnessSegments.length-1];
		while(numOfParents < nuevaPob.length){
			double x = (double)(Math.random()*entireSegment);
			if(x <= fitnessSegments[0]){
				nuevaPob[numOfParents] = pobOrdenada.get(0);
				numOfParents++;
			}
			else
				for(int i = 1; i < nuevaPob.length;i++){
					if(x > fitnessSegments[i-1] && x <= fitnessSegments[i]){
						nuevaPob[numOfParents].copiaCromosoma(pobOrdenada.get(i));
						numOfParents++;
					}
				}
		}
	}

	private double[] pobRanking(int tamPob){
		double beta = 1.5;
		double[] fitnessSegments = new double[tamPob];
		for(int i = 0; i < fitnessSegments.length; i++){
			double probOfIth = (double)((i-1)/(tamPob-1));
			probOfIth = probOfIth*2*(beta-1);
			probOfIth = beta - probOfIth;
			probOfIth = (double)probOfIth*((double)1/tamPob);
			if(i != 0)
				fitnessSegments[i] = fitnessSegments[i-1] + probOfIth;
			else
				fitnessSegments[i] = probOfIth;
		}
		return fitnessSegments;
	}
}
