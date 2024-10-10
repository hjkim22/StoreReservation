package com.reservation.repository;

import com.reservation.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByStoreId(Long storeId);
    List<ReviewEntity> findByMemberId(Long memberId);
}
