package com.reservation.service;

import com.reservation.domain.MemberEntity;
import com.reservation.domain.ReviewEntity;
import com.reservation.domain.StoreEntity;
import com.reservation.dto.review.ReviewDeleteDto;
import com.reservation.dto.review.ReviewDto;
import com.reservation.dto.review.ReviewRegisterDto;
import com.reservation.dto.review.ReviewUpdateDto;
import com.reservation.exception.ApplicationException;
import com.reservation.repository.MemberRepository;
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

    /**
     * 리뷰 생성
     * @param reviewRegisterDto 리뷰 정보를 담고 있는 DTO
     * @return 생성된 리뷰의 DTO
     * @throws ApplicationException 회원 또는 매장을 찾을 수 없는 경우
     */
    public ReviewDto createReview(ReviewRegisterDto reviewRegisterDto) {
        MemberEntity member = memberRepository.findById(reviewRegisterDto.getMemberId())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        StoreEntity store = storeRepository.findById(reviewRegisterDto.getStoreId())
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        return ReviewDto.fromEntity(
                reviewRepository.save(
                        ReviewEntity.builder()
                                .content(reviewRegisterDto.getContent())
                                .rating(reviewRegisterDto.getRating())
                                .member(member)
                                .store(store)
                                .build()
                )
        );
    }

    /**
     * 매장명으로 리뷰 조회
     * @param storeName 조회할 매장의 이름
     * @return 해당 매장의 리뷰 목록 (ReviewDto 리스트)
     */
    public List<ReviewDto> getReviewByStoreName(String storeName) {
        List<ReviewEntity> reviews = reviewRepository.findByStoreId(storeRepository.findByStoreName(storeName)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND)).getId());

        return reviews.stream()
                .map(ReviewDto::fromEntity)
                .toList();
    }

    /**
     * 유저명으로 리뷰 조회
     * @param username 조회할 유저의 이름
     * @return 해당 유저의 리뷰 목록 (ReviewDto 리스트)
     */
    public List<ReviewDto> getReviewByUsername(String username) {
        MemberEntity member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        List<ReviewEntity> reviews = reviewRepository.findByMemberId(member.getId());

        return reviews.stream()
                .map(ReviewDto::fromEntity)
                .toList();
    }

    /**
     * 리뷰 수정
     * @param reviewUpdateDto 수정할 리뷰 정보를 담고 있는 DTO
     * @return 수정된 리뷰의 DTO
     * @throws ApplicationException 리뷰를 찾을 수 없는 경우
     */
    public ReviewDto updateReview(ReviewUpdateDto reviewUpdateDto) {
        ReviewEntity review = reviewRepository.findById(reviewUpdateDto.getId())
                .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND));

        review.setContent(reviewUpdateDto.getContent());
        review.setRating(reviewUpdateDto.getRating());

        return ReviewDto.fromEntity(reviewRepository.save(review));
    }

    /**
     * 리뷰 삭제
     * @param reviewDeleteDto 삭제할 리뷰의 ID
     * @throws ApplicationException 리뷰를 찾을 수 없는 경우
     */
    public void deleteReview(ReviewDeleteDto reviewDeleteDto) {
        reviewRepository.delete(reviewRepository.findById(reviewDeleteDto.getId())
                .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND)));
    }
}
