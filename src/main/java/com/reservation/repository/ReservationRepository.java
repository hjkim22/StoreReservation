package com.reservation.repository;

import com.reservation.domain.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByMemberId(Long memberId);
    List<ReservationEntity> findByStoreId(Long storeId);
    boolean existsByStoreIdAndReservationTime(Long storeId, LocalDateTime reservationTime);
}
