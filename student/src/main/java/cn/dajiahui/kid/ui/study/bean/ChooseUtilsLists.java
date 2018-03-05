package cn.dajiahui.kid.ui.study.bean;

import java.util.List;

/**
 * Created by mj on 2018/2/23.
 */

public class ChooseUtilsLists {
    private String id;
    private String name;
    private String seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "ChooseUtilsLists{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", seq='" + seq + '\'' +
                '}';
    }

    /**
     * Created by lenovo on 2018/2/27.
     */

    public static class BeTextBookDrama {

        private List<BeTextBookDramaItem> item;
        private String music_name;
        private String music_oss_name;
        private String page_id;
        private String page_no;
        private String page_url;
        private String title;

        public List<BeTextBookDramaItem> getItem() {
            return item;
        }

        public void setItem(List<BeTextBookDramaItem> item) {
            this.item = item;
        }

        public String getMusic_name() {
            return music_name;
        }

        public void setMusic_name(String music_name) {
            this.music_name = music_name;
        }

        public String getMusic_oss_name() {
            return music_oss_name;
        }

        public void setMusic_oss_name(String music_oss_name) {
            this.music_oss_name = music_oss_name;
        }

        public String getPage_id() {
            return page_id;
        }

        public void setPage_id(String page_id) {
            this.page_id = page_id;
        }

        public String getPage_no() {
            return page_no;
        }

        public void setPage_no(String page_no) {
            this.page_no = page_no;
        }

        public String getPage_url() {
            return page_url;
        }

        public void setPage_url(String page_url) {
            this.page_url = page_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "BeMineWorks{" +
                    "item=" + item +
                    ", music_name='" + music_name + '\'' +
                    ", music_oss_name='" + music_oss_name + '\'' +
                    ", page_id='" + page_id + '\'' +
                    ", page_no='" + page_no + '\'' +
                    ", page_url='" + page_url + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    /**
     * Created by lenovo on 2018/2/27.
     */

    public static class BeTextBookDramaItem {
        private String chinese;
        private String english;
        private String tag_index;
        private String time_end;
        private String time_start;

        public String getChinese() {
            return chinese;
        }

        public void setChinese(String chinese) {
            this.chinese = chinese;
        }

        public String getEnglish() {
            return english;
        }

        public void setEnglish(String english) {
            this.english = english;
        }

        public String getTag_index() {
            return tag_index;
        }

        public void setTag_index(String tag_index) {
            this.tag_index = tag_index;
        }

        public String getTime_end() {
            return time_end;
        }

        public void setTime_end(String time_end) {
            this.time_end = time_end;
        }

        public String getTime_start() {
            return time_start;
        }

        public void setTime_start(String time_start) {
            this.time_start = time_start;
        }

        @Override
        public String toString() {
            return "BeTextBookDramaItem{" +
                    "chinese='" + chinese + '\'' +
                    ", english='" + english + '\'' +
                    ", tag_index='" + tag_index + '\'' +
                    ", time_end='" + time_end + '\'' +
                    ", time_start='" + time_start + '\'' +
                    '}';
        }
    }
}
