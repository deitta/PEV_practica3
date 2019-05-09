package base;

public class Arbol {
	private TNodo dato; // operando u operador
	Arbol Hi; // hijo izquierdo
	Arbol Hc; // hijo central
	Arbol Hd; // hijo derecho
	private int num_nodos; // numero de nodos
	int profundidad; // profundidad del arbol
	private int pasos = 1; // numero de hojas/terminales del arbol

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
			arbol.dato = operador;
			arbol.pasos += (operador.ordinal()-2) - 1; // pasos = pasosDelOperador - 1
			
			// se generan los hijos
			arbol.Hi = new Arbol(arbol.pasosMax-arbol.pasos);
			creaArbol(arbol.Hi, prof_min - 1, prof_max - 1);
			arbol.num_nodos = arbol.num_nodos + arbol.Hi.num_nodos;
			if (operador != TNodo.values()[3]) // != SIC
				arbol.pasos += arbol.Hi.pasos-1;

			if (operador == TNodo.values()[5]) { // == PROGN3
				arbol.Hc = new Arbol(arbol.pasosMax-arbol.pasos);
				creaArbol(arbol.Hc, prof_min - 1, prof_max - 1);
				arbol.num_nodos = arbol.num_nodos + arbol.Hc.num_nodos;
				arbol.pasos += arbol.Hc.pasos-1;
			}

			arbol.Hd = new Arbol(arbol.pasosMax-arbol.pasos);
			creaArbol(arbol.Hd, prof_min - 1, prof_max - 1);
			arbol.num_nodos = arbol.num_nodos + arbol.Hd.num_nodos-1;
			if (operador != TNodo.values()[3]) // != SIC
				arbol.pasos += arbol.Hd.pasos-1;
			else {
				if (arbol.Hi.pasos >= arbol.Hd.pasos)
					arbol.pasos += arbol.Hi.pasos-1;
				else arbol.pasos += arbol.Hd.pasos-1;
			}
		}
		else { // prof_min = 0
			if(prof_max == 0 || arbol.pasosMax <= 0) { // solo puede ser hoja
				// generacion del subarbol de operando/terminal
				TNodo operando = TNodo.values()[(int) (Math.random()*3)]; // simbolo de operando aleatorio
				arbol.dato = operando;
				arbol.num_nodos++;
			}
			else { // se decide aleatoriamente operando/terminal u operador/funcion
				int tipo = (int) Math.round(Math.random());
				if (tipo == 1) { // se genera operador/funcion
					// generacion del subarbol de operador/funcion
					TNodo operador;
					if (arbol.pasosMax > 1) operador = TNodo.values()[(int) (Math.random()*3)+3]; // operando = SIC/PROGN2/PROGN3
					else operador = TNodo.values()[(int) (Math.random()*2)+3]; // operando = SIC/PROGN2
					arbol.dato = operador;
					arbol.pasos += (operador.ordinal()-2) - 1; // pasos = pasosDelOperador - 1
					// se generan los hijos
					arbol.setHi(new Arbol(arbol.pasosMax-arbol.pasos));
					creaArbol(arbol.getHi(), prof_min, prof_max - 1);
					arbol.num_nodos = arbol.num_nodos + arbol.getHi().num_nodos;
					if (operador != TNodo.values()[3]) // != SIC
						arbol.pasos += arbol.getHi().pasos;

					if (operador == TNodo.values()[5]) { // == PROGN3
						arbol.Hc = new Arbol(arbol.pasosMax-arbol.pasos);
						creaArbol(arbol.Hc, prof_min, prof_max - 1);
						arbol.num_nodos = arbol.num_nodos + arbol.Hc.num_nodos;
						arbol.pasos += arbol.Hc.pasos;
					}

					arbol.Hd = new Arbol(arbol.pasosMax-arbol.pasos);
					creaArbol(arbol.Hd, prof_min, prof_max - 1);
					arbol.num_nodos = arbol.num_nodos + arbol.Hd.num_nodos;
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
					arbol.dato = operando;
					arbol.num_nodos++;
				}
			}
		}
		if (arbol.Hi != null) {
			if (arbol.Hi.profundidad >= arbol.Hd.profundidad && (arbol.Hc == null || arbol.Hi.profundidad >= arbol.Hc.profundidad))
				arbol.profundidad = arbol.Hi.profundidad;
			else if(arbol.Hc == null || arbol.Hd.profundidad >= arbol.Hc.profundidad)
				arbol.profundidad = arbol.Hd.profundidad;
			else arbol.profundidad = arbol.Hc.profundidad;
			arbol.profundidad++;
		}
	}

	public void copiaArbol(Arbol arbol) {
//		this = new Arbol();
		this.dato = arbol.dato;
		if (arbol.Hi != null) {
			this.Hi = new Arbol();
			this.Hi.copiaArbol(arbol.Hi);
			if (arbol.Hc != null) {
				this.Hc = new Arbol();
				this.Hc.copiaArbol(arbol.Hc);
			} else this.Hc = null;
			this.Hd = new Arbol();
			this.Hd.copiaArbol(arbol.Hd);
		}
		else {
			this.Hi = null;
			this.Hc = null;
			this.Hd = null;
		}
		this.num_nodos = arbol.num_nodos;
		this.profundidad = arbol.profundidad;
		this.pasos = arbol.pasos;
		this.pasosMax = arbol.pasosMax;
	}

	public Arbol buscarNodo(int nodo) {
		Arbol a;
		if (nodo  > num_nodos) return null;
		else if (nodo == 1) return this;
		else {
			if (this.Hi != null) {
				nodo--;
				a = this.Hi.buscarNodo(nodo);
				if (a != null) return a;
				nodo = nodo - this.Hi.num_nodos;
				if (this.Hc != null) {
					a = this.Hc.buscarNodo(nodo);
					if (a != null) return a;
					nodo = nodo - this.Hc.num_nodos;
				}
				a = this.Hd.buscarNodo(nodo);
				if (a != null) return a;
				nodo = nodo - this.Hd.num_nodos;
			}
			return this;
		}
	}

	public Arbol buscarHoja(int hoja) {
		Arbol a;
		if (hoja  > pasos) return null;
		else if (hoja == 1) return this;
		else {
			if (this.Hi != null) {
				a = this.Hi.buscarHoja(hoja);
				if (a != null) return a;
				hoja = hoja - this.Hi.pasos;
				if (this.Hc != null) {
					a = this.Hc.buscarHoja(hoja);
					if (a != null) return a;
					hoja = hoja - this.Hc.pasos;
				}
				a = this.Hd.buscarHoja(hoja);
				if (a != null) return a;
				hoja = hoja - this.Hd.pasos;
			}
			return this;
		}
	}
	
	public void sustituirSubarbol(int nodo, Arbol subarbol) {
		Arbol a = this.buscarNodo(nodo);
		if (this.pasosMax - this.pasos >= subarbol.pasos) a.copiaArbol(subarbol);
		actualizaRama(nodo, a, subarbol);
	}

	private void actualizaRama(int nodo, Arbol viejo, Arbol nuevo) {
		if (nodo < num_nodos && nodo > 0) {
			num_nodos = num_nodos - viejo.num_nodos + nuevo.num_nodos;
			pasos = pasos - viejo.pasos + nuevo.pasos;
			if (this.Hi != null) {
				nodo--;
				this.Hi.actualizaRama(nodo, viejo, nuevo);
				nodo -= this.Hi.num_nodos;
				if (this.Hc != null) {
					this.Hc.actualizaRama(nodo, viejo, nuevo);
					nodo -= this.Hc.num_nodos;
				}
				this.Hd.actualizaRama(nodo, viejo, nuevo);
				nodo -= this.Hd.num_nodos;
			}

			if (this.Hi != null) {
				if (this.Hi.profundidad >= this.Hd.profundidad && (this.Hc == null || this.Hi.profundidad >= this.Hc.profundidad))
					this.profundidad = this.Hi.profundidad;
				else if(this.Hc == null || this.Hd.profundidad >= this.Hc.profundidad)
					this.profundidad = this.Hd.profundidad;
				else this.profundidad = this.Hc.profundidad;
				this.profundidad++;
			}
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
	public int getNum_nodos() {
		return num_nodos;
	}
	public void setNum_nodos(int num_nodos) {
		this.num_nodos = num_nodos;
	}
	public double getPasos() {
		return pasos;
	}
	public void setPasos(int pasos) {
		this.pasos = pasos;
	}
}
