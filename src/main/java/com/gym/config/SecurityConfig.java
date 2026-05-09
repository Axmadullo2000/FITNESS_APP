package com.gym.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        // Redirect admin → /  |  client → /my/cabinet
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            response.sendRedirect(isAdmin ? "/" : "/my/cabinet");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // ── Static resources ────────────────────────────────────────
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                // ── Public auth pages ────────────────────────────────────────
                .requestMatchers("/login", "/register").permitAll()

                // ── QR + scanner pages (used from outside / tablet) ──────────
                .requestMatchers("/scan", "/scan/lookup/**", "/scan/checkin/**", "/scan/checkout/**").permitAll()
                .requestMatchers("/my/**", "/clients/*/qr").permitAll()

                // ── Admin panel ──────────────────────────────────────────────
                .requestMatchers("/", "/dashboard/**",
                        "/clients/**", "/visits/**", "/lockers/**",
                        "/api/**").hasRole("ADMIN")

                // ── Client self-checkin ──────────────────────────────────────
                .requestMatchers("/self-checkin").hasRole("CLIENT")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(successHandler())
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // Disable CSRF only for the scanner POST endpoint (called by JS on tablet)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/scan/lookup/**", "/scan/checkin/**", "/scan/checkout/**", "/api/**")
            );

        return http.build();
    }
}
