package cn.dajiahui.kid.ui.study.kinds.practice.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeChoiceOptions;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.study.kinds.practice.ExChoiceFragment;

/**
 * 选择题
 */
public class ApExChoice extends BaseAdapter {
    private int selectorPosition = -1;

    private List<BeChoiceOptions> mPptions;
    private Context context;
    private LayoutInflater mInflater;
    private ChoiceQuestionModle inbasebean;
    private Map<Integer, ShowAnswer> posttionMap = new HashMap<>();


    /*is_answer=0*/
    public ApExChoice(Context context, List<BeChoiceOptions> mPptions, ChoiceQuestionModle inbasebean) {
        this.mPptions = mPptions;
        this.context = context;
        this.inbasebean = inbasebean;
        mInflater = LayoutInflater.from(context);
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
                holder.img_rightchoice = (ImageView) convertView.findViewById(R.id.img_rightchoice);
                holder.tv_answer = (TextView) convertView.findViewById(R.id.tv_answer);
                holder.choice_root = (RelativeLayout) convertView.findViewById(R.id.choice_root);

            } else {//图片答案
                convertView = mInflater.inflate(R.layout.item_choicepic, null);
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
            posttionMap.put(position, new ShowAnswer(position, holder.img_rightchoice));
            convertView.setTag(holder);
        } else {
            //通过调用缓冲视图convertView，然后就可以调用到viewHolder,viewHolder中已经绑定了各个控件，省去了findViewById的步骤
            holder = (ViewHolder) convertView.getTag();
        }
        if (inbasebean.isAnswer() == false) {
            //如果当前的position等于传过来点击的position,就去改变他的状态
            if (selectorPosition == position) {

                holder.choice_root.setBackgroundResource(R.drawable.select_judge_image);
            } else {
                //其他的恢复原来的状态
                holder.choice_root.setBackgroundResource(R.drawable.noselect_judge_image);
            }
        } else {
            if (mPptions.get(position).getVal().equals(inbasebean.getMy_answer())) {
                /*我的答案加黄色边框*/
                holder.choice_root.setBackgroundResource(R.drawable.select_judge_image);
//                /*判断自己的答案与参考答案是否相同  相同 当前view 加绿色对号  不相同就红色×*/
                if (inbasebean.getMy_answer().equals(inbasebean.getStandard_answer())) {
                    holder.img_rightchoice.setImageResource(R.drawable.answer_true);
                } else {
                    holder.img_rightchoice.setImageResource(R.drawable.answer_false);
                    /*找出正确答案的item   把正确答案的item画个绿色对勾*/
                }
            } else {

                /*判断当前条目是是不是自己选的答案  是加绿色对号  不相同就红色× */
                /*获取当前条目的答案*/
                if (mPptions.get(position).getVal().equals(inbasebean.getStandard_answer())) {
                    holder.img_rightchoice.setImageResource(R.drawable.answer_true);
                }
            }
        }

        return convertView;
    }


    /*改变当前选择item的状态*/
    public void changeState(Context context, ExChoiceFragment.SubmitChoiseFragment submit, int pos, ChoiceQuestionModle inbasebean) {
         /*未作答情况下可以点击*/
        if (inbasebean.isAnswer() == false) {
            selectorPosition = pos;
            inbasebean.setAnswerflag("true");//学生作答标记
            inbasebean.setMy_answer(mPptions.get(pos).getVal());//学生作答答案
//            inbasebean.setChoiceitemposition(pos);//保存选择题答案的索引（用于翻页回来后给选择的条目赋予背景颜色）
//            inbasebean.setChoiceanswer((pos + 1) + "");//因为pos是从0开始
//            if (mPptions.get(pos).getVal().equals(inbasebean.getStandard_answer())) {
//                inbasebean.setIs_right("1");/*回答正确*/
//            } else {
//                inbasebean.setIs_right("0");/*回答错误*/
//            }
            notifyDataSetChanged();
            submit.submitChoiceFragment(inbasebean);
        }
    }


    @SuppressLint("ResourceAsColor")
    public void changeitemState(ChoiceQuestionModle inbasebean) {
        this.inbasebean = inbasebean;
        notifyDataSetChanged();
    }

    class ViewHolder {
        public ImageView img_answer;
        public TextView tv_answer;
        public ImageView img_rightchoice;
        public RelativeLayout choice_root;
    }

    class ShowAnswer {
        int position;
        ImageView img_rightchoice;

        public ShowAnswer(int position, ImageView img_rightchoice) {
            this.position = position;
            this.img_rightchoice = img_rightchoice;
        }

        public int getPosition() {
            return position;
        }

//        public ImageView getImg_rightchoice() {
//            return img_rightchoice;
//        }

//        @Override
//        public String toString() {
//            return "ShowAnswer{" +
//                    "position=" + position +
//                    ", img_rightchoice=" + img_rightchoice +
//                    '}';
//        }
    }

}