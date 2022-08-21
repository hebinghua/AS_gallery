package com.xiaomi.mirror;

import android.view.View;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public interface OnMirrorMenuQueryListener {
    ArrayList<MirrorMenu> onMirrorMenuQuery(View view);

    void onMirrorMenuShow(View view, boolean z);
}
