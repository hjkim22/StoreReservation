package com.reservation.service;

import com.reservation.domain.StoreEntity;
import com.reservation.dto.store.StoreDto;
import com.reservation.dto.store.StoreRegisterDto;
import com.reservation.dto.store.StoreUpdateDto;
import com.reservation.exception.ApplicationException;
import com.reservation.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.reservation.type.ErrorCode.ALREADY_EXIST_STORE;
import static com.reservation.type.ErrorCode.STORE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    /**
     * 매장 등록
     * @param registerRequest 가게 등록 요청 DTO
     * @return 등록된 가게의 정보 DTO
     */
    public StoreDto registerStore(StoreRegisterDto.Request registerRequest) {
        if (storeRepository.findByStoreName(registerRequest.getStoreName()) != null) {
            throw new ApplicationException(ALREADY_EXIST_STORE);
        }


        return StoreDto.fromEntity(
                storeRepository.save(
                        StoreEntity.builder()
                                .storeName(registerRequest.getStoreName())
                                .location(registerRequest.getLocation())
                                .description(registerRequest.getDescription())
                                .build()
                )
        );
    }

    /**
     * 매장 정보 조회
     * @param storeId 가게 ID
     * @return 가게의 정보 DTO
     */
    public StoreDto getStore(Long storeId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        return StoreDto.fromEntity(store);
    }

    /**
     * 매장 정보 수정
     * @param storeId       가게 ID
     * @param updateRequest 가게 수정 요청 DTO
     * @return 수정된 가게의 정보 DTO
     */
    public StoreDto updateStore(Long storeId, StoreUpdateDto.Request updateRequest) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        store.setStoreName(updateRequest.getStoreName());
        store.setLocation(updateRequest.getLocation());
        store.setDescription(updateRequest.getDescription());

        return StoreDto.fromEntity(storeRepository.save(store));
    }

    /**
     * 매장 삭제
     * @param storeId 가게 ID
     */
    public void deleteStore(Long storeId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        storeRepository.delete(store);
    }

    /*
        TODO
        매장 정보 수정 정보 업데이트 null 체크
        입력 DTO 유효성 검사 메서드
        필수값 검증 메서드
     */
}