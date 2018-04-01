package cn.dajiahui.kid.ui.homework.myinterface;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionadapterItemModle;

/**
 * Created by lenovo on 2018/1/18.
 */

/*保存editext输入值的接口*/
public interface SubmitEditext {
    void submitEditextInfo(int selfposition, LinkedHashMap<Integer,
            CompletionQuestionadapterItemModle> inputContainer, int position, String itemValue);
}
