package com.miui.gallery.assistant.model;

import java.util.Comparator;

/* loaded from: classes.dex */
public class MediaFeatureItemDurationComparator implements Comparator<MediaFeatureItem> {
    @Override // java.util.Comparator
    public int compare(MediaFeatureItem mediaFeatureItem, MediaFeatureItem mediaFeatureItem2) {
        return Long.compare(mediaFeatureItem2.getDuration(), mediaFeatureItem.getDuration());
    }
}
