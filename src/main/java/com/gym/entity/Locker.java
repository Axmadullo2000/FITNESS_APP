package com.gym.entity;


import com.gym.entity.types.Gender;
import com.gym.entity.types.LockerStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "lockers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"locker_number", "gender"})
})
public class Locker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer lockerNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private LockerStatus status = LockerStatus.FREE;

    public boolean isFree() {
        return status == LockerStatus.FREE;
    }

}
