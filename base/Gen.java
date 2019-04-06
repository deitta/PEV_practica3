package base;

public class Gen {
	int ciudad;

	public Gen(int ciudad){
		this.ciudad = ciudad;		
	}
	
	public String fenotipo(){
		return Ciudades.nombreCiudad(ciudad);
	}

	public void setCiudad(int ciudad){
		this.ciudad = ciudad;
	}

	public int getCiudad() {
		return ciudad; 
	}
	
	public void copiaGen(Gen gen){
		this.ciudad = gen.ciudad;
	}
	
	// Para la depuracion
	public String toString(){
		return Integer.toString(ciudad);
	}
}
