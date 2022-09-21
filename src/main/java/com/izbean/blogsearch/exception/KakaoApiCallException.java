package com.izbean.blogsearch.exception;

import com.izbean.blogsearch.dto.response.error.KakaoApiErrorResponse;
import com.izbean.blogsearch.util.MapperUtils;

public class KakaoApiCallException extends InternalException {

    private static final long serialVersionUID = -8496308862204518014L;

    private int statusCode;

    private final static String MESSAGE = "카카오 API 호출 중 에러가 발생 하였습니다.";

    public KakaoApiCallException(int statusCode, String responseBody) {
        super(MESSAGE);
        this.statusCode = statusCode;

        KakaoApiErrorResponse response = MapperUtils.readValueOrThrow(responseBody, KakaoApiErrorResponse.class);
        addValidation(response.getErrorType(), response.getMessage());
    }

    public KakaoApiCallException(int statusCode, String message, Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

}
