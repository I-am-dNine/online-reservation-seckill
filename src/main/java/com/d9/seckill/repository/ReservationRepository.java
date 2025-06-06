package com.d9.seckill.repository;

import com.d9.seckill.entity.Reservation;
import com.d9.seckill.entity.User;
import com.d9.seckill.entity.Event;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByUserAndEvent(User user, Event event);

    List<Reservation> findAllByUser(User user);

    List<Reservation> findAllByEvent(Event event);

    Page<Reservation> findAllByEvent(Event event, Pageable pageable);

}

