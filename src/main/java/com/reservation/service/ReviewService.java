package com.reservation.service;

import com.reservation.domain.MemberEntity;
import com.reservation.domain.ReviewEntity;
import com.reservation.domain.StoreEntity;
import com.reservation.dto.review.ReviewDto;
import com.reservation.exception.ApplicationException;
import com.reservation.repository.MemberRepository;
import com.reservation.repository.ReservationRepository;
import com.reservation.repository.ReviewRepository;
import com.reservation.repository.StoreRepository;
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

    /**
     * 리뷰 생성
     * @param reviewDto 리뷰 정보를 담고 있는 DTO
     * @return 생성된 리뷰의 DTO
     * @throws ApplicationException 회원 또는 가게를 찾을 수 없는 경우
     */
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

    /**
     * 특정 매장 모든 리뷰 조회
     *
     * @param storeId 조회할 가게의 ID
     * @return 가게의 리뷰 목록 (ReviewDto 리스트)
     */
    public List<ReviewDto> getReviewByStore(Long storeId) {
        List<ReviewEntity> reviews = reviewRepository.findByStoreId(storeId);

        return reviews.stream()
                .map(ReviewDto::fromEntity)
                .toList();
    }

    /**
     * 리뷰 수정
     * @param reviewDto 수정할 리뷰 정보를 담고 있는 DTO
     * @return 수정된 리뷰의 DTO
     * @throws ApplicationException 리뷰를 찾을 수 없는 경우
     */
    public ReviewDto updateReview(ReviewDto reviewDto) {
        ReviewEntity review = reviewRepository.findById(reviewDto.getReviewId())
                .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND));

        reviewDto.setContent(reviewDto.getContent());
        reviewDto.setRating(reviewDto.getRating());

        return ReviewDto.fromEntity(reviewRepository.save(review));
    }

    /**
     * 리뷰 삭제
     * @param reviewId 삭제할 리뷰의 ID
     * @throws ApplicationException 리뷰를 찾을 수 없는 경우
     */
    public void deleteReview(Long reviewId) {
        reviewRepository.delete(reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND)));
    }
}
