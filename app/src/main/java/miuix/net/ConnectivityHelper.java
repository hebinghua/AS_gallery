package miuix.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import miuix.core.util.SoftReferenceSingleton;

/* loaded from: classes3.dex */
public class ConnectivityHelper {
    public static final SoftReferenceSingleton<ConnectivityHelper> INSTANCE = new SoftReferenceSingleton<ConnectivityHelper>() { // from class: miuix.net.ConnectivityHelper.1
        @Override // miuix.core.util.SoftReferenceSingleton
        public ConnectivityHelper createInstance(Object obj) {
            return new ConnectivityHelper((Context) obj);
        }
    };
    public ConnectivityManager mConnectivityManager;

    public static ConnectivityHelper getInstance(Context context) {
        return INSTANCE.get(context);
    }

    public ConnectivityHelper(Context context) {
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    public boolean isNetworkConnected() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isUnmeteredNetworkConnected() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && !this.mConnectivityManager.isActiveNetworkMetered();
    }
}
