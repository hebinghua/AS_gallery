package com.miui.gallery.video.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.video.editor.TextStyle;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;
import java.util.List;

/* loaded from: classes2.dex */
public class WatermarkRecyclerViewAdapter extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter<WaterMarkHolder> {
    public LayoutInflater mLayoutInflater;
    public List<TextStyle> mTextStyles;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List list) {
        onBindViewHolder((WaterMarkHolder) viewHolder, i, (List<Object>) list);
    }

    public WatermarkRecyclerViewAdapter(Context context, List<TextStyle> list) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mTextStyles = list;
    }

    public TextStyle getTextStyle(int i) {
        if (BaseMiscUtil.isValid(this.mTextStyles) && i >= 0 && i < this.mTextStyles.size()) {
            return this.mTextStyles.get(i);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    /* renamed from: onCreateSingleChoiceViewHolder */
    public WaterMarkHolder mo1754onCreateSingleChoiceViewHolder(ViewGroup viewGroup, int i) {
        return new WaterMarkHolder(this.mLayoutInflater.inflate(R.layout.video_editor_text_download_item, viewGroup, false));
    }

    public void onBindViewHolder(WaterMarkHolder waterMarkHolder, int i, List<Object> list) {
        if (list.isEmpty()) {
            onBindViewHolder((WatermarkRecyclerViewAdapter) waterMarkHolder, i);
            return;
        }
        TextStyle textStyle = this.mTextStyles.get(i);
        boolean z = true;
        waterMarkHolder.updateTextEditable(textStyle.isLocal() && getSelectedItemPosition() == i);
        if (getSelectedItemPosition() != i) {
            z = false;
        }
        waterMarkHolder.updateSelected(z, textStyle.isDownloaded());
        waterMarkHolder.setStateImage(textStyle.getDownloadState());
        if (!textStyle.isDownloadSuccess()) {
            return;
        }
        textStyle.setDownloadState(17);
    }

    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    public void onBindView(WaterMarkHolder waterMarkHolder, int i) {
        TextStyle textStyle = this.mTextStyles.get(i);
        boolean z = true;
        waterMarkHolder.updateSelected(getSelectedItemPosition() == i, textStyle.isDownloaded());
        waterMarkHolder.setIcon(textStyle.getIconUrl(), textStyle.getIconResId(), textStyle.getBgColor());
        if (!textStyle.isLocal() || getSelectedItemPosition() != i) {
            z = false;
        }
        waterMarkHolder.updateTextEditable(z);
        waterMarkHolder.setStateImage(textStyle.getDownloadState());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<TextStyle> list = this.mTextStyles;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
