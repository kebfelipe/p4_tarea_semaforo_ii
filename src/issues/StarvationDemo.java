package issues;

import java.util.concurrent.Semaphore;

/**
 *
 * @author KebFelipe
 */
public class StarvationDemo {

    public static void main(String[] args) {
        Semaphore semaforo = new Semaphore(1);
        
        Thread hiloAltaPrioridad = new Thread(new TareaAltaPrioridad(semaforo), "Hilo alta prioridad");
        Thread hiloBajaPrioridad = new Thread(new TareaBajaPrioridad(semaforo), "Hilo baja prioridad");

        hiloAltaPrioridad.setPriority(Thread.MAX_PRIORITY);
        hiloBajaPrioridad.setPriority(Thread.MIN_PRIORITY);

        hiloAltaPrioridad.start();
        hiloBajaPrioridad.start();
    }

    static class TareaAltaPrioridad implements Runnable {

        private Semaphore semaforo;

        public TareaAltaPrioridad(Semaphore semaforo) {
            this.semaforo = semaforo;
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    semaforo.acquire();
                    System.out.println("Hilo de alta prioridad ejecutándose");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaforo.release();
                }
            }

        }
    }

    static class TareaBajaPrioridad implements Runnable {

        private Semaphore semaforo;

        public TareaBajaPrioridad(Semaphore semaforo) {
            this.semaforo = semaforo;
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    semaforo.acquire();
                    System.out.println("Hilo de baja prioridad ejecutándose");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaforo.release();
                }
            }

        }
    }
}
