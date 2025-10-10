package com.study.thread.start;

public class HelloThreadMain {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ": main() start");

        HelloThread helloThread = new HelloThread();
        // helloThread.run(); // run 을 호출해서는 안됨...
        System.out.println(Thread.currentThread().getName() + ": start 호출 전");
        helloThread.start();
        System.out.println(Thread.currentThread().getName() + ": start 호출 후");

        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}
