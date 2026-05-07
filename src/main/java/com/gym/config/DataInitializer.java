package com.gym.config;

import com.gym.entity.Locker;
import com.gym.entity.Tariff;
import com.gym.entity.types.Gender;
import com.gym.entity.types.LockerStatus;
import com.gym.entity.types.TariffType;
import com.gym.repository.LockerRepository;
import com.gym.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TariffRepository tariffRepository;
    private final LockerRepository lockerRepository;

    @Override
    public void run(String... args) {
        initTariffs();
        initLockers();
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

            // Create 50 male lockers (1-50)
            for (int i = 1; i <= 50; i++) {
                lockerRepository.save(Locker.builder()
                        .lockerNumber(i)
                        .gender(Gender.MALE)
                        .status(LockerStatus.FREE)
                        .build());
            }

            // Create 50 female lockers (51-100)
            for (int i = 51; i <= 100; i++) {
                lockerRepository.save(Locker.builder()
                        .lockerNumber(i)
                        .gender(Gender.FEMALE)
                        .status(LockerStatus.FREE)
                        .build());
            }

            log.info("Lockers initialized: 100 lockers created (50 male, 50 female)");
        }
    }
}
