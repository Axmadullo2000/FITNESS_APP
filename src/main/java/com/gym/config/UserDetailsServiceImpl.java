package com.gym.config;

import com.gym.entity.AppUser;
import com.gym.repository.AppUserRepository;
import com.gym.util.PhoneUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Normalize phone before lookup so "+998 90 123" and "998901234567" both work
        String normalized = PhoneUtils.normalize(username);
        AppUser user = appUserRepository.findByUsername(normalized)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + normalized));

        return new User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
