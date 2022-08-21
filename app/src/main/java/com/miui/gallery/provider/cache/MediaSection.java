package com.miui.gallery.provider.cache;

import android.text.TextUtils;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Records.kt */
/* loaded from: classes2.dex */
public class MediaSection extends Record implements ISection {
    public final int count;
    public final long id;
    public final String label;

    @Override // com.miui.gallery.provider.cache.IRecord
    public long getId() {
        return this.id;
    }

    public int getCount() {
        return this.count;
    }

    public final String getLabel() {
        return this.label;
    }

    public MediaSection(long j, int i, String str) {
        super(null);
        this.id = j;
        this.count = i;
        this.label = str;
    }

    @Override // com.miui.gallery.widget.recyclerview.DiffableItem
    public boolean itemSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        if (other instanceof MediaSection) {
            return getId() == ((MediaSection) other).getId();
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    @Override // com.miui.gallery.widget.recyclerview.DiffableItem
    public boolean contentSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        if (!(other instanceof MediaSection)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        return TextUtils.equals(this.label, ((MediaSection) other).label);
    }
}
