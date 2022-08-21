package com.miui.gallery.ui.pictures;

import android.database.ContentObserver;
import android.graphics.Rect;
import android.os.Handler;
import android.provider.Settings;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.AccessibilityUtils;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.ReflectUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.ViewUtils;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.transition.ITransitItem;
import com.miui.gallery.widget.recyclerview.transition.ITransitionalAdapter;
import com.miui.gallery.widget.recyclerview.transition.TransitionAnchor;
import com.miui.gallery.widget.recyclerview.transition.TransitionHelper;
import com.miui.gallery.widget.recyclerview.transition.TransitionListener;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class PinchManager implements TransitionListener {
    public final LazyValue<RecyclerView, OverScroller> OVER_SCROLLER = new LazyValue<RecyclerView, OverScroller>() { // from class: com.miui.gallery.ui.pictures.PinchManager.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public OverScroller mo1272onInit(RecyclerView recyclerView) {
            String name = RecyclerView.class.getName();
            Object field = ReflectUtils.getField(name, recyclerView, "mViewFlinger");
            if (field != null) {
                return (OverScroller) ReflectUtils.getField(name + "$ViewFlinger", field, "mOverScroller");
            }
            return null;
        }
    };
    public final ContentObserver mA11yServiceObserver;
    public final ITransitionalAdapter mAdapter;
    public PictureViewMode mCurrentMode;
    public boolean mIsDestroyed;
    public boolean mIsTackBackEnabled;
    public boolean mIsTransiting;
    public final PinchCallback mPinchCallback;
    public int mPreOffset;
    public int mPrePosition;
    public PictureViewMode mPreViewMode;
    public final GalleryRecyclerView mRecycler;
    public int mTargetOffset;
    public int mTargetPosition;
    public PictureViewMode mTargetViewMode;
    public final TransitionHelper mTransitionHelper;

    public PinchManager(GalleryRecyclerView galleryRecyclerView, ITransitionalAdapter iTransitionalAdapter, PinchCallback pinchCallback, PictureViewMode pictureViewMode) {
        this.mRecycler = galleryRecyclerView;
        this.mAdapter = iTransitionalAdapter;
        this.mPinchCallback = pinchCallback;
        this.mCurrentMode = pictureViewMode;
        this.mIsTackBackEnabled = AccessibilityUtils.isTalkBackEnabled(galleryRecyclerView.getContext());
        TransitionHelper transitionHelper = new TransitionHelper(this);
        this.mTransitionHelper = transitionHelper;
        transitionHelper.bindTransition(galleryRecyclerView, !this.mIsTackBackEnabled ? pictureViewMode.getSupportedZoomFlag() : 0);
        A11yServiceObserver a11yServiceObserver = new A11yServiceObserver(this, ThreadManager.getMainHandler());
        this.mA11yServiceObserver = a11yServiceObserver;
        galleryRecyclerView.getContext().getContentResolver().registerContentObserver(Settings.Secure.getUriFor("enabled_accessibility_services"), true, a11yServiceObserver);
    }

    public static PinchManager install(GalleryRecyclerView galleryRecyclerView, ITransitionalAdapter iTransitionalAdapter, PinchCallback pinchCallback, PictureViewMode pictureViewMode) {
        return new PinchManager(galleryRecyclerView, iTransitionalAdapter, pinchCallback, pictureViewMode);
    }

    public void destroy() {
        this.mIsDestroyed = true;
        this.mRecycler.getContext().getContentResolver().unregisterContentObserver(this.mA11yServiceObserver);
    }

    public void zoomInBy(float f, float f2) {
        GalleryRecyclerView galleryRecyclerView = this.mRecycler;
        if (galleryRecyclerView == null || !galleryRecyclerView.isAttachedToWindow()) {
            return;
        }
        this.mTransitionHelper.startTransition(this.mRecycler, 1, f, f2);
    }

    public boolean isTransiting() {
        return this.mIsTransiting;
    }

    public void changeMode(PictureViewMode pictureViewMode) {
        this.mTransitionHelper.updateSupportedZoomFlag(!this.mIsTackBackEnabled ? pictureViewMode.getSupportedZoomFlag() : 0);
        if (this.mCurrentMode == pictureViewMode) {
            return;
        }
        this.mCurrentMode = pictureViewMode;
        adjustOverScrollerFriction(pictureViewMode);
        this.mPinchCallback.onPictureViewModeChanged(pictureViewMode);
    }

    public final void adjustOverScrollerFriction(PictureViewMode pictureViewMode) {
        OverScroller overScroller = this.OVER_SCROLLER.get(this.mRecycler);
        if (overScroller != null) {
            float scrollFriction = ViewConfiguration.getScrollFriction();
            if (PictureViewMode.isYearMode(pictureViewMode)) {
                overScroller.setFriction(scrollFriction * 6.67f);
            } else if (PictureViewMode.isMonthMode(pictureViewMode)) {
                overScroller.setFriction(scrollFriction * 3.34f);
            } else {
                overScroller.setFriction(scrollFriction);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x002b  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x003b  */
    @Override // com.miui.gallery.widget.recyclerview.transition.TransitionListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.miui.gallery.widget.recyclerview.transition.TransitionAnchor onTransitionPrepare(androidx.recyclerview.widget.RecyclerView r18, int r19, float r20, float r21) {
        /*
            Method dump skipped, instructions count: 328
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.pictures.PinchManager.onTransitionPrepare(androidx.recyclerview.widget.RecyclerView, int, float, float):com.miui.gallery.widget.recyclerview.transition.TransitionAnchor");
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.TransitionListener
    public void onPreTransition(RecyclerView recyclerView) {
        this.mPinchCallback.onPreTransition();
        this.mIsTransiting = true;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.TransitionListener
    public void onTransitionUpdate(RecyclerView recyclerView, float f) {
        this.mPinchCallback.onTransitionUpdate(f);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.TransitionListener
    public void onTransitionFinish(RecyclerView recyclerView, boolean z) {
        PictureViewMode pictureViewMode;
        this.mPinchCallback.onTransitionFinish(z);
        if (z) {
            changeMode(this.mTargetViewMode);
            this.mRecycler.scrollToPositionWithOffset(this.mTargetPosition, this.mTargetOffset);
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, this.mTargetViewMode.name());
            SamplingStatHelper.recordCountEvent("home", "zoom", hashMap);
            HashMap hashMap2 = new HashMap();
            hashMap2.put("ref_tip", "403.1.2.1.9881");
            hashMap2.put("tip", "403.1.6.1.9890");
            hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, this.mTargetViewMode.name());
            TrackController.trackDualFinger(hashMap2);
        }
        if (!z && (pictureViewMode = this.mPreViewMode) != null) {
            changeMode(pictureViewMode);
            this.mRecycler.scrollToPositionWithOffset(this.mPrePosition, this.mPreOffset);
        }
        final GalleryRecyclerView galleryRecyclerView = this.mRecycler;
        Objects.requireNonNull(galleryRecyclerView);
        galleryRecyclerView.post(new Runnable() { // from class: com.miui.gallery.ui.pictures.PinchManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GalleryRecyclerView.this.hideScrollerBar();
            }
        });
        this.mPreViewMode = null;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.TransitionListener
    public void onPostTransition(RecyclerView recyclerView) {
        this.mPinchCallback.onPostTransition();
        this.mIsTransiting = false;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.TransitionListener
    public List<ITransitItem> calculateToTransitItems(RecyclerView recyclerView, TransitionAnchor transitionAnchor, int i, int i2, Rect rect) {
        return this.mAdapter.calculateTransitItems(recyclerView, transitionAnchor.toAdapterPosition, transitionAnchor.toAnchorId, transitionAnchor.toGuideline, i, i2, rect, transitionAnchor.toViewMode, ViewUtils.isLayoutRtl(recyclerView), true);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.TransitionListener
    public List<ITransitItem> calculateFromTransitItems(RecyclerView recyclerView, TransitionAnchor transitionAnchor, int i, int i2, Rect rect) {
        return this.mAdapter.calculateTransitItems(recyclerView, transitionAnchor.fromAdapterPosition, transitionAnchor.fromAnchorId, transitionAnchor.fromGuideline, i, i2, rect, transitionAnchor.fromViewMode, ViewUtils.isLayoutRtl(recyclerView), false);
    }

    public final void onTalkBackStateChanged(boolean z) {
        if (!this.mIsDestroyed && this.mIsTackBackEnabled != z) {
            this.mIsTackBackEnabled = z;
            changeMode(z ? PictureViewMode.MICRO_THUMB : this.mCurrentMode);
        }
    }

    /* loaded from: classes2.dex */
    public static class A11yServiceObserver extends ContentObserver {
        public final WeakReference<PinchManager> mPinchManagerRef;

        public A11yServiceObserver(PinchManager pinchManager, Handler handler) {
            super(handler);
            this.mPinchManagerRef = new WeakReference<>(pinchManager);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            PinchManager pinchManager = this.mPinchManagerRef.get();
            if (pinchManager != null) {
                pinchManager.onTalkBackStateChanged(AccessibilityUtils.isTalkBackEnabled(StaticContext.sGetAndroidContext()));
            }
        }
    }
}
