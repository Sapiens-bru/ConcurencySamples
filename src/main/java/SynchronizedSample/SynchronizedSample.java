package SynchronizedSample;

public class SynchronizedSample {

    public static void main(String[] args) {

//        Это тка НЕ РАБОТАЕТ
//        synchronized (new Object()) {
//            new Thread(new MyRunnableImpl()).start();
//            new Thread(new MyRunnableImpl()).start();
//        }
//        Не работает потому что при подобном использовании блокировку получает только главный поток.
//        Чтобы блокировка работала нужно чтобы её получали отдельные потоки

        // без блокировки. Внутри потоков вызыввается sleep но тормозит только поток внутри, поэтому текст выводится параллельно
        new Thread(new MyRunnableImpl()).start();
        new Thread(new MyRunnableImpl()).start();

        // с блокировкой через блок кода.
        // внутри потока вызывается sleep и второй(следующие) потоки ждут пока не закончится синхронизированный блок
        MyRunnableImplSyncBlock.lock = new Object();
        new Thread(new MyRunnableImplSyncBlock()).start();
        new Thread(new MyRunnableImplSyncBlock()).start();

        // c блокировкой через блок метода
        MyRunnableImplSyncMethod.lock = new MyRunnableImplSyncMethod.MyClass();
        new Thread(new MyRunnableImplSyncMethod()).start();
        new Thread(new MyRunnableImplSyncMethod()).start();

        // c блокировкой через блок метода. Явного объекта для блокировки здесь не создается.
        // синхронизация по статическому методу идет по классу, а не объекту класса.
        new Thread(new MyRunnableImplSyncStaticMethod()).start();
        new Thread(new MyRunnableImplSyncStaticMethod()).start();


    }

    static class MyRunnableImpl implements Runnable {
        @Override
        public void run() {
            System.out.println("Run before pause " + Thread.currentThread());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Run after pause " + Thread.currentThread());
        }
    }

    static class MyRunnableImplSyncBlock implements Runnable {

        static Object lock;

        @Override
        public void run() {
//            Это так НЕ РАБОТАЕТ
//            synchronized (this) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                System.out.println("Run sync " + Thread.currentThread());
//            }
//            Не работает потому что (this) это внутренняя ссылка объекта и она при каждом вызове разная
//            значит и блокировка каждый раз будет разная (что сводить ее суть на нет)

            synchronized (lock) {
                System.out.println("Run sync block before pause " + Thread.currentThread());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Run sync block after pause " + Thread.currentThread());
            }
        }
    }

    static class MyRunnableImplSyncMethod implements Runnable {

        static MyClass lock;

        @Override

//        Это НЕ РАБОТАЕТ
//        public synchronized void run() {
//        по той же причине по которой не работало synchronized(this){}
//        синхронизация по методу неявным образом использует сам объект в качестве блокировки,
//        То есть использовать синхронизированный метод мы можем только если использовать его от одного и того же объекта

        public synchronized void run() {

            lock.doThings();

        }

        static class MyClass{
            public synchronized void doThings(){
                System.out.println("Run sync method before pause " + Thread.currentThread());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Run sync method after pause " + Thread.currentThread());
            }
        }
    }

    static class MyRunnableImplSyncStaticMethod implements Runnable {

        synchronized static void doThingsStatic(){

            System.out.println("Run sync static method before pause " + Thread.currentThread());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Run sync static method after pause " + Thread.currentThread());
        }

        @Override
        public void run() {
            doThingsStatic();
        }

    }

}
