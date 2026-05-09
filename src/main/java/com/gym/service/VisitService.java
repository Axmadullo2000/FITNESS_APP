package com.gym.service;

import com.gym.entity.Client;
import com.gym.entity.Visit;

import java.util.List;
import java.util.Optional;

public interface VisitService {
    void checkIn(Client client, Long lockerId);
    void checkInWithAutoLocker(Client client);
    void checkOut(Client client);
    /** Returns the active (OPEN) visit if client is currently inside the gym. */
    Optional<Visit> findOpenVisit(Client client);
    List<Visit> findCurrentVisits();
    List<Visit> findVisitHistory(Client client);
}
