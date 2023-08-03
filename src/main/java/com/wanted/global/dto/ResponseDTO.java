package com.wanted.global.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ResponseDTO<T> {
    private String resultCode;
    private String message;
    private T data;

    public ResponseDTO(String resultCode, String message, T data) {
        this.resultCode = resultCode;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDTO<T> of(String resultCode, String msg, T data) {
        return new ResponseDTO<>(resultCode, msg, data);
    }

    public static <T> ResponseDTO<T> of(String resultCode, T data) {
        return new ResponseDTO<>(resultCode, null, data);
    }

    public static <T> ResponseDTO<T> of(String resultCode, String message) {
        return of(resultCode, message, null);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return resultCode.startsWith("S-");
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

}
