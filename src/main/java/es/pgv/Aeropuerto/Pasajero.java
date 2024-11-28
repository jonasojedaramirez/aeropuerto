package es.pgv.Aeropuerto;

public class Pasajero extends Thread {
	
	
	private String Nombre;
	private Aeropuerto objAeropuerto;
	
	public Pasajero(String nombre, Aeropuerto aeropuerto) {
		this.Nombre = nombre;
		this.objAeropuerto = aeropuerto;
	}
	
	@Override
	public void run() {
		try {
			    System.out.println("El " + this.Nombre + " está intentando acceder al aeropuerto.");
				objAeropuerto.Entrar_al_Salon_de_Pasajeros(this.Nombre);
				Thread.sleep(1000);
				objAeropuerto.Acceder_al_Cheking_de_Pasajeros(this.Nombre);
				Thread.sleep(1000);
				objAeropuerto.Acceder_al_Control_de_Pasajeros(this.Nombre);
				Thread.sleep(1000);
				objAeropuerto.Acceder_al_Embarque_de_Pasajeros(this.Nombre);
		} catch (InterruptedException e) {
			System.out.println("Al " + this.Nombre + " le ha dado un paro cardiaco.");
		}
	}

}
