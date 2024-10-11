package com.reservation.controller;

import com.reservation.dto.store.StoreDto;
import com.reservation.dto.store.StoreRegisterDto;
import com.reservation.dto.store.StoreUpdateDto;
import com.reservation.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 매장 등록
     * @param registerRequest 등록할 매장 정보 DTO
     * @return 등록된 매장 정보 DTO
     */
    @PostMapping
    public ResponseEntity<StoreRegisterDto.Response> registerStore(@Valid @RequestBody StoreRegisterDto.Request registerRequest) {
        StoreRegisterDto.Response createdStore = storeService.registerStore(registerRequest);
        return new ResponseEntity<>(createdStore, HttpStatus.CREATED);
    }

    /**
     * 매장 ID로 조회
     * @param storeId 조회할 매장 ID
     * @return 매장 정보 DTO
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable Long storeId) {
        StoreDto store = storeService.getStoreById(storeId);
        return new ResponseEntity<>(store, HttpStatus.OK);
    }

    /**
     * 매장 이름으로 조회
     * @param storeName 조회할 매장 이름
     * @return 매장 정보 DTO
     */
    @GetMapping("/{storeName}")
    public ResponseEntity<StoreDto> getStoreByStoreName(@PathVariable String storeName) {
        StoreDto store = storeService.getStoreByStoreName(storeName);
        return new ResponseEntity<>(store, HttpStatus.OK);
    }

    /**
     * 매장 정보 수정
     * @param storeId       수정할 매장 ID
     * @param updateRequest 수정할 매장 정보 DTO
     * @return 수정된 매장 정보 DTO
     */
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreDto> updateStore(@PathVariable Long storeId,
                                                @Valid @RequestBody StoreUpdateDto.Request updateRequest) {
        StoreDto updatedStore = storeService.updateStore(storeId, updateRequest);
        return new ResponseEntity<>(updatedStore, HttpStatus.OK);
    }

    /**
     * 매장 삭제
     * @param storeId 삭제할 매장 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
