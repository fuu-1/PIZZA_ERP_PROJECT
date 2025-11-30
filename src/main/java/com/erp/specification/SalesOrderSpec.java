package com.erp.specification;

import com.erp.repository.entity.SalesOrder;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SalesOrderSpec {
    public static Specification<SalesOrder> findByDate(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }

            return cb.equal(
                    cb.function("DATE", LocalDate.class, root.get("salesOrderDatetime")),
                    date
            );
        };
    };

    public static Specification<SalesOrder> findByStoreName(String storeName) {
        return ((root, query, cb) -> {
            if (storeName == null || storeName.isEmpty()) { return null;}

            return cb.equal(
                    root.get("store").get("storeName"),
                    storeName
            );
        });
    }
}

