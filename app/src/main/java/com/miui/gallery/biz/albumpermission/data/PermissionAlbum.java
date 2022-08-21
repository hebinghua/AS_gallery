package com.miui.gallery.biz.albumpermission.data;

import com.miui.gallery.provider.cache.IAlbum;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: PermissionAlbum.kt */
/* loaded from: classes.dex */
public final class PermissionAlbum implements IAlbum {
    public final List<IAlbum> albums;
    public boolean applicable;
    public final String coverPath;
    public final String directoryPath;
    public boolean granted;
    public final long id;
    public final String[] localPaths;
    public final String name;
    public final String serverId;

    public PermissionAlbum(long j, String name, String str, String str2, String str3, boolean z, boolean z2, List<IAlbum> albums, String[] localPaths) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(albums, "albums");
        Intrinsics.checkNotNullParameter(localPaths, "localPaths");
        this.id = j;
        this.name = name;
        this.directoryPath = str;
        this.coverPath = str2;
        this.serverId = str3;
        this.granted = z;
        this.applicable = z2;
        this.albums = albums;
        this.localPaths = localPaths;
    }

    @Override // com.miui.gallery.provider.cache.IRecord
    public long getId() {
        return this.id;
    }

    @Override // com.miui.gallery.provider.cache.IAlbum
    public String getName() {
        return this.name;
    }

    public String getCoverPath() {
        return this.coverPath;
    }

    public final boolean getGranted() {
        return this.granted;
    }

    public final void setGranted(boolean z) {
        this.granted = z;
    }

    public final boolean getApplicable() {
        return this.applicable;
    }

    public final void setApplicable(boolean z) {
        this.applicable = z;
    }

    public final List<IAlbum> getAlbums() {
        return this.albums;
    }

    public final String[] getLocalPaths() {
        return this.localPaths;
    }
}
