package cn.dajiahui.kid.ui.study.bean;

import java.util.List;

/**
 * Created by majin on 2018/1/26.
 * <p>
 * 随身听
 */

public class BePersonalStereo {
    private List<BePersonalStereoPageData> page_data;
    private String title;
    private String unit_id;

    public List<BePersonalStereoPageData> getPage_data() {
        return page_data;
    }

    public void setPage_data(List<BePersonalStereoPageData> page_data) {
        this.page_data = page_data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }
}
