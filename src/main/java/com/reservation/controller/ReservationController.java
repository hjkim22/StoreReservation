package com.reservation.controller;

import com.reservation.dto.reservation.ReservationDto;
import com.reservation.dto.reservation.ReservationUpdateDto;
import com.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약 등록
     * @param reservationDto 예약 정보
     * @param memberId       회원 ID
     * @param storeId        매장 ID
     * @return 생성된 예약 정보
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReservationDto> createReservation(
            @RequestBody ReservationDto reservationDto,
            @RequestParam Long memberId,
            @RequestParam Long storeId) {
        ReservationDto createdReservation = reservationService.createReservation(reservationDto, memberId, storeId);
        return ResponseEntity.ok(createdReservation);
    }

    /**
     * 특정 회원 예약 조회
     * @param memberId 회원 ID
     * @return 해당 회원의 예약 목록
     */
    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER')")
    // USER, MANAGER 모두 접근 가능
    public ResponseEntity<List<ReservationDto>> getReservationsByMemberId(@PathVariable Long memberId) {
        List<ReservationDto> reservations = reservationService.getReservationsByMemberId(memberId);
        return ResponseEntity.ok(reservations);
    }

    /**
     * 예약 ID로 조회
     * @param reservationId 예약 ID
     * @return 예약 정보
     */
    @GetMapping("/{reservationId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER')")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long reservationId) {
        ReservationDto reservation = reservationService.getReservationById(reservationId);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 예약 수정
     * @param reservationId 예약 ID
     * @param updateDto     수정할 예약 정보
     * @return 수정된 예약 정보
     */
    @PutMapping("/{reservationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReservationDto> updateReservation(
            @PathVariable Long reservationId,
            @RequestBody ReservationUpdateDto updateDto) {
        ReservationDto updatedReservation = reservationService.updateReservation(reservationId, updateDto);
        return ResponseEntity.ok(updatedReservation);
    }

    /**
     * 예약 삭제
     * @param reservationId 예약 ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
