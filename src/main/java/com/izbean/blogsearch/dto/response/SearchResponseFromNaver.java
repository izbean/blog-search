package com.izbean.blogsearch.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchResponseFromNaver implements Serializable {

    private static final long serialVersionUID = -9072337310807909495L;

    private String lastBuildDate;

    private int total;

    private int start;

    private int display;

    private List<Item> items;

    @Data
    public static class Item {

        private String title;

        private String link;

        private String description;

        @JsonProperty("bloggername")
        private String bloggerName;

        @JsonProperty("bloggerlink")
        private String bloggerLink;

        @JsonProperty("postdate")
        private String postDate;

    }

}
