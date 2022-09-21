package com.izbean.blogsearch.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchRequestForNaver {

    private final String query;

    private final Integer display;

    private final Integer start;

    private final String sort;

    @Builder
    public SearchRequestForNaver(String query, Integer display, Integer start, String sort) {
        this.query = query;
        this.display = display;
        this.start = start;
        this.sort = sort;
    }

    public static SearchRequestForNaver of(SearchRequest request) {
        return SearchRequestForNaver.builder()
                .query(request.getQuery())
                .display(request.getSize())
                .start(request.getPage())
                .sort("recency".equals(request.getSort()) ? "date" : "sim")
                .build();
    }

}
