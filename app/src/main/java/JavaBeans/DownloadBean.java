package JavaBeans;

/**
 * Created by Bobofree on 2016/12/29.
 */
public class DownloadBean {
    private String name;
    private String downloadTime;
    private String downloadSize;

    public DownloadBean() {

    }

    public DownloadBean(String name, String downloadTime) {
        this.name = name;
        this.downloadTime = downloadTime;
    }

    public String getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(String downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
