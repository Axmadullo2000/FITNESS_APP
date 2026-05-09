package com.gym.service.impl;

import com.gym.entity.Locker;
import com.gym.entity.types.Gender;
import com.gym.entity.types.LockerStatus;
import com.gym.repository.LockerRepository;
import com.gym.service.LockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LockerServiceImpl implements LockerService {

    private final LockerRepository lockerRepository;

    @Override
    public List<Locker> findAll() {
        return lockerRepository.findAll();
    }

    @Override
    public Locker findById(Long id) {
        return lockerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Шкафчик не найден"));
    }

    @Override
    public List<Locker> findFreeByGender(Gender gender) {
        return lockerRepository.findByGenderAndStatusOrderByLockerNumber(gender, LockerStatus.FREE);
    }

    @Override
    public List<Locker> findByGender(Gender gender) {
        return lockerRepository.findByGenderOrderByLockerNumber(gender);
    }

    @Override
    public long countFree() {
        return lockerRepository.countByStatus(LockerStatus.FREE);
    }

    @Override
    public long countOccupied() {
        return lockerRepository.countByStatus(LockerStatus.OCCUPIED);
    }

    @Override
    public long countFreeByGender(Gender gender) {
        return lockerRepository.countByGenderAndStatus(gender, LockerStatus.FREE);
    }

    @Override
    public long countOccupiedByGender(Gender gender) {
        return lockerRepository.countByGenderAndStatus(gender, LockerStatus.OCCUPIED);
    }
}
