package com.elasitc.api.controller;


import com.elasitc.api.domain.dto.BookRequestDto;
import com.elasitc.api.service.ElasticIndexService;
import com.elasitc.api.service.ElasticSearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MngController {

    private final ElasticIndexService service;

    @Operation(summary = "인덱스 생성", description = "새 인덱스를 생성 하고 alias설정을 합니다.")
    @ResponseBody
    @GetMapping("/create/index")
    public String create() {

        try {
            //새 인덱스 생성
            service.createIndex();
            //색인

            //alias 설정
            service.setAlias();

        } catch (Exception e) {
            return "fail";
        }

        return "success";
    }

    @ResponseBody
    @GetMapping("/delete/index")
    public String deleteIndex(String indexNm) {
        //오래된 인덱스 삭제
        service.deleteIndex(indexNm);

        return "success";
    }

    @ResponseBody
    @GetMapping("/indexing/param")
    public String indexing(BookRequestDto requestDto) {
        return service.indexing(requestDto);
    }

    @ResponseBody
    @GetMapping("/indexing/default")
    public String indexingDefault() {
        String result;

        BookRequestDto requestDto = new BookRequestDto();

        List<String> category = new ArrayList<>();
        category.add("국어");

        requestDto.setId(1L);
        requestDto.setTitle("초등 국어");
        requestDto.setCategory(category);
        requestDto.setPrice(5000);
        requestDto.setExp("초등학교 1학년 1학기 국어 과목");

        result = service.indexing(requestDto);

        requestDto.setId(2L);
        requestDto.setTitle("중학 국어");
        requestDto.setCategory(category);
        requestDto.setPrice(7000);
        requestDto.setExp("중학교 1학년 1학기 국어 과목");

        result = service.indexing(requestDto);
        category.add("수능");

        requestDto.setId(3L);
        requestDto.setTitle("고등 국어");
        requestDto.setCategory(category);
        requestDto.setPrice(10000);
        requestDto.setExp("고등학교 1학년 1학기 국어 과목 수능");

        result = service.indexing(requestDto);

        category.remove(0);
        category.add("수학");

        requestDto.setId(4L);
        requestDto.setTitle("초등 수학");
        requestDto.setCategory(category);
        requestDto.setPrice(5000);
        requestDto.setExp("초등학교 1학년 1학기 수학 과목");

        result = service.indexing(requestDto);

        requestDto.setId(5L);
        requestDto.setTitle("중학 수학");
        requestDto.setCategory(category);
        requestDto.setPrice(7000);
        requestDto.setExp("중학교 1학년 1학기 수학 과목");

        result = service.indexing(requestDto);

        requestDto.setId(6L);
        requestDto.setTitle("고등 수학");
        requestDto.setCategory(category);
        requestDto.setPrice(10000);
        requestDto.setExp("고등학교 1학년 1학기 수학 과목");

        result = service.indexing(requestDto);

        category.add("수능");

        requestDto.setId(7L);
        requestDto.setTitle("수능완성 수리영역");
        requestDto.setCategory(category);
        requestDto.setPrice(10000);
        requestDto.setExp("고등학교 1학년 2학기 수학 과목 수능 수학은 이제 ebs에서");

        result = service.indexing(requestDto);

        requestDto.setId(8L);
        requestDto.setTitle("수능완성 수리영역2");
        requestDto.setCategory(category);
        requestDto.setPrice(10000);
        requestDto.setExp("고등학교 2학년 1학기 수학 과목 수능 수학은 이제 ebs에서 이제는 2학년");

        result = service.indexing(requestDto);

        requestDto.setId(9L);
        requestDto.setTitle("수능완성 수리영역2-2");
        requestDto.setCategory(category);
        requestDto.setPrice(10000);
        requestDto.setExp("고등학교 2학년 2학기 수학 과목 수능 수학은 이제 ebs에서 2학년 마무리");

        result = service.indexing(requestDto);

        requestDto.setId(10L);
        requestDto.setTitle("수능완성 수리영역3");
        requestDto.setCategory(category);
        requestDto.setPrice(10000);
        requestDto.setExp("고등학교 3학년 1학기 수학 과목 수능 수학은 이제 ebs에서 마지막 스퍼트");

        result = service.indexing(requestDto);

        requestDto.setId(11L);
        requestDto.setTitle("수능완성 수리영역3-2");
        requestDto.setCategory(category);
        requestDto.setPrice(10000);
        requestDto.setExp("고등학교 3학년 1학기 수학 과목 수능 수리영억 마지막 마무리");

        result = service.indexing(requestDto);

        requestDto.setId(12L);
        requestDto.setTitle("수리영역 1-1");
        requestDto.setCategory(category);
        requestDto.setPrice(10000);
        requestDto.setExp("고등학교 1학년 1학기 수학을 배우자");

        result = service.indexing(requestDto);

        requestDto.setId(13L);
        requestDto.setTitle("수리영역 1-2");
        requestDto.setCategory(category);
        requestDto.setPrice(10000);
        requestDto.setExp("고등학교 1학년 2학기 수학을 배워볼까");

        result = service.indexing(requestDto);

        requestDto.setId(14L);
        requestDto.setTitle("수리영역 2-1");
        requestDto.setCategory(category);
        requestDto.setPrice(10000);
        requestDto.setExp("고등학교 2학년 1학기 수학은 이제 시작이지");

        result = service.indexing(requestDto);

        return result;
    }


}
