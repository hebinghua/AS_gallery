package com.miui.gallery.movie.entity;

import com.miui.gallery.movie.ui.factory.TemplateFactory;

/* loaded from: classes2.dex */
public class TemplateResource extends MovieResource {
    @Override // com.miui.gallery.movie.entity.MovieResource
    public String getStatTypeString() {
        return "template";
    }

    @Override // com.miui.gallery.movie.entity.MovieResource
    public String getDownloadSrcPath() {
        return TemplateFactory.getTemplatePath(this.pathKey);
    }

    @Override // com.miui.gallery.movie.entity.MovieResource
    public String getStatNameString() {
        return "template-" + this.label;
    }
}
