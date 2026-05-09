package com.gym.repository;

import com.gym.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByPhone(String phone);
    Optional<Client> findByPhone(String phone);
    Optional<Client> findByToken(String token);

    @Query("""
        SELECT c FROM Client c
        WHERE
            LOWER(c.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :q, '%'))
            OR c.phone LIKE CONCAT('%', :q, '%')
        """)
    List<Client> search(@Param("q") String query);

}
