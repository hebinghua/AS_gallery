package com.miui.gallery.editor.photo.app;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyVideoExporter;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderData;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class VideoExportManager {
    public OnProgressCallback mOnProgressCallback;
    public RenderData mRenderData;
    public Map<Effect, IVideoExporter> mVideoExporterMap;

    /* loaded from: classes2.dex */
    public interface OnProgressCallback {
        void onProgress(int i);
    }

    public VideoExportManager() {
        HashMap hashMap = new HashMap();
        this.mVideoExporterMap = hashMap;
        hashMap.put(Effect.SKY, new SkyVideoExporter());
    }

    public void setRenderData(RenderData renderData) {
        this.mRenderData = renderData;
    }

    public boolean hasExportTask() {
        return this.mRenderData != null;
    }

    public int export(Bitmap bitmap, Uri uri) {
        RenderData renderData;
        IVideoExporter iVideoExporter;
        if (bitmap == null || uri == null || !TextUtils.equals(Action.FILE_ATTRIBUTE, uri.getScheme()) || (renderData = this.mRenderData) == null || !renderData.isVideo() || (iVideoExporter = this.mVideoExporterMap.get(this.mRenderData.mType)) == null) {
            return 2;
        }
        String path = uri.getPath();
        iVideoExporter.setCallback(new IVideoExporter.Callback() { // from class: com.miui.gallery.editor.photo.app.VideoExportManager.1
            @Override // com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter.Callback
            public void onProgress(int i) {
                if (VideoExportManager.this.mOnProgressCallback != null) {
                    VideoExportManager.this.mOnProgressCallback.onProgress(i);
                }
            }
        });
        return iVideoExporter.export(path);
    }

    public void cancel() {
        IVideoExporter iVideoExporter;
        RenderData renderData = this.mRenderData;
        if (renderData == null || !renderData.isVideo() || (iVideoExporter = this.mVideoExporterMap.get(this.mRenderData.mType)) == null) {
            return;
        }
        iVideoExporter.cancel();
    }

    public void onCancel() {
        this.mRenderData = null;
    }

    public void release() {
        IVideoExporter iVideoExporter;
        this.mOnProgressCallback = null;
        RenderData renderData = this.mRenderData;
        if (renderData == null || (iVideoExporter = this.mVideoExporterMap.get(renderData.mType)) == null) {
            return;
        }
        iVideoExporter.release();
    }

    public void setOnProgressCallback(OnProgressCallback onProgressCallback) {
        this.mOnProgressCallback = onProgressCallback;
    }
}
