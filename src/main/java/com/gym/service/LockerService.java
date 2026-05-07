package com.gym.service;

import com.gym.entity.Locker;
import com.gym.entity.types.Gender;
import com.gym.entity.types.LockerStatus;
import com.gym.repository.LockerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LockerService {

    private final LockerRepository lockerRepository;

    public List<Locker> findAll() {
        return lockerRepository.findAll();
    }

    public Locker findById(Long id) {
        return lockerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Шкафчик не найден"));
    }

    public List<Locker> findFreeByGender(Gender gender) {
        return lockerRepository.findByGenderAndStatusOrderByLockerNumber(gender, LockerStatus.FREE);
    }

    public List<Locker> findByGender(Gender gender) {
        return lockerRepository.findByGenderOrderByLockerNumber(gender);
    }

    public long countFree() {
        return lockerRepository.countByStatus(LockerStatus.FREE);
    }

    public long countOccupied() {
        return lockerRepository.countByStatus(LockerStatus.OCCUPIED);
    }

    public long countFreeByGender(Gender gender) {
        return lockerRepository.countByGenderAndStatus(gender, LockerStatus.FREE);
    }

    public long countOccupiedByGender(Gender gender) {
        return lockerRepository.countByGenderAndStatus(gender, LockerStatus.OCCUPIED);
    }
}
