package com.miui.gallery.biz.albumpermission;

import com.miui.gallery.biz.albumpermission.data.PermissionAlbum;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AlbumPermissionFragment$$ExternalSyntheticLambda2 implements Predicate {
    public static final /* synthetic */ AlbumPermissionFragment$$ExternalSyntheticLambda2 INSTANCE = new AlbumPermissionFragment$$ExternalSyntheticLambda2();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean granted;
        granted = ((PermissionAlbum) obj).getGranted();
        return granted;
    }
}
