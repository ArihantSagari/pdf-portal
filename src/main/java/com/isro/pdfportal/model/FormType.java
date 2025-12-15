
package com.isro.pdfportal.model;

public enum FormType {
    LEAVE_APPLICATION("Leave Application"),
    TRAVEL_ALLOWANCE("Travel Allowance Claim"),
    MEDICAL_REIMBURSEMENT("Medical Reimbursement"),
    TRAINING_NOMINATION("Training Nomination Form"),
    ASSET_DECLARATION("Annual Asset Declaration"),
    NOC_HIGHER_STUDIES("NOC for Higher Studies"),
    DUTY_CHANGE_REQUEST("Duty Change Request"),
    ID_CARD_REISSUE("ID Card Reissue Request"),
    QUARTER_ALLOTMENT("Quarter Allotment Request"),
    IT_DECLARATION("Income Tax Declaration Form");

    private final String label;

    FormType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
