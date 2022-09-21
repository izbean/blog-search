package com.izbean.blogsearch.service;

import com.izbean.blogsearch.dto.request.SearchRequest;
import com.izbean.blogsearch.dto.response.SearchResponse;
import com.izbean.blogsearch.dto.response.SearchTop10Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {

    private final SearchCacheService searchCacheService;

    private final QueryService queryService;

    public SearchResponse search(SearchRequest request, String clientIp) {
        SearchResponse response = queryService.getBlogKeywordSearch(request);

        if (response == null) {
            return new SearchResponse();
        }

        updateUserSearchKeywordCache(request, clientIp);

        return response;
    }

    @Transactional(readOnly = true)
    public List<SearchTop10Response> getHitTop10SearchKeywords() {
        return searchCacheService.getHitTop10SearchKeywords();
    }

    public void updateUserSearchKeywordCache(SearchRequest request, String clientIp) {
        if (searchCacheService.getUserSearchKeywordCache(clientIp, request.getQuery()) == null) {
            searchCacheService.putUserSearchKeywordCache(clientIp, request);
            searchCacheService.increaseSearchKeywordCountCache(request.getQuery());
        }
    }

}
