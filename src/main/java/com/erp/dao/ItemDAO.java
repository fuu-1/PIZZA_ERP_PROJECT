package com.erp.dao;

import com.erp.dto.ItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemDAO {

    // 1) 등록
    int addItem(ItemDTO item);

    // 2) 수정
    int setItem(ItemDTO item);

    // 3) 삭제 (del_date 업데이트)
    int removeItem(Long itemNo);

    // 4) 전체 목록 조회
    List<ItemDTO> getItemList(@Param("itemCategory") String itemCategory,
                              @Param("ingredientName") String ingredientName, @Param("itemCode") String itemCode);

    // 상세 조회
    ItemDTO getItemDetail(Long itemNo);

    ItemDTO getItemByItemNo(@Param("itemNo") Long itemNo);
}
