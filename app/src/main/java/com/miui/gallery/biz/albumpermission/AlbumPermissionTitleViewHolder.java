package com.miui.gallery.biz.albumpermission;

import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AlbumPermissionTitleViewHolder.kt */
/* loaded from: classes.dex */
public final class AlbumPermissionTitleViewHolder extends BaseViewHolder {
    public final TextView title;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AlbumPermissionTitleViewHolder(TextView title) {
        super(title);
        Intrinsics.checkNotNullParameter(title, "title");
        this.title = title;
        title.setTextSize(0, title.getContext().getResources().getDimensionPixelSize(R.dimen.album_permission_title_text_size));
        title.setTextColor(title.getContext().getResources().getColor(R.color.photo_page_list_header_color, title.getContext().getTheme()));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.bottomMargin = title.getContext().getResources().getDimensionPixelSize(R.dimen.album_permission_title_margin_bottom);
        title.setLayoutParams(layoutParams);
    }

    public final void bind(int i) {
        if (i == 1) {
            TextView textView = this.title;
            textView.setText(textView.getContext().getString(R.string.album_permission_title_non_granted));
        } else if (i != 2) {
        } else {
            TextView textView2 = this.title;
            textView2.setText(textView2.getContext().getString(R.string.album_permission_title_granted));
        }
    }
}
