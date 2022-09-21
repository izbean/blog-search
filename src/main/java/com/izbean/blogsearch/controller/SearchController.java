package com.izbean.blogsearch.controller;

import com.izbean.blogsearch.dto.request.SearchRequest;
import com.izbean.blogsearch.dto.response.SearchResponse;
import com.izbean.blogsearch.dto.response.SearchTop10Response;
import com.izbean.blogsearch.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public SearchResponse search(
            @ModelAttribute @Valid SearchRequest request,
            HttpServletRequest httpServletRequest
    ) {
        return searchService.search(request, httpServletRequest.getRemoteAddr());
    }

    @GetMapping("/search/keywords/top10")
    public List<SearchTop10Response> getHitTop10SearchKeywords() {
        return searchService.getHitTop10SearchKeywords();
    }

}
