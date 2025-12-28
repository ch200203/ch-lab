package com.study.thread.start;

public class DemonThreadMain {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");
        DaemonThread daemonThread = new DaemonThread();
        daemonThread.setDaemon(false); // 데몬 스레드 여부 (default는 false)
        daemonThread.start();

        System.out.println(Thread.currentThread().getName() + ": main() end");

    }

    static class DaemonThread extends Thread {


        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": run() start");
            System.out.println("is Daemon : " + this.isDaemon());

            try {
                Thread.sleep(10000); // 10초간 실행
            } catch (InterruptedException e) {
                // `run()` 메서드 안에서 `Thread.sleep()` 를 호출할 때 체크 예외인
                // `InterruptedException` 을 밖으로 던질 수 없고 반드시 잡아야 한다.
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread().getName() + ": run() end");
        }
    }
}
