package com.erp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@SpringBootTest
public class SalesListServiceTest {

    @Autowired
    private SalesListService salesListService;

    @Test
    void getSalesDetailTest(){
        System.out.println(salesListService.getSalesDetail(2L, LocalDate.of(2025,11,17)));
    }

    @Test
    void getSalesListTest(){
        Pageable pageable = PageRequest.of(0, 10);
        System.out.println(salesListService.getSalesList(null,null,null,pageable).getContent());
    }

}
