package com.gym.config;

import com.gym.entity.AppUser;
import com.gym.entity.Locker;
import com.gym.entity.Tariff;
import com.gym.entity.types.Gender;
import com.gym.entity.types.LockerStatus;
import com.gym.entity.types.TariffType;
import com.gym.repository.AppUserRepository;
import com.gym.repository.LockerRepository;
import com.gym.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TariffRepository tariffRepository;
    private final LockerRepository lockerRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdmin();
        initTariffs();
        initLockers();
    }

    private void initAdmin() {
        if (!appUserRepository.existsByUsername("admin")) {
            appUserRepository.save(AppUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .build());
            log.info("Admin user created: admin / admin123");
        }
    }

    private void initTariffs() {
        if (tariffRepository.count() == 0) {
            log.info("Initializing tariffs...");

            tariffRepository.save(Tariff.builder()
                    .type(TariffType.EVERY_DAY_WITH_TRAINER)
                    .price(new BigDecimal("500000"))
                    .totalSessions(24)
                    .hasTrainer(true)
                    .build());

            tariffRepository.save(Tariff.builder()
                    .type(TariffType.EVERY_DAY_NO_TRAINER)
                    .price(new BigDecimal("350000"))
                    .totalSessions(24)
                    .hasTrainer(false)
                    .build());

            tariffRepository.save(Tariff.builder()
                    .type(TariffType.EVEY_OTHER_DAY_WITH_TRAINER)
                    .price(new BigDecimal("300000"))
                    .totalSessions(12)
                    .hasTrainer(true)
                    .build());

            tariffRepository.save(Tariff.builder()
                    .type(TariffType.EVEY_OTHER_DAY_NO_TRAINER)
                    .price(new BigDecimal("200000"))
                    .totalSessions(12)
                    .hasTrainer(false)
                    .build());

            log.info("Tariffs initialized: 4 tariffs created");
        }
    }

    private void initLockers() {
        if (lockerRepository.count() == 0) {
            log.info("Initializing lockers...");

            lockerRepository.saveAll(
                Stream.of(Gender.MALE, Gender.FEMALE)
                    .flatMap(gender -> IntStream.rangeClosed(1, 50)
                        .mapToObj(n -> Locker.builder()
                                .lockerNumber(n)
                                .gender(gender)
                                .status(LockerStatus.FREE)
                                .build()))
                    .toList()
            );

            log.info("Lockers initialized: 100 lockers created (male 1–50, female 1–50)");
        }
    }
}
