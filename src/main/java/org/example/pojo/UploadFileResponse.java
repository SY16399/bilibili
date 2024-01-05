package org.example.pojo;
//文件实体类
public class UploadFileResponse {
    private String fileName;
    private String fileDownLoadUri;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, String fileDownLoadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownLoadUri = fileDownLoadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownLoadUri() {
        return fileDownLoadUri;
    }

    public void setFileDownLoadUri(String fileDownLoadUri) {
        this.fileDownLoadUri = fileDownLoadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
