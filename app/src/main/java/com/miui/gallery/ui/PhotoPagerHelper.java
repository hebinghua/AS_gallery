package com.miui.gallery.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import androidx.tracing.Trace;
import com.github.chrisbanes.photoview.OnBackgroundAlphaChangedListener;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnScaleChangeListener;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.util.RecyclerLayoutCache;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.util.photoview.TrimMemoryCallback;
import com.miui.gallery.widget.GalleryViewPager;
import com.miui.gallery.widget.PagerAdapter;
import com.miui.gallery.widget.ViewPager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class PhotoPagerHelper {
    public OnAlphaChangedListener mAlphaChangedListener;
    public OnDisplayRectChangedListener mDisplayRectChangedListener;
    public CloudImageLoadingListener mDownloadListener;
    public OnExitListener mExitListener;
    public OnImageLoadFinishListener mOnImageLoadFinishListener;
    public OnSpecialTypeEnterListener mOnSpecialTypeEnterListener;
    public OnPageChangedListener mPageChangedListener;
    public OnPageSettledListener mPageSettledListener;
    public OnPhotoDeleteListener mPhotoDeleteListener;
    public OnPhotoViewDragDownOutListener mPhotoViewDragDownOutListener;
    public OnRotateListener mRotateListener;
    public OnScaleChangedListener mScaleChangedListener;
    public RecyclerLayoutCache mSpecialTypeEnterViewCache;
    public OnSingleTapListener mTapListener;
    public ViewPager mViewPager;
    public ViewPager.OnPageSettledListener mInternalPageSettledListener = new ViewPager.OnPageSettledListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.1
        @Override // com.miui.gallery.widget.ViewPager.OnPageSettledListener
        public void onPageSettled(int i) {
            PhotoPagerHelper.this.setPageSettled(i);
        }
    };
    public ViewPager.OnPageChangeListener mInternalPageChangedListener = new ViewPager.OnPageChangeListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.2
        @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            PhotoPagerHelper.this.setOnPageScrolled(i, f, i2);
        }

        @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
            PhotoPagerHelper.this.setPageChanged(i);
        }

        @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
            PhotoPagerHelper.this.setPageScrollStateChanged(i);
        }
    };
    public OnViewTapListener mInternalTapListener = new OnViewTapListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.3
        @Override // com.github.chrisbanes.photoview.OnViewTapListener
        public void onViewTap(View view, float f, float f2) {
            if (PhotoPagerHelper.this.mTapListener != null) {
                PhotoPagerHelper.this.mTapListener.onTap(f, f2);
            }
        }
    };
    public CloudImageLoadingListener mInternalImageLoadingListener = new CloudImageLoadingListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.4
        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingStarted(Uri uri, DownloadType downloadType, View view) {
            if (PhotoPagerHelper.this.mDownloadListener != null) {
                PhotoPagerHelper.this.mDownloadListener.onLoadingStarted(uri, downloadType, view);
            }
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingFailed(Uri uri, DownloadType downloadType, View view, ErrorCode errorCode, String str) {
            if (PhotoPagerHelper.this.mDownloadListener != null) {
                PhotoPagerHelper.this.mDownloadListener.onLoadingFailed(uri, downloadType, view, errorCode, str);
            }
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingComplete(Uri uri, DownloadType downloadType, View view, Bitmap bitmap) {
            if (PhotoPagerHelper.this.mDownloadListener != null) {
                PhotoPagerHelper.this.mDownloadListener.onLoadingComplete(uri, downloadType, view, bitmap);
            }
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onDownloadComplete(Uri uri, DownloadType downloadType, View view, String str) {
            if (PhotoPagerHelper.this.mDownloadListener != null) {
                PhotoPagerHelper.this.mDownloadListener.onDownloadComplete(uri, downloadType, view, str);
            }
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingCancelled(Uri uri, DownloadType downloadType, View view) {
            if (PhotoPagerHelper.this.mDownloadListener != null) {
                PhotoPagerHelper.this.mDownloadListener.onLoadingCancelled(uri, downloadType, view);
            }
        }
    };
    public OnScaleChangeListener mInternalScaleListener = new OnScaleChangeListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.5
        @Override // com.github.chrisbanes.photoview.OnScaleChangeListener
        public void onScaleChange(float f, float f2, float f3, float f4, float f5) {
            if (PhotoPagerHelper.this.mScaleChangedListener != null) {
                PhotoPagerHelper.this.mScaleChangedListener.onScaleChanged(f, f2, f5);
            }
        }
    };
    public com.github.chrisbanes.photoview.OnExitListener mInternalExitListener = new com.github.chrisbanes.photoview.OnExitListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.6
        @Override // com.github.chrisbanes.photoview.OnExitListener
        public void onExit() {
            if (PhotoPagerHelper.this.mExitListener != null) {
                PhotoPagerHelper.this.mExitListener.onExit();
            }
        }
    };
    public com.github.chrisbanes.photoview.OnPhotoViewDragDownOutListener mInternalPhotoViewDragDownOutListener = new com.github.chrisbanes.photoview.OnPhotoViewDragDownOutListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.7
        @Override // com.github.chrisbanes.photoview.OnPhotoViewDragDownOutListener
        public void onPhotoDragDownOut() {
            if (PhotoPagerHelper.this.mPhotoViewDragDownOutListener != null) {
                PhotoPagerHelper.this.mPhotoViewDragDownOutListener.onPhotoDragDownOut();
            }
        }
    };
    public OnMatrixChangedListener mInternalMatrixListener = new OnMatrixChangedListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.8
        @Override // com.github.chrisbanes.photoview.OnMatrixChangedListener
        public void onMatrixChanged(RectF rectF) {
            if (PhotoPagerHelper.this.mDisplayRectChangedListener != null) {
                PhotoPagerHelper.this.mDisplayRectChangedListener.onDisplayRectChanged(rectF);
            }
        }
    };
    public ViewGroup.OnHierarchyChangeListener mInternalHierarchyChangeListener = new ViewGroup.OnHierarchyChangeListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.9
        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewAdded(View view, View view2) {
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewRemoved(View view, View view2) {
        }
    };
    public OnBackgroundAlphaChangedListener mInternalAlphaChangedListener = new OnBackgroundAlphaChangedListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.10
        @Override // com.github.chrisbanes.photoview.OnBackgroundAlphaChangedListener
        public void onAlphaChanged(float f) {
            if (PhotoPagerHelper.this.mAlphaChangedListener != null) {
                PhotoPagerHelper.this.mAlphaChangedListener.onAlphaChanged(f);
            }
        }
    };
    public PhotoPageItem.OnImageLoadFinishListener mInternalOnImageLoadFinishListener = new PhotoPageItem.OnImageLoadFinishListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.11
        @Override // com.miui.gallery.ui.PhotoPageItem.OnImageLoadFinishListener
        public void onImageLoadFinish(String str, boolean z, Bitmap bitmap) {
            if (PhotoPagerHelper.this.mOnImageLoadFinishListener != null) {
                PhotoPagerHelper.this.mOnImageLoadFinishListener.onImageLoadFinish(str);
            }
        }
    };
    public PhotoPageItem.OnSpecialTypeEnterListener mInternalOnSpecialTypeEnterListener = new PhotoPageItem.OnSpecialTypeEnterListener() { // from class: com.miui.gallery.ui.PhotoPagerHelper.12
        @Override // com.miui.gallery.ui.PhotoPageItem.OnSpecialTypeEnterListener
        public void onEnterClick(BaseDataItem baseDataItem, long j) {
            if (PhotoPagerHelper.this.mOnSpecialTypeEnterListener != null) {
                PhotoPagerHelper.this.mOnSpecialTypeEnterListener.onEntersClick(baseDataItem, j);
            }
        }
    };
    public TrimMemoryCallback mInternalTrimMemoryCallback = new TrimMemoryCallback() { // from class: com.miui.gallery.ui.PhotoPagerHelper.13
        @Override // com.miui.gallery.util.photoview.TrimMemoryCallback
        public void onTrimMemory(int i) {
            LinkedList linkedList = new LinkedList();
            int currentItem = PhotoPagerHelper.this.mViewPager.getCurrentItem();
            for (int i2 = currentItem - 1; i2 <= currentItem + 1; i2++) {
                PhotoPageItem item = PhotoPagerHelper.this.getItem(i2);
                if (item != null) {
                    linkedList.add(item);
                }
            }
            for (int i3 = 0; i3 < PhotoPagerHelper.this.getActiveItemCount(); i3++) {
                PhotoPageItem itemByNativeIndex = PhotoPagerHelper.this.getItemByNativeIndex(i3);
                if (itemByNativeIndex != null && !linkedList.contains(itemByNativeIndex)) {
                    itemByNativeIndex.onTrimMemory(i);
                }
            }
        }

        @Override // com.miui.gallery.util.photoview.TrimMemoryCallback
        public void onStopTrimMemory(int i) {
            for (int i2 = 0; i2 < PhotoPagerHelper.this.getActiveItemCount(); i2++) {
                PhotoPageItem itemByNativeIndex = PhotoPagerHelper.this.getItemByNativeIndex(i2);
                if (itemByNativeIndex != null) {
                    itemByNativeIndex.onStopTrimMemory(i);
                }
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface ItemFunction<R> {
        default boolean accept(PhotoPageItem photoPageItem) {
            return true;
        }

        R run(PhotoPageItem photoPageItem);
    }

    /* loaded from: classes2.dex */
    public interface OnAlphaChangedListener {
        void onAlphaChanged(float f);
    }

    /* loaded from: classes2.dex */
    public interface OnDisplayRectChangedListener {
        void onDisplayRectChanged(RectF rectF);
    }

    /* loaded from: classes2.dex */
    public interface OnExitListener {
        void onExit();
    }

    /* loaded from: classes2.dex */
    public interface OnImageLoadFinishListener {
        void onImageLoadFinish(String str);
    }

    /* loaded from: classes2.dex */
    public interface OnPageChangedListener {
        void onPageChanged(int i);

        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);
    }

    /* loaded from: classes2.dex */
    public interface OnPageSettledListener {
        void onPageSettled(int i);
    }

    /* loaded from: classes2.dex */
    public interface OnPhotoDeleteListener {
        void onDelete();
    }

    /* loaded from: classes2.dex */
    public interface OnPhotoViewDragDownOutListener {
        void onPhotoDragDownOut();
    }

    /* loaded from: classes2.dex */
    public interface OnRotateListener {
        void onRotate(float f);
    }

    /* loaded from: classes2.dex */
    public interface OnScaleChangedListener {
        void onScaleChanged(float f, float f2, float f3);
    }

    /* loaded from: classes2.dex */
    public interface OnSingleTapListener {
        void onTap(float f, float f2);
    }

    /* loaded from: classes2.dex */
    public interface OnSpecialTypeEnterListener {
        void onEntersClick(BaseDataItem baseDataItem, long j);
    }

    public PhotoPagerHelper(ViewPager viewPager) {
        this.mViewPager = viewPager;
        viewPager.setOnPageSettledListener(this.mInternalPageSettledListener);
        this.mViewPager.setOnPageChangeListener(this.mInternalPageChangedListener);
        this.mViewPager.setOnHierarchyChangeListener(this.mInternalHierarchyChangeListener);
    }

    public void setOnPageScrolled(int i, float f, int i2) {
        OnPageChangedListener onPageChangedListener = this.mPageChangedListener;
        if (onPageChangedListener != null) {
            onPageChangedListener.onPageScrolled(i, f, i2);
        }
    }

    public void setPageChanged(int i) {
        OnPageChangedListener onPageChangedListener = this.mPageChangedListener;
        if (onPageChangedListener != null) {
            onPageChangedListener.onPageChanged(i);
        }
        PhotoPageItem item = getItem(i);
        if (item != null) {
            item.setSpecialTypeEnterViewCache(this.mSpecialTypeEnterViewCache);
            item.onSelecting();
        }
    }

    public void setPageScrollStateChanged(int i) {
        PhotoPageItem currentItem = getCurrentItem();
        if (currentItem != null) {
            if (i == 1) {
                currentItem.onPageScrollDragging();
            } else if (i == 0) {
                currentItem.onPageScrollIdle();
            }
        }
        OnPageChangedListener onPageChangedListener = this.mPageChangedListener;
        if (onPageChangedListener != null) {
            onPageChangedListener.onPageScrollStateChanged(i);
        }
    }

    public <R> List<R> invokePageItems(ItemFunction<R> itemFunction) {
        int activeItemCount = getActiveItemCount();
        ArrayList arrayList = new ArrayList(activeItemCount);
        for (int i = 0; i < activeItemCount; i++) {
            PhotoPageItem itemByNativeIndex = getItemByNativeIndex(i);
            if (itemFunction.accept(itemByNativeIndex)) {
                arrayList.add(itemFunction.run(itemByNativeIndex));
            }
        }
        return arrayList;
    }

    public void setPageSettled(int i) {
        OnPageSettledListener onPageSettledListener = this.mPageSettledListener;
        if (onPageSettledListener != null) {
            onPageSettledListener.onPageSettled(i);
        }
        Trace.beginSection("swapListener");
        int previousItem = this.mViewPager.getPreviousItem();
        PhotoPageItem item = getItem(previousItem);
        if (item != null) {
            Trace.beginSection("onUnSelected");
            item.resetDefaultPhotoStatus();
            Trace.beginSection("doUnSelected");
            item.onUnSelected(false, previousItem - i);
            Trace.endSection();
            item.setOnViewTapListener(null);
            item.setCloudImageLoadingListener(null);
            item.setOnExitListener(null);
            item.setOnPhotoViewDragDownOutListener(null);
            item.setOnScaleChangeListener(null);
            item.setOnRotateListener(null);
            item.removeOnMatrixChangeListener(this.mInternalMatrixListener);
            item.setOnBackgroundAlphaChangedListener(null);
            item.setTrimMemoryCallback(null);
            item.setOnSpecialTypeEnterListener(null);
            item.setOnDeleteListener(null);
            item.setSpecialTypeEnterViewCache(this.mSpecialTypeEnterViewCache);
            item.setOnImageLoadFinishListener(this.mInternalOnImageLoadFinishListener);
            Trace.endSection();
        }
        PhotoPageItem item2 = getItem(i);
        if (item2 != null) {
            Trace.beginSection("selected");
            item2.setOnViewTapListener(this.mInternalTapListener);
            item2.setCloudImageLoadingListener(this.mInternalImageLoadingListener);
            item2.setOnExitListener(this.mInternalExitListener);
            item2.setOnPhotoViewDragDownOutListener(this.mInternalPhotoViewDragDownOutListener);
            item2.setOnScaleChangeListener(this.mInternalScaleListener);
            item2.setOnRotateListener(this.mRotateListener);
            item2.addOnMatrixChangeListener(this.mInternalMatrixListener);
            item2.setOnBackgroundAlphaChangedListener(this.mInternalAlphaChangedListener);
            item2.setTrimMemoryCallback(this.mInternalTrimMemoryCallback);
            item2.setOnSpecialTypeEnterListener(this.mInternalOnSpecialTypeEnterListener);
            item2.setOnDeleteListener(this.mPhotoDeleteListener);
            item2.setSpecialTypeEnterViewCache(this.mSpecialTypeEnterViewCache);
            item2.setOnImageLoadFinishListener(this.mInternalOnImageLoadFinishListener);
            Trace.beginSection("doSelected");
            item2.onSelected(false);
            Trace.endSection();
            Trace.endSection();
        }
        Trace.endSection();
    }

    public void setOnPageSettledListener(OnPageSettledListener onPageSettledListener) {
        this.mPageSettledListener = onPageSettledListener;
    }

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.mPageChangedListener = onPageChangedListener;
    }

    public void setOnTapListener(OnSingleTapListener onSingleTapListener) {
        this.mTapListener = onSingleTapListener;
    }

    public void setOnDownloadListener(CloudImageLoadingListener cloudImageLoadingListener) {
        CloudImageLoadingListener cloudImageLoadingListener2 = this.mDownloadListener;
        if (cloudImageLoadingListener2 == null || cloudImageLoadingListener != cloudImageLoadingListener2) {
            this.mDownloadListener = cloudImageLoadingListener;
        }
    }

    public void setOnScaleChangedListener(OnScaleChangedListener onScaleChangedListener) {
        this.mScaleChangedListener = onScaleChangedListener;
    }

    public void setOnRotateListener(OnRotateListener onRotateListener) {
        this.mRotateListener = onRotateListener;
    }

    public void setOnExitListener(OnExitListener onExitListener) {
        this.mExitListener = onExitListener;
    }

    public void setOnPhotoViewDragDownOutListener(OnPhotoViewDragDownOutListener onPhotoViewDragDownOutListener) {
        this.mPhotoViewDragDownOutListener = onPhotoViewDragDownOutListener;
    }

    public void setOnDisplayRectChangedListener(OnDisplayRectChangedListener onDisplayRectChangedListener) {
        this.mDisplayRectChangedListener = onDisplayRectChangedListener;
    }

    public void setOnAlphaChangedListener(OnAlphaChangedListener onAlphaChangedListener) {
        this.mAlphaChangedListener = onAlphaChangedListener;
    }

    public void setOnImageLoadFinishListener(OnImageLoadFinishListener onImageLoadFinishListener) {
        this.mOnImageLoadFinishListener = onImageLoadFinishListener;
    }

    public void setOnSpecialTypeEnterListener(OnSpecialTypeEnterListener onSpecialTypeEnterListener) {
        this.mOnSpecialTypeEnterListener = onSpecialTypeEnterListener;
    }

    public void setSpecialTypeEnterViewCache(RecyclerLayoutCache recyclerLayoutCache) {
        this.mSpecialTypeEnterViewCache = recyclerLayoutCache;
    }

    public void setOnDeleteListener(OnPhotoDeleteListener onPhotoDeleteListener) {
        this.mPhotoDeleteListener = onPhotoDeleteListener;
    }

    public int getActiveItemCount() {
        return this.mViewPager.getActiveCount();
    }

    public PhotoPageItem getItemByNativeIndex(int i) {
        Object itemByNativeIndex = this.mViewPager.getItemByNativeIndex(i);
        if (PhotoPageAdapter.isItemValidate(itemByNativeIndex)) {
            return (PhotoPageItem) itemByNativeIndex;
        }
        return null;
    }

    public PhotoPageItem getItem(int i) {
        Object item = this.mViewPager.getItem(i);
        if (PhotoPageAdapter.isItemValidate(item)) {
            return (PhotoPageItem) item;
        }
        return null;
    }

    public PhotoPageItem getCurrentItem() {
        return getItem(this.mViewPager.getCurrentItem());
    }

    public void onStart() {
        PhotoPageItem currentItem = getCurrentItem();
        if (currentItem != null) {
            currentItem.onSelected(true);
        }
    }

    public void onStop() {
        PhotoPageItem currentItem = getCurrentItem();
        int activeItemCount = getActiveItemCount();
        for (int i = 0; i < activeItemCount; i++) {
            PhotoPageItem itemByNativeIndex = getItemByNativeIndex(i);
            if (itemByNativeIndex != null && currentItem != itemByNativeIndex) {
                itemByNativeIndex.onTrimMemory(2);
            }
        }
        if (currentItem != null) {
            currentItem.onUnSelected(true, 0);
        }
    }

    public void clearTrimMemoryFlag() {
        int activeItemCount = getActiveItemCount();
        for (int i = 0; i < activeItemCount; i++) {
            PhotoPageItem itemByNativeIndex = getItemByNativeIndex(i);
            if (itemByNativeIndex != null) {
                itemByNativeIndex.clearTrimMemoryFlag();
            }
        }
    }

    public void stopTrimMemory() {
        int activeItemCount = getActiveItemCount();
        for (int i = 0; i < activeItemCount; i++) {
            PhotoPageItem itemByNativeIndex = getItemByNativeIndex(i);
            if (itemByNativeIndex != null) {
                itemByNativeIndex.onStopTrimMemory(2);
            }
        }
    }

    public void onActivityTransition() {
        int activeItemCount = getActiveItemCount();
        for (int i = 0; i < activeItemCount; i++) {
            PhotoPageItem itemByNativeIndex = getItemByNativeIndex(i);
            if (itemByNativeIndex != null) {
                itemByNativeIndex.onActivityTransition();
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        PhotoPageItem currentItem = getCurrentItem();
        if (currentItem != null) {
            currentItem.onActivityResult(i, i2, intent);
        }
    }

    public boolean doExitTransition(ItemViewInfo itemViewInfo, PhotoPageItem.PhotoTransitionListener photoTransitionListener) {
        PhotoPageItem currentItem = getCurrentItem();
        if (currentItem != null) {
            currentItem.animExit(itemViewInfo, photoTransitionListener);
            return true;
        }
        return false;
    }

    public void onActionBarVisibleChanged(boolean z, int i) {
        int activeItemCount = getActiveItemCount();
        PhotoPageItem currentItem = getCurrentItem();
        for (int i2 = 0; i2 < activeItemCount; i2++) {
            PhotoPageItem itemByNativeIndex = getItemByNativeIndex(i2);
            if (itemByNativeIndex != null) {
                itemByNativeIndex.dispatchActionBarVisibleChanged(Boolean.valueOf(z), i, itemByNativeIndex == currentItem);
            }
        }
    }

    public RectF getCurItemDisplayRect() {
        Drawable drawable;
        PhotoPageItem currentItem = getCurrentItem();
        if (currentItem == null || (drawable = currentItem.mPhotoView.getDrawable()) == null) {
            return null;
        }
        RectF rectF = new RectF(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        currentItem.mPhotoView.getAbsoluteRect(rectF);
        return rectF;
    }

    public float getCurrentItemScale() {
        PhotoPageItem currentItem = getCurrentItem();
        if (currentItem != null) {
            return currentItem.mPhotoView.getScale();
        }
        return 0.0f;
    }

    public int getCorrectPosition(int i, boolean z) {
        if (z) {
            PagerAdapter adapter = this.mViewPager.getAdapter();
            return GalleryViewPager.processingIndex(i, adapter == null ? 0 : adapter.getCount(), true);
        }
        return i;
    }
}
