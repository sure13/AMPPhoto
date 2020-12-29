package com.example.ampphoto.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsSeekBar;

public class VerticalSeekBar extends AbsSeekBar {

    private Drawable mThumb;

    public interface OnSeekBarChangeListener {
        void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser);

        void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar);

        void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar);
    }

    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    public VerticalSeekBar(Context context) {
        this(context, null);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mOnSeekBarChangeListener2) {
        mOnSeekBarChangeListener = mOnSeekBarChangeListener2;
    }

    void onStartTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    void onStopTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    void onProgressRefresh(float scale, boolean fromUser) {
        Drawable thumb = mThumb;
        if (thumb != null) {
            setThumbPos(getHeight(), thumb, scale, Integer.MIN_VALUE);
            invalidate();
        }
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), isPressed());
        }
    }

    private void setThumbPos(int w, Drawable thumb, float scale, int gap) {
        int available = w - getPaddingLeft() - getPaddingRight();
        int thumbWidth = thumb.getIntrinsicWidth();
        int thumbHeight = thumb.getIntrinsicHeight();
        available -= thumbWidth;

        // The extra space for the thumb to move on the track
        available += getThumbOffset() * 2;

        int thumbPos = (int) (scale * available);

        int topBound, bottomBound;
        if (gap == Integer.MIN_VALUE) {
            Rect oldBounds = thumb.getBounds();
            topBound = oldBounds.top;
            bottomBound = oldBounds.bottom;
        } else {
            topBound = gap;
            bottomBound = gap + thumbHeight;
        }
        thumb.setBounds(thumbPos, topBound, thumbPos + thumbWidth, bottomBound);
    }

    @Override
    protected void onDraw(Canvas c) {
        c.rotate(90);// 鍙嶈浆90搴︼紝灏嗘按骞砈eekBar绔栬捣鏉�
        c.translate(0, -getWidth());// 灏嗙粡杩囨棆杞悗寰楀埌鐨刅erticalSeekBar绉诲埌姝ｇ‘鐨勪綅缃�,娉ㄦ剰缁忔棆杞悗瀹介珮鍊间簰鎹�
        super.onDraw(c);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());// 瀹介珮鍊间簰鎹�
    }

    @Override
    public void setThumb(Drawable thumb) {
        mThumb = thumb;
        super.setThumb(thumb);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldw, oldh);// 瀹介珮鍊间簰鎹�
    }

    // 涓庢簮鐮佸畬鍏ㄧ浉鍚岋紝浠呬负璋冪敤瀹介珮鍊间簰鎹㈠鐞嗙殑onStartTrackingTouch()鏂规硶
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                setPressed(true);
                onStartTrackingTouch();
                trackTouchEvent(event);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                trackTouchEvent(event);
                attemptClaimDrag();
                break;
            }

            case MotionEvent.ACTION_UP: {
                trackTouchEvent(event);
                onStopTrackingTouch();
                setPressed(false);
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate();
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                onStopTrackingTouch();
                setPressed(false);
                invalidate(); // see above explanation
                break;
            }

            default:
                break;
        }
        return true;
    }

    // 瀹介珮鍊间簰鎹㈠鐞�
    private void trackTouchEvent(MotionEvent event) {
        final int height = getHeight();
        final int available = height - getPaddingBottom() - getPaddingTop();
        int Y = (int) event.getY();
        float scale;
//		System.out.println("height: "+height+"  available: "+available+"  y: "+Y);
//		System.out.println("getPaddingBottom: "+getPaddingBottom()+"  getPaddingTop: "+getPaddingTop());
        float progress = 0;
        if (Y > height - getPaddingBottom()) {
            scale = 1.0f;
        } else if (Y < getPaddingTop()) {
            scale = 0.0f;
        } else {
            //	scale = (float) (height - getPaddingBottom() - Y) / (float) available;
            scale = (float) (Y) / (float) available;
        }

        final int max = getMax();
        progress = scale * max;
        setProgress((int) progress);
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), isPressed());
        }

    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

}
