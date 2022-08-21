package com.bumptech.glide.load;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public interface Key {
    public static final Charset CHARSET = Charset.forName(Keyczar.DEFAULT_ENCODING);

    boolean equals(Object obj);

    int hashCode();

    void updateDiskCacheKey(MessageDigest messageDigest);
}
