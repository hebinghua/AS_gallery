package miuix.appcompat.internal.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageView;
import miuix.appcompat.R$id;
import miuix.appcompat.R$string;
import miuix.appcompat.internal.app.widget.actionbar.CollapseTitle;
import miuix.appcompat.internal.app.widget.actionbar.ExpandTitle;
import miuix.internal.util.AttributeResolver;

/* loaded from: classes3.dex */
public class ActionBarViewFactory {
    /* renamed from: $r8$lambda$aajRVFUsse6Ne9K6wQiW-AOVnN4 */
    public static /* synthetic */ void m2602$r8$lambda$aajRVFUsse6Ne9K6wQiWAOVnN4(AppCompatImageView appCompatImageView, Context context) {
        lambda$generateTitleUpView$0(appCompatImageView, context);
    }

    public static View generateTitleUpView(final Context context, ViewGroup viewGroup) {
        final AppCompatImageView appCompatImageView = new AppCompatImageView(context);
        appCompatImageView.setId(R$id.up);
        appCompatImageView.setVisibility(8);
        appCompatImageView.post(new Runnable() { // from class: miuix.appcompat.internal.util.ActionBarViewFactory$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ActionBarViewFactory.m2602$r8$lambda$aajRVFUsse6Ne9K6wQiWAOVnN4(AppCompatImageView.this, context);
            }
        });
        if (viewGroup != null) {
            viewGroup.addView(appCompatImageView);
        }
        return appCompatImageView;
    }

    public static /* synthetic */ void lambda$generateTitleUpView$0(AppCompatImageView appCompatImageView, Context context) {
        appCompatImageView.setImageDrawable(AttributeResolver.resolveDrawable(context, 16843531));
        appCompatImageView.setContentDescription(context.getResources().getString(R$string.actionbar_button_up_description));
    }

    public static ExpandTitle generateExpandTitle(Context context) {
        ExpandTitle expandTitle = new ExpandTitle(context);
        expandTitle.init();
        return expandTitle;
    }

    public static CollapseTitle generateCollapseTitle(Context context, int i, int i2) {
        CollapseTitle collapseTitle = new CollapseTitle(context, i, i2);
        collapseTitle.init();
        return collapseTitle;
    }
}
