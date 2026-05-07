package com.gym.service;

import com.gym.entity.Client;
import com.gym.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public List<Client> search(String q) {
        if (q == null || q.isBlank()) return findAll();
        return clientRepository.search(q);
    }

    public void save(Client client) {
        if (client.getId() == null && clientRepository.existsByPhone(client.getPhone())) {
            throw new RuntimeException("Клиент с таким номером телефона уже существует");
        }
        clientRepository.save(client);
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));
    }
}
