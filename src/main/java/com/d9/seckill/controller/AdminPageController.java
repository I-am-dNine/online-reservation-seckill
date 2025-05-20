package com.d9.seckill.controller;

import com.d9.seckill.dto.PagedResult;
import com.d9.seckill.dto.ReservationAdminDTO;
import com.d9.seckill.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public String listReservations(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model,
                                   HttpSession session) {
        // 这里只简单处理：确认已登录
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        PagedResult<ReservationAdminDTO> paged = reservationService.getAllPaged(Optional.empty(), page, size);
        model.addAttribute("paged", paged);
        return "admin_reservations";
    }
}