package com.erp.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class SearchRequestDTO {

    private Long storeNo;
    private String category;
    private String searchType;
    private String keyword;

    private String reason;     // 입고/판매/폐기/조정 (null 허용)
    private LocalDate dateFrom; // null 허용 (YYYY-MM-DD)
    private LocalDate dateTo;   // null 허용 (YYYY-MM-DD)


    private Integer page;
    private Integer size;
}
