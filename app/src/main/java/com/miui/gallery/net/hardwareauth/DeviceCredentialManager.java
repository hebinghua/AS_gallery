package com.miui.gallery.net.hardwareauth;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.util.Base64;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.MemoryPreferenceHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class DeviceCredentialManager {
    public static boolean SUPPORT_CLOUD_CREDENTIAL;
    public static boolean hasCheckedSupportCloudCredential;

    public static /* synthetic */ void $r8$lambda$yKyT44WzOcvLcTnIVijxN_5HdRI(Context context) {
        checkInternal(context);
    }

    public static void checkSupportCloudCredential(final Context context) {
        AsyncTask.execute(new Runnable() { // from class: com.miui.gallery.net.hardwareauth.DeviceCredentialManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DeviceCredentialManager.$r8$lambda$yKyT44WzOcvLcTnIVijxN_5HdRI(context);
            }
        });
    }

    public static boolean getSupportCloudCredential(Context context) {
        return !hasCheckedSupportCloudCredential || SUPPORT_CLOUD_CREDENTIAL;
    }

    public static void checkInternal(Context context) {
        if (context == null) {
            return;
        }
        int i = MemoryPreferenceHelper.getInt(BaseGalleryPreferences.PrefKeys.OCR_SUPPORT_KEY, -1);
        if (i == 1) {
            SUPPORT_CLOUD_CREDENTIAL = true;
            hasCheckedSupportCloudCredential = true;
            return;
        }
        boolean z = false;
        if (i == 0) {
            SUPPORT_CLOUD_CREDENTIAL = false;
            hasCheckedSupportCloudCredential = true;
            return;
        }
        ContentProviderClient acquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(Uri.parse("content://com.miui.cloudservice.GalleryProxySecurityDeviceCredentialProvider"));
        if (acquireContentProviderClient != null) {
            z = true;
        }
        SUPPORT_CLOUD_CREDENTIAL = z;
        hasCheckedSupportCloudCredential = true;
        if (acquireContentProviderClient != null) {
            acquireContentProviderClient.close();
        }
        MemoryPreferenceHelper.putInt(BaseGalleryPreferences.PrefKeys.OCR_SUPPORT_KEY, SUPPORT_CLOUD_CREDENTIAL ? 1 : 0);
    }

    public static String getFidFromCloudService(Context context) {
        ContentProviderClient acquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(Uri.parse("content://com.miui.cloudservice.GalleryProxySecurityDeviceCredentialProvider"));
        if (acquireContentProviderClient == null) {
            return null;
        }
        try {
            Bundle call = acquireContentProviderClient.call("getSecurityDeviceId", null, null);
            if (call != null) {
                return call.getString("extra_security_device_id", "");
            }
            return null;
        } catch (Exception e) {
            DefaultLogger.e("DeviceCredentialManager", "getFidFromCloudService exception %s", e);
            return null;
        } finally {
            acquireContentProviderClient.close();
        }
    }

    public static String signSyncFromCloudService(Context context, byte[] bArr) {
        ContentProviderClient acquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(Uri.parse("content://com.miui.cloudservice.GalleryProxySecurityDeviceCredentialProvider"));
        try {
            if (acquireContentProviderClient == null) {
                return null;
            }
            Bundle bundle = new Bundle();
            bundle.putByteArray("extra_to_sign_data", bArr);
            MyResultReceiver myResultReceiver = new MyResultReceiver(null);
            bundle.putParcelable("extra_receiver", createReceiverForSending(myResultReceiver));
            acquireContentProviderClient.call("signWithDeviceCredential", null, bundle);
            if (!myResultReceiver.waitForResult()) {
                return null;
            }
            if (myResultReceiver.getSignedData() != null) {
                return Base64.encodeToString(myResultReceiver.getSignedData(), 8);
            }
            return null;
        } catch (Exception e) {
            DefaultLogger.e("DeviceCredentialManager", "signSyncFromCloudService exception %s", e);
            return null;
        } finally {
            acquireContentProviderClient.close();
        }
    }

    public static ResultReceiver createReceiverForSending(ResultReceiver resultReceiver) {
        Parcel obtain = Parcel.obtain();
        resultReceiver.writeToParcel(obtain, 0);
        obtain.setDataPosition(0);
        ResultReceiver resultReceiver2 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(obtain);
        obtain.recycle();
        return resultReceiver2;
    }

    /* loaded from: classes2.dex */
    public static class MyResultReceiver extends ResultReceiver {
        public CountDownLatch mSignDataLatch;
        public byte[] mSignedData;

        public MyResultReceiver(Handler handler) {
            super(handler);
            this.mSignDataLatch = new CountDownLatch(1);
        }

        @Override // android.os.ResultReceiver
        public void onReceiveResult(int i, Bundle bundle) {
            if (i == 0) {
                this.mSignedData = bundle.getByteArray("extra_signed_data");
            }
            this.mSignDataLatch.countDown();
        }

        public byte[] getSignedData() {
            return this.mSignedData;
        }

        public boolean waitForResult() throws InterruptedException {
            this.mSignDataLatch.await(1000L, TimeUnit.MILLISECONDS);
            return true;
        }
    }
}
