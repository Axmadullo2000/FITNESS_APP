package com.gym.service.impl;

import com.gym.entity.Card;
import com.gym.entity.Client;
import com.gym.repository.CardRepository;
import com.gym.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public Card findActiveCardByClient(Client client) {
        return cardRepository.findActiveCardByClient(client).orElse(null);
    }
}
