package com.jonas.UnlockIT.controller;

import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


public class LockResponseFuture extends CompletableFuture<String> {
    private final String lockId;

    public LockResponseFuture(String lockId) {
        this.lockId = lockId;
    }

    public String getLockId() {
        return lockId;
    }
}
