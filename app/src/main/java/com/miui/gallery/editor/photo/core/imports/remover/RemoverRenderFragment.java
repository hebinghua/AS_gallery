package com.miui.gallery.editor.photo.core.imports.remover;

import android.content.Intent;
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
import com.miui.gallery.editor.photo.app.remover.Inpaint;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemoverFragment;
import com.miui.gallery.editor.photo.core.common.model.RemoverData;
import com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView;
import com.miui.gallery.editor.photo.widgets.UndoRedoView;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.listener.SingleClickListener;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/* loaded from: classes2.dex */
public class RemoverRenderFragment extends AbstractRemoverFragment implements RenderRecord {
    public RemoverGestureView.RemoverCallback mCallback;
    public boolean mCanBackToOrigin;
    public ListIterator<RemoverPaintData> mCurrIterator;
    public Handler mHandler;
    public boolean mIsNightMode;
    public float mPaintSize;
    public LottieAnimationView mProgressBar;
    public ProgressRunnable mProgressRunnable;
    public int mRecordCurr;
    public int mRecordHead;
    public int mRecordListTail;
    public int mRecordTail;
    public RemoverGestureView mRemoverGestureView;
    public RemoverPaintData mRemoverPaintData;
    public List<RemoverPaintData> mRemoverPaintDataList;
    public TextView mTitleView;
    public UndoRedoView mUndoRedoView;

    /* renamed from: $r8$lambda$h32-v8d9HJGZ0uBPqu9RvjsEc3w */
    public static /* synthetic */ boolean m853$r8$lambda$h32v8d9HJGZ0uBPqu9RvjsEc3w(RemoverRenderFragment removerRenderFragment, View view, MotionEvent motionEvent) {
        return removerRenderFragment.lambda$onViewCreated$0(view, motionEvent);
    }

    public RemoverRenderFragment() {
        ArrayList arrayList = new ArrayList();
        this.mRemoverPaintDataList = arrayList;
        this.mCurrIterator = arrayList.listIterator();
        this.mRecordHead = 0;
        this.mRecordCurr = 0;
        this.mHandler = new Handler(new CustomCallback());
        this.mProgressRunnable = new ProgressRunnable();
        this.mCanBackToOrigin = true;
        this.mCallback = new RemoverGestureView.RemoverCallback() { // from class: com.miui.gallery.editor.photo.core.imports.remover.RemoverRenderFragment.2
            {
                RemoverRenderFragment.this = this;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.RemoverCallback
            public void onScribble() {
                RemoverRenderFragment.this.mUndoRedoView.setVisibility(8);
                RemoverRenderFragment.this.hideCompareButton();
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.RemoverCallback
            public void onRemoveStart() {
                RemoverRenderFragment.this.mUndoRedoView.setVisibility(0);
                RemoverRenderFragment.this.showCompareButton();
                RemoverRenderFragment.this.mRemoverGestureView.setIsProcessing(true);
                RemoverRenderFragment.this.mUndoRedoView.onMenuEnabled(false);
                RemoverRenderFragment.this.mHandler.postDelayed(RemoverRenderFragment.this.mProgressRunnable, 500L);
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.RemoverCallback
            public void removeDone(RemoverPaintData removerPaintData) {
                RemoverRenderFragment.this.mHandler.removeCallbacks(RemoverRenderFragment.this.mProgressRunnable);
                RemoverRenderFragment.this.mProgressBar.setVisibility(8);
                if (removerPaintData != null) {
                    RemoverRenderFragment.this.mRemoverPaintData = removerPaintData;
                    RemoverRenderFragment.this.recordCurrent();
                    RemoverRenderFragment.this.getTitleView().setVisibility(8);
                    RemoverRenderFragment.this.mUndoRedoView.onMenuUpdated(RemoverRenderFragment.this.mRecordCurr != RemoverRenderFragment.this.mRecordHead, false);
                }
                RemoverRenderFragment.this.mRemoverGestureView.setIsProcessing(false);
                RemoverRenderFragment.this.mUndoRedoView.onMenuEnabled(true);
                RemoverRenderFragment.this.showCompareButton();
            }
        };
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Inpaint.initialize();
        this.mIsNightMode = MiscUtil.isNightMode(getActivity());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        EditorOrientationHelper.copyLayoutParams(new RemoverRenderView(getContext()), getView(), true);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new RemoverRenderView(layoutInflater.getContext());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mRemoverGestureView = (RemoverGestureView) view.findViewById(R.id.remover_gesture_view);
        this.mProgressBar = (LottieAnimationView) view.findViewById(R.id.progress);
        this.mRemoverGestureView.setBitmap(getBitmap());
        this.mRemoverGestureView.setRemoverCallback(this.mCallback);
        this.mRemoverGestureView.setStrokeSize((int) this.mPaintSize);
        setCompareTouchListener(new View.OnTouchListener() { // from class: com.miui.gallery.editor.photo.core.imports.remover.RemoverRenderFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                return RemoverRenderFragment.m853$r8$lambda$h32v8d9HJGZ0uBPqu9RvjsEc3w(RemoverRenderFragment.this, view2, motionEvent);
            }
        });
        this.mCanBackToOrigin = true;
        this.mUndoRedoView = (UndoRedoView) view.findViewById(R.id.undo_redo);
        TextView titleView = getTitleView();
        this.mTitleView = titleView;
        titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.editor_ic_help, 0);
        this.mTitleView.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.editor_menu_remove_text_img_padding_end));
        this.mTitleView.setOnClickListener(new SingleClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.remover.RemoverRenderFragment.1
            {
                RemoverRenderFragment.this = this;
            }

            @Override // com.miui.gallery.listener.SingleClickListener
            public void onSingleClick(View view2) {
                Intent intent = new Intent("com.miui.gallery.action.VIEW_WEB");
                Locale locale = Locale.US;
                Object[] objArr = new Object[2];
                objArr[0] = RemoverRenderFragment.this.mIsNightMode ? "&mode=dark" : "";
                objArr[1] = Locale.getDefault();
                intent.putExtra(MapBundleKey.MapObjKey.OBJ_URL, String.format(locale, "https://i.mi.com/static2?filename=MicloudWebBill/event/gallery/MagicAllh5-v1.html%1$s#%2$s", objArr));
                intent.putExtra("from_photo_editor", true);
                RemoverRenderFragment.this.startActivity(intent);
                SamplingStatHelper.recordCountEvent("photo_editor", "remove_tips_click");
            }
        });
        this.mUndoRedoView.setRenderRecordListener(this);
    }

    public /* synthetic */ boolean lambda$onViewCreated$0(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mRemoverGestureView.drawOrigin(true);
            HashMap hashMap = new HashMap();
            hashMap.put("page", this.mEffect.name());
            SamplingStatHelper.recordCountEvent("photo_editor", "compare_button_touch", hashMap);
        } else if (1 == motionEvent.getAction() || 3 == motionEvent.getAction()) {
            this.mRemoverGestureView.drawOrigin(false);
        }
        return true;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        hideCompareButton();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        List<RemoverPaintData> list = this.mRemoverPaintDataList;
        return list == null || list.isEmpty();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.valueOf(this.mRecordListTail + 1));
        return arrayList;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        ArrayList arrayList = new ArrayList();
        List<RemoverPaintData> list = this.mRemoverPaintDataList;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i <= this.mRecordListTail; i++) {
                arrayList.add(this.mRemoverPaintDataList.get(i));
            }
        }
        return new RemoverRenderData(arrayList);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
        this.mRemoverGestureView.onClear();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractRemoverFragment
    public void setPaintSize(float f) {
        this.mPaintSize = f;
        RemoverGestureView removerGestureView = this.mRemoverGestureView;
        if (removerGestureView != null) {
            removerGestureView.setStrokeSize((int) f);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractRemoverFragment
    public void setRemoverData(RemoverData removerData) {
        if (removerData == null) {
            return;
        }
        int i = removerData.mType;
        if (i == 0) {
            this.mRemoverGestureView.setElementType(RemoverGestureView.ElementType.FREE);
        } else if (i != 1) {
        } else {
            this.mRemoverGestureView.setElementType(RemoverGestureView.ElementType.LINE);
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
        DefaultLogger.d("RemoverRenderFragment", "recordCurrent  mRecordCurr : %d", Integer.valueOf(i2));
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
        this.mRemoverGestureView.writeRecordFile();
    }

    @Override // com.miui.gallery.editor.photo.app.RenderRecord
    public void previousRecord() {
        boolean z = true;
        int i = (this.mRecordCurr - 1) % 10;
        this.mRecordCurr = i;
        if (i < 0) {
            this.mRecordCurr = i + 10;
        }
        DefaultLogger.d("RemoverRenderFragment", "previousRecord  mRecordCurr : %d", Integer.valueOf(this.mRecordCurr));
        if (this.mCurrIterator.hasPrevious()) {
            this.mCurrIterator.previous();
            this.mRecordListTail = this.mCurrIterator.previousIndex();
        }
        this.mRemoverGestureView.renderPreviousBuffer();
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
        boolean z = true;
        this.mRecordCurr = (this.mRecordCurr + 1) % 10;
        if (this.mCurrIterator.hasNext()) {
            this.mCurrIterator.next();
            this.mRecordListTail = this.mCurrIterator.previousIndex();
        }
        this.mRemoverGestureView.renderNextBuffer();
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
            RemoverRenderFragment.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            RemoverRenderFragment.this.mProgressBar.setVisibility(0);
        }
    }
}
