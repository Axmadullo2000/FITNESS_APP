package com.gym.controller;

import com.gym.entity.types.Gender;
import com.gym.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("loginError", "Неверный номер телефона или пароль");
        if (logout != null) model.addAttribute("logoutMsg", "Вы вышли из системы");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String phone,
                           @RequestParam String password,
                           @RequestParam String gender,
                           RedirectAttributes ra) {
        try {
            authService.register(firstName, lastName, phone, password, Gender.valueOf(gender));
            ra.addFlashAttribute("success", "Регистрация успешна! Войдите в систему.");
            return "redirect:/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
