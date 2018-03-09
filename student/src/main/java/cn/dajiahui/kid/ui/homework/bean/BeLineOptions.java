package cn.dajiahui.kid.ui.homework.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/1/10.
 * 解析 连线题 Options
 */

public class BeLineOptions  implements Serializable {
    private List<BeLineLeft> left;
    private List<BeLineRight> right;

    public List<BeLineLeft> getLeft() {
        return left;
    }

    public void setLeft(List<BeLineLeft> left) {
        this.left = left;
    }

    public List<BeLineRight> getRight() {
        return right;
    }

    public void setRight(List<BeLineRight> right) {
        this.right = right;
    }
}
