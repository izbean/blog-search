package com.izbean.blogsearch.config.cache;

import lombok.Getter;

@Getter
public enum CacheType {

    USER_SEARCH_KEYWORD("usersSearchKeywords", 30),

    TOP_SEARCH_KEYWORD_HIT("topSearchKeywordsHit", 60),

    TOP_SEARCH_KEYWORD("topSearchKeywords", 30);

    final String name;
    final int expireAfterWriteSec;
    final int maximumSize;

    CacheType(String name, int expireAfterWriteSec) {
        this.name = name;
        this.expireAfterWriteSec = expireAfterWriteSec;
        this.maximumSize = ConstConfig.DEFAULT_MAX_SIZE;
    }

    static class ConstConfig {
        static final int DEFAULT_MAX_SIZE = 100_000;
    }

}
