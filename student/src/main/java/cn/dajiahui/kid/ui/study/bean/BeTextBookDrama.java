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
    private List<BeTextBookDramaPageData> page_data;
    private String unit_id;
    private String title;


    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BeTextBookDramaPageData> getPage_data() {
        return page_data;
    }

    public void setPage_data(List<BeTextBookDramaPageData> page_data) {
        this.page_data = page_data;
    }
}
