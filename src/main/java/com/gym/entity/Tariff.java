package com.gym.entity;

import com.gym.entity.types.TariffType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    public TariffType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 24 сеанса — каждый день (24 раза в месяц)
     * 12 сеансов — через день (12 раз в месяц)
     */
    @Column(nullable = false)
    private Integer totalSessions;

    @Column(nullable = false)
    private Boolean hasTrainer;

    public String getDisplayName() {
        return type.getDisplayName();
    }
}
