package com.miui.gallery.provider.cache;

import com.miui.gallery.provider.cache.IMedia;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BurstMedia.kt */
/* loaded from: classes2.dex */
public final class BurstMedia<T extends IMedia> implements Comparable<BurstMedia<T>> {
    public final int burstIndex;
    public final T media;
    public final int position;

    public BurstMedia(int i, int i2, T media) {
        Intrinsics.checkNotNullParameter(media, "media");
        this.burstIndex = i;
        this.position = i2;
        this.media = media;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return compareTo((BurstMedia) ((BurstMedia) obj));
    }

    public final int getBurstIndex() {
        return this.burstIndex;
    }

    public final int getPosition() {
        return this.position;
    }

    public final T getMedia() {
        return this.media;
    }

    public int compareTo(BurstMedia<T> other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return -Intrinsics.compare(this.burstIndex, other.burstIndex);
    }
}
