package com.izbean.blogsearch.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchResponseFromKakao implements Serializable {

    private static final long serialVersionUID = -8241101038812608462L;

    @JsonProperty("documents")
    private List<Document> documents;

    @JsonProperty("meta")
    private Meta meta;

    @Data
    public static class Meta {

        @JsonProperty("is_end")
        private boolean isEnd;

        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("pageable_count")
        private int pageableCount;

    }

    @Data
    public static class Document {

        @JsonProperty("title")
        private String title;

        @JsonProperty("contents")
        private String contents;

        @JsonProperty("url")
        private String url;

        @JsonProperty("blogname")
        private String blogName;

        @JsonProperty("thumbnail")
        private String thumbnail;

        @JsonProperty("datetime")
        private String dateTime;

    }

}
