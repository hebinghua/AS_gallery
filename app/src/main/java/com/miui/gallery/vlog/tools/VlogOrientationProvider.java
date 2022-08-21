package com.miui.gallery.vlog.tools;

import android.content.Context;
import androidx.annotation.Keep;
import com.miui.gallery.widget.OrientationProvider;

@Keep
/* loaded from: classes2.dex */
public class VlogOrientationProvider implements OrientationProvider {
    @Override // com.miui.gallery.widget.OrientationProvider
    public boolean isPortrait(Context context) {
        return !VlogUtils.isLandscape(context);
    }
}
