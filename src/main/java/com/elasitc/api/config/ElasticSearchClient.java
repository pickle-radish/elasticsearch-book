package com.elasitc.api.config;

import lombok.NonNull;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchClient {

    /*@NonNull
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
    }*/


    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // Create the low-level client
        RestClient httpClient = RestClient.builder(
                new HttpHost("localhost", 9200)
        ).build();

        return new RestHighLevelClientBuilder(httpClient).setApiCompatibilityMode(true).build();
    }

}
