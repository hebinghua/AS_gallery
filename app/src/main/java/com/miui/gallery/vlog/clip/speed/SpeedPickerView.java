package com.miui.gallery.vlog.clip.speed;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.gallery.baseui.R$color;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;

/* loaded from: classes2.dex */
public class SpeedPickerView extends LinearLayout implements View.OnClickListener {
    public static final double[] SUPPORT_SPEEDS = {3.0d, 2.0d, 1.0d, 0.5d, 0.25d};
    public TextView mDoubleTv;
    public TextView mHalfTv;
    public int mIndicatorColor;
    public float mIndicatorCornerRadius;
    public GradientDrawable mIndicatorDrawable;
    public int mIndicatorHeight;
    public Rect mIndicatorRect;
    public int mIndicatorWidth;
    public TextView mNormalTv;
    public TextView mQuarterTv;
    public Speed mSpeed;
    public SpeedCallback mSpeedCallback;
    public int mTextColorDisable;
    public int mTextColorNormal;
    public int mTextColorSelect;
    public TextView mTrebleTv;

    /* loaded from: classes2.dex */
    public interface SpeedCallback {
        void onSpeedReselect(Speed speed);

        void onSpeedSelect(Speed speed);
    }

    public SpeedPickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIndicatorDrawable = new GradientDrawable();
        this.mIndicatorRect = new Rect();
        init(context);
    }

    public final TextView createAndAddTextView(Context context, int i, String str) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R$layout.vlog_speed_picker_item_layout, (ViewGroup) this, false);
        textView.setText(getResources().getText(i));
        textView.setOnClickListener(this);
        textView.setContentDescription(str);
        addView(textView);
        return textView;
    }

    public final void init(Context context) {
        setOrientation(0);
        setBackground(getResources().getDrawable(R$drawable.vlog_speed_picker_bg, null));
        this.mTrebleTv = createAndAddTextView(context, R$string.vlog_speed_treble, getResources().getString(R$string.vlog_talkback_speed_treble));
        this.mDoubleTv = createAndAddTextView(context, R$string.vlog_speed_double, getResources().getString(R$string.vlog_talkback_speed_double));
        this.mNormalTv = createAndAddTextView(context, R$string.vlog_speed_normal, getResources().getString(R$string.vlog_talkback_speed_normal));
        this.mHalfTv = createAndAddTextView(context, R$string.vlog_speed_half, getResources().getString(R$string.vlog_talkback_speed_half));
        this.mQuarterTv = createAndAddTextView(context, R$string.vlog_speed_quarter, getResources().getString(R$string.vlog_talkback_speed_quarter));
        initAnim();
        this.mIndicatorWidth = (int) getResources().getDimension(R$dimen.vlog_single_clip_speed_picker_indicator_width);
        this.mIndicatorHeight = (int) getResources().getDimension(R$dimen.vlog_single_clip_speed_picker_indicator_height);
        this.mTextColorNormal = getResources().getColor(R$color.white, null);
        this.mTextColorDisable = getResources().getColor(R$color.white_30_transparent, null);
        this.mTextColorSelect = getResources().getColor(R$color.black, null);
        this.mIndicatorCornerRadius = getResources().getDimension(R$dimen.vlog_single_clip_speed_picker_indicator_corner_radius);
        int color = getResources().getColor(com.miui.gallery.vlog.R$color.vlog_speed_indicator_color, null);
        this.mIndicatorColor = color;
        this.mIndicatorDrawable.setColor(color);
        this.mIndicatorDrawable.setCornerRadius(this.mIndicatorCornerRadius);
        this.mSpeed = Speed.NORMAL;
        this.mNormalTv.setTextColor(-16777216);
    }

    public final void initAnim() {
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(this.mTrebleTv, build, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mDoubleTv, build, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mNormalTv, build, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mHalfTv, build, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mQuarterTv, build, null, null, true);
    }

    public final void setItemEnable(TextView textView, boolean z) {
        if (textView == getViewBySpeed(this.mSpeed)) {
            textView.setTextColor(this.mTextColorSelect);
            return;
        }
        if (z) {
            textView.setTextColor(this.mTextColorNormal);
        } else {
            textView.setTextColor(this.mTextColorDisable);
        }
        textView.setEnabled(z);
    }

    public void setItemEnables(boolean[] zArr) {
        setItemEnable(this.mTrebleTv, zArr[0]);
        setItemEnable(this.mDoubleTv, zArr[1]);
        setItemEnable(this.mNormalTv, zArr[2]);
        setItemEnable(this.mHalfTv, zArr[3]);
        setItemEnable(this.mQuarterTv, zArr[4]);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mIndicatorRect.top = getPaddingTop();
        Rect rect = this.mIndicatorRect;
        rect.bottom = rect.top + this.mIndicatorHeight;
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calcIndicatorRect(getViewBySpeed(this.mSpeed));
        this.mIndicatorDrawable.setBounds(this.mIndicatorRect);
        this.mIndicatorDrawable.draw(canvas);
    }

    public final void updateIndicatorDrawable() {
        invalidate();
    }

    public final void calcIndicatorRect(View view) {
        this.mIndicatorRect.left = (int) (view.getX() - ((this.mIndicatorWidth - view.getWidth()) / 2));
        Rect rect = this.mIndicatorRect;
        rect.right = rect.left + this.mIndicatorWidth;
    }

    public final void onSpeedReselect(Speed speed) {
        SpeedCallback speedCallback = this.mSpeedCallback;
        if (speedCallback != null) {
            speedCallback.onSpeedReselect(speed);
        }
    }

    public final void onSpeedSelect(Speed speed) {
        updateIndicatorDrawable();
        SpeedCallback speedCallback = this.mSpeedCallback;
        if (speedCallback != null) {
            speedCallback.onSpeedSelect(speed);
        }
    }

    public final Speed getSpeedFromView(View view) {
        Speed speed = Speed.NORMAL;
        if (view == this.mTrebleTv) {
            return Speed.TREBLE;
        }
        if (view == this.mDoubleTv) {
            return Speed.DOUBLE;
        }
        if (view == this.mNormalTv) {
            return speed;
        }
        if (view == this.mHalfTv) {
            return Speed.HALF;
        }
        return view == this.mQuarterTv ? Speed.QUARTER : speed;
    }

    /* renamed from: com.miui.gallery.vlog.clip.speed.SpeedPickerView$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed;

        static {
            int[] iArr = new int[Speed.values().length];
            $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed = iArr;
            try {
                iArr[Speed.TREBLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.DOUBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.HALF.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.QUARTER.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.NORMAL.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public final TextView getViewBySpeed(Speed speed) {
        TextView textView = this.mNormalTv;
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[speed.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return this.mDoubleTv;
            }
            if (i == 3) {
                return this.mHalfTv;
            }
            return i != 4 ? textView : this.mQuarterTv;
        }
        return this.mTrebleTv;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Speed speedFromView = getSpeedFromView(view);
        Speed speed = this.mSpeed;
        if (speed == speedFromView) {
            onSpeedReselect(speed);
            return;
        }
        getViewBySpeed(speed).setTextColor(this.mTextColorNormal);
        this.mSpeed = speedFromView;
        getViewBySpeed(speedFromView).setTextColor(this.mTextColorSelect);
        onSpeedSelect(this.mSpeed);
    }

    public void setSpeedCallback(SpeedCallback speedCallback) {
        this.mSpeedCallback = speedCallback;
    }

    public double[] getSupportSpeeds() {
        return SUPPORT_SPEEDS;
    }
}
