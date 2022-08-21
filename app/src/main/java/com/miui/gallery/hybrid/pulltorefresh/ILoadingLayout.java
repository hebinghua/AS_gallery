package com.miui.gallery.hybrid.pulltorefresh;

import android.graphics.drawable.Drawable;

/* loaded from: classes2.dex */
public interface ILoadingLayout {
    void setLastUpdatedLabel(CharSequence charSequence);

    void setLoadingDrawable(Drawable drawable);

    void setPullLabel(CharSequence charSequence);

    void setRefreshingLabel(CharSequence charSequence);

    void setReleaseLabel(CharSequence charSequence);
}
