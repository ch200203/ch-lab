package com.study.thread.start.test;

import static com.study.util.MyLogger.log;

/**
 * 문제 1. Thread 상속
 * 1. Thread 클래스를 상속받은 CounterThread 라는 클래스를 만들자.
 * 2. 이 스레드는 1 ~ 5 까지의 숫자를 1초 간격으로 출력한다. 앞서 만든 log() 함수를 사용하여 출력한다.
 * 3. main() 메서드에서 CounterThread 스레드 클래스를 만들고 실행하라.
 */
public class StartTest1Main {
    public static void main(String[] args) {
        CounterThread counterThread = new CounterThread();
        counterThread.start();
    }

    static class CounterThread extends Thread {
        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                log("value : " + i);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
