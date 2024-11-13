package com.choi76.web_socket_jwt.global.exception;

import com.choi76.web_socket_jwt.global.enums.statuscode.BaseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 커스텀  예외 처리 일반화
@RequiredArgsConstructor
public class GeneralException extends RuntimeException{
    private final BaseCode errorStatus;

    public String getErrorCode() {
        return errorStatus.getCode();
    }

    public String getErrorReason() {
        return errorStatus.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return errorStatus.getHttpStatus();
    }

    @Override
    public String getMessage() {
        return errorStatus.getMessage();
    }
}
