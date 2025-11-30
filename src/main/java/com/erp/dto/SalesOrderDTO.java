package com.erp.dto;

import com.erp.repository.entity.SalesOrder;
import com.erp.repository.entity.StoreOrderDetail;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SalesOrderDTO {

    private long salesOrderNo;
    private long storeNo;
    private LocalDateTime salesOrderDatetime;
    private String storeName;
    private int salesOrderCount;
    private int salesOrderAmount;

    public static SalesOrderDTO fromEntity(SalesOrder order) {

        // 주문 메뉴 총 개수(menuCount 합산)
        int totalCount = order.getOrderDetails().stream()
                .mapToInt(StoreOrderDetail::getMenuCount)
                .sum();

        return new SalesOrderDTO(
                order.getSalesOrderNo(),
                order.getStore().getStoreNo(),
                order.getSalesOrderDatetime(),
                order.getStore().getStoreName(),
                (long) totalCount,
                Long.valueOf(order.getSalesOrderAmount())      // ★ 이미 DB 값
        );
    }

    public SalesOrderDTO(Long salesOrderNo,
                         Long storeNo,
                         LocalDateTime salesOrderDatetime,
                         String storeName,
                         Long salesOrderCount,
                         Long salesOrderAmount) {

        this.salesOrderNo = salesOrderNo;
        this.storeNo = storeNo;
        this.salesOrderDatetime = salesOrderDatetime;
        this.storeName = storeName;
        this.salesOrderCount = salesOrderCount.intValue();
        this.salesOrderAmount = salesOrderAmount.intValue();
    }

    public SalesOrderDTO(Long salesOrderNo, Long salesOrderCount, Long salesOrderAmount) {
        this.salesOrderNo = salesOrderNo;
        this.salesOrderCount = salesOrderCount.intValue();
        this.salesOrderAmount = salesOrderAmount.intValue();
    }
}
