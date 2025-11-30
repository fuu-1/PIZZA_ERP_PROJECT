package com.erp.controller;

import com.erp.dao.StoreDAO;
import com.erp.dto.StoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SalesController {

    private final StoreDAO storeDAO;

    @GetMapping("/salesDetailUI")
    public String salesDetailUI(@RequestParam Long storeNo,
                                @RequestParam String salesDate,
                                Model model) {


        StoreDTO store = storeDAO.getStoreDetail(storeNo);

        model.addAttribute("storeNo", storeNo);
        model.addAttribute("salesDate", salesDate);
        model.addAttribute("storeName", store.getStoreName());

        return "sales/salesDetailUI";
    }
    @GetMapping("/manager/salesMain")
    private String salesMain(){ return "sales/salesMainUI"; }

    @GetMapping("/store/storeSalesMain")
    private String storeSalesMain(){ return "sales/storeSalesMainUI"; }
}
