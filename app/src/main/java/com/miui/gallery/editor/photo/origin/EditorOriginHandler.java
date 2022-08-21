package com.miui.gallery.editor.photo.origin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.editor.photo.app.DraftManager;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.origin.EditorOriginFunc;
import com.miui.gallery.editor.photo.utils.BigBitmapLoadUtils;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public class EditorOriginHandler {
    public EditorOriginFunc mBinder;
    public Context mContext;
    public int mHandlerMode;
    public ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.miui.gallery.editor.photo.origin.EditorOriginHandler.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            EditorOriginHandler.this.mBinder = EditorOriginFunc.Stub.asInterface(iBinder);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            EditorOriginHandler.this.mBinder = null;
        }
    };
    public Uri mSource;

    public EditorOriginHandler(Context context, Uri uri) {
        this.mContext = context;
        this.mSource = uri;
        if (BuildUtil.isEditorProcess()) {
            this.mHandlerMode = 2;
        } else if (useDoubleProcess()) {
            this.mHandlerMode = 1;
        } else {
            this.mHandlerMode = 0;
        }
        DefaultLogger.d("EditorOriginHandler", "editor handler mode:%d", Integer.valueOf(this.mHandlerMode));
    }

    public final boolean useDoubleProcess() {
        return Build.VERSION.SDK_INT < 26 && !BigBitmapLoadUtils.isBigMemoryApp(this.mContext);
    }

    public boolean doExport(DraftManager draftManager, Uri uri) {
        if (!needTransProcess() || draftManager.isPreviewSameWithOrigin()) {
            return draftManager.export(uri);
        }
        return doOriginHandler(uri, draftManager.getRenderDataList(), draftManager.getBundle(), draftManager.isWithWatermark());
    }

    public boolean isInMainProcess() {
        return this.mHandlerMode == 0;
    }

    public boolean needTransProcess() {
        return this.mHandlerMode == 1;
    }

    public void onStart() {
        if (needTransProcess()) {
            bindEditorService();
        }
    }

    public void onDestroy() {
        if (needTransProcess()) {
            unbindEditorService();
        }
    }

    public final void bindEditorService() {
        try {
            this.mContext.bindService(new Intent(this.mContext, EditorOriginHandlerService.class), this.mServiceConnection, BaiduSceneResult.ACCOUNT_BOOK);
        } catch (Exception e) {
            DefaultLogger.e("EditorOriginHandler", e);
        }
    }

    public final void unbindEditorService() {
        try {
            this.mContext.unbindService(this.mServiceConnection);
        } catch (Exception e) {
            DefaultLogger.e("EditorOriginHandler", e);
        }
    }

    public boolean doOriginHandler(Uri uri, List<RenderData> list, Bundle bundle, boolean z) {
        if (this.mBinder != null) {
            try {
                DefaultLogger.d("EditorOriginHandler", "doOriginHandler");
                return this.mBinder.handlerOrigin(new OriginRenderData(list, this.mSource, uri, bundle, z));
            } catch (RemoteException e) {
                DefaultLogger.e("EditorOriginHandler", e);
                return false;
            }
        }
        DefaultLogger.e("EditorOriginHandler", "doOriginHandler bind is null");
        return false;
    }
}
