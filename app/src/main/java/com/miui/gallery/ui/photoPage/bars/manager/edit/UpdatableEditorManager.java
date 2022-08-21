package com.miui.gallery.ui.photoPage.bars.manager.edit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public abstract class UpdatableEditorManager extends BaseEditorManager {
    public BroadcastReceiver mEditorReceiver;
    public Runnable mOnLoadTimeOut;

    public static /* synthetic */ void $r8$lambda$TiXzCDNwwOYc3sfbicbWj0_80ys(UpdatableEditorManager updatableEditorManager) {
        updatableEditorManager.lambda$new$0();
    }

    public abstract String getTargetPackageName();

    public abstract boolean shouldInsertMediaStore();

    public UpdatableEditorManager(IDataProvider iDataProvider, PhotoPageFragment photoPageFragment) {
        super(iDataProvider, photoPageFragment);
        this.mOnLoadTimeOut = new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.manager.edit.UpdatableEditorManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UpdatableEditorManager.$r8$lambda$TiXzCDNwwOYc3sfbicbWj0_80ys(UpdatableEditorManager.this);
            }
        };
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager
    public void onStartEditor() {
        super.onStartEditor();
        registerReceiver();
        this.mResultHandled = false;
        this.mTargetFilePath = null;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager
    public void onImageLoadFinish(String str) {
        super.onImageLoadFinish(str);
        if (this.mTargetFilePath == null || str == null || !TextUtils.equals(Uri.parse(str).getPath(), this.mTargetFilePath)) {
            return;
        }
        DefaultLogger.d("UpdatableEditorManager", "onImageLoadFinish");
        sendPreparedResult();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        ThreadManager.getMainHandler().removeCallbacks(this.mOnLoadTimeOut);
        unregisterReceiver();
        if (i2 == -1 && !this.mResultHandled) {
            handleEditorResult(intent);
        }
        this.mResultHandled = false;
        this.mTargetFilePath = null;
    }

    public final boolean handleEditorResult(Intent intent) {
        Uri data;
        if (intent == null || (data = intent.getData()) == null) {
            return false;
        }
        String path = data.getPath();
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        insertAndNotifyDataSet(path, shouldInsertMediaStore(), true);
        setTargetPath(path);
        return true;
    }

    public /* synthetic */ void lambda$new$0() {
        DefaultLogger.w("UpdatableEditorManager", "editor return to photo, image loading time out.");
        sendPreparedResult();
    }

    public final void registerReceiver() {
        if (this.mEditorReceiver != null || this.mActivity == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.miui.gallery.action.EDITOR_RETURN");
        try {
            intentFilter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            DefaultLogger.w("UpdatableEditorManager", e);
        }
        EditorBroadcastReceiver editorBroadcastReceiver = new EditorBroadcastReceiver();
        this.mEditorReceiver = editorBroadcastReceiver;
        this.mActivity.registerReceiver(editorBroadcastReceiver, intentFilter);
    }

    public final void unregisterReceiver() {
        BaseActivity baseActivity;
        BroadcastReceiver broadcastReceiver = this.mEditorReceiver;
        if (broadcastReceiver == null || (baseActivity = this.mActivity) == null) {
            return;
        }
        try {
            baseActivity.unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            DefaultLogger.w("UpdatableEditorManager", e);
        }
        this.mEditorReceiver = null;
    }

    /* loaded from: classes2.dex */
    public class EditorBroadcastReceiver extends BroadcastReceiver {
        public EditorBroadcastReceiver() {
            UpdatableEditorManager.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || !"com.miui.gallery.action.EDITOR_RETURN".equals(intent.getAction())) {
                return;
            }
            DefaultLogger.d("UpdatableEditorManager", "onEditorReturn");
            UpdatableEditorManager updatableEditorManager = UpdatableEditorManager.this;
            updatableEditorManager.mResultHandled = updatableEditorManager.handleEditorResult(intent);
            UpdatableEditorManager updatableEditorManager2 = UpdatableEditorManager.this;
            if (!updatableEditorManager2.mResultHandled) {
                updatableEditorManager2.sendPreparedResult();
                return;
            }
            updatableEditorManager2.mFragment.clearTrimMemory();
            ThreadManager.getMainHandler().postDelayed(UpdatableEditorManager.this.mOnLoadTimeOut, 2000L);
        }
    }

    public final void sendPreparedResult() {
        ThreadManager.getMainHandler().removeCallbacks(this.mOnLoadTimeOut);
        if (this.mActivity == null) {
            return;
        }
        Intent intent = new Intent("com.miui.gallery.action.IMAGE_PREPARED");
        intent.setPackage(getTargetPackageName());
        this.mActivity.sendBroadcast(intent);
        this.mTargetFilePath = null;
        DefaultLogger.d("UpdatableEditorManager", "sendPreparedResult");
    }

    public void onDestroy() {
        release();
        unregisterReceiver();
        ThreadManager.getMainHandler().removeCallbacks(this.mOnLoadTimeOut);
    }
}
