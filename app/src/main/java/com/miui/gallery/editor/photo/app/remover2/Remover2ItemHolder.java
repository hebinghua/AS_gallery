package com.miui.gallery.editor.photo.app.remover2;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.Remover2Data;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class Remover2ItemHolder extends RecyclerView.ViewHolder {
    public ImageView mIconView;
    public TextView mLabelView;

    public Remover2ItemHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mLabelView = (TextView) view.findViewById(R.id.labelView);
        this.mIconView = (ImageView) view.findViewById(R.id.iconView);
    }

    public void bind(Remover2Data remover2Data, boolean z) {
        this.mIconView.setImageResource(remover2Data.mIcon);
        this.mLabelView.setText(remover2Data.name);
        if (z) {
            this.mIconView.setColorFilter(-16777216, PorterDuff.Mode.SRC_IN);
        } else {
            this.mIconView.setColorFilter(-1, PorterDuff.Mode.SRC_IN);
        }
    }
}
