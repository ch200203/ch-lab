package com.ch.demo.order.service;

import org.springframework.cache.CacheManager;

import java.util.Objects;

public class CacheServcie {

    private final CacheManager cacheManager;


    public CacheServcie(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void evict(String cacheName) {
        Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
    }

    public void evictAll() {
        cacheManager.getCacheNames().clear();
    }
}
