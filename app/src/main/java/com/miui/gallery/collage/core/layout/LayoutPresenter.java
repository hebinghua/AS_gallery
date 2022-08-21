package com.miui.gallery.collage.core.layout;

import android.content.Context;
import android.util.SparseArray;
import com.miui.gallery.R;
import com.miui.gallery.collage.BitmapManager;
import com.miui.gallery.collage.app.common.CollageMenuFragment;
import com.miui.gallery.collage.app.common.CollageRenderFragment;
import com.miui.gallery.collage.app.common.IDataLoader;
import com.miui.gallery.collage.app.layout.LayoutMenuFragment;
import com.miui.gallery.collage.core.CollagePresenter;
import com.miui.gallery.collage.core.RenderEngine;
import com.miui.gallery.collage.core.layout.LayoutDataLoader;
import java.util.List;

/* loaded from: classes.dex */
public class LayoutPresenter extends CollagePresenter {
    public SparseArray<List<LayoutModel>> mLayoutSparseArray = new SparseArray<>();

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public String getMenuFragmentTag() {
        return "fragment_layout:menu";
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public String getRenderFragmentTag() {
        return "fragment_layout:render";
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public int getTitle() {
        return R.string.collage_title_layout;
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public void onDetach() {
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public boolean supportImageSize(int i) {
        return i > 1;
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public CollageMenuFragment onCreateMenuFragment() {
        return new LayoutMenuFragment();
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public CollageRenderFragment onCreateRenderFragment() {
        return new LayoutFragment();
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public RenderEngine createEngine(Context context, BitmapManager bitmapManager) {
        return new LayoutRenderEngine(context, bitmapManager);
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public boolean hasResourceData() {
        return this.mLayoutSparseArray.size() != 0;
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public IDataLoader onCreateDataLoader(final CollagePresenter.DataLoadListener dataLoadListener) {
        return new LayoutDataLoader(this.mViewInterface.getContext().getAssets(), new LayoutDataLoader.DataLoadListener() { // from class: com.miui.gallery.collage.core.layout.LayoutPresenter.1
            @Override // com.miui.gallery.collage.core.layout.LayoutDataLoader.DataLoadListener
            public void onDataLoad(SparseArray<List<LayoutModel>> sparseArray) {
                LayoutPresenter.this.mLayoutSparseArray.clear();
                for (int i = 0; i < sparseArray.size(); i++) {
                    int keyAt = sparseArray.keyAt(i);
                    LayoutPresenter.this.mLayoutSparseArray.put(keyAt, sparseArray.get(keyAt));
                }
                CollagePresenter.DataLoadListener dataLoadListener2 = dataLoadListener;
                if (dataLoadListener2 != null) {
                    dataLoadListener2.onDataLoad();
                }
            }
        });
    }

    public List<LayoutModel> getLayouts(int i) {
        return this.mLayoutSparseArray.get(i);
    }
}
