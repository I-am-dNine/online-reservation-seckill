package com.d9.seckill.controller;

import com.d9.seckill.entity.Event;
import com.d9.seckill.service.EventService;
import com.d9.seckill.service.SeckillService;
import com.d9.seckill.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventPageController {

    private final EventService eventService;
    private final SeckillService seckillService;
    private final UserRepository userRepository;

    @GetMapping("/events")
    public String listEvents(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        
        List<Event> events = eventService.findAll();
        model.addAttribute("events", events);
        return "events";
    }

    @PostMapping("/seckill/{id}")
    public String doSeckill(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String username = auth.getName();
        
        // Verify user exists
        if (!userRepository.existsByUsername(username)) {
            return "redirect:/login";
        }
        
        String result = seckillService.doSeckill(username, id);
        List<Event> events = eventService.findAll();

        model.addAttribute("events", events);
        model.addAttribute("message", result);
        return "events";
    }
}
