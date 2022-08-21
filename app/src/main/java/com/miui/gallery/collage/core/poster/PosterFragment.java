package com.miui.gallery.collage.core.poster;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.R;
import com.miui.gallery.collage.CollageUtils;
import com.miui.gallery.collage.app.common.AbstractPosterFragment;
import com.miui.gallery.collage.core.RenderData;
import com.miui.gallery.collage.core.layout.LayoutItemModel;
import com.miui.gallery.collage.core.layout.LayoutModel;
import com.miui.gallery.collage.render.CollageRender;
import com.miui.gallery.collage.render.PosterElementRender;
import com.miui.gallery.collage.widget.CollageImageView;
import com.miui.gallery.collage.widget.CollageLayout;
import com.miui.gallery.collage.widget.PosterLayout;
import com.miui.gallery.util.ScreenUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class PosterFragment extends AbstractPosterFragment {
    public CollageLayout mCollageLayout;
    public float mDefaultRatio;
    public LayoutModel mLayoutModel;
    public PosterLayout mPosterLayout;
    public int mPosterLayoutInitWidth;
    public PosterModel mPosterModel;
    public ViewGroup mRootView;
    public boolean mModelReady = false;
    public boolean mViewReady = false;
    public boolean mInit = false;
    public Map<Bitmap, CollageImageView> mImageViewMap = new HashMap();
    public CollageLayout.BitmapExchangeListener mBitmapExchangeListener = new CollageLayout.BitmapExchangeListener() { // from class: com.miui.gallery.collage.core.poster.PosterFragment.3
        @Override // com.miui.gallery.collage.widget.CollageLayout.BitmapExchangeListener
        public void onBitmapExchange(Bitmap bitmap, Bitmap bitmap2) {
            PosterFragment.this.mImageViewMap.put(bitmap, (CollageImageView) PosterFragment.this.mImageViewMap.get(bitmap2));
            PosterFragment.this.mImageViewMap.put(bitmap2, (CollageImageView) PosterFragment.this.mImageViewMap.get(bitmap));
        }
    };

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.collage_poster_render, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        this.mRootView = (ViewGroup) view;
        this.mPosterLayout = (PosterLayout) view.findViewById(R.id.poster_layout);
        this.mCollageLayout = (CollageLayout) view.findViewById(R.id.collage_layout);
        this.mViewReady = true;
        this.mDefaultRatio = getResourceFloat(getResources(), R.dimen.poster_image_ratio, 0.75f);
        this.mPosterLayoutInitWidth = Math.round(ScreenUtils.getScreenWidth() - (getResources().getDimension(R.dimen.collage_render_margin_left) * 2.0f));
        refreshLayout();
    }

    @Override // com.miui.gallery.collage.app.common.AbstractPosterFragment
    public void onSelectModel(PosterModel posterModel, LayoutModel layoutModel) {
        this.mPosterModel = posterModel;
        this.mLayoutModel = layoutModel;
        this.mModelReady = true;
        refreshLayout();
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        refreshLayout();
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
        return new PosterRenderData(CollageRender.generateRenderData(getActivity(), this.mPosterModel, this.mLayoutModel, this.mCollageLayout, this.mPosterLayoutInitWidth));
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public HashMap<String, String> onSimple() {
        HashMap<String, String> hashMap = new HashMap<>();
        PosterModel posterModel = this.mPosterModel;
        String str = posterModel == null ? null : posterModel.name;
        if (str != null && str.length() > 3) {
            str = str.substring(3);
        }
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "Poster");
        Bitmap[] bitmapArr = this.mBitmaps;
        hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.valueOf(bitmapArr == null ? 0 : bitmapArr.length));
        hashMap.put("name", str);
        return hashMap;
    }

    public final void refreshLayout() {
        if (!this.mInit) {
            if (!this.mModelReady || !this.mBitmapReady || !this.mViewReady) {
                return;
            }
            generateCollageLayout(this.mLayoutModel);
            refreshCollagePosition(this.mPosterModel);
            setPosterModelToPosterLayout(this.mPosterModel);
            this.mCollageLayout.setReplaceImageListener(this.mReplaceImageListener);
            this.mCollageLayout.setBitmapExchangeListener(this.mBitmapExchangeListener);
            this.mInit = true;
            return;
        }
        refreshCollageLayout(this.mLayoutModel);
        refreshCollagePosition(this.mPosterModel);
        setPosterModelToPosterLayout(this.mPosterModel);
    }

    public final void generateCollageLayout(LayoutModel layoutModel) {
        this.mCollageLayout.removeAllViews();
        LayoutItemModel[] layoutItemModelArr = layoutModel.items;
        for (int i = 0; i < layoutItemModelArr.length; i++) {
            Bitmap bitmap = this.mBitmaps[i];
            CollageImageView collageImageView = new CollageImageView(getActivity());
            if (i < this.mBitmaps.length) {
                collageImageView.setBitmap(bitmap);
                this.mImageViewMap.put(bitmap, collageImageView);
            }
            LayoutItemModel layoutItemModel = layoutItemModelArr[i];
            if (layoutItemModel != null) {
                this.mCollageLayout.addView(collageImageView, new CollageLayout.LayoutParams(layoutItemModel.clipType, layoutItemModel.data));
            }
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

    public final void refreshCollagePosition(PosterModel posterModel) {
        final Drawable[] drawableArr;
        final CollagePositionModel collagePositionModelByImageSize = CollagePositionModel.getCollagePositionModelByImageSize(posterModel.collagePositions, this.mBitmaps.length);
        this.mCollageLayout.setLayoutParams(new PosterLayout.LayoutParams(collagePositionModelByImageSize.position));
        this.mCollageLayout.setCollageMargin(collagePositionModelByImageSize.margin, collagePositionModelByImageSize.ignoreEdgeMargin);
        String[] strArr = collagePositionModelByImageSize.masks;
        if (strArr == null || strArr.length <= 0) {
            drawableArr = null;
        } else {
            int length = strArr.length;
            drawableArr = new Drawable[length];
            for (int i = 0; i < length; i++) {
                Resources resources = getResources();
                drawableArr[i] = CollageUtils.getDrawableByAssets(resources, collagePositionModelByImageSize.relativePath + File.separator + strArr[i]);
            }
        }
        this.mCollageLayout.post(new Runnable() { // from class: com.miui.gallery.collage.core.poster.PosterFragment.1
            @Override // java.lang.Runnable
            public void run() {
                PosterFragment.this.mCollageLayout.setMasks(drawableArr);
                PosterFragment.this.mCollageLayout.setRadius(collagePositionModelByImageSize.radius);
            }
        });
    }

    public final void setPosterModelToPosterLayout(final PosterModel posterModel) {
        float f = posterModel.ratio;
        if (f == 0.0f) {
            f = this.mDefaultRatio;
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mPosterLayout.getLayoutParams();
        if (Float.compare(f, Float.parseFloat(layoutParams.dimensionRatio)) != 0) {
            layoutParams.dimensionRatio = String.valueOf(f);
            this.mPosterLayout.requestLayout();
        }
        this.mPosterLayout.post(new Runnable() { // from class: com.miui.gallery.collage.core.poster.PosterFragment.2
            @Override // java.lang.Runnable
            public void run() {
                int width = PosterFragment.this.mPosterLayout.getWidth();
                int height = PosterFragment.this.mPosterLayout.getHeight();
                float f2 = width / PosterFragment.this.mPosterLayoutInitWidth;
                final PosterElementRender posterElementRender = new PosterElementRender();
                posterElementRender.initializeAsync(posterModel, width, height, f2, PosterFragment.this.getActivity(), new PosterElementRender.LoadDataListener() { // from class: com.miui.gallery.collage.core.poster.PosterFragment.2.1
                    @Override // com.miui.gallery.collage.render.PosterElementRender.LoadDataListener
                    public void onLoadFinish() {
                        PosterFragment.this.mPosterLayout.setRenderData(posterElementRender);
                    }
                });
            }
        });
    }

    @Override // com.miui.gallery.collage.app.common.CollageRenderFragment
    public void onBitmapReplace(Bitmap bitmap, Bitmap bitmap2) {
        CollageImageView collageImageView = this.mImageViewMap.get(bitmap);
        if (collageImageView == null) {
            return;
        }
        collageImageView.setBitmap(bitmap2);
        this.mImageViewMap.remove(bitmap);
        this.mImageViewMap.put(bitmap2, collageImageView);
    }

    public static float getResourceFloat(Resources resources, int i, float f) {
        TypedValue typedValue = new TypedValue();
        try {
            resources.getValue(i, typedValue, true);
            return typedValue.getFloat();
        } catch (Exception unused) {
            Log.e("PosterFragment", "Missing resource " + Integer.toHexString(i));
            return f;
        }
    }
}
