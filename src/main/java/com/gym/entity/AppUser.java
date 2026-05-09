package com.gym.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Phone number for clients, "admin" for admin accounts */
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /** "ROLE_ADMIN" or "ROLE_CLIENT" */
    @Column(nullable = false)
    private String role;

    /** Non-null only for ROLE_CLIENT users */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}
