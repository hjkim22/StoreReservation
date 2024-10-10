package com.reservation.service;

import com.reservation.dto.store.StoreDto;
import com.reservation.dto.store.StoreRegisterDto;
import com.reservation.exception.ApplicationException;
import com.reservation.repository.StoreRepository;
import com.reservation.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreDto registerStore(StoreRegisterDto.Request registerRequest) {
        if (storeRepository.findByStoreName(registerRequest.) !=null){
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_STORE);
        }
    }
}
