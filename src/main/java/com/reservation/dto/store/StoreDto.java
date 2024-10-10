package com.reservation.dto.store;

import com.reservation.domain.StoreEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {

    private Long id;
    private String storeName;
    private String location;
    private String description;

    public static StoreDto fromEntity(StoreEntity store) {
        return StoreDto.builder()
                .id(store.getId())
                .storeName(store.getStoreName())
                .location(store.getLocation())
                .description(store.getDescription())
                .build();
    }
}
