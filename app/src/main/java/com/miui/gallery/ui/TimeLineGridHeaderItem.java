package com.miui.gallery.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.tracing.Trace;
import com.miui.gallery.R;
import com.miui.gallery.ui.album.main.base.config.PhotoIconConfig;
import com.miui.gallery.widget.recyclerview.transition.ITransitionalView;

/* loaded from: classes2.dex */
public abstract class TimeLineGridHeaderItem extends RelativeLayout implements CheckableView, ITransitionalView {
    public static String STRING_NULL = "null";
    public static Drawable sHeaderBackground;
    public static int sLastNightMode;
    public int mBehindTextSize;
    public Context mContext;
    public EndInfoHelper mEndInfo;
    public int mFrontTextSize;
    public int mHeight;
    public final SelectGroupHelper mSelectGroupOrNot;
    public TextView mStartInfo;

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return false;
    }

    @Override // android.widget.Checkable
    public void toggle() {
    }

    public TimeLineGridHeaderItem(Context context) {
        this(context, null);
    }

    public TimeLineGridHeaderItem(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TimeLineGridHeaderItem(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Trace.beginSection("TimeLineGridHeaderItemInit");
        this.mContext = context;
        setLayoutDirection(3);
        setId(R.id.header_main);
        this.mFrontTextSize = getContext().getResources().getDimensionPixelSize(R.dimen.home_page_grid_header_front_text);
        this.mBehindTextSize = getContext().getResources().getDimensionPixelSize(R.dimen.timeline_time_text_size);
        TextView textView = new TextView(context);
        this.mStartInfo = textView;
        textView.setId(R.id.start_info);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -1);
        layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.timeline_time_margin_bottom);
        layoutParams.setMarginStart(getResources().getDimensionPixelSize(R.dimen.timeline_time_margin));
        layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.timeline_time_margin_end));
        layoutParams.addRule(20);
        this.mStartInfo.setGravity(80);
        this.mStartInfo.setIncludeFontPadding(true);
        this.mStartInfo.setMaxLines(1);
        this.mStartInfo.setTextAppearance(2131951866);
        this.mStartInfo.setTextDirection(5);
        this.mStartInfo.setTextSize(0, this.mFrontTextSize);
        this.mStartInfo.setEllipsize(TextUtils.TruncateAt.END);
        this.mStartInfo.setLayoutParams(layoutParams);
        addView(this.mStartInfo);
        Trace.endSection();
        ViewStub viewStub = new ViewStub(context, (int) R.layout.time_line_end_info);
        viewStub.setId(R.id.end_info);
        viewStub.setInflatedId(R.id.end_info);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -1);
        setMarginTopAndBottom(layoutParams2);
        layoutParams2.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.recent_header_album_name_margin_end));
        layoutParams2.addRule(21);
        layoutParams2.addRule(17, this.mStartInfo.getId());
        viewStub.setLayoutParams(layoutParams2);
        addView(viewStub);
        ViewStub viewStub2 = new ViewStub(context, (int) R.layout.time_line_select);
        viewStub2.setId(R.id.select_stub);
        viewStub2.setInflatedId(R.id.select_stub);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        setMarginTopAndBottom(layoutParams3);
        layoutParams3.addRule(21);
        layoutParams3.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.timeline_time_margin));
        viewStub2.setLayoutParams(layoutParams3);
        addView(viewStub2);
        this.mEndInfo = new EndInfoHelper(viewStub);
        this.mSelectGroupOrNot = new SelectGroupHelper(getContext(), viewStub2);
        setBackground(getHeaderBackground(getContext()));
    }

    public void setMarginTopAndBottom(RelativeLayout.LayoutParams layoutParams) {
        layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.timeline_time_margin_top);
        layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.timeline_time_margin_bottom);
    }

    public static Drawable getHeaderBackground(Context context) {
        int i = context.getResources().getConfiguration().uiMode & 48;
        if (sHeaderBackground == null || i != sLastNightMode) {
            sHeaderBackground = context.getDrawable(R.drawable.sticky_header_bg);
            sLastNightMode = i;
        }
        return sHeaderBackground;
    }

    public void bindData(CharSequence charSequence, CharSequence charSequence2, boolean z) {
        bindData(charSequence, charSequence2, getResources().getDimensionPixelSize(R.dimen.time_line_header_height), z);
    }

    public void bindData(CharSequence charSequence, CharSequence charSequence2, int i, boolean z) {
        if (!z) {
            i = 1;
        }
        if (i != this.mHeight) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = i;
            this.mHeight = i;
            setLayoutParams(layoutParams);
        }
        this.mStartInfo.setText(charSequence);
        if (charSequence2 != null) {
            this.mEndInfo.setText(charSequence2);
        }
    }

    @Override // android.widget.RelativeLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), i2);
    }

    @Override // com.miui.gallery.ui.Checkable
    public void setCheckable(boolean z) {
        if (z) {
            this.mEndInfo.setVisibility(8);
        } else {
            this.mEndInfo.setVisibility(0);
        }
        this.mSelectGroupOrNot.setCheckable(z);
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        this.mSelectGroupOrNot.setChecked(z);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionalView
    public void updateBackgroundAlpha(int i) {
        Drawable background = getBackground();
        if (background != null) {
            background.setAlpha(i);
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionalView
    public void updateContentAlpha(float f) {
        this.mStartInfo.setAlpha(f);
        this.mEndInfo.setAlpha(f);
    }

    @Override // com.miui.gallery.ui.CheckableView
    public void setCheckableListener(View.OnClickListener onClickListener) {
        this.mSelectGroupOrNot.setCheckableListener(onClickListener);
    }

    /* loaded from: classes2.dex */
    public static class EndInfoHelper {
        public TextView mEndInfo;
        public View.OnClickListener mOnClickListener;
        public final ViewStub mViewStub;
        public int mVisibility = 0;
        public float mAlpha = 1.0f;

        public EndInfoHelper(ViewStub viewStub) {
            this.mViewStub = viewStub;
        }

        public void setText(CharSequence charSequence) {
            if (this.mEndInfo == null) {
                TextView textView = (TextView) this.mViewStub.inflate();
                this.mEndInfo = textView;
                textView.setVisibility(this.mVisibility);
                this.mEndInfo.setAlpha(this.mAlpha);
                this.mEndInfo.setGravity(8388629);
                this.mEndInfo.setOnClickListener(this.mOnClickListener);
            }
            this.mEndInfo.setText(charSequence);
            if (TextUtils.isEmpty(charSequence)) {
                this.mEndInfo.setImportantForAccessibility(2);
            } else {
                this.mEndInfo.setContentDescription(charSequence);
            }
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            TextView textView = this.mEndInfo;
            if (textView == null) {
                this.mOnClickListener = onClickListener;
            } else {
                textView.setOnClickListener(onClickListener);
            }
        }

        public void setVisibility(int i) {
            this.mVisibility = i;
            TextView textView = this.mEndInfo;
            if (textView == null) {
                return;
            }
            textView.setVisibility(i);
        }

        public void setAlpha(float f) {
            this.mAlpha = f;
            TextView textView = this.mEndInfo;
            if (textView == null) {
                return;
            }
            textView.setAlpha(f);
        }
    }

    /* loaded from: classes2.dex */
    public static class SelectGroupHelper {
        public boolean isChecked;
        public final Context mContext;
        public View.OnClickListener mSelectClickListener;
        public final ViewStub mSelectStub;
        public View mSelectView;
        public final Animator.AnimatorListener mSelectViewExitListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.TimeLineGridHeaderItem.SelectGroupHelper.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SelectGroupHelper.this.mSelectView.setVisibility(8);
                SelectGroupHelper.this.mSelectView.setAlpha(1.0f);
            }
        };
        public final ValueAnimator mSelectViewEnterAnimator = PhotoIconConfig.getPhotoIconConfig().getEnterAnimator();
        public final ValueAnimator mSelectViewExitAnimator = PhotoIconConfig.getPhotoIconConfig().getExitAnimator();

        public SelectGroupHelper(Context context, ViewStub viewStub) {
            this.mContext = context;
            this.mSelectStub = viewStub;
        }

        public void setCheckableListener(View.OnClickListener onClickListener) {
            View view = this.mSelectView;
            if (view == null) {
                this.mSelectClickListener = onClickListener;
            } else {
                view.setOnClickListener(onClickListener);
            }
        }

        public void setChecked(boolean z) {
            View view = this.mSelectView;
            if (view == null) {
                this.isChecked = z;
            } else {
                ((TextView) view).setText(this.mContext.getString(z ? R.string.miuix_appcompat_deselect_all : R.string.miuix_appcompat_select_all));
            }
        }

        public void setCheckable(boolean z) {
            if (z) {
                if (this.mSelectView == null) {
                    this.mSelectView = this.mSelectStub.inflate();
                    setCheckableListener(this.mSelectClickListener);
                    setChecked(this.isChecked);
                }
                if (this.mSelectView.getBackground() == null) {
                    this.mSelectView.setBackgroundResource(R.drawable.select_all_button);
                }
                if (this.mSelectViewEnterAnimator.isRunning() || this.mSelectView.getVisibility() == 0) {
                    return;
                }
                this.mSelectView.setVisibility(0);
                this.mSelectViewEnterAnimator.setTarget(this.mSelectView);
                this.mSelectViewEnterAnimator.start();
                return;
            }
            View view = this.mSelectView;
            if (view == null || view.getAlpha() == 0.0f || this.mSelectViewExitAnimator.isRunning()) {
                return;
            }
            this.mSelectViewExitAnimator.setTarget(this.mSelectView);
            this.mSelectViewExitAnimator.addListener(this.mSelectViewExitListener);
            this.mSelectViewExitAnimator.start();
        }
    }
}
