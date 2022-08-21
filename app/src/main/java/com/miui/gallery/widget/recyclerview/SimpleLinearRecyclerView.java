package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.miui.gallery.baseui.R$styleable;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.widget.OrientationProvider;

/* loaded from: classes3.dex */
public class SimpleLinearRecyclerView extends SimpleRecyclerView {
    public OrientationProvider mOrientationProvider;

    public SimpleLinearRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SimpleLinearRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOrientationProvider = OrientationProvider.PORTRAIT;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.OrientationProvider);
        String string = obtainStyledAttributes.getString(R$styleable.OrientationProvider_orientation_provider);
        obtainStyledAttributes.recycle();
        OrientationProvider createOrientationProvider = OrientationProvider.createOrientationProvider(string);
        if (createOrientationProvider != null) {
            this.mOrientationProvider = createOrientationProvider;
        }
        configLayoutManager();
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        if (linearLayoutManager == null) {
            return;
        }
        int orientation = linearLayoutManager.getOrientation();
        int i = !isPortrait();
        if (orientation == i) {
            return;
        }
        linearLayoutManager.setOrientation(i);
        invalidateItemDecorations();
    }

    public final boolean isPortrait() {
        return this.mOrientationProvider.isPortrait(getContext());
    }

    public void configLayoutManager() {
        int i = !isPortrait() ? 1 : 0;
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getContext());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getContext()));
        customScrollerLinearLayoutManager.setOrientation(i);
        setLayoutManager(customScrollerLinearLayoutManager);
    }
}
