package com.gym.service.impl;

import com.gym.entity.Tariff;
import com.gym.repository.TariffRepository;
import com.gym.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;

    @Override
    public List<Tariff> findAll() {
        return tariffRepository.findAll();
    }
}
