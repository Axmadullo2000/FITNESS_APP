package com.gym.service;

import com.gym.entity.Client;
import com.gym.entity.MemberShip;

import java.util.List;

public interface MemberShipService {
    List<MemberShip> findByClient(Client client);
    MemberShip createMembership(Client client, Long tariffId);
}
