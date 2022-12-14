package com.a601.backend.api.domain.dto.common;

import java.time.LocalDateTime;

import com.a601.backend.api.domain.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String error;
    private final String code;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.error = errorCode.getStatus().name();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }

}