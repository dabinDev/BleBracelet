package github.opensource.dialog.prompt;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * Project :  promptlibrary.
 * Package github.opensource.dialog.prompt;
 * Created by :  Benjamin.
 * Created time: 2017/10/30 15:35
 * Changed by :  Benjamin.
 * Changed time: 2017/10/30 15:35
 * Class description:
 */

public class PromptButton {
    private boolean isDelyClick;
    private String text = "confirm";
    private boolean focus;
    private int textColor = Color.BLACK;
    private float textSize = 18;
    private RectF rect;
    private PromptButtonListener listener;
    private int focusBacColor = Color.parseColor("#DCDCDC");

    private boolean dismissAfterClick = true;

    public PromptButton(String text, PromptButtonListener listener) {
        this.text = text;
        this.listener = listener;
    }

    public PromptButton(String text, PromptButtonListener listener, boolean delayClick) {
        this.text = text;
        this.listener = listener;
        this.isDelyClick = delayClick;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setTouchRect(RectF rectF) {
        this.rect = rectF;
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public PromptButtonListener getListener() {
        return listener;
    }

    public void setListener(PromptButtonListener listener) {
        this.listener = listener;
    }

    public int getFocusBacColor() {
        return focusBacColor;
    }

    public void setFocusBacColor(int focusBacColor) {
        this.focusBacColor = focusBacColor;
    }

    public boolean isDismissAfterClick() {
        return dismissAfterClick;
    }

    public void setDismissAfterClick(boolean dismissAfterClick) {
        this.dismissAfterClick = dismissAfterClick;
    }

    public boolean isDelyClick() {
        return isDelyClick;
    }

    public void setDelyClick(boolean delyClick) {
        isDelyClick = delyClick;
    }

}
