package com.miui.gallery.vlog.ratio;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.R$id;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.entity.RatioData;

/* loaded from: classes2.dex */
public class RatioHolder extends RecyclerView.ViewHolder {
    public ImageView mIcon;
    public TextView mName;

    public RatioHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        TextView textView = (TextView) view.findViewById(R$id.text);
        this.mName = textView;
        textView.setTextSize(0, view.getResources().getDimension(R$dimen.vlog_ratio_item_title_size));
        this.mIcon = (ImageView) view.findViewById(R$id.icon);
    }

    public void bind(RatioData ratioData) {
        this.mName.setText(ratioData.getName());
        this.mIcon.setImageResource(ratioData.getResId());
    }
}
