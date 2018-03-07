package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;
import java.util.Map;

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

    private String page_id;
    private Map<Integer, Integer> mScoreMap;

    /*课本剧*/
    public BeGoTextBookSuccess(String mineWorksTempPath, String page_id,
                               String mineWorksName, String userUrl,
                               String userName, String makeTime
    ) {
        this.mineWorksTempPath = mineWorksTempPath;
        this.page_id = page_id;
        this.mineWorksName = mineWorksName;
        this.userUrl = userUrl;
        this.userName = userName;
        this.makeTime = makeTime;

    }


    /*卡拉ok*/

//    public BeGoTextBookSuccess(String mineWorksTempPath, String page_id,
//                               String mineWorksName, String userUrl,
//                               String userName, String makeTime) {
//
//        this.mineWorksTempPath = mineWorksTempPath;
//        this.page_id = page_id;
//        this.mineWorksName = mineWorksName;
//        this.userUrl = userUrl;
//        this.userName = userName;
//        this.makeTime = makeTime;
//    }


    public Map<Integer, Integer> getmScoreMap() {
        return mScoreMap;
    }

    public void setmScoreMap(Map<Integer, Integer> mScoreMap) {
        this.mScoreMap = mScoreMap;
    }

    public String getPage_id() {
        return page_id;
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

    @Override
    public String toString() {
        return "BeGoTextBookSuccess{" +
                "mineWorksTempPath='" + mineWorksTempPath + '\'' +
                ", mineWorksName='" + mineWorksName + '\'' +
                ", userUrl='" + userUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", makeTime='" + makeTime + '\'' +
                ", page_id='" + page_id + '\'' +
                '}';
    }
}


