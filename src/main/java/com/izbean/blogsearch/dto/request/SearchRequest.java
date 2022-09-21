package com.izbean.blogsearch.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class SearchRequest {

    @NotEmpty(message = "검색어는 필수 입력 값 입니다.")
    private String query;

    @Pattern(regexp = "accuracy|recency", message = "정렬 방식은 accuracy(정확도순) 또는 recency(최신순) 이어야 합니다.")
    private String sort = "accuracy";

    @Max(value = 50, message = "결과 페이지 번호는 1~50까지 입력 할 수 있습니다.")
    @Positive(message = "결과 페이지 번호는 1~50까지 입력 할 수 있습니다.")
    private Integer page = 1;

    @Max(value = 50, message = "페이지에 보여지는 총 글 개수 설정은 1~50까지 입력 할 수 있습니다.")
    @Positive(message = "페이지에 보여지는 총 글 개수 설정은 1~50까지 입력 할 수 있습니다.")
    private Integer size = 10;

    @Builder
    public SearchRequest(String query, String sort, Integer page, Integer size) {
        this.query = query;
        this.sort = sort;
        this.page = page;
        this.size = size;
    }

}
