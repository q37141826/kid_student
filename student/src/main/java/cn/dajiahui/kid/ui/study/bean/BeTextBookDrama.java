package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mj on 2018/1/31.
 * <p>
 * <p>
 * 课本剧
 */

public class BeTextBookDrama implements Serializable {

    public BeTextBookDrama(String title, String video_url) {
        this.title = title;
        this.video_url = video_url;
    }

    private String title;
    private String video_url;
    private List<BeTextBookDramaoptions> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public List<BeTextBookDramaoptions> getList() {
        return list;
    }

    public void setList(List<BeTextBookDramaoptions> list) {
        this.list = list;
    }
}
