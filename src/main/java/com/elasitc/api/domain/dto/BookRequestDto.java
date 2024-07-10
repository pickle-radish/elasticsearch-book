package com.elasitc.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDto {

    private Long id;
    private String title;
    private List<String> category;
    private int price;
    private String exp;
}
