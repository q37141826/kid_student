package cn.dajiahui.kid.ui.notice;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.GsonType;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.time.TimeUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.ActivityUtil;
import com.fxtx.framework.util.BaseUtil;
import com.fxtx.framework.widgets.BasicListView;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.BadgeController;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.http.bean.BeTeFile;
import cn.dajiahui.kid.ui.album.adapter.ApPhotoEval;
import cn.dajiahui.kid.ui.album.bean.BePhotoEval;
import cn.dajiahui.kid.ui.album.bean.BePhotoEvalItem;
import cn.dajiahui.kid.ui.notice.adapter.ApTeNote;
import cn.dajiahui.kid.ui.notice.bean.BeNoticeDetails;
import cn.dajiahui.kid.ui.notice.view.FxEmojiconView;

/**
 * Created by z on 2016/3/17.
 * 通知详情活动
 */
public class NoticeDetailsActivity extends FxActivity {
    private TextView tvTitle, tvClass, tvMsg, tvContent;
    private BasicListView listFile;
    private ApTeNote adapterFile;
    private ArrayList<BeTeFile> beTeFiles = new ArrayList<BeTeFile>();
    private String noticeId;
    private BeNoticeDetails details;
    private ExpandableListView listView;
    private View line1, line2, viewEval;
    private TextView tvEval;
    private ArrayList<BePhotoEval> evals = new ArrayList<BePhotoEval>();
    private ApPhotoEval evalAdapter;
    private String parentId;
    private String replyUserId;
    private int group = -1;
    private FxEmojiconView emojiView;
    private int opinionCount;
    private TextView tvNull;

    @Override
    protected void initView() {
        noticeId = getIntent().getStringExtra(Constant.bundle_id);
        setContentView(R.layout.activity_notice_details);
        listView = getView(R.id.listview);
        View view = getLayoutInflater().inflate(R.layout.notice_head, null);
        view.setOnClickListener(onClick);
        listView.addHeaderView(view);
        //基本通知信息
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvClass = (TextView) view.findViewById(R.id.tvClass);
        tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        listFile = (BasicListView) view.findViewById(R.id.basicListview);
        tvNull = (TextView) view.findViewById(R.id.list_dataNoTv);
        adapterFile = new ApTeNote(context, beTeFiles);
        listFile.setHaveScrollbar(false);
        listFile.setAdapter(adapterFile);
        //图片点击
        evalAdapter = new ApPhotoEval(evals, context);
        listView.setAdapter(evalAdapter);
        //点击子类
        //点击子类
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                BePhotoEvalItem item = evals.get(groupPosition).getList().get(childPosition);//回复人
                //回复他
                group = groupPosition;
                sendData(item.getParentId(), item.getUserId(), item.getUserName());
                return true;
            }
        });
        //点击父类
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //回复他
                group = groupPosition;
                BePhotoEval item = evals.get(groupPosition);
                sendData(item.getObjectId(), item.getUserId(), item.getUserName());
                return true;
            }
        });

        line1 = view.findViewById(R.id.line1);
        line2 = view.findViewById(R.id.line2);
        viewEval = view.findViewById(R.id.viewEval);

        tvEval = (TextView) view.findViewById(R.id.tvEval);
        emojiView = getView(R.id.emojiView);
        emojiView.setInput(emojiconInput);
        emojiView.setVisibility(View.GONE);
    }

    private void sendData(String parentId, String replyUserId, String replyName) {
        this.parentId = parentId;
        this.replyUserId = replyUserId;
        if (replyName == null) {
            group = -1;
            emojiView.setEdiTextHint(R.string.hint_eval);
        } else {
            emojiView.toggleEmojicon(true);
            BaseUtil.showSoftInput(emojiView.getEdit());
            emojiView.setEdiTextHint("回复" + replyName);
        }
    }

    private void setViewData() {
        if (details == null)
            return;
        tvTitle.setText(details.getTitle());
        if (details.getType() == 1) {
            tvMsg.setText("系统通知 " + TimeUtil.timeFormat(details.getAddTime(), TimeUtil.yyMD));
        } else {
            tvMsg.setText(details.getUserName() + " " + TimeUtil.timeFormat(details.getAddTime(), TimeUtil.yyMD));
        }
        tvContent.setText(details.getContent());
        if (details.getType() == 3) {
            //出现评论
            tvClass.setText(details.getClassName());
            emojiView.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
            viewEval.setVisibility(View.VISIBLE);
            opinionCount = details.getCommentCount();
            tvEval.setText(getString(R.string.num, details.getCommentCount()));
            if (details.getCommentCount() == 0) {
                tvNull.setVisibility(View.VISIBLE);
            }
        } else {
            details.getList().clear();
            tvClass.setVisibility(View.GONE);
            emojiView.setVisibility(View.GONE);
        }
        evals.addAll(details.getList());
        evalAdapter.notifyDataSetChanged();
        expandGroup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.notice_details);
        onBackText();
        showfxDialog();
        httpData();
    }

    @Override
    public void httpData() {
        RequestUtill.getInstance().httpNoticeDetail(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    List<BeTeFile> temp = json.parsingListArray("attrList", new GsonType<List<BeTeFile>>() {
                    });
                    if (temp != null) {
                        beTeFiles.clear();
                        beTeFiles.addAll(temp);
                        adapterFile.notifyDataSetChanged();
                    }
                    details = json.parsingObject(BeNoticeDetails.class);
                    setViewData();
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), noticeId);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.notice_heade) {
                sendData("0", null, null);
            }
        }
    };
    private FxEmojiconView.OnEmojiconInput emojiconInput = new FxEmojiconView.OnEmojiconInput() {
        @Override
        public void onEdit(String string) {
            httpSend(string);
        }
    };

    //发表评论
    public void httpSend(String content) {
        if (StringUtil.isEmpty(content)) {
            ToastUtil.showToast(context, getString(R.string.hint_comments_text));
            return;
        }
        showfxDialog();
        RequestUtill.getInstance().httpNoticeComment(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    tvNull.setVisibility(View.GONE);
                    BePhotoEval item = json.parsingObject(BePhotoEval.class);
                    if (item != null) {
                        item.setAvator(UserController.getInstance().getUser().getAvator());
                        if (group == -1) {
                            opinionCount++;
                            tvEval.setText(getString(R.string.num, opinionCount));
                        }
                        if (group > -1) {
                            BePhotoEval eval = evals.get(group);
                            eval.getList().add(item);
                        } else {
                            evals.add(item);
                        }
                        evalAdapter.notifyDataSetChanged();
                        listView.expandGroup(evalAdapter.getGroupCount() - 1);
                        group = -1;
                        sendData("0", null, null);
                    }
                    emojiView.getEdit().setText("");
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), noticeId, content, parentId, replyUserId);
    }

    /**
     * 展开数组
     */
    private void expandGroup() {
        // 展开所有
        for (int i = 0; i < evalAdapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
    }

    @Override
    public void onBackPressed() {
        if (emojiView.isShowEmoji())
            emojiView.toggleEmojicon(false);
        else
            finishActivity();
    }

    @Override
    protected void finishActivity() {
        if (details != null)
            if (3 == details.getType()) {
                if ("0".equals(details.getIsRead())) {
                    FxDialog dialog = new FxDialog(context) {
                        @Override
                        public void onRightBtn(int flag) {
                            httpRead();
                        }

                        @Override
                        public void onLeftBtn(int flag) {
                            ActivityUtil.getInstance().finishThisActivity(NoticeDetailsActivity.this);
                            if (setOnBackAnim()) {
                                finishAnim();
                            }
                        }
                    };
                    dialog.setTitle(R.string.prompt);
                    dialog.setMessage(R.string.notice_msg_ok);
                    dialog.show();
                } else {
                    super.finishActivity();
                }
            } else {
                super.finishActivity();
            }
        else
            super.finishActivity();
    }

    public void httpRead() {
        showfxDialog();
        RequestUtill.getInstance().httpReadNotice(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    ActivityUtil.getInstance().finishThisActivity(NoticeDetailsActivity.this);
                    if (setOnBackAnim()) {
                        finishAnim();
                    }
                    BadgeController.getInstance().noticeBadge--;
                    BadgeController.getInstance().sendBadgeMessage(context);
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), noticeId);
    }
}
