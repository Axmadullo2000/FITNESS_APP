package com.gym.controller;

import com.gym.entity.Client;
import com.gym.entity.Locker;
import com.gym.service.ClientService;
import com.gym.service.LockerService;
import com.gym.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final ClientService clientService;
    private final LockerService lockerService;

    @GetMapping
    public String checkinPage(@RequestParam(required = false) String q, Model model) {
        List<Client> clients = clientService.search(q);
        model.addAttribute("clients", clients);
        model.addAttribute("currentVisits", visitService.findCurrentVisits());

        // Build map of client ID to available lockers for their gender
        Map<Long, List<Locker>> clientLockers = new HashMap<>();
        for (Client client : clients) {
            if (client.getGender() != null) {
                clientLockers.put(client.getId(), lockerService.findFreeByGender(client.getGender()));
            }
        }
        model.addAttribute("clientLockers", clientLockers);

        return "visits/checkin";
    }

    @PostMapping("/checkin/{id}")
    public String checkIn(@PathVariable Long id,
                          @RequestParam Long lockerId,
                          RedirectAttributes ra) {
        try {
            Client client = clientService.findById(id);
            visitService.checkIn(client, lockerId);
            ra.addFlashAttribute("success", "Клиент " + client.getFullName() + " вошёл в зал");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/visits";
    }

    @PostMapping("/checkout/{id}")
    public String checkOut(@PathVariable Long id, RedirectAttributes ra) {
        try {
            Client client = clientService.findById(id);
            visitService.checkOut(client);
            ra.addFlashAttribute("success", "Клиент " + client.getFullName() + " покинул зал");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/visits";
    }
}
