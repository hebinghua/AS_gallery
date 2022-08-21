package com.miui.gallery.editor.photo.core.imports.adjust2;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2RenderFragment;
import com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2TaskThread;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class Adjust2RenderFragment extends AbstractEffectFragment {
    public static final String TAG = Adjust2RenderFragment.class.getSimpleName();
    public Adjust2TaskThread mAdjustTaskThread;
    public Bitmap mCurrentBitmap;
    public float mDownX;
    public float mDownY;
    public boolean mLongTouchTrigger;
    public float mMinTouchSlop;
    public BitmapGestureView mPreView;
    public LottieAnimationView mProgressBar;
    public List<Adjust2Data> mEffects = new ArrayList();
    public RenderEngine[] mEngines = new RenderEngine[Effect.values().length];
    public Handler mMainHandler = new Handler();
    public ProgressRunnable mProgressRunnable = new ProgressRunnable();
    public View.OnTouchListener mOnTouchListener = new View.OnTouchListener() { // from class: com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2RenderFragment.1
        {
            Adjust2RenderFragment.this = this;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked != 2) {
                        if (actionMasked != 3) {
                            if (actionMasked == 5 && motionEvent.getPointerCount() > 1) {
                                Adjust2RenderFragment.this.mPreView.removeCallbacks(Adjust2RenderFragment.this.mOnLongTouchDownRunnable);
                            }
                        }
                    } else if (Math.hypot(motionEvent.getX() - Adjust2RenderFragment.this.mDownX, motionEvent.getY() - Adjust2RenderFragment.this.mDownY) > Adjust2RenderFragment.this.mMinTouchSlop) {
                        Adjust2RenderFragment.this.mPreView.removeCallbacks(Adjust2RenderFragment.this.mOnLongTouchDownRunnable);
                    }
                }
                Adjust2RenderFragment.this.mPreView.removeCallbacks(Adjust2RenderFragment.this.mOnLongTouchDownRunnable);
                if (Adjust2RenderFragment.this.mLongTouchTrigger) {
                    Adjust2RenderFragment.this.doRender();
                }
            } else {
                Adjust2RenderFragment.this.mLongTouchTrigger = false;
                Adjust2RenderFragment.this.mPreView.postDelayed(Adjust2RenderFragment.this.mOnLongTouchDownRunnable, 100L);
                Adjust2RenderFragment.this.mDownX = motionEvent.getX();
                Adjust2RenderFragment.this.mDownY = motionEvent.getY();
            }
            return false;
        }
    };
    public Runnable mOnLongTouchDownRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2RenderFragment.2
        {
            Adjust2RenderFragment.this = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            Adjust2RenderFragment.this.mLongTouchTrigger = true;
            Adjust2RenderFragment.this.mPreView.setBitmap(Adjust2RenderFragment.this.getBitmap());
        }
    };
    public Adjust2TaskThread.AdjustTaskListener mAdjustTaskListener = new AnonymousClass3();

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isSupportAnimation() {
        return false;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new Adjust2RenderView(layoutInflater.getContext());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new Adjust2RenderView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        BitmapGestureView bitmapGestureView = (BitmapGestureView) view.findViewById(R.id.image_placeholder);
        this.mPreView = bitmapGestureView;
        bitmapGestureView.setBitmap(getBitmap());
        LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(R.id.progress);
        this.mProgressBar = lottieAnimationView;
        lottieAnimationView.setVisibility(8);
        Adjust2TaskThread adjust2TaskThread = new Adjust2TaskThread();
        this.mAdjustTaskThread = adjust2TaskThread;
        adjust2TaskThread.setAdjustTaskListener(this.mAdjustTaskListener);
        ((TextView) view.findViewById(R.id.effect_title)).setTypeface(Typeface.create("mipro-medium", 0));
        this.mPreView.setOnTouchListener(this.mOnTouchListener);
        this.mMinTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mPreView.setStrokeEnable(false);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void add(Metadata metadata, Object obj) {
        if (metadata instanceof Adjust2Data) {
            this.mEffects.add((Adjust2Data) metadata);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void remove(Metadata metadata) {
        int indexOf = this.mEffects.indexOf(metadata);
        if (indexOf >= 0) {
            this.mEffects.remove(indexOf);
        }
    }

    public final void renderBitmap(Bitmap bitmap, Adjust2RenderData adjust2RenderData) {
        this.mAdjustTaskThread.sendFilterTaskMsg(bitmap, adjust2RenderData);
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

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void render() {
        doRender();
    }

    public final void doRender() {
        Adjust2RenderData adjust2RenderData = new Adjust2RenderData(this.mEffects);
        adjust2RenderData.mType = Effect.ADJUST2;
        if (this.mEffects.isEmpty()) {
            this.mPreView.setBitmap(getBitmap());
        } else {
            renderBitmap(getBitmap(), adjust2RenderData);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        return this.mEffects.size() == 0;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        List<Adjust2Data> list = this.mEffects;
        if (list != null && !list.isEmpty()) {
            for (Adjust2Data adjust2Data : this.mEffects) {
                String str = adjust2Data.getEffectName().split(";")[0];
                arrayList.add(str);
                hashMap.put("effect", str);
                hashMap.put("result", Float.toString((int) adjust2Data.progress));
                getHostAbility().sample("adjust_", hashMap);
            }
        }
        return arrayList;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        RenderEngine[] renderEngineArr;
        super.onDestroy();
        this.mAdjustTaskThread.quitSafely();
        this.mAdjustTaskThread.setAdjustTaskListener(null);
        Bitmap bitmap = this.mCurrentBitmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.mCurrentBitmap.recycle();
            this.mCurrentBitmap = null;
        }
        for (RenderEngine renderEngine : this.mEngines) {
            if (renderEngine != null) {
                renderEngine.release();
            }
        }
        this.mPreView = null;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        return new Adjust2RenderData(this.mEffects);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
        this.mEffects.clear();
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2RenderFragment$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Adjust2TaskThread.AdjustTaskListener {
        public static /* synthetic */ void $r8$lambda$lzeKXt_EIwfvooAMDZnTj0_fMkQ(AnonymousClass3 anonymousClass3) {
            anonymousClass3.lambda$handleMessage$0();
        }

        public AnonymousClass3() {
            Adjust2RenderFragment.this = r1;
        }

        @Override // com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2TaskThread.AdjustTaskListener
        public void handleMessage(Message message) {
            Adjust2TaskThread.AdjustTaskData adjustTaskData = (Adjust2TaskThread.AdjustTaskData) message.obj;
            RenderEngine findEngine = RenderEngine.findEngine(Adjust2RenderFragment.this.getActivity(), adjustTaskData.renderData, Adjust2RenderFragment.this.mEngines);
            Adjust2RenderFragment.this.mCurrentBitmap = findEngine.render(adjustTaskData.currentBitmap, new Adjust2RenderData(adjustTaskData.renderData.mEffects), false);
            Adjust2RenderFragment.this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2RenderFragment$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Adjust2RenderFragment.AnonymousClass3.$r8$lambda$lzeKXt_EIwfvooAMDZnTj0_fMkQ(Adjust2RenderFragment.AnonymousClass3.this);
                }
            });
        }

        public /* synthetic */ void lambda$handleMessage$0() {
            if (Adjust2RenderFragment.this.mPreView != null) {
                Adjust2RenderFragment.this.mPreView.setBitmap(Adjust2RenderFragment.this.mCurrentBitmap);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class ProgressRunnable implements Runnable {
        public ProgressRunnable() {
            Adjust2RenderFragment.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Adjust2RenderFragment.this.getActivity() == null || !Adjust2RenderFragment.this.getActivity().isInMultiWindowMode()) {
                Adjust2RenderFragment.this.mProgressBar.setVisibility(0);
            }
        }
    }
}
