package com.miui.gallery.provider;

/* loaded from: classes2.dex */
public interface InternalContract$Media {
    public static final String MEDIA_DETAIL_EXTRA_SELECTION;
    public static final String SELECTION_FORMAT_QUERY_MEDIA_GROUP = "(alias_hidden=0 AND alias_rubbish=0 AND localGroupId != " + String.valueOf(-1000L) + " AND ((specialTypeFlags !=0 AND specialTypeFlags & %s != 0) OR (mimeType='image/gif' OR alias_fold_burst=1)))";

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("alias_hidden = 0 AND alias_rubbish = 0 AND localGroupId != ");
        sb.append(String.valueOf(-1000L));
        MEDIA_DETAIL_EXTRA_SELECTION = sb.toString();
    }
}
