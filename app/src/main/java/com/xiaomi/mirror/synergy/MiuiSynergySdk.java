package com.xiaomi.mirror.synergy;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.xiaomi.mirror.ISameAccountApCallback;
import com.xiaomi.stat.MiStat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class MiuiSynergySdk {
    public static final int SYNERGY_ERR = -1;
    public static final int SYNERGY_OK = 0;
    public static final int SYNERGY_SOFTAP_ALREADY_CONNECTED = 2;
    public static final int SYNERGY_SOFTAP_ALREADY_CONNECTING = 1;
    private static final String TAG = "MiuiSynergy";
    private final Executor mExecutor = Executors.newCachedThreadPool();
    private final ISameAccountApCallback mISameAccountApCallback = new ISameAccountApCallback.Stub() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.4
        @Override // com.xiaomi.mirror.ISameAccountApCallback
        public void onApConnectedStatusUpdate(int i, Bundle bundle) {
            if (MiuiSynergySdk.this.mSameAccountApCallback != null) {
                MiuiSynergySdk.this.mSameAccountApCallback.onApConnectedStatusUpdate(i, bundle == null ? null : new SameAccountAccessPoint(bundle));
            }
        }

        @Override // com.xiaomi.mirror.ISameAccountApCallback
        public void onApInfoUpdate(Bundle bundle) {
            if (MiuiSynergySdk.this.mSameAccountApCallback != null) {
                MiuiSynergySdk.this.mSameAccountApCallback.onApInfoUpdate(bundle == null ? null : new SameAccountAccessPoint(bundle));
            }
        }
    };
    private SameAccountApCallback mSameAccountApCallback;

    /* loaded from: classes3.dex */
    public interface ChooseFileCallback {
        void onFileChosen(ClipData clipData);
    }

    /* loaded from: classes3.dex */
    public static final class Holder {
        private static final MiuiSynergySdk INSTANCE = new MiuiSynergySdk();

        private Holder() {
        }
    }

    /* loaded from: classes3.dex */
    public static class Option {
        public Bitmap icon;
        private String id;
        public String title;

        public int invoke(Activity activity, Uri uri, String str) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(CallMethod.ARG_URI, uri);
            bundle.putString(CallMethod.ARG_EXTRA_STRING, str);
            bundle.putString("id", this.id);
            bundle.putInt(CallMethod.ARG_DISPLAY_ID, activity.getWindow().getDecorView().getDisplay().getDisplayId());
            try {
                CallMethod.doCall(activity.getContentResolver(), CallMethod.METHOD_OPEN_ON_SYNERGY, null, bundle);
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    /* loaded from: classes3.dex */
    public interface QueryOpenCallback {
        void onQueryResult(List<Option> list);
    }

    /* loaded from: classes3.dex */
    public static class SameAccountAccessPoint {
        private int batteryPercent;
        private boolean is5G;
        private String ssid;

        public SameAccountAccessPoint(Bundle bundle) {
            this.batteryPercent = -1;
            if (bundle == null) {
                return;
            }
            this.ssid = bundle.getString("apSsid");
            this.is5G = bundle.getBoolean(CallMethod.RESULT_AP_IS5G);
            this.batteryPercent = bundle.getInt(CallMethod.RESULT_BATTERY_PERCENT, -1);
        }

        public int getBatteryPercent() {
            return this.batteryPercent;
        }

        public String getSsid() {
            return this.ssid;
        }

        public boolean isIs5G() {
            return this.is5G;
        }
    }

    /* loaded from: classes3.dex */
    public interface SameAccountApCallback {
        void onApConnectedStatusUpdate(int i, SameAccountAccessPoint sameAccountAccessPoint);

        void onApInfoUpdate(SameAccountAccessPoint sameAccountAccessPoint);
    }

    public static MiuiSynergySdk getInstance() {
        return Holder.INSTANCE;
    }

    public static Uri getUriFor(String str) {
        return new Uri.Builder().scheme(MiStat.Param.CONTENT).authority(CallMethod.CALL_PROVIDER_AUTHORITY).path(str).build();
    }

    public int chooseFileOnSynergy(Activity activity, final ChooseFileCallback chooseFileCallback) {
        final Bundle bundle = new Bundle();
        bundle.putInt(CallMethod.ARG_DISPLAY_ID, activity.getWindow().getDecorView().getDisplay().getDisplayId());
        final ContentResolver contentResolver = activity.getContentResolver();
        this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.2
            @Override // java.lang.Runnable
            public void run() {
                ClipData clipData = null;
                Bundle doCall = CallMethod.doCall(contentResolver, CallMethod.METHOD_CHOOSE_FILE_FROM_SYNERGY, null, bundle);
                ChooseFileCallback chooseFileCallback2 = chooseFileCallback;
                if (doCall != null) {
                    clipData = (ClipData) doCall.getParcelable("clipData");
                }
                chooseFileCallback2.onFileChosen(clipData);
            }
        });
        return 0;
    }

    public int connectSameAccountAp(Context context, String str) {
        Bundle bundle = new Bundle();
        bundle.putString("apSsid", str);
        try {
            Bundle doCall = CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_CONNECT_SAME_ACCOUNT_AP, null, bundle);
            if (doCall != null) {
                return doCall.getInt(CallMethod.RESULT_SOFTAP_STATE, -1);
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public IBinder getAliveBinder(Context context) {
        Bundle doCall = CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_GET_ALIVE_BINDER, null, null);
        if (doCall == null) {
            return null;
        }
        return doCall.getBinder(CallMethod.RESULT_BINDER);
    }

    public CallRelayService getCallRelayService(Context context) {
        IBinder binder;
        Bundle doCall = CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_GET_CALL_RELAY_SERVICE, null, null);
        if (doCall == null || (binder = doCall.getBinder(CallMethod.RESULT_BINDER)) == null) {
            return null;
        }
        return new CallRelayService(binder);
    }

    public int getInt(Context context, Uri uri, int i) {
        String string = getString(context, uri, null);
        if (string != null) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException unused) {
            }
        }
        return i;
    }

    public String getString(Context context, Uri uri, String str) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CallMethod.ARG_URI, uri);
        try {
            Bundle doCall = CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_GET, null, bundle);
            return doCall == null ? str : doCall.getString("value");
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public boolean isFloatWindowShow(Context context) {
        try {
            Bundle doCall = CallMethod.doCall(context.getContentResolver(), "isFloatWindowShow", null, null);
            if (doCall != null) {
                if (doCall.getBoolean("isFloatWindowShow")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isP2PWorking(Context context) {
        try {
            Bundle doCall = CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_IS_P2P_WORKING, null, null);
            if (doCall != null) {
                if (doCall.getBoolean(CallMethod.RESULT_ENABLE_BOOLEAN)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSupportTakePhoto(Context context) {
        try {
            Bundle doCall = CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_IS_SUPPORT_TAKE_PHOTO, null, null);
            if (doCall != null) {
                if (doCall.getBoolean(CallMethod.RESULT_ENABLE_BOOLEAN)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSynergyEnable(Context context) {
        try {
            Bundle doCall = CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_IS_SYNERGY_ENABLE, null, null);
            if (doCall != null) {
                if (doCall.getBoolean(CallMethod.RESULT_ENABLE_BOOLEAN)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ParcelFileDescriptor openDirect(Context context, Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CallMethod.ARG_URI, uri);
        try {
            Bundle doCall = CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_OPEN_DIRECT, null, bundle);
            if (doCall != null) {
                return (ParcelFileDescriptor) doCall.getParcelable(CallMethod.RESULT_FILE_DESCRIPTOR);
            }
            return null;
        } catch (Exception e) {
            throw new IOException("open failed", e);
        }
    }

    public int openMiCloudOnSynergy(Context context) {
        try {
            CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_OPEN_MI_CLOUD_ON_SYNERGY, null, null);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int openOnSynergy(Activity activity, Uri uri, String str) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CallMethod.ARG_URI, uri);
        bundle.putString(CallMethod.ARG_EXTRA_STRING, str);
        bundle.putInt(CallMethod.ARG_DISPLAY_ID, activity.getWindow().getDecorView().getDisplay().getDisplayId());
        try {
            CallMethod.doCall(activity.getContentResolver(), CallMethod.METHOD_OPEN_ON_SYNERGY, null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int queryOpenOnSynergy(Context context, String str, final QueryOpenCallback queryOpenCallback) {
        final Bundle bundle = new Bundle();
        bundle.putString(CallMethod.ARG_EXTENSION, str);
        final ContentResolver contentResolver = context.getContentResolver();
        this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.1
            @Override // java.lang.Runnable
            public void run() {
                Bundle doCall = CallMethod.doCall(contentResolver, CallMethod.METHOD_QUERY_OPEN_ON_SYNERGY, null, bundle);
                if (doCall == null) {
                    queryOpenCallback.onQueryResult(null);
                    return;
                }
                ArrayList parcelableArrayList = doCall.getParcelableArrayList(CallMethod.RESULT_OPTION_LIST);
                if (parcelableArrayList == null) {
                    queryOpenCallback.onQueryResult(null);
                    return;
                }
                ArrayList arrayList = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    Option option = new Option();
                    option.id = bundle2.getString("id");
                    option.title = bundle2.getString("title");
                    option.icon = (Bitmap) bundle2.getParcelable(CallMethod.RESULT_ICON);
                    arrayList.add(option);
                }
                queryOpenCallback.onQueryResult(arrayList);
            }
        });
        return 0;
    }

    public SameAccountAccessPoint querySameAccountApInfo(Context context) {
        try {
            Bundle doCall = CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_QUERY_SAME_ACCOUNT_AP, null, null);
            if (doCall != null && !TextUtils.isEmpty(doCall.getString("apSsid"))) {
                return new SameAccountAccessPoint(doCall);
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public int registerSameAccountApCallback(Context context, SameAccountApCallback sameAccountApCallback) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBinder(CallMethod.ARG_AP_CALLBACK, this.mISameAccountApCallback.asBinder());
            this.mSameAccountApCallback = sameAccountApCallback;
            CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_REGISTER_AP_CALLBACK, null, bundle);
            return 0;
        } catch (Exception unused) {
            return -1;
        }
    }

    public int saveToSynergy(Activity activity, ClipData clipData, String str) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("clipData", clipData);
        bundle.putString(CallMethod.ARG_EXTRA_STRING, str);
        bundle.putInt(CallMethod.ARG_DISPLAY_ID, activity.getWindow().getDecorView().getDisplay().getDisplayId());
        try {
            CallMethod.doCall(activity.getContentResolver(), CallMethod.METHOD_SAVE_TO_SYNERGY, null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int takePhotoCancel(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putInt(CallMethod.ARG_DISPLAY_ID, activity.getWindow().getDecorView().getDisplay().getDisplayId());
        try {
            CallMethod.doCall(activity.getContentResolver(), CallMethod.METHOD_TAKE_PHOTO_CANCEL, null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int takePhotoOnSynergy(Activity activity, final ChooseFileCallback chooseFileCallback) {
        final Bundle bundle = new Bundle();
        bundle.putInt(CallMethod.ARG_DISPLAY_ID, activity.getWindow().getDecorView().getDisplay().getDisplayId());
        final ContentResolver contentResolver = activity.getContentResolver();
        this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.3
            @Override // java.lang.Runnable
            public void run() {
                ClipData clipData = null;
                Bundle doCall = CallMethod.doCall(contentResolver, CallMethod.METHOD_TAKE_PHOTO_FROM_SYNERGY, null, bundle);
                ChooseFileCallback chooseFileCallback2 = chooseFileCallback;
                if (doCall != null) {
                    clipData = (ClipData) doCall.getParcelable("clipData");
                }
                chooseFileCallback2.onFileChosen(clipData);
            }
        });
        return 0;
    }

    public int unRegisterSameAccountApCallback(Context context) {
        try {
            CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_UNREGISTER_AP_CALLBACK, null, null);
            this.mSameAccountApCallback = null;
            return 0;
        } catch (Exception unused) {
            return -1;
        }
    }

    public int updateTitle(Context context, String str) {
        Bundle bundle = new Bundle();
        bundle.putString("title", str);
        try {
            CallMethod.doCall(context.getContentResolver(), CallMethod.METHOD_UPDATE_TITLE, null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
