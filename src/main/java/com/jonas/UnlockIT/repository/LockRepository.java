package com.jonas.UnlockIT.repository;

import com.jonas.UnlockIT.entity.locks.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LockRepository extends JpaRepository<Lock, Long> {
}
