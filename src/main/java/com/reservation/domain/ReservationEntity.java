package com.reservation.domain;

import com.reservation.type.ArrivalStatus;
import com.reservation.type.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Enumerated(EnumType.STRING)
    private ArrivalStatus arrivalStatus;

    @NotNull(message = "예약 날짜는 필수입니다.")
    @Future(message = "예약 날짜는 현재 날짜 이후여야 합니다.")
    private LocalDate reservationDate;

    @NotNull(message = "예약 시간은 필수입니다.")
    private LocalTime reservationTime;
}