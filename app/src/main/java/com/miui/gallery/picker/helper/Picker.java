package com.miui.gallery.picker.helper;

import android.database.DataSetObserver;
import java.util.List;

/* loaded from: classes2.dex */
public interface Picker extends Iterable<String> {

    /* loaded from: classes2.dex */
    public enum ImageType {
        THUMBNAIL,
        ORIGIN,
        ORIGIN_OR_DOWNLOAD_INFO
    }

    /* loaded from: classes2.dex */
    public enum MediaType {
        IMAGE,
        VIDEO,
        ALL
    }

    /* loaded from: classes2.dex */
    public enum Mode {
        SINGLE,
        MULTIPLE
    }

    /* loaded from: classes2.dex */
    public enum ResultType {
        ID,
        LEGACY_GENERAL,
        LEGACY_MEDIA,
        OPEN_URI
    }

    int baseline();

    void cancel();

    int capacity();

    boolean clear();

    boolean contains(String str);

    int count();

    void done(int i);

    String[] getFilterMimeTypes();

    int getFromType();

    ImageType getImageType();

    MediaType getMediaType();

    Mode getMode();

    ResultType getResultType();

    boolean isFull();

    boolean isPickOwner();

    boolean pick(String str);

    boolean pickAll(List<String> list);

    void registerObserver(DataSetObserver dataSetObserver);

    boolean remove(String str);

    boolean removeAll(List<String> list);

    void setFilterMimeTypes(String[] strArr);

    void setFromType(int i);

    void setImageType(ImageType imageType);

    void setMediaType(MediaType mediaType);

    void setPickOwner(boolean z);

    void setResultType(ResultType resultType);
}
