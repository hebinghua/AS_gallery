package com.miui.gallery.editor.photo.core.imports.text.typeface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;
import java.util.List;

/* loaded from: classes2.dex */
public class TypeFaceAdapter extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter<TypeFaceHolder> {
    public LayoutInflater mLayoutInflater;
    public List<TextStyle> mTextStyleList;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List list) {
        onBindViewHolder((TypeFaceHolder) viewHolder, i, (List<Object>) list);
    }

    public TypeFaceAdapter(Context context, List<TextStyle> list) {
        this.mTextStyleList = list;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    /* renamed from: onCreateSingleChoiceViewHolder */
    public TypeFaceHolder mo1797onCreateSingleChoiceViewHolder(ViewGroup viewGroup, int i) {
        return new TypeFaceHolder(this.mLayoutInflater.inflate(R.layout.text_edit_dialog_options_font_panel_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    public void onBindView(TypeFaceHolder typeFaceHolder, int i) {
        TextStyle textStyle = this.mTextStyleList.get(i);
        typeFaceHolder.bind(textStyle, i == getSelectedItemPosition());
        if (textStyle.isDownloadSuccess()) {
            textStyle.setState(17);
        }
    }

    public void onBindViewHolder(TypeFaceHolder typeFaceHolder, int i, List<Object> list) {
        if (list.isEmpty()) {
            onBindViewHolder((TypeFaceAdapter) typeFaceHolder, i);
            return;
        }
        TextStyle textStyle = this.mTextStyleList.get(i);
        typeFaceHolder.bind(textStyle, i == getSelectedItemPosition());
        if (!textStyle.isDownloadSuccess()) {
            return;
        }
        textStyle.setState(17);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<TextStyle> list = this.mTextStyleList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }
}
