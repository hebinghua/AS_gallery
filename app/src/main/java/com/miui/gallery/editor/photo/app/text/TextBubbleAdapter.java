package com.miui.gallery.editor.photo.app.text;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.editor.photo.core.imports.text.TextConfig;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class TextBubbleAdapter extends Adapter<TextBubbleHolder> implements Selectable {
    public Context mContext;
    public Selectable.Delegator mDelegator;
    public List<TextData> mTextDataList;

    public TextBubbleAdapter(Context context, List<TextData> list, int i) {
        this.mTextDataList = list;
        this.mContext = context;
        this.mDelegator = new Selectable.Delegator(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public TextBubbleHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new TextBubbleHolder(LayoutInflater.from(this.mContext).inflate(R.layout.text_signature_divider_menu_item, viewGroup, false));
        }
        return new TextBubbleHolder(LayoutInflater.from(this.mContext).inflate(R.layout.text_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(TextBubbleHolder textBubbleHolder, int i) {
        if (textBubbleHolder.getItemViewType() != 1) {
            super.onBindViewHolder((TextBubbleAdapter) textBubbleHolder, i);
            textBubbleHolder.bind(this.mTextDataList.get(i));
            this.mDelegator.onBindViewHolder(textBubbleHolder, i);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        TextData textData = this.mTextDataList.get(i);
        return (!(textData instanceof TextConfig) || ((TextConfig) textData).getItemType() != 1) ? 2 : 1;
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<TextData> list = this.mTextDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void setSelection(int i) {
        this.mDelegator.setSelection(i);
    }

    @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public int getSelection() {
        return this.mDelegator.getSelection();
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mDelegator.onAttachedToRecyclerView(recyclerView);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mDelegator.onDetachedFromRecyclerView(recyclerView);
    }

    public TextData getItemData(int i) {
        List<TextData> list = this.mTextDataList;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mTextDataList.get(i);
    }
}
