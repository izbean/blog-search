package com.izbean.blogsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api")
@Data
public class ApiProperties {

    private Search search;

    @Data
    public static class Search {

        private Naver naver;

        private Kakao kakao;

        @Data
        public static class Naver {
            private String blogSearchUrl;
            private String clientId;
            private String clientSecret;
        }

        @Data
        public static class Kakao {
            private String blogSearchUrl;
            private String apiKey;
        }

    }

    public String getKakaoApiKey() {
        return search.kakao.apiKey;
    }

    public String getKakaoApiBlogSearchUrl() {
        return search.kakao.blogSearchUrl;
    }

    public String getNaverApiBlogSearchUrl() {
        return search.naver.blogSearchUrl;
    }

    public String getNaverApiClientId() {
        return search.naver.clientId;
    }

    public String getNaverApiClientSecret() {
        return search.naver.clientSecret;
    }

}
