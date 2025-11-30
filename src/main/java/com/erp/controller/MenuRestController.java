package com.erp.controller;

import com.erp.dto.MenuDTO;
import com.erp.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuRestController {
    private final MenuService menuService;

    @GetMapping("menuList")
    public ResponseEntity<List<MenuDTO>> getMenuList(
            @RequestParam(required = false) String menuCategory,
            @RequestParam(required = false) String releaseStatus
    ) {
        List<MenuDTO> menuList = menuService.getMenuList(menuCategory, releaseStatus);
        return ResponseEntity.ok(menuList);
    }
}
