package com.fxtx.framework.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.fxtx.framework.R;
import com.fxtx.framework.http.OkHttpClientManager;
import com.fxtx.framework.platforms.umeng.UmengUtil;
import com.fxtx.framework.util.ActivityUtil;
import com.fxtx.framework.widgets.StatusBarCompat;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.fxtx.framework.widgets.refresh.MaterialRefreshListener;


public abstract class FxActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    public TextView titleView;
    protected FxProgressDialog progressDialog;
    protected final int PROGRESS_BACK = -1;
    public Activity context;
    private SparseArray<View> mViews = new SparseArray<View>();
    public int mPageNum = 1; //分页
    protected boolean isCreateView = false;
    public int mPageSize = 10; //默认一页10个条目
    public TextView tv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.getInstance().addActivity(this);
        /*禁止息屏*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        initView();

        toolbar = getView(R.id.toolbar);
        setStatusBar(toolbar);
        titleView = getView(R.id.tool_title);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setOnMenuItemClickListener(onMenuItemClick);
        }
        Log.e("where",this.getClass().getSimpleName());
    }

    protected void onCreate(Bundle savedInstanceState, int resId) {
        onCreate(savedInstanceState);
        setContentView(resId);
    }

    public void setStatusBar(Toolbar title) {
        StatusBarCompat.compat(this, getResources().getColor(R.color.app_bg));
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public void initRefresh(final MaterialRefreshLayout mRefresh) {
        if (mRefresh == null)
            return;
        mRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mPageNum = 1;
                mRefresh.setLoadMore(true);
                httpData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                httpData();
            }
        });
    }

    /**
     * @param isLastPag： 0 不是最后一页 1 是最后一页
     */
    public void finishRefreshAndLoadMoer(MaterialRefreshLayout mRefresh, int isLastPag) {
        if (mRefresh != null) {
            mRefresh.finishRefresh();
            mRefresh.finishRefreshLoadMore();
            mRefresh.finishRefreshing();
        }
        if (isLastPag == 0) {
            //不是最后一页
            mRefresh.setLoadMore(true);
        } else {
            mRefresh.setLoadMore(false);
        }
    }

    public void httpData() {
    }

    /**
     * 初始化对象  在 初始化的时候 就需要 setContext（R.layout.xxx）
     */
    protected abstract void initView();


    public void showfxDialog() {
        showfxDialog(R.string.fx_login);
    }


    public void showfxDialog(Object title) {
        if (this.isFinishing())
            return;
        if (progressDialog == null) {
            progressDialog = new FxProgressDialog(this);
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        //点击返回
                        dismissfxDialog(PROGRESS_BACK);
                        return true;
                    }
                    return false;
                }
            });
        }
        if (title != null) {
            if (title instanceof String) {
                progressDialog.setTextMsg((String) title);
            } else {
                progressDialog.setTextMsg((Integer) title);
            }
        }
        progressDialog.show();
    }

    protected void dismissfxDialog(int flag) {
        if (this.isFinishing()) {
            return;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void dismissfxDialog() {
        dismissfxDialog(0);
    }

    //设置标题左边的图标
    public void setLeftIco(int ids, View.OnClickListener onclick) {
        if (toolbar != null) {
            if (ids == 0) {
                Drawable navigationIcon = toolbar.getNavigationIcon();
                navigationIcon.setAlpha(0);
                toolbar.setNavigationOnClickListener(null);
                return;
            }
            toolbar.setNavigationIcon(ids);
            toolbar.setNavigationOnClickListener(onclick);
        }
    }


    public void onBack() {
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ico_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishActivity();
                }
            });
        }
    }

    /*显示弹框*/
    public void onBackTextShowProgress() {
        if (toolbar != null) {
            TextView tv = getView(R.id.tool_left);
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_back, 0, 0, 0);
//            tv.setText(R.string.back_text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackShowProgress(v);
                }
            });
        }
    }

    public void onBackShowProgress(View view) {

    }

    /*左上角退出无文字*/
    public void onBackText() {
        if (toolbar != null) {
            TextView tv = getView(R.id.tool_left);
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_back, 0, 0, 0);
//            tv.setText(R.string.back_text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /*右上角 加图片*/
    public void onRightBtn(int drawableId, int textId) {
        if (toolbar != null) {
            TextView tv = getView(R.id.tool_right);
            tv.setText(textId);
            tv.setVisibility(View.VISIBLE);
            tv.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightBtnClick(v);
                }
            });
        }
    }

    /*右上角 不加图片*/
    public void onRightBtn(int textId) {
        if (toolbar != null) {
            tv_right = getView(R.id.tool_right);
            tv_right.setText(textId);
            tv_right.setVisibility(View.VISIBLE);

            tv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightBtnClick(v);
                }
            });
        }
    }

    public void onRightBtnClick(View view) {

    }

    public void rightBtHine() {
        if (toolbar != null) {
            TextView tv = getView(R.id.tool_right);
            if (tv != null)
                tv.setVisibility(View.GONE);
        }
    }

    public void onBack(int resid, View.OnClickListener onClickListener) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ico_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishActivity();
                }
            });
        }
    }

    /**
     * 设置了 onBack 后 如果要隐藏按钮 就调用这个方法就可以了。 显示的话 还是在调用onBack()方法
     */
    public void hideBack() {
        Drawable nullDrawable = null;
        toolbar.setNavigationIcon(nullDrawable);
    }

    protected void setfxTtitle(String title) {
        if (titleView != null)
            titleView.setText(title);
    }

    protected void setfxTitleOnclick(View.OnClickListener listener) {
        titleView.setOnClickListener(listener);
    }

    protected void setfxTtitle(int res) {
        if (titleView != null) {
            titleView.setText(res);

        }
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            setFxOnMenuItemClick(menuItem);
            return true;
        }
    };

    protected void setFxOnMenuItemClick(MenuItem menuItem) {

    }

    /**
     * 设置是否开关闭动画
     *
     * @return
     */
    protected boolean setOnBackAnim() {
        return true;
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in,
                R.anim.base_slide_remain);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.base_slide_right_in,
                R.anim.base_slide_remain);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtil.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtil.onPause(this);
    }

    protected void finishActivity() {
        dismissfxDialog();
        ActivityUtil.getInstance().finishThisActivity(this);
        if (setOnBackAnim()) {
            finishAnim();
        }
    }

    protected void finishAnim() {
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    protected void onDestroy() {
        dismissfxDialog();
        //关闭activity 的时候 结束线程
        OkHttpClientManager.getInstance().cancelTag(this);
        super.onDestroy();
    }

    public View getContentView() {
        return findViewById(android.R.id.content);
    }

}
