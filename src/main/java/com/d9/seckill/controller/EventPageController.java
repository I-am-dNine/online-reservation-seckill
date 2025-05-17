package com.d9.seckill.controller;

import com.d9.seckill.entity.Event;
import com.d9.seckill.service.EventService;
import com.d9.seckill.service.SeckillService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/events")
    public String listEvents(Model model) {
        List<Event> events = eventService.findAll();
        model.addAttribute("events", events);
        return "events";
    }

    @PostMapping("/seckill/{id}")
    public String doSeckill(@PathVariable Long id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String result = seckillService.doSeckill(username, id);
        List<Event> events = eventService.findAll();

        model.addAttribute("events", events);
        model.addAttribute("message", result);
        return "events";
    }
}
