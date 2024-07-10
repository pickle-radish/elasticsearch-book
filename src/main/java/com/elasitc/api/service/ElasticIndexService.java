package com.elasitc.api.service;

import com.elasitc.api.domain.document.BookDocument;
import com.elasitc.api.domain.dto.BookRequestDto;
import com.elasitc.api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElasticIndexService {


    private final BookRepository bookRepository;
    private final ElasticsearchOperations elasticsearchOperations;


    public void createIndex() throws Exception {

        try {
            // resources에서 yaml 파일 읽어오기
            Yaml yml = new Yaml();
            InputStream settingStream = this.getClass().getClassLoader().getResourceAsStream("elastic/settings/setting.yml");
            Map<String, Object> setting = yml.load(settingStream);

            // resources에서 json 파일 읽어오기
            ClassPathResource cpr = new ClassPathResource("elastic/mappings/mapping.json");
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            String jsonTxt = new String(bdata, StandardCharsets.UTF_8);
            Document mapping = Document.create().fromJson(jsonTxt);

            //이름 + 오늘 날짜로 인덱스 명 설정
            LocalDate now = LocalDate.now();
            IndexCoordinates indexCoordinates = IndexCoordinates.of("book-" + now.toString());

            //인덱스 생성
            elasticsearchOperations.indexOps(indexCoordinates).create(setting, mapping);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }

    }


    public void setAlias() throws Exception {
        try {

            //이름 + 오늘 날짜로 인덱스 명 설정
            LocalDate now = LocalDate.now();
            IndexCoordinates indexCoordinates = IndexCoordinates.of("book-" + now.toString());

            //alias 설정
            AliasActions aliasActions = new AliasActions();

            //기존 인덱서의 alias 제거
            /*aliasActions.add(new AliasAction.Remove(AliasActionParameters.builder()
                    .withIndices("book-"+now.minusDays(1).toString())
//                    .withIndices("book-2024-07-08")
                    .withAliases("book")
                    .build()));*/

            //새 인덱스에 alias 추가
            aliasActions.add(new AliasAction.Add(AliasActionParameters.builder()
                    .withIndices("book-" + now.toString())
                    .withAliases("book")
                    .build()));

            elasticsearchOperations.indexOps(indexCoordinates).alias(aliasActions);


        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }

    }

    public void deleteIndex(String indexNm) {
        elasticsearchOperations.indexOps(IndexCoordinates.of(indexNm)).delete();
    }


    public String indexing(BookRequestDto bookRequestDto) {

//        bookRepository.save()

        bookRepository.save(BookDocument.dtoToDoc(bookRequestDto));

        return "success";
    }

}
