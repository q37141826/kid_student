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
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.homeworkdetails.ChoiceFragment;

/**
 * 选择题
 */
public class ApChoice extends BaseAdapter {
    private int selectorPosition = -1;

    private List<BeChoiceOptions> mPptions;
    private Context context;
    private LayoutInflater mInflater;


    public ApChoice(Context context, List<BeChoiceOptions> mPptions) {
        this.mPptions = mPptions;
        this.context = context;
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
//            Logger.d("majin","mPptions.get(position).getType():"+mPptions.get(position).getType());
            /*区别答案的类型*/
            if (mPptions.get(position).getType().equals("1")) {//文字答案
            convertView = mInflater.inflate(R.layout.item_choicetext, null);
            //把convertView中的控件保存到viewHolder中
            holder.img_choice = (ImageView) convertView.findViewById(R.id.img_choice);
            holder.img_rightchoice = (ImageView) convertView.findViewById(R.id.img_rightchoice);
            holder.tv_answer = (TextView) convertView.findViewById(R.id.tv_answer);
            holder.choice_root = (RelativeLayout) convertView.findViewById(R.id.choice_root);

            }

            else {//图片答案
                convertView = mInflater.inflate(R.layout.item_choicepic, null);
                //把convertView中的控件保存到viewHolder中
                holder.img_choice = (ImageView) convertView.findViewById(R.id.img_choice);
                //item右边的对勾控件
                holder.img_rightchoice = (ImageView) convertView.findViewById(R.id.img_rightchoice);
                holder.img_answer = (ImageView) convertView.findViewById(R.id.img_answer);
                holder.choice_root = (RelativeLayout) convertView.findViewById(R.id.choice_root);
            }

            if (mPptions.get(position).getType().equals("1")) {//文字答案
                holder.tv_answer.setText(mPptions.get(position).getLabel());

            }

            else {
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
            holder.img_choice.setImageResource(R.drawable.ico_im_ok);
        } else {
            //其他的恢复原来的状态
            holder.img_choice.setImageResource(R.drawable.ico_im_not);
        }

        return convertView;
    }


    public void changeState(Context context, ChoiceFragment.SubmitChoiseFragment submit, int pos, ChoiceQuestionModle inbasebean) {
        selectorPosition = pos;
        Toast.makeText(context, "选择：" + mPptions.get(pos).getLabel(), Toast.LENGTH_SHORT).show();
        inbasebean.setAnswerflag("true");//学生作答标记
        inbasebean.setChoiceanswer(mPptions.get(pos).getLabel());
        inbasebean.setSubmitAnswer(mPptions.get(pos).getLabel());//学生做答案
        inbasebean.setChoiceitemposition(pos);//保存选择题答案的索引（用于翻页回来后给选择的条目赋予背景颜色）
        submit.submitChoiceFragment(inbasebean);

        notifyDataSetChanged();
    }

    /*点击check后调用*/
    @SuppressLint("ResourceAsColor")
    public void changeitemState(QuestionModle questionModle, int posi, ListView listView) {

        ViewHolder holder = null;
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();

        /* item 学生作答正确  正确答案 画绿色背景    其余错误的答案画红色背景 */
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
            View view = listView.getChildAt(posi - visibleFirstPosi);
            holder = (ViewHolder) view.getTag();
            holder.img_choice.setImageResource(R.drawable.ico_im_ok);
            //            holder.choice_root.setBackgroundColor();//给正确答案外边画个框框
        } else {
            holder.img_choice.setImageResource(R.drawable.ico_im_not);
        }

    }


    class ViewHolder {
        public ImageView img_choice;
        public ImageView img_answer;
        public TextView tv_answer;
        public ImageView img_rightchoice;
        public RelativeLayout choice_root;
    }


}