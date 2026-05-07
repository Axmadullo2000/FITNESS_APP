package com.gym.entity;


import com.gym.entity.types.MemberShipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MemberShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MemberShipStatus status = MemberShipStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "membership", cascade = CascadeType.ALL)
    private Card card;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
