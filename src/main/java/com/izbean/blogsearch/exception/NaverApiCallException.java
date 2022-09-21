package com.izbean.blogsearch.exception;

import com.izbean.blogsearch.dto.response.error.KakaoApiErrorResponse;
import com.izbean.blogsearch.dto.response.error.NaverApiErrorResponse;
import com.izbean.blogsearch.util.MapperUtils;

public class NaverApiCallException extends InternalException {

    private static final long serialVersionUID = -8496308862204518014L;

    private int statusCode;

    private final static String MESSAGE = "네이버 API 호출 중 에러가 발생 하였습니다.";

    public NaverApiCallException(int statusCode, String responseBody) {
        super(MESSAGE);
        this.statusCode = statusCode;

        NaverApiErrorResponse response = MapperUtils.readValueOrThrow(responseBody, NaverApiErrorResponse.class);
        addValidation("message", response.getErrorMessage());
    }

    public NaverApiCallException(int statusCode, String message, Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

}
