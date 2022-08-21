package com.miui.gallery.net.download;

import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: classes2.dex */
public class ConnectionController {

    /* loaded from: classes2.dex */
    public static final class Holder<T> {
        public final int reason;
        public final T value;

        public Holder(T t) {
            this.value = t;
            this.reason = 0;
        }

        public Holder(int i) {
            this.value = null;
            this.reason = i;
        }
    }

    public static Holder<HttpURLConnection> open(Uri uri, int i) {
        String scheme = uri.getScheme();
        if (TextUtils.equals(scheme, "http") || TextUtils.equals(scheme, "https")) {
            return open(uri.toString(), i);
        }
        throw new IllegalArgumentException("not support scheme " + scheme);
    }

    public static Holder<HttpURLConnection> open(String str, int i) {
        int verify = verify(i);
        if (verify != 0) {
            return new Holder<>(verify);
        }
        try {
            DefaultLogger.d("ConnectionController", "try open http connection");
            return new Holder<>((HttpURLConnection) new URL(str).openConnection());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url " + str, e);
        } catch (IOException e2) {
            DefaultLogger.w("ConnectionController", e2);
            return new Holder<>(4);
        }
    }

    public static int verify(int i) {
        DefaultLogger.d("ConnectionController", "refreshing network state");
        if (!BaseNetworkUtils.isNetworkConnected()) {
            return 1;
        }
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            return 2;
        }
        return (i != 1 || !BaseNetworkUtils.isActiveNetworkMetered()) ? 0 : 3;
    }
}
