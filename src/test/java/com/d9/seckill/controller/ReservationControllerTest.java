package com.d9.seckill.controller;

import com.d9.seckill.config.TestSecurityConfig;
import com.d9.seckill.dto.ReservationDTO;
import com.d9.seckill.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(ReservationController.class)
@ActiveProfiles("test")  
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReserveSuccessfully() throws Exception {
        Mockito.when(reservationService.reserve(1L)).thenReturn("预约成功");

        mockMvc.perform(post("/api/reservation/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("预约成功"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnBadRequestWhenAlreadyReserved() throws Exception {
        Mockito.when(reservationService.reserve(1L))
                .thenThrow(new RuntimeException("你已经预约过该项目"));

        mockMvc.perform(post("/api/reservation/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("你已经预约过该项目"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnMyReservations() throws Exception {
        List<ReservationDTO> mockList = List.of(
                new ReservationDTO("活动 A", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)),
                new ReservationDTO("活动 B", LocalDateTime.now(), LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4))
        );

        Mockito.when(reservationService.getMyReservations()).thenReturn(mockList);

        mockMvc.perform(get("/api/reservation/me"))
                .andExpect(status().isOk());
        // ⚠️ 若需验证返回内容可加 JSON 判断（使用 Jackson + jsonPath）
    }
}
