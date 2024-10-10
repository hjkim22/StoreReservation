package com.reservation.dto.store;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateDto {

    public static class Request {
        @NotNull
        @Size(min = 1, max = 50)
        private String storeName;

        @NotNull
        @Size(min = 1, max = 50)
        private String location;

        @Size(max = 100)
        private String description;
    }

    public static class Response {
        private Long id;
        private String message;
    }
}