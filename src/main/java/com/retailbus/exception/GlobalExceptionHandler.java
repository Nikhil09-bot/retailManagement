package com.retailbus.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.retailbus.customer.exception.CustomerNotFoundException;
import com.retailbus.dto.ApiResponse;
import com.retailbus.inventory.exception.InventoryException;
import com.retailbus.product.exception.BusinessValidationException;
import com.retailbus.product.exception.DuplicateProductException;
import com.retailbus.product.exception.InvalidStockOperationException;
import com.retailbus.product.exception.ProductNotFoundException;
import com.retailbus.purchaseorder.exception.InvalidPurchaseOrderException;
import com.retailbus.purchaseorder.exception.PurchaseOrderNotFoundException;
import com.retailbus.sales.exception.InvalidSaleOperationException;
import com.retailbus.sales.exception.SaleNotFoundException;
import com.retailbus.supplier.exception.DuplicateSupplierException;
import com.retailbus.supplier.exception.SupplierNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global Exception Handler — handles all module exceptions.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Products ───────────────────────────────────────────────────────────────

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleProductNotFound(ProductNotFoundException ex, WebRequest req) {
        log.warn("Product not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateProduct(DuplicateProductException ex, WebRequest req) {
        log.warn("Duplicate product: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidStockOperationException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidStock(InvalidStockOperationException ex, WebRequest req) {
        log.warn("Invalid stock operation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessValidation(BusinessValidationException ex, WebRequest req) {
        log.warn("Business validation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
    }

    // ── Inventory ─────────────────────────────────────────────────────────────

    @ExceptionHandler(InventoryException.class)
    public ResponseEntity<ApiResponse<?>> handleInventory(InventoryException ex, WebRequest req) {
        log.warn("Inventory error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
    }

    // ── Suppliers ─────────────────────────────────────────────────────────────

    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleSupplierNotFound(SupplierNotFoundException ex, WebRequest req) {
        log.warn("Supplier not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateSupplierException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateSupplier(DuplicateSupplierException ex, WebRequest req) {
        log.warn("Duplicate supplier: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
    }

    // ── Purchase Orders ───────────────────────────────────────────────────────

    @ExceptionHandler(PurchaseOrderNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlePoNotFound(PurchaseOrderNotFoundException ex, WebRequest req) {
        log.warn("Purchase order not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidPurchaseOrderException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidPo(InvalidPurchaseOrderException ex, WebRequest req) {
        log.warn("Invalid purchase order operation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
    }

    // ── Sales ─────────────────────────────────────────────────────────────────

    @ExceptionHandler(SaleNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleSaleNotFound(SaleNotFoundException ex, WebRequest req) {
        log.warn("Sale not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidSaleOperationException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidSale(InvalidSaleOperationException ex, WebRequest req) {
        log.warn("Invalid sale operation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
    }

    // ── Customers ─────────────────────────────────────────────────────────────

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomerNotFound(CustomerNotFoundException ex, WebRequest req) {
        log.warn("Customer not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    // ── Legacy exceptions from Module 1 ───────────────────────────────────────

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(ResourceNotFoundException ex, WebRequest req) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusiness(BusinessException ex, WebRequest req) {
        log.warn("Business error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
    }

    // ── Validation ────────────────────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex, WebRequest req) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName, error.getDefaultMessage());
        });
        log.warn("Validation errors: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.builder()
                        .success(false)
                        .message("Validation failed")
                        .data(errors)
                        .timestamp(System.currentTimeMillis())
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleUnreadableMessage(HttpMessageNotReadableException ex, WebRequest req) {
        String message = "Invalid request body";
        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = invalidFormatException.getPath().stream()
                    .map(InvalidFormatException.Reference::getFieldName)
                    .filter(field -> field != null && !field.isBlank())
                    .collect(Collectors.joining("."));

            String invalidValue = String.valueOf(invalidFormatException.getValue());
            Class<?> targetType = invalidFormatException.getTargetType();

            if (targetType != null && targetType.isEnum()) {
                String acceptedValues = Arrays.stream(targetType.getEnumConstants())
                        .map(value -> ((Enum<?>) value).name())
                        .collect(Collectors.joining(", "));

                message = String.format(
                        "Invalid value '%s' for field '%s'. Accepted values: %s",
                        invalidValue,
                        fieldName,
                        acceptedValues
                );
            } else if (!fieldName.isBlank()) {
                message = String.format("Invalid value '%s' for field '%s'", invalidValue, fieldName);
            }
        } else if (cause.getMessage() != null && !cause.getMessage().isBlank()) {
            message = cause.getMessage();
        }

        log.warn("Unreadable HTTP message: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(message));
    }

    // ── General ───────────────────────────────────────────────────────────────

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException ex, WebRequest req) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobal(Exception ex, WebRequest req) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred. Please try again later."));
    }
}
