package miuix.appcompat.internal.app.widget.actionbar;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$id;
import miuix.internal.util.AttributeResolver;

/* loaded from: classes3.dex */
public class ExpandTitle {
    public Context mContext;
    public TextView mExpandSubtitleView;
    public LinearLayout mExpandTitleLayout;
    public TextView mExpandTitleView;

    /* renamed from: $r8$lambda$7ouH51Qa0YJINlh91g-igAz52Iw */
    public static /* synthetic */ void m2601$r8$lambda$7ouH51Qa0YJINlh91gigAz52Iw(ExpandTitle expandTitle) {
        expandTitle.lambda$init$0();
    }

    public ExpandTitle(Context context) {
        this.mContext = context;
    }

    public void init() {
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        this.mExpandTitleLayout = linearLayout;
        linearLayout.setImportantForAccessibility(2);
        this.mExpandTitleLayout.setEnabled(false);
        this.mExpandTitleLayout.setOrientation(1);
        this.mExpandTitleLayout.post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.actionbar.ExpandTitle$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ExpandTitle.m2601$r8$lambda$7ouH51Qa0YJINlh91gigAz52Iw(ExpandTitle.this);
            }
        });
        TextView textView = new TextView(this.mContext, null, R$attr.expandTitleTheme);
        this.mExpandTitleView = textView;
        textView.setId(R$id.action_bar_title_expand);
        this.mExpandTitleLayout.addView(this.mExpandTitleView, getChildLayoutParams());
        TextView textView2 = new TextView(this.mContext, null, R$attr.expandSubtitleTheme);
        this.mExpandSubtitleView = textView2;
        textView2.setId(R$id.action_bar_subtitle_expand);
        this.mExpandSubtitleView.setVisibility(8);
        this.mExpandTitleLayout.addView(this.mExpandSubtitleView, getChildLayoutParams());
        Resources resources = this.mContext.getResources();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mExpandSubtitleView.getLayoutParams();
        layoutParams.topMargin = resources.getDimensionPixelOffset(R$dimen.action_bar_subtitle_top_margin);
        layoutParams.bottomMargin = resources.getDimensionPixelOffset(R$dimen.action_bar_subtitle_bottom_margin);
    }

    public /* synthetic */ void lambda$init$0() {
        this.mExpandTitleLayout.setBackground(AttributeResolver.resolveDrawable(this.mContext, 16843676));
    }

    public final LinearLayout.LayoutParams getChildLayoutParams() {
        return new LinearLayout.LayoutParams(-2, -2);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mExpandTitleLayout.setOnClickListener(onClickListener);
    }

    public void setTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.mExpandTitleView.setText(charSequence);
        }
    }

    public void setSubTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.mExpandSubtitleView.setText(charSequence);
        }
    }

    public void setEnabled(boolean z) {
        this.mExpandTitleLayout.setEnabled(z);
    }

    public void setSubTitleVisibility(int i) {
        this.mExpandSubtitleView.setVisibility(i);
    }

    public void setTitleVisibility(int i) {
        this.mExpandTitleView.setVisibility(i);
    }

    public void setVisibility(int i) {
        this.mExpandTitleLayout.setVisibility(i);
    }

    public View getLayout() {
        return this.mExpandTitleLayout;
    }
}
