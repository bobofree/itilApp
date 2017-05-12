package DownloadVideo;

public class LoadInfo {
    private int fileSize;
    private int complete;
    private String url;

    public LoadInfo() {
    }

    public LoadInfo(int fileSize, int complete, String url) {
        this.fileSize = fileSize;
        this.complete = complete;
        this.url = url;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "LoadInfo{" +
                "fileSize=" + fileSize +
                ", complete=" + complete +
                ", url='" + url + '\'' +
                '}';
    }
}
