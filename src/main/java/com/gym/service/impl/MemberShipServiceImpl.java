package com.gym.service.impl;

import com.gym.entity.Card;
import com.gym.entity.Client;
import com.gym.entity.MemberShip;
import com.gym.entity.Tariff;
import com.gym.entity.types.CardStatus;
import com.gym.entity.types.MemberShipStatus;
import com.gym.repository.CardRepository;
import com.gym.repository.MemberShipRepository;
import com.gym.repository.TariffRepository;
import com.gym.service.MemberShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberShipServiceImpl implements MemberShipService {

    private final MemberShipRepository memberShipRepository;
    private final TariffRepository tariffRepository;
    private final CardRepository cardRepository;

    @Override
    public List<MemberShip> findByClient(Client client) {
        return memberShipRepository.findAllByClientOrderByCreatedAtDesc(client);
    }

    @Override
    public MemberShip createMembership(Client client, Long tariffId) {
        Tariff tariff = tariffRepository.findById(tariffId)
                .orElseThrow(() -> new RuntimeException("Тариф не найден"));

        cardRepository.findActiveCardByClient(client).ifPresent(card -> {
            throw new RuntimeException("У клиента уже есть активный абонемент (" +
                    card.getRemainingSessions() + " сеансов осталось)");
        });

        MemberShip membership = memberShipRepository.save(MemberShip.builder()
                .client(client)
                .tariff(tariff)
                .status(MemberShipStatus.ACTIVE)
                .build());

        cardRepository.save(Card.builder()
                .membership(membership)
                .totalSessions(tariff.getTotalSessions())
                .remainingSessions(tariff.getTotalSessions())
                .status(CardStatus.ACTIVE)
                .build());

        return membership;
    }
}
