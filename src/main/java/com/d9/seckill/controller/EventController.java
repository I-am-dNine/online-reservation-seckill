package com.d9.seckill.controller;

import com.d9.seckill.entity.Event;
import com.d9.seckill.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@Tag(name = "活动管理", description = "活动CRUD和库存预加载接口")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "创建活动", description = "管理员创建新的活动")
    public Event create(@RequestBody Event event) {
        return eventService.create(event);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新活动", description = "管理员更新活动信息")
    public Event update(@PathVariable Long id, @RequestBody Event event) {
        return eventService.update(id, event);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除活动", description = "管理员删除指定活动")
    public void delete(@PathVariable Long id) {
        eventService.delete(id);
    }

    @GetMapping
    @Operation(summary = "获取活动列表", description = "获取所有活动信息")
    public List<Event> getAll() {
        return eventService.findAll();
    }

    @PostMapping("/preload/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "预加载库存", description = "将活动库存预加载到Redis中")
    public String preload(@PathVariable Long id) {
        eventService.preloadStockToRedis(id);
        return "库存已加载到 Redis";
    }

}
