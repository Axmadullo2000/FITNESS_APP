package com.gym.controller;

import com.gym.entity.Client;
import com.gym.service.CardService;
import com.gym.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class ClientCabinetController {

    private final ClientService clientService;
    private final CardService cardService;

    @GetMapping("/cabinet")
    public String cabinet(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Client client = clientService.findClientByPhone(userDetails.getUsername());
        model.addAttribute("client", client);
        model.addAttribute("card", cardService.findActiveCardByClient(client));
        return "my/cabinet";
    }
}
