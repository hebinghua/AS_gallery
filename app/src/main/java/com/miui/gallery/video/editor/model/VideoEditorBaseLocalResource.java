package com.miui.gallery.video.editor.model;

import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.video.editor.factory.VideoEditorModuleFactory;

/* loaded from: classes2.dex */
public class VideoEditorBaseLocalResource extends LocalResource {
    public VideoEditorModuleFactory mModuleFactory;

    public VideoEditorBaseLocalResource(VideoEditorModuleFactory videoEditorModuleFactory) {
        this.mModuleFactory = videoEditorModuleFactory;
    }
}
