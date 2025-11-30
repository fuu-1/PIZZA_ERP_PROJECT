package com.erp.dao;

import com.erp.dto.MenuDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuDAO {

    void setMenu(MenuDTO menu);

    void addMenu(MenuDTO menu);

    void removeMenu(Long menuNo);

    List<MenuDTO> getMenuList(@Param("menuCategory")String menuCategory, @Param("releaseStatus") String releaseStatus);

    List<MenuDTO> getMenuByMenuCode(String menuCode);

    MenuDTO getMenuByMenuNo(Long menuNo);
}
