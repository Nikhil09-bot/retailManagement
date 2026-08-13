package com.retailbus.sales.mapper;

import com.retailbus.sales.dto.SaleItemResponse;
import com.retailbus.sales.dto.SaleResponse;
import com.retailbus.sales.entity.Sale;
import com.retailbus.sales.entity.SaleItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SaleMapper.
 * Converts Sale entities to response DTOs.
 */
@Component
public class SaleMapper {

    /**
     * Convert Sale entity to SaleResponse DTO.
     */
    public SaleResponse toResponse(Sale sale) {
        List<SaleItemResponse> itemResponses = sale.getItems() == null ? List.of() :
                sale.getItems().stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList());

        return SaleResponse.builder()
                .id(sale.getId())
                .receiptNumber(sale.getReceiptNumber())
                .status(sale.getStatus())
                .subtotal(sale.getSubtotal())
                .taxAmount(sale.getTaxAmount())
                .discountAmount(sale.getDiscountAmount())
                .grandTotal(sale.getGrandTotal())
                .paymentMethod(sale.getPaymentMethod())
                .notes(sale.getNotes())
                .items(itemResponses)
                .createdAt(sale.getCreatedAt())
                .build();
    }

    /**
     * Convert SaleItem entity to SaleItemResponse DTO.
     */
    public SaleItemResponse toItemResponse(SaleItem item) {
        return SaleItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productCode(item.getProduct().getProductCode())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .build();
    }
}

