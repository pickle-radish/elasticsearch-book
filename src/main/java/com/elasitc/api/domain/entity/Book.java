package com.elasitc.api.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    private Long id;

    private String title;

    private List<String> category;

    private int price;

    private String exp;
}
