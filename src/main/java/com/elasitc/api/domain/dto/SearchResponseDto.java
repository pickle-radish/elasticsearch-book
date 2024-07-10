package com.elasitc.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.elasticsearch.client.elc.Aggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResponseDto {

    private long totalHits;
    private double maxScore;
    private List<?> searchHits;
    private LinkedHashMap<String, Object> aggregations;

    public static SearchResponseDto setResult(SearchHits<?> searchHits) {

        //집계aggregation 꺼내기
        LinkedHashMap<String, Object> aggMap = new LinkedHashMap<>();
        ArrayList<ElasticsearchAggregation> aggregations = (ArrayList<ElasticsearchAggregation>) searchHits.getAggregations().aggregations();
        for(ElasticsearchAggregation elasticsearchAggregation:  aggregations) {
//            ElasticsearchAggregation elasticsearchAggregation = (ElasticsearchAggregation) aggregations.get(0);
            Aggregation aggregation = elasticsearchAggregation.aggregation();
            String aggStr = aggregation.getAggregate().toString();

            try {
                //Aggregate의 map 형식으로 되어 있어 json 형태로 사용하기 위해 {}로 감싸준다
                JSONObject jsonObject = new JSONObject("{" + aggStr + "}");

                LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();

                // Handle the "buckets" array
                JSONObject aggregate = jsonObject.getJSONObject("Aggregate");
                JSONArray bucketsArray = aggregate.getJSONArray("buckets");

                List<HashMap<String, Object>> buckets = new ArrayList<>();
                for (int i = 0; i < bucketsArray.length(); i++) {
                    HashMap<String, Object> bucketsMap = new HashMap<>();
                    JSONObject bucket = bucketsArray.getJSONObject(i);
                    String key = bucket.getString("key");
                    int docCount = bucket.getInt("doc_count");
                    bucketsMap.put("key", key);
                    bucketsMap.put("docCount", docCount);
                    buckets.add(bucketsMap);
                }

                resultMap.put("buckets", buckets);

                // Handle other keys
                resultMap.put("doc_count_error_upper_bound", aggregate.getInt("doc_count_error_upper_bound"));
                resultMap.put("sum_other_doc_count", aggregate.getInt("sum_other_doc_count"));

                aggMap.put(aggregation.getName(), resultMap);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        return SearchResponseDto.builder()
                .totalHits(searchHits.getTotalHits())
                .maxScore(searchHits.getMaxScore())
                .searchHits(searchHits.getSearchHits())
                .aggregations(aggMap)
                .build();
    }

}
