package com.izbean.blogsearch.dto.response.error;

import lombok.Data;

@Data
public class NaverApiErrorResponse {

    private String errorCode;

    private String errorMessage;

}
