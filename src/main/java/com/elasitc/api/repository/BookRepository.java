package com.elasitc.api.repository;

import com.elasitc.api.domain.document.BookDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookRepository extends ElasticsearchRepository<BookDocument, Long> {
    List<BookDocument> findByTitle(String keyword);
    List<BookDocument> findByCategory(String keyword);

    @Highlight(
            fields = {
                    @HighlightField(name = "exp")
            },
            parameters= @HighlightParameters(preTags = "<b>", postTags = "</b>")
    )
    SearchPage<BookDocument> findByExp(String keyword, Pageable pageable);

}
