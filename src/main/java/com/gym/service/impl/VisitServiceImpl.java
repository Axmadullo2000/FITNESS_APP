package com.gym.service.impl;

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
import com.gym.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final CardRepository cardRepository;
    private final LockerRepository lockerRepository;

    @Override
    @Transactional
    public void checkIn(Client client, Long lockerId) {
        visitRepository.findByClientAndStatus(client, VisitStatus.OPEN).ifPresent(v -> {
            throw new RuntimeException("Клиент уже находится в зале");
        });

        Card card = cardRepository.findActiveCardByClient(client)
                .orElseThrow(() -> new RuntimeException("У клиента нет активного абонемента"));

        if (card.getExpiresAt().isBefore(LocalDateTime.now())) {
            card.setStatus(CardStatus.EXPIRED);
            cardRepository.save(card);
            throw new RuntimeException("Срок действия карты истёк");
        }

        if (card.getRemainingSessions() <= 0) {
            card.setStatus(CardStatus.EXPIRED);
            cardRepository.save(card);
            throw new RuntimeException("Все сеансы использованы");
        }

        Locker locker = lockerRepository.findById(lockerId)
                .orElseThrow(() -> new RuntimeException("Шкафчик не найден"));

        if (locker.getStatus() != LockerStatus.FREE) {
            throw new RuntimeException("Выбранный шкафчик занят");
        }

        if (locker.getGender() != client.getGender()) {
            throw new RuntimeException("Шкафчик не соответствует полу клиента");
        }

        card.setRemainingSessions(card.getRemainingSessions() - 1);
        card.setStatus(CardStatus.IN_USE);
        cardRepository.save(card);

        locker.setStatus(LockerStatus.OCCUPIED);
        lockerRepository.save(locker);

        Visit visit = Visit.builder()
                .client(client)
                .card(card)
                .locker(locker)
                .status(VisitStatus.OPEN)
                .build();
        visitRepository.save(visit);
    }

    @Override
    @Transactional
    public void checkInWithAutoLocker(Client client) {
        Locker locker = lockerRepository
                .findFirstByStatusAndGender(LockerStatus.FREE, client.getGender())
                .orElseThrow(() -> new RuntimeException(
                        "Нет свободных шкафчиков для " +
                        (client.getGender().name().equals("MALE") ? "мужчин" : "женщин")));
        checkIn(client, locker.getId());
    }

    @Override
    @Transactional
    public void checkOut(Client client) {
        Visit visit = visitRepository.findByClientAndStatus(client, VisitStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Клиент не находится в зале"));

        visit.setCheckOut(LocalDateTime.now());
        visit.setStatus(VisitStatus.CLOSED);
        visitRepository.save(visit);

        Locker locker = visit.getLocker();
        locker.setStatus(LockerStatus.FREE);
        lockerRepository.save(locker);

        Card card = visit.getCard();
        card.setStatus(card.getRemainingSessions() > 0 ? CardStatus.ACTIVE : CardStatus.EXPIRED);
        cardRepository.save(card);
    }

    @Override
    public Optional<Visit> findOpenVisit(Client client) {
        return visitRepository.findByClientAndStatus(client, VisitStatus.OPEN);
    }

    @Override
    public List<Visit> findCurrentVisits() {
        return visitRepository.findAllOpenWithDetails(VisitStatus.OPEN);
    }

    @Override
    public List<Visit> findVisitHistory(Client client) {
        return visitRepository.findByClientOrderByCheckInDesc(client);
    }
}
