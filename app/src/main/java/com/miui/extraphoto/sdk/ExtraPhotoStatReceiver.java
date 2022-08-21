package com.miui.extraphoto.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ExtraPhotoStatReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null || !intent.getAction().equals("com.miui.motionphoto.action.EXTRA_PHOTO_STAT")) {
            return;
        }
        int intExtra = intent.getIntExtra("stat_type", -1);
        HashMap hashMap = null;
        if (intExtra != 0) {
            if (intExtra != 1) {
                if (intExtra == 2) {
                    SamplingStatHelper.recordPageStart(null, intent.getStringExtra("page"));
                    return;
                } else if (intExtra != 3) {
                    return;
                } else {
                    SamplingStatHelper.recordPageEnd(null, intent.getStringExtra("page"));
                    return;
                }
            }
            String stringExtra = intent.getStringExtra("string_property");
            String stringExtra2 = intent.getStringExtra("string_value");
            if (TextUtils.isEmpty(stringExtra) || TextUtils.isEmpty(stringExtra2)) {
                return;
            }
            SamplingStatHelper.recordStringPropertyEvent(stringExtra, stringExtra2);
            return;
        }
        String stringExtra3 = intent.getStringExtra("stat_event");
        String stringExtra4 = intent.getStringExtra("sub_event");
        if (TextUtils.isEmpty(stringExtra3) || TextUtils.isEmpty(stringExtra4)) {
            return;
        }
        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("param_keys");
        ArrayList<String> stringArrayListExtra2 = intent.getStringArrayListExtra("param_values");
        if (stringArrayListExtra != null && stringArrayListExtra2 != null && stringArrayListExtra.size() > 0 && stringArrayListExtra.size() == stringArrayListExtra2.size()) {
            hashMap = new HashMap();
            for (int i = 0; i < stringArrayListExtra.size(); i++) {
                hashMap.put(stringArrayListExtra.get(i), stringArrayListExtra2.get(i));
            }
        }
        if (hashMap != null) {
            SamplingStatHelper.recordCountEvent(stringExtra3, stringExtra4, hashMap);
        } else {
            SamplingStatHelper.recordCountEvent(stringExtra3, stringExtra4);
        }
    }
}
