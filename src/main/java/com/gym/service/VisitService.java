package com.gym.service;

import com.gym.entity.Card;
import com.gym.entity.Client;
import com.gym.entity.Locker;
import com.gym.entity.Visit;
import com.gym.entity.types.CardStatus;
import com.gym.entity.types.LockerStatus;
import com.gym.entity.types.VisitStatus;
import com.gym.repository.CardRepository;
import com.gym.repository.LockerRepository;
import com.gym.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final CardRepository cardRepository;
    private final LockerRepository lockerRepository;

    @Transactional
    public void checkIn(Client client, Long lockerId) {
        // 1. Already in gym?
        visitRepository.findByClientAndStatus(client, VisitStatus.OPEN).ifPresent(v -> {
            throw new RuntimeException("Клиент уже находится в зале");
        });

        // 2. Active card?
        Card card = cardRepository.findActiveCardByClient(client)
                .orElseThrow(() -> new RuntimeException("У клиента нет активного абонемента"));

        // 3. Expired by date?
        if (card.getExpiresAt().isBefore(LocalDateTime.now())) {
            card.setStatus(CardStatus.EXPIRED);
            cardRepository.save(card);
            throw new RuntimeException("Срок действия карты истёк");
        }

        // 4. Sessions left?
        if (card.getRemainingSessions() <= 0) {
            card.setStatus(CardStatus.EXPIRED);
            cardRepository.save(card);
            throw new RuntimeException("Все сеансы использованы");
        }

        // 5. Get selected locker and validate
        Locker locker = lockerRepository.findById(lockerId)
                .orElseThrow(() -> new RuntimeException("Шкафчик не найден"));

        if (locker.getStatus() != LockerStatus.FREE) {
            throw new RuntimeException("Выбранный шкафчик занят");
        }

        if (locker.getGender() != client.getGender()) {
            throw new RuntimeException("Шкафчик не соответствует полу клиента");
        }

        // 6. Decrement sessions
        card.setRemainingSessions(card.getRemainingSessions() - 1);
        card.setStatus(CardStatus.IN_USE);
        cardRepository.save(card);

        // 7. Mark locker as occupied
        locker.setStatus(LockerStatus.OCCUPIED);
        lockerRepository.save(locker);

        // 8. Save visit
        Visit visit = Visit.builder()
                .client(client)
                .card(card)
                .locker(locker)
                .status(VisitStatus.OPEN)
                .build();
        visitRepository.save(visit);
    }

    @Transactional
    public void checkOut(Client client) {
        // Find open visit
        Visit visit = visitRepository.findByClientAndStatus(client, VisitStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Клиент не находится в зале"));

        // Close visit
        visit.setCheckOut(LocalDateTime.now());
        visit.setStatus(VisitStatus.CLOSED);
        visitRepository.save(visit);

        // Free locker
        Locker locker = visit.getLocker();
        locker.setStatus(LockerStatus.FREE);
        lockerRepository.save(locker);

        // Update card status
        Card card = visit.getCard();
        if (card.getRemainingSessions() > 0) {
            card.setStatus(CardStatus.ACTIVE);
        } else {
            card.setStatus(CardStatus.EXPIRED);
        }
        cardRepository.save(card);
    }

    public List<Visit> findCurrentVisits() {
        return visitRepository.findAllOpenWithDetails(VisitStatus.OPEN);
    }

    public List<Visit> findVisitHistory(Client client) {
        return visitRepository.findByClientOrderByCheckInDesc(client);
    }
}
