package com.reservation.service;

import com.reservation.domain.MemberEntity;
import com.reservation.domain.ReservationEntity;
import com.reservation.domain.ReviewEntity;
import com.reservation.domain.StoreEntity;
import com.reservation.dto.review.ReviewDto;
import com.reservation.exception.ApplicationException;
import com.reservation.repository.MemberRepository;
import com.reservation.repository.ReservationRepository;
import com.reservation.repository.ReviewRepository;
import com.reservation.repository.StoreRepository;
import com.reservation.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    public ReviewDto createReview(ReviewDto reviewDto) {
        MemberEntity member = memberRepository.findById(reviewDto.getMemberId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        StoreEntity store = storeRepository.findById(reviewDto.getStoreId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.STORE_NOT_FOUND));

        ReviewEntity review = reviewRepository.save(ReviewEntity)
    }
}
