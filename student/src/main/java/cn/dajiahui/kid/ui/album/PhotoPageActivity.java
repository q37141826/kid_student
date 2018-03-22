package cn.dajiahui.kid.ui.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fxtx.framework.anim.AnimUtil;
import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.StatusBarCompat;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.squareup.okhttp.Request;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.http.ShareHttp;
import cn.dajiahui.kid.ui.album.bean.BePhoto;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * Created by z on 2016/3/10.
 * 大图展示界面
 */
public class PhotoPageActivity extends FxActivity {
    private List<BePhoto> photos = new ArrayList<BePhoto>();
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private TextView tvLike, tvdelete;
    private int index = 0;
    private boolean isShow = true;
    private View bottomView;
    private int selectId, deleteId;
    private ProgressBar progressBar;
    private boolean isMenu;//是否允许出现菜单

    @Override
    protected void initView() {
        ArrayList<BePhoto> ph = (ArrayList<BePhoto>) getIntent().getSerializableExtra(Constant.bundle_obj);
        if (ph != null)
            photos.addAll(ph);
        index = getIntent().getIntExtra(Constant.bundle_id, 0);
        if (index > photos.size())
            index = 0;
        isMenu = getIntent().getBooleanExtra(Constant.bundle_type, false);
        setContentView(R.layout.activity_page_phtot);
        viewPager = getView(R.id.viewPage);
        progressBar = getView(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        bottomView = getView(R.id.bottomView);
        if (!isMenu) {
            bottomView.setVisibility(View.GONE);
        }
        adapter = new PagerAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tvLike = getView(R.id.tvLike);
        tvLike.setOnClickListener(onClick);
        getView(R.id.tvShart).setOnClickListener(onClick);
        getView(R.id.tvDown).setOnClickListener(onClick);
        tvdelete = getView(R.id.tvDelete);
        tvdelete.setOnClickListener(onClick);
        getView(R.id.tvDetails).setOnClickListener(onClick);
        setLike(photos.get(index));
        viewPager.setCurrentItem(index);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                setLike(photos.get(index));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void setLike(BePhoto photo) {
        if (!isMenu)
            return;
        String likeNum;
        if (photo.getLikeCount() > 999) {
            likeNum = "999+";
        } else {
            likeNum = photo.getLikeCount() + "";
        }
        tvLike.setText(likeNum);
        if (photo.getIsLike() == 0) {
            tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_like, 0, 0, 0);
        } else {
            tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_like1, 0, 0, 0);
        }
        if (StringUtil.sameStr(photo.getAddUserId(), UserController.getInstance().getUserId())) {
            tvdelete.setVisibility(View.VISIBLE);
        } else {
            tvdelete.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvLike:
                    //添加喜欢和取消喜欢
                    showfxDialog();
                    httpData();
                    break;
                case R.id.tvShart:
                    //分享
                    new ShareHttp(PhotoPageActivity.this, photos.get(index).getObjectId()).startShare();
                    break;
                case R.id.tvDown:
                    //下载
                    showDialoog(1);
                    break;
                case R.id.tvDelete:
                    //删除
                    showDialoog(0);
                    break;
                case R.id.tvDetails:
                    //点击进入详情
                    DjhJumpUtil.getInstance().startPhotoDetails(context, photos.get(index).getObjectId());
                    break;
            }
        }
    };
    private FxDialog dialogDeorDw;

    private void showDialoog(int flag) {
        if (dialogDeorDw == null) {
            dialogDeorDw = new FxDialog(context) {
                @Override
                public void onRightBtn(int floag) {
                    if (floag == 0) {
                        //删除
                        httpDelete();
                    } else {
                        //下载
                        httpDownImg();
                    }
                }

                @Override
                public void onLeftBtn(int floag) {
                }
            };
        }
        dialogDeorDw.setFloag(flag);
        if (flag == 0) {
            //删除
            dialogDeorDw.setTitle(R.string.delete);
            dialogDeorDw.setMessage(R.string.hint_delete_photo);
        } else {
            //下载
            dialogDeorDw.setTitle(R.string.down);
            dialogDeorDw.setMessage(R.string.hint_down_photo);
        }
        dialogDeorDw.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setBackgroundResource(R.color.transparent);

        onBackText();
    }

    @Override
    public void httpData() {
        BePhoto bePhoto = photos.get(index);
        selectId = index;
        final int like = bePhoto.getIsLike() == 0 ? 1 : 0;
        RequestUtill.getInstance().httpPictureLike(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus()  == 1) {
                    BePhoto photo = photos.get(selectId);
                    photo.setIsLike(like);
                    photo.setCount(like);
                    if (selectId == index) {
                        setLike(photo);
                    }
                    setResult(RESULT_OK);
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), bePhoto.getObjectId(), like);
    }

    //删除照片
    public void httpDelete() {
        BePhoto bePhoto = photos.get(index);
        deleteId = index;
        RequestUtill.getInstance().httpPictureDelete(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus()  == 1) {
                    setResult(RESULT_OK);
                    photos.remove(deleteId);
                    if (selectId > deleteId) {
                        selectId--;
                    }
                    adapter.notifyDataSetChanged();
                    if (photos.size() == 0) {
                        finishActivity();
                    }
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        }, bePhoto.getObjectId());
    }

    //下载照片
    public void httpDownImg() {
        BePhoto photo = photos.get(index);
        progressBar.setVisibility(View.VISIBLE);
        final String fileName = StringUtil.getStrLeSplitter(photo.getPicUrl(), "/");
        RequestUtill.getInstance().downImageFile(
                context, photo.getOriginalUrl(), fileName,
                new ResultCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtil.showToast(context, ErrorCode.error(e));
                    }

                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);
//                        ImageUtil.scanFile(context, UserController.getInstance().getUserImageFile(context) + fileName);
                        ToastUtil.showToast(context, R.string.hint_down_ok);
                    }

                    @Override
                    public void inProgress(float progress) {
                        super.inProgress(progress);
                        //下载进行中
                        progressBar.setProgress((int) progress);
                    }
                });
    }

    @Override
    public void setStatusBar(android.support.v7.widget.Toolbar title) {
        StatusBarCompat.compat(this, title, getResources().getColor(R.color.color5A));

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int arg0) {
            BePhoto item = photos.get(arg0);
            Bundle bundle = new Bundle();
            if (item.getPicUrl().endsWith("gif") || item.getPicUrl().endsWith("GIF")) {
                bundle.putString(Constant.bundle_obj, item.getOriginalUrl());
                bundle.putBoolean(Constant.bundle_type, true);
            } else {
                bundle.putString(Constant.bundle_obj, item.getPicUrl());
                bundle.putBoolean(Constant.bundle_type, false);
            }
            FrPhoto fragment = new FrPhoto();
            fragment.setOnImageClick(onImageClick);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return photos != null ? photos.size() : 0;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    private OnImageClick onImageClick = new OnImageClick() {
        @Override
        public void onItemClick() {
            if (isShow) {
                //隐藏
                if (isMenu) {
                    AnimUtil.hideUptoDown(bottomView);
                }
                AnimUtil.hideDowntoUp(toolbar);
            } else {
                //显示
                if (isMenu) {
                    AnimUtil.showDowntoUp(bottomView);
                }
                AnimUtil.showUptoDown(toolbar);
            }
            isShow = !isShow;
        }
    };

    public interface OnImageClick {
        void onItemClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}

