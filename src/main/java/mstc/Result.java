package mstc;

public class Result {

    boolean revoked;
    String serialNumber;
    String crlUrl;
    java.util.Date revocationDate;
    String subjectName;

    public boolean isRevoked() {
        return revoked;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getCrlUrl() {
        return crlUrl;
    }

    public java.util.Date getRevocationDate() {
        return revocationDate;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
}