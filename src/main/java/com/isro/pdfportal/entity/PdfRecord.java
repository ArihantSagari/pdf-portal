package com.isro.pdfportal.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pdf_records")
public class PdfRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "form_type", length = 150)
    private String formType;

    @Column(name = "form_type_label")
    private String formTypeLabel;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "pdf_data", columnDefinition = "bytea")
    private byte[] pdfData;

    @Column(name = "subject")
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // getters and setters
    public Long getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public String getFormType() { return formType; }
    public void setFormType(String formType) { this.formType = formType; }
    public String getFormTypeLabel() { return formTypeLabel; }
    public void setFormTypeLabel(String formTypeLabel) { this.formTypeLabel = formTypeLabel; }
    public byte[] getPdfData() { return pdfData; }
    public void setPdfData(byte[] pdfData) { this.pdfData = pdfData; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
