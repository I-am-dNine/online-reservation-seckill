package com.d9.seckill.service;

import com.d9.seckill.entity.*;
import com.d9.seckill.repository.*;
import com.d9.seckill.dto.ReservationAdminDTO;
import com.d9.seckill.dto.ReservationDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public String reserve(Long eventId) {
        // 1. 从 SecurityContext 拿当前用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 2. 查找活动
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));

        // 3. 是否重复预约
        if (reservationRepository.existsByUserAndEvent(user, event)) {
            throw new RuntimeException("你已经预约过该项目");
        }

        // 4. 检查名额
        if (event.getAvailableSlots() <= 0) {
            throw new RuntimeException("该项目名额已满");
        }

        // 5. 扣名额并保存预约记录
        event.setAvailableSlots(event.getAvailableSlots() - 1);
        eventRepository.save(event); // 更新名额

        Reservation reservation = Reservation.builder()
                .user(user)
                .event(event)
                .reservedAt(LocalDateTime.now())
                .build();
        reservationRepository.save(reservation);

        return "预约成功";
    }

    public List<ReservationDTO> getMyReservations() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    
        List<Reservation> list = reservationRepository.findAllByUser(user);
    
        return list.stream().map(r -> new ReservationDTO(
                r.getEvent().getTitle(),
                r.getReservedAt(),
                r.getEvent().getStartTime(),
                r.getEvent().getEndTime()
        )).collect(Collectors.toList());
    }

    public List<ReservationAdminDTO> getAll(Optional<Long> eventIdOpt) {
        List<Reservation> list;
    
        if (eventIdOpt.isPresent()) {
            Event event = eventRepository.findById(eventIdOpt.get())
                .orElseThrow(() -> new RuntimeException("活动不存在"));
            list = reservationRepository.findAllByEvent(event);
        } else {
            list = reservationRepository.findAll();
        }
    
        return list.stream().map(r -> new ReservationAdminDTO(
            r.getId(),
            r.getUser().getUsername(),
            r.getEvent().getTitle(),
            r.getReservedAt()
        )).collect(Collectors.toList());
    }
    
}
