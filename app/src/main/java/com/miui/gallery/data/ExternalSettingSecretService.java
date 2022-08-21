package com.miui.gallery.data;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.miui.gallery.data.IExternalSettingSecretInterface;
import com.miui.gallery.util.logger.DefaultLogger;

@Deprecated
/* loaded from: classes.dex */
public class ExternalSettingSecretService extends Service {

    /* loaded from: classes.dex */
    public static class Deletor extends IExternalSettingSecretInterface.Stub {
        public Deletor() {
        }

        @Override // com.miui.gallery.data.IExternalSettingSecretInterface
        public void preSettingSecret(String[] strArr) {
            DefaultLogger.e("ExternalSettingSecretService", "someone is accessing ExternalSettingSecretService");
        }

        @Override // com.miui.gallery.data.IExternalSettingSecretInterface
        public void onFinishSettingSecret(String[] strArr, String[] strArr2) {
            DefaultLogger.e("ExternalSettingSecretService", "someone is accessing ExternalSettingSecretService");
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return new Deletor();
    }
}
