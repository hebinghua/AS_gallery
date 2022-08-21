package com.miui.gallery.provider;

/* loaded from: classes2.dex */
public interface InternalContract$ShareImage {
    public static final String ALIAS_SIZE_FIRST = InternalContract$Cloud.ALIAS_SIZE_FIRST;
    public static final String ALIAS_CLEAR_FIRST = InternalContract$Cloud.ALIAS_CLEAR_FIRST;
    public static final String ALIAS_MICRO_THUMBNAIL = " CASE WHEN " + InternalContract$Cloud.ALIAS_MICRO_VALID + " THEN microthumbfile WHEN " + InternalContract$Cloud.ALIAS_THUMBNAIL_VALID + " THEN thumbnailFile ELSE localFile END ";
}
