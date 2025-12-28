package com.study.thread.start;

public class HelloRunnableMain {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");

        // 실행결과는 같으나 스레드와 해당 스레드가 실행할 작업을 서로 분리.
        HelloRunnable helloRunnable = new HelloRunnable();
        Thread thread = new Thread(helloRunnable); // 스레드 객체를 생성할 떄, 실행할 작업을 생성자로 전달.
        thread.start();

        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}
