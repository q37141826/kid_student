package cn.dajiahui.kid.ui.mine.personalinformation;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.dajiahui.kid.R;

/**
 * Created by lenovo on 2018/3/3.
 */

public class FixSexDialog {
    private Context context;
    private Dialog dialog;

    public FixSexDialog(Context context) {
        this.context = context;
    }

    /**
     * 示加载界面
     **/
    public void show() {
        if (dialog == null) {
            dialog = new Dialog(context, R.style.custom_dialog);
            //设置对话框布局
            dialog.setContentView(R.layout.fix_sex_dialog);

            dialog.setCanceledOnTouchOutside(true);
            //获取对话框的窗口，并设置窗口参数
            Window win = dialog.getWindow();
            //设置弹窗在底部
            win.setGravity(Gravity.BOTTOM);

            WindowManager.LayoutParams lp = win.getAttributes(); // 获取对话框当前的参数值
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
            lp.x = 100;
            lp.y = 100;
            win.setAttributes(lp);

            TextView cancle = (TextView) dialog.findViewById(R.id.cancle);
            dialog.show();


            //取消
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }

    }


    /**
     * 隐藏加载界面
     **/
    public void dismiss() {
//隐藏对话框之前先判断对话框是否存在，以及是否正在显示
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    public TextView Boy() {
        TextView boy = (TextView) dialog.findViewById(R.id.boy);

        return boy;
    }

    public TextView Girl() {
        TextView girl = (TextView) dialog.findViewById(R.id.girl);
        return girl;
    }
}
