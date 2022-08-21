package com.miui.gallery.editor.photo.app.remover;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.RemoverData;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class RemoverItemHolder extends RecyclerView.ViewHolder {
    public ImageView mIconView;
    public TextView mLabelView;

    public RemoverItemHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mLabelView = (TextView) view.findViewById(R.id.labelView);
        this.mIconView = (ImageView) view.findViewById(R.id.iconView);
    }

    public void bind(RemoverData removerData, boolean z) {
        this.mIconView.setImageResource(removerData.mIcon);
        this.mLabelView.setText(removerData.name);
        if (z) {
            this.mIconView.setColorFilter(-16777216, PorterDuff.Mode.SRC_IN);
        } else {
            this.mIconView.setColorFilter(-1, PorterDuff.Mode.SRC_IN);
        }
    }
}
