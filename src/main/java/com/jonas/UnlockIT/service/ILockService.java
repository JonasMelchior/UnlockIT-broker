package com.jonas.UnlockIT.service;

import com.jonas.UnlockIT.entity.locks.Lock;

import java.util.List;
import java.util.Optional;

public interface ILockService {
    List<Lock> findAll();
    Lock findById(String id);
    void save(Lock lock);
    void delete(Lock lock);
}
