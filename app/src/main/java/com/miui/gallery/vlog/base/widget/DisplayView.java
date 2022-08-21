package com.miui.gallery.vlog.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.xiaomi.milab.videosdk.XmsTextureView;

/* loaded from: classes2.dex */
public class DisplayView extends XmsTextureView {
    public Context mContext;

    public DisplayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
    }
}
