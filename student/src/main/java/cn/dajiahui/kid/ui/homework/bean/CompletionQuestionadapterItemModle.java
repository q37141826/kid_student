package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/2/26.
 */

public class CompletionQuestionadapterItemModle  implements Serializable {
    private String showItemright;//显示正确答案
    private String showItemMy;//显示我的答案
    private int showItemRightColor = -1;//0正确 绿色 1 错误 红色

    public CompletionQuestionadapterItemModle(String showItemright, String showItemMy) {
        this.showItemright = showItemright;
        this.showItemMy = showItemMy;
    }

    public int getShowItemRightColor() {
        return showItemRightColor;
    }

    public void setShowItemRightColor(int showItemRightColor) {
        this.showItemRightColor = showItemRightColor;
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

    @Override
    public String toString() {
        return "CompletionQuestionadapterItemModle{" +
                "showItemright='" + showItemright + '\'' +
                ", showItemMy='" + showItemMy + '\'' +
                ", showItemRightColor=" + showItemRightColor +
                '}';
    }
}
