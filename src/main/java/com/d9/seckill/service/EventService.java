package com.d9.seckill.service;

import com.d9.seckill.entity.Event;
import com.d9.seckill.dto.EventAdminDTO;
import com.d9.seckill.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Event create(Event event) {
        event.setAvailableSlots(event.getTotalSlots()); // 初始时剩余名额 = 总名额
        return eventRepository.save(event);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event update(Long id, Event updated) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setTitle(updated.getTitle());
        event.setDescription(updated.getDescription());
        event.setTotalSlots(updated.getTotalSlots());
        event.setAvailableSlots(updated.getAvailableSlots());
        event.setStartTime(updated.getStartTime());
        event.setEndTime(updated.getEndTime());

        return eventRepository.save(event);
    }

    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    public void preloadStockToRedis(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
    
        String key = "event:stock:" + eventId;
        redisTemplate.opsForValue().set(key, event.getAvailableSlots());
    }

    public List<EventAdminDTO> getAllForAdmin() {
        return eventRepository.findAll().stream().map(e ->
            new EventAdminDTO(
                e.getId(),
                e.getTitle(),
                e.getAvailableSlots(),
                e.getTotalSlots(),
                e.getStartTime(),
                e.getEndTime()
            )
        ).collect(Collectors.toList());
    }
    
}
