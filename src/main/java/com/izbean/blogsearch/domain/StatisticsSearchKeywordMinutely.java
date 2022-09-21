package com.izbean.blogsearch.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class StatisticsSearchKeywordMinutely {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String keyword;

    @NotNull
    private Long hit;

    @NotNull
    private LocalDateTime createAt;

    @Builder
    public StatisticsSearchKeywordMinutely(Long id, String keyword, Long hit, LocalDateTime createAt) {
        this.id = id;
        this.keyword = keyword;
        this.hit = hit;
        this.createAt = createAt;
    }

}
