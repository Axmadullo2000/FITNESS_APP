package com.gym.repository;

import com.gym.entity.Locker;
import com.gym.entity.types.Gender;
import com.gym.entity.types.LockerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LockerRepository extends JpaRepository<Locker, Long> {

    Optional<Locker> findFirstByStatus(LockerStatus status);
    Long countByStatus(LockerStatus status);

    List<Locker> findByGenderAndStatusOrderByLockerNumber(Gender gender, LockerStatus status);
    List<Locker> findByGenderOrderByLockerNumber(Gender gender);
    Long countByGenderAndStatus(Gender gender, LockerStatus status);
}
