package com.miui.gallery.hybrid.pulltorefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase;

@SuppressLint({"ViewConstructor"})
/* loaded from: classes2.dex */
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {
    public static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    public final ImageView mHeaderImage;
    public final ProgressBar mHeaderProgress;
    public final TextView mHeaderText;
    public View mInnerLayout;
    public final PullToRefreshBase.Mode mMode;
    public CharSequence mPullLabel;
    public CharSequence mRefreshingLabel;
    public CharSequence mReleaseLabel;
    public final PullToRefreshBase.Orientation mScrollDirection;
    public final TextView mSubHeaderText;
    public boolean mUseIntrinsicAnimation;

    public abstract int getDefaultDrawableResId();

    public abstract void onLoadingDrawableSet(Drawable drawable);

    public abstract void onPullImpl(float f);

    public abstract void pullToRefreshImpl();

    public abstract void refreshingImpl();

    public abstract void releaseToRefreshImpl();

    public abstract void resetImpl();

    public LoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation orientation, TypedArray typedArray) {
        super(context);
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        Drawable drawable;
        this.mMode = mode;
        this.mScrollDirection = orientation;
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[orientation.ordinal()];
        LayoutInflater.from(context).inflate(R.layout.hybrid_pull_to_refresh_header_vertical, this);
        View findViewById = findViewById(R.id.fl_inner);
        this.mInnerLayout = findViewById;
        this.mHeaderText = (TextView) findViewById.findViewById(R.id.pull_to_refresh_text);
        this.mHeaderProgress = (ProgressBar) this.mInnerLayout.findViewById(R.id.pull_to_refresh_progress);
        this.mSubHeaderText = (TextView) this.mInnerLayout.findViewById(R.id.pull_to_refresh_sub_text);
        this.mHeaderImage = (ImageView) this.mInnerLayout.findViewById(R.id.pull_to_refresh_image);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mInnerLayout.getLayoutParams();
        int[] iArr = AnonymousClass1.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode;
        int i2 = iArr[mode.ordinal()];
        layoutParams.gravity = orientation == PullToRefreshBase.Orientation.VERTICAL ? 80 : 5;
        this.mPullLabel = context.getString(R.string.hybrid_pull_to_refresh_pull_label);
        this.mRefreshingLabel = context.getString(R.string.hybrid_pull_to_refresh_refreshing_label);
        this.mReleaseLabel = context.getString(R.string.hybrid_pull_to_refresh_release_label);
        if (typedArray.hasValue(7) && (drawable = typedArray.getDrawable(7)) != null) {
            ViewCompat.setBackground(this, drawable);
        }
        if (typedArray.hasValue(9)) {
            TypedValue typedValue = new TypedValue();
            typedArray.getValue(9, typedValue);
            setTextAppearance(typedValue.data);
        }
        if (typedArray.hasValue(19)) {
            TypedValue typedValue2 = new TypedValue();
            typedArray.getValue(19, typedValue2);
            setSubTextAppearance(typedValue2.data);
        }
        if (typedArray.hasValue(10) && (colorStateList2 = typedArray.getColorStateList(10)) != null) {
            setTextColor(colorStateList2);
        }
        if (typedArray.hasValue(8) && (colorStateList = typedArray.getColorStateList(8)) != null) {
            setSubTextColor(colorStateList);
        }
        Drawable drawable2 = typedArray.hasValue(2) ? typedArray.getDrawable(2) : null;
        if (iArr[mode.ordinal()] != 2) {
            if (typedArray.hasValue(5)) {
                drawable2 = typedArray.getDrawable(5);
            } else if (typedArray.hasValue(6)) {
                drawable2 = typedArray.getDrawable(6);
            }
        } else if (typedArray.hasValue(4)) {
            drawable2 = typedArray.getDrawable(4);
        } else if (typedArray.hasValue(3)) {
            drawable2 = typedArray.getDrawable(3);
        }
        setLoadingDrawable(drawable2 == null ? context.getResources().getDrawable(getDefaultDrawableResId()) : drawable2);
        reset();
    }

    /* renamed from: com.miui.gallery.hybrid.pulltorefresh.LoadingLayout$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode;
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation;

        static {
            int[] iArr = new int[PullToRefreshBase.Mode.values().length];
            $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode = iArr;
            try {
                iArr[PullToRefreshBase.Mode.PULL_FROM_START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode[PullToRefreshBase.Mode.PULL_FROM_END.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            int[] iArr2 = new int[PullToRefreshBase.Orientation.values().length];
            $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation = iArr2;
            try {
                iArr2[PullToRefreshBase.Orientation.VERTICAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[PullToRefreshBase.Orientation.HORIZONTAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public final void setHeight(int i) {
        getLayoutParams().height = i;
        requestLayout();
    }

    public final void setWidth(int i) {
        getLayoutParams().width = i;
        requestLayout();
    }

    public final int getContentSize() {
        if (AnonymousClass1.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[this.mScrollDirection.ordinal()] == 2) {
            return this.mInnerLayout.getWidth();
        }
        return this.mInnerLayout.getHeight();
    }

    public final void onPull(float f) {
        if (!this.mUseIntrinsicAnimation) {
            onPullImpl(f);
        }
    }

    public final void pullToRefresh() {
        TextView textView = this.mHeaderText;
        if (textView != null) {
            textView.setText(this.mPullLabel);
        }
        pullToRefreshImpl();
    }

    public final void refreshing() {
        TextView textView = this.mHeaderText;
        if (textView != null) {
            textView.setText(this.mRefreshingLabel);
        }
        if (this.mUseIntrinsicAnimation) {
            ((AnimationDrawable) this.mHeaderImage.getDrawable()).start();
        } else {
            refreshingImpl();
        }
        TextView textView2 = this.mSubHeaderText;
        if (textView2 != null) {
            textView2.setVisibility(8);
        }
    }

    public final void releaseToRefresh() {
        TextView textView = this.mHeaderText;
        if (textView != null) {
            textView.setText(this.mReleaseLabel);
        }
        releaseToRefreshImpl();
    }

    public final void reset() {
        TextView textView = this.mHeaderText;
        if (textView != null) {
            textView.setText(this.mPullLabel);
        }
        this.mHeaderImage.setVisibility(0);
        if (this.mUseIntrinsicAnimation) {
            ((AnimationDrawable) this.mHeaderImage.getDrawable()).stop();
        } else {
            resetImpl();
        }
        TextView textView2 = this.mSubHeaderText;
        if (textView2 != null) {
            if (TextUtils.isEmpty(textView2.getText())) {
                this.mSubHeaderText.setVisibility(8);
            } else {
                this.mSubHeaderText.setVisibility(0);
            }
        }
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.ILoadingLayout
    public void setLastUpdatedLabel(CharSequence charSequence) {
        setSubHeaderText(charSequence);
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.ILoadingLayout
    public final void setLoadingDrawable(Drawable drawable) {
        this.mHeaderImage.setImageDrawable(drawable);
        this.mUseIntrinsicAnimation = drawable instanceof AnimationDrawable;
        onLoadingDrawableSet(drawable);
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.ILoadingLayout
    public void setPullLabel(CharSequence charSequence) {
        this.mPullLabel = charSequence;
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.ILoadingLayout
    public void setRefreshingLabel(CharSequence charSequence) {
        this.mRefreshingLabel = charSequence;
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.ILoadingLayout
    public void setReleaseLabel(CharSequence charSequence) {
        this.mReleaseLabel = charSequence;
    }

    public void setTextTypeface(Typeface typeface) {
        this.mHeaderText.setTypeface(typeface);
    }

    private void setSubHeaderText(CharSequence charSequence) {
        if (this.mSubHeaderText != null) {
            if (TextUtils.isEmpty(charSequence)) {
                this.mSubHeaderText.setVisibility(8);
                return;
            }
            this.mSubHeaderText.setText(charSequence);
            if (8 != this.mSubHeaderText.getVisibility()) {
                return;
            }
            this.mSubHeaderText.setVisibility(0);
        }
    }

    private void setSubTextAppearance(int i) {
        TextView textView = this.mSubHeaderText;
        if (textView != null) {
            textView.setTextAppearance(getContext(), i);
        }
    }

    private void setSubTextColor(ColorStateList colorStateList) {
        TextView textView = this.mSubHeaderText;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    private void setTextAppearance(int i) {
        TextView textView = this.mHeaderText;
        if (textView != null) {
            textView.setTextAppearance(getContext(), i);
        }
        TextView textView2 = this.mSubHeaderText;
        if (textView2 != null) {
            textView2.setTextAppearance(getContext(), i);
        }
    }

    private void setTextColor(ColorStateList colorStateList) {
        TextView textView = this.mHeaderText;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
        TextView textView2 = this.mSubHeaderText;
        if (textView2 != null) {
            textView2.setTextColor(colorStateList);
        }
    }
}
