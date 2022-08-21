package com.miui.gallery.editor.photo.screen.text;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class ScreenTextBubbleAdapter extends Adapter<ScreenTextBubbleHolder> implements Selectable {
    public Context mContext;
    public Selectable.Delegator mDelegator;
    public List<TextData> mTextDataList;

    public ScreenTextBubbleAdapter(Context context, List<TextData> list, int i) {
        this.mTextDataList = list;
        this.mContext = context;
        this.mDelegator = new Selectable.Delegator(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ScreenTextBubbleHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ScreenTextBubbleHolder(LayoutInflater.from(this.mContext).inflate(R.layout.screen_text_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ScreenTextBubbleHolder screenTextBubbleHolder, int i) {
        super.onBindViewHolder((ScreenTextBubbleAdapter) screenTextBubbleHolder, i);
        screenTextBubbleHolder.bind(this.mTextDataList.get(i), i);
        this.mDelegator.onBindViewHolder(screenTextBubbleHolder, i);
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
