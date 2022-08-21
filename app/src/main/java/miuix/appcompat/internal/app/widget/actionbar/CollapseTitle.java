package miuix.appcompat.internal.app.widget.actionbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$drawable;
import miuix.appcompat.R$id;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.DeviceHelper;

/* loaded from: classes3.dex */
public class CollapseTitle {
    public int mCollapseSubtitleStyle;
    public TextView mCollapseSubtitleView;
    public LinearLayout mCollapseTitleLayout;
    public int mCollapseTitleStyle;
    public TextView mCollapseTitleView;
    public Context mContext;
    public float mDefaultSubtitleSize = 0.0f;
    public boolean mIsTitleDirty = false;
    public float mTitleLength = 0.0f;

    public static /* synthetic */ void $r8$lambda$EGdFs28b4GQPurlZx0PwOD497oI(CollapseTitle collapseTitle) {
        collapseTitle.lambda$init$0();
    }

    public static /* synthetic */ void $r8$lambda$amF6b5eLzHJMObzMKvMs7nWhCoQ(CollapseTitle collapseTitle) {
        collapseTitle.onPortraitChange();
    }

    public static /* synthetic */ void $r8$lambda$gHkx_L0bhEvL4pyCtJlNpeE5XOs(CollapseTitle collapseTitle) {
        collapseTitle.lambda$init$1();
    }

    /* renamed from: $r8$lambda$zCkPLxbiZGhwuYhkWcw-u3Uhepc */
    public static /* synthetic */ void m2600$r8$lambda$zCkPLxbiZGhwuYhkWcwu3Uhepc(CollapseTitle collapseTitle) {
        collapseTitle.onLandscapeChange();
    }

    public CollapseTitle(Context context, int i, int i2) {
        this.mContext = context;
        this.mCollapseTitleStyle = i;
        this.mCollapseSubtitleStyle = i2;
    }

    public void init() {
        Resources resources = this.mContext.getResources();
        int i = 1;
        boolean z = resources.getConfiguration().orientation == 2;
        if (DeviceHelper.isTablet(this.mContext) || !z) {
            i = 0;
        }
        this.mDefaultSubtitleSize = resources.getDimensionPixelSize(R$dimen.miuix_appcompat_subtitle_text_size);
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        this.mCollapseTitleLayout = linearLayout;
        linearLayout.setImportantForAccessibility(2);
        this.mCollapseTitleView = new TextView(this.mContext, null, R$attr.collapseTitleTheme);
        this.mCollapseSubtitleView = new TextView(this.mContext, null, R$attr.collapseSubtitleTheme);
        this.mCollapseTitleLayout.setEnabled(false);
        this.mCollapseTitleLayout.setOrientation(i ^ 1);
        this.mCollapseTitleLayout.post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.actionbar.CollapseTitle$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CollapseTitle.$r8$lambda$EGdFs28b4GQPurlZx0PwOD497oI(CollapseTitle.this);
            }
        });
        this.mCollapseTitleView.setId(R$id.action_bar_title);
        this.mCollapseTitleLayout.addView(this.mCollapseTitleView, getChildLayoutParams());
        this.mCollapseSubtitleView.setId(R$id.action_bar_subtitle);
        this.mCollapseSubtitleView.setVisibility(8);
        if (i != 0) {
            this.mCollapseSubtitleView.post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.actionbar.CollapseTitle$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    CollapseTitle.$r8$lambda$gHkx_L0bhEvL4pyCtJlNpeE5XOs(CollapseTitle.this);
                }
            });
        }
        this.mCollapseTitleLayout.addView(this.mCollapseSubtitleView, getChildLayoutParams());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mCollapseSubtitleView.getLayoutParams();
        if (i != 0) {
            layoutParams.setMarginStart(resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_subtitle_start_margin));
            return;
        }
        layoutParams.topMargin = resources.getDimensionPixelOffset(R$dimen.action_bar_subtitle_top_margin);
        layoutParams.bottomMargin = resources.getDimensionPixelOffset(R$dimen.action_bar_subtitle_bottom_margin);
    }

    public /* synthetic */ void lambda$init$0() {
        this.mCollapseTitleLayout.setBackground(AttributeResolver.resolveDrawable(this.mContext, 16843676));
    }

    public /* synthetic */ void lambda$init$1() {
        this.mCollapseSubtitleView.setBackgroundResource(R$drawable.miuix_appcompat_action_bar_subtitle_bg_land);
    }

    public final LinearLayout.LayoutParams getChildLayoutParams() {
        return new LinearLayout.LayoutParams(-2, -2);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mCollapseTitleLayout.setOnClickListener(onClickListener);
    }

    public void setTitle(CharSequence charSequence) {
        if (!TextUtils.equals(charSequence, this.mCollapseTitleView.getText())) {
            this.mCollapseTitleView.setText(charSequence);
            this.mIsTitleDirty = true;
        }
    }

    public void setSubTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.mCollapseSubtitleView.setText(charSequence);
        }
    }

    public void setEnabled(boolean z) {
        this.mCollapseTitleLayout.setEnabled(z);
    }

    public void setSubTitleVisibility(int i) {
        if (this.mCollapseSubtitleView.getVisibility() != i) {
            this.mCollapseSubtitleView.setVisibility(i);
        }
    }

    public void setSubTitleTextSize(float f) {
        this.mCollapseSubtitleView.setTextSize(0, f);
    }

    public void setTitleVisibility(int i) {
        if (this.mCollapseTitleView.getVisibility() != i) {
            this.mCollapseTitleView.setVisibility(i);
        }
    }

    public ViewGroup getTitleParent() {
        return (ViewGroup) this.mCollapseTitleView.getParent();
    }

    public void setVisibility(int i) {
        this.mCollapseTitleLayout.setVisibility(i);
    }

    public int getVisibility() {
        return this.mCollapseTitleLayout.getVisibility();
    }

    public View getLayout() {
        return this.mCollapseTitleLayout;
    }

    public Rect getHitRect() {
        Rect rect = new Rect();
        this.mCollapseTitleLayout.getHitRect(rect);
        return rect;
    }

    public void updateTitleCenter(boolean z) {
        ViewGroup titleParent = getTitleParent();
        int i = 1;
        if (titleParent instanceof LinearLayout) {
            ((LinearLayout) titleParent).setGravity((z ? 1 : 8388611) | 16);
        }
        this.mCollapseTitleView.setGravity((z ? 1 : 8388611) | 16);
        this.mCollapseTitleView.setEllipsize(TextUtils.TruncateAt.END);
        TextView textView = this.mCollapseSubtitleView;
        if (!z) {
            i = 8388611;
        }
        textView.setGravity(i | 16);
        this.mCollapseSubtitleView.setEllipsize(TextUtils.TruncateAt.END);
    }

    public boolean canTitleBeShown(String str) {
        if (this.mIsTitleDirty) {
            this.mTitleLength = this.mCollapseTitleView.getPaint().measureText(str);
            this.mIsTitleDirty = false;
        }
        return this.mTitleLength <= ((float) this.mCollapseTitleView.getMeasuredWidth());
    }

    public float getSubtitleAdjustSize() {
        float f = this.mDefaultSubtitleSize;
        Resources resources = this.mContext.getResources();
        int min = (Math.min(this.mCollapseTitleLayout.getMeasuredHeight() - this.mCollapseTitleView.getMeasuredHeight(), this.mCollapseSubtitleView.getMeasuredHeight()) - this.mCollapseSubtitleView.getPaddingTop()) - this.mCollapseSubtitleView.getPaddingBottom();
        if (min <= 0) {
            return f;
        }
        TextPaint textPaint = new TextPaint(this.mCollapseSubtitleView.getPaint());
        textPaint.setTextSize(f);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int ceil = (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
        float f2 = f / 2.0f;
        float f3 = resources.getDisplayMetrics().scaledDensity;
        while (ceil > min && f >= f2) {
            f -= f3;
            textPaint.setTextSize(f);
            Paint.FontMetrics fontMetrics2 = textPaint.getFontMetrics();
            ceil = (int) Math.ceil(fontMetrics2.descent - fontMetrics2.ascent);
        }
        return f;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (!DeviceHelper.isTablet(this.mContext)) {
            if (configuration.orientation == 2) {
                this.mCollapseSubtitleView.post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.actionbar.CollapseTitle$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        CollapseTitle.m2600$r8$lambda$zCkPLxbiZGhwuYhkWcwu3Uhepc(CollapseTitle.this);
                    }
                });
            } else {
                this.mCollapseSubtitleView.post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.actionbar.CollapseTitle$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        CollapseTitle.$r8$lambda$amF6b5eLzHJMObzMKvMs7nWhCoQ(CollapseTitle.this);
                    }
                });
            }
        }
    }

    public final void onLandscapeChange() {
        Resources resources = this.mContext.getResources();
        this.mCollapseTitleLayout.setOrientation(0);
        this.mCollapseSubtitleView.setTextAppearance(this.mContext, this.mCollapseTitleStyle);
        this.mCollapseSubtitleView.setBackgroundResource(R$drawable.miuix_appcompat_action_bar_subtitle_bg_land);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mCollapseSubtitleView.getLayoutParams();
        layoutParams.setMarginStart(resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_subtitle_start_margin));
        layoutParams.topMargin = 0;
        layoutParams.bottomMargin = 0;
        this.mCollapseSubtitleView.setLayoutParams(layoutParams);
    }

    public final void onPortraitChange() {
        Resources resources = this.mContext.getResources();
        this.mCollapseTitleLayout.setOrientation(1);
        this.mCollapseSubtitleView.setTextAppearance(this.mContext, this.mCollapseSubtitleStyle);
        this.mCollapseSubtitleView.setBackground(null);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mCollapseSubtitleView.getLayoutParams();
        layoutParams.setMarginStart(0);
        layoutParams.topMargin = resources.getDimensionPixelOffset(R$dimen.action_bar_subtitle_top_margin);
        layoutParams.bottomMargin = resources.getDimensionPixelOffset(R$dimen.action_bar_subtitle_bottom_margin);
        this.mCollapseSubtitleView.setPadding(0, 0, 0, 0);
        this.mCollapseSubtitleView.setLayoutParams(layoutParams);
        setSubTitleTextSize(getSubtitleAdjustSize());
    }
}
