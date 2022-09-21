package com.izbean.blogsearch.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchTop10Response {

    private String keyword;

    private Long hit;

}
