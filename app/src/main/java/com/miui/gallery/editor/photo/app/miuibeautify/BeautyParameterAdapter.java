package com.miui.gallery.editor.photo.app.miuibeautify;

import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautifyData;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class BeautyParameterAdapter extends Adapter<ParameterViewHolder> {
    public int[] mIcons;
    public List<MiuiBeautifyData> mParameterData;

    public BeautyParameterAdapter(List<MiuiBeautifyData> list, int[] iArr) {
        this.mParameterData = list;
        this.mIcons = iArr;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ParameterViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ParameterViewHolder(getInflater().inflate(R.layout.miuibeauty_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ParameterViewHolder parameterViewHolder, int i) {
        super.onBindViewHolder((BeautyParameterAdapter) parameterViewHolder, i);
        parameterViewHolder.bind(this.mIcons[i], this.mParameterData.get(i));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mParameterData.size();
    }
}
