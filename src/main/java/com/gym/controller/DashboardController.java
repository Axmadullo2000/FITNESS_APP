package com.gym.controller;

import com.gym.service.ClientService;
import com.gym.service.LockerService;
import com.gym.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final VisitService visitService;
    private final LockerService lockerService;
    private final ClientService clientService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("currentVisits", visitService.findCurrentVisits());
        model.addAttribute("visitorsCount", visitService.findCurrentVisits().size());
        model.addAttribute("freeLockers", lockerService.countFree());
        model.addAttribute("occupiedLockers", lockerService.countOccupied());
        model.addAttribute("totalClients", clientService.search(null).size());
        return "dashboard";
    }
}
