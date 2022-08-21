package com.miui.gallery.collage.core.poster;

import android.content.Context;
import android.util.SparseArray;
import com.miui.gallery.R;
import com.miui.gallery.collage.BitmapManager;
import com.miui.gallery.collage.app.common.CollageMenuFragment;
import com.miui.gallery.collage.app.common.CollageRenderFragment;
import com.miui.gallery.collage.app.common.IDataLoader;
import com.miui.gallery.collage.app.poster.PosterMenuFragment;
import com.miui.gallery.collage.core.CollagePresenter;
import com.miui.gallery.collage.core.RenderEngine;
import com.miui.gallery.collage.core.layout.LayoutModel;
import com.miui.gallery.collage.core.poster.PosterDataLoader;
import java.util.List;

/* loaded from: classes.dex */
public class PosterPresenter extends CollagePresenter {
    public SparseArray<List<LayoutModel>> mPosterLayoutSparseArray = new SparseArray<>();
    public SparseArray<List<PosterModel>> mPosterSparseArray = new SparseArray<>();

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public String getMenuFragmentTag() {
        return "fragment_poster:menu";
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public String getRenderFragmentTag() {
        return "fragment_poster:render";
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public int getTitle() {
        return R.string.collage_title_poster;
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public void onDetach() {
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public CollageMenuFragment onCreateMenuFragment() {
        return new PosterMenuFragment();
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public CollageRenderFragment onCreateRenderFragment() {
        return new PosterFragment();
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public RenderEngine createEngine(Context context, BitmapManager bitmapManager) {
        return new PosterRenderEngine(context, bitmapManager);
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public boolean hasResourceData() {
        return (this.mPosterLayoutSparseArray.size() == 0 || this.mPosterSparseArray.size() == 0) ? false : true;
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public IDataLoader onCreateDataLoader(final CollagePresenter.DataLoadListener dataLoadListener) {
        return new PosterDataLoader(this.mViewInterface.getContext().getAssets(), new PosterDataLoader.DataLoadListener() { // from class: com.miui.gallery.collage.core.poster.PosterPresenter.1
            @Override // com.miui.gallery.collage.core.poster.PosterDataLoader.DataLoadListener
            public void onDataLoad(SparseArray<List<LayoutModel>> sparseArray, SparseArray<List<PosterModel>> sparseArray2) {
                PosterPresenter.this.mPosterLayoutSparseArray.clear();
                for (int i = 0; i < sparseArray.size(); i++) {
                    int keyAt = sparseArray.keyAt(i);
                    PosterPresenter.this.mPosterLayoutSparseArray.put(keyAt, sparseArray.get(keyAt));
                }
                PosterPresenter.this.mPosterSparseArray.clear();
                for (int i2 = 0; i2 < sparseArray2.size(); i2++) {
                    int keyAt2 = sparseArray2.keyAt(i2);
                    PosterPresenter.this.mPosterSparseArray.put(keyAt2, sparseArray2.get(keyAt2));
                }
                CollagePresenter.DataLoadListener dataLoadListener2 = dataLoadListener;
                if (dataLoadListener2 != null) {
                    dataLoadListener2.onDataLoad();
                }
            }
        });
    }

    public List<PosterModel> getPosters() {
        return this.mPosterSparseArray.get(this.mImageCount);
    }

    public LayoutModel getPosterCollageLayout(PosterModel posterModel) {
        int posterLayoutIndex = getPosterLayoutIndex(posterModel, this.mImageCount);
        if (posterLayoutIndex != -1) {
            return this.mPosterLayoutSparseArray.get(this.mImageCount).get(posterLayoutIndex);
        }
        return null;
    }

    public static int getPosterLayoutIndex(PosterModel posterModel, int i) {
        int[] iArr;
        for (int i2 : posterModel.collageModels) {
            if (i2 / 10 == i) {
                return i2 % 10;
            }
        }
        return -1;
    }
}
