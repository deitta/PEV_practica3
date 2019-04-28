package base;

public class Arbol {
	TNodo dato; // operando u operador
	Arbol Hi; // hijo izquierdo
	Arbol Hc; // hijo central
	Arbol Hd; // hijo derecho
	int num_nodos; // numero de nodos
	int profundidad; // profundidad del arbol
	int pasos = 1; // numero de hojas/terminales del arbol

	private int pasosMax;

	public static enum TNodo {
		AVANZA, GIRA_DERECHA, GIRA_IZQUIERDA,
		SIC, PROGN2, PROGN3;

		public int getPos(String nombre) {
			switch(nombre) {
			case "AVANZA": return 0;
			case "GIRA_DERECHA": return 1;
			case "GIRA_IZQUIERDA": return 2;
			case "SIC": return 3;
			case "PROGN2": return 4;
			case "PROGN3": return 5;
			default: return -1;
			}
		}

		public String getNombre(int pos) {
			switch(pos) {
			case 0: return "AVANZA";
			case 1: return "GIRA_DERECHA";
			case 2: return "GIRA_IZQUIERDA";
			case 3: return "SIC";
			case 4: return "PROGN2";
			case 5: return "PROGN3";
			default: return null;
			}
		}
	}

	public Arbol(int pasosMax) {
		this.pasosMax = pasosMax;
	}
	
	void creaArbol(Arbol arbol, int prof_min, int prof_max){
		if(prof_min > 0) { // no puede ser hoja
			// generacion del subarbol de operador/funcion
			TNodo operador = TNodo.values()[(int) (Math.random()*3)+3];
			arbol.dato = operador;
			pasos += (operador.ordinal()-2) - 1; // pasos = pasosDelOperador - 1
			// se generan los hijos
			arbol.Hi = new Arbol(pasosMax-pasos);
			creaArbol(arbol.Hi, prof_min - 1, prof_max - 1);
			arbol.num_nodos += arbol.Hi.num_nodos;
			if (operador != TNodo.values()[3]) pasos += arbol.Hi.pasos;

			if (operador == TNodo.values()[5]) { // tres operandos
				arbol.Hc = new Arbol(pasosMax-pasos);
				creaArbol(arbol.Hc, prof_min - 1, prof_max - 1);
				arbol.num_nodos += arbol.Hc.num_nodos;
				pasos += arbol.Hc.pasos;
			}
			else Hc = null; // dos operandos

			arbol.Hd = new Arbol(pasosMax-pasos);
			creaArbol(arbol.Hd, prof_min - 1, prof_max - 1);
			arbol.num_nodos += arbol.Hd.num_nodos;
			if (operador != TNodo.values()[3]) pasos += arbol.Hd.pasos;
			else {
				if (arbol.Hi.pasos >= arbol.Hd.pasos)
					pasos += arbol.Hi.pasos;
				else pasos += arbol.Hd.pasos;
			}
		}
		else { // prof_min = 0
			if(prof_max == 0) { // solo puede ser hoja
				// generacion del subarbol de operando/terminal
				TNodo operando = TNodo.values()[(int) (Math.random()*3)]; // simbolo de operando aleatorio
				arbol.dato = operando;
				arbol.num_nodos++;
			}
			else { // se decide aleatoriamente operando/terminal u operador/funcion
				int tipo = (int) Math.round(Math.random());
				if (tipo == 1) { // se genera operador/funcion
					// generacion del subarbol de operador/funcion
					TNodo operador = TNodo.values()[(int) (Math.random()*3)+3];
					arbol.dato = operador;
					pasos += (operador.ordinal()-2) - 1; // pasos = pasosDelOperador - 1
					// se generan los hijos
					arbol.Hi = new Arbol(pasosMax-pasos);
					creaArbol(arbol.Hi, prof_min - 1, prof_max - 1);
					arbol.num_nodos += arbol.Hi.num_nodos;
					if (operador != TNodo.values()[3]) pasos += arbol.Hi.pasos;

					if (operador == TNodo.values()[5]) { // tres operandos
						arbol.Hc = new Arbol(pasosMax-pasos);
						creaArbol(arbol.Hc, prof_min - 1, prof_max - 1);
						arbol.num_nodos += arbol.Hc.num_nodos;
						pasos += arbol.Hc.pasos;
					}
					else Hc = null; // dos operandos

					arbol.Hd = new Arbol(pasosMax-pasos);
					creaArbol(arbol.Hd, prof_min - 1, prof_max - 1);
					arbol.num_nodos += arbol.Hd.num_nodos;
					if (operador != TNodo.values()[3]) pasos += arbol.Hd.pasos;
					else {
						if (arbol.Hi.pasos >= arbol.Hd.pasos)
							pasos += arbol.Hi.pasos;
						else pasos += arbol.Hd.pasos;
					}
				} // se genera operando
				else { // generacion del subarbol de operando/terminal
					TNodo operando = TNodo.values()[(int) (Math.random()*3)]; // simbolo de operando aleatorio
					arbol.dato = operando;
					arbol.num_nodos++;
				}
			}
		}
	}

}
