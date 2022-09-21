package com.izbean.blogsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izbean.blogsearch.config.ApiProperties;
import com.izbean.blogsearch.dto.request.SearchRequest;
import com.izbean.blogsearch.dto.request.SearchRequestForKakao;
import com.izbean.blogsearch.dto.request.SearchRequestForNaver;
import com.izbean.blogsearch.dto.response.SearchResponse;
import com.izbean.blogsearch.dto.response.SearchResponseFromKakao;
import com.izbean.blogsearch.dto.response.SearchResponseFromNaver;
import com.izbean.blogsearch.exception.KakaoApiCallException;
import com.izbean.blogsearch.exception.NaverApiCallException;
import com.izbean.blogsearch.util.HttpUtils;
import com.izbean.blogsearch.util.MultiValueMapUtils;
import io.netty.channel.ChannelOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
@Service
public class QueryService {

    private final ApiProperties apiProperties;

    private final ObjectMapper objectMapper;

    public SearchResponse getBlogKeywordSearch(SearchRequest request) {
        SearchResponseFromKakao kakao = getBlogKeywordSearchFromKakao(SearchRequestForKakao.of(request));

        if (kakao != null) {
            return SearchResponse.ofKakao(kakao, request.getPage(), request.getSize());
        }

        SearchResponseFromNaver naver = getBlogKeywordSearchFromNaver(SearchRequestForNaver.of(request));

        if (naver != null) {
            return SearchResponse.ofNaver(naver, request.getPage());
        }

        return null;
    }

    public SearchResponseFromKakao getBlogKeywordSearchFromKakao(SearchRequestForKakao request) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.AUTHORIZATION, "KakaoAK " + apiProperties.getKakaoApiKey());

        try {
            return getQuerySingle(apiProperties.getKakaoApiBlogSearchUrl(), headers, MultiValueMapUtils.convert(objectMapper, request), SearchResponseFromKakao.class);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is5xxServerError()) {
                log.error("QueryService.getBlogKeywordSearchFromKakao Provider Server Error: {}", e.getMessage());
            } else {
                throw new KakaoApiCallException(e.getStatusCode().value(), e.getResponseBodyAsString());
            }
        }

        return null;
    }

    public SearchResponseFromNaver getBlogKeywordSearchFromNaver(SearchRequestForNaver request) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Naver-Client-Id", apiProperties.getNaverApiClientId());
        headers.add("X-Naver-Client-Secret", apiProperties.getNaverApiClientSecret());

        try {
            return getQuerySingle(apiProperties.getNaverApiBlogSearchUrl(), headers, MultiValueMapUtils.convert(objectMapper, request), SearchResponseFromNaver.class);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is5xxServerError())
                log.error("QueryService.getBlogKeywordSearchFromNaver Provider Server Error: {}", e.getMessage());

            throw new NaverApiCallException(e.getStatusCode().value(), e.getResponseBodyAsString());
        }
    }

    private <T> T getQuerySingle(String url, MultiValueMap<String, String> headers, MultiValueMap<String, String> params, Class<T> tClass) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                .responseTimeout(Duration.ofSeconds(5));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build()
                .get()
                .uri(uriBuilder -> HttpUtils.parseUri(uriBuilder, url).queryParams(params).build())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .onStatus(HttpStatus::isError, ClientResponse::createException)
                .bodyToMono(tClass)
                .block();
    }

}
