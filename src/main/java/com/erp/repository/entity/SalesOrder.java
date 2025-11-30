package com.erp.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Setter
@Getter
@Table(name = "sales_order")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long salesOrderNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_no")
    private Store store;
    @Column(nullable = false)
    private LocalDateTime salesOrderDatetime;
    @Column(nullable = false)
    private Integer salesOrderAmount;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<StoreOrderDetail> orderDetails = new ArrayList<>();

    public void addOrderDetail(StoreOrderDetail detail) {
        this.orderDetails.add(detail);
        detail.setSalesOrder(this);
    }

}
