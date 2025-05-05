package com.d9.seckill.controller;

import com.d9.seckill.entity.Event;
import com.d9.seckill.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Event create(@RequestBody Event event) {
        return eventService.create(event);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Event update(@PathVariable Long id, @RequestBody Event event) {
        return eventService.update(id, event);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        eventService.delete(id);
    }

    @GetMapping
    public List<Event> getAll() {
        return eventService.findAll();
    }

    @PostMapping("/preload/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String preload(@PathVariable Long id) {
        eventService.preloadStockToRedis(id);
        return "库存已加载到 Redis";
    }

}
