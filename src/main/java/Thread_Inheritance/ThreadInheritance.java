package Thread_Inheritance;

public class ThreadInheritance {

    public static void main(String[] args) {
        MyThread myThread1 = new MyThread();
        MyThread myThread2 = new MyThread();
        myThread1.start();
        myThread2.start();
    }

    static class MyThread extends Thread{
        @Override
        public synchronized void start() {
            super.start();
            System.out.println("Start " + Thread.currentThread()); //Выполняется в main потоке
        }

        @Override
        public void run() {
            super.run();
            System.out.println("Run " + Thread.currentThread()); //Выполняется в отдельном потоке
        }
    }
}
