package com.isro.pdfportal.service;

import com.isro.pdfportal.model.FormData;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfServiceImpl implements PdfService {

    @Override
    public byte[] generatePdf(FormData data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 50, 50, 60, 60);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fonts
            Font orgFont   = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font titleFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font valueFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            // ========== HEADER ==========
            Paragraph orgName = new Paragraph("ISRO DOCUMENT PDF PORTAL", orgFont);
            orgName.setAlignment(Element.ALIGN_CENTER);
            orgName.setSpacingAfter(4);
            document.add(orgName);

            Paragraph subTitle = new Paragraph(
                    normalize(data.getFormTypeLabel()),
                    titleFont
            );
            subTitle.setAlignment(Element.ALIGN_CENTER);
            subTitle.setSpacingAfter(18);
            document.add(subTitle);

            // ========== MAIN INFO TABLE ==========
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1.2f, 2.8f}); // label, value
            infoTable.setSpacingBefore(5);
            infoTable.setSpacingAfter(15);

            addRow(infoTable, "Staff No",    data.getStaffNo(),       labelFont, valueFont);
            addRow(infoTable, "Name",        data.getFullName(),      labelFont, valueFont);
            addRow(infoTable, "Department",  data.getDepartment(),    labelFont, valueFont);
            addRow(infoTable, "Designation", data.getDesignation(),   labelFont, valueFont);
            addRow(infoTable, "Email",       data.getEmail(),         labelFont, valueFont);
            addRow(infoTable, "Phone",       data.getPhone(),         labelFont, valueFont);

            addRow(infoTable, "Subject",     data.getSubject(),       labelFont, valueFont);

            // Details / Reason – justify inside value cell
            addRowJustified(infoTable, "Details / Reason",
                    data.getDetails(), labelFont, valueFont);

            addRow(infoTable, "From date",   data.getFromDate(),      labelFont, valueFont);
            addRow(infoTable, "To date",     data.getToDate(),        labelFont, valueFont);
            addRow(infoTable, "Amount",      data.getAmount(),        labelFont, valueFont);
            addRow(infoTable, "Remarks",     data.getRemarks(),       labelFont, valueFont);

            document.add(infoTable);

            // ========== SIGNATURE AREA ==========
            Paragraph space = new Paragraph("\n\n");
            document.add(space);

            PdfPTable signTable = new PdfPTable(2);
            signTable.setWidthPercentage(100);
            signTable.setWidths(new float[]{1.5f, 1.5f});

            PdfPCell empty = new PdfPCell(new Phrase(""));
            empty.setBorder(Rectangle.NO_BORDER);

            PdfPCell signCell = new PdfPCell(
                    new Phrase("Signature of Staff\n(" +
                            normalize(data.getFullName()) + ")", valueFont)
            );
            signCell.setBorder(Rectangle.NO_BORDER);
            signCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            signTable.addCell(empty);
            signTable.addCell(signCell);

            document.add(signTable);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    // ---------- Helpers ----------

    private void addRow(PdfPTable table, String label, String value,
                        Font labelFont, Font valueFont) {
        value = normalize(value);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.BOX);
        labelCell.setPadding(5f);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.BOX);
        valueCell.setPadding(5f);
        valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addRowJustified(PdfPTable table, String label, String value,
                                 Font labelFont, Font valueFont) {
        value = normalize(value);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.BOX);
        labelCell.setPadding(5f);
        labelCell.setVerticalAlignment(Element.ALIGN_TOP);

        Paragraph p = new Paragraph(value, valueFont);
        p.setAlignment(Element.ALIGN_JUSTIFIED);

        PdfPCell valueCell = new PdfPCell(p);
        valueCell.setBorder(Rectangle.BOX);
        valueCell.setPadding(5f);
        valueCell.setVerticalAlignment(Element.ALIGN_TOP);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    // If value is null or blank, show "-NA-"
    private String normalize(String s) {
        if (s == null) return "-NA-";
        String trimmed = s.trim();
        return trimmed.isEmpty() ? "-NA-" : trimmed;
    }
}
