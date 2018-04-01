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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeChoiceOptions;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.homeworkdetails.ChoiceFragment;

/**
 * 选择题
 */
public class ApChoice extends BaseAdapter {
    private int selectorPosition = -1;

    private List<BeChoiceOptions> mPptions;
    private Context context;
    private LayoutInflater mInflater;
    private ChoiceQuestionModle inbasebean;
    private Map<Integer, ShowAnswer> posttionMap = new HashMap<>();


    /*is_answer=0*/
    public ApChoice(Context context, List<BeChoiceOptions> mPptions, ChoiceQuestionModle inbasebean) {
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
                holder.tv_choice_text = (TextView) convertView.findViewById(R.id.tv_choice_text);
                holder.choice_root = (RelativeLayout) convertView.findViewById(R.id.choice_root);

            } else {//图片答案
                convertView = mInflater.inflate(R.layout.item_choicepic, null);
                holder.img_rightchoice = (ImageView) convertView.findViewById(R.id.img_rightchoice);
                holder.img_answer = (ImageView) convertView.findViewById(R.id.img_answer);
                holder.choice_root = (RelativeLayout) convertView.findViewById(R.id.choice_root);
                holder.tv_choice_pic = (TextView) convertView.findViewById(R.id.tv_choice_pic);
            }

            if (mPptions.get(position).getType().equals("2")) {//文字答案
                holder.tv_answer.setText(mPptions.get(position).getContent());

            } else {
                Glide.with(context).load(mPptions.get(position).getContent()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_answer);

            }
            posttionMap.put(position, new ShowAnswer(position, holder.img_rightchoice));
            convertView.setTag(holder);
        } else {
            //通过调用缓冲视图convertView，然后就可以调用到viewHolder,viewHolder中已经绑定了各个控件，省去了findViewById的步骤
            holder = (ViewHolder) convertView.getTag();
        }

        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == position) {
            holder.choice_root.setBackgroundResource(R.drawable.select_judge_image);
        } else {
            //其他的恢复原来的状态
            holder.choice_root.setBackgroundResource(R.drawable.noselect_judge_image);
        }

        /*添加遮罩 首先判断是否完成*/
        if (inbasebean.getIs_complete().equals("1")) {
            /*我的答案有 ㊒ 就认为是未作答 直接显示正确答案 */
            if (inbasebean.getMy_answer().equals("㊒")) {
                /*判断当前条目是是不是自己选的答案  是加绿色对号  不相同就红色× */
                /*获取当前条目的答案*/
                if (mPptions.get(position).getVal().equals(inbasebean.getStandard_answer())) {
                    holder.img_rightchoice.setImageResource(R.drawable.answer_true);
                }

            } else {

                if (mPptions.get(position).getVal().equals(inbasebean.getMy_answer())) {
                    /*我的答案加黄色边框*/
                    holder.choice_root.setBackgroundResource(R.drawable.select_judge_image);
                    /*判断自己的答案与参考答案是否相同  相同 当前view 加绿色对号  不相同就红色×*/
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
        }
        return convertView;
    }


    /*改变当前选择item的状态*/
    public void changeState(Context context, ChoiceFragment.SubmitChoiseFragment submit, int pos, ChoiceQuestionModle inbasebean) {
        selectorPosition = pos;
        inbasebean.setAnswerflag("true");//学生作答标记
        inbasebean.setMy_answer(mPptions.get(pos).getVal());//学生作答答案
        inbasebean.setChoiceitemposition(pos);//保存选择题答案的索引（用于翻页回来后给选择的条目赋予背景颜色）

        /*回答正确*/
        if (mPptions.get(pos).getVal().equals(inbasebean.getStandard_answer())) {
            inbasebean.setIs_right("1");
        } else {/*回答错误*/
            inbasebean.setIs_right("0");
        }
        submit.submitChoiceFragment(inbasebean);
        if (inbasebean.getIs_answered().equals("0")) {

            notifyDataSetChanged();
        }
    }


    @SuppressLint("ResourceAsColor")
    public void changeitemState(int posi, ListView listView, ChoiceQuestionModle inbasebean) {

        ViewHolder holder = null;
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (inbasebean.getIs_answered().equals("0")) {
            /* item 学生作答正确  正确答案 画黄色背景    其余错误的答案画红色背景 */
            if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
                View view = listView.getChildAt(posi - visibleFirstPosi);
                holder = (ViewHolder) view.getTag();

                holder.choice_root.setBackgroundResource(R.drawable.select_judge_image);//给正确答案外边画个框框

            } else {
                holder.choice_root.setBackgroundResource(R.drawable.noselect_judge_image);//给正确答案外边画个框框
            }
        }
    }

    class ViewHolder {
        public ImageView img_answer;
        public TextView tv_answer, tv_choice_text, tv_choice_pic;
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

        public ImageView getImg_rightchoice() {
            return img_rightchoice;
        }


    }

}