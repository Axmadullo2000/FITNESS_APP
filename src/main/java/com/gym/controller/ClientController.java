package com.gym.controller;


import com.gym.entity.Client;
import com.gym.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final MemberShipService membershipService;
    private final VisitService visitService;
    private final CardService cardService;
    private final TariffService tariffService;

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("clients", clientService.search(q));

        return "clients/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }

    @PostMapping("/new")
    public String processForm(@ModelAttribute Client client,
                              RedirectAttributes ra) {
        try {
            clientService.save(client);
            ra.addFlashAttribute("success", "Клиент успешно зарегистрирован");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/clients/new";
        }
        return "redirect:/clients";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id);
        model.addAttribute("client", client);
        model.addAttribute("memberships", membershipService.findByClient(client));
        log.info("Memberships found: {}", membershipService.findByClient(client));
        model.addAttribute("visits", visitService.findVisitHistory(client));
        model.addAttribute("activeCard", cardService.findActiveCardByClient(client));
        model.addAttribute("tariffs", tariffService.findAll());
        return "clients/detail";
    }

    @PostMapping("/{id}/membership")
    public String createMembership(@PathVariable Long id,
                                   @RequestParam Long tariffId,
                                   RedirectAttributes ra) {
        try {
            Client client = clientService.findById(id);
            membershipService.createMembership(client, tariffId);
            ra.addFlashAttribute("success", "Абонемент успешно оформлен");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/clients/" + id;
    }

}
