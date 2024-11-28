package es.pgv.Aeropuerto;

public class Tripulante extends Thread {
	
	
	private String Nombre;
	private Aeropuerto objAeropuerto;
	
	public Tripulante(String nombre, Aeropuerto aeropuerto) {
		this.Nombre = nombre;
		this.objAeropuerto = aeropuerto;
	}
	
	@Override
	public void run() {
		try {
			    System.out.println("El " + this.Nombre + " está intentando acceder al control de tripulación.");
				objAeropuerto.Acceder_al_Control_de_Tripulacion(this.Nombre);
				Thread.sleep(1000);
				objAeropuerto.Acceder_al_Embarque_de_Tripulacion(this.Nombre);
		} catch (InterruptedException e) {
			System.out.println("El " + this.Nombre + " ha tenido un percanse.");
		}
	}

}