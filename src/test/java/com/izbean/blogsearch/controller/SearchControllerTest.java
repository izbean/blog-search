package com.izbean.blogsearch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izbean.blogsearch.dto.request.SearchRequest;
import com.izbean.blogsearch.util.MultiValueMapUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 정상() throws Exception {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("이효리")
                .page(1)
                .size(20)
                .sort("accuracy")
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/search")
                        .queryParams(MultiValueMapUtils.convert(objectMapper, request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.display").value(20))
                .andExpect(jsonPath("$.meta.currentPage").value(1))
                .andExpect(jsonPath("$.documents.length()", Matchers.is(20)))
                .andDo(print());
    }

    @Test
    public void 검색어_누락() throws Exception {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("")
                .page(1)
                .size(20)
                .sort("accuracy")
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/search")
                        .queryParams(MultiValueMapUtils.convert(objectMapper, request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못 된 요청 입니다."))
                .andExpect(jsonPath("$.validation.query").value("검색어는 필수 입력 값 입니다."))
                .andDo(print());
    }

    @Test
    public void 유효하지_않은_페이지_번호() throws Exception {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("이효리")
                .page(9999)
                .size(20)
                .sort("accuracy")
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/search")
                        .queryParams(MultiValueMapUtils.convert(objectMapper, request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못 된 요청 입니다."))
                .andExpect(jsonPath("$.validation.page").value("결과 페이지 번호는 1~50까지 입력 할 수 있습니다."))
                .andDo(print());
    }

    @Test
    public void 유효하지_않은_정렬_방식() throws Exception {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("이효리")
                .page(1)
                .size(20)
                .sort("wrongXXXXXXXXX")
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/search")
                        .queryParams(MultiValueMapUtils.convert(objectMapper, request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못 된 요청 입니다."))
                .andExpect(jsonPath("$.validation.sort").value("정렬 방식은 accuracy(정확도순) 또는 recency(최신순) 이어야 합니다."))
                .andDo(print());
    }

    @Test
    public void 유효하지_않은_페이지_최대_글_개수() throws Exception {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("이효리")
                .page(1)
                .size(999999)
                .sort("accuracy")
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/search")
                        .queryParams(MultiValueMapUtils.convert(objectMapper, request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못 된 요청 입니다."))
                .andExpect(jsonPath("$.validation.size").value("페이지에 보여지는 총 글 개수 설정은 1~50까지 입력 할 수 있습니다."))
                .andDo(print());
    }

}