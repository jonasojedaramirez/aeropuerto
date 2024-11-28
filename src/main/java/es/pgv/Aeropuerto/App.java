package es.pgv.Aeropuerto;

/**
 * Autor: Daniel Pérez Pérez
 * Fecha: 05/09/2022
 * 
 * Modificado para presentarlo por Jonás Ojeda Ramírez
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	System.out.println("INICIO SIMULACIÓN");
    	
    	//Se inicializa el aeropuerto
        Aeropuerto objAeropuerto = new Aeropuerto();
        
       /* //Se envían 8 tripulantes al control de tripulación
        for (int i = 1; i <= 8; i++) {
        	new Tripulante("Tripulante " + i, objAeropuerto).start();
        }*/
        
        //Se envían 140 pasajeros al aeropuerto
        for (int i = 1; i <= 14; i++) {  // lo pongo wn 14 para probar
        	new Pasajero("Pasajero " + i, objAeropuerto).start();
        }
        
        //Se autoriza a 2 aviones a operar los vuelos NT361 y NT362
        for (int i = 1; i <= 2; i++) {
        	new Avion("Vuelo NT36" + i, objAeropuerto).start();
        }
        
    }
}
