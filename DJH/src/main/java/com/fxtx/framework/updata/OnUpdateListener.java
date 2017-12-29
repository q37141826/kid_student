package com.fxtx.framework.updata;

/**
 * @ClassName: OnUpdateInterfaces.java
 * @author djh-zy
 * @Date 2015年5月19日 下午1:57:34
 * @Description: 版本更新回调
 */
public interface OnUpdateListener {
	/** 下载完成 执行安装时才会调用*/
	public void onUpdateSuccess();// 更新成功
	/**更新中发送错误 */
	public void onUpdateError(String error);// 更新错误
	/** 取消更新 等级  1：关闭app,2 不关闭app ，0 无更新*/
	public void onUpdateCancel(int type);//取消更新
}
