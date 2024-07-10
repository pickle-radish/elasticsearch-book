package com.elasitc.api.domain.document;


import com.elasitc.api.domain.dto.BookRequestDto;
import com.elasitc.api.domain.entity.Book;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Document(indexName = "book-#{T(java.time.LocalDate).now().toString()}", createIndex = true)
@Document(indexName = "book", createIndex = false)
@Setting(settingPath = "/elastic/settings/setting.yml")
@Mapping(mappingPath = "/elastic/mappings/mapping.json")
public class BookDocument {

    @Id
    private Long id;

    private String title;

    private List<String> category;

    private int price;

    private String exp;


    public static BookDocument entityToDoc(Book book) {
        return BookDocument.builder()
                .id(book.getId())
                .title(book.getTitle())
                .category(book.getCategory())
                .price(book.getPrice())
                .exp(book.getExp())
                .build();
    }

    public static BookDocument dtoToDoc(BookRequestDto book) {
        return BookDocument.builder()
                .id(book.getId())
                .title(book.getTitle())
                .category(book.getCategory())
                .price(book.getPrice())
                .exp(book.getExp())
                .build();
    }

}
