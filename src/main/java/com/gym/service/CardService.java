package com.gym.service;


import com.gym.entity.Card;
import com.gym.entity.Client;
import com.gym.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public Card findActiveCardByClient(Client client) {
        return cardRepository.findActiveCardByClient(client)
                .orElse(null);
    }

}
