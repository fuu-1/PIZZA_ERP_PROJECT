package com.erp.controller;

import com.erp.auth.PrincipalDetails;
import com.erp.dto.SalesChartDTO;
import com.erp.dto.StoreDailyMenuSalesDTO;
import com.erp.service.SalesChartService;
import com.erp.service.SalesKPIService;
import com.erp.service.SalesListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class StoreSalesRestController {

    private final SalesChartService salesChartService;
    @GetMapping("/store/salesChart")
    public SalesChartDTO getSalesChart(
            @AuthenticationPrincipal PrincipalDetails principal,
            String startDate,
            String endDate,
            String type
    ) {

        Long storeNo = principal.getStoreNo();

        return salesChartService.getSalesChartByStore(
                storeNo,
                startDate,
                endDate,
                type
        );
    }

}