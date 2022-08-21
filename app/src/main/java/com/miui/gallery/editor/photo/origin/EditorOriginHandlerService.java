package com.miui.gallery.editor.photo.origin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.miui.gallery.editor.photo.app.DraftManager;
import com.miui.gallery.editor.photo.origin.EditorOriginFunc;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class EditorOriginHandlerService extends Service {
    public EditorOriginFunc.Stub mBinder = new EditorOriginFunc.Stub() { // from class: com.miui.gallery.editor.photo.origin.EditorOriginHandlerService.1
        @Override // com.miui.gallery.editor.photo.origin.EditorOriginFunc
        public boolean handlerOrigin(OriginRenderData originRenderData) throws RemoteException {
            DefaultLogger.d("EditorOriginHandlerService", "handlerOrigin start");
            DraftManager draftManager = new DraftManager(EditorOriginHandlerService.this, originRenderData.mSource, originRenderData.mBundle, originRenderData.mWithWatermark);
            draftManager.setRenderDataList(originRenderData.mRenderDataList);
            boolean export = draftManager.export(originRenderData.mOut);
            draftManager.release();
            DefaultLogger.d("EditorOriginHandlerService", "handlerOrigin end");
            return export;
        }
    };

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        DefaultLogger.d("EditorOriginHandlerService", "onCreate");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        DefaultLogger.d("EditorOriginHandlerService", "onDestroy");
    }
}
