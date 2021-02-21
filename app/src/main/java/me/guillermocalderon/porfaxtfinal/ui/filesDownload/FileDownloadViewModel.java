package me.guillermocalderon.porfaxtfinal.ui.filesDownload;

public class FileDownloadViewModel {

    private String fichName;
    private String fichLink;

    public FileDownloadViewModel(String fichName, String fichLink) {
        this.fichName = fichName;
        this.fichLink = fichLink;
    }

    public String getFichName() {
        return fichName;
    }

    public void setFichName(String fichName) {
        this.fichName = fichName;
    }

    public String getFichLink() {
        return fichLink;
    }

    public void setFichLink(String fichLink) {
        this.fichLink = fichLink;
    }
}
