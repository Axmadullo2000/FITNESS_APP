package com.gym.repository;

import com.gym.entity.Card;
import com.gym.entity.Client;
import com.gym.entity.types.CardStatus;
import com.gym.entity.types.LockerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("""
        SELECT c FROM Card c
        WHERE c.membership.client = :client
        AND (c.status = 'ACTIVE' OR c.status = 'IN_USE')
""")
    Optional<Card> findActiveCardByClient(Client client);

}
