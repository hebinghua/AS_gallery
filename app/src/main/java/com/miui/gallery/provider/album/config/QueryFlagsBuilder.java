package com.miui.gallery.provider.album.config;

import ch.qos.logback.core.util.FileSize;

/* loaded from: classes2.dex */
public class QueryFlagsBuilder {
    public long queryFlag;

    public QueryFlagsBuilder() {
    }

    public QueryFlagsBuilder(long j) {
        this.queryFlag = j;
    }

    public QueryFlagsBuilder addToFlags(long j) {
        this.queryFlag = j | this.queryFlag;
        return this;
    }

    public QueryFlagsBuilder removeFlags(long j) {
        this.queryFlag = (~j) & this.queryFlag;
        return this;
    }

    public QueryFlagsBuilder queryAll() {
        return addToFlags(2147483648L);
    }

    public QueryFlagsBuilder joinOtherShareAlbums() {
        return addToFlags(32768L);
    }

    public QueryFlagsBuilder joinAllVirtualAlbum() {
        return addToFlags(983040L);
    }

    public QueryFlagsBuilder joinVideoAlbum() {
        return addToFlags(65536L);
    }

    public QueryFlagsBuilder joinAllPhotoAlbum() {
        return addToFlags(131072L);
    }

    public QueryFlagsBuilder joinFavoritesAlbum() {
        return addToFlags(262144L);
    }

    public QueryFlagsBuilder joinVirtualScreenshotsRecorders() {
        return addToFlags(524288L);
    }

    public QueryFlagsBuilder excludeRealScreenshotsAndRecorders() {
        return addToFlags(FileSize.MB_COEFFICIENT);
    }

    public QueryFlagsBuilder excludeImmutableAlbum() {
        return addToFlags(8589934592L);
    }

    public QueryFlagsBuilder excludeSystemAlbum() {
        return addToFlags(16777216L);
    }

    public QueryFlagsBuilder excludeNormalAlbums() {
        return addToFlags(33554432L);
    }

    public QueryFlagsBuilder excludeUnimportantAlbum() {
        return addToFlags(14680064L);
    }

    public QueryFlagsBuilder returnAlbumTypeByAttributes(long j) {
        return addToFlags(j);
    }

    public QueryFlagsBuilder excludeRubbishAlbum() {
        return addToFlags(2097152L);
    }

    public QueryFlagsBuilder excludeHiddenAlbum() {
        return addToFlags(4194304L);
    }

    public QueryFlagsBuilder excludeOtherAlbum() {
        return addToFlags(8388608L);
    }

    public QueryFlagsBuilder excludeEmptyAlbum() {
        return addToFlags(469762048L);
    }

    public QueryFlagsBuilder excludeEmptySystemAlbum() {
        return addToFlags(268435456L);
    }

    public QueryFlagsBuilder excludeEmptyThirdPartyAlbum() {
        return addToFlags(134217728L);
    }

    public QueryFlagsBuilder excludeRawAlbum() {
        return addToFlags(17179869184L);
    }

    public QueryFlagsBuilder onlyImageMediaType() {
        return addToFlags(536870912L);
    }

    public QueryFlagsBuilder onlyVideoMediaType() {
        return addToFlags(FileSize.GB_COEFFICIENT);
    }

    public long build() {
        return this.queryFlag;
    }
}
