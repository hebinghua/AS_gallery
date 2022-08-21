package com.miui.gallery.scanner.core.task.convertor.internal.legacy;

import android.content.Context;
import com.miui.gallery.scanner.core.task.convertor.internal.base.AbsClassicalScanStrategy;
import com.miui.gallery.scanner.core.task.convertor.internal.base.AllAlbumDirectoriesScanner;
import com.miui.gallery.scanner.core.task.convertor.internal.base.MediaProviderScanner;

/* loaded from: classes2.dex */
public class LegacyClassicalScanStrategy extends AbsClassicalScanStrategy {
    public LegacyClassicalScanStrategy(Context context) {
        super(context);
        this.mScanners.add(new MediaProviderScanner());
        this.mScanners.add(new AllAlbumDirectoriesScanner());
    }
}
