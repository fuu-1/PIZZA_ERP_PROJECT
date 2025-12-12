package com.erp.controller;

import com.erp.controller.exception.StoreNotFoundException;
import com.erp.dto.StoreDTO;
import com.erp.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/admin/memberListUI")
    @PreAuthorize("hasRole('ADMIN')")
    public String memberListUI() {
        return "member/memberListUI";
    }

    /**
     * 직영점 상세보기 UI
     * 예) /admin/storeDetailUI?storeNo=1&fromTab=store&fromPage=2
     */
    @GetMapping("/admin/storeDetailUI")
    @PreAuthorize("hasRole('ADMIN')")
    public String storeDetailUI(@RequestParam("storeNo") long storeNo,
                                @RequestParam(name = "fromTab",  required = false, defaultValue = "store") String fromTab,
                                @RequestParam(name = "fromPage", required = false, defaultValue = "1")     Integer fromPage,
                                Model model) {

        StoreDTO store = memberService.getStoreDetail(storeNo);
        if (store == null) {
            throw new StoreNotFoundException("직영점 정보를 찾을 수 없습니다. storeNo=" + storeNo);
        }

        model.addAttribute("store", store);
        model.addAttribute("fromTab", fromTab);
        model.addAttribute("fromPage", fromPage);

        return "member/storeDetailUI";
    }
}
