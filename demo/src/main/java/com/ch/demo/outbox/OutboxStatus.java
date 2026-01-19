package com.ch.demo.outbox;

public enum OutboxStatus {
    PENDING,
    PROCESSED,
    FAILED
}
