package com.miui.gallery.editor.photo.app.miuibeautify;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautifyData;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class ParameterViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;

    public ParameterViewHolder(View view) {
        super(view);
        FolmeUtil.setCustomTouchAnim(view, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
        this.mTextView = (TextView) view.findViewById(R.id.label);
    }

    public void bind(int i, MiuiBeautifyData miuiBeautifyData) {
        this.mTextView.setCompoundDrawablesWithIntrinsicBounds(0, i, 0, 0);
        this.mTextView.setText(miuiBeautifyData.name);
    }
}
