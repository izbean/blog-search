package com.izbean.blogsearch.service;

import com.izbean.blogsearch.config.cache.CacheType;
import com.izbean.blogsearch.domain.StatisticsSearchKeywordMinutely;
import com.izbean.blogsearch.domain.StatisticsSearchKeywordMinutelyRepository;
import com.izbean.blogsearch.dto.request.SearchRequest;
import com.izbean.blogsearch.dto.response.SearchResponse;
import com.izbean.blogsearch.dto.response.SearchTop10Response;
import com.izbean.blogsearch.exception.KakaoApiCallException;
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
class SearchServiceTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    private SearchService searchService;

    @Autowired
    private StatisticsSearchKeywordMinutelyRepository statisticsSearchKeywordMinutelyRepository;

    @BeforeEach
    void clean() {
        statisticsSearchKeywordMinutelyRepository.deleteAll();
        cacheManager.getCacheNames().forEach(i -> cacheManager.getCache(i).clear());
    }

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
        SearchResponse response = searchService.search(request, "127.0.0.1");

        // then
        assertEquals(1, response.getMeta().getCurrentPage());
        assertEquals(20, response.getDocuments().size());
    }

    @Test
    void 카카오_API_에_잘못된_파라미터_전달() {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("")
                .page(1)
                .size(20)
                .sort("accuracy")
                .build();

        // when & then
        assertThrows(KakaoApiCallException.class, () -> searchService.search(request, "127.0.0.1"));
    }

    @Test
    void 검색어_상위_10개를_검색_한다() {
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
        List<SearchTop10Response> response = searchService.getHitTop10SearchKeywords();

        // then
        assertEquals(10, response.size());
        assertEquals("이효리", response.get(0).getKeyword());
        assertEquals(20L, response.get(0).getHit());
        assertEquals("이효리3", response.get(1).getKeyword());
        assertEquals("이효리2", response.get(2).getKeyword());
        assertEquals("이효리4", response.get(3).getKeyword());
    }

    @Test
    void 검색어_카운팅_캐시를_업데이트_한다() {
        // given
        SearchRequest request = SearchRequest.builder()
                .query("이효리")
                .page(1)
                .size(20)
                .sort("accuracy")
                .build();

        String ip1 = "127.0.0.1";
        String ip2 = "127.0.0.2";

        // when
        searchService.updateUserSearchKeywordCache(request, ip1);
        searchService.updateUserSearchKeywordCache(request, ip1);
        searchService.updateUserSearchKeywordCache(request, ip2);

        // then
        CaffeineCache userSearchKeywordCache = (CaffeineCache) cacheManager.getCache(CacheType.USER_SEARCH_KEYWORD.getName());
        CaffeineCache topSearchKeywordCountCache = (CaffeineCache) cacheManager.getCache(CacheType.TOP_SEARCH_KEYWORD_HIT.getName());

        assertNotNull(userSearchKeywordCache.getNativeCache().getIfPresent(request.getQuery() + "_" + ip1));
        assertNotNull(userSearchKeywordCache.getNativeCache().getIfPresent(request.getQuery() + "_" + ip2));
        assertEquals(2L, topSearchKeywordCountCache.getNativeCache().getIfPresent(request.getQuery()));
    }

}