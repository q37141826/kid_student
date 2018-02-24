package cn.dajiahui.kid.ui.homework.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.SubmitEditext;
import cn.dajiahui.kid.util.Logger;

/*填空题 横划listview适配器*/
public class HorizontallListViewAdapter extends BaseAdapter {

    private Context mContext;

    private final SubmitEditext submitEditext;
    private MyFoucus myFoucus;
    private EditChangedListener editChangedListener;//editext监听器
    public Map<Integer, String> inputContainer = new HashMap();//存editext的集合

    //    private Map<Integer, CompletionQuestionModle> mList;//数据源
    private int letterNum;//每个横划listview的item的数量
    private int selfposition;//HorizontallList在碎片中的索引（用于取出当前的HorizontallList）
    private String haveFocus = "";//用于网络请求后清空editext所有焦点
    public String IsShowRightAnswer = "";//是否显示editext

    public void setmList(Map<Integer, CompletionQuestionModle> mList) {
//        this.mList = mList;
        this.IsShowRightAnswer = "yes";
        haveFocus = "false";
    }

    /*获取答案的集合*/
    public Map getInputContainer() {
        return inputContainer;
    }

    public void setInputContainer(Map<Integer, String> inputContainer) {
        if (inputContainer != null) {
            if (this.inputContainer == null) {
                this.inputContainer = new HashMap();
            }
            for (int a = 0; a < inputContainer.size(); a++) {
                this.inputContainer.put(a, inputContainer.get(a));
            }
            Logger.d(this.inputContainer.toString());
        }
        notifyDataSetChanged();
    }


    public HorizontallListViewAdapter(Context context, SubmitEditext submitEditext, int selfposition, Map<Integer, String> inputContainer, int letterNum, CompletionQuestionModle inbasebean) {
        this.mContext = context;
        this.letterNum = letterNum;
        this.submitEditext = submitEditext;
        this.selfposition = selfposition;
        if (inputContainer != null) {
            for (int a = 0; a < inputContainer.size(); a++) {
                this.inputContainer.put(a, inputContainer.get(a));
            }
        }

        this.haveFocus = inbasebean.getIsFocusable();
        this.IsShowRightAnswer = inbasebean.getIsShowRightAnswer();
        myFoucus = new MyFoucus();
        editChangedListener = new EditChangedListener();

    }

    @Override
    public int getCount() {
        return letterNum;
    }

    @Override
    public Object getItem(int position) {
        return letterNum;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /*不用优化 要获取每个editext的实例*/
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderView holderView = null;

        if (convertView == null) {

            holderView = new HolderView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.match_league_round_item, parent, false);

            holderView.editext = (EditText) convertView.findViewById(R.id.editext);
            holderView.tv_rightanswer = (TextView) convertView.findViewById(R.id.tv_rightanswer);
            holderView.tv_num = (TextView) convertView.findViewById(R.id.tv_num);

            if (position == 0) {
                holderView.tv_num.setVisibility(View.VISIBLE);
                holderView.tv_num.setText((this.selfposition + 1) + ".");
            } else {
                holderView.tv_num.setVisibility(View.GONE);
            }

            // 注册上自己写的焦点监听
            holderView.editext.setOnFocusChangeListener(myFoucus);

            convertView.setTag(holderView);

        } else {
            holderView = (HolderView) convertView.getTag();

        }

        // setTag是个好东西呀，把position放上去，一会用
        holderView.editext.setTag(position);


        View currentFocus = ((Activity) mContext).getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }


        holderView.editext.removeTextChangedListener(editChangedListener);

        if (this.inputContainer.containsKey(position)) {
            if (this.inputContainer.get(position) != null) {
                holderView.editext.setText(this.inputContainer.get(position).toString());
            }
        } else {
            holderView.editext.setText("");
        }


        if (IsShowRightAnswer.equals("no")) {
            /*不显示正确答案*/
            holderView.tv_rightanswer.setText("");

        } else if (IsShowRightAnswer.equals("yes")) {
//              /*显示正确答案*/
//            holderView.textView.setText(mList.get(position).getAnalysisAnswer());
//            /*字颜色是绿色*/
////            holderView.textView.setTextColor( );
//            holderView.editext.setText(mList.get(position).getAnalysisMineAnswer());
//
//               /*更改边框颜色*/
//            if (mList.get(position).getTextcolor().equals("green")) {
//
//                holderView.editext.setBackgroundResource(R.drawable.select_completion_editext);
//            } else {
//                holderView.editext.setBackgroundResource(R.drawable.select_judge_image);
//
//            }
        }


        /*清空焦点*/
        if (haveFocus.equals("false")) {
            holderView.editext.setClickable(false);
            holderView.editext.setFocusable(false);
        }
        holderView.editext.addTextChangedListener(editChangedListener);

        return convertView;
    }

    class HolderView {
        EditText editext;
        TextView tv_rightanswer;
        TextView tv_num;
    }

    class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        private final int charMaxNum = 1;
        private EditText editText;
        private int position;


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            temp = s;

        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            inputContainer.put(position, s.toString());
//            /*得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
            editStart = this.editText.getSelectionStart();
            editEnd = this.editText.getSelectionEnd();

            if (temp.length() > charMaxNum) {
//                Toast.makeText(mContext, "你输入的字数已经超过了限制！", Toast.LENGTH_LONG).show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                if (!s.equals("")) {
                    this.editText.setText(s);
                    submitEditext.submitEditextInfo(selfposition);
                }
                this.editText.setSelection(tempSelection);
            }

        }
    }

    class MyFoucus implements View.OnFocusChangeListener {
        // 当获取焦点时修正myWatch中的position值,这是最重要的一步!
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                int position = (int) v.getTag();

                editChangedListener.position = position;
                editChangedListener.editText = (EditText) v;
            }
        }
    }


}
