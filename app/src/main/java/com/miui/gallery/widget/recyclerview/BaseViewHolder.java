package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.BindAwareViewHolder;
import com.miui.gallery.ui.album.main.utils.factory.GalleryViewCreator;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes3.dex */
public class BaseViewHolder extends BindAwareViewHolder {
    public BaseViewHolder(View view) {
        super(view);
    }

    public static View getView(ViewGroup viewGroup, int i) {
        return getInflater(viewGroup.getContext()).inflate(i, viewGroup, false);
    }

    public static BaseViewHolder create(ViewGroup viewGroup, int i) {
        return create(viewGroup, i, false);
    }

    public static BaseViewHolder create(ViewGroup viewGroup, int i, boolean z) {
        return new BaseViewHolder(getInflater(viewGroup.getContext()).inflate(i, viewGroup, z));
    }

    public static LayoutInflater getInflater(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        try {
            if (from.getFactory2() != null) {
                from = from.cloneInContext(context);
            }
            from.setFactory2(GalleryViewCreator.getViewFactory());
        } catch (Exception e) {
            DefaultLogger.e("BaseViewHolder", e);
        }
        return from;
    }
}
