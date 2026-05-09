package com.gym.service;

import com.gym.entity.Card;
import com.gym.entity.Client;

public interface CardService {
    Card findActiveCardByClient(Client client);
}
