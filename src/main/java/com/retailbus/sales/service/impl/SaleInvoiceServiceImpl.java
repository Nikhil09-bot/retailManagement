package com.retailbus.sales.service.impl;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.retailbus.exception.ResourceNotFoundException;
import com.retailbus.sales.dto.SaleItemResponse;
import com.retailbus.sales.dto.SaleResponse;
import com.retailbus.sales.service.SaleInvoiceService;
import com.retailbus.sales.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * SaleInvoiceServiceImpl.
 * Builds a PDF invoice/receipt document for a sale using OpenPDF.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SaleInvoiceServiceImpl implements SaleInvoiceService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLACK);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
    private static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
    private static final Font BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);
    private static final Font SMALL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);
    private static final Color BRAND_COLOR = new Color(33, 82, 158);

    private final SaleService saleService;

    @Override
    public byte[] generateInvoicePdf(Long saleId) {
        SaleResponse sale = saleService.getSaleById(saleId);
        if (sale == null) {
            throw new ResourceNotFoundException("Sale not found with id: " + saleId);
        }

        log.info("Generating PDF invoice for sale ID {} (receipt {})", saleId, sale.getReceiptNumber());

        Document document = new Document(PageSize.A5);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            addHeader(document, sale);
            addItemsTable(document, sale);
            addTotals(document, sale);
            addFooter(document);

            document.close();
        } catch (DocumentException e) {
            log.error("Failed to generate PDF invoice for sale {}", saleId, e);
            throw new IllegalStateException("Failed to generate PDF invoice", e);
        }

        return out.toByteArray();
    }

    private void addHeader(Document document, SaleResponse sale) throws DocumentException {
        Paragraph title = new Paragraph("RetailBusiness", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph("Sales Invoice / Receipt", BOLD_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(12f);
        document.add(subtitle);

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(10f);

        addPlainCell(infoTable, "Receipt #: " + sale.getReceiptNumber(), Element.ALIGN_LEFT);
        addPlainCell(infoTable, "Status: " + sale.getStatus(), Element.ALIGN_RIGHT);
        addPlainCell(infoTable, "Date: " + sale.getCreatedAt().format(DATE_FORMAT), Element.ALIGN_LEFT);
        addPlainCell(infoTable, "Payment: " + sale.getPaymentMethod(), Element.ALIGN_RIGHT);

        document.add(infoTable);
    }

    private void addPlainCell(PdfPTable table, String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setBorder(Element.ALIGN_TOP);
        cell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        cell.setPaddingBottom(4f);
        table.addCell(cell);
    }

    private void addItemsTable(Document document, SaleResponse sale) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{3f, 2.5f, 1f, 1.5f, 1.5f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(6f);
        table.setSpacingAfter(10f);

        addHeaderCell(table, "Product");
        addHeaderCell(table, "Code");
        addHeaderCell(table, "Qty");
        addHeaderCell(table, "Unit Price");
        addHeaderCell(table, "Line Total");

        if (sale.getItems() != null) {
            for (SaleItemResponse item : sale.getItems()) {
                addBodyCell(table, item.getProductName(), Element.ALIGN_LEFT);
                addBodyCell(table, item.getProductCode(), Element.ALIGN_LEFT);
                addBodyCell(table, String.valueOf(item.getQuantity()), Element.ALIGN_CENTER);
                addBodyCell(table, formatMoney(item.getUnitPrice()), Element.ALIGN_RIGHT);
                addBodyCell(table, formatMoney(item.getLineTotal()), Element.ALIGN_RIGHT);
            }
        }

        document.add(table);
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(BRAND_COLOR);
        cell.setPadding(6f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, NORMAL_FONT));
        cell.setPadding(5f);
        cell.setHorizontalAlignment(alignment);
        table.addCell(cell);
    }

    private void addTotals(Document document, SaleResponse sale) throws DocumentException {
        PdfPTable totals = new PdfPTable(new float[]{3f, 1.5f});
        totals.setWidthPercentage(60);
        totals.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totals.setSpacingAfter(14f);

        addTotalRow(totals, "Subtotal", sale.getSubtotal(), NORMAL_FONT);
        addTotalRow(totals, "Tax", sale.getTaxAmount(), NORMAL_FONT);
        addTotalRow(totals, "Discount", negate(sale.getDiscountAmount()), NORMAL_FONT);
        addTotalRow(totals, "Grand Total", sale.getGrandTotal(), BOLD_FONT);

        document.add(totals);

        if (sale.getNotes() != null && !sale.getNotes().isBlank()) {
            Paragraph notes = new Paragraph("Notes: " + sale.getNotes(), SMALL_FONT);
            notes.setSpacingAfter(10f);
            document.add(notes);
        }
    }

    private BigDecimal negate(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value.negate();
    }

    private void addTotalRow(PdfPTable table, String label, BigDecimal value, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setPadding(3f);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(formatMoney(value), font));
        valueCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setPadding(3f);
        table.addCell(valueCell);
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph();
        footer.add(new Chunk("\nThank you for your business!", SMALL_FONT));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private String formatMoney(BigDecimal value) {
        BigDecimal amount = value == null ? BigDecimal.ZERO : value;
        return amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }
}

