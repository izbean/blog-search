package com.izbean.blogsearch.service;

import com.github.benmanes.caffeine.cache.Cache;
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

    private final CacheManager cacheManager;

    private final StatisticsSearchKeywordMinutelyRepository statisticsSearchKeywordMinutelyRepository;

    public SearchRequest getUserSearchKeywordCache(String ip, String query) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(CacheType.USER_SEARCH_KEYWORD.getName());

        if (cache == null)
            return null;

        return (SearchRequest) cache.getNativeCache().getIfPresent(query + "_" + ip);
    }

    @Cacheable(value = "usersSearchKeywords", key = "#request.query + '_' + #ip")
    public SearchRequest putUserSearchKeywordCache(String ip, SearchRequest request) {
        return request;
    }

    public void increaseSearchKeywordCountCache(String keyword) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(CacheType.TOP_SEARCH_KEYWORD_HIT.getName());

        if (cache == null) {
            return;
        }

        Cache<Object, Object> nativeCache = cache.getNativeCache();

        Object value = nativeCache.getIfPresent(keyword) != null ? nativeCache.getIfPresent(keyword) : 0L;

        if (value instanceof Long) {
            nativeCache.put(keyword, ((Long) value) + 1);
        }
    }


    @Cacheable(value = "topSearchKeywords")
    @Transactional(readOnly = true)
    public List<SearchTop10Response> getHitTop10SearchKeywords() {
        return statisticsSearchKeywordMinutelyRepository.findHitTop10SearchKeywords(Pageable.ofSize(10));
    }

}
