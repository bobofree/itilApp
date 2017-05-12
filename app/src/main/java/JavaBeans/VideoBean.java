package JavaBeans;

import java.io.Serializable;

/**
 * Created by Bobo on 2016/8/4.
 */
public class VideoBean implements Serializable{
    private int id;
    private int viewers;
    private String name;
    private String datetime;
    private String size;
    private String img_name;

    public VideoBean() {
    }

    public VideoBean(int id, String name, String size, String datetime, int viewers, String img_name) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
        this.viewers = viewers;
        this.img_name = img_name;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViewers() {
        return viewers;
    }

    public void setViewers(int viewers) {
        this.viewers = viewers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
