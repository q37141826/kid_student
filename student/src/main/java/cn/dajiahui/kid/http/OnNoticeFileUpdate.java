package cn.dajiahui.kid.http;

import java.util.ArrayList;

import cn.dajiahui.kid.ui.album.bean.BePhoto;

public interface OnNoticeFileUpdate extends OnFileUpdate {
    void saveFile(ArrayList<BePhoto> photos);
}