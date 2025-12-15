package com.isro.pdfportal.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pdf_records")
public class PdfRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "form_type", nullable = false, length = 100)
    private String formType;

    @Column(name = "form_type_label", nullable = false, length = 200)
    private String formTypeLabel;

    @Column(nullable = false, length = 250)
    private String subject;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * IMPORTANT:
     * bytea (not OID)
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "pdf_data", nullable = false, columnDefinition = "BYTEA")
    private byte[] pdfData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getFormTypeLabel() {
		return formTypeLabel;
	}

	public void setFormTypeLabel(String formTypeLabel) {
		this.formTypeLabel = formTypeLabel;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public byte[] getPdfData() {
		return pdfData;
	}

	public void setPdfData(byte[] pdfData) {
		this.pdfData = pdfData;
	}

    // getters & setters
    
    
    
}
