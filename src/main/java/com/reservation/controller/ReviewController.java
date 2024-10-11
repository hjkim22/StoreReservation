package com.reservation.controller;

import com.reservation.dto.review.ReviewDeleteDto;
import com.reservation.dto.review.ReviewDto;
import com.reservation.dto.review.ReviewRegisterDto;
import com.reservation.dto.review.ReviewUpdateDto;
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
     * @param reviewRegisterDto 생성할 리뷰 정보
     * @return 생성된 리뷰의 DTO
     */
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewRegisterDto reviewRegisterDto) {
        ReviewDto createdReview = reviewService.createReview(reviewRegisterDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    /**
     * 매장명으로 리뷰 조회
     * @param storeName 조회할 매장의 이름
     * @return 해당 매장의 리뷰 목록
     */
    @GetMapping("/store/{storeName}")
    public ResponseEntity<List<ReviewDto>> getReviewsByStoreName(@PathVariable String storeName) {
        List<ReviewDto> reviews = reviewService.getReviewByStoreName(storeName);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * 유저명으로 리뷰 조회
     * @param username 조회할 유저의 이름
     * @return 해당 유저의 리뷰 목록
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUsername(@PathVariable String username) {
        List<ReviewDto> reviews = reviewService.getReviewByUsername(username);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * 리뷰 수정
     * @param reviewUpdateDto 수정할 리뷰 정보
     * @return 수정된 리뷰의 DTO
     */
    @PutMapping
    public ResponseEntity<ReviewDto> updateReview(@Valid @RequestBody ReviewUpdateDto reviewUpdateDto) {
        ReviewDto updatedReview = reviewService.updateReview(reviewUpdateDto);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    /**
     * 리뷰 삭제
     * @param reviewId 삭제할 리뷰의 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(new ReviewDeleteDto(reviewId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
