package com.miui.gallery.adapter;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.widget.recyclerview.ProportionIntegerTagView;
import com.miui.gallery.widget.recyclerview.ProportionTagBaseAdapter;
import com.miui.gallery.widget.recyclerview.ProportionTagModel;
import com.miui.gallery.widget.recyclerview.ProportionTagView;

/* loaded from: classes.dex */
public class ProportionStringTagAdapter extends ProportionTagBaseAdapter<Integer> {
    public ProportionStringTagAdapter(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.widget.recyclerview.ProportionTagBaseAdapter
    public ProportionTagView<Integer> onCreatedView() {
        ProportionIntegerTagView proportionIntegerTagView = new ProportionIntegerTagView(this.mContext);
        proportionIntegerTagView.setStyle(R.style.TagProportion);
        return proportionIntegerTagView;
    }

    @Override // com.miui.gallery.widget.recyclerview.ProportionTagBaseAdapter
    public void onBindView(ProportionTagView<Integer> proportionTagView, int i) {
        proportionTagView.setContent((Integer) ((ProportionTagModel) this.mProportionTagModels.get(i)).mo532getTag());
    }
}
