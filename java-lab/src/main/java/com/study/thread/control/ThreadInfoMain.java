package com.study.thread.control;

import com.study.thread.start.HelloRunnable;

import static com.study.util.MyLogger.log;

public class ThreadInfoMain {

    public static void main(String[] args) {
        // Main 스레드
        Thread thread = Thread.currentThread();
        log("mainThread = " + thread);
        log("mainThread.threadId() = " + thread.threadId());
        log("mainThread.getName() = " + thread.getName());
        log("mainThread.getPriority = " + thread.getPriority()); // default = 5 , 높을 수록 더 많이 실행됨(근데 운영체제가 알아서함)
        log("mainThread.getThreadGroup() = " + thread.getThreadGroup());
        log("mainThread.getState() = " + thread.getState()); // 실행될 수 있는 상태


        Thread myThread = new Thread(new HelloRunnable());
        log("myThread = " + myThread);
        log("myThread.threadId() = " + myThread.threadId());
        log("myThread.getName() = " + myThread.getName());
        log("myThread.getPriority = " + myThread.getPriority());
        log("myThread.getThreadGroup() = " + myThread.getThreadGroup());
        log("myThread.getState() = " + myThread.getState());
    }
}
