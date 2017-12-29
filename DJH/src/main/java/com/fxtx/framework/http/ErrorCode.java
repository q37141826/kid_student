package com.fxtx.framework.http;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by z on 2015/9/15.
 */
public class ErrorCode {

    /**
     * 错误日志
     * @param e
     * @return
     */
   public static String error(Exception e){
       if (e == null)
           return "";
       e.printStackTrace();
       String error ="网络错误";
       if(e instanceof ConnectException)
           error = "连接服务器失败";
       else if(e instanceof SocketTimeoutException)
           error = "访问服务器超时";
       else if(e instanceof RuntimeException)
           error = "访问服务器失败";
       return error;
   }

}

