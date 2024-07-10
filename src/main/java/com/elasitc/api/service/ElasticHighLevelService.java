package com.elasitc.api.service;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticHighLevelService {

    private final RestHighLevelClient client;

    public SearchHits searchClient(String keyword) {

        try {


            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

            BoolQueryBuilder searchQueryBuilder = QueryBuilders.boolQuery();

            searchQueryBuilder.should(QueryBuilders.matchQuery("exp", keyword));

            boolQueryBuilder.must(searchQueryBuilder);

            List<String> filters = new ArrayList<>();
            filters.add("수능");
            filters.add("국어");

            boolQueryBuilder.filter(QueryBuilders.termsQuery("category", filters));

            searchSourceBuilder.query(boolQueryBuilder);

            SearchRequest request = new SearchRequest("book");
            request.source(searchSourceBuilder);

            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();

            return searchHits;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
