package base;

import java.util.Comparator;

public class OrdenCromosoma implements Comparator<Cromosoma> {
	public int compare(Cromosoma A, Cromosoma B) {
		return Double.compare(B.getAdptacion(), A.getAdptacion());
	}
}
