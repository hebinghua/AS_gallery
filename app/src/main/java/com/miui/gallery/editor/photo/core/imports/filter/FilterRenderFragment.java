package com.miui.gallery.editor.photo.core.imports.filter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.arcsoftbeauty.ArcsoftBeautyJni;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorCheckHelper;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.RenderMetaData;
import com.miui.gallery.editor.photo.core.imports.filter.FilterEditorView;
import com.miui.gallery.editor.photo.core.imports.filter.render.EmptyGPUImageFilter;
import com.miui.gallery.editor.photo.core.imports.filter.render.GPUImage;
import com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.bokeh.MiPortraitSegmenter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterRenderFragment extends AbstractEffectFragment implements SurfaceHolder.Callback {
    public Bitmap mBeautyBitmap;
    public BeautyTask mBeautyTask;
    public Bitmap mCurrentBitmap;
    public FilterEditorView mGLSurfaceView;
    public GPUImage mGPUImage;
    public boolean mIsRenderOrigin;
    public boolean mNeedDoRender;
    public ImageView mPlaceholderView;
    public LottieAnimationView mProgressBar;
    public List<RenderMetaData> mEffects = new ArrayList();
    public boolean mCompareEnable = false;
    public ProgressRunnable mProgressRunnable = new ProgressRunnable();
    public Handler mHandler = new Handler();
    public FilterEditorView.OnLongTouchCallback mOnLongTouchCallback = new FilterEditorView.OnLongTouchCallback() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterRenderFragment.1
        {
            FilterRenderFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.core.imports.filter.FilterEditorView.OnLongTouchCallback
        public void onLongTouchDown() {
            if (!FilterRenderFragment.this.mCompareEnable) {
                return;
            }
            FilterRenderFragment.this.mIsRenderOrigin = true;
            FilterRenderFragment.this.renderCompareOrigin();
        }

        @Override // com.miui.gallery.editor.photo.core.imports.filter.FilterEditorView.OnLongTouchCallback
        public void onLongTouchUp() {
            if (!FilterRenderFragment.this.mCompareEnable) {
                return;
            }
            FilterRenderFragment.this.mIsRenderOrigin = false;
            FilterRenderFragment.this.renderCompareCurrent();
        }
    };
    public BitmapGestureGLView.OnMatrixChangeListener mOnMatrixChangeListener = new BitmapGestureGLView.OnMatrixChangeListener() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterRenderFragment.2
        {
            FilterRenderFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView.OnMatrixChangeListener
        public void onBitmapMatrixChange() {
            updateGLPosition();
        }

        @Override // com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView.OnMatrixChangeListener
        public void onCanvasMatrixChange() {
            updateGLPosition();
        }

        public final void updateGLPosition() {
            if (FilterRenderFragment.this.mGPUImage != null) {
                FilterRenderFragment.this.mGPUImage.updateGLPosition(FilterRenderFragment.this.mGLSurfaceView.getGLPosition());
                FilterRenderFragment.this.mGPUImage.requestRender();
            }
        }
    };
    public MiPortraitSegmenter.IPortraitEnhanceListener mIPortraitEnhanceListener = new MiPortraitSegmenter.IPortraitEnhanceListener() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterRenderFragment.4
        {
            FilterRenderFragment.this = this;
        }

        @Override // com.xiaomi.bokeh.MiPortraitSegmenter.IPortraitEnhanceListener
        public void processResult(Bitmap bitmap) {
            if (bitmap == null) {
                return;
            }
            FilterRenderFragment.this.mCurrentBitmap = bitmap;
            FilterRenderFragment.this.enableComparison(true);
            if (FilterRenderFragment.this.mIsRenderOrigin) {
                return;
            }
            FilterRenderFragment.this.renderBitmap(bitmap);
        }
    };

    public static /* synthetic */ void $r8$lambda$T6Z0YMlapkvX_77xhZiDdAKPNzU(FilterRenderFragment filterRenderFragment) {
        filterRenderFragment.lambda$surfaceCreated$0();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isSupportAnimation() {
        return false;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void showCompareButton() {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new FilterRenderView(layoutInflater.getContext());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new FilterRenderView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        FilterEditorView filterEditorView = (FilterEditorView) view.findViewById(R.id.gl_surface_view);
        this.mGLSurfaceView = filterEditorView;
        filterEditorView.setZOrderOnTop(false);
        float parseFloat = Float.parseFloat(getString(R.string.filter_bg_color_component));
        GPUImage gPUImage = new GPUImage(getActivity());
        this.mGPUImage = gPUImage;
        gPUImage.setBackgroundColor(parseFloat, parseFloat, parseFloat);
        this.mGPUImage.setImage(getBitmap());
        this.mGPUImage.setGLSurfaceView(this.mGLSurfaceView);
        this.mGLSurfaceView.setZOrderOnTop(false);
        this.mGLSurfaceView.setBitmap(getBitmap());
        this.mGLSurfaceView.setOnMatrixChangeListener(this.mOnMatrixChangeListener);
        this.mGLSurfaceView.setEnabled(false);
        this.mGLSurfaceView.getHolder().addCallback(this);
        this.mProgressBar = (LottieAnimationView) view.findViewById(R.id.progress);
        this.mGLSurfaceView.setOnLongTouchCallback(this.mOnLongTouchCallback);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_placeholder);
        this.mPlaceholderView = imageView;
        imageView.setImageBitmap(getBitmap());
        if (this.mNeedDoRender) {
            doRender();
        }
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void enterImmersive() {
        super.enterImmersive();
        EditorMiscHelper.enterImmersive(getTitleView());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void exitImmersive() {
        super.exitImmersive();
        EditorMiscHelper.exitImmersive(getTitleView());
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        FilterEditorView filterEditorView = this.mGLSurfaceView;
        if (filterEditorView != null) {
            filterEditorView.setVisibility(8);
        }
        hideCompareButton();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        BeautyTask beautyTask = this.mBeautyTask;
        if (beautyTask != null) {
            beautyTask.cancel(true);
        }
        this.mGPUImage.deleteImage();
        this.mCurrentBitmap = null;
        if (PortraitColorCheckHelper.isPortraitEnable()) {
            MiPortraitSegmenter.getInstance().destroy();
        }
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onMultiWindowModeChanged(boolean z) {
        super.onMultiWindowModeChanged(z);
        if (z || !this.mCompareEnable) {
            return;
        }
        showCompareButton();
    }

    public void enableComparison(boolean z) {
        this.mCompareEnable = z;
        if (getActivity() != null && getActivity().isInMultiWindowMode()) {
            hideCompareButton();
        } else if (this.mCompareEnable) {
            if (this.mGLSurfaceView.getWidth() == 0) {
                this.mGLSurfaceView.post(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterRenderFragment.3
                    {
                        FilterRenderFragment.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        FilterRenderFragment.this.showCompareButton();
                    }
                });
            } else {
                showCompareButton();
            }
        } else {
            hideCompareButton();
        }
    }

    public final void renderCompareOrigin() {
        renderBitmap(getBitmap());
        HashMap hashMap = new HashMap();
        hashMap.put("page", this.mEffect.name());
        SamplingStatHelper.recordCountEvent("photo_editor", "compare_button_touch", hashMap);
    }

    public final void renderCompareCurrent() {
        Bitmap bitmap;
        if (new FilterRenderData(this.mEffects).isPortraitColor() && (bitmap = this.mCurrentBitmap) != null) {
            renderBitmap(bitmap);
        } else {
            render();
        }
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);
        GPUImage gPUImage = this.mGPUImage;
        if (gPUImage != null) {
            gPUImage.setImage(bitmap);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void add(Metadata metadata, Object obj) {
        if (metadata instanceof RenderMetaData) {
            this.mEffects.add((RenderMetaData) metadata);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void remove(Metadata metadata) {
        int indexOf;
        if (!(metadata instanceof RenderMetaData) || (indexOf = this.mEffects.indexOf(metadata)) < 0) {
            return;
        }
        this.mEffects.remove(indexOf);
    }

    public final void renderBitmap(Bitmap bitmap) {
        renderBitmap(bitmap, new EmptyGPUImageFilter());
    }

    public final void renderBitmap(Bitmap bitmap, BaseOriginalFilter baseOriginalFilter) {
        this.mGPUImage.setImage(bitmap);
        this.mGPUImage.setFilter(baseOriginalFilter);
        this.mGPUImage.requestRender();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void render() {
        if (this.mGPUImage == null) {
            this.mNeedDoRender = true;
        } else {
            doRender();
        }
    }

    public final void doRender() {
        FilterRenderData filterRenderData = new FilterRenderData(this.mEffects);
        boolean z = true;
        if (filterRenderData.isPortraitBeauty() && ArcsoftBeautyJni.idBeautyAvailable()) {
            if (this.mBeautyBitmap == null) {
                BeautyTask beautyTask = new BeautyTask();
                this.mBeautyTask = beautyTask;
                beautyTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getBitmap());
                return;
            }
            doRenderAfterBeauty();
        } else if (filterRenderData.isPortraitColor()) {
            FilterItem portraitColorFilterItem = filterRenderData.getPortraitColorFilterItem();
            if (portraitColorFilterItem == null) {
                return;
            }
            Bitmap cloneBitmap = BitmapUtils.cloneBitmap(getBitmap());
            if (cloneBitmap == getBitmap()) {
                DefaultLogger.d("FilterRenderFragment", "waitSegment: createBitmap return same object as src");
            } else {
                MiPortraitSegmenter.getInstance().updateEnhanceBitmap(cloneBitmap, (portraitColorFilterItem.progress * 1.0f) / 10.0f, this.mIPortraitEnhanceListener);
            }
        } else {
            BaseOriginalFilter instantiate = filterRenderData.instantiate();
            renderBitmap(getBitmap(), instantiate);
            if (instantiate instanceof IFilterEmptyValidate) {
                z = true ^ ((IFilterEmptyValidate) instantiate).isEmpty();
            }
            enableComparison(z);
        }
    }

    public final void doRenderAfterBeauty() {
        FilterRenderData filterRenderData = new FilterRenderData(this.mEffects);
        if (!isAdded() || !filterRenderData.isPortraitBeauty()) {
            return;
        }
        BaseOriginalFilter instantiate = filterRenderData.instantiate();
        renderBitmap(this.mBeautyBitmap, instantiate);
        enableComparison(!(instantiate instanceof EmptyGPUImageFilter));
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        return new FilterRenderData(this.mEffects).instantiate() instanceof EmptyGPUImageFilter;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        ArrayList arrayList = new ArrayList();
        List<RenderMetaData> list = this.mEffects;
        if (list != null && !list.isEmpty()) {
            Iterator<RenderMetaData> it = this.mEffects.iterator();
            while (it.hasNext()) {
                arrayList.add("V9-" + it.next().name);
            }
        }
        return arrayList;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        return new FilterRenderData(this.mEffects);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
        this.mEffects.clear();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        DefaultLogger.d("FilterRenderFragment", "surfaceCreated");
        this.mGLSurfaceView.postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterRenderFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FilterRenderFragment.$r8$lambda$T6Z0YMlapkvX_77xhZiDdAKPNzU(FilterRenderFragment.this);
            }
        }, 200L);
    }

    public /* synthetic */ void lambda$surfaceCreated$0() {
        ImageView imageView = this.mPlaceholderView;
        if (imageView != null) {
            imageView.setVisibility(8);
            this.mGLSurfaceView.setEnabled(true);
        }
    }

    /* loaded from: classes2.dex */
    public class BeautyTask extends AsyncTask<Bitmap, Void, Bitmap> {
        public BeautyTask() {
            FilterRenderFragment.this = r1;
        }

        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Bitmap... bitmapArr) {
            Bitmap copy = bitmapArr[0].copy(Bitmap.Config.ARGB_8888, true);
            ArcsoftBeautyJni.smartBeauty(copy);
            return copy;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            FilterRenderFragment.this.mBeautyBitmap = bitmap;
            FilterRenderFragment.this.doRenderAfterBeauty();
        }
    }

    /* loaded from: classes2.dex */
    public class ProgressRunnable implements Runnable {
        public ProgressRunnable() {
            FilterRenderFragment.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (FilterRenderFragment.this.getActivity() == null || !FilterRenderFragment.this.getActivity().isInMultiWindowMode()) {
                FilterRenderFragment.this.mProgressBar.setVisibility(0);
            }
        }
    }

    public void showProgressView(long j) {
        this.mHandler.postDelayed(this.mProgressRunnable, j);
    }

    public void hideProgressView() {
        this.mHandler.removeCallbacks(this.mProgressRunnable);
        this.mProgressBar.setVisibility(4);
    }
}
