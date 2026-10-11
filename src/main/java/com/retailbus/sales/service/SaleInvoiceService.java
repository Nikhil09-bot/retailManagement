package com.retailbus.sales.service;

/**
 * SaleInvoiceService.
 * Generates a printable PDF invoice/receipt for a completed sale.
 */
public interface SaleInvoiceService {

    /**
     * Generate a PDF invoice for the given sale ID.
     *
     * @param saleId the ID of the sale
     * @return the raw PDF bytes
     */
    byte[] generateInvoicePdf(Long saleId);
}

