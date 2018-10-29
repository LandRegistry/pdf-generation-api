package pdfgenerator.models;


public class StorageResult {

    private String documentUrl;
    private String externalUrl;

    public StorageResult(String documentUrl, String externalUrl) {
        this.documentUrl = documentUrl;
        this.externalUrl = externalUrl;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }
}
