package com.isro.pdfportal.service;

import com.isro.pdfportal.model.FormData;

public interface PdfService {
    byte[] generatePdf(FormData data);
}
