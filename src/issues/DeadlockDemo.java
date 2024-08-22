package issues;

import static java.lang.Thread.currentThread;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeadlockDemo {

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaforo = new Semaphore(1);
        
        Object ob1 = new Object();
        Object ob2 = new Object();
        Object ob3 = new Object();

        Thread t1 = new Thread(new SyncThread(ob1, ob2, semaforo), "hilo1");
        Thread t2 = new Thread(new SyncThread(ob2, ob3, semaforo), "hilo2");
        Thread t3 = new Thread(new SyncThread(ob3, ob1, semaforo), "hilo3");

        t1.start();
        Thread.sleep(5000);
        t2.start();
        Thread.sleep(5000);
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Finalizado");
    }
}

class SyncThread implements Runnable {

    private Semaphore semaforo;
    private Object ob1;
    private Object ob2;

    public SyncThread(Object ob1, Object ob2, Semaphore semaforo) {
        this.ob1 = ob1;
        this.ob2 = ob2;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        try {
            semaforo.acquire();
            System.out.println(currentThread().getName() + " generando lock en " + ob1);
            synchronized (ob1) {
                System.out.println(currentThread().getName() + " lock generado en " + ob1);
                work();
                System.out.println(currentThread().getName() + " generando lock en " + ob2);
                synchronized (ob2) {
                    System.out.println(currentThread().getName() + " lock generado en " + ob2);
                    work();
                }
                System.out.println(currentThread().getName() + " lock liberado en " + ob2);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SyncThread.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            semaforo.release();
        }
        System.out.println(currentThread().getName() + " lock liberado en " + ob1);
        System.out.println("Finalizó ejecución");
    }

    private void work() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}