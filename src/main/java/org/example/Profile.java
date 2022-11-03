package org.example;

public class Profile {
    private  String Country;
    private String fullName;
    private String sex;
    private String birthDate;
    private String citizenship;
    private String documentType;
    private String issuingCountryOrOrganization;
    private String reasonOfcheck;
    private String docNumber;
    private String validity;

    private UserStep userStep;

    public UserStep getUserStep() {
        return userStep;
    }

    public void setUserStep(UserStep userStep) {
        this.userStep = userStep;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getIssuingCountryOrOrganization() {
        return issuingCountryOrOrganization;
    }

    public void setIssuingCountryOrOrganization(String issuingCountryOrOrganization) {
        this.issuingCountryOrOrganization = issuingCountryOrOrganization;
    }

    public String getReasonOfcheck() {
        return reasonOfcheck;
    }

    public void setReasonOfcheck(String reasonOfcheck) {
        this.reasonOfcheck = reasonOfcheck;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }
}
