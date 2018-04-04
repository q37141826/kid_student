package com.fxtx.framework.updata;


/*版本更新计数*/
public class Appupdate {


    private static Appupdate appflag;

    private Appupdate() {

    }

    //静态单利模式
    public static Appupdate getAppupdate() {

        if (appflag == null) {

            appflag = new Appupdate();
        }
        return appflag;
    }

    public static int activityCount = 0;//版本更新时的计数
}
