package com.miui.gallery.vlog.nav;

import android.view.View;
import android.widget.TextView;
import com.miui.gallery.editor.utils.FolmeUtilsEditor;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;

/* loaded from: classes2.dex */
public class VlogNavViewHolder extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder {
    public TextView mTitle;

    public VlogNavViewHolder(View view) {
        super(view);
        FolmeUtilsEditor.animButton(view);
        this.mTitle = (TextView) view.findViewById(R$id.title);
    }

    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder
    public void setSelect(boolean z) {
        this.mTitle.setSelected(z);
    }

    public void setTitle(int i) {
        this.mTitle.setText(i);
    }
}
