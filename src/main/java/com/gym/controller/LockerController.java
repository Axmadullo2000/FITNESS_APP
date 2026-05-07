package com.gym.controller;

import com.gym.entity.types.Gender;
import com.gym.service.LockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lockers")
@RequiredArgsConstructor
public class LockerController {

    private final LockerService lockerService;

    @GetMapping
    public String grid(Model model) {
        model.addAttribute("maleLockers", lockerService.findByGender(Gender.MALE));
        model.addAttribute("femaleLockers", lockerService.findByGender(Gender.FEMALE));
        model.addAttribute("maleFreeCount", lockerService.countFreeByGender(Gender.MALE));
        model.addAttribute("maleOccupiedCount", lockerService.countOccupiedByGender(Gender.MALE));
        model.addAttribute("femaleFreeCount", lockerService.countFreeByGender(Gender.FEMALE));
        model.addAttribute("femaleOccupiedCount", lockerService.countOccupiedByGender(Gender.FEMALE));
        return "lockers/grid";
    }
}
