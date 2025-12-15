package com.isro.pdfportal.controller;

import com.isro.pdfportal.config.CustomUserDetails;
import com.isro.pdfportal.entity.PdfRecord;
import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.model.FormData;
import com.isro.pdfportal.model.FormType;
import com.isro.pdfportal.service.PdfRecordService;
import com.isro.pdfportal.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/forms")
public class FormController {

    private final PdfService pdfService;
    private final PdfRecordService pdfRecordService;

    public FormController(PdfService pdfService,
                          PdfRecordService pdfRecordService) {
        this.pdfService = pdfService;
        this.pdfRecordService = pdfRecordService;
    }

 // Step 1: After selecting form type
    @GetMapping("/create")
    public String createForm(@RequestParam("type") String type,
                             Authentication authentication,
                             Model model) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return "redirect:/login";
        }

        User user = details.getUser();

        FormType formType = FormType.valueOf(type);

        FormData formData = new FormData();
        formData.setFormType(formType.name());
        formData.setFormTypeLabel(formType.getLabel());

        // Subject fixed based on form type
        formData.setSubject(formType.getLabel());

        // Prefill from user
        formData.setStaffNo(user.getStaffNo());
        formData.setFullName(user.getFullName());
        formData.setDepartment(user.getDepartment());
        formData.setDesignation(user.getDesignation());
        formData.setEmail(user.getEmail());
        formData.setPhone(user.getPhone());

        model.addAttribute("formData", formData);
        return "form-create";
    }

    // Step 2: Preview after user fills form
    @PostMapping("/preview")
    public String previewForm(@ModelAttribute("formData") FormData formData,
                              Model model) {
        model.addAttribute("formData", formData);
        return "form-preview";
    }

    // Step 3: Download PDF after preview + save to history
    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadPdf(@ModelAttribute("formData") FormData formData,
                                              Authentication authentication) {

        byte[] pdfBytes = pdfService.generatePdf(formData);

        // save history only if logged in
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails details) {
            User user = details.getUser();
            pdfRecordService.saveRecord(user, formData, pdfBytes);
        }

        String fileName = formData.getFormType() + "_" + formData.getStaffNo() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // Download from history (only records of that user)
    @GetMapping("/history/download/{id}")
    public ResponseEntity<byte[]> downloadFromHistory(@PathVariable("id") Long id,
                                                      Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return ResponseEntity.status(401).build();
        }

        User user = details.getUser();
        PdfRecord record = pdfRecordService.getRecordForUser(id, user);

        String fileName = record.getFormType() + "_" + user.getStaffNo() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(record.getPdfData());
    }
}