package com.isro.pdfportal.repository;

import com.isro.pdfportal.entity.PdfRecord;
import com.isro.pdfportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PdfRecordRepository extends JpaRepository<PdfRecord, Long> {

    List<PdfRecord> findByUserAndCreatedAtAfterOrderByCreatedAtDesc(
            User user, LocalDateTime fromDateTime
    );

    Optional<PdfRecord> findByIdAndUser(Long id, User user);
}