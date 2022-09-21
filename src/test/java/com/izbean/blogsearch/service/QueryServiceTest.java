package com.izbean.blogsearch.service;

import com.izbean.blogsearch.dto.request.SearchRequest;
import com.izbean.blogsearch.dto.request.SearchRequestForKakao;
import com.izbean.blogsearch.dto.request.SearchRequestForNaver;
import com.izbean.blogsearch.dto.response.SearchResponse;
import com.izbean.blogsearch.dto.response.SearchResponseFromKakao;
import com.izbean.blogsearch.dto.response.SearchResponseFromNaver;
import com.izbean.blogsearch.exception.NaverApiCallException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QueryServiceTest {

    @Autowired
    private QueryService queryService;

    @Test
    void 블로그_글_검색을_한다() {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("이효리")
                .page(1)
                .size(20)
                .sort("accuracy")
                .build();

        // when
        SearchResponse response = queryService.getBlogKeywordSearch(request);

        // then
        assertNotNull(response);
        assertEquals(20, response.getDocuments().size());
        assertTrue(response.getDocuments().get(0).getTitle().contains("이효리"));
    }

    @Test
    void 카카오_블로그_글_검색을_한다() {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("이효리")
                .page(1)
                .size(20)
                .sort("accuracy")
                .build();

        // when
        SearchResponseFromKakao response = queryService.getBlogKeywordSearchFromKakao(SearchRequestForKakao.of(request));

        // then
        assertNotNull(response);
        assertEquals(20, response.getDocuments().size());
        assertTrue(response.getDocuments().get(0).getTitle().contains("이효리"));
    }

    @Test
    void 네이버_블로그_글_검색을_한다() {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("이효리")
                .page(1)
                .size(20)
                .sort("accuracy")
                .build();

        // when
        SearchResponseFromNaver response = queryService.getBlogKeywordSearchFromNaver(SearchRequestForNaver.of(request));

        // then
        assertNotNull(response);
        assertEquals(20, response.getItems().size());
        assertTrue(response.getItems().get(0).getTitle().contains("이효리"));
    }

    @Test
    void 네이버_API_에_잘못된_파라미터_전달() {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("")
                .page(1)
                .size(20)
                .sort("accuracy")
                .build();

        // when & then
        assertThrows(NaverApiCallException.class, () -> queryService.getBlogKeywordSearchFromNaver(SearchRequestForNaver.of(request)));
    }

}