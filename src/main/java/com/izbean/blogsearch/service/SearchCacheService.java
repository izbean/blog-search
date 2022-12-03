package com.izbean.blogsearch.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.izbean.blogsearch.cache.SearchCacheManager;
import com.izbean.blogsearch.config.cache.CacheType;
import com.izbean.blogsearch.domain.StatisticsSearchKeywordMinutelyRepository;
import com.izbean.blogsearch.dto.request.SearchRequest;
import com.izbean.blogsearch.dto.response.SearchTop10Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchCacheService {

    private final SearchCacheManager searchCacheManager;

    private final StatisticsSearchKeywordMinutelyRepository statisticsSearchKeywordMinutelyRepository;

    public SearchRequest getUserSearchKeywordCache(String ip, String query) {
        return searchCacheManager.getUserSearchRequest(ip, query);
    }

    public void putUserSearchKeywordCache(String ip, SearchRequest request) {
        searchCacheManager.createUserSearchRequest(ip, request);
    }

    public void increaseSearchKeywordCountCache(String keyword) {
        searchCacheManager.increaseSearchKeywordHit(keyword);
    }

    @Cacheable(value = "topSearchKeywords")
    @Transactional(readOnly = true)
    public List<SearchTop10Response> getHitTop10SearchKeywords() {
        return statisticsSearchKeywordMinutelyRepository.findHitTop10SearchKeywords(Pageable.ofSize(10));
    }

}
