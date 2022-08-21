package com.miui.gallery.ui.share;

import android.net.Uri;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public interface PrepareItem extends Parcelable {
    int getFlags();

    Uri getPreparedUri();

    Uri getPreparedUriInLastStep();

    Uri getSrcUri();

    void onPrepared(Uri uri);

    void onStepPrepared(Uri uri, int i);
}
