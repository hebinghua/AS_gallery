package com.miui.gallery.provider.cache;

import android.text.TextUtils;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Records.kt */
/* loaded from: classes2.dex */
public final class RecentMediaSection extends MediaSection {
    public final Long albumId;

    public RecentMediaSection(long j, int i, String str, Long l) {
        super(j, i, str);
        this.albumId = l;
    }

    @Override // com.miui.gallery.provider.cache.MediaSection, com.miui.gallery.widget.recyclerview.DiffableItem
    public boolean contentSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        if (other instanceof RecentMediaSection) {
            RecentMediaSection recentMediaSection = (RecentMediaSection) other;
            return TextUtils.equals(getLabel(), recentMediaSection.getLabel()) && Objects.equals(this.albumId, recentMediaSection.albumId);
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }
}
