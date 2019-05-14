package base;

public class Arbol {
	private TNodo dato; // operando u operador
	Arbol Hi; // hijo izquierdo
	Arbol Hc; // hijo central
	Arbol Hd; // hijo derecho
	private int num_nodos; // numero de nodos
	int altura; // altura del arbol
	private int profundidad; // profundidad del arbol
	private int pasos = 1; // numero de hojas/terminales del arbol

	private int pasosMax; // numero max de pasos que puede dar

	public static enum TNodo {
		AVANZA, GIRA_DERECHA, GIRA_IZQUIERDA,
		SIC, PROGN2, PROGN3;
	}

	public Arbol(int pasosMax, int profundidad) {
		this.pasosMax = pasosMax;
		this.setProfundidad(profundidad);
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
			arbol.Hi = new Arbol(arbol.pasosMax-arbol.pasos, arbol.getProfundidad()+1);
			creaArbol(arbol.Hi, prof_min - 1, prof_max - 1);
			if (operador != TNodo.values()[3]) // != SIC
				arbol.pasos += arbol.Hi.pasos-1;

			if (operador == TNodo.values()[5]) { // == PROGN3
				arbol.Hc = new Arbol(arbol.pasosMax-arbol.pasos, arbol.getProfundidad()+1);
				creaArbol(arbol.Hc, prof_min - 1, prof_max - 1);
				arbol.pasos += arbol.Hc.pasos-1;
			}

			arbol.Hd = new Arbol(arbol.pasosMax-arbol.pasos, arbol.getProfundidad()+1);
			creaArbol(arbol.Hd, prof_min - 1, prof_max - 1);
		}
		else { // prof_min = 0
			if(prof_max == 0 || arbol.pasosMax <= 0) { // solo puede ser hoja
				// generacion del subarbol de operando/terminal
				TNodo operando = TNodo.values()[(int) (Math.random()*3)]; // simbolo de operando aleatorio
				arbol.dato = operando;
				arbol.num_nodos = 1;
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
					arbol.setHi(new Arbol(arbol.pasosMax-arbol.pasos, arbol.getProfundidad()+1));
					creaArbol(arbol.getHi(), prof_min, prof_max - 1);
					if (operador != TNodo.values()[3]) // != SIC
						arbol.pasos += arbol.getHi().pasos-1;

					if (operador == TNodo.values()[5]) { // == PROGN3
						arbol.Hc = new Arbol(arbol.pasosMax-arbol.pasos, arbol.getProfundidad()+1);
						creaArbol(arbol.Hc, prof_min, prof_max - 1);
						arbol.pasos += arbol.Hc.pasos-1;
					}

					arbol.Hd = new Arbol(arbol.pasosMax-arbol.pasos, arbol.getProfundidad()+1);
					creaArbol(arbol.Hd, prof_min, prof_max - 1);
				} // se genera operando
				else { // generacion del subarbol de operando/terminal
					TNodo operando = TNodo.values()[(int) (Math.random()*3)]; // simbolo de operando aleatorio
					arbol.dato = operando;
					arbol.num_nodos = 1;
				}
			}
		}
		if (arbol.esFuncion()) {
			if (arbol.Hi.altura >= arbol.Hd.altura && (arbol.Hc == null || arbol.Hi.altura >= arbol.Hc.altura))
				arbol.altura = arbol.Hi.altura;
			else if(arbol.Hc == null || arbol.Hd.altura >= arbol.Hc.altura)
				arbol.altura = arbol.Hd.altura;
			else arbol.altura = arbol.Hc.altura;
			arbol.altura++;
			
			arbol.num_nodos = 1 + arbol.Hi.num_nodos + arbol.Hd.num_nodos;
			if (arbol.Hc != null) arbol.num_nodos += arbol.Hc.num_nodos;
			
			if (arbol.dato != TNodo.values()[3]) // != SIC
				arbol.pasos += arbol.Hd.pasos-1;
			else {
				if (arbol.getHi().pasos >= arbol.Hd.pasos)
					arbol.pasos += arbol.getHi().pasos-1;
			}
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
		this.altura = arbol.altura;
		this.pasos = arbol.pasos;
		this.pasosMax = arbol.pasosMax;
		this.profundidad = arbol.profundidad;
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

	public Arbol buscarFuncion(int numFuncion) {
		Arbol a;
		if (numFuncion  > (num_nodos - pasos)) {
			return null;
		}
		else if (esFuncion() && numFuncion == 1) return this;
		else {
			if (this.Hi != null) {
				numFuncion--;
				a = this.Hi.buscarFuncion(numFuncion);
				if (a != null) return a;
				if (a == null) numFuncion -= this.Hi.num_nodos - this.Hi.pasos ;
				if (this.Hc != null) {
					a = this.Hc.buscarFuncion(numFuncion);
					if (a != null) return a;
					if (a == null) numFuncion -= this.Hc.num_nodos - this.Hc.pasos ;
				}
				a = this.Hd.buscarFuncion(numFuncion);
				if (a != null) return a;
			}
			return this;
		}
	}

	public Arbol buscarHoja(int hoja) {
		Arbol a;
		if (hoja  > pasos) return null;
		else if (hoja == 1 && esTerminal()) return this;
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
	
	public boolean sustituirSubarbol(int nodo, Arbol subarbol) {
		Arbol a = this.buscarNodo(nodo);
		if (this.pasosMax - this.pasos >= subarbol.pasos) {
			a.copiaArbol(subarbol);
			profundidad = 0;
			actualizaRama(nodo, a, subarbol);
			return true;
		}
		return false;
	}

	private void actualizaRama(int nodo, Arbol viejo, Arbol nuevo) {
		if (nodo < num_nodos && nodo > 0) {
			num_nodos = num_nodos - viejo.num_nodos + nuevo.num_nodos;
			pasos = pasos - viejo.pasos + nuevo.pasos;
			if (this.Hi != null) {
				nodo--;
				this.Hi.profundidad = profundidad + 1;
				this.Hi.actualizaRama(nodo, viejo, nuevo);
				nodo -= this.Hi.num_nodos;
				if (this.Hc != null) {
					this.Hc.profundidad = profundidad + 1;
					this.Hc.actualizaRama(nodo, viejo, nuevo);
					nodo -= this.Hc.num_nodos;
				}
				this.Hd.profundidad = profundidad + 1;
				this.Hd.actualizaRama(nodo, viejo, nuevo);
				nodo -= this.Hd.num_nodos;

				if (this.Hi.altura >= this.Hd.altura && (this.Hc == null || this.Hi.altura >= this.Hc.altura))
					this.altura = this.Hi.altura;
				else if(this.Hc == null || this.Hd.altura >= this.Hc.altura)
					this.altura = this.Hd.altura;
				else this.altura = this.Hc.altura;
				this.altura++;
			}
		}
	}
	
	public boolean esTerminal() {
		if(this.dato == TNodo.AVANZA || this.dato == TNodo.GIRA_DERECHA || this.dato == TNodo.GIRA_IZQUIERDA)
			return true;
		return false;
	}
	
	public boolean esFuncion() {
		if(this.dato == TNodo.SIC || this.dato == TNodo.PROGN2 || this.dato == TNodo.PROGN3)
			return true;
		return false;
	}

	public String toString() {
		String s;

		s = this.getDato().name();
		if (this.Hi != null) {
			s += "(" + this.Hi.toString() + ",";
			if (this.Hc != null) s += this.Hc.toString() + ",";
			s += this.Hd.toString() + ")";
		}
		
		return s;
	}
	
//	public String toString(String guiones, String espacios) {
//		String s = "";
//		
//		if (this.Hi != null) {
//			s += espacios+"|-" + "(" + Integer.toString(this.Hi.getNum_nodos()) + ")";
//			if (this.Hc != null) s += "("+Integer.toString(this.Hc.getNum_nodos()) + ")";
//			else s += "   ";
//			s += "("+Integer.toString(this.Hd.getNum_nodos()) + ")\n";
//			
//			s += this.Hi.toString(guiones+"-", espacios+"   ");
//			if (this.Hc != null) s += this.Hc.toString(guiones+"-", espacios+"     ");
//			s += this.Hd.toString(guiones+"-", espacios+"         ");
//		}
//		
//		return s;
//	}
	
	// Para depurar
	public static void main(String args[]) {
		Arbol a = new Arbol(400, 0);
		a.creaArbol(a, 2, 4);
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
	public int getAltura() {
		return altura;
	}
	public void setAltura(int profundidad) {
		this.altura = profundidad;
	}
	public int getProfundidad() {
		return profundidad;
	}
	public void setProfundidad(int profundidad) {
		this.profundidad = profundidad;
	}
	
}
