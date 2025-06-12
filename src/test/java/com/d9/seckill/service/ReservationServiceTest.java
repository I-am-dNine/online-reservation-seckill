package com.d9.seckill.service;

import java.time.LocalDateTime;
import java.util.Collections;

import com.d9.seckill.entity.Event;
import com.d9.seckill.entity.User;
import com.d9.seckill.repository.EventRepository;
import com.d9.seckill.repository.ReservationRepository;
import com.d9.seckill.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setup() {
        reservationRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
        SecurityContextHolder.clearContext();
    }

    private void loginAs(User user) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, Collections.emptyList());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void shouldReserveSuccessfully() {
        // Arrange
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass");
        user.setRole("USER");
        user = userRepository.save(user);
        loginAs(user);

        Event event = new Event();
        event.setTitle("Test Event");
        event.setTotalSlots(10);
        event.setAvailableSlots(10); // 重点字段
        event.setStartTime(LocalDateTime.now().plusDays(1));
        event = eventRepository.save(event);

        // Act
        String result = reservationService.reserve(event.getId());

        // Assert
        assertThat(result).isEqualTo("预约成功");
        assertThat(reservationRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldThrowWhenDuplicateReservation() {
        User user = new User();
        user.setUsername("repeatUser");
        user.setPassword("pass");
        user.setRole("USER");
        user = userRepository.save(user);
        loginAs(user);

        Event event = new Event();
        event.setTitle("Duplicate Test");
        event.setTotalSlots(5);
        event.setAvailableSlots(5);
        event.setStartTime(LocalDateTime.now().plusDays(1));
        event = eventRepository.save(event);

        reservationService.reserve(event.getId());

        // Act + Assert
        Long eventId = event.getId();
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> reservationService.reserve(eventId));
        assertThat(ex.getMessage()).isEqualTo("你已经预约过该项目");
    }

    @Test
    void shouldThrowWhenNoAvailableSlots() {
        User user = new User();
        user.setUsername("noSlotUser");
        user.setPassword("pass");
        user.setRole("USER");
        user = userRepository.save(user);
        loginAs(user);

        Event event = new Event();
        event.setTitle("Full Event");
        event.setTotalSlots(1);
        event.setAvailableSlots(0); // 名额已满
        event.setStartTime(LocalDateTime.now().plusDays(1));
        event = eventRepository.save(event);

        Long eventId = event.getId();
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> reservationService.reserve(eventId));
        assertThat(ex.getMessage()).isEqualTo("该项目名额已满");
    }
}
