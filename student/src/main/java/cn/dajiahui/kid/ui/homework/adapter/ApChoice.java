package cn.dajiahui.kid.ui.homework.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeChoiceOptions;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.homeworkdetails.ChoiceFragment;
import cn.dajiahui.kid.util.Logger;

/**
 * 选择题
 */
public class ApChoice extends BaseAdapter {
    private int selectorPosition = -1;

    private List<BeChoiceOptions> mPptions;
    private Context context;
    private LayoutInflater mInflater;
    private ChoiceQuestionModle inbasebean;
    int currentposition = -1;

    /*is_answer=0*/
    public ApChoice(Context context, List<BeChoiceOptions> mPptions, ChoiceQuestionModle inbasebean) {
        this.mPptions = mPptions;
        this.context = context;
        this.inbasebean = inbasebean;
        mInflater = LayoutInflater.from(context);
    }

    /*is_answer=1*/
    public ApChoice(Context context, List<BeChoiceOptions> mPptions, ChoiceQuestionModle inbasebean, int currentposition) {
        this.mPptions = mPptions;
        this.context = context;
        this.currentposition = currentposition;
        this.inbasebean = inbasebean;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mPptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mPptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            /*区别答案的类型*/
            if (mPptions.get(position).getType().equals("2")) {//文字答案
                convertView = mInflater.inflate(R.layout.item_choicetext, null);
                //把convertView中的控件保存到viewHolder中
//            holder.img_choice = (ImageView) convertView.findViewById(R.id.img_choice);
                holder.img_rightchoice = (ImageView) convertView.findViewById(R.id.img_rightchoice);
                holder.tv_answer = (TextView) convertView.findViewById(R.id.tv_answer);
                holder.choice_root = (RelativeLayout) convertView.findViewById(R.id.choice_root);

            } else {//图片答案
                convertView = mInflater.inflate(R.layout.item_choicepic, null);
                //把convertView中的控件保存到viewHolder中
//                holder.img_choice = (ImageView) convertView.findViewById(R.id.img_choice);
                //item右边的对勾控件
                holder.img_rightchoice = (ImageView) convertView.findViewById(R.id.img_rightchoice);
                holder.img_answer = (ImageView) convertView.findViewById(R.id.img_answer);
                holder.choice_root = (RelativeLayout) convertView.findViewById(R.id.choice_root);
            }

            if (mPptions.get(position).getType().equals("2")) {//文字答案
                holder.tv_answer.setText(mPptions.get(position).getContent());

            } else {
                Glide.with(context)
                        .load(mPptions.get(position).getContent())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.img_answer);
            }

            convertView.setTag(holder);
        } else {
            //通过调用缓冲视图convertView，然后就可以调用到viewHolder,viewHolder中已经绑定了各个控件，省去了findViewById的步骤
            holder = (ViewHolder) convertView.getTag();
        }

        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == position) {
//            holder.img_choice.setImageResource(R.drawable.ico_im_ok);
            holder.choice_root.setBackgroundResource(R.drawable.select_judge_image);
        } else {
            //其他的恢复原来的状态
//            holder.img_choice.setImageResource(R.drawable.ico_im_not);
            holder.choice_root.setBackgroundResource(R.drawable.noselect_judge_image);
        }


        if (inbasebean.getIs_answer().equals("1")) {
            Logger.d("");
            if (currentposition == position) {
                   /*正確的*/
                holder.choice_root.setBackgroundResource(R.drawable.select_judge_image);
            } else {
                /*错误的*/
                holder.choice_root.setBackgroundResource(R.drawable.noselect_judge_image);
            }

        }

        return convertView;
    }


    /*改变当前选择item的状态*/
    public void changeState(Context context, ChoiceFragment.SubmitChoiseFragment submit, int pos, ChoiceQuestionModle inbasebean) {
        selectorPosition = pos;
        Toast.makeText(context, "选择：" + mPptions.get(pos).getLabel(), Toast.LENGTH_SHORT).show();

        inbasebean.setAnswerflag("true");//学生作答标记
        inbasebean.setMy_answer(mPptions.get(pos).getVal());//学生作答答案
        inbasebean.setChoiceitemposition(pos);//保存选择题答案的索引（用于翻页回来后给选择的条目赋予背景颜色）

        inbasebean.setChoiceanswer((pos + 1) + "");//因为pos是从0开始
        /*回答正确*/
        if (mPptions.get(pos).getVal().equals(inbasebean.getStandard_answer())) {
            inbasebean.setIs_right("1");
        } else {/*回答错误*/
            inbasebean.setIs_right("0");
        }
        submit.submitChoiceFragment(inbasebean);
        notifyDataSetChanged();
    }


    @SuppressLint("ResourceAsColor")
    public void changeitemState(int posi, ListView listView) {

        ViewHolder holder = null;
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();

        /* item 学生作答正确  正确答案 画黄色背景    其余错误的答案画红色背景 */
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
            View view = listView.getChildAt(posi - visibleFirstPosi);
            holder = (ViewHolder) view.getTag();

            holder.choice_root.setBackgroundResource(R.drawable.select_judge_image);//给正确答案外边画个框框

        } else {

            holder.choice_root.setBackgroundResource(R.drawable.noselect_judge_image);//给正确答案外边画个框框

        }


    }

    class ViewHolder {
        public ImageView img_answer;
        public TextView tv_answer;
        public ImageView img_rightchoice;
        public RelativeLayout choice_root;
    }


}