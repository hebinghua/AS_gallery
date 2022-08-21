package com.miui.gallery.storage;

import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.util.Pair;

/* compiled from: IDocumentUILauncherOwner.kt */
/* loaded from: classes2.dex */
public interface IDocumentUILauncherOwner {
    ActivityResultLauncher<Pair<String, Uri>> getCreateDocumentDirLauncher();

    ActivityResultLauncher<Uri> getOpenDocumentTreeLauncher();
}
