package com.miui.gallery.collage.core.stitching;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.R;
import com.miui.gallery.collage.app.common.AbstractStitchingFragment;
import com.miui.gallery.collage.core.RenderData;
import com.miui.gallery.collage.widget.CollageStitchingLayout;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.Arrays;
import java.util.HashMap;

/* loaded from: classes.dex */
public class StitchingFragment extends AbstractStitchingFragment {
    public CollageStitchingLayout mStitchingLayout;
    public StitchingModel mStitchingModel;
    public boolean mModelReady = false;
    public boolean mViewReady = false;
    public boolean mInit = false;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.collage_stitching_render, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        this.mStitchingLayout = (CollageStitchingLayout) view.findViewById(R.id.drag_layout);
        this.mViewReady = true;
        refreshLayout();
    }

    public final void refreshLayout() {
        if (!this.mInit) {
            if (!this.mModelReady || !this.mBitmapReady || !this.mViewReady) {
                return;
            }
            generateStitchingLayout(this.mStitchingModel);
            this.mStitchingLayout.setReplaceImageListener(this.mReplaceImageListener);
            this.mInit = true;
            return;
        }
        refreshStitchingLayout(this.mStitchingModel);
    }

    public final void generateStitchingLayout(StitchingModel stitchingModel) {
        CollageStitchingLayout collageStitchingLayout = this.mStitchingLayout;
        Bitmap[] bitmapArr = this.mBitmaps;
        collageStitchingLayout.setBitmaps((Bitmap[]) Arrays.copyOf(bitmapArr, bitmapArr.length));
        this.mStitchingLayout.setStitchingModel(stitchingModel);
    }

    public final void refreshStitchingLayout(StitchingModel stitchingModel) {
        this.mStitchingLayout.setStitchingModel(stitchingModel);
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public void onBitmapsReceive() {
        refreshLayout();
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public void dismissControlWindow() {
        CollageStitchingLayout collageStitchingLayout = this.mStitchingLayout;
        if (collageStitchingLayout != null) {
            collageStitchingLayout.dismissControlWindow();
        }
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public RenderData export() {
        return new StitchingRenderData(this.mStitchingLayout.generateRenderData());
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public HashMap<String, String> onSimple() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "Stitching");
        Bitmap[] bitmapArr = this.mBitmaps;
        hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.valueOf(bitmapArr == null ? 0 : bitmapArr.length));
        StitchingModel stitchingModel = this.mStitchingModel;
        hashMap.put("name", stitchingModel == null ? null : stitchingModel.name);
        return hashMap;
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public void onBitmapReplace(Bitmap bitmap, Bitmap bitmap2) {
        this.mStitchingLayout.notifyBitmapReplace(bitmap, bitmap2);
    }

    @Override // com.miui.gallery.collage.app.common.AbstractStitchingFragment
    public void onSelectModel(StitchingModel stitchingModel) {
        this.mStitchingModel = stitchingModel;
        this.mModelReady = true;
        refreshLayout();
    }
}
