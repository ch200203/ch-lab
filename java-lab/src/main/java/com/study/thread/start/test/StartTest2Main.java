package com.study.thread.start.test;

import static com.study.util.MyLogger.log;

/**
 * 문제 2. Runnable 구현
 * 1. CounterRunnable 이라는 이름의 클래스를 만들자, 이 클래스는 Runnable 인터페이스를 구현한다.
 * 2. CounterRunnable 1 ~ 5까지의 숫자를 1초간격으로 출력해야 한다. 앞서 우리가 만든 log() 기능을 이용해서 출력하자.
 * 3. main() 메서드에서 CounterRunnable 인스턴스를 이용해서 Thread 를 생성하고 실행하라
 * 4. 스레드의 이름은 "Counter" 로 지정해야한다.
 */
public class StartTest2Main {
    public static void main(String[] args) {
        Thread thread = new Thread(new CounterRunnable(), "counter");
        thread.start();
    }

    static class CounterRunnable implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                log("value : " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
