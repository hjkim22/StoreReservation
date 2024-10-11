package com.reservation.service;

import com.reservation.domain.MemberEntity;
import com.reservation.domain.ReservationEntity;
import com.reservation.domain.StoreEntity;
import com.reservation.dto.reservation.ReservationDto;
import com.reservation.dto.reservation.ReservationUpdateDto;
import com.reservation.exception.ApplicationException;
import com.reservation.repository.MemberRepository;
import com.reservation.repository.ReservationRepository;
import com.reservation.repository.StoreRepository;
import com.reservation.type.ArrivalStatus;
import com.reservation.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.reservation.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    /**
     * 예약 등록
     * @param reservationDto 예약 정보를 담고 있는 DTO
     * @param memberId       회원 ID
     * @param storeId        매장 ID
     * @return 생성된 예약 정보를 담고 있는 DTO
     */
    public ReservationDto createReservationEntity(ReservationDto reservationDto, Long memberId, Long storeId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        LocalDateTime reservationDateTime = reservationDto.getReservationDateTime();
        LocalDateTime now = LocalDateTime.now();

        if (reservationDateTime.isBefore(now)) {
            throw new ApplicationException(RESERVATION_TIME_EXCEEDED);
        }

        if (now.plusMinutes(10).isAfter(reservationDateTime)) {
            throw new ApplicationException(CHECK_IT_10_MINUTES_BEFORE_THE_RESERVATION_TIME);
        }

        if (reservationRepository.existsByStoreIdAndReservationTime(storeId, reservationDateTime)) {
            throw new ApplicationException(ALREADY_RESERVED);
        }

        return createReservationEntity(reservationDto, member, store);
    }

    /**
     * 예약 ID 예약 정보 조회
     * @param reservationId 조회할 예약의 ID
     * @return 조회된 예약 정보를 담고 있는 DTO
     * @throws ApplicationException 예약이 존재하지 않을 경우
     */
    public ReservationDto getReservationById(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(RESERVATION_NOT_FOUND));
        return ReservationDto.fromEntity(reservation);
    }

    /**
     * 특정 매장 모든 예약 정보 조회
     * @param storeId 조회할 매장의 ID
     * @return 매장에서의 예약 정보를 담고 있는 DTO 리스트
     */
    public List<ReservationDto> getReservationsByStoreId(Long storeId) {
        List<ReservationEntity> reservations = reservationRepository.findByStoreId(storeId);
        return reservations.stream()
                .map(ReservationDto::fromEntity)
                .toList();
    }

    /**
     * 특정 사용자 모든 예약 정보 조회
     * @param memberId 조회할 사용자의 ID
     * @return 사용자의 예약 정보를 담고 있는 DTO 리스트
     */
    public List<ReservationDto> getReservationsByMemberId(Long memberId) {
        List<ReservationEntity> reservations = reservationRepository.findByMemberId(memberId);
        return reservations.stream()
                .map(ReservationDto::fromEntity)
                .toList();
    }

    /**
     * 예약 정보 수정
     * @param reservationId 수정할 예약의 ID
     * @param updateDto     수정할 정보를 담고 있는 DTO
     * @return 수정된 예약 정보를 담고 있는 DTO
     */
    public ReservationDto updateReservation(Long reservationId, ReservationUpdateDto updateDto) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(RESERVATION_NOT_FOUND));

        if (reservation.getReservationStatus() != ReservationStatus.STANDBY) {
            throw new ApplicationException(RESERVATION_STATUS_CHECK_ERROR);
        }

        reservation.setReservationDate(updateDto.getReservationDate());
        reservation.setReservationTime(updateDto.getReservationTime());

        return ReservationDto.fromEntity(reservationRepository.save(reservation));
    }

    /**
     * 예약 삭제
     * @param reservationId 삭제할 예약의 ID
     */
    public void deleteReservation(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(RESERVATION_NOT_FOUND));

        reservationRepository.delete(reservation);
    }

    /**
     * 예약 엔티티 생성
     * @param reservationDto 예약 정보를 담고 있는 DTO
     * @param member         회원 엔티티
     * @param store          매장 엔티티
     * @return 생성된 예약 정보를 담고 있는 DTO
     */
    private ReservationDto createReservationEntity(ReservationDto reservationDto, MemberEntity member, StoreEntity store) {
        return ReservationDto.fromEntity(
                reservationRepository.save(
                        ReservationEntity.builder()
                                .member(member)
                                .store(store)
                                .reservationStatus(ReservationStatus.STANDBY)
                                .arrivalStatus(ArrivalStatus.READY)
                                .reservationDate(reservationDto.getReservationDate())
                                .reservationTime(reservationDto.getReservationTime())
                                .build()));
    }
}