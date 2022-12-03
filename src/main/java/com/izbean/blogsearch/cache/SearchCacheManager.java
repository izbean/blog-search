package com.izbean.blogsearch.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.izbean.blogsearch.config.cache.CacheType;
import com.izbean.blogsearch.dto.request.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SearchCacheManager {

    private final CacheManager cacheManager;

    public SearchRequest getUserSearchRequest(String ip, String query) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(CacheType.USER_SEARCH_KEYWORD.getName());

        if (cache == null)
            return null;

        return (SearchRequest) cache.getNativeCache().getIfPresent(query + "_" + ip);
    }

    public void createUserSearchRequest(String ip, SearchRequest request) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(CacheType.USER_SEARCH_KEYWORD.getName());

        if (cache == null)
            return;

        Cache<Object, Object> nativeCache = cache.getNativeCache();

        String key = request.getQuery() + "_" + ip;
        Object value = cache.getNativeCache().getIfPresent(key);

        if (value == null)
            nativeCache.put(key, request);
    }

    public void increaseSearchKeywordHit(String keyword) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(CacheType.TOP_SEARCH_KEYWORD_HIT.getName());

        if (cache == null)
            return;


        Cache<Object, Object> nativeCache = cache.getNativeCache();

        Object value = nativeCache.getIfPresent(keyword) != null ? nativeCache.getIfPresent(keyword) : 0L;

        if (value instanceof Long)
            nativeCache.put(keyword, ((Long) value) + 1);
    }

}
