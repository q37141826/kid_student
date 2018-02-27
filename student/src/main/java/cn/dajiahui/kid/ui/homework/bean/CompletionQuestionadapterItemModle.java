package cn.dajiahui.kid.ui.homework.bean;

/**
 * Created by lenovo on 2018/2/26.
 */

public class CompletionQuestionadapterItemModle {
    private String showItemright;//显示正确答案
    private String showItemMy;//显示我的答案

    public CompletionQuestionadapterItemModle(String showItemright, String showItemMy) {
        this.showItemright = showItemright;
        this.showItemMy = showItemMy;
    }

    public String getShowItemMy() {
        return showItemMy;
    }

    public void setShowItemMy(String showItemMy) {
        this.showItemMy = showItemMy;
    }


    public String getShowItemright() {
        return showItemright;
    }

    public void setShowItemright(String showItemright) {
        this.showItemright = showItemright;
    }
}
