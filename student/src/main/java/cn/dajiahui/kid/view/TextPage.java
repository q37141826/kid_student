package cn.dajiahui.kid.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;




public class TextPage extends EditText {
    private int off; //字符串的偏移值
    private Context context;

    public TextPage(Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    public TextPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize();
    }

    private void initialize() {
        setGravity(Gravity.TOP);
        setBackgroundColor(Color.WHITE);
    }



    public boolean isEdit = false;//



    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        //不做任何处理，为了阻止长按的时候弹出上下文菜单    
    }

    @Override
    public boolean getDefaultEditable() {
        return false;
    }

    int curOff = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Layout layout = getLayout();
        int line = 0;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                line = layout.getLineForVertical(getScrollY() + (int) event.getY());
                off = layout.getOffsetForHorizontal(line, (int) event.getX());
                Selection.setSelection(getEditableText(), off);
                curOff = off;
                break;
            case MotionEvent.ACTION_MOVE:
                line = layout.getLineForVertical(getScrollY() + (int) event.getY());
                curOff = layout.getOffsetForHorizontal(line, (int) event.getX());
                Selection.setSelection(getEditableText(), off, curOff);
                break;
            case MotionEvent.ACTION_UP:
                boolean flag = true;
                if (curOff != off) {
                } else {
                    if (pgInterface != null)
                        pgInterface.downContent(off);//
                }
                break;
        }
        return true;
    }

    public void initStatus() {
        Selection.setSelection(getEditableText(), off);
    }

    private PgInterface pgInterface;

    public interface PgInterface {
        void selectContent(String selectContent, int startIndex, int endIndex);

        void downContent(int clickContent);

        void moveContent(int clickContent);
    }

} 