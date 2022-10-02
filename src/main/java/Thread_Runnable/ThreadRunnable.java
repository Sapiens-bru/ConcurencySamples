package Thread_Runnable;

public class ThreadRunnable {

    public static void main(String[] args) {

        // Имплементация интерфейса обычным классом
        new Thread(
                new MyRunnableImpl()
        ).start();

        // Имплементация анонимным классом
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Run anonymous " + Thread.currentThread());
                    }
                }
        ).start();

        // Имплементация лямбдой
        new Thread(
                () -> System.out.println("Run lambda " + Thread.currentThread())
        ).start();

        // Имплементация ссылкой на метод
        new Thread(
                MyClass::myMethod
        ).start();
    }

    static class MyRunnableImpl implements Runnable {
        @Override
        public void run() {
            System.out.println("Run " + Thread.currentThread());
        }
    }

    static class MyClass {
        public static void myMethod() {
            System.out.println("Run method reference " + Thread.currentThread());
        }
    }
}
