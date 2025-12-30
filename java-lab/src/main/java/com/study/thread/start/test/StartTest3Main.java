package com.study.thread.start.test;

import static com.study.util.MyLogger.log;

/**
 * 문제 3. Runnable 익명 클래스 구현
 * 방금 작성한 문제 2를 익명클래스로 구현하라
 */
public class StartTest3Main {
    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
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
        };

        Thread thread = new Thread(runnable, "counter");
        thread.start();
    }
}
