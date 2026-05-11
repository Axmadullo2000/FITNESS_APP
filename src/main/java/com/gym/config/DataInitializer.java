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
        if (!appUserRepository.existsByUsername("+998998266611")) {
            appUserRepository.save(AppUser.builder()
                    .username("+998998266611")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .build());
            log.info("Admin user created: admin / admin123");
        }
    }

    private void initTariffs() {
        log.info("Syncing tariffs...");
        upsertTariff(TariffType.EVERY_DAY_WITH_TRAINER,    new BigDecimal("1500000"), 24, true);
        upsertTariff(TariffType.EVERY_DAY_NO_TRAINER,      new BigDecimal("800000"),  24, false);
        upsertTariff(TariffType.EVEY_OTHER_DAY_WITH_TRAINER, new BigDecimal("1000000"), 12, true);
        upsertTariff(TariffType.EVEY_OTHER_DAY_NO_TRAINER,   new BigDecimal("400000"),  12, false);
        log.info("Tariffs synced");
    }

    private void upsertTariff(TariffType type, BigDecimal price, int sessions, boolean hasTrainer) {
        Tariff tariff = tariffRepository.findByType(type).orElseGet(() ->
                Tariff.builder().type(type).build());
        tariff.setPrice(price);
        tariff.setTotalSessions(sessions);
        tariff.setHasTrainer(hasTrainer);
        tariffRepository.save(tariff);
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
