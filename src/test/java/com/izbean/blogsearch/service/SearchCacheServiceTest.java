package com.izbean.blogsearch.service;

import com.izbean.blogsearch.config.cache.CacheType;
import com.izbean.blogsearch.domain.StatisticsSearchKeywordMinutely;
import com.izbean.blogsearch.domain.StatisticsSearchKeywordMinutelyRepository;
import com.izbean.blogsearch.dto.request.SearchRequest;
import com.izbean.blogsearch.dto.response.SearchTop10Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchCacheServiceTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private SearchCacheService searchCacheService;

    @Autowired
    private StatisticsSearchKeywordMinutelyRepository statisticsSearchKeywordMinutelyRepository;

    @BeforeEach
    void clean() {
        statisticsSearchKeywordMinutelyRepository.deleteAll();
        cacheManager.getCacheNames().forEach(i -> cacheManager.getCache(i).clear());
    }

    @Test
    void 유저가_검색_했던_키워드_캐시를_가져온다() {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("이효리")
                .page(1)
                .size(20)
                .sort("accuracy")
                .build();

        String ip1 = "127.0.0.1";

        CaffeineCache userSearchKeywordCache = (CaffeineCache) cacheManager.getCache(CacheType.USER_SEARCH_KEYWORD.getName());
        userSearchKeywordCache.getNativeCache().put(request.getQuery() + "_" + ip1, request);

        // when
        SearchRequest searchRequest = searchCacheService.getUserSearchKeywordCache(ip1, request.getQuery());

        // then
        assertNotNull(searchRequest);
        assertEquals(request, searchRequest);
    }

    @Test
    void 유저가_검색한_키워드를_캐시로_등록한다() {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("이효리")
                .page(1)
                .size(20)
                .sort("accuracy")
                .build();

        String ip1 = "127.0.0.1";

        // when
        searchCacheService.putUserSearchKeywordCache(ip1, request);

        // then
        CaffeineCache userSearchKeywordCache = (CaffeineCache) cacheManager.getCache(CacheType.USER_SEARCH_KEYWORD.getName());
        SearchRequest requestFromCache = (SearchRequest) userSearchKeywordCache.getNativeCache().getIfPresent(request.getQuery() + "_" + ip1);

        assertNotNull(requestFromCache);
        assertEquals(request, requestFromCache);
    }

    @Test
    void 유저가_검색한_키워드의_히트를_1_증가_시킨다() {
        // given
        String keyword = "이효리";

        // when
        searchCacheService.increaseSearchKeywordCountCache(keyword);
        searchCacheService.increaseSearchKeywordCountCache(keyword);

        // then
        CaffeineCache topSearchKeywordHitCache = (CaffeineCache) cacheManager.getCache(CacheType.TOP_SEARCH_KEYWORD_HIT.getName());

        assertNotNull(topSearchKeywordHitCache);
        assertEquals(2L, topSearchKeywordHitCache.getNativeCache().getIfPresent(keyword));
    }

    @Test
    void 실시간_인기_검색어_TOP_10_을_가져온다() {
        // given
        LocalDateTime currentTime = LocalDateTime.now();
        statisticsSearchKeywordMinutelyRepository.saveAll(
                List.of(
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리").hit(10L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리").hit(10L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리2").hit(8L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리3").hit(9L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리4").hit(5L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리5").hit(1L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리6").hit(1L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리7").hit(1L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리8").hit(1L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리9").hit(1L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리10").hit(1L).createAt(currentTime).build(),
                        StatisticsSearchKeywordMinutely.builder().keyword("이효리11").hit(1L).createAt(currentTime).build()
                )
        );

        // when
        List<SearchTop10Response> response = searchCacheService.getHitTop10SearchKeywords();

        // then
        assertEquals(10, response.size());
        assertEquals("이효리", response.get(0).getKeyword());
        assertEquals(20L, response.get(0).getHit());
        assertEquals("이효리3", response.get(1).getKeyword());
        assertEquals("이효리2", response.get(2).getKeyword());
        assertEquals("이효리4", response.get(3).getKeyword());
    }

}