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
     *
     * @param reservationDto 예약 정보를 담고 있는 DTO
     * @param memberId       회원 ID
     * @param storeId        매장 ID
     * @return 생성된 예약 정보를 담고 있는 DTO
     */
    public ReservationDto createReservation(ReservationDto reservationDto, Long memberId, Long storeId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        return ReservationDto.fromEntity(
                reservationRepository.save(
                        ReservationEntity.builder()
                                .member(member)
                                .store(store)
                                .reservationStatus(ReservationStatus.STANDBY) // 기본 상태를 대기 상태로 설정
                                .arrivalStatus(ArrivalStatus.READY) // 초기 상태를 대기 상태로 설정
                                .reservationDate(reservationDto.getReservationDate())
                                .reservationTime(reservationDto.getReservationTime())
                                .build()
                )
        );
    }

    /**
     * 특정 사용자 예약 목록 조회
     * @param memberId 사용자의 ID
     * @return 사용자의 예약 목록 DTO 리스트
     */
    public List<ReservationDto> getReservationsByMemberId(Long memberId) {
        List<ReservationEntity> reservations = reservationRepository.findByMemberId(memberId);
        return reservations.stream()
                .map(ReservationDto::fromEntity)
                .toList();
    }

    /**
     * 예약 ID 단일 예약 조회
     * @param reservationId 예약의 ID
     * @return 조회된 예약 정보를 담고 있는 DTO
     */
    public ReservationDto getReservationById(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(RESERVATION_NOT_FOUND));

        return ReservationDto.fromEntity(reservation);
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

    /*
      TODO
      findByStoreId 사용 메서드
      도착 상태 메서드
      승인 상태 메서드
      모든 예약 조회
     */
}