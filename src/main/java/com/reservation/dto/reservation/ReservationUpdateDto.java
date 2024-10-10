package com.reservation.dto.reservation;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdateDto {

    private Long id;

    @NotNull(message = "예약 날짜는 필수입니다.")
    @Future(message = "예약 날짜는 현재 날짜 이후여야 합니다.")
    private LocalDate reservationDate;

    @NotNull(message = "예약 시간은 필수입니다.")
    private LocalTime reservationTime;
}