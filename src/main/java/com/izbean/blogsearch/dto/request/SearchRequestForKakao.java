package com.izbean.blogsearch.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchRequestForKakao {

    private final String query;

    private final String sort;

    private final Integer page;

    private final Integer size;

    @Builder
    public SearchRequestForKakao(String query, String sort, Integer page, Integer size) {
        this.query = query;
        this.sort = sort;
        this.page = page;
        this.size = size;
    }

    public static SearchRequestForKakao of(SearchRequest request) {
        return SearchRequestForKakao.builder()
                .query(request.getQuery())
                .sort(request.getSort())
                .page(request.getPage())
                .size(request.getSize())
                .build();
    }

}
