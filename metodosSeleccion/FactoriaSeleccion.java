package metodosSeleccion;

public class FactoriaSeleccion {
	public static AlgoritmoSeleccion getAlgoritmoDeSeleccion(String algoritmo, int participantes) {
		switch(algoritmo) {
			case "Ruleta": return new SeleccionRuleta();
			case "Torneo": return new SeleccionTorneo(participantes);
			case "Restos": return new SeleccionRestos();
			case "Ranking": return new SeleccionRanking();
			case "Truncamiento": return new SeleccionTruncamiento();
			case "Propia": return new SeleccionPropia();
			default: return new SeleccionRuleta();
		}
	}
}
