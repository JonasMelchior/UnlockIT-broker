package com.jonas.UnlockIT.service;

import com.jonas.UnlockIT.entity.locks.Lock;
import com.jonas.UnlockIT.repository.LockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Configurable
public class LockService implements ILockService{

    @Autowired
    private LockRepository repository;

    @Override
    public List<Lock> findAll() {
        return (List<Lock>) repository.findAll();
    }

    @Override
    public Lock findById(String id) {
        for (Lock lock : findAll()) {
            if (lock.getMCUIdentifier().equals(id)) {
                return lock;
            }
        }
        return null;
    }

    @Override
    public void save(Lock lock) {
        repository.save(lock);
    }

    @Override
    public void delete(Lock lock) {
        repository.delete(lock);
    }
}
