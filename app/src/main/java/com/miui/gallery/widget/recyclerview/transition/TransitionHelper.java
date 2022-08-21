package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import androidx.core.math.MathUtils;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DeviceCharacteristics;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.transition.ImageTransitionRender;
import com.miui.gallery.widget.recyclerview.transition.ScaleTouchListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes3.dex */
public class TransitionHelper implements ScaleTouchListener.OnScaleListener {
    public static final int DELAY_MILLIS_FOR_LAYOUT;
    public static final Interpolator FAST_ALPHA_INTERPOLATOR;
    public static final Interpolator SLOW_ALPHA_INTERPOLATOR;
    public TransitionAnchor mAnchor;
    public List<HeaderTransitItem> mFromHeaderTransitItems;
    public List<List<ItemWrapper>> mFromSpanGroups;
    public ScaleTouchListener mScaleTouchListener;
    public List<HeaderTransitItem> mToHeaderTransitItems;
    public List<List<ItemWrapper>> mToSpanGroups;
    public WeakReference<RecyclerView> mTransitingRecycler;
    public Transition mTransition;
    public final TransitionListener mTransitionListener;
    public ITransitionOverlay mTransitionOverlay;
    public float mCurScale = 1.0f;
    public boolean mIsFastScale = false;
    public float mScaleBeginFactor = 1.0f;
    public int mState = -1;

    public static /* synthetic */ void $r8$lambda$IlzjrBZdDaZh2MkJOxlii3H4r0E(TransitionHelper transitionHelper, float f, float f2, float f3) {
        transitionHelper.lambda$startAutoTransition$0(f, f2, f3);
    }

    public static /* synthetic */ void $r8$lambda$q91Sc1sBPjUjfELDvfgr_4PuWpY(List list, List list2, ITransitItem iTransitItem) {
        lambda$wrapTransitItems$3(list, list2, iTransitItem);
    }

    static {
        DELAY_MILLIS_FOR_LAYOUT = DeviceCharacteristics.isHighEndDevice() ? 200 : 250;
        FAST_ALPHA_INTERPOLATOR = new CubicEaseOutInterpolator();
        SLOW_ALPHA_INTERPOLATOR = new LinearInterpolator();
    }

    public TransitionHelper(TransitionListener transitionListener) {
        this.mTransitionListener = transitionListener;
    }

    public void bindTransition(RecyclerView recyclerView, int i) {
        ScaleTouchListener scaleTouchListener = new ScaleTouchListener(recyclerView, this);
        this.mScaleTouchListener = scaleTouchListener;
        recyclerView.removeOnItemTouchListener(scaleTouchListener);
        recyclerView.addOnItemTouchListener(this.mScaleTouchListener);
        this.mScaleTouchListener.updateSupportedZoomFlag(i);
    }

    public void updateSupportedZoomFlag(int i) {
        this.mScaleTouchListener.updateSupportedZoomFlag(i);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ScaleTouchListener.OnScaleListener
    public boolean onScaleBegin(RecyclerView recyclerView, int i, float f, float f2, float f3) {
        int i2 = this.mState;
        boolean z = false;
        if (i2 != -1) {
            DefaultLogger.w("TransitionHelper", "onScaleBegin, current state %s doesn't support manual scale", Integer.valueOf(i2));
            return false;
        }
        TransitionAnchor onTransitionPrepare = this.mTransitionListener.onTransitionPrepare(recyclerView, i, f2, f3);
        saveTransitingViews(recyclerView);
        if (onTransitionPrepare != null && onTransitionPrepare.isValid()) {
            this.mTransitionListener.onPreTransition(recyclerView);
            this.mAnchor = onTransitionPrepare;
            setState(0);
            this.mCurScale = 1.0f;
            this.mScaleBeginFactor = f;
            if (Math.abs(f - 1.0f) > 0.1f) {
                z = true;
            }
            this.mIsFastScale = z;
            List<ITransitItem> calculateFromTransitItems = this.mTransitionListener.calculateFromTransitItems(recyclerView, this.mAnchor, recyclerView.getWidth(), recyclerView.getHeight(), new Rect(recyclerView.getPaddingStart(), recyclerView.getPaddingTop(), recyclerView.getPaddingEnd(), recyclerView.getPaddingBottom()));
            ArrayList arrayList = new ArrayList();
            this.mFromSpanGroups = arrayList;
            LinkedList linkedList = new LinkedList();
            this.mFromHeaderTransitItems = linkedList;
            wrapTransitItems(calculateFromTransitItems, arrayList, linkedList);
            return true;
        }
        stopTransitionInternal(false);
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x008a  */
    @Override // com.miui.gallery.widget.recyclerview.transition.ScaleTouchListener.OnScaleListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onScale(androidx.recyclerview.widget.RecyclerView r17, int r18, float r19) {
        /*
            r16 = this;
            r10 = r16
            r11 = r18
            int r0 = r10.mState
            java.lang.String r12 = "TransitionHelper"
            r13 = 0
            if (r0 == 0) goto L15
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            java.lang.String r1 = "onScale current state %s, does onScaleBegin hasn't called?"
            com.miui.gallery.util.logger.DefaultLogger.w(r12, r1, r0)
            return r13
        L15:
            boolean r0 = r10.mIsFastScale
            r14 = 1
            r15 = r0 ^ 1
            com.miui.gallery.widget.recyclerview.transition.TransitionAnchor r2 = r10.mAnchor
            if (r2 == 0) goto L82
            com.miui.gallery.widget.recyclerview.transition.TransitionListener r0 = r10.mTransitionListener
            int r3 = r17.getWidth()
            int r4 = r17.getHeight()
            android.graphics.Rect r5 = new android.graphics.Rect
            int r1 = r17.getPaddingStart()
            int r6 = r17.getPaddingTop()
            int r7 = r17.getPaddingEnd()
            int r8 = r17.getPaddingBottom()
            r5.<init>(r1, r6, r7, r8)
            r1 = r17
            java.util.List r0 = r0.calculateToTransitItems(r1, r2, r3, r4, r5)
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r10.mToSpanGroups = r1
            java.util.LinkedList r2 = new java.util.LinkedList
            r2.<init>()
            r10.mToHeaderTransitItems = r2
            wrapTransitItems(r0, r1, r2)
            com.miui.gallery.widget.recyclerview.transition.TransitionAnchor r0 = r10.mAnchor
            long r3 = r0.fromAnchorId
            long r5 = r0.toAnchorId
            com.miui.gallery.ui.pictures.PictureViewMode r7 = r0.fromViewMode
            com.miui.gallery.ui.pictures.PictureViewMode r8 = r0.toViewMode
            boolean r9 = r0.marginStart
            r0 = r16
            r1 = r17
            r2 = r18
            boolean r0 = r0.prepareTransition(r1, r2, r3, r5, r7, r8, r9)
            if (r0 != 0) goto L75
            java.lang.String r0 = "prepare transition error"
            com.miui.gallery.util.logger.DefaultLogger.e(r12, r0)
            r10.stopTransitionInternal(r13)
            return r13
        L75:
            r0 = 0
            r10.mAnchor = r0
            if (r15 == 0) goto L82
            float r0 = r10.mScaleBeginFactor
            r1 = r17
            r10.doScale(r1, r11, r0)
            goto L84
        L82:
            r1 = r17
        L84:
            if (r15 == 0) goto L8a
            r16.doScale(r17, r18, r19)
            return r14
        L8a:
            r10.setState(r14)
            r0 = 0
            r1 = 1065353216(0x3f800000, float:1.0)
            r10.startAutoTransition(r0, r1, r11)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.recyclerview.transition.TransitionHelper.onScale(androidx.recyclerview.widget.RecyclerView, int, float):boolean");
    }

    public final void doScale(RecyclerView recyclerView, int i, float f) {
        float f2 = this.mCurScale * f;
        this.mCurScale = f2;
        if (i == 2) {
            this.mCurScale = MathUtils.clamp(f2, 0.0f, 1.0f);
        }
        if (i == 1) {
            this.mCurScale = MathUtils.clamp(this.mCurScale, 1.0f, 2.0f);
        }
        float translateProgress = translateProgress();
        this.mTransitionOverlay.updateProgress(recyclerView, false, translateProgress);
        this.mTransitionListener.onTransitionUpdate(recyclerView, translateProgress);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ScaleTouchListener.OnScaleListener
    public void onScaleEnd(RecyclerView recyclerView, int i, float f) {
        int i2 = this.mState;
        if (i2 != 0) {
            DefaultLogger.w("TransitionHelper", "onScaleEnd current state %s, does onScaleBegin have been called?", Integer.valueOf(i2));
            return;
        }
        this.mCurScale *= f;
        float translateProgress = translateProgress();
        float f2 = 0.0f;
        if (translateProgress > 0.0f) {
            f2 = 1.0f;
        }
        boolean z = true;
        if (this.mTransitionOverlay == null) {
            DefaultLogger.w("TransitionHelper", "does prepareTransition have been called?");
            if (f2 <= 0.99f) {
                z = false;
            }
            stopTransitionInternal(z);
            return;
        }
        setState(1);
        if (f2 <= 0.99f) {
            i = i == 1 ? 2 : 1;
        }
        startAutoTransition(translateProgress, f2, i);
    }

    public final void startAutoTransition(final float f, float f2, int i) {
        this.mTransition = new Transition(this.mTransitingRecycler.get(), new Transition.OnUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.transition.TransitionHelper$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.widget.recyclerview.transition.TransitionHelper.Transition.OnUpdateListener
            public final void onUpdate(float f3, float f4) {
                TransitionHelper.$r8$lambda$IlzjrBZdDaZh2MkJOxlii3H4r0E(TransitionHelper.this, f, f3, f4);
            }
        }, new CubicEaseOutInterpolator(), 500);
        RecyclerView recyclerView = this.mTransitingRecycler.get();
        if (recyclerView != null) {
            recyclerView.suppressLayout(true);
        }
        this.mTransition.start(f, f2);
    }

    public /* synthetic */ void lambda$startAutoTransition$0(float f, float f2, float f3) {
        RecyclerView recyclerView = this.mTransitingRecycler.get();
        boolean z = false;
        if (recyclerView == null) {
            DefaultLogger.w("TransitionHelper", "view released while updating transition");
            stopTransitionInternal(false);
            return;
        }
        this.mTransitionOverlay.updateProgress(recyclerView, f == 0.0f, f2);
        if (Float.compare(f2, f3) == 0) {
            if (f3 > 0.99f) {
                z = true;
            }
            stopTransitionInternal(z);
            return;
        }
        this.mTransitionListener.onTransitionUpdate(recyclerView, f2);
    }

    public final void setState(int i) {
        this.mState = i;
    }

    public final float translateProgress() {
        return Math.abs(this.mCurScale - 1.0f);
    }

    public final boolean prepareTransition(RecyclerView recyclerView, int i, long j, long j2, PictureViewMode pictureViewMode, PictureViewMode pictureViewMode2, boolean z) {
        if (this.mTransitionOverlay == null) {
            this.mTransitionOverlay = new TransitionOverlay(recyclerView.getContext());
        }
        this.mTransitionOverlay.attach(recyclerView);
        List<ITransitionRender> calculateRenderItems = calculateRenderItems(recyclerView, this.mFromSpanGroups, this.mToSpanGroups, this.mFromHeaderTransitItems, this.mToHeaderTransitItems, i, j, j2, pictureViewMode, pictureViewMode2, z);
        List<List<ItemWrapper>> list = this.mFromSpanGroups;
        if (list != null) {
            list.clear();
            this.mFromSpanGroups = null;
        }
        List<List<ItemWrapper>> list2 = this.mToSpanGroups;
        if (list2 != null) {
            list2.clear();
            this.mToSpanGroups = null;
        }
        List<HeaderTransitItem> list3 = this.mFromHeaderTransitItems;
        if (list3 != null) {
            list3.clear();
            this.mFromHeaderTransitItems = null;
        }
        List<HeaderTransitItem> list4 = this.mToHeaderTransitItems;
        if (list4 != null) {
            list4.clear();
            this.mToHeaderTransitItems = null;
        }
        if (calculateRenderItems == null) {
            return false;
        }
        this.mTransitionOverlay.prepare(recyclerView, calculateRenderItems);
        return true;
    }

    public void startTransition(final RecyclerView recyclerView, final int i, float f, float f2) {
        int i2 = this.mState;
        if (i2 == 1) {
            DefaultLogger.i("TransitionHelper", "auto transition is ongoing, ignore this starting");
            return;
        }
        if (i2 != -1) {
            DefaultLogger.i("TransitionHelper", "start auto transition while not idle, preState is: %d", Integer.valueOf(i2));
            stopTransition();
        }
        TransitionAnchor onTransitionPrepare = this.mTransitionListener.onTransitionPrepare(recyclerView, i, f, f2);
        saveTransitingViews(recyclerView);
        if (onTransitionPrepare == null || !onTransitionPrepare.isValid()) {
            DefaultLogger.e("TransitionHelper", "invalid anchor %s", onTransitionPrepare);
            stopTransitionInternal(false);
            return;
        }
        this.mAnchor = onTransitionPrepare;
        this.mTransitionListener.onPreTransition(recyclerView);
        List<ITransitItem> calculateFromTransitItems = this.mTransitionListener.calculateFromTransitItems(recyclerView, this.mAnchor, recyclerView.getWidth(), recyclerView.getHeight(), new Rect(recyclerView.getPaddingStart(), recyclerView.getPaddingTop(), recyclerView.getPaddingEnd(), recyclerView.getPaddingBottom()));
        ArrayList arrayList = new ArrayList();
        this.mFromSpanGroups = arrayList;
        LinkedList linkedList = new LinkedList();
        this.mFromHeaderTransitItems = linkedList;
        wrapTransitItems(calculateFromTransitItems, arrayList, linkedList);
        recyclerView.post(new Runnable() { // from class: com.miui.gallery.widget.recyclerview.transition.TransitionHelper$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                TransitionHelper.this.lambda$startTransition$1(recyclerView, i);
            }
        });
    }

    public /* synthetic */ void lambda$startTransition$1(RecyclerView recyclerView, int i) {
        int i2 = this.mState;
        if (i2 != -1) {
            DefaultLogger.w("TransitionHelper", "cur state %s doesn't support auto transition", Integer.valueOf(i2));
            return;
        }
        TransitionAnchor transitionAnchor = this.mAnchor;
        if (transitionAnchor == null) {
            DefaultLogger.w("TransitionHelper", "anchor is null, transition maybe aborted");
            return;
        }
        List<ITransitItem> calculateToTransitItems = this.mTransitionListener.calculateToTransitItems(recyclerView, transitionAnchor, recyclerView.getWidth(), recyclerView.getHeight(), new Rect(recyclerView.getPaddingStart(), recyclerView.getPaddingTop(), recyclerView.getPaddingEnd(), recyclerView.getPaddingBottom()));
        ArrayList arrayList = new ArrayList();
        this.mToSpanGroups = arrayList;
        LinkedList linkedList = new LinkedList();
        this.mToHeaderTransitItems = linkedList;
        wrapTransitItems(calculateToTransitItems, arrayList, linkedList);
        RecyclerView recyclerView2 = this.mTransitingRecycler.get();
        TransitionAnchor transitionAnchor2 = this.mAnchor;
        if (prepareTransition(recyclerView2, i, transitionAnchor2.fromAnchorId, transitionAnchor2.toAnchorId, transitionAnchor2.fromViewMode, transitionAnchor2.toViewMode, transitionAnchor2.marginStart)) {
            setState(1);
            startAutoTransition(0.0f, 1.0f, i);
            return;
        }
        stopTransitionInternal(false);
    }

    public final void stopTransition() {
        stopTransitionInternal(false);
    }

    public final void stopTransitionInternal(boolean z) {
        WeakReference<RecyclerView> weakReference = this.mTransitingRecycler;
        final RecyclerView recyclerView = weakReference != null ? weakReference.get() : null;
        if (recyclerView != null) {
            recyclerView.suppressLayout(false);
            this.mTransitionListener.onTransitionFinish(recyclerView, z);
            recyclerView.postDelayed(new Runnable() { // from class: com.miui.gallery.widget.recyclerview.transition.TransitionHelper$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    TransitionHelper.this.lambda$stopTransitionInternal$2(recyclerView);
                }
            }, DELAY_MILLIS_FOR_LAYOUT);
        }
        Transition transition = this.mTransition;
        if (transition != null) {
            transition.stop();
        }
    }

    public /* synthetic */ void lambda$stopTransitionInternal$2(RecyclerView recyclerView) {
        ITransitionOverlay iTransitionOverlay = this.mTransitionOverlay;
        if (iTransitionOverlay != null) {
            iTransitionOverlay.release();
            this.mTransitionOverlay.detach(recyclerView);
        }
        this.mTransitionListener.onPostTransition(recyclerView);
        this.mAnchor = null;
        this.mScaleBeginFactor = 1.0f;
        this.mIsFastScale = false;
        setState(-1);
        clearTransitingViews();
    }

    public final void saveTransitingViews(RecyclerView recyclerView) {
        this.mTransitingRecycler = new WeakReference<>(recyclerView);
    }

    public final void clearTransitingViews() {
        WeakReference<RecyclerView> weakReference = this.mTransitingRecycler;
        if (weakReference != null) {
            weakReference.clear();
        }
    }

    public static List<ITransitionRender> calculateRenderItems(RecyclerView recyclerView, List<List<ItemWrapper>> list, List<List<ItemWrapper>> list2, List<HeaderTransitItem> list3, List<HeaderTransitItem> list4, int i, long j, long j2, PictureViewMode pictureViewMode, PictureViewMode pictureViewMode2, boolean z) {
        IRenderItemCalculator timeLineRenderItemCalculator;
        if (BaseMiscUtil.isValid(list3) || BaseMiscUtil.isValid(list4) || PictureViewMode.isLargeDevice()) {
            timeLineRenderItemCalculator = new TimeLineRenderItemCalculator();
        } else {
            timeLineRenderItemCalculator = new GridRenderItemCalculator();
        }
        return timeLineRenderItemCalculator.calculateRenderItems(recyclerView, list, list2, list3, list4, i, j, j2, pictureViewMode, pictureViewMode2, z);
    }

    public static ImageTransitionRender buildImageTransitionRender(RecyclerView recyclerView, ImageTransitItem imageTransitItem, RectF rectF, RectF rectF2, int i, int i2, Interpolator interpolator) {
        return new ImageTransitionRender.Builder(recyclerView).setLocalPath(imageTransitItem.getTransitPath()).setPreviewPath(imageTransitItem.getTransitPreviewPath()).setDrawable(imageTransitItem.getTransitDrawable()).setScaleType(imageTransitItem.getTransitScaleType()).setRequestOptions(imageTransitItem.getOptions()).setPreviewOptions(imageTransitItem.getPreviewOptions()).setImageSize(imageTransitItem.getImageSize()).setFromFrame(rectF).setToFrame(rectF2).setFromAlpha(i).setToAlpha(i2).setAlphaInterpolator(interpolator).build();
    }

    public static ItemWrapper findItem(List<List<ItemWrapper>> list, long j) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            for (ItemWrapper itemWrapper : list.get(i)) {
                if (itemWrapper.getTransitId() == j) {
                    return itemWrapper;
                }
            }
        }
        return null;
    }

    public static void wrapTransitItems(List<ITransitItem> list, List<List<ItemWrapper>> list2, final List<HeaderTransitItem> list3) {
        if (list.isEmpty()) {
            return;
        }
        final LinkedList<ITransitItem> linkedList = new LinkedList();
        list.forEach(new Consumer() { // from class: com.miui.gallery.widget.recyclerview.transition.TransitionHelper$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TransitionHelper.$r8$lambda$q91Sc1sBPjUjfELDvfgr_4PuWpY(list3, linkedList, (ITransitItem) obj);
            }
        });
        Collections.sort(linkedList, TransitionHelper$$ExternalSyntheticLambda3.INSTANCE);
        ArrayList arrayList = null;
        int i = -1;
        int i2 = -1;
        for (ITransitItem iTransitItem : linkedList) {
            if (arrayList == null || arrayList.get(0).getTransitFrame().top != iTransitItem.getTransitFrame().top) {
                arrayList = new ArrayList();
                list2.add(arrayList);
                i++;
                i2 = 0;
            }
            arrayList.add(new ItemWrapper(iTransitItem, i, i2));
            i2++;
        }
    }

    public static /* synthetic */ void lambda$wrapTransitItems$3(List list, List list2, ITransitItem iTransitItem) {
        if (iTransitItem instanceof HeaderTransitItem) {
            list.add((HeaderTransitItem) iTransitItem);
        } else if (!(iTransitItem instanceof ImageTransitItem)) {
        } else {
            list2.add(iTransitItem);
        }
    }

    public static /* synthetic */ int lambda$wrapTransitItems$4(ITransitItem iTransitItem, ITransitItem iTransitItem2) {
        float f;
        float f2;
        RectF transitFrame = iTransitItem.getTransitFrame();
        RectF transitFrame2 = iTransitItem2.getTransitFrame();
        if (Float.compare(transitFrame.top, transitFrame2.top) == 0) {
            f = transitFrame.left;
            f2 = transitFrame2.left;
        } else {
            f = transitFrame.top;
            f2 = transitFrame2.top;
        }
        return (int) (f - f2);
    }

    public static void assertLayoutManagerNonNull(RecyclerView.LayoutManager layoutManager) {
        Objects.requireNonNull(layoutManager, "LayoutManager shouldn't be null");
    }

    /* loaded from: classes3.dex */
    public static class Transition implements Runnable {
        public final WeakReference<View> mDriveView;
        public final int mDuration;
        public final Scroller mScroller;
        public final OnUpdateListener mUpdateListener;

        /* loaded from: classes3.dex */
        public interface OnUpdateListener {
            void onUpdate(float f, float f2);
        }

        public Transition(View view, OnUpdateListener onUpdateListener, Interpolator interpolator, int i) {
            this.mDriveView = new WeakReference<>(view);
            this.mUpdateListener = onUpdateListener;
            this.mScroller = new Scroller(view.getContext(), interpolator);
            this.mDuration = i;
        }

        public final View getDriveView() {
            return this.mDriveView.get();
        }

        public void start(float f, float f2) {
            stop();
            this.mScroller.startScroll((int) (f * 1.0E8f), 0, (int) ((f2 - f) * 1.0E8f), 0, this.mDuration);
            drive();
        }

        public void stop() {
            this.mScroller.forceFinished(true);
            View driveView = getDriveView();
            if (driveView != null) {
                driveView.removeCallbacks(this);
            }
        }

        public final void drive() {
            View driveView = getDriveView();
            if (driveView != null) {
                driveView.postOnAnimation(this);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mScroller.computeScrollOffset()) {
                float currX = (this.mScroller.getCurrX() * 1.0f) / 1.0E8f;
                OnUpdateListener onUpdateListener = this.mUpdateListener;
                if (onUpdateListener != null) {
                    onUpdateListener.onUpdate(MathUtils.clamp(currX, 0.0f, 1.0f), (this.mScroller.getFinalX() * 1.0f) / 1.0E8f);
                }
                drive();
            }
        }
    }

    public static View findClosestAnchorUnder(RecyclerView recyclerView, float f, float f2) {
        View findViewUnder = findViewUnder(recyclerView, f, f2);
        if (findViewUnder instanceof TransitionalAnchor) {
            return findViewUnder;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        assertLayoutManagerNonNull(layoutManager);
        int childCount = layoutManager.getChildCount();
        RectF rectF = new RectF();
        double d = 2.147483647E9d;
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = layoutManager.getChildAt(i2);
            if (childAt instanceof TransitionalAnchor) {
                getViewFrame(childAt, rectF);
                double distance = distance(rectF, f, f2);
                if (distance > SearchStatUtils.POW && distance < d) {
                    i = i2;
                    d = distance;
                }
            }
        }
        return layoutManager.getChildAt(i);
    }

    public static View findViewUnder(RecyclerView recyclerView, float f, float f2) {
        return recyclerView.findChildViewUnder(f, f2);
    }

    public static double distance(RectF rectF, float f, float f2) {
        if (rectF == null) {
            return -1.0d;
        }
        return Math.hypot(f - rectF.centerX(), f2 - rectF.centerY());
    }

    public static void getViewFrame(View view, RectF rectF) {
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        rectF.set(view.getLeft() + translationX, view.getTop() + translationY, view.getRight() + translationX, view.getBottom() + translationY);
    }
}
