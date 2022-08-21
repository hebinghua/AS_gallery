package com.miui.gallery.editor.photo.core.imports.remover2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.RenderRecord;
import com.miui.gallery.editor.photo.app.remover2.Inpaint2;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemover2Fragment;
import com.miui.gallery.editor.photo.core.common.model.Remover2Data;
import com.miui.gallery.editor.photo.core.imports.remover2.Inpaint2Manager;
import com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView;
import com.miui.gallery.editor.photo.widgets.UndoRedoView;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.listener.SingleClickListener;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/* loaded from: classes2.dex */
public class Remover2RenderFragment extends AbstractRemover2Fragment implements RenderRecord {
    public Remover2GestureView.RemoverCallback mCallback;
    public boolean mCanBackToOrigin;
    public ListIterator<Remover2PaintData> mCurrIterator;
    public int mCurrentType;
    public Handler mHandler;
    public Inpaint2Manager.Inpaint2Callback mInpaintProCallback;
    public Inpaint2Manager mManager;
    public float mPaintSize;
    public LottieAnimationView mProgressBar;
    public ProgressRunnable mProgressRunnable;
    public int mRecordCurr;
    public int mRecordHead;
    public int mRecordListTail;
    public int mRecordTail;
    public Remover2GestureView mRemover2GestureView;
    public Remover2PaintData mRemoverPaintData;
    public List<Remover2PaintData> mRemoverPaintDataList;
    public RenderCallback mRenderCallback;
    public List<String> mSampleList;
    public SingleClickListener mTitleClickListener;
    public UndoRedoView mUndoRedoView;

    /* loaded from: classes2.dex */
    public interface RenderCallback {
        void clearPeopleEnable(boolean z);

        void initFinished();

        void inpaintFinished();

        void segmentFinished();
    }

    public static /* synthetic */ boolean $r8$lambda$FThhwlWrQy1wEdHhSzucxV6vFSU(Remover2RenderFragment remover2RenderFragment, View view, MotionEvent motionEvent) {
        return remover2RenderFragment.lambda$onViewCreated$0(view, motionEvent);
    }

    public Remover2RenderFragment() {
        ArrayList arrayList = new ArrayList();
        this.mRemoverPaintDataList = arrayList;
        this.mCurrIterator = arrayList.listIterator();
        this.mRecordHead = 0;
        this.mRecordCurr = 0;
        this.mHandler = new Handler(new CustomCallback());
        this.mProgressRunnable = new ProgressRunnable();
        this.mCanBackToOrigin = true;
        this.mSampleList = new ArrayList();
        this.mCallback = new Remover2GestureView.RemoverCallback() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2RenderFragment.1
            {
                Remover2RenderFragment.this = this;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.RemoverCallback
            public void tuneLine(float[] fArr, float[] fArr2) {
                if (Remover2RenderFragment.this.mManager != null) {
                    Remover2RenderFragment.this.mManager.tuneLine(fArr, fArr2);
                }
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.RemoverCallback
            public void inpaint(Bitmap bitmap, Remover2NNFData remover2NNFData, int i, int[] iArr) {
                if (Remover2RenderFragment.this.mManager != null) {
                    Remover2RenderFragment.this.mManager.inpaint(bitmap, remover2NNFData, i, iArr);
                }
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.RemoverCallback
            public void removeDone(Remover2PaintData remover2PaintData) {
                Remover2RenderFragment.this.mHandler.removeCallbacks(Remover2RenderFragment.this.mProgressRunnable);
                Remover2RenderFragment.this.mProgressBar.setVisibility(8);
                if (remover2PaintData != null) {
                    Remover2RenderFragment.this.mRemoverPaintData = remover2PaintData;
                    Remover2RenderFragment.this.recordCurrent();
                    Remover2RenderFragment.this.getTitleView().setVisibility(8);
                    Remover2RenderFragment.this.mUndoRedoView.onMenuUpdated(Remover2RenderFragment.this.mRecordCurr != Remover2RenderFragment.this.mRecordHead, false);
                }
                Remover2RenderFragment.this.mRemover2GestureView.setIsProcessing(false);
                Remover2RenderFragment.this.mUndoRedoView.onMenuEnabled(true);
                Remover2RenderFragment.this.showCompareButton();
                if (Remover2RenderFragment.this.mCurrentType == 0) {
                    if (Remover2RenderFragment.this.mSampleList.contains("remover2_free")) {
                        return;
                    }
                    Remover2RenderFragment.this.mSampleList.add("remover2_free");
                } else if (Remover2RenderFragment.this.mCurrentType == 1) {
                    if (Remover2RenderFragment.this.mSampleList.contains("remover2_line")) {
                        return;
                    }
                    Remover2RenderFragment.this.mSampleList.add("remover2_line");
                } else if (Remover2RenderFragment.this.mCurrentType != 2 || Remover2RenderFragment.this.mSampleList.contains("remover2_people")) {
                } else {
                    Remover2RenderFragment.this.mSampleList.add("remover2_people");
                }
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.RemoverCallback
            public void clearPeopleEnable(boolean z) {
                if (Remover2RenderFragment.this.mRenderCallback != null) {
                    Remover2RenderFragment.this.mRenderCallback.clearPeopleEnable(z);
                }
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.RemoverCallback
            public void isShowCompareButton(boolean z) {
                if (z) {
                    Remover2RenderFragment.this.showCompareButton();
                } else {
                    Remover2RenderFragment.this.hideCompareButton();
                }
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.RemoverCallback
            public void showToast(int i) {
                Remover2RenderFragment.this.getHostAbility().showInnerToast(Remover2RenderFragment.this.getString(i));
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.RemoverCallback
            public void isShowTopView(boolean z) {
                if (z) {
                    Remover2RenderFragment.this.mUndoRedoView.setVisibility(0);
                } else {
                    Remover2RenderFragment.this.mUndoRedoView.setVisibility(8);
                }
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.RemoverCallback
            public void deletePeopleClick() {
                Remover2RenderFragment.this.getHostAbility().sample("remover2_delete_people");
            }
        };
        this.mTitleClickListener = new SingleClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2RenderFragment.2
            {
                Remover2RenderFragment.this = this;
            }

            @Override // com.miui.gallery.listener.SingleClickListener
            public void onSingleClick(View view) {
                Intent intent = new Intent("com.miui.gallery.action.VIEW_WEB");
                intent.putExtra(MapBundleKey.MapObjKey.OBJ_URL, String.format(Locale.US, "https://i.mi.com/static2?filename=MicloudWebBill/event/gallery/MagicAllh5-v2.html&mode=dark#%1$s", Locale.getDefault()));
                intent.putExtra("from_photo_editor", true);
                Remover2RenderFragment.this.startActivity(intent);
                SamplingStatHelper.recordCountEvent("photo_editor", "remove_tips_click");
            }
        };
        this.mInpaintProCallback = new Inpaint2Manager.Inpaint2Callback() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2RenderFragment.3
            {
                Remover2RenderFragment.this = this;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Inpaint2Manager.Inpaint2Callback
            public void initFinished() {
                if (Remover2RenderFragment.this.mRenderCallback != null) {
                    Remover2RenderFragment.this.mRenderCallback.initFinished();
                }
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Inpaint2Manager.Inpaint2Callback
            public void isInpaintProcessing(boolean z) {
                if (z) {
                    Remover2RenderFragment.this.mHandler.postDelayed(Remover2RenderFragment.this.mProgressRunnable, 600L);
                } else {
                    Remover2RenderFragment.this.mHandler.removeCallbacks(Remover2RenderFragment.this.mProgressRunnable);
                    Remover2RenderFragment.this.mProgressBar.setVisibility(8);
                }
                Remover2RenderFragment.this.mRemover2GestureView.setIsProcessing(z);
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Inpaint2Manager.Inpaint2Callback
            public void inpaintFinished(int i, int i2) {
                Remover2RenderFragment.this.mRemover2GestureView.inpaintFinished(i, i2);
                if (Remover2RenderFragment.this.mRenderCallback != null) {
                    Remover2RenderFragment.this.mRenderCallback.inpaintFinished();
                }
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Inpaint2Manager.Inpaint2Callback
            public void tuneLineFinished(int i, Bitmap bitmap) {
                Remover2RenderFragment.this.mRemover2GestureView.tuneLineFinished(i, bitmap);
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Inpaint2Manager.Inpaint2Callback
            public void segmentFinished() {
                if (Remover2RenderFragment.this.mRenderCallback != null) {
                    Remover2RenderFragment.this.mRenderCallback.segmentFinished();
                }
            }
        };
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new Remover2RenderView(layoutInflater.getContext());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        EditorOrientationHelper.copyLayoutParams(new Remover2RenderView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mRemover2GestureView = (Remover2GestureView) view.findViewById(R.id.remover_gesture_view);
        this.mProgressBar = (LottieAnimationView) view.findViewById(R.id.progress);
        this.mRemover2GestureView.setBitmap(getBitmap());
        this.mRemover2GestureView.setRemoverCallback(this.mCallback);
        this.mRemover2GestureView.setStrokeSize((int) this.mPaintSize);
        this.mCanBackToOrigin = true;
        Inpaint2Manager inpaint2Manager = new Inpaint2Manager();
        this.mManager = inpaint2Manager;
        inpaint2Manager.setCallback(this.mInpaintProCallback);
        this.mManager.init(this.mRemover2GestureView.getPreview());
        setCompareTouchListener(new View.OnTouchListener() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2RenderFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                return Remover2RenderFragment.$r8$lambda$FThhwlWrQy1wEdHhSzucxV6vFSU(Remover2RenderFragment.this, view2, motionEvent);
            }
        });
        this.mUndoRedoView = (UndoRedoView) view.findViewById(R.id.undo_redo);
        TextView titleView = getTitleView();
        titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.editor_remover2_help, 0);
        titleView.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.editor_menu_remove_text_img_padding_end));
        titleView.setOnClickListener(this.mTitleClickListener);
        this.mUndoRedoView.setRenderRecordListener(this);
    }

    public /* synthetic */ boolean lambda$onViewCreated$0(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mRemover2GestureView.drawOrigin(true);
            HashMap hashMap = new HashMap();
            hashMap.put("page", this.mEffect.name());
            SamplingStatHelper.recordCountEvent("photo_editor", "compare_button_touch", hashMap);
        } else if (1 == motionEvent.getAction() || 3 == motionEvent.getAction()) {
            this.mRemover2GestureView.drawOrigin(false);
        }
        return true;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        this.mCallback = null;
        this.mRenderCallback = null;
        Inpaint2Manager inpaint2Manager = this.mManager;
        if (inpaint2Manager != null) {
            inpaint2Manager.setCallback(null);
        }
        super.onDestroyView();
        hideCompareButton();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        Inpaint2.getInstance().release();
    }

    public void onSegment() {
        Inpaint2Manager inpaint2Manager = this.mManager;
        if (inpaint2Manager != null) {
            inpaint2Manager.segment();
        }
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        List<Remover2PaintData> list = this.mRemoverPaintDataList;
        return list == null || list.isEmpty();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        return this.mSampleList;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        ArrayList arrayList = new ArrayList();
        List<Remover2PaintData> list = this.mRemoverPaintDataList;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i <= this.mRecordListTail; i++) {
                arrayList.add(this.mRemoverPaintDataList.get(i));
            }
        }
        return new Remover2RenderData(arrayList);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
        this.mRemover2GestureView.onClear();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractRemover2Fragment
    public void setPaintSize(float f) {
        this.mPaintSize = f;
        Remover2GestureView remover2GestureView = this.mRemover2GestureView;
        if (remover2GestureView != null) {
            remover2GestureView.setStrokeSize((int) f);
        }
        LottieAnimationView lottieAnimationView = this.mProgressBar;
        if (lottieAnimationView != null) {
            lottieAnimationView.setVisibility(8);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractRemover2Fragment
    public void setRemover2Data(Remover2Data remover2Data) {
        if (remover2Data == null) {
            return;
        }
        int i = remover2Data.mType;
        this.mCurrentType = i;
        if (i == 0) {
            this.mRemover2GestureView.setElementType(0);
        } else if (i == 1) {
            this.mRemover2GestureView.setElementType(1);
        } else if (i != 2) {
        } else {
            this.mRemover2GestureView.setElementType(2);
            Inpaint2Manager inpaint2Manager = this.mManager;
            if (inpaint2Manager == null) {
                return;
            }
            this.mRemover2GestureView.showPeopleMask(inpaint2Manager.getPeopleMask(), this.mManager.getBoxes());
        }
    }

    public void recordCurrent() {
        int i = this.mRecordCurr;
        if (i + 1 == 10) {
            this.mCanBackToOrigin = false;
        }
        int i2 = (i + 1) % 10;
        this.mRecordCurr = i2;
        this.mRecordTail = i2;
        DefaultLogger.d("Remover2RenderFragment", "recordCurrent  mRecordCurr : %d", Integer.valueOf(i2));
        int i3 = this.mRecordCurr;
        if (i3 == this.mRecordHead) {
            this.mRecordHead = (i3 + 1) % 10;
        }
        if (this.mCurrIterator.hasNext()) {
            this.mCurrIterator.next();
            this.mCurrIterator.set(this.mRemoverPaintData);
            this.mRecordListTail = this.mCurrIterator.previousIndex();
        } else {
            this.mCurrIterator.add(this.mRemoverPaintData);
            this.mRecordListTail = this.mCurrIterator.previousIndex();
        }
        this.mRemover2GestureView.writeRecordType();
        this.mRemover2GestureView.writeRecordFile();
    }

    @Override // com.miui.gallery.editor.photo.app.RenderRecord
    public void previousRecord() {
        if (isProcessing()) {
            return;
        }
        boolean z = true;
        int i = (this.mRecordCurr - 1) % 10;
        this.mRecordCurr = i;
        if (i < 0) {
            this.mRecordCurr = i + 10;
        }
        DefaultLogger.d("Remover2RenderFragment", "previousRecord  mRecordCurr : %d", Integer.valueOf(this.mRecordCurr));
        if (this.mCurrIterator.hasPrevious()) {
            this.mCurrIterator.previous();
            this.mRecordListTail = this.mCurrIterator.previousIndex();
        }
        this.mRemover2GestureView.renderPreviousBuffer();
        UndoRedoView undoRedoView = this.mUndoRedoView;
        int i2 = this.mRecordCurr;
        boolean z2 = i2 != this.mRecordHead;
        if (i2 == this.mRecordTail) {
            z = false;
        }
        undoRedoView.onMenuUpdated(z2, z);
        if (this.mRecordCurr != 0 || !this.mCanBackToOrigin) {
            return;
        }
        hideCompareButton();
    }

    @Override // com.miui.gallery.editor.photo.app.RenderRecord
    public void nextRecord() {
        if (isProcessing()) {
            return;
        }
        boolean z = true;
        this.mRecordCurr = (this.mRecordCurr + 1) % 10;
        if (this.mCurrIterator.hasNext()) {
            this.mCurrIterator.next();
            this.mRecordListTail = this.mCurrIterator.previousIndex();
        }
        this.mRemover2GestureView.renderNextBuffer();
        UndoRedoView undoRedoView = this.mUndoRedoView;
        int i = this.mRecordCurr;
        boolean z2 = i != this.mRecordHead;
        if (i == this.mRecordTail) {
            z = false;
        }
        undoRedoView.onMenuUpdated(z2, z);
        showCompareButton();
    }

    /* loaded from: classes2.dex */
    public static class CustomCallback implements Handler.Callback {
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            return true;
        }

        public CustomCallback() {
        }
    }

    /* loaded from: classes2.dex */
    public class ProgressRunnable implements Runnable {
        public ProgressRunnable() {
            Remover2RenderFragment.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            Remover2RenderFragment.this.mProgressBar.setVisibility(0);
        }
    }

    public boolean isProcessing() {
        return this.mRemover2GestureView.isProcessing();
    }

    public void clearAllPeople() {
        if (isProcessing()) {
            return;
        }
        this.mRemover2GestureView.clearAllPeople();
    }

    public void setRenderCallback(RenderCallback renderCallback) {
        this.mRenderCallback = renderCallback;
    }
}
