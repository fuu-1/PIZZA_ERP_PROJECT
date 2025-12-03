package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class StoreStockDTO {
    private final Timestamp changeDatetime;   // 변동일시
    private final String itemCategory;        // 품목 카테고리
    private final String itemCode;            // 품목코드
    private final String itemName;            // 품목명
    private final String changeReason;        // 변동유형(입고/판매/폐기/조정)
    private final Integer changeQuantity;     // 변동수량(+/-)
    private final Integer currentQuantity;    // 변동 후 재고
    private final String disposalReason;      // 폐기사유(null 가능)
    private final String stockUnit;           // 재고단위 (ITEM.stock_unit)
}
