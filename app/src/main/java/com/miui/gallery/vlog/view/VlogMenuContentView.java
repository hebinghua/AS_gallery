package com.miui.gallery.vlog.view;

import android.content.Context;
import android.widget.FrameLayout;
import com.miui.gallery.vlog.R$layout;

/* loaded from: classes2.dex */
public class VlogMenuContentView extends FrameLayout {
    public Context mContext;

    public VlogMenuContentView(Context context) {
        super(context);
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        FrameLayout.inflate(context, R$layout.vlog_menu_content_layout, this);
    }
}
