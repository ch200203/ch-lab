package com.study.thread.start.test;

import static com.study.util.MyLogger.log;

/**
 * 문제 4. 여러 스레드 사용
 * - Thread-A, Thread-B 두 스레드를 만들어라
 * - Thread-A 는 1초에 한 번씩 "A" 를 출력한다.
 * - Thread-B 는 0.5 초에 한 번씩 "B"를 출력한다.
 * 이 프로그램은 강제 종료할 때 까지 계속 실행된다.
 */
public class StartTest4Main {
    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            while (true) {
                log("A");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Thread-A");

        Thread threadB = new Thread(() -> {
            while (true) {
                log("B");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Thread-B");

        threadA.start();
        threadB.start();
    }

    // 다른 방법
    public void mainV2() {
        Thread threadA = new Thread(new PrintWork("A", 1000), "Thread-A");
        Thread threadB = new Thread(new PrintWork("B", 500), "Thread-B");

        threadA.start();
        threadB.start();
    }

    static class PrintWork implements Runnable {
        private final String content;
        private final int sleepMs;

        public PrintWork(String content, int sleepMs) {
            this.content = content;
            this.sleepMs = sleepMs;
        }

        @Override
        public void run() {
            while (true) {
                log(content);
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
