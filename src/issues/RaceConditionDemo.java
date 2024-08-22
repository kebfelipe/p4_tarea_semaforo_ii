package issues;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KebFelipe
 */
public class RaceConditionDemo {
        
    public static void main(String[] args) throws InterruptedException {
        
        Semaphore semaforo = new Semaphore(1);
        Contador contador = new Contador();
        
        Thread hilo1 = new Thread(new HiloContador(contador, semaforo), "Hilo 1");
        Thread hilo2 = new Thread(new HiloContador(contador, semaforo), "Hilo 2");

        hilo1.start();
        hilo2.start();

        hilo1.join();
        hilo2.join();

        System.out.println("Valor final del contador: " + contador.getContador());
    }
}

class HiloContador implements Runnable {

    private final Semaphore semaforo;
    private final Contador contador;

    public HiloContador(Contador contador, Semaphore semaforo) {
        this.contador = contador;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        try {
            semaforo.acquire();
            for (int i = 0; i < 1000; i++) {
                contador.incrementarContador();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloContador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            semaforo.release();
        }
    }

}

class Contador {

    private int contador = 0;

    public void incrementarContador() {
        contador++;
    }

    public int getContador() {
        return contador;
    }

}
