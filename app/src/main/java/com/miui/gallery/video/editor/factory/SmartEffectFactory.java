package com.miui.gallery.video.editor.factory;

import android.content.Context;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.video.editor.config.VideoEditorConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SmartEffectFactory extends VideoEditorModuleFactory {
    @Override // com.miui.gallery.video.editor.factory.VideoEditorModuleFactory
    public String getTemplatePath(long j) {
        return VideoEditorConfig.SMART_EFFECT_PATH + File.separator + j;
    }

    @Override // com.miui.gallery.video.editor.factory.VideoEditorModuleFactory
    public List<LocalResource> getLocalTemplateEntities(Context context) {
        return new ArrayList();
    }

    @Override // com.miui.gallery.video.editor.factory.VideoEditorModuleFactory
    public String getUnzipPath() {
        return VideoEditorConfig.SMART_EFFECT_PATH;
    }
}
