// com.erp.repository.StoreStockRepository
package com.erp.repository;

import com.erp.dto.StoreStockDTO;
import com.erp.repository.entity.StoreStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface StoreStockRepository extends JpaRepository<StoreStock, Long> {

    // 목록 검색(페이지네이션 + 모든 조건)
    @Query(
            value = """
SELECT new com.erp.dto.StoreStockDTO(
  ss.changeDatetime,
  i.itemCategory,
  i.itemCode,
  i.itemName,
  ss.changeReason,
  ss.changeQuantity,
  ss.currentQuantity,
  ss.disposalReason,
  i.stockUnit
)
FROM StoreStock ss
JOIN StoreItem si ON ss.storeItemNo = si.storeItemNo
JOIN Item      i  ON si.itemNo      = i.itemNo
WHERE si.storeNo = :storeNo
  AND (:category IS NULL OR i.itemCategory = :category)
  AND (:reason   IS NULL OR ss.changeReason = :reason)
  AND (:start    IS NULL OR ss.changeDatetime >= :start)
  AND (:end      IS NULL OR ss.changeDatetime <  :end)
  AND (
        :keyword IS NULL OR
        (:searchType = 'NAME' AND i.itemName LIKE CONCAT('%', :keyword, '%')) OR
        (:searchType = 'CODE' AND i.itemCode LIKE CONCAT('%', :keyword, '%'))
      )
ORDER BY ss.storeStockNo DESC
""",
            countQuery = """
SELECT COUNT(ss)
FROM StoreStock ss
JOIN StoreItem si ON ss.storeItemNo = si.storeItemNo
JOIN Item      i  ON si.itemNo      = i.itemNo
WHERE si.storeNo = :storeNo
  AND (:category IS NULL OR i.itemCategory = :category)
  AND (:reason   IS NULL OR ss.changeReason = :reason)
  AND (:start    IS NULL OR ss.changeDatetime >= :start)
  AND (:end      IS NULL OR ss.changeDatetime <  :end)
  AND (
        :keyword IS NULL OR
        (:searchType = 'NAME' AND i.itemName LIKE CONCAT('%', :keyword, '%')) OR
        (:searchType = 'CODE' AND i.itemCode LIKE CONCAT('%', :keyword, '%'))
      )
"""
    )
    Page<StoreStockDTO> searchStoreStock(@Param("storeNo") Long storeNo,
                                         @Param("category") String category,
                                         @Param("reason") String reason,
                                         @Param("searchType") String searchType, // 'NAME' | 'CODE' | null
                                         @Param("keyword") String keyword,
                                         @Param("start") Timestamp start,        // null 허용
                                         @Param("end") Timestamp end,            // null 허용(다음날 00:00)
                                         Pageable pageable);

    StoreStock findFirstByStoreItemNoOrderByStoreStockNoDesc(Long storeItemNo);
}
