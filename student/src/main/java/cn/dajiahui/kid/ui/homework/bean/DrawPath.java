package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.dajiahui.kid.controller.Constant;

/**
 * Created by lenovo on 2018/1/13.
 */

public class DrawPath implements Serializable {
    int pathColor;
    cn.dajiahui.kid.ui.homework.bean.Point leftPoint = Constant.PointZero;
    cn.dajiahui.kid.ui.homework.bean.Point rightPoint = Constant.PointZero;

    private Map<String, String> answerMap = new HashMap<>();


    /*记录坐标点*/
    public DrawPath(Point leftPoint, Point rightPoint) {
        this.leftPoint = leftPoint;
        this.rightPoint = rightPoint;

    }

    public Map<String, String> getAnswerMap() {
        return answerMap;
    }

    public void setAnswerMap(Map<String, String> answerMap) {
        this.answerMap = answerMap;
    }


    public int getPathColor() {
        return pathColor;
    }

    public void setPathColor(int pathColor) {
        this.pathColor = pathColor;
    }

    public cn.dajiahui.kid.ui.homework.bean.Point getLeftPoint() {
        return leftPoint;
    }

    public void setLeftPoint(cn.dajiahui.kid.ui.homework.bean.Point leftPoint) {
        this.leftPoint = leftPoint;
    }

    public cn.dajiahui.kid.ui.homework.bean.Point getRightPoint() {
        return rightPoint;
    }

    public void setRightPoint(cn.dajiahui.kid.ui.homework.bean.Point point) {
        this.rightPoint = point;
    }

    @Override
    public String toString() {
        return "DrawPath{" +
                "pathColor=" + pathColor +
                ", answerMap=" + answerMap +
                ", leftPoint=" + leftPoint +
                ", rightPoint=" + rightPoint +
                '}';
    }
}
