package com.izbean.blogsearch.dto.response.error;

import lombok.Data;

@Data
public class KakaoApiErrorResponse {

    private String errorType;

    private String message;

}
