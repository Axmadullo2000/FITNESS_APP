package com.gym.repository;

import com.gym.entity.Tariff;
import com.gym.entity.types.TariffType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TariffRepository extends JpaRepository<Tariff, Long> {
    Optional<Tariff> findByType(TariffType type);
}