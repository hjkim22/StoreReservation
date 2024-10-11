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
     * @param registerRequest 매장 등록 요청 DTO
     * @return 등록된 매장의 정보 DTO
     */
    public StoreRegisterDto.Response registerStore(StoreRegisterDto.Request registerRequest) {
        if (storeRepository.findByStoreName(registerRequest.getStoreName()) != null) {
            throw new ApplicationException(ALREADY_EXIST_STORE);
        }

        StoreEntity savedStore = createStore(registerRequest);

        return StoreRegisterDto.Response.builder()
                .id(savedStore.getId())
                .storeName(savedStore.getStoreName())
                .location(savedStore.getLocation())
                .description(savedStore.getDescription())
                .message("매장 등록 성공")
                .build();
    }

    /**
     * 매장 정보 조회
     * @param storeId 매장 ID
     * @return 매장의 정보 DTO
     */
    public StoreDto getStoreById(Long storeId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        return StoreDto.fromEntity(store);
    }

    /**
     * 매장 정보 조회
     * @param storeName 매장명
     * @return 매장의 정보 DTO
     */
    public StoreDto getStoreByStoreName(String storeName) {
        StoreEntity store = storeRepository.findByStoreName(storeName)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        return StoreDto.fromEntity(store);
    }

    /**
     * 매장 정보 수정
     * @param storeId       매장 ID
     * @param updateRequest 매장 수정 요청 DTO
     * @return 수정된 매장 정보 DTO
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
     * @param storeId 매장 ID
     */
    public void deleteStore(Long storeId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        storeRepository.delete(store);
    }

    /**
     * 매장 엔티티 생성
     * @param registerRequest 매장 등록 요청 객체
     * @return 생성된 매장 엔티티
     */
    private StoreEntity createStore(StoreRegisterDto.Request registerRequest) {
        return storeRepository.save(
                StoreEntity.builder()
                        .storeName(registerRequest.getStoreName())
                        .location(registerRequest.getLocation())
                        .description(registerRequest.getDescription())
                        .build()
        );
    }
}