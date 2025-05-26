package com.d9.seckill.service;

import com.d9.seckill.dto.EventAdminDTO;
import com.d9.seckill.entity.Event;
import com.d9.seckill.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testCreateEvent_ShouldSetAvailableSlotsAndSave() {
        Event input = Event.builder().title("Test").totalSlots(100).build();
        Event saved = Event.builder().title("Test").totalSlots(100).availableSlots(100).build();

        when(eventRepository.save(any(Event.class))).thenReturn(saved);

        Event result = eventService.create(input);

        assertEquals(100, result.getAvailableSlots());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testFindAll_ShouldReturnListOfEvents() {
        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.findAll();

        assertEquals(2, result.size());
        verify(eventRepository).findAll();
    }

    @Test
    void testUpdate_ShouldModifyAndSave() {
        Event original = Event.builder().id(1L).title("Old").build();
        Event updated = Event.builder().title("New").description("Desc").totalSlots(100).availableSlots(80)
                .startTime(LocalDateTime.now()).endTime(LocalDateTime.now().plusHours(1)).build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(original));
        when(eventRepository.save(any(Event.class))).thenReturn(original);

        Event result = eventService.update(1L, updated);

        assertEquals("New", result.getTitle());
        verify(eventRepository).save(original);
    }

    @Test
    void testDelete_ShouldCallRepository() {
        doNothing().when(eventRepository).deleteById(1L);
        eventService.delete(1L);
        verify(eventRepository).deleteById(1L);
    }

    @Test
    void testPreloadStockToRedis_ShouldSetRedisKey() {
        Event event = Event.builder().id(1L).availableSlots(50).build();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        eventService.preloadStockToRedis(1L);

        verify(redisTemplate.opsForValue(), times(1)).set("event:stock:1", 50);
    }

    @Test
    void testGetAllForAdmin_ShouldConvertToDTO() {
        Event e = Event.builder().id(1L).title("E1").availableSlots(20).totalSlots(50)
                .startTime(LocalDateTime.now()).endTime(LocalDateTime.now().plusHours(2)).build();
        when(eventRepository.findAll()).thenReturn(List.of(e));

        List<EventAdminDTO> dtos = eventService.getAllForAdmin();
        assertEquals(1, dtos.size());
        assertEquals("E1", dtos.get(0).getTitle());
    }
}

