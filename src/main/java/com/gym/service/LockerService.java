package com.gym.service;

import com.gym.entity.Locker;
import com.gym.entity.types.Gender;

import java.util.List;

public interface LockerService {
    List<Locker> findAll();
    Locker findById(Long id);
    List<Locker> findFreeByGender(Gender gender);
    List<Locker> findByGender(Gender gender);
    long countFree();
    long countOccupied();
    long countFreeByGender(Gender gender);
    long countOccupiedByGender(Gender gender);
}
