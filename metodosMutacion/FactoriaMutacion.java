package metodosMutacion;


public class FactoriaMutacion {
	public static AlgoritmoMutacion getAlgoritmoDeMutacion(String algoritmo) {
		switch(algoritmo) {
			case "Terminal": return new MutacionTerminal();
			case "Funcion": return new MutacionFuncion();
			case "Inicializacion": return new MutacionInicializacion();
			default: return new MutacionTerminal();
		}
	}
}
