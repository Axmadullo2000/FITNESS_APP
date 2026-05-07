package com.gym.repository;

import com.gym.entity.Client;
import com.gym.entity.MemberShip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberShipRepository extends JpaRepository<MemberShip, Long> {

    List<MemberShip> findAllByClientOrderByCreatedAtDesc(Client client);

}
