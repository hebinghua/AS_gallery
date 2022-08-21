package com.miui.gallery.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Trace;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.asynclayoutinflater.view.GalleryAsyncLayoutInflater;
import androidx.asynclayoutinflater.view.OnInflateFinishedListener;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.BaseConfig$ScreenConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.model.MediaItem;
import com.miui.gallery.provider.ProcessingMedia;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.IdleUITaskHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.widget.PagerAdapter;
import com.miui.gallery.widget.ViewPager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes.dex */
public class PhotoPageAdapter extends PagerAdapter implements PhotoPageItem.PhotoTransitionListener, PhotoPageItem.OnImageLoadFinishListener {
    public boolean isFirstLoad;
    public boolean isPreviewing;
    public CheckState.Source mCheckSource;
    public ChoiceMode mChoiceMode;
    public FragmentActivity mContext;
    public IDataProvider mDataProvider;
    public BaseDataSet mDataSet;
    public Point mDisplaySize;
    public int mInitCount;
    public PhotoPageInteractionListener mPhotoPageInteractionListener;
    public PhotoPageItem.PhotoTransitionListener mPhotoTransitionListener;
    public ImageLoadParams mPreviewCache;
    public OnPreviewedListener mPreviewedListener;
    public WeakReference<PhotoPageItem> mPreviewingItem;
    public Map<String, ProcessingMedia> mProcessingMediaMap;
    public float mSlipProgress;
    public int mSlippedRectBottom;
    public int mSlippedRectTop;
    public ItemViewInfo mTransitInfo;
    public boolean mUseSlipModeV2;
    public ViewProvider mViewProvider;

    /* loaded from: classes.dex */
    public interface ChoiceModeInterface {
        boolean isChecked(long j);

        void onItemCheckedChanged(int i, long j, boolean z);

        void setChecked(int i, long j, boolean z);
    }

    /* loaded from: classes.dex */
    public interface MultiChoiceModeListener {
        void onAllItemsCheckedStateChanged(boolean z);

        void onItemCheckedStateChanged(int i, long j, boolean z);
    }

    /* loaded from: classes.dex */
    public interface OnPreviewedListener {
        void onPreviewed();
    }

    /* loaded from: classes.dex */
    public interface PhotoPageInteractionListener {
        int getActionBarHeight();

        int getMenuBarHeight();

        void notifyDataChanged();

        void playVideo(BaseDataItem baseDataItem, String str);
    }

    /* loaded from: classes.dex */
    public static class PlaceHolderItem {
    }

    /* loaded from: classes.dex */
    public interface ProgressHandlerCallBack {
        void onProgressCancel();
    }

    public static /* synthetic */ void $r8$lambda$T8oElUdVsOA9U_w_6gRuKWnxIzM(PhotoPageAdapter photoPageAdapter, Object obj, ViewGroup viewGroup) {
        photoPageAdapter.lambda$destroyItem$0(obj, viewGroup);
    }

    public int getLayoutId(int i) {
        if (i != 0) {
            if (i == 1) {
                return R.layout.photo_page_gif_item;
            }
            if (i == 2) {
                return R.layout.photo_page_video_item;
            }
            if (i == 3) {
                return R.layout.photo_page_face_item;
            }
            return 0;
        }
        return R.layout.photo_page_image_item;
    }

    public int getViewTypeCount() {
        return 4;
    }

    public boolean isNeedPostInstantiateItemTask() {
        return true;
    }

    public final boolean isProcessingStatusChanged(ProcessingMedia processingMedia, ProcessingMedia processingMedia2) {
        return (processingMedia != null && processingMedia2 == null) || (processingMedia2 != null && processingMedia == null);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public PhotoPageAdapter(FragmentActivity fragmentActivity, int i, ImageLoadParams imageLoadParams, ItemViewInfo itemViewInfo, OnPreviewedListener onPreviewedListener, PhotoPageInteractionListener photoPageInteractionListener) {
        this(fragmentActivity, i, imageLoadParams, null, itemViewInfo, onPreviewedListener, photoPageInteractionListener);
    }

    public PhotoPageAdapter(FragmentActivity fragmentActivity, int i, ImageLoadParams imageLoadParams, ViewProvider viewProvider, ItemViewInfo itemViewInfo, OnPreviewedListener onPreviewedListener, PhotoPageInteractionListener photoPageInteractionListener) {
        this.isFirstLoad = true;
        this.mCheckSource = new CheckState.Source() { // from class: com.miui.gallery.adapter.PhotoPageAdapter.1
            public BaseDataSet mBaseDataSet;

            {
                PhotoPageAdapter.this = this;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.CheckState.Source
            public void setBaseDataSet(BaseDataSet baseDataSet) {
                this.mBaseDataSet = baseDataSet;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.CheckState.Source
            public int getItemPos(long j, int i2) {
                BaseDataSet baseDataSet = this.mBaseDataSet;
                if (baseDataSet != null) {
                    return baseDataSet.getIndexOfItem(new BaseDataItem().setKey(j), i2);
                }
                return -1;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.CheckState.Source
            public long getItemKey(int i2) {
                BaseDataSet baseDataSet = this.mBaseDataSet;
                if (baseDataSet != null) {
                    return baseDataSet.getItemKey(i2);
                }
                return -1L;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.CheckState.Source
            public int getCount() {
                BaseDataSet baseDataSet;
                if (PhotoPageAdapter.this.isPreviewing || (baseDataSet = this.mBaseDataSet) == null) {
                    return PhotoPageAdapter.this.mInitCount;
                }
                return baseDataSet.getCount();
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.CheckState.Source
            public void notifyCheckState() {
                PhotoPageAdapter.this.notifyDataSetChanged();
            }
        };
        this.mContext = fragmentActivity;
        this.mInitCount = i;
        this.mPreviewCache = imageLoadParams;
        this.mTransitInfo = itemViewInfo;
        if (viewProvider == null) {
            ViewProvider generateDefaultViewProvider = generateDefaultViewProvider();
            this.mViewProvider = generateDefaultViewProvider;
            generateDefaultViewProvider.init(fragmentActivity);
        } else {
            this.mViewProvider = viewProvider;
        }
        this.mPreviewedListener = onPreviewedListener;
        this.mPhotoPageInteractionListener = photoPageInteractionListener;
        if (this.mPreviewCache != null) {
            this.isPreviewing = true;
        }
        if (this.mDisplaySize == null) {
            this.mDisplaySize = new Point(BaseConfig$ScreenConfig.getScreenWidth(), BaseConfig$ScreenConfig.getScreenHeight());
        }
    }

    public PhotoPageAdapter() {
        this.isFirstLoad = true;
        this.mCheckSource = new CheckState.Source() { // from class: com.miui.gallery.adapter.PhotoPageAdapter.1
            public BaseDataSet mBaseDataSet;

            {
                PhotoPageAdapter.this = this;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.CheckState.Source
            public void setBaseDataSet(BaseDataSet baseDataSet) {
                this.mBaseDataSet = baseDataSet;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.CheckState.Source
            public int getItemPos(long j, int i2) {
                BaseDataSet baseDataSet = this.mBaseDataSet;
                if (baseDataSet != null) {
                    return baseDataSet.getIndexOfItem(new BaseDataItem().setKey(j), i2);
                }
                return -1;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.CheckState.Source
            public long getItemKey(int i2) {
                BaseDataSet baseDataSet = this.mBaseDataSet;
                if (baseDataSet != null) {
                    return baseDataSet.getItemKey(i2);
                }
                return -1L;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.CheckState.Source
            public int getCount() {
                BaseDataSet baseDataSet;
                if (PhotoPageAdapter.this.isPreviewing || (baseDataSet = this.mBaseDataSet) == null) {
                    return PhotoPageAdapter.this.mInitCount;
                }
                return baseDataSet.getCount();
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.CheckState.Source
            public void notifyCheckState() {
                PhotoPageAdapter.this.notifyDataSetChanged();
            }
        };
    }

    public void changeDataSet(BaseDataSet baseDataSet) {
        changeDataSet(baseDataSet, false);
    }

    public void changeDataSet(BaseDataSet baseDataSet, boolean z) {
        DefaultLogger.d("PhotoPageAdapter", "change dataset [%s] to [%s], ignoreDelayNotify %s", this.mDataSet, baseDataSet, Boolean.valueOf(z));
        if (this.isPreviewing && z) {
            clearPreviewParams();
            notifyPreviewedCallback();
        }
        this.mDataSet = baseDataSet;
        boolean z2 = true;
        if (this.isPreviewing) {
            WeakReference<PhotoPageItem> weakReference = this.mPreviewingItem;
            PhotoPageItem photoPageItem = weakReference != null ? weakReference.get() : null;
            if (photoPageItem != null) {
                refreshItem(photoPageItem, this.mPreviewCache.getPos());
                z2 = false;
            }
        }
        if (z2) {
            notifyDataSetChanged();
        }
        CheckState.Source source = this.mCheckSource;
        if (source != null) {
            source.setBaseDataSet(baseDataSet);
        }
        ChoiceMode choiceMode = this.mChoiceMode;
        if (choiceMode != null) {
            choiceMode.notifyDataChanged(this.mContext, baseDataSet);
        }
    }

    public void release() {
        BaseDataSet baseDataSet = this.mDataSet;
        if (baseDataSet != null) {
            baseDataSet.release();
            this.mDataSet = null;
        }
        ChoiceMode choiceMode = this.mChoiceMode;
        if (choiceMode != null) {
            choiceMode.release();
            this.mChoiceMode = null;
        }
        this.mPreviewedListener = null;
        this.mPhotoTransitionListener = null;
        this.mPhotoPageInteractionListener = null;
    }

    public void setProcessingMedias(Map<String, ProcessingMedia> map) {
        this.mProcessingMediaMap = map;
    }

    public final void clearPreviewParams() {
        this.isPreviewing = false;
        WeakReference<PhotoPageItem> weakReference = this.mPreviewingItem;
        if (weakReference != null) {
            weakReference.clear();
            this.mPreviewingItem = null;
        }
    }

    public BaseDataSet getDataSet() {
        return this.mDataSet;
    }

    public List<Integer> getViewTypePerCountForLowRam() {
        ArrayList arrayList = new ArrayList(getViewTypeCount());
        for (int i = 0; i < getViewTypeCount(); i++) {
            if (i != 0) {
                arrayList.add(i, 1);
            } else {
                arrayList.add(i, 6);
            }
        }
        return arrayList;
    }

    public int getViewType(int i) {
        Trace.beginSection("getViewType");
        try {
            Trace.beginSection("getDataItem");
            BaseDataItem dataItem = getDataItem(i);
            Trace.endSection();
            if (this.isPreviewing) {
                if (this.mPreviewCache.match(dataItem, i)) {
                    if (this.mPreviewCache.isGif()) {
                        return 1;
                    }
                    if (this.mPreviewCache.isVideo()) {
                        return 2;
                    }
                    return this.mPreviewCache.isFromFace() ? 3 : 0;
                }
            } else if (dataItem != null) {
                if (dataItem.isGif()) {
                    return 1;
                }
                if (dataItem.isVideo()) {
                    return 2;
                }
                return dataItem.hasFace() ? 3 : 0;
            }
            return -1;
        } finally {
            Trace.endSection();
        }
    }

    public static ViewProvider generateDefaultPhotoPageViewProvider() {
        return new PhotoPageAdapter().generateDefaultViewProvider();
    }

    public final ViewProvider generateDefaultViewProvider() {
        if (BaseBuildUtil.isLowRamDevice()) {
            return new ViewProvider(getViewTypeCount(), getViewTypePerCountForLowRam(), getLayoutArray());
        }
        return new ViewProvider(getViewTypeCount(), ViewPager.DEFAULT_MORE_OFFSCREEN_PAGES + 2 + 1, getLayoutArray());
    }

    public SparseIntArray getLayoutArray() {
        SparseIntArray sparseIntArray = new SparseIntArray(4);
        sparseIntArray.put(0, getLayoutId(0));
        sparseIntArray.put(1, getLayoutId(1));
        sparseIntArray.put(2, getLayoutId(2));
        sparseIntArray.put(3, getLayoutId(3));
        return sparseIntArray;
    }

    public BaseDataItem getDataItem(int i) {
        BaseDataSet baseDataSet = this.mDataSet;
        if (baseDataSet == null || i < 0 || i >= baseDataSet.getCount()) {
            return null;
        }
        return this.mDataSet.getItem(null, i);
    }

    public boolean isPreviewing() {
        return this.isPreviewing;
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public int getCount() {
        BaseDataSet baseDataSet;
        if (this.isPreviewing || (baseDataSet = this.mDataSet) == null) {
            return this.mInitCount;
        }
        return baseDataSet.getCount();
    }

    public final void startOrFinishItemActionMode(PhotoPageItem photoPageItem) {
        ChoiceMode choiceMode = this.mChoiceMode;
        if (choiceMode != null && choiceMode.isInAction()) {
            photoPageItem.startActionMode(this.mChoiceMode.getOriginIInstance(), this.mChoiceMode.getSelectIInstance(), this.mChoiceMode.getRenderIInstance());
        } else {
            photoPageItem.finishActionMode();
        }
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        IDataProvider iDataProvider;
        Trace.beginSection("instantiateItem");
        int viewType = getViewType(i);
        if (viewType == -1) {
            Trace.endSection();
            return new PlaceHolderItem();
        }
        Trace.beginSection("acquireView_addView");
        PhotoPageItem acquire = this.mViewProvider.acquire(viewType, viewGroup);
        Trace.beginSection("addView");
        viewGroup.addView(acquire, -1, -1);
        Trace.endSection();
        Trace.endSection();
        Trace.beginSection("instantiateItemData");
        if (!isNeedPostInstantiateItemTask() || (iDataProvider = this.mDataProvider) == null || iDataProvider.getFieldData().mCurrent.slipState == 0) {
            instantiateItemData(acquire, i);
        }
        Trace.endSection();
        return acquire;
    }

    public final void instantiateItemData(PhotoPageItem photoPageItem, int i) {
        if (this.isFirstLoad && this.isPreviewing && this.mPreviewCache.match(null, i)) {
            try {
                Trace.beginSection("addView");
                Trace.beginSection("bindData");
                photoPageItem.setUseSlipModeV2(this.mUseSlipModeV2);
                photoPageItem.setSlipProgress(this.mSlipProgress);
                photoPageItem.setSlippedRect(this.mSlippedRectTop, this.mSlippedRectBottom);
                DefaultLogger.d("photoPageStartup", "start bind image");
                bindData(photoPageItem, i);
                photoPageItem.setPhotoPageCallback(this.mPhotoPageInteractionListener);
                photoPageItem.addOnMatrixChangeListener(photoPageItem);
                startOrFinishItemActionMode(photoPageItem);
                Trace.endSection();
                return;
            } finally {
                this.isFirstLoad = false;
                Trace.endSection();
            }
        }
        Trace.beginSection("bindData");
        photoPageItem.setUseSlipModeV2(this.mUseSlipModeV2);
        photoPageItem.setSlipProgress(this.mSlipProgress);
        photoPageItem.setSlippedRect(this.mSlippedRectTop, this.mSlippedRectBottom);
        bindData(photoPageItem, i);
        photoPageItem.setPhotoPageCallback(this.mPhotoPageInteractionListener);
        photoPageItem.addOnMatrixChangeListener(photoPageItem);
        startOrFinishItemActionMode(photoPageItem);
        Trace.endSection();
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void destroyItem(final ViewGroup viewGroup, int i, final Object obj) {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.adapter.PhotoPageAdapter$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PhotoPageAdapter.$r8$lambda$T8oElUdVsOA9U_w_6gRuKWnxIzM(PhotoPageAdapter.this, obj, viewGroup);
            }
        });
    }

    public /* synthetic */ void lambda$destroyItem$0(Object obj, ViewGroup viewGroup) {
        if (!isItemValidate(obj)) {
            return;
        }
        Trace.beginSection("destroyItem");
        this.mViewProvider.restore(viewGroup, (PhotoPageItem) obj);
        Trace.endSection();
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void refreshItem(Object obj, int i) {
        if (isItemValidate(obj)) {
            DefaultLogger.d("PhotoPageAdapter", "refreshItem %d", Integer.valueOf(i));
            PhotoPageItem photoPageItem = (PhotoPageItem) obj;
            photoPageItem.setUseSlipModeV2(this.mUseSlipModeV2);
            photoPageItem.setSlipProgress(this.mSlipProgress);
            photoPageItem.setSlippedRect(this.mSlippedRectTop, this.mSlippedRectBottom);
            bindData(photoPageItem, i);
            photoPageItem.addOnMatrixChangeListener(photoPageItem);
            startOrFinishItemActionMode(photoPageItem);
        }
    }

    @Override // com.github.chrisbanes.photoview.TransitionListener
    public void onTransitEnd() {
        DefaultLogger.d("PhotoPageAdapter", "onTransitEnd");
        onPreviewedEnd();
        PhotoPageItem.PhotoTransitionListener photoTransitionListener = this.mPhotoTransitionListener;
        if (photoTransitionListener != null) {
            photoTransitionListener.onTransitEnd();
        }
    }

    @Override // com.github.chrisbanes.photoview.TransitionListener
    public void onTransitUpdate(float f) {
        PhotoPageItem.PhotoTransitionListener photoTransitionListener = this.mPhotoTransitionListener;
        if (photoTransitionListener != null) {
            photoTransitionListener.onTransitUpdate(f);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem.OnImageLoadFinishListener
    public void onImageLoadFinish(String str, boolean z, Bitmap bitmap) {
        if (!needTransit()) {
            onPreviewedEnd();
        }
    }

    public final void onPreviewedEnd() {
        if (this.isPreviewing) {
            notifyPreviewedCallback();
            clearPreviewParams();
            if (this.mDataSet == null) {
                return;
            }
            IdleUITaskHelper.getInstance().addTask(new Runnable() { // from class: com.miui.gallery.adapter.PhotoPageAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    public final void notifyPreviewedCallback() {
        if (this.mPreviewedListener != null) {
            DefaultLogger.d("PhotoPageAdapter", "notifyPreviewedCallback");
            this.mPreviewedListener.onPreviewed();
            this.mPreviewedListener = null;
        }
    }

    public final boolean needTransit() {
        ItemViewInfo itemViewInfo = this.mTransitInfo;
        return itemViewInfo != null && itemViewInfo.isLocationValid();
    }

    public final void bindData(PhotoPageItem photoPageItem, int i) {
        photoPageItem.setTag(R.id.tag_item_position, Integer.valueOf(i));
        ImageLoadParams imageLoadParams = this.mPreviewCache;
        photoPageItem.setIsFromCamera(imageLoadParams == null ? false : imageLoadParams.isFromCamera());
        DefaultLogger.d("PhotoPageAdapter", "isPreviewing:" + this.isPreviewing);
        ProcessingMedia processingMedia = null;
        if (this.isPreviewing) {
            if (!this.mPreviewCache.match(null, i)) {
                return;
            }
            if (this.mPreviewingItem == null) {
                this.mPreviewingItem = new WeakReference<>(photoPageItem);
            }
            if (photoPageItem.getCacheItem() == null) {
                photoPageItem.setCacheItem(this.mPreviewCache, this);
                if (needTransit()) {
                    photoPageItem.animEnter(this.mTransitInfo, this);
                }
            }
            if (this.mDataSet == null || photoPageItem.getDataItem() != null) {
                return;
            }
            BaseDataItem dataItem = getDataItem(this.mDataSet.getIndexOfItem(new BaseDataItem().setKey(this.mPreviewCache.getKey()), this.mPreviewCache.getPos()));
            if (dataItem != null) {
                Map<String, ProcessingMedia> map = this.mProcessingMediaMap;
                if (map != null) {
                    processingMedia = map.get(dataItem.getOriginalPath());
                }
                photoPageItem.setProcessingMedia(processingMedia);
                photoPageItem.swapItem(dataItem);
            }
            if (this.mDataProvider == null || dataItem == null) {
                return;
            }
            DefaultLogger.d("PhotoPageAdapter", "PrepareMenuData" + dataItem.getKey() + ",callPrepareMenuData =>");
            this.mDataProvider.getViewModelData().setPrepareData(dataItem, i);
        } else if (this.mDataSet == null) {
        } else {
            Trace.beginSection("getDataItem");
            BaseDataItem dataItem2 = getDataItem(i);
            Trace.endSection();
            Trace.beginSection("setProcessingMedia");
            ProcessingMedia processingMedia2 = photoPageItem.getProcessingMedia();
            Map<String, ProcessingMedia> map2 = this.mProcessingMediaMap;
            ProcessingMedia processingMedia3 = (map2 == null || dataItem2 == null) ? null : map2.get(dataItem2.getOriginalPath());
            photoPageItem.setProcessingMedia(processingMedia3);
            Trace.endSection();
            BaseDataItem dataItem3 = photoPageItem.getDataItem();
            if (dataItem3 == null || dataItem3.isModified(dataItem2) || isProcessingStatusChanged(processingMedia2, processingMedia3)) {
                Trace.beginSection("swapItem");
                ImageLoadParams imageLoadParams2 = this.mPreviewCache;
                if (imageLoadParams2 != null && imageLoadParams2.match(dataItem2, i)) {
                    photoPageItem.swapItem(dataItem2, this.mPreviewCache);
                    this.mPreviewCache = null;
                } else {
                    photoPageItem.swapItem(dataItem2, null);
                }
                Trace.endSection();
                IDataProvider iDataProvider = this.mDataProvider;
                if (iDataProvider != null && dataItem2 != null) {
                    iDataProvider.getViewModelData().setPrepareData(dataItem2, i);
                }
            }
            Trace.beginSection("notifyPreviewedCallback");
            if (this.mPreviewCache == null) {
                notifyPreviewedCallback();
            }
            Trace.endSection();
        }
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public int getSlipWidth(int i, int i2) {
        int displayHeight;
        int displayWidth;
        BaseDataItem dataItem = getDataItem(i2);
        if (dataItem == null) {
            return super.getSlipWidth(i, i2);
        }
        if (dataItem.getWidth() <= 0 || dataItem.getHeight() <= 0) {
            return super.getSlipWidth(i, i2);
        }
        int orientation = dataItem.getOrientation();
        if (((dataItem instanceof MediaItem) && (orientation == 90 || orientation == 270)) || ExifUtil.isWidthHeightRotated(orientation)) {
            displayHeight = dataItem.getDisplayWidth();
            displayWidth = dataItem.getDisplayHeight();
        } else {
            displayHeight = dataItem.getDisplayHeight();
            displayWidth = dataItem.getDisplayWidth();
        }
        if (!this.mUseSlipModeV2 && BaseFileMimeUtil.isVideoFromMimeType(dataItem.getMimeType())) {
            displayHeight = dataItem.getDisplayHeight();
            displayWidth = dataItem.getDisplayWidth();
        }
        if (!this.mUseSlipModeV2) {
            float f = ((i * 1.0f) * displayWidth) / displayHeight;
            return f > getMinSlideWidth() ? (int) f : super.getSlipWidth(i, i2);
        }
        Point point = this.mDisplaySize;
        int i3 = point.y;
        int i4 = point.x;
        double d = 1.0d;
        double d2 = displayHeight > i3 ? (i3 * 1.0d) / displayHeight : 1.0d;
        double d3 = displayWidth * d2;
        int i5 = (int) d3;
        double d4 = i5 > i4 ? (i4 * 1.0d) / i5 : 1.0d;
        double d5 = ((int) (d3 * d4)) * ((i * 1.0d) / ((int) ((displayHeight * d2) * d4)));
        int i6 = (int) d5;
        if (i6 < 450) {
            d = 450.0d / i6;
        }
        int i7 = (int) (d5 * d);
        if (i7 <= 990) {
            return i7;
        }
        return 990;
    }

    public final float getMinSlideWidth() {
        return this.mDisplaySize.x * 0.5f;
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public int getItemPosition(Object obj, int i) {
        int i2 = -1;
        if (this.isPreviewing) {
            return isItemValidate(obj) ? -3 : -1;
        }
        BaseDataSet baseDataSet = this.mDataSet;
        if (baseDataSet != null) {
            i2 = -2;
            if (i >= 0 && i < baseDataSet.getCount() && isTypeMatch(obj, i)) {
                return -3;
            }
        }
        return i2;
    }

    public boolean isTypeMatch(Object obj, int i) {
        if (isItemValidate(obj)) {
            Integer num = (Integer) ((PhotoPageItem) obj).getTag(R.id.tag_view_type);
            BaseDataItem dataItem = getDataItem(i);
            if (dataItem == null) {
                return false;
            }
            return (dataItem.isVideo() && num.intValue() == 2) || (dataItem.isGif() && num.intValue() == 1) || ((dataItem.hasFace() && num.intValue() == 3) || (!dataItem.isVideo() && !dataItem.isGif() && !dataItem.hasFace() && num.intValue() == 0));
        }
        return false;
    }

    public static boolean isItemValidate(Object obj) {
        return obj instanceof PhotoPageItem;
    }

    public void setDataProvider(IDataProvider iDataProvider) {
        this.mDataProvider = iDataProvider;
    }

    public ChoiceMode startActionMode(MultiChoiceModeListener multiChoiceModeListener) {
        if (this.mChoiceMode == null) {
            this.mChoiceMode = new ChoiceMode(this.mCheckSource, multiChoiceModeListener);
        }
        this.mChoiceMode.startAction();
        return this.mChoiceMode;
    }

    public void setUseSlipModeV2(boolean z) {
        this.mUseSlipModeV2 = z;
    }

    public void setSlipProgress(float f) {
        if (this.mUseSlipModeV2) {
            this.mSlipProgress = f;
        }
    }

    public void setSlippedRect(int i, int i2) {
        this.mSlippedRectTop = i;
        this.mSlippedRectBottom = i2;
    }

    public void updateDisplaySize(Point point) {
        this.mDisplaySize = point;
        DefaultLogger.d("PhotoPageAdapter", "updateDisplaySize: %dx%d", Integer.valueOf(point.x), Integer.valueOf(point.y));
    }

    /* loaded from: classes.dex */
    public static class CheckState {
        public Map<Integer, Long> mSelectedIds;
        public Source mSource;
        public final Object mSync = new Object();
        public volatile boolean inIniting = false;

        /* loaded from: classes.dex */
        public interface Source {
            int getCount();

            long getItemKey(int i);

            int getItemPos(long j, int i);

            void notifyCheckState();

            void setBaseDataSet(BaseDataSet baseDataSet);
        }

        public static /* synthetic */ void $r8$lambda$IkwJ19e7Z9Vu8zwTzz_0LkFwkCY(List list, Integer num, Long l) {
            list.add(num);
        }

        public static /* synthetic */ void $r8$lambda$hDtJFljuXMX7BodaNCFYNhd9eDs(List list, Integer num, Long l) {
            list.add(l);
        }

        public CheckState(Source source) {
            this.mSource = source;
            this.mSelectedIds = new HashMap(source.getCount());
        }

        public void setChecked(int i, long j, boolean z) {
            if (this.inIniting) {
                return;
            }
            synchronized (this.mSync) {
                if (z) {
                    this.mSelectedIds.put(Integer.valueOf(i), Long.valueOf(j));
                } else {
                    this.mSelectedIds.remove(Integer.valueOf(i));
                }
            }
        }

        public long getCheckedIdByPosition(int i) {
            if (this.inIniting) {
                return -1L;
            }
            synchronized (this.mSync) {
                if (!isCheckedPos(i)) {
                    return -1L;
                }
                return this.mSelectedIds.get(Integer.valueOf(i)).longValue();
            }
        }

        public void appendCheck(int i, long j) {
            if (this.inIniting) {
                return;
            }
            synchronized (this.mSync) {
                this.mSelectedIds.put(Integer.valueOf(i), Long.valueOf(j));
            }
        }

        public boolean isCheckedPos(int i) {
            boolean containsKey;
            if (this.inIniting) {
                return false;
            }
            synchronized (this.mSync) {
                containsKey = this.mSelectedIds.containsKey(Integer.valueOf(i));
            }
            return containsKey;
        }

        public boolean isCheckedId(long j) {
            boolean containsValue;
            if (this.inIniting) {
                return false;
            }
            synchronized (this.mSync) {
                containsValue = this.mSelectedIds.containsValue(Long.valueOf(j));
            }
            return containsValue;
        }

        public void cleanAll() {
            if (this.inIniting) {
                return;
            }
            synchronized (this.mSync) {
                this.mSelectedIds.clear();
            }
        }

        public final Map<Integer, Long> findSelectedIds(Map<Integer, Long> map, BaseDataSet baseDataSet, Supplier<Boolean> supplier) {
            synchronized (this.mSync) {
                this.inIniting = true;
                int size = map.size();
                this.mSource.setBaseDataSet(baseDataSet);
                if (size <= 0) {
                    this.inIniting = false;
                    return map;
                }
                HashMap hashMap = new HashMap(size);
                for (Map.Entry<Integer, Long> entry : map.entrySet()) {
                    long longValue = entry.getValue().longValue();
                    int itemPos = this.mSource.getItemPos(longValue, entry.getKey().intValue());
                    if (itemPos > -1) {
                        hashMap.put(Integer.valueOf(itemPos), Long.valueOf(longValue));
                    }
                    if (supplier != null && supplier.get().booleanValue()) {
                        DefaultLogger.d("PhotoPageAdapter", "findSelectedIds stopped halfway!");
                        this.inIniting = false;
                        return hashMap;
                    }
                }
                this.inIniting = false;
                return hashMap;
            }
        }

        public final void setSelectedIds(Map<Integer, Long> map) {
            if (this.inIniting) {
                return;
            }
            synchronized (this.mSync) {
                this.mSelectedIds = map;
            }
        }

        public Map<Integer, Long> getSelectedMap() {
            Map<Integer, Long> map;
            if (this.inIniting) {
                return Collections.emptyMap();
            }
            synchronized (this.mSync) {
                map = this.mSelectedIds;
            }
            return map;
        }

        public List<Integer> getSelectedPositions() {
            final ArrayList arrayList;
            if (this.inIniting) {
                return Collections.emptyList();
            }
            synchronized (this.mSync) {
                arrayList = new ArrayList(this.mSelectedIds.size());
                this.mSelectedIds.forEach(new BiConsumer() { // from class: com.miui.gallery.adapter.PhotoPageAdapter$CheckState$$ExternalSyntheticLambda0
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        PhotoPageAdapter.CheckState.$r8$lambda$IkwJ19e7Z9Vu8zwTzz_0LkFwkCY(arrayList, (Integer) obj, (Long) obj2);
                    }
                });
            }
            return arrayList;
        }

        public List<Long> getSelectedIds() {
            final ArrayList arrayList;
            if (this.inIniting) {
                return Collections.emptyList();
            }
            synchronized (this.mSync) {
                arrayList = new ArrayList(this.mSelectedIds.size());
                this.mSelectedIds.forEach(new BiConsumer() { // from class: com.miui.gallery.adapter.PhotoPageAdapter$CheckState$$ExternalSyntheticLambda1
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        PhotoPageAdapter.CheckState.$r8$lambda$hDtJFljuXMX7BodaNCFYNhd9eDs(arrayList, (Integer) obj, (Long) obj2);
                    }
                });
            }
            return arrayList;
        }

        public int getSelectedCount() {
            int size;
            if (this.inIniting) {
                return 0;
            }
            synchronized (this.mSync) {
                size = this.mSelectedIds.size();
            }
            return size;
        }
    }

    /* loaded from: classes.dex */
    public static class ChoiceMode implements ProgressHandlerCallBack {
        public MultiChoiceModeListener mChoiceModeListener;
        public boolean mInAction;
        public CheckState mOriginCheck;
        public ChoiceModeInterface mOriginInterface;
        public ProgressHandler mProgressHandler;
        public ChoiceModeInterface mRenderInterface;
        public CheckState mRenderState;
        public CheckState mSelectCheck;
        public ChoiceModeInterface mSelectInterface;
        public CheckState.Source mSource;
        public String mType = "";
        public AsyncTask<CheckState, Void, Void> mUpdateSelectIdsTask;

        /* loaded from: classes.dex */
        public static class UpdateSelectIdsTask extends AsyncTask<CheckState, Void, Void> {
            public boolean isCancelled;
            public final BaseDataSet mBaseDataSet;
            public final ProgressHandler mProgressHandler;

            public static /* synthetic */ Boolean $r8$lambda$r18kYnOo_n8_n6OyGpwymFrE4YA(UpdateSelectIdsTask updateSelectIdsTask) {
                return updateSelectIdsTask.lambda$doInBackground$0();
            }

            public UpdateSelectIdsTask(BaseDataSet baseDataSet, ProgressHandler progressHandler) {
                this.mBaseDataSet = baseDataSet;
                this.mProgressHandler = progressHandler;
            }

            @Override // android.os.AsyncTask
            public void onPreExecute() {
                if (this.isCancelled) {
                    return;
                }
                this.mProgressHandler.show();
            }

            @Override // android.os.AsyncTask
            public Void doInBackground(CheckState... checkStateArr) {
                try {
                    TimingTracing.beginTracing("PhotoPageAdapter", "notifyDataChanged_async");
                    if (this.isCancelled) {
                        return null;
                    }
                    for (CheckState checkState : checkStateArr) {
                        if (checkState != null) {
                            checkState.setSelectedIds(checkState.findSelectedIds(checkState.getSelectedMap(), this.mBaseDataSet, new Supplier() { // from class: com.miui.gallery.adapter.PhotoPageAdapter$ChoiceMode$UpdateSelectIdsTask$$ExternalSyntheticLambda0
                                @Override // java.util.function.Supplier
                                public final Object get() {
                                    return PhotoPageAdapter.ChoiceMode.UpdateSelectIdsTask.$r8$lambda$r18kYnOo_n8_n6OyGpwymFrE4YA(PhotoPageAdapter.ChoiceMode.UpdateSelectIdsTask.this);
                                }
                            }));
                        }
                    }
                    return null;
                } finally {
                    TimingTracing.stopTracing(null);
                }
            }

            public /* synthetic */ Boolean lambda$doInBackground$0() {
                return Boolean.valueOf(this.isCancelled);
            }

            @Override // android.os.AsyncTask
            public void onCancelled() {
                DefaultLogger.d("PhotoPageAdapter", "Update Select Ids Task be canceled！");
                this.isCancelled = true;
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(Void r1) {
                ProgressHandler progressHandler = this.mProgressHandler;
                if (progressHandler == null || !progressHandler.isShowing()) {
                    return;
                }
                this.mProgressHandler.hide();
            }
        }

        /* loaded from: classes.dex */
        public static class ProgressHandler {
            public boolean isShowing;
            public final ProgressHandlerCallBack mCallback;
            public final FragmentActivity mContext;
            public final ProgressDialog mProgressDialog;
            public final Runnable mHideRunnable = new Runnable() { // from class: com.miui.gallery.adapter.PhotoPageAdapter.ChoiceMode.ProgressHandler.1
                {
                    ProgressHandler.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (ProgressHandler.this.mProgressDialog == null || !ProgressHandler.this.mProgressDialog.isShowing() || ProgressHandler.this.mContext == null || ProgressHandler.this.mContext.isFinishing() || ProgressHandler.this.mContext.isDestroyed()) {
                        return;
                    }
                    ProgressHandler.this.mProgressDialog.dismiss();
                }
            };
            public final Runnable mShowRunnable = new Runnable() { // from class: com.miui.gallery.adapter.PhotoPageAdapter.ChoiceMode.ProgressHandler.2
                {
                    ProgressHandler.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (ProgressHandler.this.mProgressDialog == null || ProgressHandler.this.mProgressDialog.isShowing() || ProgressHandler.this.mContext == null || ProgressHandler.this.mContext.isFinishing() || ProgressHandler.this.mContext.isDestroyed()) {
                        return;
                    }
                    ProgressHandler.this.mProgressDialog.show();
                }
            };
            public final Handler mHandler = new Handler();

            /* renamed from: $r8$lambda$aiB-H_THbLUFLy8xi_COOHezDbY */
            public static /* synthetic */ void m514$r8$lambda$aiBH_THbLUFLy8xi_COOHezDbY(ProgressHandler progressHandler, DialogInterface dialogInterface) {
                progressHandler.lambda$new$0(dialogInterface);
            }

            public ProgressHandler(ProgressHandlerCallBack progressHandlerCallBack, FragmentActivity fragmentActivity) {
                this.mCallback = progressHandlerCallBack;
                this.mContext = fragmentActivity;
                ProgressDialog progressDialog = new ProgressDialog(fragmentActivity);
                this.mProgressDialog = progressDialog;
                progressDialog.setMessage(fragmentActivity.getString(R.string.face_albumset_loading_data));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.adapter.PhotoPageAdapter$ChoiceMode$ProgressHandler$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnCancelListener
                    public final void onCancel(DialogInterface dialogInterface) {
                        PhotoPageAdapter.ChoiceMode.ProgressHandler.m514$r8$lambda$aiBH_THbLUFLy8xi_COOHezDbY(PhotoPageAdapter.ChoiceMode.ProgressHandler.this, dialogInterface);
                    }
                });
            }

            public /* synthetic */ void lambda$new$0(DialogInterface dialogInterface) {
                this.mCallback.onProgressCancel();
            }

            public void show() {
                this.mHandler.removeCallbacks(this.mHideRunnable);
                this.mHandler.post(this.mShowRunnable);
                this.isShowing = true;
            }

            public void hide() {
                this.mHandler.removeCallbacks(this.mShowRunnable);
                this.mHandler.post(this.mHideRunnable);
                this.isShowing = false;
            }

            public void release() {
                Handler handler = this.mHandler;
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
                ProgressDialog progressDialog = this.mProgressDialog;
                if (progressDialog == null || !progressDialog.isShowing()) {
                    return;
                }
                this.mProgressDialog.dismissWithoutAnimation();
            }

            public boolean isShowing() {
                return this.isShowing;
            }
        }

        public ChoiceMode(CheckState.Source source, MultiChoiceModeListener multiChoiceModeListener) {
            this.mInAction = false;
            this.mOriginCheck = new CheckState(source);
            this.mSelectCheck = new CheckState(source);
            this.mRenderState = new CheckState(source);
            this.mSource = source;
            this.mChoiceModeListener = multiChoiceModeListener;
            this.mInAction = true;
        }

        public void notifyDataChanged(FragmentActivity fragmentActivity, BaseDataSet baseDataSet) {
            try {
                if (this.mOriginCheck.getSelectedCount() + this.mSelectCheck.getSelectedCount() + this.mRenderState.getSelectedCount() <= 0) {
                    return;
                }
                int count = baseDataSet.getCount();
                if (count > 10000 && fragmentActivity != null) {
                    DefaultLogger.w("PhotoPageAdapter", "notifyDataChanged count[%s] over limit, async find selected ids", Integer.valueOf(count));
                    cancelUpdateTask();
                    if (this.mProgressHandler == null) {
                        this.mProgressHandler = new ProgressHandler(this, fragmentActivity);
                    }
                    this.mUpdateSelectIdsTask = new UpdateSelectIdsTask(baseDataSet, this.mProgressHandler).executeOnExecutor(com.miui.gallery.util.thread.ThreadManager.getExecutor(79), this.mOriginCheck, this.mSelectCheck, this.mRenderState);
                    return;
                }
                TimingTracing.beginTracing("PhotoPageAdapter", "notifyDataChanged_sync");
                CheckState checkState = this.mOriginCheck;
                checkState.setSelectedIds(checkState.findSelectedIds(checkState.getSelectedMap(), baseDataSet, null));
                CheckState checkState2 = this.mSelectCheck;
                checkState2.setSelectedIds(checkState2.findSelectedIds(checkState2.getSelectedMap(), baseDataSet, null));
                CheckState checkState3 = this.mRenderState;
                checkState3.setSelectedIds(checkState3.findSelectedIds(checkState3.getSelectedMap(), baseDataSet, null));
            } finally {
                TimingTracing.stopTracing(null);
            }
        }

        public final void cancelUpdateTask() {
            AsyncTask<CheckState, Void, Void> asyncTask = this.mUpdateSelectIdsTask;
            if (asyncTask == null || asyncTask.getStatus() != AsyncTask.Status.RUNNING) {
                return;
            }
            DefaultLogger.d("PhotoPageAdapter", "cancel Update Task");
            this.mUpdateSelectIdsTask.cancel(true);
            this.mUpdateSelectIdsTask = null;
        }

        @Override // com.miui.gallery.adapter.PhotoPageAdapter.ProgressHandlerCallBack
        public void onProgressCancel() {
            cancelUpdateTask();
        }

        public void release() {
            cancelUpdateTask();
            ProgressHandler progressHandler = this.mProgressHandler;
            if (progressHandler != null) {
                progressHandler.release();
                this.mProgressHandler = null;
            }
        }

        public ChoiceModeInterface getOriginIInstance() {
            if (this.mOriginInterface == null) {
                this.mOriginInterface = new ChoiceModeInterface() { // from class: com.miui.gallery.adapter.PhotoPageAdapter.ChoiceMode.1
                    {
                        ChoiceMode.this = this;
                    }

                    @Override // com.miui.gallery.adapter.PhotoPageAdapter.ChoiceModeInterface
                    public void onItemCheckedChanged(int i, long j, boolean z) {
                        if (z) {
                            ChoiceMode.this.mSelectCheck.setChecked(i, j, z);
                        }
                        int count = ChoiceMode.this.mSource.getCount();
                        for (int i2 = 0; i2 < count; i2++) {
                            ChoiceMode.this.mOriginCheck.setChecked(i2, ChoiceMode.this.mSource.getItemKey(i2), z);
                        }
                        ChoiceMode.this.mSource.notifyCheckState();
                        if (ChoiceMode.this.mChoiceModeListener != null) {
                            ChoiceMode.this.mChoiceModeListener.onItemCheckedStateChanged(i, j, z);
                        }
                    }

                    @Override // com.miui.gallery.adapter.PhotoPageAdapter.ChoiceModeInterface
                    public void setChecked(int i, long j, boolean z) {
                        ChoiceMode.this.mOriginCheck.setChecked(i, j, z);
                    }

                    @Override // com.miui.gallery.adapter.PhotoPageAdapter.ChoiceModeInterface
                    public boolean isChecked(long j) {
                        return ChoiceMode.this.mOriginCheck.isCheckedId(j);
                    }
                };
            }
            return this.mOriginInterface;
        }

        public ChoiceModeInterface getSelectIInstance() {
            if (this.mSelectInterface == null) {
                this.mSelectInterface = new ChoiceModeInterface() { // from class: com.miui.gallery.adapter.PhotoPageAdapter.ChoiceMode.2
                    {
                        ChoiceMode.this = this;
                    }

                    @Override // com.miui.gallery.adapter.PhotoPageAdapter.ChoiceModeInterface
                    public void onItemCheckedChanged(int i, long j, boolean z) {
                        ChoiceMode.this.mSelectCheck.setChecked(i, j, z);
                        if (ChoiceMode.this.mChoiceModeListener != null) {
                            ChoiceMode.this.mChoiceModeListener.onItemCheckedStateChanged(i, j, z);
                        }
                    }

                    @Override // com.miui.gallery.adapter.PhotoPageAdapter.ChoiceModeInterface
                    public void setChecked(int i, long j, boolean z) {
                        ChoiceMode.this.mSelectCheck.setChecked(i, j, z);
                    }

                    @Override // com.miui.gallery.adapter.PhotoPageAdapter.ChoiceModeInterface
                    public boolean isChecked(long j) {
                        return ChoiceMode.this.mSelectCheck.isCheckedId(j);
                    }
                };
            }
            return this.mSelectInterface;
        }

        public ChoiceModeInterface getRenderIInstance() {
            if (this.mRenderInterface == null) {
                this.mRenderInterface = new ChoiceModeInterface() { // from class: com.miui.gallery.adapter.PhotoPageAdapter.ChoiceMode.3
                    {
                        ChoiceMode.this = this;
                    }

                    @Override // com.miui.gallery.adapter.PhotoPageAdapter.ChoiceModeInterface
                    public void onItemCheckedChanged(int i, long j, boolean z) {
                        ChoiceMode.this.mRenderState.setChecked(i, j, z);
                    }

                    @Override // com.miui.gallery.adapter.PhotoPageAdapter.ChoiceModeInterface
                    public void setChecked(int i, long j, boolean z) {
                        ChoiceMode.this.mRenderState.setChecked(i, j, z);
                    }

                    @Override // com.miui.gallery.adapter.PhotoPageAdapter.ChoiceModeInterface
                    public boolean isChecked(long j) {
                        return ChoiceMode.this.mRenderState.isCheckedId(j);
                    }
                };
            }
            return this.mRenderInterface;
        }

        public List<Integer> getOriginItems() {
            return this.mOriginCheck.getSelectedPositions();
        }

        public List<Long> getOriginIds() {
            return this.mOriginCheck.getSelectedIds();
        }

        public List<Integer> getSelectItems() {
            return this.mSelectCheck.getSelectedPositions();
        }

        public List<Long> getSelectIds() {
            return this.mSelectCheck.getSelectedIds();
        }

        public List<Integer> getRenderItems() {
            return this.mRenderState.getSelectedPositions();
        }

        public List<Long> getRenderIds() {
            return this.mRenderState.getSelectedIds();
        }

        public boolean isInAction() {
            return this.mInAction;
        }

        public void startAction() {
            this.mInAction = true;
            this.mSource.notifyCheckState();
        }

        public void chooseAll() {
            refreshAllState(true);
            MultiChoiceModeListener multiChoiceModeListener = this.mChoiceModeListener;
            if (multiChoiceModeListener != null) {
                multiChoiceModeListener.onAllItemsCheckedStateChanged(true);
            }
        }

        public void unChooseAll() {
            refreshAllState(false);
            MultiChoiceModeListener multiChoiceModeListener = this.mChoiceModeListener;
            if (multiChoiceModeListener != null) {
                multiChoiceModeListener.onAllItemsCheckedStateChanged(true);
            }
        }

        public final void refreshAllState(boolean z) {
            if (z) {
                int count = this.mSource.getCount();
                for (int i = 0; i < count; i++) {
                    long j = -1;
                    boolean isCheckedPos = this.mSelectCheck.isCheckedPos(i);
                    boolean isCheckedPos2 = this.mOriginCheck.isCheckedPos(i);
                    boolean isCheckedPos3 = this.mRenderState.isCheckedPos(i);
                    if (isCheckedPos) {
                        j = this.mSelectCheck.getCheckedIdByPosition(i);
                    } else if (isCheckedPos2) {
                        j = this.mOriginCheck.getCheckedIdByPosition(i);
                    } else if (isCheckedPos3) {
                        j = this.mRenderState.getCheckedIdByPosition(i);
                    }
                    if (j < 0) {
                        j = this.mSource.getItemKey(i);
                    }
                    this.mSelectCheck.setChecked(i, j, true);
                }
            } else {
                this.mSelectCheck.cleanAll();
                this.mOriginCheck.cleanAll();
                this.mRenderState.cleanAll();
            }
            this.mSource.notifyCheckState();
        }

        public int getCount() {
            return this.mSource.getCount();
        }

        public String getType() {
            return this.mType;
        }

        public void setType(String str) {
            if (TextUtils.equals(this.mType, "image&video")) {
                return;
            }
            if (TextUtils.isEmpty(this.mType)) {
                this.mType = str;
            } else if (TextUtils.equals(str, this.mType)) {
            } else {
                this.mType = "image&video";
            }
        }

        public void setChecked(int i, long j, boolean z) {
            this.mSelectCheck.setChecked(i, j, z);
            this.mSource.notifyCheckState();
            MultiChoiceModeListener multiChoiceModeListener = this.mChoiceModeListener;
            if (multiChoiceModeListener != null) {
                multiChoiceModeListener.onItemCheckedStateChanged(i, j, z);
            }
        }

        public void setRenderChecked(int i, long j, boolean z) {
            this.mRenderState.setChecked(i, j, z);
        }

        public void appendOriginChecked(int i, long j, boolean z) {
            if (z) {
                this.mOriginCheck.appendCheck(i, j);
            }
        }

        public void appendCheck(int i, long j) {
            this.mSelectCheck.appendCheck(i, j);
        }

        public void finishInit() {
            this.mSource.notifyCheckState();
            MultiChoiceModeListener multiChoiceModeListener = this.mChoiceModeListener;
            if (multiChoiceModeListener != null) {
                multiChoiceModeListener.onItemCheckedStateChanged(0, this.mSource.getItemKey(0), true);
            }
        }

        public void finish() {
            this.mInAction = false;
            this.mSource.notifyCheckState();
        }
    }

    /* loaded from: classes.dex */
    public static class ViewProvider {
        public final String TAG;
        public List<Integer> mCountPerType;
        public SparseIntArray mLayoutIds;
        public ConcurrentHashMap<Integer, ArrayBlockingQueue> mViewCache;
        public int mViewTypeCount;

        /* renamed from: $r8$lambda$1ktwCUQlX2WC-lu6NZGPPNR9d00 */
        public static /* synthetic */ void m515$r8$lambda$1ktwCUQlX2WClu6NZGPPNR9d00(ViewProvider viewProvider, int i, View view, int i2, ViewGroup viewGroup) {
            viewProvider.lambda$init$0(i, view, i2, viewGroup);
        }

        public static /* synthetic */ void lambda$init$1(View view, int i, ViewGroup viewGroup) {
        }

        public ViewProvider(int i, int i2, SparseIntArray sparseIntArray) {
            this.TAG = "ViewProvider";
            this.mViewTypeCount = i;
            this.mCountPerType = new ArrayList(this.mViewTypeCount);
            this.mLayoutIds = sparseIntArray;
            this.mViewCache = new ConcurrentHashMap<>();
            for (int i3 = 0; i3 < this.mViewTypeCount; i3++) {
                this.mCountPerType.add(i3, Integer.valueOf(i2));
                this.mViewCache.put(Integer.valueOf(i3), new ArrayBlockingQueue(i2));
            }
        }

        public ViewProvider(int i, List<Integer> list, SparseIntArray sparseIntArray) {
            this.TAG = "ViewProvider";
            if (list == null || list.size() != i) {
                DefaultLogger.e("ViewProvider", "couterPerType is invalid");
                return;
            }
            this.mViewTypeCount = i;
            this.mCountPerType = list;
            this.mLayoutIds = sparseIntArray;
            this.mViewCache = new ConcurrentHashMap<>();
            for (int i2 = 0; i2 < this.mViewTypeCount; i2++) {
                this.mViewCache.put(Integer.valueOf(i2), new ArrayBlockingQueue(this.mCountPerType.get(i2).intValue()));
            }
        }

        public void initBy(LayoutInflater layoutInflater) {
            for (int i = 0; i < this.mViewTypeCount; i++) {
                for (int i2 = 0; i2 < this.mCountPerType.get(i).intValue(); i2++) {
                    PhotoPageItem photoPageItem = (PhotoPageItem) layoutInflater.inflate(this.mLayoutIds.get(i), (ViewGroup) null, false);
                    photoPageItem.setTag(R.id.tag_view_type, Integer.valueOf(i));
                    restore(null, photoPageItem);
                }
            }
        }

        public void init(Context context) {
            for (final int i = 0; i < this.mViewTypeCount; i++) {
                for (int i2 = 0; i2 < this.mCountPerType.get(i).intValue(); i2++) {
                    new GalleryAsyncLayoutInflater(context).inflate(this.mLayoutIds.get(i), null, new OnInflateFinishedListener() { // from class: com.miui.gallery.adapter.PhotoPageAdapter$ViewProvider$$ExternalSyntheticLambda0
                        @Override // androidx.asynclayoutinflater.view.OnInflateFinishedListener
                        public final void onInflateFinished(View view, int i3, ViewGroup viewGroup) {
                            PhotoPageAdapter.ViewProvider.m515$r8$lambda$1ktwCUQlX2WClu6NZGPPNR9d00(PhotoPageAdapter.ViewProvider.this, i, view, i3, viewGroup);
                        }
                    }, PhotoPageAdapter$ViewProvider$$ExternalSyntheticLambda1.INSTANCE);
                }
            }
        }

        public /* synthetic */ void lambda$init$0(int i, View view, int i2, ViewGroup viewGroup) {
            DefaultLogger.d("ViewProvider", "async inflate [%s] finished.", view);
            view.setTag(R.id.tag_view_type, Integer.valueOf(i));
            restore(viewGroup, (PhotoPageItem) view);
        }

        public void restore(ViewGroup viewGroup, PhotoPageItem photoPageItem) {
            Trace.beginSection("removeView");
            if (viewGroup != null) {
                viewGroup.removeView(photoPageItem);
            }
            Trace.endSection();
            Integer num = (Integer) photoPageItem.getTag(R.id.tag_view_type);
            if (num == null || num.intValue() >= this.mViewTypeCount) {
                DefaultLogger.e("ViewProvider", "error type [%d] for [%s].", num, photoPageItem);
                return;
            }
            try {
                this.mViewCache.get(num).add(photoPageItem);
            } catch (Exception unused) {
                DefaultLogger.e("ViewProvider", "caches is full");
            }
        }

        public PhotoPageItem acquire(int i, ViewGroup viewGroup) {
            Trace.beginSection("acquire");
            try {
                Trace.beginSection("getCache");
                PhotoPageItem photoPageItem = (PhotoPageItem) this.mViewCache.get(Integer.valueOf(i)).poll();
                Trace.endSection();
                if (photoPageItem == null) {
                    DefaultLogger.i("photoPageStartup", "no reusable item for type [%d], inflate sync.", Integer.valueOf(i));
                    PhotoPageItem photoPageItem2 = (PhotoPageItem) LayoutInflater.from(viewGroup.getContext()).inflate(this.mLayoutIds.get(i), viewGroup, false);
                    photoPageItem2.setTag(R.id.tag_view_type, Integer.valueOf(i));
                    return photoPageItem2;
                }
                DefaultLogger.d("ViewProvider", "reuse [%s].", photoPageItem);
                return photoPageItem;
            } finally {
                Trace.endSection();
            }
        }
    }
}
