package com.miui.mishare.app.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;
import miui.os.Build;

/* loaded from: classes3.dex */
public class NearbyUtils {
    public static boolean supportNearby(Context context) {
        return Build.IS_INTERNATIONAL_BUILD && getNearbyComponent(context) != null;
    }

    public static ComponentName getNearbyComponent(Context context) {
        Intent intent = new Intent("com.google.android.gms.SHARE_NEARBY");
        intent.setPackage("com.google.android.gms");
        intent.setType("application/octet-stream");
        return intent.resolveActivity(context.getPackageManager());
    }

    public static Intent getShareIntent(Context context, List<Uri> list) {
        String str;
        Intent intent = new Intent();
        intent.setFlags(1);
        if (list.size() == 1) {
            Uri uri = list.get(0);
            str = context.getContentResolver().getType(uri);
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", uri);
        } else {
            intent.setAction("android.intent.action.SEND_MULTIPLE");
            intent.putParcelableArrayListExtra("android.intent.extra.STREAM", new ArrayList<>(list));
            str = "image/*";
        }
        intent.setType(str);
        intent.setComponent(getNearbyComponent(context));
        return intent;
    }
}
