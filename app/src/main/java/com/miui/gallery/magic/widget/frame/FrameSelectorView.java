package com.miui.gallery.magic.widget.frame;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import ch.qos.logback.classic.Level;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ScreenUtils;
import com.nexstreaming.nexeditorsdk.nexCrop;

/* loaded from: classes2.dex */
public class FrameSelectorView extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {
    public int MAX_TIME;
    public boolean isMax;
    public boolean isTouchInSeekBar;
    public int mDuration;
    public FrameLayout.LayoutParams mGroupLayoutParam;
    public OnHandleSideBarListener mHandleSideBarListener;
    public OnHandlerBarPositionCallback mHandlerBarPositionCallback;
    public FrameBodyView mHandlerBody;
    public ImageView mHandlerLeft;
    public ImageView mHandlerRight;
    public float mHotArea;
    public boolean mIsTouching;
    public int mLeftAndRightWidth;
    public int mMaxWidth;
    public float mMinWidth;
    public float mNegativeMargin;
    public int mOriginLeftMargin;
    public ViewGroup.LayoutParams mOriginParam;
    public int mOriginRightMargin;
    public int mOriginWidth;
    public float mOriginX;
    public int mPreProgress;
    public ProgressChangeLister mProgressLister;
    public SeekBar mSeekBar;
    public float mSeekBarThubWidth;
    public int mWindWidth;
    public float maxViewWidth;
    public float start;
    public boolean stopSetProgress;

    /* loaded from: classes2.dex */
    public interface OnHandleSideBarListener {
        void handleSideBar(int i, boolean z);
    }

    /* loaded from: classes2.dex */
    public interface OnHandlerBarPositionCallback {
        void update(int i, int i2);
    }

    /* loaded from: classes2.dex */
    public interface ProgressChangeLister {
        void onProgress(int i, boolean z);

        void onStartTouch(int i);

        void onStopTrackingTouch();

        void onTouchMove();

        void onTouchUpFrame();
    }

    public static /* synthetic */ void $r8$lambda$ItDYSYFKVxl6V9xTTyI2livyV6U(FrameSelectorView frameSelectorView) {
        frameSelectorView.lambda$new$0();
    }

    public FrameSelectorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPreProgress = -1;
        this.mMaxWidth = nexCrop.ABSTRACT_DIMENSION;
        this.mMinWidth = 1.0f;
        this.MAX_TIME = Level.INFO_INT;
        this.isTouchInSeekBar = false;
        this.start = 0.0f;
        View inflate = LayoutInflater.from(context).inflate(R$layout.ts_magic_frame_selector_view, this);
        this.mHandlerLeft = (ImageView) inflate.findViewById(R$id.handler_left);
        this.mHandlerRight = (ImageView) inflate.findViewById(R$id.handler_right);
        this.mSeekBar = (SeekBar) inflate.findViewById(R$id.magic_cut_seek_bar);
        this.mHandlerBody = (FrameBodyView) inflate.findViewById(R$id.handler_body);
        this.mHandlerLeft.setOnTouchListener(new HandlerLeftTouchListener());
        this.mHandlerRight.setOnTouchListener(new HandlerRightTouchListener());
        this.mHandlerBody.setHandlerBodyTouchListener(new HandlerBodyTouchListener());
        this.mWindWidth = ScreenUtils.getScreenWidth();
        this.mNegativeMargin = context.getResources().getDimension(R$dimen.magic_px_010) * 2.0f;
        Resources resources = context.getResources();
        int i = R$dimen.magic_padding_80;
        this.mLeftAndRightWidth = (int) ((context.getResources().getDimension(R$dimen.magic_px_56) * 2.0f) + (resources.getDimension(i) * 2.0f) + this.mNegativeMargin);
        this.mSeekBarThubWidth = context.getResources().getDimension(R$dimen.magic_18px);
        post(new Runnable() { // from class: com.miui.gallery.magic.widget.frame.FrameSelectorView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FrameSelectorView.$r8$lambda$ItDYSYFKVxl6V9xTTyI2livyV6U(FrameSelectorView.this);
            }
        });
        this.mSeekBar.setOnSeekBarChangeListener(this);
        this.mHotArea = getContext().getResources().getDimension(R$dimen.seek_bar_hot_area);
        this.maxViewWidth = this.mWindWidth - (context.getResources().getDimension(i) * 2.0f);
    }

    public /* synthetic */ void lambda$new$0() {
        ViewGroup.LayoutParams layoutParams;
        this.mGroupLayoutParam = (FrameLayout.LayoutParams) getLayoutParams();
        if (this.mHandlerBarPositionCallback == null || (layoutParams = this.mOriginParam) == null) {
            return;
        }
        updateView(layoutParams.width);
        invalidate();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }

    public final void onManualMoveHandle(int i, int i2) {
        if (i2 == 0) {
            this.mSeekBar.setVisibility(4);
            return;
        }
        if (i2 != 1) {
            if (i2 == 2) {
                int i3 = getCurrentTimes(this.mDuration)[i];
                OnHandleSideBarListener onHandleSideBarListener = this.mHandleSideBarListener;
                if (onHandleSideBarListener != null && this.mPreProgress != i3) {
                    this.mPreProgress = i3;
                    onHandleSideBarListener.handleSideBar(i3, true);
                }
                if (this.mHandlerBarPositionCallback != null) {
                    updateView(this.mOriginParam.width);
                }
                onTouchMove();
                return;
            } else if (i2 != 3) {
                return;
            }
        }
        this.mSeekBar.setVisibility(0);
        this.mPreProgress = -1;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (this.mGroupLayoutParam == null || this.mProgressLister == null) {
            return;
        }
        int[] currentTimes = getCurrentTimes(this.mDuration);
        this.mProgressLister.onProgress(((int) ((i / 100.0d) * (currentTimes[1] - currentTimes[0]) * 1.0d)) + currentTimes[0], z);
    }

    public void onTouchMove() {
        ProgressChangeLister progressChangeLister = this.mProgressLister;
        if (progressChangeLister != null) {
            progressChangeLister.onTouchMove();
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
        ProgressChangeLister progressChangeLister = this.mProgressLister;
        if (progressChangeLister != null) {
            this.stopSetProgress = true;
            progressChangeLister.onStartTouch(seekBar.getProgress());
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        ProgressChangeLister progressChangeLister = this.mProgressLister;
        if (progressChangeLister != null) {
            this.stopSetProgress = false;
            progressChangeLister.onStopTrackingTouch();
        }
    }

    public void setProgressChangeLister(ProgressChangeLister progressChangeLister) {
        this.mProgressLister = progressChangeLister;
    }

    public void setHandlerSideBarListener(OnHandleSideBarListener onHandleSideBarListener) {
        this.mHandleSideBarListener = onHandleSideBarListener;
    }

    public void setHandlerBarPositionCallback(OnHandlerBarPositionCallback onHandlerBarPositionCallback) {
        this.mHandlerBarPositionCallback = onHandlerBarPositionCallback;
    }

    public final void upTouch() {
        ProgressChangeLister progressChangeLister = this.mProgressLister;
        if (progressChangeLister != null) {
            progressChangeLister.onTouchUpFrame();
        }
        if (this.isMax) {
            this.mHandlerLeft.setBackgroundResource(R$drawable.magic_clip_frame_left);
            this.mHandlerRight.setBackgroundResource(R$drawable.magic_clip_frame_right);
            this.mHandlerBody.setBackgroundResource(R$drawable.magic_frame_selector_body);
        }
    }

    public void setDuration(int i) {
        int i2 = this.mWindWidth - this.mLeftAndRightWidth;
        this.mMaxWidth = i2;
        this.mDuration = i;
        float f = i;
        this.mMinWidth = (1000.0f / f) * i2;
        int i3 = this.MAX_TIME;
        if (i3 < i) {
            int i4 = (int) (((i3 * 1.0f) / f) * i2);
            this.mMaxWidth = i4;
            if (i4 == 0) {
                this.mMaxWidth = 1;
            }
            this.mMinWidth = (1000.0f / i3) * this.mMaxWidth;
        }
        ViewGroup.LayoutParams layoutParams = this.mHandlerBody.getLayoutParams();
        this.mOriginParam = layoutParams;
        layoutParams.width = this.mMaxWidth;
        this.mHandlerBody.setLayoutParams(layoutParams);
        if (this.mHandlerBarPositionCallback == null || this.mGroupLayoutParam == null) {
            return;
        }
        updateView(this.mOriginParam.width);
        invalidate();
    }

    public final void updateView(int i) {
        if (BaseMiscUtil.isRTLDirection()) {
            int width = this.mGroupLayoutParam.rightMargin + this.mHandlerLeft.getWidth();
            this.mHandlerBarPositionCallback.update(width, i + width);
            return;
        }
        int width2 = this.mGroupLayoutParam.leftMargin + this.mHandlerLeft.getWidth();
        this.mHandlerBarPositionCallback.update(width2, i + width2);
    }

    public int[] getCurrentTimes(int i) {
        int i2;
        double d;
        int i3;
        double d2;
        int bodyWidth = getBodyWidth();
        if (BaseMiscUtil.isRTLDirection()) {
            MagicLog.INSTANCE.showLog("FrameSelectorView yzz RTL duration: " + i);
            int bodyRight = getBodyRight();
            if (i > this.MAX_TIME) {
                int i4 = this.mWindWidth;
                int i5 = this.mLeftAndRightWidth;
                float f = this.mSeekBarThubWidth;
                double d3 = i;
                i3 = (int) (((bodyRight * 1.0d) / ((i4 - i5) - f)) * d3);
                d2 = (((bodyRight + bodyWidth) * 1.0d) / ((i4 - i5) - f)) * d3;
            } else {
                int i6 = this.mMaxWidth;
                double d4 = i;
                i3 = (int) (((bodyRight * 1.0d) / i6) * d4);
                d2 = (((bodyRight + bodyWidth) * 1.0d) / i6) * d4;
            }
            int i7 = (int) d2;
            MagicLog.INSTANCE.showLog("FrameSelectorView yzz from: " + i3 + ", to: " + i7);
            int i8 = i7 - i3;
            int i9 = this.MAX_TIME;
            if (i8 > i9) {
                i7 = i3 + i9;
            }
            return new int[]{i3, i7};
        }
        MagicLog.INSTANCE.showLog("FrameSelectorView duration: " + i);
        int bodyLeft = getBodyLeft();
        if (i > this.MAX_TIME) {
            int i10 = this.mWindWidth;
            int i11 = this.mLeftAndRightWidth;
            float f2 = this.mSeekBarThubWidth;
            double d5 = i;
            i2 = (int) (((bodyLeft * 1.0d) / ((i10 - i11) - f2)) * d5);
            d = (((bodyLeft + bodyWidth) * 1.0d) / ((i10 - i11) - f2)) * d5;
        } else {
            int i12 = this.mMaxWidth;
            double d6 = i;
            i2 = (int) (((bodyLeft * 1.0d) / i12) * d6);
            d = (((bodyLeft + bodyWidth) * 1.0d) / i12) * d6;
        }
        int i13 = (int) d;
        MagicLog.INSTANCE.showLog("FrameSelectorView yzz from: " + i2 + ", to: " + i13);
        int i14 = i13 - i2;
        int i15 = this.MAX_TIME;
        if (i14 > i15) {
            i13 = i2 + i15;
        }
        return new int[]{i2, i13};
    }

    /* loaded from: classes2.dex */
    public class HandlerLeftTouchListener implements View.OnTouchListener {
        public HandlerLeftTouchListener() {
            FrameSelectorView.this = r1;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int i;
            int i2;
            if (BaseMiscUtil.isRTLDirection()) {
                if (motionEvent.getAction() == 0) {
                    FrameSelectorView.this.start = motionEvent.getRawX();
                    FrameSelectorView.this.onManualMoveHandle(0, 0);
                    if (FrameSelectorView.this.mIsTouching) {
                        return false;
                    }
                    FrameSelectorView.this.mOriginX = motionEvent.getRawX();
                    FrameSelectorView frameSelectorView = FrameSelectorView.this;
                    frameSelectorView.mOriginWidth = frameSelectorView.mHandlerBody.getWidth();
                    FrameSelectorView frameSelectorView2 = FrameSelectorView.this;
                    frameSelectorView2.mOriginParam = frameSelectorView2.mHandlerBody.getLayoutParams();
                    FrameSelectorView.this.mIsTouching = true;
                } else if (motionEvent.getAction() == 2) {
                    int rawX = (int) (motionEvent.getRawX() - FrameSelectorView.this.mOriginX);
                    MagicLog magicLog = MagicLog.INSTANCE;
                    magicLog.showLog("yzz  ---->LeftTouch  RTL : move  delta:  " + rawX);
                    if ((FrameSelectorView.this.mOriginWidth - rawX) + FrameSelectorView.this.getBodyLeft() + FrameSelectorView.this.getHandlerWidth() + FrameSelectorView.this.mNegativeMargin >= FrameSelectorView.this.maxViewWidth || (i2 = FrameSelectorView.this.mOriginWidth - rawX) < 0) {
                        return true;
                    }
                    if (i2 > FrameSelectorView.this.mMinWidth) {
                        if (i2 < FrameSelectorView.this.mMaxWidth) {
                            FrameSelectorView.this.mOriginParam.width = FrameSelectorView.this.mOriginWidth - rawX;
                            FrameSelectorView.this.mHandlerBody.setLayoutParams(FrameSelectorView.this.mOriginParam);
                            FrameSelectorView.this.onManualMoveHandle(0, 2);
                            FrameSelectorView.this.setDefView();
                        } else {
                            FrameSelectorView.this.setMaxView();
                        }
                    } else {
                        MagicLog magicLog2 = MagicLog.INSTANCE;
                        magicLog2.showLog("yzz frameselect ---->LeftTouch: move:  " + i2 + "  ,min: " + FrameSelectorView.this.mMinWidth);
                        FrameSelectorView.this.setMinView();
                        return true;
                    }
                } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    FrameSelectorView.this.mIsTouching = false;
                    if (FrameSelectorView.this.mOriginWidth - ((int) (motionEvent.getRawX() - FrameSelectorView.this.mOriginX)) > FrameSelectorView.this.mMaxWidth) {
                        FrameSelectorView.this.TouchUpToast();
                    }
                    if (Math.abs(motionEvent.getRawX() - FrameSelectorView.this.start) > 2.0f) {
                        FrameSelectorView.this.upTouch();
                        FrameSelectorView.this.onManualMoveHandle(0, 1);
                    } else {
                        FrameSelectorView.this.showProgressView(false);
                    }
                    MagicLog magicLog3 = MagicLog.INSTANCE;
                    magicLog3.showLog("onTouch_mIsTouching: " + FrameSelectorView.this.mIsTouching);
                }
            } else if (motionEvent.getAction() == 0) {
                FrameSelectorView.this.start = motionEvent.getRawX();
                FrameSelectorView.this.onManualMoveHandle(0, 0);
                if (FrameSelectorView.this.mIsTouching) {
                    return false;
                }
                FrameSelectorView.this.mOriginX = motionEvent.getRawX();
                FrameSelectorView frameSelectorView3 = FrameSelectorView.this;
                frameSelectorView3.mOriginWidth = frameSelectorView3.mHandlerBody.getWidth();
                FrameSelectorView frameSelectorView4 = FrameSelectorView.this;
                frameSelectorView4.mOriginParam = frameSelectorView4.mHandlerBody.getLayoutParams();
                FrameSelectorView frameSelectorView5 = FrameSelectorView.this;
                frameSelectorView5.mOriginLeftMargin = frameSelectorView5.mGroupLayoutParam.leftMargin;
                MagicLog magicLog4 = MagicLog.INSTANCE;
                magicLog4.showLog("onTouch_mIsTouching: " + FrameSelectorView.this.mIsTouching);
                FrameSelectorView.this.mIsTouching = true;
            } else if (motionEvent.getAction() == 2) {
                int rawX2 = (int) (motionEvent.getRawX() - FrameSelectorView.this.mOriginX);
                int i3 = (int) ((FrameSelectorView.this.mOriginWidth + FrameSelectorView.this.mOriginLeftMargin) - FrameSelectorView.this.mSeekBarThubWidth);
                if (rawX2 < (-FrameSelectorView.this.mOriginLeftMargin)) {
                    rawX2 = -FrameSelectorView.this.mOriginLeftMargin;
                }
                if (FrameSelectorView.this.getLeftMargin(rawX2) >= i3 || (i = FrameSelectorView.this.mOriginWidth - rawX2) < 0) {
                    return true;
                }
                if (i > FrameSelectorView.this.mMinWidth) {
                    if (i <= FrameSelectorView.this.mMaxWidth) {
                        FrameSelectorView.this.mOriginParam.width = i;
                        FrameSelectorView.this.mHandlerBody.setLayoutParams(FrameSelectorView.this.mOriginParam);
                        FrameSelectorView.this.mGroupLayoutParam.leftMargin = FrameSelectorView.this.getLeftMargin(rawX2);
                        FrameSelectorView frameSelectorView6 = FrameSelectorView.this;
                        frameSelectorView6.setLayoutParams(frameSelectorView6.mGroupLayoutParam);
                        FrameSelectorView.this.onManualMoveHandle(0, 2);
                        FrameSelectorView.this.setDefView();
                    } else {
                        FrameSelectorView.this.setMaxView();
                    }
                } else {
                    MagicLog magicLog5 = MagicLog.INSTANCE;
                    magicLog5.showLog("frameselect ---->LeftTouch: all:  " + i + "  ,min: " + FrameSelectorView.this.mMinWidth);
                    FrameSelectorView.this.setMinView();
                    return true;
                }
            } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                FrameSelectorView.this.mIsTouching = false;
                if (FrameSelectorView.this.mOriginWidth - ((int) (motionEvent.getRawX() - FrameSelectorView.this.mOriginX)) > FrameSelectorView.this.mMaxWidth) {
                    FrameSelectorView.this.TouchUpToast();
                }
                if (Math.abs(motionEvent.getRawX() - FrameSelectorView.this.start) > 2.0f) {
                    FrameSelectorView.this.upTouch();
                    FrameSelectorView.this.onManualMoveHandle(0, 1);
                } else {
                    FrameSelectorView.this.showProgressView(false);
                }
                MagicLog magicLog6 = MagicLog.INSTANCE;
                magicLog6.showLog("onTouch_mIsTouching: " + FrameSelectorView.this.mIsTouching);
            }
            return true;
        }
    }

    public final void showProgressView(boolean z) {
        if (z) {
            this.mSeekBar.setVisibility(0);
        } else {
            this.mSeekBar.setVisibility(4);
        }
    }

    public final void setMaxView() {
        if (this.mDuration <= this.MAX_TIME) {
            return;
        }
        this.mHandlerLeft.setBackgroundResource(R$drawable.magic_clip_frame_max_left);
        this.mHandlerRight.setBackgroundResource(R$drawable.magic_clip_frame_max_right);
        this.mHandlerBody.setBackgroundResource(R$drawable.magic_frame_selector_max_body);
        ViewGroup.LayoutParams layoutParams = this.mOriginParam;
        layoutParams.width = this.mMaxWidth;
        this.mHandlerBody.setLayoutParams(layoutParams);
        this.isMax = true;
        onTouchMove();
        if (this.mHandlerBarPositionCallback == null) {
            return;
        }
        updateView(this.mHandlerBody.getWidth());
    }

    public final void setMinView() {
        this.mHandlerLeft.setBackgroundResource(R$drawable.magic_clip_frame_max_left);
        this.mHandlerRight.setBackgroundResource(R$drawable.magic_clip_frame_max_right);
        this.mHandlerBody.setBackgroundResource(R$drawable.magic_frame_selector_max_body);
        this.isMax = true;
    }

    public final void setDefView() {
        if (!this.isMax) {
            return;
        }
        this.isMax = false;
        this.mHandlerLeft.setBackgroundResource(R$drawable.magic_clip_frame_left);
        this.mHandlerRight.setBackgroundResource(R$drawable.magic_clip_frame_right);
        this.mHandlerBody.setBackgroundResource(R$drawable.magic_frame_selector_body);
    }

    public final int getLeftMargin(int i) {
        int i2 = this.mOriginLeftMargin + i;
        if (i2 < 0) {
            return 0;
        }
        return i2;
    }

    public final int getRightMargin(int i) {
        int i2 = this.mOriginRightMargin - i;
        if (i2 < 0) {
            return 0;
        }
        return i2;
    }

    /* loaded from: classes2.dex */
    public class HandlerRightTouchListener implements View.OnTouchListener {
        public HandlerRightTouchListener() {
            FrameSelectorView.this = r1;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int i;
            int i2;
            if (BaseMiscUtil.isRTLDirection()) {
                if (motionEvent.getAction() == 0) {
                    FrameSelectorView.this.start = motionEvent.getRawX();
                    FrameSelectorView.this.onManualMoveHandle(0, 0);
                    if (FrameSelectorView.this.mIsTouching) {
                        return false;
                    }
                    FrameSelectorView.this.mOriginX = motionEvent.getRawX();
                    FrameSelectorView frameSelectorView = FrameSelectorView.this;
                    frameSelectorView.mOriginWidth = frameSelectorView.mHandlerBody.getWidth();
                    FrameSelectorView frameSelectorView2 = FrameSelectorView.this;
                    frameSelectorView2.mOriginParam = frameSelectorView2.mHandlerBody.getLayoutParams();
                    FrameSelectorView frameSelectorView3 = FrameSelectorView.this;
                    frameSelectorView3.mOriginLeftMargin = frameSelectorView3.mGroupLayoutParam.leftMargin;
                    FrameSelectorView frameSelectorView4 = FrameSelectorView.this;
                    frameSelectorView4.mOriginRightMargin = frameSelectorView4.mGroupLayoutParam.rightMargin;
                    MagicLog magicLog = MagicLog.INSTANCE;
                    magicLog.showLog("onTouch_mIsTouching: " + FrameSelectorView.this.mIsTouching);
                    FrameSelectorView.this.mIsTouching = true;
                } else if (motionEvent.getAction() == 2) {
                    int rawX = (int) (motionEvent.getRawX() - FrameSelectorView.this.mOriginX);
                    int i3 = (int) ((FrameSelectorView.this.mOriginWidth + FrameSelectorView.this.mOriginRightMargin) - FrameSelectorView.this.mSeekBarThubWidth);
                    if (rawX > FrameSelectorView.this.mOriginRightMargin) {
                        rawX = FrameSelectorView.this.mOriginRightMargin;
                    }
                    MagicLog magicLog2 = MagicLog.INSTANCE;
                    magicLog2.showLog("yzz ---->right RTL: mOriginRightMargin:  " + FrameSelectorView.this.mOriginRightMargin + "  , delta: " + rawX);
                    if (FrameSelectorView.this.getRightMargin(rawX) >= i3 || (i2 = FrameSelectorView.this.mOriginWidth + rawX) < 0) {
                        return true;
                    }
                    if (i2 > FrameSelectorView.this.mMinWidth) {
                        if (i2 <= FrameSelectorView.this.mMaxWidth) {
                            FrameSelectorView.this.mOriginParam.width = i2;
                            FrameSelectorView.this.mHandlerBody.setLayoutParams(FrameSelectorView.this.mOriginParam);
                            FrameSelectorView.this.mGroupLayoutParam.rightMargin = FrameSelectorView.this.getRightMargin(rawX);
                            FrameSelectorView frameSelectorView5 = FrameSelectorView.this;
                            frameSelectorView5.setLayoutParams(frameSelectorView5.mGroupLayoutParam);
                            FrameSelectorView.this.onManualMoveHandle(1, 2);
                            FrameSelectorView.this.setDefView();
                        } else {
                            FrameSelectorView.this.setMaxView();
                        }
                    } else {
                        MagicLog magicLog3 = MagicLog.INSTANCE;
                        magicLog3.showLog("frameselect ---->right RTL all:  " + i2 + "  ,min: " + FrameSelectorView.this.mMinWidth);
                        FrameSelectorView.this.setMinView();
                        return true;
                    }
                } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    FrameSelectorView.this.mIsTouching = false;
                    if (FrameSelectorView.this.mOriginWidth + ((int) (motionEvent.getRawX() - FrameSelectorView.this.mOriginX)) > FrameSelectorView.this.mMaxWidth) {
                        FrameSelectorView.this.TouchUpToast();
                    }
                    if (Math.abs(motionEvent.getRawX() - FrameSelectorView.this.start) > 2.0f) {
                        FrameSelectorView.this.upTouch();
                        FrameSelectorView.this.onManualMoveHandle(0, 1);
                    } else {
                        FrameSelectorView.this.showProgressView(false);
                    }
                    MagicLog magicLog4 = MagicLog.INSTANCE;
                    magicLog4.showLog("onTouch_mIsTouching: " + FrameSelectorView.this.mIsTouching);
                }
            } else if (motionEvent.getAction() == 0) {
                FrameSelectorView.this.start = motionEvent.getRawX();
                FrameSelectorView.this.onManualMoveHandle(1, 0);
                if (FrameSelectorView.this.mIsTouching) {
                    return false;
                }
                FrameSelectorView.this.mOriginX = motionEvent.getRawX();
                FrameSelectorView frameSelectorView6 = FrameSelectorView.this;
                frameSelectorView6.mOriginWidth = frameSelectorView6.mHandlerBody.getWidth();
                FrameSelectorView frameSelectorView7 = FrameSelectorView.this;
                frameSelectorView7.mOriginParam = frameSelectorView7.mHandlerBody.getLayoutParams();
                FrameSelectorView.this.mIsTouching = true;
            } else if (motionEvent.getAction() == 2) {
                int rawX2 = (int) (motionEvent.getRawX() - FrameSelectorView.this.mOriginX);
                if (FrameSelectorView.this.mOriginWidth + rawX2 + FrameSelectorView.this.getBodyLeft() + FrameSelectorView.this.getHandlerWidth() + FrameSelectorView.this.mNegativeMargin >= FrameSelectorView.this.maxViewWidth || (i = FrameSelectorView.this.mOriginWidth + rawX2) < 0) {
                    return true;
                }
                if (i > FrameSelectorView.this.mMinWidth) {
                    if (i < FrameSelectorView.this.mMaxWidth) {
                        FrameSelectorView.this.mOriginParam.width = FrameSelectorView.this.mOriginWidth + rawX2;
                        FrameSelectorView.this.mHandlerBody.setLayoutParams(FrameSelectorView.this.mOriginParam);
                        FrameSelectorView.this.onManualMoveHandle(1, 2);
                        FrameSelectorView.this.setDefView();
                    } else {
                        FrameSelectorView.this.setMaxView();
                    }
                    MagicLog magicLog5 = MagicLog.INSTANCE;
                    magicLog5.showLog("yzz  ---->RightTouch: move:    ,min: " + FrameSelectorView.this.mMinWidth);
                } else {
                    MagicLog magicLog6 = MagicLog.INSTANCE;
                    magicLog6.showLog("frameselect ---->RightTouch: move:  " + i + "  ,min: " + FrameSelectorView.this.mMinWidth);
                    FrameSelectorView.this.setMinView();
                    return true;
                }
            } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                FrameSelectorView.this.mIsTouching = false;
                if (FrameSelectorView.this.mOriginWidth + ((int) (motionEvent.getRawX() - FrameSelectorView.this.mOriginX)) > FrameSelectorView.this.mMaxWidth) {
                    FrameSelectorView.this.TouchUpToast();
                }
                if (Math.abs(motionEvent.getRawX() - FrameSelectorView.this.start) > 2.0f) {
                    FrameSelectorView.this.upTouch();
                    FrameSelectorView.this.onManualMoveHandle(0, 1);
                }
            }
            return true;
        }
    }

    public void TouchUpToast() {
        if (this.mDuration >= this.MAX_TIME) {
            MagicToast.showToast(getContext(), getContext().getString(R$string.magic_video_cut_max, 20));
        }
    }

    /* loaded from: classes2.dex */
    public class HandlerBodyTouchListener implements View.OnTouchListener {
        public HandlerBodyTouchListener() {
            FrameSelectorView.this = r1;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (FrameSelectorView.this.checkProgress(motionEvent)) {
                return false;
            }
            if (BaseMiscUtil.isRTLDirection()) {
                if (motionEvent.getAction() == 0) {
                    FrameSelectorView.this.start = motionEvent.getRawX();
                    FrameSelectorView.this.onManualMoveHandle(0, 0);
                    FrameSelectorView.this.isTouchInSeekBar = false;
                    FrameSelectorView.this.mOriginX = motionEvent.getRawX();
                    FrameSelectorView frameSelectorView = FrameSelectorView.this;
                    frameSelectorView.mOriginRightMargin = frameSelectorView.mGroupLayoutParam.rightMargin;
                    FrameSelectorView frameSelectorView2 = FrameSelectorView.this;
                    frameSelectorView2.mOriginWidth = frameSelectorView2.mHandlerBody.getWidth();
                    FrameSelectorView.this.mIsTouching = true;
                    FrameSelectorView.this.showProgressView(false);
                    MagicLog magicLog = MagicLog.INSTANCE;
                    magicLog.showLog("onTouch_mIsTouching: " + FrameSelectorView.this.mIsTouching);
                } else if (motionEvent.getAction() == 2 && !FrameSelectorView.this.isTouchInSeekBar) {
                    if (Math.abs(motionEvent.getRawX() - FrameSelectorView.this.start) > 3.0f) {
                        int rawX = (int) (motionEvent.getRawX() - FrameSelectorView.this.mOriginX);
                        MagicLog magicLog2 = MagicLog.INSTANCE;
                        magicLog2.showLog("yzz  ---->body: move:  " + rawX);
                        int i = FrameSelectorView.this.mOriginWidth + FrameSelectorView.this.mGroupLayoutParam.rightMargin + FrameSelectorView.this.mLeftAndRightWidth;
                        MagicLog magicLog3 = MagicLog.INSTANCE;
                        magicLog3.showLog("yzz  ---->body: mOriginWidth :  " + FrameSelectorView.this.mOriginWidth + ",mGroupLayoutParam.rightMargin " + FrameSelectorView.this.mGroupLayoutParam.rightMargin + ", all " + i + ", mWindWidth " + FrameSelectorView.this.mWindWidth + ", mLeftAndRightWidth " + FrameSelectorView.this.mLeftAndRightWidth);
                        if (i < FrameSelectorView.this.mWindWidth || rawX > 0) {
                            FrameSelectorView.this.mGroupLayoutParam.rightMargin = FrameSelectorView.this.getRightMargin(rawX);
                            FrameSelectorView frameSelectorView3 = FrameSelectorView.this;
                            frameSelectorView3.setLayoutParams(frameSelectorView3.mGroupLayoutParam);
                            FrameSelectorView.this.onManualMoveHandle(0, 2);
                        }
                    }
                } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    FrameSelectorView.this.mIsTouching = false;
                    FrameSelectorView.this.stopSetProgress = false;
                    MagicLog magicLog4 = MagicLog.INSTANCE;
                    magicLog4.showLog("onTouch_mIsTouching: " + FrameSelectorView.this.mIsTouching);
                    FrameSelectorView.this.isTouchInSeekBar = false;
                    if (Math.abs(motionEvent.getRawX() - FrameSelectorView.this.start) > 2.0f) {
                        FrameSelectorView.this.upTouch();
                        FrameSelectorView.this.onManualMoveHandle(0, 1);
                    } else {
                        FrameSelectorView.this.showProgressView(true);
                    }
                }
            } else if (motionEvent.getAction() == 0) {
                FrameSelectorView.this.start = motionEvent.getRawX();
                FrameSelectorView.this.onManualMoveHandle(0, 0);
                FrameSelectorView.this.isTouchInSeekBar = false;
                FrameSelectorView.this.mOriginX = motionEvent.getRawX();
                FrameSelectorView frameSelectorView4 = FrameSelectorView.this;
                frameSelectorView4.mOriginLeftMargin = frameSelectorView4.mGroupLayoutParam.leftMargin;
                FrameSelectorView frameSelectorView5 = FrameSelectorView.this;
                frameSelectorView5.mOriginWidth = frameSelectorView5.mHandlerBody.getWidth();
                FrameSelectorView.this.mIsTouching = true;
                FrameSelectorView.this.showProgressView(false);
                MagicLog magicLog5 = MagicLog.INSTANCE;
                magicLog5.showLog("onTouch_mIsTouching: " + FrameSelectorView.this.mIsTouching);
            } else if (motionEvent.getAction() == 2 && !FrameSelectorView.this.isTouchInSeekBar) {
                if (Math.abs(motionEvent.getRawX() - FrameSelectorView.this.start) > 3.0f) {
                    int rawX2 = (int) (motionEvent.getRawX() - FrameSelectorView.this.mOriginX);
                    if (FrameSelectorView.this.mOriginWidth + FrameSelectorView.this.mGroupLayoutParam.leftMargin + FrameSelectorView.this.mLeftAndRightWidth < FrameSelectorView.this.mWindWidth || rawX2 < 0) {
                        FrameSelectorView.this.mGroupLayoutParam.leftMargin = FrameSelectorView.this.getLeftMargin(rawX2);
                        FrameSelectorView frameSelectorView6 = FrameSelectorView.this;
                        frameSelectorView6.setLayoutParams(frameSelectorView6.mGroupLayoutParam);
                        FrameSelectorView.this.onManualMoveHandle(0, 2);
                    }
                }
            } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                FrameSelectorView.this.mIsTouching = false;
                FrameSelectorView.this.stopSetProgress = false;
                MagicLog magicLog6 = MagicLog.INSTANCE;
                magicLog6.showLog("onTouch_mIsTouching: " + FrameSelectorView.this.mIsTouching);
                FrameSelectorView.this.isTouchInSeekBar = false;
                if (Math.abs(motionEvent.getRawX() - FrameSelectorView.this.start) > 2.0f) {
                    FrameSelectorView.this.upTouch();
                    FrameSelectorView.this.onManualMoveHandle(0, 1);
                } else {
                    FrameSelectorView.this.showProgressView(true);
                }
            }
            return true;
        }
    }

    public final boolean checkProgress(MotionEvent motionEvent) {
        float width = (this.mSeekBar.getWidth() * this.mSeekBar.getProgress()) / 100.0f;
        float x = motionEvent.getX();
        float f = this.mHotArea;
        float f2 = width - f;
        float f3 = width + f;
        if (x <= f2 || x >= f3) {
            return false;
        }
        this.isTouchInSeekBar = true;
        if (motionEvent.getAction() == 1) {
            this.mSeekBar.onTouchEvent(motionEvent);
        }
        showProgressView(true);
        return true;
    }

    public int getBodyLeft() {
        return this.mGroupLayoutParam.leftMargin;
    }

    public int getBodyRight() {
        return this.mGroupLayoutParam.rightMargin;
    }

    public int getHandlerWidth() {
        return this.mHandlerLeft.getWidth() * 2;
    }

    public int getBodyWidth() {
        return this.mHandlerBody.getWidth();
    }

    public void setSeekBarProgress(int i) {
        if (this.mIsTouching || this.mSeekBar == null || this.stopSetProgress) {
            return;
        }
        int[] currentTimes = getCurrentTimes(this.mDuration);
        double d = (currentTimes[1] - currentTimes[0]) * 1.0d;
        int i2 = i - currentTimes[0];
        if (this.mSeekBar.getVisibility() == 4) {
            this.mSeekBar.setVisibility(0);
        }
        double d2 = (i2 / d) * 100.0d;
        if (d2 - this.mSeekBar.getProgress() > 5.0d) {
            this.mSeekBar.setProgress((int) d2);
        } else {
            this.mSeekBar.setProgress((int) d2);
        }
    }
}
