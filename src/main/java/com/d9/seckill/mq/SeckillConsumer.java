package com.d9.seckill.mq;

import com.d9.seckill.config.RabbitMQConfig;
import com.d9.seckill.dto.SeckillMessage;
import com.d9.seckill.entity.*;
import com.d9.seckill.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SeckillConsumer {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ReservationRepository reservationRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    @Transactional
    public void consume(SeckillMessage message) {
        String username = message.getUsername();
        Long eventId = message.getEventId();

        // Early return if user doesn't exist
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            System.err.println("User not found: " + username);
            return;
        }

        Event event = eventRepository.findById(eventId).orElseThrow();

        // 幂等性检查（防止重复预约）
        if (reservationRepository.existsByUserAndEvent(user, event)) {
            return;
        }

        // 再次库存确认（数据库层）
        if (event.getAvailableSlots() <= 0) {
            return;
        }

        event.setAvailableSlots(event.getAvailableSlots() - 1);
        eventRepository.save(event);

        Reservation reservation = Reservation.builder()
                .user(user)
                .event(event)
                .reservedAt(LocalDateTime.now())
                .build();
        reservationRepository.save(reservation);
    }
}