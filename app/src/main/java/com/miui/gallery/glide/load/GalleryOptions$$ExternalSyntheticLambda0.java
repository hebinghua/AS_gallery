package com.miui.gallery.glide.load;

import com.bumptech.glide.load.Option;
import java.security.MessageDigest;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class GalleryOptions$$ExternalSyntheticLambda0 implements Option.CacheKeyUpdater {
    public static final /* synthetic */ GalleryOptions$$ExternalSyntheticLambda0 INSTANCE = new GalleryOptions$$ExternalSyntheticLambda0();

    @Override // com.bumptech.glide.load.Option.CacheKeyUpdater
    public final void update(byte[] bArr, Object obj, MessageDigest messageDigest) {
        GalleryOptions.lambda$static$0(bArr, (Boolean) obj, messageDigest);
    }
}
