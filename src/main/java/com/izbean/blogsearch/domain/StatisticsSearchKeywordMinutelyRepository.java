package com.izbean.blogsearch.domain;

import com.izbean.blogsearch.dto.response.SearchTop10Response;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsSearchKeywordMinutelyRepository extends JpaRepository<StatisticsSearchKeywordMinutely, Long> {

    @Query("select new com.izbean.blogsearch.dto.response.SearchTop10Response(s.keyword, sum(s.hit)) from StatisticsSearchKeywordMinutely s group by s.keyword order by sum(s.hit) desc")
    List<SearchTop10Response> findHitTop10SearchKeywords(Pageable pageable);

}
