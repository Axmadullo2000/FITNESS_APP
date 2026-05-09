package com.gym.service.impl;

import com.gym.entity.AppUser;
import com.gym.entity.Client;
import com.gym.entity.types.Gender;
import com.gym.repository.AppUserRepository;
import com.gym.repository.ClientRepository;
import com.gym.service.AuthService;
import com.gym.util.PhoneUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(String firstName, String lastName, String phone,
                         String password, Gender gender) {
        String normalizedPhone = PhoneUtils.normalize(phone);

        if (appUserRepository.existsByUsername(normalizedPhone)) {
            throw new RuntimeException("Этот номер телефона уже зарегистрирован");
        }

        Client client = clientRepository.save(Client.builder()
                .firstName(firstName.trim())
                .lastName(lastName.trim())
                .phone(normalizedPhone)
                .gender(gender)
                .build());

        appUserRepository.save(AppUser.builder()
                .username(normalizedPhone)
                .password(passwordEncoder.encode(password))
                .role("ROLE_CLIENT")
                .client(client)
                .build());
    }
}
