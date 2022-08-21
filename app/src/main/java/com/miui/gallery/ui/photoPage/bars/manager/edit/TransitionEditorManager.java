package com.miui.gallery.ui.photoPage.bars.manager.edit;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public abstract class TransitionEditorManager extends BaseEditorManager {
    public boolean mEverStartedEditor;
    public Runnable mOnLoadTimeOut;
    public long mStartTransition;

    public abstract boolean handleEditorResult(Intent intent);

    public void onCanceled() {
    }

    public TransitionEditorManager(IDataProvider iDataProvider, PhotoPageFragment photoPageFragment) {
        super(iDataProvider, photoPageFragment);
        this.mOnLoadTimeOut = new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager.1
            @Override // java.lang.Runnable
            public void run() {
                DefaultLogger.w("TransitionEditorManager", "editor return to photo, image loading time out.");
                TransitionEditorManager.this.finishTransition();
            }
        };
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager
    public void onStartEditor() {
        super.onStartEditor();
        this.mEverStartedEditor = true;
        BaseDataItem dataItem = this.mDataProvider.getFieldData().mCurrent.getDataItem();
        if (dataItem == null || (!dataItem.isVideo() && this.mFragment.isCurrentImageOverDisplayArea())) {
            this.mFragment.setTopBarContentVisibility(false);
            this.mFragment.hideBars(true);
        }
        this.mFragment.onActivityTransition();
    }

    public void onActivityReenter(Intent intent) {
        DefaultLogger.d("TransitionEditorManager", "Transition onActivityReenter %s", Boolean.valueOf(this.mEverStartedEditor));
        this.mTargetFilePath = null;
        this.mTargetId = 0L;
        boolean handleEditorResult = handleEditorResult(intent);
        this.mResultHandled = handleEditorResult;
        if (Build.VERSION.SDK_INT <= 23 && !this.mEverStartedEditor) {
            DefaultLogger.w("TransitionEditorManager", "Transition stop, because activity restart.");
        } else if (!handleEditorResult) {
        } else {
            this.mStartTransition = System.currentTimeMillis();
            ActivityCompat.postponeEnterTransition(this.mActivity);
            ThreadManager.getMainHandler().postDelayed(this.mOnLoadTimeOut, 2000L);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager
    public void onImageLoadFinish(String str) {
        super.onImageLoadFinish(str);
        if (this.mTargetId > 0) {
            finishTransition();
        } else if (this.mTargetFilePath == null || str == null || !TextUtils.equals(Uri.parse(str).getPath(), this.mTargetFilePath)) {
        } else {
            finishTransition();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishTransition() {
        DefaultLogger.d("TransitionEditorManager", "editor transition delay %s", Long.valueOf(System.currentTimeMillis() - this.mStartTransition));
        ThreadManager.getMainHandler().removeCallbacks(this.mOnLoadTimeOut);
        BaseActivity baseActivity = this.mActivity;
        if (baseActivity != null) {
            ActivityCompat.startPostponedEnterTransition(baseActivity);
        }
        this.mTargetFilePath = null;
        this.mTargetId = 0L;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager
    public void release() {
        super.release();
        ThreadManager.getMainHandler().removeCallbacks(this.mOnLoadTimeOut);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager
    public void onActivityResult(int i, final int i2, final Intent intent) {
        super.onActivityResult(i, i2, intent);
        ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager.2
            @Override // java.lang.Runnable
            public void run() {
                BaseDataItem dataItem = TransitionEditorManager.this.mDataProvider.getFieldData().mCurrent.getDataItem();
                TransitionEditorManager.this.mFragment.setTopBarContentVisibility(true);
                TransitionEditorManager.this.mFragment.showBars(true);
                if (dataItem == null || (!dataItem.isVideo() && TransitionEditorManager.this.mFragment.isCurrentImageOverDisplayArea())) {
                    TransitionEditorManager.this.mFragment.setSystemBarVisibility(true);
                }
                TransitionEditorManager.this.mFragment.hideNarBarForFullScreenGesture();
                TransitionEditorManager.this.mFragment.onActivityResultForSpecialType();
                int i3 = i2;
                if (i3 != -1) {
                    if (i3 != 0) {
                        return;
                    }
                    TransitionEditorManager.this.onCanceled();
                    return;
                }
                TransitionEditorManager transitionEditorManager = TransitionEditorManager.this;
                if (!transitionEditorManager.mResultHandled) {
                    transitionEditorManager.handleEditorResult(intent);
                    TransitionEditorManager transitionEditorManager2 = TransitionEditorManager.this;
                    transitionEditorManager2.mTargetFilePath = null;
                    transitionEditorManager2.mTargetId = 0L;
                }
                TransitionEditorManager.this.mResultHandled = false;
            }
        }, 10L);
    }
}
