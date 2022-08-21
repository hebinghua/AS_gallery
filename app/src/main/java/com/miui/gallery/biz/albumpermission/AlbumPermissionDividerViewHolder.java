package com.miui.gallery.biz.albumpermission;

import android.widget.ImageView;
import android.widget.LinearLayout;
import com.miui.gallery.R;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AlbumPermissionDividerViewHolder.kt */
/* loaded from: classes.dex */
public final class AlbumPermissionDividerViewHolder extends BaseViewHolder {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AlbumPermissionDividerViewHolder(ImageView divider) {
        super(divider);
        Intrinsics.checkNotNullParameter(divider, "divider");
        divider.setBackgroundResource(R.drawable.divider_line_panel);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 2);
        layoutParams.topMargin = divider.getContext().getResources().getDimensionPixelSize(R.dimen.album_permission_divider_margin_top_bottom);
        layoutParams.bottomMargin = divider.getContext().getResources().getDimensionPixelSize(R.dimen.album_permission_divider_margin_top_bottom);
        divider.setLayoutParams(layoutParams);
        divider.setPadding(divider.getContext().getResources().getDimensionPixelSize(R.dimen.panel_item_padding), 0, divider.getContext().getResources().getDimensionPixelSize(R.dimen.panel_item_padding), 0);
    }
}
