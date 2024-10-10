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

import java.util.List;

import static com.reservation.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    public ReviewDto createReview(ReviewDto reviewDto) {
        MemberEntity member = memberRepository.findById(reviewDto.getMemberId())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        StoreEntity store = storeRepository.findById(reviewDto.getStoreId())
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));


        return ReviewDto.fromEntity(
                reviewRepository.save(
                        ReviewEntity.builder()
                                .content(reviewDto.getContent())
                                .rating(reviewDto.getRating())
                                .member(member)
                                .store(store)
                                .build()
                )
        );
    }

    public List<ReviewDto> getReviewByStore(Long storeId) {
        List<ReviewEntity> reviews = reviewRepository.findByStoreId(storeId);

        return reviews.stream()
                .map(ReviewDto::fromEntity)
                .toList();
    }

    public ReviewDto updateReview(ReviewDto reviewDto) {
        ReviewEntity review = reviewRepository.findById(reviewDto.getMemberId())
                .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND));

        reviewDto.setContent(reviewDto.getContent());
        reviewDto.setRating(reviewDto.getRating());

        return ReviewDto.fromEntity(reviewRepository.save(review));
    }
}
