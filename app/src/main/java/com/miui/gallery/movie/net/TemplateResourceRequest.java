package com.miui.gallery.movie.net;

import com.miui.gallery.movie.MovieConfig;
import com.miui.gallery.movie.entity.MovieResource;
import com.miui.gallery.movie.entity.TemplateResource;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.settings.Settings;

/* loaded from: classes2.dex */
public class TemplateResourceRequest extends LocalResourceRequest {
    @Override // com.miui.gallery.movie.net.LocalResourceRequest
    public long getParentId() {
        if (MovieConfig.isUserXmSdk()) {
            return 14390483457474720L;
        }
        if (!BaseBuildUtil.isInternational()) {
            return 14400175559868576L;
        }
        return Settings.getRegion().endsWith("IN") ? 14390296618598560L : 14400115027214432L;
    }

    @Override // com.miui.gallery.movie.net.LocalResourceRequest
    public MovieResource newLocalResource() {
        return new TemplateResource();
    }
}
