package com.izbean.blogsearch.dto.response;

import lombok.*;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SearchResponse {

    private Meta meta;

    private List<Document> documents;


    public static SearchResponse ofKakao(SearchResponseFromKakao kakaoResponse, Integer currentPage, Integer display) {
        SearchResponse.Meta responseMeta = null;
        List<SearchResponse.Document> responseDocuments = null;

        SearchResponseFromKakao.Meta kakaoMeta = kakaoResponse.getMeta();
        List<SearchResponseFromKakao.Document> kakaoDocuments = kakaoResponse.getDocuments();

        if (kakaoMeta != null) {
            responseMeta = Meta.builder()
                    .totalCount(kakaoResponse.getMeta().getTotalCount())
                    .display(display)
                    .currentPage(currentPage)
                    .isEnd(kakaoResponse.getMeta().isEnd())
                    .build();
        }

        if (!CollectionUtils.isEmpty(kakaoDocuments)) {
            responseDocuments = kakaoDocuments.stream()
                    .map(document -> Document.builder()
                            .title(document.getTitle())
                            .contents(document.getContents())
                            .url(document.getUrl())
                            .blogName(document.getBlogName())
                            .thumbnail(document.getThumbnail())
                            .dateTime(document.getDateTime())
                            .build())
                    .collect(Collectors.toList());
        }

        return SearchResponse.builder()
                .meta(responseMeta)
                .documents(responseDocuments)
                .build();
    }

    public static SearchResponse ofNaver(SearchResponseFromNaver naverResponse, Integer currentPage) {
        SearchResponse.Meta responseMeta = Meta.builder()
                .totalCount(naverResponse.getTotal())
                .display(naverResponse.getDisplay())
                .currentPage(currentPage)
                .build();
        List<SearchResponse.Document> responseDocuments = null;

        List<SearchResponseFromNaver.Item> naverItems = naverResponse.getItems();

        if (!CollectionUtils.isEmpty(naverItems)) {
            responseDocuments = naverItems.stream()
                    .map(document -> {
                        LocalDate parsePostDate = LocalDate.parse(document.getPostDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                        String formatPostDate = parsePostDate.format(formatter);

                        return Document.builder()
                                .title(document.getTitle())
                                .contents(document.getDescription())
                                .url(document.getLink())
                                .blogName(document.getBloggerName())
                                .dateTime(formatPostDate)
                                .build();
                    })
                    .collect(Collectors.toList());
        }

        return SearchResponse.builder()
                .meta(responseMeta)
                .documents(responseDocuments)
                .build();
    }

    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Meta {

        private final Integer totalCount;

        private final Integer display;

        private final Integer currentPage;

        private final Boolean isEnd;

        @Builder
        public Meta(Integer totalCount, Integer display, Integer currentPage, Boolean isEnd) {
            this.totalCount = totalCount;
            this.display = display;
            this.currentPage = currentPage;
            this.isEnd = isEnd;
        }

    }

    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Document {

        private final String title;

        private final String contents;

        private final String url;

        private final String blogName;

        private final String thumbnail;

        private final String dateTime;

        @Builder
        public Document(String title, String contents, String url, String blogName, String thumbnail, String dateTime) {
            this.title = title;
            this.contents = contents;
            this.url = url;
            this.blogName = blogName;
            this.thumbnail = thumbnail;
            this.dateTime = dateTime;
        }

    }

    @Builder
    public SearchResponse(Meta meta, List<Document> documents) {
        this.meta = meta;
        this.documents = documents;
    }

}
