package com.reservation.controller;

import com.reservation.dto.review.ReviewDto;
import com.reservation.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 생성
     * @param reviewDto 생성할 리뷰 정보
     * @return 생성된 리뷰의 DTO
     */
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        ReviewDto createdReview = reviewService.createReview(reviewDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    /**
     * 특정 매장의 모든 리뷰 조회
     * @param storeId 조회할 매장의 ID
     * @return 매장의 모든 리뷰 리스트
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByStore(@PathVariable Long storeId) {
        List<ReviewDto> reviews = reviewService.getReviewByStore(storeId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * 리뷰 수정
     * @param reviewDto 수정할 리뷰 정보
     * @return 수정된 리뷰의 DTO
     */
    @PutMapping
    public ResponseEntity<ReviewDto> updateReview(@Valid @RequestBody ReviewDto reviewDto) {
        ReviewDto updatedReview = reviewService.updateReview(reviewDto);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    /**
     * 리뷰 삭제
     * @param reviewId 삭제할 리뷰의 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
