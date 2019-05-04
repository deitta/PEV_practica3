package base;

public class Arbol {
	private TNodo dato; // operando u operador
	private Arbol Hi; // hijo izquierdo
	Arbol Hc; // hijo central
	Arbol Hd; // hijo derecho
	int num_nodos; // numero de nodos
	int profundidad; // profundidad del arbol
	int pasos = 1; // numero de hojas/terminales del arbol

	private int pasosMax; // numero max de pasos que puede dar

	public static enum TNodo {
		AVANZA, GIRA_DERECHA, GIRA_IZQUIERDA,
		SIC, PROGN2, PROGN3;
	}

	public Arbol(int pasosMax) {
		this.pasosMax = pasosMax;
	}
	public Arbol() {}

	public void creaArbol(Arbol arbol, int prof_min, int prof_max){
		if (prof_min > 0 && arbol.pasosMax > 0) { // no puede ser hoja
			// generacion del subarbol de operador/funcion
			TNodo operador;
			if (arbol.pasosMax > 1) operador = TNodo.values()[(int) (Math.random()*3)+3]; // operando = SIC/PROGN2/PROGN3
			else operador = TNodo.values()[(int) (Math.random()*2)+3]; // operando = SIC/PROGN2
			arbol.setDato(operador);
			arbol.pasos += (operador.ordinal()-2) - 1; // pasos = pasosDelOperador - 1
			
			// se generan los hijos
			arbol.setHi(new Arbol(arbol.pasosMax-arbol.pasos));
			creaArbol(arbol.getHi(), prof_min - 1, prof_max - 1);
			arbol.num_nodos += arbol.getHi().num_nodos;
			if (operador != TNodo.values()[3]) // != SIC
				arbol.pasos += arbol.getHi().pasos-1;

			if (operador == TNodo.values()[5]) { // == PROGN3
				arbol.setHc(new Arbol(arbol.pasosMax-arbol.pasos));
				creaArbol(arbol.getHc(), prof_min - 1, prof_max - 1);
				arbol.num_nodos += arbol.getHc().num_nodos;
				arbol.pasos += arbol.getHc().pasos-1;
			}

			arbol.setHd(new Arbol(arbol.pasosMax-arbol.pasos));
			creaArbol(arbol.getHd(), prof_min - 1, prof_max - 1);
			arbol.num_nodos += arbol.getHd().num_nodos-1;
			if (operador != TNodo.values()[3]) // != SIC
				arbol.pasos += arbol.getHd().pasos-1;
			else {
				if (arbol.getHi().pasos >= arbol.getHd().pasos)
					arbol.pasos += arbol.getHi().pasos-1;
				else arbol.pasos += arbol.getHd().pasos-1;
			}
		}
		else { // prof_min = 0
			if(prof_max == 0 || arbol.pasosMax <= 0) { // solo puede ser hoja
				// generacion del subarbol de operando/terminal
				TNodo operando = TNodo.values()[(int) (Math.random()*3)]; // simbolo de operando aleatorio
				arbol.setDato(operando);
				arbol.num_nodos++;
			}
			else { // se decide aleatoriamente operando/terminal u operador/funcion
				int tipo = (int) Math.round(Math.random());
				if (tipo == 1) { // se genera operador/funcion
					// generacion del subarbol de operador/funcion
					TNodo operador;
					if (arbol.pasosMax > 1) operador = TNodo.values()[(int) (Math.random()*3)+3]; // operando = SIC/PROGN2/PROGN3
					else operador = TNodo.values()[(int) (Math.random()*2)+3]; // operando = SIC/PROGN2
					arbol.setDato(operador);
					arbol.pasos += (operador.ordinal()-2) - 1; // pasos = pasosDelOperador - 1
					// se generan los hijos
					arbol.setHi(new Arbol(arbol.pasosMax-arbol.pasos));
					creaArbol(arbol.getHi(), prof_min, prof_max - 1);
					arbol.num_nodos += arbol.getHi().num_nodos;
					if (operador != TNodo.values()[3]) // != SIC
						arbol.pasos += arbol.getHi().pasos;

					if (operador == TNodo.values()[5]) { // == PROGN3
						arbol.Hc = new Arbol(arbol.pasosMax-arbol.pasos);
						creaArbol(arbol.Hc, prof_min, prof_max - 1);
						arbol.num_nodos += arbol.Hc.num_nodos;
						arbol.pasos += arbol.Hc.pasos;
					}

					arbol.Hd = new Arbol(arbol.pasosMax-arbol.pasos);
					creaArbol(arbol.Hd, prof_min, prof_max - 1);
					arbol.num_nodos += arbol.Hd.num_nodos;
					if (operador != TNodo.values()[3]) // != SIC
						arbol.pasos += arbol.Hd.pasos;
					else {
						if (arbol.getHi().pasos >= arbol.Hd.pasos)
							arbol.pasos += arbol.getHi().pasos;
						else arbol.pasos += arbol.Hd.pasos;
					}
				} // se genera operando
				else { // generacion del subarbol de operando/terminal
					TNodo operando = TNodo.values()[(int) (Math.random()*3)]; // simbolo de operando aleatorio
					arbol.setDato(operando);
					arbol.num_nodos++;
				}
			}
		}
		if (arbol.getHi() != null) {
			if (arbol.getHi().profundidad >= arbol.Hd.profundidad && (arbol.Hc == null || arbol.getHi().profundidad >= arbol.Hc.profundidad))
				arbol.profundidad = arbol.getHi().profundidad;
			else if(arbol.Hc == null || arbol.Hd.profundidad >= arbol.Hc.profundidad)
				arbol.profundidad = arbol.Hd.profundidad;
			else arbol.profundidad = arbol.Hc.profundidad;
			arbol.profundidad++;
		}
	}

	public String toString() {
		String s;
		
		s = this.getDato().name();
		if (this.getHi() != null) {
			s += "(" + this.getHi().toString() + ",";
			if (this.Hc != null) s += this.Hc.toString() + ",";
			s += this.Hd.toString() + ")";
		}
		
		return s;
	}
	
	// Para depurar
	public static void main(String args[]) {
		Arbol a = new Arbol(400);
		a.creaArbol(a, 3, 9);
/*			a.Hi = new Arbol();
				a.Hi.Hi = new Arbol();
				a.Hi.Hd = new Arbol();
			a.Hc = new Arbol();
				a.Hc.Hi = new Arbol();
				a.Hc.Hd = new Arbol();
					a.Hc.Hd.Hi = new Arbol();
					a.Hc.Hd.Hd = new Arbol();
			a.Hd = new Arbol();
				a.Hd.Hi = new Arbol();
				a.Hd.Hd = new Arbol();

		a.dato = TNodo.values()[5];
			a.Hi.dato = TNodo.values()[3];
				a.Hi.Hi.dato = TNodo.values()[0];
				a.Hi.Hd.dato = TNodo.values()[0];
			a.Hc.dato = TNodo.values()[4];
				a.Hc.Hi.dato = TNodo.values()[0];
				a.Hc.Hd.dato = TNodo.values()[3];
					a.Hc.Hd.Hi.dato = TNodo.values()[0];
					a.Hc.Hd.Hd.dato = TNodo.values()[2];
			a.Hd.dato = TNodo.values()[3];
				a.Hd.Hi.dato = TNodo.values()[0];
				a.Hd.Hd.dato = TNodo.values()[1];
*/		
		System.out.println(a.toString());
	}
	
	
	public TNodo getDato() {
		return dato;
	}
	public void setDato(TNodo dato) {
		this.dato = dato;
	}
	public Arbol getHi() {
		return Hi;
	}
	public void setHi(Arbol hi) {
		Hi = hi;
	}
	public Arbol getHc() {
		return Hc;
	}
	public void setHc(Arbol hc) {
		Hc = hc;
	}
	public Arbol getHd() {
		return Hd;
	}
	public void setHd(Arbol hd) {
		Hd = hd;
	}
}
