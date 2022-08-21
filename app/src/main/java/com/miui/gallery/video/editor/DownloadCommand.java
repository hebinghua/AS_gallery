package com.miui.gallery.video.editor;

import com.miui.gallery.video.editor.factory.VideoEditorModuleFactory;
import com.miui.gallery.video.editor.model.VideoEditorBaseModel;

/* loaded from: classes2.dex */
public class DownloadCommand {
    public VideoEditorBaseModel mBaseModel;
    public long mId;
    public boolean mIsTemplate;
    public int mPosition;
    public String mUnzipPath;
    public String mZipPath;

    public DownloadCommand(VideoEditorBaseModel videoEditorBaseModel, int i, VideoEditorModuleFactory videoEditorModuleFactory) {
        this.mBaseModel = videoEditorBaseModel;
        this.mId = videoEditorBaseModel.getId();
        this.mPosition = i;
        this.mIsTemplate = videoEditorBaseModel.isTemplate();
        this.mZipPath = videoEditorModuleFactory.getTemplatePath(videoEditorBaseModel.getId()) + ".zip";
        this.mUnzipPath = this.mIsTemplate ? videoEditorModuleFactory.getUnzipPath() : "";
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof DownloadCommand) && ((DownloadCommand) obj).getId() == this.mId;
    }

    public int hashCode() {
        return String.valueOf(this.mId).hashCode();
    }

    public String getZipPath() {
        return this.mZipPath;
    }

    public String getUnzipPath() {
        return this.mUnzipPath;
    }

    public long getId() {
        return this.mId;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public VideoEditorBaseModel getData() {
        return this.mBaseModel;
    }

    public boolean isTemplate() {
        return this.mIsTemplate;
    }
}
