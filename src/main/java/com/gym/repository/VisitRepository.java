package com.gym.repository;

import com.gym.entity.Client;
import com.gym.entity.Visit;
import com.gym.entity.types.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("""
            SELECT v FROM Visit v
            JOIN FETCH v.client JOIN FETCH v.locker
            WHERE v.status = :status
        """)
    List<Visit> findAllOpenWithDetails(@Param("status") VisitStatus status);

    List<Visit> findByClientOrderByCheckInDesc(Client client);

    Optional<Visit> findByClientAndStatus(Client client, VisitStatus status);
}
