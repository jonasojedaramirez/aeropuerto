package es.pgv.Aeropuerto;

import java.util.concurrent.Semaphore;

public class Aeropuerto {
	
	//Contadores que registran el número de pasajeros o tripulantes en cada zona
	private int SalonDePasajeros;
	private int ChekingDePasajeros;
	private int ControlDePasajeros;
	private int EmbarqueDePasajeros;
	private int ControlDeTripulacion;
	private int EmbarqueDeTripulacion;
	
	
	// creo un mutex por recurso en el que leen dos actores minimo y por lo menos uno escribe
	// creo un mutex por cada sala, cada recurso, al que acceden al menos dos o mas y uno lee y escribe
	Semaphore mutexSalonDePasajeros;
	Semaphore mutexChekingDePasajeros;
	Semaphore mutexControlDePasajeros;
	Semaphore mutexEmbarqueDePasajeros;
	Semaphore mutexControlDeTripulacion;
	Semaphore mutexEmbarqueDeTripulacion;
	
	
	
	
	// creo los semaforos para controlar los limites es un semafor por el par, actor-recurso.
	Semaphore semSalonDePasajeros_Productor;
	Semaphore semSalonDePasajeros_Consumidor;
	Semaphore semCheckInPasajeros_Productor;
	Semaphore semCheckInPasajeros_Consumidor;
	Semaphore semControlDePasajeros_Productor;
	Semaphore semControlDePasajeros_Consumidor;
	Semaphore semEmbarqueDePasajeros_Productor;
	Semaphore semEmbarqueDePasajeros_Consumidor;
	Semaphore semControlDeTripulacion_Productor;
	Semaphore semControlDeTripulacion_Consumidor;
	Semaphore semEmbarqueDeTripulacion_Productor;
	Semaphore semEmbarqueDeTripulacion_Consumidor;
	
	
	//Se inicializa el aeropuerto. En este constructor también podrías inicializar los semaforos.
	public Aeropuerto() {
		
		this.SalonDePasajeros = 0;
		this.ChekingDePasajeros = 0;
		this.ControlDePasajeros = 0;
		this.EmbarqueDePasajeros = 0;
		this.ChekingDePasajeros = 0;
		this.EmbarqueDeTripulacion = 0;
		
		// inicializo los mutex
		this.mutexSalonDePasajeros = new Semaphore(1);  // el problema dice que no se pide garantizar prioridad
		this.mutexChekingDePasajeros = new Semaphore(1, true);
		this.mutexControlDePasajeros = new Semaphore(1, true);
		this.mutexEmbarqueDePasajeros = new Semaphore(1, true);
		this.mutexControlDeTripulacion = new Semaphore(1, true);
		this.mutexEmbarqueDeTripulacion = new Semaphore(1, true);
		
		// inicializo los semaforos que controlan los limites y garantizo la prioridad
		
		this.semSalonDePasajeros_Productor = new Semaphore(100); // el problema dice que no se pide garantizar prioridad
		this.semSalonDePasajeros_Consumidor = new Semaphore(0);  // el problema dice que no se pide garantizar prioridad
		this.semCheckInPasajeros_Productor = new Semaphore(6, true);
		this.semCheckInPasajeros_Consumidor = new Semaphore (0, true);
		this.semControlDePasajeros_Productor = new Semaphore(3, true);
		this.semControlDePasajeros_Consumidor = new Semaphore(0, true);
		this.semEmbarqueDePasajeros_Productor = new Semaphore(100, true);
		this.semEmbarqueDePasajeros_Consumidor = new Semaphore(0, true);	
		
		
		
		this.semControlDeTripulacion_Productor = new Semaphore(2, true);  // maximo huecos dos
		this.semControlDeTripulacion_Consumidor = new Semaphore(0, true); // 
		this.semEmbarqueDeTripulacion_Productor = new Semaphore(4, true); // hay 4 huecos
		this.semEmbarqueDeTripulacion_Consumidor = new Semaphore(0, true); // 
		
	}
	
	//Método que permitirá al pasajero intentar acceder al hall del aeropuerto.
	public void Entrar_al_Salon_de_Pasajeros(String NombrePasajero) {
		try {
			this.semSalonDePasajeros_Productor.acquire(100);  
			this.mutexSalonDePasajeros.acquire();
			this.SalonDePasajeros++;
			System.out.println("El " + NombrePasajero + " ha entrado en el salón de pasajeros. (Pasajeros zona salon = "+SalonDePasajeros+")");
			this.mutexSalonDePasajeros.release();
			this.semCheckInPasajeros_Consumidor.release();  // aviso de que hay un hueco libre
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}

	//Método que permitirá al pasajero acceder al cheking.
	public void Acceder_al_Cheking_de_Pasajeros(String NombrePasajero) {
		
		// quito los que salen del salon
			try {
				this.semSalonDePasajeros_Consumidor.release();
				this.mutexSalonDePasajeros.acquire();
				this.SalonDePasajeros--;
				System.out.println("El " + NombrePasajero + " sale del salón de pasajeros. (Pasajeros zona salon = "+SalonDePasajeros+")");
				this.mutexSalonDePasajeros.release();
				this.semSalonDePasajeros_Productor.release(100);  // aviso de los huecos libres
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
		
		
		try {
			this.semCheckInPasajeros_Productor.acquire(6);
			this.mutexChekingDePasajeros.acquire();
			this.ChekingDePasajeros+=6;
			System.out.println("El " + NombrePasajero + " ha accedido al cheking de pasajeros. (Pasajeros zona cheking = "+ChekingDePasajeros+")");
			this.mutexChekingDePasajeros.release();
			this.semCheckInPasajeros_Consumidor.release();  // aviso de hueco libre
					
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
				
				
	}
	
	//Método que permitirá al pasajero acceder a la zona de control.
	public void Acceder_al_Control_de_Pasajeros(String NombrePasajero) {
			// saco los que salen de la anterior sala
				try {
					this.semCheckInPasajeros_Consumidor.acquire();
					this.mutexChekingDePasajeros.acquire();
					this.ChekingDePasajeros--;
					System.out.println("El " + NombrePasajero + " ha salido del cheking de pasajeros. (Pasajeros zona cheking = "+ChekingDePasajeros+")");
					this.mutexSalonDePasajeros.release();
					this.semCheckInPasajeros_Productor.release(6);  // aviso de los huecos libres
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
		try {
				this.semControlDePasajeros_Productor.acquire(3);
				this.mutexControlDePasajeros.acquire();
				this.ControlDePasajeros+=3;
				System.out.println("El " + NombrePasajero + " ha entrado en el control de pasajeros. (Pasajeros zona control = "+ControlDePasajeros+")");
				this.mutexControlDePasajeros.release();
				this.semControlDePasajeros_Consumidor.release();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
		
	}
	
	//Método que permitirá al pasajero acceder a la zona de embarque.
	public void Acceder_al_Embarque_de_Pasajeros(String NombrePasajero) {
				// resto los que salen de la anterior sale
		
			try {
					this.semControlDePasajeros_Consumidor.release();
					this.mutexControlDePasajeros.acquire();
					this.ControlDePasajeros-=3;
					System.out.println("El " + NombrePasajero + " ha salido de control de pasajeros. (Pasajeros zona control = "+ControlDePasajeros+")");
					this.mutexControlDePasajeros.release();
					this.semControlDePasajeros_Productor.acquire(3);
			} catch (InterruptedException e) {
				
					e.printStackTrace();
					}
					
		
		
			try {
					this.semEmbarqueDePasajeros_Productor.acquire(100);
					this.mutexEmbarqueDePasajeros.acquire();
					this.EmbarqueDePasajeros += 100;
					System.out.println("El " + NombrePasajero + " ha entrado a la zona de embarque de pasajeros. (Pasajeros zona embarque = "+EmbarqueDePasajeros+")");
					this.mutexEmbarqueDePasajeros.release();
					this.semControlDePasajeros_Productor.release(3);  // aviso de hueco libre
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

	}
	
	//Método que permitirá a un tripulante acceder al control.
	public void Acceder_al_Control_de_Tripulacion(String NombreTripulante) {
				try {
					
					semControlDeTripulacion_Productor.acquire();
					mutexControlDeTripulacion.acquire();
					ControlDeTripulacion++;
					System.out.println("El " + NombreTripulante + " ha entrado al control de tripulación. (Tripulantes zona control = "+ControlDeTripulacion+")");
					mutexControlDeTripulacion.release();
					semControlDeTripulacion_Consumidor.release();  // aviso que hay un hueco
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
				
	}
	
	//Método que permitirá a un tripulante acceder a su zona de embarque.
	public void Acceder_al_Embarque_de_Tripulacion(String NombreTripulante) {
			//debo primero restar los recursos de control para poder entrar a embarque, en ese caso es consumidor
		try {
			
			this.semControlDeTripulacion_Consumidor.acquire();
			
			this.mutexControlDeTripulacion.acquire();
			this.ControlDeTripulacion--;				// resto de control de tripulacion
			this.mutexControlDeTripulacion.release();
			
			this.semControlDeTripulacion_Productor.release();   // aviso que hay un hueco libre
		
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
			
		
						
		try {
			this.semEmbarqueDeTripulacion_Productor.acquire();
			
			this.mutexEmbarqueDeTripulacion.acquire();
			this.EmbarqueDeTripulacion++;
			System.out.println("El " + NombreTripulante + " ha entrado a la zona de embarque de la tripulación. (Tripulantes zona embarque = "+EmbarqueDeTripulacion+")");
			this.mutexEmbarqueDeTripulacion.release();
			this.semEmbarqueDeTripulacion_Consumidor.release();
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
				
	}
	
	//Método que permitirá a un vuelo embarcar a los 4 tripulantes.
	public void Embarcar_Tripulantes_Al_Vuelo(String NombreVuelo) {

				try {
					this.semEmbarqueDeTripulacion_Consumidor.acquire(4);
					this.mutexEmbarqueDeTripulacion.acquire();
					this.EmbarqueDeTripulacion =-4; // resto los que salen de embarque
					this.mutexEmbarqueDeTripulacion.release();
					
					this.semEmbarqueDeTripulacion_Productor.release(4);
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				System.out.println("El " + NombreVuelo + " ha embarcado a 4 tripulantes.");

	}
	
	//Método que permitirá a un vuelo embarcar al pasaje.
	public void Embarcar_Pasajeros_Al_Vuelo(String NombreVuelo) {
		
				System.out.println("El " + NombreVuelo + " ha embarcado a 70 pasajeros. (Pasajeros zona embarque = "+EmbarqueDePasajeros+")");

	}
	
	

}
