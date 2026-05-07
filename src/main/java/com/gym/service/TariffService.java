package com.gym.service;


import com.gym.entity.Tariff;
import com.gym.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepository tariffRepository;

    public List<Tariff> findAll() {
        return tariffRepository.findAll();
    }

}
