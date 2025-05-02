package com.d9.seckill.service;

import com.d9.seckill.entity.Event;
import com.d9.seckill.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

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
}
