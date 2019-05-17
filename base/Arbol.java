package base;

public class Arbol {
	private TNodo dato; // operando u operador
	Arbol Hi; // hijo izquierdo
	Arbol Hc; // hijo central
	Arbol Hd; // hijo derecho
	int num_nodos; // numero de nodos
	int altura; // altura del arbol
	int pasos = 1; // numero de hojas/terminales del arbol

	int pasosMax; // numero max de pasos que puede dar

	public static enum TNodo {
		AVANZA, GIRA_DERECHA, GIRA_IZQUIERDA,
		SIC, PROGN2, PROGN3;
	}

	public Arbol(int pasosMax) {
		this.pasosMax = pasosMax;
	}
	public Arbol() {}

	public void creaArbol(int prof_min, int prof_max){
		num_nodos = 1;
		int tipo = (int) Math.round(Math.random()); // se decide aleatoriamente operando/terminal u operador/funcion

		// se genera operador/funcion si tiene que crecer o si no ha alcanzado la prof_max y el tipo es uno
		if (prof_min > 0 || (prof_max > 0 && tipo == 1)) {
			// generacion del subarbol de operador/funcion
			TNodo operador;
			if (pasosMax > 1) operador = TNodo.values()[(int) (Math.random()*3)+3]; // operando = SIC/PROGN2/PROGN3
			else operador = TNodo.values()[(int) (Math.random()*2)+3]; // operando = SIC/PROGN2
			dato = operador;
			pasos += (operador.ordinal()-2) - 1; // pasos = pasosDelOperador - 1

			// hijo izq
			setHi(new Arbol());
			Hi.creaArbol(prof_min - 1, prof_max - 1);
			num_nodos += Hi.num_nodos;
			pasos = getHi().pasos;
			
			// hijo central
			if (operador == TNodo.values()[5]) { // == PROGN3
				Hc = new Arbol();
				Hc.creaArbol(prof_min - 1, prof_max - 1);
				pasos += Hc.pasos;
				num_nodos += Hc.num_nodos;
			}
			
			// hijo drch
			Hd = new Arbol();
			Hd.creaArbol(prof_min - 1, prof_max - 1);
			num_nodos += Hd.num_nodos;
			pasos += Hd.pasos;

			altura = Math.max(Hi.altura, Hd.altura);
			if(Hc != null) altura = Math.max(altura, Hc.altura);
			altura++;
		} // se genera operando
		else { // solo puede ser hoja
			// generacion del subarbol de operando/terminal
			TNodo operando = TNodo.values()[(int) (Math.random()*3)]; // simbolo de operando aleatorio
			dato = operando;
		}
	}

	public void copiaArbol(Arbol arbol) {
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
			return null;
		}
	}

	public Arbol buscarFuncion(int numFuncion) {
		Arbol a;
		if (numFuncion  > (num_nodos - pasos)) return null;
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
			return null;
		}
	}

	public Arbol buscarHoja(int hoja) {
		Arbol a;
		if (hoja > pasos) return null;
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
			return null;
		}
	}

	// sutituye en el arbol this el nodo por subarbol
	public void sustituirSubarbol(int nodo, Arbol subarbol) {
		if (nodo == 1) this.copiaArbol(subarbol);
		else if (num_nodos >= nodo && nodo > 0 && Hi != null) {
			nodo--;
			Hi.sustituirSubarbol(nodo, subarbol);
			nodo -= Hi.num_nodos;
			if (Hc != null) {
				Hc.sustituirSubarbol(nodo, subarbol);
				nodo -= Hc.num_nodos;
			}
			Hd.sustituirSubarbol(nodo, subarbol);

			num_nodos = Hi.num_nodos + Hd.num_nodos + 1;
			pasos = Hi.pasos + Hd.pasos;
			if (Hc != null) {
				num_nodos += Hc.num_nodos;
				pasos += Hc.pasos;
			}
			
			altura = Math.max(Hi.altura, Hd.altura);
			if(Hc != null) altura = Math.max(altura, Hc.altura);
			altura++;
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
		String s = "";

		s += this.getDato().name();
		if (this.Hi != null) {
			s += "(" + this.Hi.toString() + ",";
			if (this.Hc != null) s += this.Hc.toString() + ",";
			s += this.Hd.toString() + ")";
		}

		return s;
	}

	// Para depurar bonito
/*	public String toString() {
		String s = "", t = "";

		for (int i = 5; i > altura; i--)
			t += "\t";
		s += num_nodos + ": ";
		s += this.getDato().name();
		if (this.Hi != null) {
			s += "(\n" + t + "Hi: " + this.Hi.toString() + ",";
			if (this.Hc != null) s += "\n" + t + "Hc: " + this.Hc.toString() + ",";
			s += "\n" + t + "Hd: " + this.Hd.toString() + ")";
		}

		return s;
	}
*/
	
	// Para depurar
	public static void main(String args[]) {
		Arbol a = new Arbol(400), b = new Arbol(400);
		int nodo;
		a.creaArbol(2, 3);
		b.creaArbol(1, 2);
		nodo = (int) (Math.random()*(a.getNum_nodos())+1);
		System.out.println(a.toString() + "\n" + b.toString() + "\nNodo: " + nodo);
		a.sustituirSubarbol(nodo, b);
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
	public int getPasosMax() {
		return pasosMax;
	}
	public void setPasosMax(int pasosMax) {
		this.pasosMax = pasosMax;
	}

}
