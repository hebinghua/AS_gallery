package com.miui.gallery.collage.core.stitching;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.collage.BitmapManager;
import com.miui.gallery.collage.app.common.CollageMenuFragment;
import com.miui.gallery.collage.app.common.CollageRenderFragment;
import com.miui.gallery.collage.app.common.IDataLoader;
import com.miui.gallery.collage.app.stitching.StitchingMenuFragment;
import com.miui.gallery.collage.core.CollagePresenter;
import com.miui.gallery.collage.core.RenderEngine;
import com.miui.gallery.collage.core.stitching.StitchingDataLoader;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class StitchingPresenter extends CollagePresenter {
    public List<StitchingModel> mStitchingModelList = new ArrayList();

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public String getMenuFragmentTag() {
        return "fragment_stitching:menu";
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public String getRenderFragmentTag() {
        return "fragment_stitching:render";
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public int getTitle() {
        return R.string.collage_title_stitching;
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
        return new StitchingMenuFragment();
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public CollageRenderFragment onCreateRenderFragment() {
        return new StitchingFragment();
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public RenderEngine createEngine(Context context, BitmapManager bitmapManager) {
        return new StitchingEngine(context, bitmapManager);
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public boolean hasResourceData() {
        return !this.mStitchingModelList.isEmpty();
    }

    @Override // com.miui.gallery.collage.core.CollagePresenter
    public IDataLoader onCreateDataLoader(final CollagePresenter.DataLoadListener dataLoadListener) {
        return new StitchingDataLoader(this.mViewInterface.getContext().getAssets(), new StitchingDataLoader.DataLoadListener() { // from class: com.miui.gallery.collage.core.stitching.StitchingPresenter.1
            @Override // com.miui.gallery.collage.core.stitching.StitchingDataLoader.DataLoadListener
            public void onDataLoad(List<StitchingModel> list) {
                StitchingPresenter.this.mStitchingModelList.clear();
                StitchingPresenter.this.mStitchingModelList.addAll(list);
                CollagePresenter.DataLoadListener dataLoadListener2 = dataLoadListener;
                if (dataLoadListener2 != null) {
                    dataLoadListener2.onDataLoad();
                }
            }
        });
    }

    public List<StitchingModel> getStitching() {
        return this.mStitchingModelList;
    }
}
