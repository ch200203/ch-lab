package com.study.thread.start;

public class BadThreadMain {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ": main() start");

        HelloThread helloThread = new HelloThread();
        System.out.println(Thread.currentThread().getName() + ": start 호출 전");
        helloThread.run(); // run 직접실행 -> main 스레드가 실행해버림. -> 따라서 start() 로 실행해야함
        System.out.println(Thread.currentThread().getName() + ": start 호출 후");

        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}
