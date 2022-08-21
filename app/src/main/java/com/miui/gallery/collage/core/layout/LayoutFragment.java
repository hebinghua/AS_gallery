package com.miui.gallery.collage.core.layout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.R;
import com.miui.gallery.collage.app.common.AbstractLayoutFragment;
import com.miui.gallery.collage.core.RenderData;
import com.miui.gallery.collage.render.CollageRender;
import com.miui.gallery.collage.widget.CollageImageView;
import com.miui.gallery.collage.widget.CollageLayout;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class LayoutFragment extends AbstractLayoutFragment {
    public CollageLayout mCollageLayout;
    public LayoutModel mLayoutModel;
    public boolean mModelReady = false;
    public boolean mViewReady = false;
    public boolean mInit = false;
    public Map<Bitmap, CollageImageView> mImageViewMap = new HashMap();
    public CollageLayout.BitmapExchangeListener mBitmapExchangeListener = new CollageLayout.BitmapExchangeListener() { // from class: com.miui.gallery.collage.core.layout.LayoutFragment.1
        @Override // com.miui.gallery.collage.widget.CollageLayout.BitmapExchangeListener
        public void onBitmapExchange(Bitmap bitmap, Bitmap bitmap2) {
            LayoutFragment.this.mImageViewMap.put(bitmap, (CollageImageView) LayoutFragment.this.mImageViewMap.get(bitmap2));
            LayoutFragment.this.mImageViewMap.put(bitmap2, (CollageImageView) LayoutFragment.this.mImageViewMap.get(bitmap));
        }
    };

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.collage_layout_render, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        this.mCollageLayout = (CollageLayout) view.findViewById(R.id.collage_layout);
        this.mViewReady = true;
        refreshLayout();
    }

    @Override // com.miui.gallery.collage.app.common.AbstractLayoutFragment
    public void onSelectModel(LayoutModel layoutModel) {
        this.mLayoutModel = layoutModel;
        this.mModelReady = true;
        refreshLayout();
    }

    @Override // com.miui.gallery.collage.app.common.AbstractLayoutFragment
    public void onSelectMargin(float f) {
        this.mCollageLayout.setCollageMargin(f, false);
    }

    @Override // com.miui.gallery.collage.app.common.AbstractLayoutFragment
    public void onSelectRatio(float f) {
        ((ConstraintLayout.LayoutParams) this.mCollageLayout.getLayoutParams()).dimensionRatio = String.valueOf(f);
        this.mCollageLayout.requestLayout();
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public void onBitmapsReceive() {
        refreshLayout();
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public void dismissControlWindow() {
        CollageLayout collageLayout = this.mCollageLayout;
        if (collageLayout != null) {
            collageLayout.dismissControlWindow();
        }
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public boolean isActivating() {
        CollageLayout collageLayout = this.mCollageLayout;
        return collageLayout != null && collageLayout.isActivating();
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public RenderData export() {
        this.mLayoutModel.ratio = Float.parseFloat(((ConstraintLayout.LayoutParams) this.mCollageLayout.getLayoutParams()).dimensionRatio);
        FragmentActivity activity = getActivity();
        LayoutModel layoutModel = this.mLayoutModel;
        CollageLayout collageLayout = this.mCollageLayout;
        return new LayoutRenderData(CollageRender.generateRenderData(activity, null, layoutModel, collageLayout, collageLayout.getWidth()));
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public HashMap<String, String> onSimple() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "Layout");
        Bitmap[] bitmapArr = this.mBitmaps;
        hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.valueOf(bitmapArr == null ? 0 : bitmapArr.length));
        LayoutModel layoutModel = this.mLayoutModel;
        hashMap.put("name", layoutModel == null ? null : layoutModel.name);
        return hashMap;
    }

    public final void refreshLayout() {
        if (!this.mInit) {
            if (!this.mModelReady || !this.mBitmapReady || !this.mViewReady) {
                return;
            }
            this.mCollageLayout.setBackgroundColor(-1);
            generateCollageLayout(this.mLayoutModel);
            this.mCollageLayout.setReplaceImageListener(this.mReplaceImageListener);
            this.mCollageLayout.setBitmapExchangeListener(this.mBitmapExchangeListener);
            this.mInit = true;
            return;
        }
        refreshCollageLayout(this.mLayoutModel);
    }

    public final void generateCollageLayout(LayoutModel layoutModel) {
        this.mCollageLayout.removeAllViews();
        LayoutItemModel[] layoutItemModelArr = layoutModel.items;
        for (int i = 0; i < layoutItemModelArr.length; i++) {
            Bitmap bitmap = this.mBitmaps[i];
            LayoutItemModel layoutItemModel = layoutItemModelArr[i];
            CollageImageView collageImageView = new CollageImageView(getActivity());
            if (i < this.mBitmaps.length) {
                collageImageView.setBitmap(bitmap);
                this.mImageViewMap.put(bitmap, collageImageView);
            }
            this.mCollageLayout.addView(collageImageView, new CollageLayout.LayoutParams(layoutItemModel.clipType, layoutItemModel.data));
        }
    }

    public final void refreshCollageLayout(LayoutModel layoutModel) {
        LayoutItemModel[] layoutItemModelArr = layoutModel.items;
        int childCount = this.mCollageLayout.getChildCount();
        for (int i = 0; i < Math.min(layoutItemModelArr.length, childCount); i++) {
            LayoutItemModel layoutItemModel = layoutItemModelArr[i];
            this.mCollageLayout.getChildAt(i).setLayoutParams(new CollageLayout.LayoutParams(layoutItemModel.clipType, layoutItemModel.data));
        }
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public void onBitmapReplace(Bitmap bitmap, Bitmap bitmap2) {
        CollageImageView collageImageView = this.mImageViewMap.get(bitmap);
        collageImageView.setBitmap(bitmap2);
        this.mImageViewMap.remove(bitmap);
        this.mImageViewMap.put(bitmap2, collageImageView);
    }
}
