package com.erp.service;

import com.erp.controller.request.SearchRequestDTO;
import com.erp.dto.PageResponseDTO;
import com.erp.dto.StoreStockDTO;
import com.erp.repository.StoreStockRepository;
import com.erp.repository.entity.StoreStock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Timestamp;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StoreStockService {

    private final StoreStockRepository storeStockRepository;

    // ====== 조회 ======
    public PageResponseDTO<StoreStockDTO> search(SearchRequestDTO req) {
        // 페이지 기본값
        int page = (req.getPage() == null || req.getPage() < 1) ? 1 : req.getPage();
        int size = (req.getSize() == null || req.getSize() < 1) ? 10 : req.getSize();
        Pageable pageable = PageRequest.of(page - 1, size);

        // 카테고리 "전체"/빈값 → null
        String category = normBlankToNull(req.getCategory());
        if ("전체".equals(category)) category = null;

        // 변동유형 빈값 → null (입고/판매/폐기/조정)
        String reason = normBlankToNull(req.getReason());

        // 검색타입 매핑: ITEM_NAME/ITEM_CODE → NAME/CODE
        String searchType = switch (normBlankToNull(req.getSearchType())) {
            case "ITEM_NAME" -> "NAME";
            case "ITEM_CODE" -> "CODE";
            default -> null;
        };

        String keyword = normBlankToNull(req.getKeyword());

        // 날짜 범위: [start, end) 로 사용 (end=다음날 00:00)
        Timestamp startTs = null, endTs = null;
        if (req.getDateFrom() != null) {
            startTs = toTimestampAtStartOfDay(req.getDateFrom());
        }
        if (req.getDateTo() != null) {
            endTs = toTimestampAtStartOfDay(req.getDateTo().plusDays(1));
        }

        var pageObj = storeStockRepository.searchStoreStock(
                req.getStoreNo(), category, reason, searchType, keyword, startTs, endTs, pageable
        );

        // 페이지 블럭(5개 기준)
        int totalPages = Math.max(1, pageObj.getTotalPages());
        int blockSize = 5;
        int current = pageObj.getNumber() + 1;
        int blockIdx = (current - 1) / blockSize;
        int startPage = blockIdx * blockSize + 1;
        int endPage = Math.min(totalPages, startPage + blockSize - 1);

        return PageResponseDTO.<StoreStockDTO>builder()
                .content(pageObj.getContent())
                .page(pageObj.getNumber())
                .size(pageObj.getSize())
                .totalElements(pageObj.getTotalElements())
                .totalPages(totalPages)
                .startPage(startPage)
                .endPage(endPage)
                .hasPrevBlock(startPage > 1)
                .hasNextBlock(endPage < totalPages)
                .build();
    }

    private static String normBlankToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static Timestamp toTimestampAtStartOfDay(LocalDate d) {
        return Timestamp.valueOf(d.atStartOfDay());
    }
    private int getLatestQuantity(Long storeItemNo) {
        var log = storeStockRepository.findFirstByStoreItemNoOrderByStoreStockNoDesc(storeItemNo);
        return (log == null) ? 0 : log.getCurrentQuantity();
    }

    @Transactional
    public int dispose(Long storeItemNo, int quantity, String reason) {
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "폐기 수량은 1 이상이어야 합니다.");
        }
        int latest = getLatestQuantity(storeItemNo);
        if (quantity > latest) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "폐기 수량이 현재 재고를 초과합니다.");
        }

        var saved = storeStockRepository.save(
                StoreStock.builder()
                        .storeItemNo(storeItemNo)
                        .changeQuantity(-quantity)          // 마이너스
                        .changeReason("폐기")
                        .currentQuantity(latest - quantity) // 최신 재고 반영
                        .disposalReason(reason)
                        .build()
        );
        return saved.getCurrentQuantity();
    }
}
