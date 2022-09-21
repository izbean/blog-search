package com.izbean.blogsearch.scheduler;

import com.github.benmanes.caffeine.cache.Cache;
import com.izbean.blogsearch.config.cache.CacheType;
import com.izbean.blogsearch.domain.StatisticsSearchKeywordMinutely;
import com.izbean.blogsearch.domain.StatisticsSearchKeywordMinutelyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SearchKeywordScheduler {

    private final CacheManager cacheManager;

    private final StatisticsSearchKeywordMinutelyRepository statisticsSearchKeywordMinutelyRepository;

    @Transactional(readOnly = true)
    @Scheduled(fixedDelay = 60 * 1_000)
    public void syncTopSearchKeywordCount() {
        String topSearchKeywordCountKey = CacheType.TOP_SEARCH_KEYWORD_HIT.getName();
        CaffeineCache topSearchKeywordCount = (CaffeineCache) cacheManager.getCache(topSearchKeywordCountKey);
        LocalDateTime currentTime = LocalDateTime.now();
        log.debug("syncTopSearchKeywordCount start - {} ", currentTime);

        if (topSearchKeywordCount == null) {
            log.error("SearchKeywordScheduler syncTopSearchKeywordCount(): Not found Cache Key {}", topSearchKeywordCountKey);
            return;
        }

        Cache<Object, Object> nativeCache = topSearchKeywordCount.getNativeCache();

        List<StatisticsSearchKeywordMinutely> statisticsSearchKeywordMinutelyList = new ArrayList<>();

        for (Object key : nativeCache.asMap().keySet()) {
            Object value = nativeCache.getIfPresent(key);

            if (value != null) {
                statisticsSearchKeywordMinutelyList.add(
                        StatisticsSearchKeywordMinutely.builder()
                                .keyword(String.valueOf(key))
                                .hit((Long) value)
                                .createAt(currentTime)
                                .build()
                );
            }
        }

        statisticsSearchKeywordMinutelyRepository.saveAll(statisticsSearchKeywordMinutelyList);
        topSearchKeywordCount.clear();
    }

}
