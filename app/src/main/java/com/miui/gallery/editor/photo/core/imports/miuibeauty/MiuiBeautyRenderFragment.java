package com.miui.gallery.editor.photo.core.imports.miuibeauty;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.miui.filtersdk.beauty.BeautyParameterType;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.RenderRecord;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderFragment;
import com.miui.gallery.editor.photo.widgets.BeautyImageView;
import com.miui.gallery.editor.photo.widgets.StrokeBoardView;
import com.miui.gallery.stat.SamplingStatHelper;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Deprecated
/* loaded from: classes2.dex */
public class MiuiBeautyRenderFragment extends AbstractEffectFragment implements RenderRecord, BeautyImageView.InitRenderCallback {
    public List<Map<BeautyParameterType, Float>> mBeautyParameterList;
    public Map<BeautyParameterType, Float> mBeautyParameters;
    public ProgressBar mBeautyProgressBar;
    public Button mCompareBtn;
    public boolean mCompareOrigin;
    public ListIterator<Map<BeautyParameterType, Float>> mCurrIterator;
    public OnBeautyProcessListener mOnBeautyProcessListener;
    public ImageView mOriginalImageView;
    public Bitmap mPreProcessBitmap;
    public FrameLayout mPreviewContainer;
    public int mRecordHead;
    public int mRecordTail;
    public BeautyImageView mRenderView;
    public boolean mShowProgressDelay = true;
    public StrokeBoardView mStrokeBoardView;
    public Bitmap mToBeCompared;

    /* loaded from: classes2.dex */
    public interface OnBeautyProcessListener {
        void onBeautyProcessEnd();

        void onBeautyProcessStart();
    }

    public static /* synthetic */ boolean $r8$lambda$euqwOgrOZXP3PBcXtdRj6efmzfM(MiuiBeautyRenderFragment miuiBeautyRenderFragment, View view, MotionEvent motionEvent) {
        return miuiBeautyRenderFragment.lambda$enableComparison$0(view, motionEvent);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BeautyImageView.InitRenderCallback
    public void onRenderFinish() {
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        return null;
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void remove(Metadata metadata) {
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LinkedList linkedList = new LinkedList();
        this.mBeautyParameterList = linkedList;
        this.mCurrIterator = linkedList.listIterator();
        this.mRecordHead = -1;
        this.mRecordTail = -1;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.miui_beauty_fragment, viewGroup, false);
        BeautyImageView beautyImageView = (BeautyImageView) viewGroup2.findViewById(R.id.glsurfaceview_image);
        this.mRenderView = beautyImageView;
        beautyImageView.setInitRenderCallback(this);
        Bitmap preProcessBitmapForPreview = MiuiBeautyEngine.preProcessBitmapForPreview(getBitmap());
        this.mPreProcessBitmap = preProcessBitmapForPreview;
        this.mToBeCompared = preProcessBitmapForPreview.copy(preProcessBitmapForPreview.getConfig(), true);
        this.mRenderView.setPicData(this.mPreProcessBitmap);
        this.mCompareOrigin = true;
        this.mPreviewContainer = (FrameLayout) viewGroup2.findViewById(R.id.preview_container);
        this.mCompareBtn = (Button) viewGroup2.findViewById(R.id.compare_btn);
        this.mBeautyProgressBar = (ProgressBar) viewGroup2.findViewById(R.id.beauty_progress_bar);
        StrokeBoardView strokeBoardView = (StrokeBoardView) viewGroup2.findViewById(R.id.stroke_view);
        this.mStrokeBoardView = strokeBoardView;
        strokeBoardView.setBitmap(this.mPreProcessBitmap);
        return viewGroup2;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void add(Metadata metadata, Object obj) {
        Map<BeautyParameterType, Float> map = obj instanceof Map ? (Map) obj : null;
        this.mBeautyParameters = map;
        this.mRenderView.updateBeautyProcessor(map);
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void render() {
        new RenderTask(this).execute(new Void[0]);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        return this.mBeautyParameterList.isEmpty();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        if (this.mBeautyParameterList.isEmpty()) {
            return new MiuiBeautyRenderData(null);
        }
        MiuiBeautyRenderData miuiBeautyRenderData = new MiuiBeautyRenderData(this.mBeautyParameterList.get(0));
        for (int i = 1; i <= this.mRecordTail; i++) {
            miuiBeautyRenderData.addParams(this.mBeautyParameterList.get(i));
        }
        return miuiBeautyRenderData;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
        clearAllRecords();
    }

    public void setCompareOrigin(boolean z) {
        this.mCompareOrigin = z;
    }

    public void enableComparison(boolean z) {
        if (z) {
            this.mCompareBtn.setVisibility(0);
            this.mCompareBtn.bringToFront();
            this.mCompareBtn.setOnTouchListener(new View.OnTouchListener() { // from class: com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderFragment$$ExternalSyntheticLambda0
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    return MiuiBeautyRenderFragment.$r8$lambda$euqwOgrOZXP3PBcXtdRj6efmzfM(MiuiBeautyRenderFragment.this, view, motionEvent);
                }
            });
            return;
        }
        this.mCompareBtn.setVisibility(8);
    }

    public /* synthetic */ boolean lambda$enableComparison$0(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            if (this.mOriginalImageView == null) {
                ImageView imageView = new ImageView(getActivity());
                this.mOriginalImageView = imageView;
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                this.mOriginalImageView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            }
            this.mOriginalImageView.setImageBitmap(this.mCompareOrigin ? this.mPreProcessBitmap : this.mToBeCompared);
            this.mPreviewContainer.addView(this.mOriginalImageView, 1);
            this.mCompareBtn.setSelected(true);
            HashMap hashMap = new HashMap();
            hashMap.put("page", this.mEffect.name());
            SamplingStatHelper.recordCountEvent("photo_editor", "compare_button_touch", hashMap);
        } else if (1 == motionEvent.getAction() || 3 == motionEvent.getAction()) {
            this.mPreviewContainer.removeView(this.mOriginalImageView);
            this.mCompareBtn.setSelected(false);
        }
        return true;
    }

    public boolean isBeautyParamWorked() {
        return this.mBeautyParameters != null;
    }

    public boolean isComparisonEnable() {
        return this.mCompareBtn.getVisibility() != 8;
    }

    @Override // com.miui.gallery.editor.photo.app.RenderRecord
    public void previousRecord() {
        if (this.mCurrIterator.hasPrevious()) {
            this.mCurrIterator.previous();
            this.mRecordTail = this.mCurrIterator.previousIndex();
        }
        this.mRenderView.renderPreviousBuffer();
    }

    @Override // com.miui.gallery.editor.photo.app.RenderRecord
    public void nextRecord() {
        if (this.mCurrIterator.hasNext()) {
            this.mCurrIterator.next();
            this.mRecordTail = this.mCurrIterator.previousIndex();
        }
        this.mRenderView.renderNextBuffer();
    }

    public void clearAllRecords() {
        this.mRenderView.clearAllRecords();
    }

    public void recordCurrent() {
        if (this.mCurrIterator.hasNext()) {
            this.mCurrIterator.next();
            this.mCurrIterator.set(this.mBeautyParameters);
            this.mRecordTail = this.mCurrIterator.previousIndex();
        } else {
            this.mCurrIterator.add(this.mBeautyParameters);
            this.mRecordTail = this.mCurrIterator.previousIndex();
        }
        this.mRenderView.writeRecordFile();
        this.mRenderView.getBmpFromCurrBuffer(this.mToBeCompared);
    }

    /* loaded from: classes2.dex */
    public static class RenderTask extends AsyncTask<Void, Void, Void> {
        public WeakReference<MiuiBeautyRenderFragment> mFragmentWeakReference;
        public boolean mTaskDone;

        public static /* synthetic */ void $r8$lambda$9tnfJHBWQKA6jF7tYcUdjdu0GP4(RenderTask renderTask) {
            renderTask.lambda$onPreExecute$0();
        }

        public RenderTask(MiuiBeautyRenderFragment miuiBeautyRenderFragment) {
            this.mFragmentWeakReference = new WeakReference<>(miuiBeautyRenderFragment);
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            MiuiBeautyRenderFragment miuiBeautyRenderFragment = this.mFragmentWeakReference.get();
            if (miuiBeautyRenderFragment != null) {
                miuiBeautyRenderFragment.mRenderView.reloadTexture(false);
                miuiBeautyRenderFragment.mRenderView.requestRender();
                return null;
            }
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            MiuiBeautyRenderFragment miuiBeautyRenderFragment = this.mFragmentWeakReference.get();
            if (miuiBeautyRenderFragment == null || miuiBeautyRenderFragment.mBeautyParameters == null) {
                return;
            }
            if (miuiBeautyRenderFragment.mOnBeautyProcessListener != null) {
                miuiBeautyRenderFragment.mOnBeautyProcessListener.onBeautyProcessStart();
            }
            this.mTaskDone = false;
            if (!miuiBeautyRenderFragment.mShowProgressDelay) {
                miuiBeautyRenderFragment.mBeautyProgressBar.setVisibility(0);
            } else {
                miuiBeautyRenderFragment.mRenderView.postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderFragment$RenderTask$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        MiuiBeautyRenderFragment.RenderTask.$r8$lambda$9tnfJHBWQKA6jF7tYcUdjdu0GP4(MiuiBeautyRenderFragment.RenderTask.this);
                    }
                }, 1000L);
            }
        }

        public /* synthetic */ void lambda$onPreExecute$0() {
            MiuiBeautyRenderFragment miuiBeautyRenderFragment;
            if (this.mTaskDone || (miuiBeautyRenderFragment = this.mFragmentWeakReference.get()) == null) {
                return;
            }
            miuiBeautyRenderFragment.mBeautyProgressBar.setVisibility(0);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void r3) {
            super.onPostExecute((RenderTask) r3);
            this.mTaskDone = true;
            MiuiBeautyRenderFragment miuiBeautyRenderFragment = this.mFragmentWeakReference.get();
            if (miuiBeautyRenderFragment != null) {
                miuiBeautyRenderFragment.mBeautyProgressBar.setVisibility(8);
                if (miuiBeautyRenderFragment.mOnBeautyProcessListener == null) {
                    return;
                }
                miuiBeautyRenderFragment.mOnBeautyProcessListener.onBeautyProcessEnd();
            }
        }
    }

    public void setOnBeautyProcessListener(OnBeautyProcessListener onBeautyProcessListener) {
        this.mOnBeautyProcessListener = onBeautyProcessListener;
    }

    public void setShowProgressDelay(boolean z) {
        this.mShowProgressDelay = z;
    }
}
