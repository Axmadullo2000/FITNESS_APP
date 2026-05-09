package com.gym.service.impl;

import com.gym.entity.Client;
import com.gym.repository.ClientRepository;
import com.gym.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> search(String q) {
        if (q == null || q.isBlank()) return findAll();
        return clientRepository.search(q);
    }

    @Override
    public void save(Client client) {
        if (client.getId() == null && clientRepository.existsByPhone(client.getPhone())) {
            throw new RuntimeException("Клиент с таким номером телефона уже существует");
        }
        clientRepository.save(client);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));
    }

    @Override
    public Optional<Client> findByToken(String token) {
        return clientRepository.findByToken(token);
    }

    @Override
    public Client findClientByPhone(String phone) {
        return clientRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));
    }

    @Override
    public boolean existsByPhone(String phone) {
        return clientRepository.existsByPhone(phone);
    }
}
