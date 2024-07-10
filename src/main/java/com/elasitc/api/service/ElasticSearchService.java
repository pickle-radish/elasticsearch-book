package com.elasitc.api.service;


import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.elasitc.api.domain.document.BookDocument;
import com.elasitc.api.domain.dto.BookRequestDto;
import com.elasitc.api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ElasticSearchService {

    private final BookRepository bookRepository;
    private final ElasticsearchOperations elasticsearchOperations;


    public List<BookDocument> searching(String keyword) {
        List<BookDocument> result = new ArrayList<>();

//        result = bookRepository.findByTitle(keyword);
        result = bookRepository.findByCategory(keyword);

        return result;
    }

    public SearchHits<BookDocument> searchNative(String keyword) {

        // 페이징 처리 page, page당 개수
        Pageable pageable = PageRequest.of(0, 5);

        //하이라이트 쿼리 생성
        HighlightParameters highlightParameters = HighlightParameters.builder()
                .withPreTags("<b>")
                .withPostTags("</b>")
                .build();

        //하이라이트 필드 리스트
        List<HighlightField> highlightFields = new ArrayList<>();
        highlightFields.add(new HighlightField("title"));
        highlightFields.add(new HighlightField("exp"));

        // Define the highlight field
        Highlight highlight = new Highlight(highlightParameters, highlightFields);

        // Define the highlight query
        HighlightQuery highlightQuery = new HighlightQuery(highlight, null);


        Query query = NativeQuery.builder()
                //집계
                .withAggregation("category", Aggregation.of(a -> a
                        .terms(ta -> ta.field("category").size(10))))
                .withAggregation("price", Aggregation.of(a -> a
                        .terms(ta -> ta.field("price").size(10))))
                //하이라이트
                .withHighlightQuery(highlightQuery)
                //쿼리
                .withQuery(q -> q
                        .multiMatch(m -> m
                                .fields("title", "exp")
                                .query(keyword)
                                .operator(Operator.And)
                                .type(TextQueryType.CrossFields)
                        )
                )
                //페이지 처리
                .withPageable(pageable)
                .build();

        Query query2 = NativeQuery.builder()
                //쿼리
                .withQuery(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .multiMatch(mq -> mq
                                                .fields("title", "exp")
                                                .query(keyword)
                                                .operator(Operator.And)
                                                .type(TextQueryType.CrossFields)
                                        )
                                )
                        )
                )
                .build();



        BoolQuery.Builder queryBuilder = QueryBuilders.bool();
        queryBuilder.should(co.elastic.clients.elasticsearch._types.query_dsl.Query.of(q -> q
                .match(m -> m
                        .field("exp")
                        .query(keyword)
                )
        ));
        queryBuilder.must(co.elastic.clients.elasticsearch._types.query_dsl.Query.of(q -> q
                .match(m -> m
                        .field("title")
                        .query(keyword)
                )
        ));

        BoolQuery boolQuery = queryBuilder.build();

        Query query3 = NativeQuery.builder()
                //쿼리
                .withQuery(q -> q
                        .bool(boolQuery)
                )
                .build();

        SearchHits<BookDocument> searchHits = elasticsearchOperations.search(query3, BookDocument.class);

        return searchHits;
    }

    public SearchPage<BookDocument> searchHighlight(String keyword) {
//        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(1, 10, sort);

        SearchPage<BookDocument> result = bookRepository.findByExp(keyword, pageable);


        return result;

    }


}
