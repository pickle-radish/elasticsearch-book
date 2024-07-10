package com.elasitc.api.controller;

import com.elasitc.api.domain.document.BookDocument;
import com.elasitc.api.domain.dto.SearchResponseDto;
import com.elasitc.api.service.ElasticHighLevelService;
import com.elasitc.api.service.ElasticSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {


    private final ElasticSearchService service;
    private final ElasticHighLevelService highLevelService;


    @ResponseBody
    @GetMapping("/search")
    public List<?> search(String keyword) {
        return service.searching(keyword);
    }

    @ResponseBody
    @GetMapping("/search/highlight")
    public SearchPage<?> searchHighlight(String keyword) {
        return service.searchHighlight(keyword);
    }

    @Operation(summary = "native 쿼리 검색", description = "native 쿼리 빌더를 사용한 검색")
    @ResponseBody
    @GetMapping("/search/native")
    public SearchResponseDto searchNative(
            @Parameter(required = true, description = "검색어")
            String keyword
    ) throws JsonProcessingException {
        SearchHits<BookDocument> searchHits = service.searchNative(keyword);

        SearchResponseDto responseDto = SearchResponseDto.setResult(searchHits);

        return responseDto;
    }


    @ResponseBody
    @GetMapping("/search/highlevel")
    public Object searchHighLevel(String keyword) {

        org.elasticsearch.search.SearchHits searchHits = highLevelService.searchClient(keyword);

        return searchHits;
    }

}
