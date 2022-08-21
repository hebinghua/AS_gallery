package com.miui.gallery.movie.net;

import com.miui.gallery.movie.MovieConfig;
import com.miui.gallery.movie.entity.AudioResource;
import com.miui.gallery.movie.entity.MovieResource;

/* loaded from: classes2.dex */
public class AudioResourceRequest extends LocalResourceRequest {
    @Override // com.miui.gallery.movie.net.LocalResourceRequest
    public long getParentId() {
        return MovieConfig.isUserXmSdk() ? 14399936858423456L : 14400214035857504L;
    }

    @Override // com.miui.gallery.movie.net.LocalResourceRequest
    public MovieResource newLocalResource() {
        return new AudioResource();
    }
}
