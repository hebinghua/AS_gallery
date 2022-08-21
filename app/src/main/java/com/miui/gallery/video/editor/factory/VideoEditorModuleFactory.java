package com.miui.gallery.video.editor.factory;

import android.content.Context;
import com.miui.gallery.net.resource.LocalResource;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class VideoEditorModuleFactory {
    public abstract List<LocalResource> getLocalTemplateEntities(Context context);

    public abstract String getTemplatePath(long j);

    public String getUnzipPath() {
        return "";
    }
}
