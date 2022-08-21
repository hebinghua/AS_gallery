package com.miui.gallery.movie.ui.adapter;

import android.content.Context;
import android.view.View;
import com.miui.gallery.movie.R$layout;
import com.miui.gallery.movie.entity.TemplateResource;
import com.miui.gallery.movie.ui.adapter.BaseAdapter;

/* loaded from: classes2.dex */
public class TemplateAdapter extends BaseAdapter<TemplateResource> {
    public TemplateAdapter(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter
    public int getLayoutId(int i) {
        return R$layout.movie_layout_template_item;
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter
    public BaseAdapter.BaseHolder<TemplateResource> getHolder(View view) {
        return new BaseResourceHolder(view);
    }
}
