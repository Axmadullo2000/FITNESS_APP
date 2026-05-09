package com.gym.controller;

import com.gym.entity.Client;
import com.gym.entity.Locker;
import com.gym.entity.Visit;
import com.gym.service.ClientService;
import com.gym.service.LockerService;
import com.gym.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ScanController {

    private final ClientService clientService;
    private final VisitService  visitService;
    private final LockerService lockerService;

    @GetMapping("/scan")
    public String scanPage() {
        return "scan/scanner";
    }

    /**
     * Called immediately after QR is scanned.
     * Returns client info and either:
     *   - inGym=true  + current locker number  → scanner will show checkout confirmation
     *   - inGym=false + list of free lockers    → scanner will show locker selection
     */
    @GetMapping("/scan/lookup/{token}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> lookup(@PathVariable String token) {
        return clientService.findByToken(token)
                .map(client -> {
                    Optional<Visit> openVisit = visitService.findOpenVisit(client);

                    if (openVisit.isPresent()) {
                        // Client is already inside — prepare checkout data
                        Visit visit = openVisit.get();
                        return ResponseEntity.ok(Map.<String, Object>of(
                                "found",        true,
                                "inGym",        true,
                                "name",         client.getFullName(),
                                "lockerNumber", visit.getLocker().getLockerNumber()));
                    }

                    // Client is outside — show free lockers for their gender
                    List<Locker> freeLockers = lockerService.findFreeByGender(client.getGender());
                    List<Map<String, Object>> lockerList = freeLockers.stream()
                            .map(l -> Map.<String, Object>of(
                                    "id",     l.getId(),
                                    "number", l.getLockerNumber()))
                            .toList();

                    return ResponseEntity.ok(Map.<String, Object>of(
                            "found",   true,
                            "inGym",   false,
                            "name",    client.getFullName(),
                            "gender",  client.getGender().name(),
                            "lockers", lockerList));
                })
                .orElseGet(() -> ResponseEntity.ok(
                        Map.of("found", false, "message", "Клиент не найден")));
    }

    /**
     * Check-in: receptionist picks a specific locker after QR scan.
     */
    @PostMapping("/scan/checkin/{token}/{lockerId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkin(@PathVariable String token,
                                                       @PathVariable Long lockerId) {
        return clientService.findByToken(token)
                .map(client -> {
                    try {
                        visitService.checkIn(client, lockerId);
                        int lockerNumber = lockerService.findById(lockerId).getLockerNumber();
                        return ResponseEntity.ok(Map.<String, Object>of(
                                "success",      true,
                                "name",         client.getFullName(),
                                "lockerNumber", lockerNumber,
                                "message",      "Добро пожаловать!"));
                    } catch (Exception e) {
                        return ResponseEntity.ok(Map.<String, Object>of(
                                "success", false,
                                "name",    client.getFullName(),
                                "message", e.getMessage()));
                    }
                })
                .orElseGet(() -> ResponseEntity.ok(
                        Map.of("success", false, "message", "Клиент не найден")));
    }

    /**
     * Check-out: client scans QR on the way out.
     */
    @PostMapping("/scan/checkout/{token}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkout(@PathVariable String token) {
        return clientService.findByToken(token)
                .map(client -> {
                    try {
                        // Get locker number before checkout clears it
                        Optional<Visit> visit = visitService.findOpenVisit(client);
                        int lockerNumber = visit.map(v -> v.getLocker().getLockerNumber()).orElse(0);

                        visitService.checkOut(client);
                        return ResponseEntity.ok(Map.<String, Object>of(
                                "success",      true,
                                "name",         client.getFullName(),
                                "lockerNumber", lockerNumber,
                                "message",      "До свидания!"));
                    } catch (Exception e) {
                        return ResponseEntity.ok(Map.<String, Object>of(
                                "success", false,
                                "name",    client.getFullName(),
                                "message", e.getMessage()));
                    }
                })
                .orElseGet(() -> ResponseEntity.ok(
                        Map.of("success", false, "message", "Клиент не найден")));
    }
}
