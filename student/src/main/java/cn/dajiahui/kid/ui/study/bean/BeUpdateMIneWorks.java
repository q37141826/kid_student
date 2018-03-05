package cn.dajiahui.kid.ui.study.bean;

/**
 * Created by mj on 2018/3/5.
 * <p>
 * 解析我的作品
 */

public class BeUpdateMIneWorks {
    private String date;
    private String shareUrl;
    private String thumbnail;
    private String video;

    public String getDate() {
        return date;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getVideo() {
        return video;
    }

    @Override
    public String toString() {
        return "BeUpdateMIneWorks{" +
                "date='" + date + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", video='" + video + '\'' +
                '}';
    }
}
