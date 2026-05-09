package com.gym.service;

import com.gym.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAll();
    List<Client> search(String q);
    void save(Client client);
    Client findById(Long id);
    Optional<Client> findByToken(String token);
    /** Finds client by phone (used after login to load current user's client). */
    Client findClientByPhone(String phone);
    boolean existsByPhone(String phone);
}
