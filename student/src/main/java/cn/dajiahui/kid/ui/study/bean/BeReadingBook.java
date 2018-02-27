package cn.dajiahui.kid.ui.study.bean;

import java.util.List;

/**
 * Created by mj on 2018/2/26.
 * <p>
 * 点读本
 */

public class BeReadingBook {

    private List<BeReadingBookPageData> page_data;
    private String title;
    private String unit_id;

    public List<BeReadingBookPageData> getPage_data() {
        return page_data;
    }

    public void setPage_data(List<BeReadingBookPageData> page_data) {
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

    @Override
    public String toString() {
        return "BeReadingBook{" +
                "page_data=" + page_data +
                ", title='" + title + '\'' +
                ", unit_id='" + unit_id + '\'' +
                '}';
    }
}
