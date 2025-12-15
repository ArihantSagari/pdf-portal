package com.isro.pdfportal.service;

import com.isro.pdfportal.entity.PdfRecord;
import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.model.FormData;
import com.isro.pdfportal.repository.PdfRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PdfRecordService {

    private final PdfRecordRepository pdfRecordRepository;

    public PdfRecordService(PdfRecordRepository pdfRecordRepository) {
        this.pdfRecordRepository = pdfRecordRepository;
    }

    // Save a new record after PDF is generated
    public void saveRecord(User user, FormData formData, byte[] pdfBytes) {
        PdfRecord record = new PdfRecord();
        record.setUser(user);
        record.setFormType(formData.getFormType());
        record.setFormTypeLabel(formData.getFormTypeLabel());
        record.setSubject(formData.getSubject());
        record.setCreatedAt(LocalDateTime.now());
        record.setPdfData(pdfBytes);
        pdfRecordRepository.save(record);
    }

    // Get user’s records for the last N days
    public List<PdfRecord> getRecentRecords(User user, int days) {
        LocalDateTime from = LocalDateTime.now().minusDays(days);
        return pdfRecordRepository
                .findByUserAndCreatedAtAfterOrderByCreatedAtDesc(user, from);
    }

    // Get a single record for this user
    public PdfRecord getRecordForUser(Long id, User user) {
        return pdfRecordRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Record not found"));
    }
}