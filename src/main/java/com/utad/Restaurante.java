package com.utad;

public class Restaurante {
    private int mesasDisponiblesExclusivas;

    public Restaurante(){
        this.mesasDisponiblesExclusivas = 0;
    }

    //--------------------------------------------PARTE CITICA----------------------------------------------------------

    public synchronized void pedirMesaExclusiva(String nombreCliente, int cantidadMesas, double tiempoTarda){
        try{
            while (cantidadMesas > 3){
                System.out.println("Error "+ nombreCliente + " has intentado reservar una cantidad de mesas mayor a las mesas existentes ");
                wait();
            }
            System.out.println(nombreCliente + " has reservado " + cantidadMesas +  "mesas exclusivas");
            mesasDisponiblesExclusivas -= cantidadMesas;
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }


    public synchronized void habilitarMesaExclusiva(String nombreRestaurante, int cantidadMesas, double tiempoTarda){
        if ( cantidadMesas >= 1 && cantidadMesas < 3){
            System.out.println(nombreRestaurante + " habilitó " + cantidadMesas + " mesas exclusivas");
            mesasDisponiblesExclusivas += cantidadMesas;
            notifyAll();
        }else {
            System.out.println( "Error solo se pueden habilitar un n" +
                    "umero de 1 a 3 mesas para clientes exlcusivos");
        }
    }


    //--------------------------------------------PRODUCTOR-------------------------------------------------------------


    public class Productor extends Thread{
        private String nombre;
        private Restaurante restaurante;
        private int cantidadMesas;


        public Productor(String nombre, Restaurante restaurante, int cantidadMesas){
            this.nombre = nombre;
            this.restaurante = restaurante;
            this.cantidadMesas = cantidadMesas;
        }


        public void run(){
            try {
                int tiempoTarda = (int) (Math.random() *6 + 10) *1000 ;
                Thread.sleep(tiempoTarda);
                restaurante.habilitarMesaExclusiva(nombre, cantidadMesas, tiempoTarda);
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }

    //--------------------------------------------CLIENTE---------------------------------------------------------------

    public class Cliente extends Thread {
        private String nombre;
        private Restaurante restaurante;
        private int cantidadMesas;


        public Cliente(String nombre, Restaurante restaurante, int cantidadMesas) {
            this.nombre = nombre;
            this.restaurante = restaurante;
            this.cantidadMesas = cantidadMesas;
        }


        public void run() {
            try {
                int tiempoTarda = (int) (Math.random() * 6 + 10) * 1000;
                Thread.sleep(tiempoTarda);
                restaurante.pedirMesaExclusiva(nombre, cantidadMesas, tiempoTarda);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    //-------------------------------------------MAIN---------------------------------------------------------------


    public static void main(String[] args) {
        Restaurante restaurante = new Restaurante();
        Productor administrador = new Restaurante().new Productor("Restaurante italiano", restaurante, 1);
        Productor administrador2 = new Restaurante().new Productor("Restaurante italiano", restaurante, 1);
        Cliente cliente1 = new Restaurante().new Cliente("Carlos", restaurante, 5);
        Cliente cliente2 = new Restaurante().new Cliente("Katherine", restaurante, 2);
        Cliente cliente3 = new Restaurante().new Cliente("Juliana", restaurante, 1);

        administrador.start();
        administrador2.start();
        cliente1.start();
        cliente2.start();
        cliente3.start();

    }

}
