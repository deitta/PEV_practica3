package practica3pe;

public class Arbol {
	String dato; // operando u operador
	Arbol Hi; // hijo izquierdo
	Arbol Hc; // hijo central
	Arbol Hd; // hijo derecho
	int num_nodos; // número de nodos
	int profundidad; // profundidad del árbol

	public static enum Tipo {
		AVANZA, GIRA_DERECHA, GIRA_IZQUIERDA,
		SIC, PROGN2, PROGN3
	}

	void creaArbol(Arbol arbol, int prof_min, int prof_max){
		if(prof_min > 0) {
			//no puede ser hoja
			 //generación del subarbol de operador
			Tipo tipos[] = new Tipo[Tipo.values().length];
			Arbol.Tipo x = Tipo.[0].values();
			int numTipoElegido = (int) (Math.random() * (tipos.length - 3) + 3); //((int)(Math.random()*(Max-Min))+Min)
			Tipo operador = tipos[numTipoElegido];//operador_aleatorio; //símbolo de operador aleatorio
			dato = operador.toString();
			 //se generan los hijos
			 Hi = construir_arbol(arbol.HI, prof_min - 1, prof_max - 1);
			 arbol.num_nodos = arbol.num_nodos + arbol.HI.num_nodos;
			si tres_operandos(operador) entonces
			Hc = construir_arbol(arbol.HC, prof_min - 1, prof_max - 1);
			arbol.num_nodos = arbol.num_nodos + arbol.HC.num_nodos;
			eoc // dos operandos
			Hc = NULL;
			Hd = construir_arbol(arbol.HD, prof_min - 1, prof_max - 1);
			arbol.num_nodos = arbol.num_nodos + arbol.HD.num_nodos;
			eoc // prof_min = 0
		}

		if(prof_max == 0) { // sólo puede ser hoja
			 // generación del subarbol de operando
			 operando = operando_aleatorio;
			// símbolo de operando aleatorio
			 arbol.dato = operando;
			 arbol.num_nodos = arbol.num_nodos + 1;
			eoc
			 // se decide aleatoriamente operando u operador
			 tipo = aleatorio_cero_uno;
			 si tipo = 1 entonces // se genera operador
			 // generación del subarbol de operador
			 {      }
			 eoc // se genera operando
			// generación del subarbol de operando
			 { }
		 }
	}
}
