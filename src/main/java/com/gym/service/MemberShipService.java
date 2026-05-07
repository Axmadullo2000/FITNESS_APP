package com.gym.service;


import com.gym.entity.Card;
import com.gym.entity.Client;
import com.gym.entity.MemberShip;
import com.gym.entity.Tariff;
import com.gym.entity.types.CardStatus;
import com.gym.entity.types.MemberShipStatus;
import com.gym.repository.CardRepository;
import com.gym.repository.MemberShipRepository;
import com.gym.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberShipService {
    private final MemberShipRepository memberShipRepository;
    private final TariffRepository tariffRepository;
    private final CardRepository cardRepository;

    public List<MemberShip> findByClient(Client client) {
        return memberShipRepository.findAllByClientOrderByCreatedAtDesc(client);
    }

    public MemberShip createMembership(Client client, Long tariffId) {

        Tariff tariff = tariffRepository.findById(tariffId)
                .orElseThrow(() -> new RuntimeException("Тариф не найден"));

        // Проверяем: нет ли уже активной карты у клиента
        cardRepository.findActiveCardByClient(client).ifPresent(card -> {
            throw new RuntimeException("У клиента уже есть активный абонемент (" +
                    card.getRemainingSessions() + " сеансов осталось)");
        });

        MemberShip membership = MemberShip.builder()
                .client(client)
                .tariff(tariff)
                .status(MemberShipStatus.ACTIVE)
                .build();
        membership = memberShipRepository.save(membership);

        // Создаём карту
        Card card = Card.builder()
                .membership(membership)
                .totalSessions(tariff.getTotalSessions())
                .remainingSessions(tariff.getTotalSessions())
                .status(CardStatus.ACTIVE)
                .build();
        cardRepository.save(card);

        return membership;
    }
}
