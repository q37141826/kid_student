package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;

/**
 * Created by mj on 2018/2/3.
 * <p>
 * 去课本成功的数据
 */

public class BeGoTextBookSuccess implements Serializable {


    private String mineWorksTempPath;
    private String mineWorksName;
    private String userUrl;
    private String userName;
    private String makeTime;
    private String score;

    /*课本剧*/
    public BeGoTextBookSuccess(String mineWorksTempPath, String mineWorksName, String userUrl, String userName, String makeTime, String score) {
        this.mineWorksTempPath = mineWorksTempPath;
        this.mineWorksName = mineWorksName;
        this.userUrl = userUrl;
        this.userName = userName;
        this.makeTime = makeTime;
        this.score = score;
    }


    /*卡拉ok*/

    public BeGoTextBookSuccess(String mineWorksTempPath, String mineWorksName, String userUrl, String userName, String makeTime) {
        this.mineWorksTempPath = mineWorksTempPath;
        this.mineWorksName = mineWorksName;
        this.userUrl = userUrl;
        this.userName = userName;
        this.makeTime = makeTime;
    }

    public String getMineWorksTempPath() {
        return mineWorksTempPath;
    }

    public String getMineWorksName() {
        return mineWorksName;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getMakeTime() {
        return makeTime;
    }

    public String getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "BeGoTextBookSuccess{" +
                "mineWorksPath='" + mineWorksTempPath + '\'' +
                ", mineWorksName='" + mineWorksName + '\'' +
                ", userUrl='" + userUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", makeTime='" + makeTime + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}


