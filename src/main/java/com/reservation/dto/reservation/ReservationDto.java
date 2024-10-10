package com.reservation.dto.reservation;

import com.reservation.domain.ReservationEntity;
import com.reservation.type.ArrivalStatus;
import com.reservation.type.ReservationStatus;
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
public class ReservationDto {
    private Long id;
    private Long memberId;
    private Long storeId;
    private ReservationStatus reservationStatus;
    private ArrivalStatus arrivalStatus;

    @NotNull(message = "예약 날짜는 필수입니다.")
    @Future(message = "예약 날짜는 현재 날짜 이후여야 합니다.")
    private LocalDate reservationDate;

    @NotNull(message = "예약 시간은 필수입니다.")
    private LocalTime reservationTime;

    public static ReservationDto fromEntity(ReservationEntity reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .memberId(reservation.getMember().getId())
                .storeId(reservation.getStore().getId())
                .reservationStatus(reservation.getReservationStatus())
                .arrivalStatus(reservation.getArrivalStatus())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .build();
    }
}
