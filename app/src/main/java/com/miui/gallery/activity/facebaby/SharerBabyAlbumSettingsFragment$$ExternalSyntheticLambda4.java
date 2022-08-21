package com.miui.gallery.activity.facebaby;

import android.database.Cursor;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.util.GalleryUtils;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SharerBabyAlbumSettingsFragment$$ExternalSyntheticLambda4 implements GalleryUtils.QueryHandler {
    public static final /* synthetic */ SharerBabyAlbumSettingsFragment$$ExternalSyntheticLambda4 INSTANCE = new SharerBabyAlbumSettingsFragment$$ExternalSyntheticLambda4();

    @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
    /* renamed from: handle */
    public final Object mo1712handle(Cursor cursor) {
        BabyInfo lambda$getBabyInfoFromDB$4;
        lambda$getBabyInfoFromDB$4 = SharerBabyAlbumSettingsFragment.lambda$getBabyInfoFromDB$4(cursor);
        return lambda$getBabyInfoFromDB$4;
    }
}
