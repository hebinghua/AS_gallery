package com.baidu.mapapi.http;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.mapapi.JNIInitializer;
import com.baidu.mapapi.common.Logger;
import com.baidu.mapsdkplatform.comapi.util.h;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class HttpClient {
    public static boolean isHttpsEnable = true;
    public HttpURLConnection a;
    private String b = null;
    private String c = null;
    private int d;
    private int e;
    private String f;
    private ProtoResultCallback g;

    /* loaded from: classes.dex */
    public enum HttpStateError {
        NO_ERROR,
        NETWORK_ERROR,
        INNER_ERROR,
        REQUEST_ERROR,
        SERVER_ERROR
    }

    /* loaded from: classes.dex */
    public static abstract class ProtoResultCallback {
        public abstract void onFailed(HttpStateError httpStateError);

        public abstract void onSuccess(String str);
    }

    /* loaded from: classes.dex */
    public static class a implements HostnameVerifier {
        @Override // javax.net.ssl.HostnameVerifier
        public boolean verify(String str, SSLSession sSLSession) {
            return HttpsURLConnection.getDefaultHostnameVerifier().verify(str, sSLSession);
        }
    }

    public HttpClient(String str, ProtoResultCallback protoResultCallback) {
        this.f = str;
        this.g = protoResultCallback;
    }

    private HttpURLConnection a() {
        HttpsURLConnection httpsURLConnection;
        try {
            URL url = new URL(this.b);
            if (isHttpsEnable) {
                HttpsURLConnection httpsURLConnection2 = (HttpsURLConnection) url.openConnection();
                httpsURLConnection2.setHostnameVerifier(new a());
                httpsURLConnection = httpsURLConnection2;
            } else {
                httpsURLConnection = (HttpURLConnection) url.openConnection();
            }
            httpsURLConnection.setRequestMethod(this.f);
            httpsURLConnection.setDoOutput(false);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setConnectTimeout(this.d);
            httpsURLConnection.setReadTimeout(this.e);
            return httpsURLConnection;
        } catch (Exception e) {
            Log.e("HttpClient", "url connect failed");
            if (Logger.debugEnable()) {
                e.printStackTrace();
                return null;
            }
            Logger.logW("HttpClient", e.getMessage());
            return null;
        }
    }

    public static String getAuthToken() {
        return h.d;
    }

    public static String getPhoneInfo() {
        return h.d();
    }

    public boolean checkNetwork() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) JNIInitializer.getCachedContext().getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 29) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return networkCapabilities != null && networkCapabilities.hasCapability(12);
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
        } catch (Exception e) {
            if (Logger.debugEnable()) {
                e.printStackTrace();
            } else {
                Logger.logW("HttpClient", e.getMessage());
            }
            e.printStackTrace();
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v11, types: [int] */
    /* JADX WARN: Type inference failed for: r1v18, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v9, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v9, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.lang.StringBuilder] */
    public void request(String str) {
        BufferedReader bufferedReader;
        Throwable th;
        InputStream inputStream;
        BufferedReader bufferedReader2;
        Exception e;
        ?? r1;
        this.b = str;
        if (!checkNetwork()) {
            this.g.onFailed(HttpStateError.NETWORK_ERROR);
            return;
        }
        HttpURLConnection a2 = a();
        this.a = a2;
        if (a2 == null) {
            Log.e("HttpClient", "url connection failed");
            this.g.onFailed(HttpStateError.INNER_ERROR);
        } else if (TextUtils.isEmpty(this.b)) {
            this.g.onFailed(HttpStateError.REQUEST_ERROR);
        } else {
            try {
                this.a.connect();
                try {
                    try {
                        r1 = this.a.getResponseCode();
                    } catch (Throwable th2) {
                        th = th2;
                    }
                    try {
                        if (200 != r1) {
                            Log.e("HttpClient", "responseCode is: " + r1);
                            HttpStateError httpStateError = r1 >= 500 ? HttpStateError.SERVER_ERROR : r1 >= 400 ? HttpStateError.REQUEST_ERROR : HttpStateError.INNER_ERROR;
                            if (Logger.debugEnable()) {
                                Logger.logW("HttpClient", this.a.getErrorStream().toString());
                            } else {
                                Logger.logW("HttpClient", "Get response from server failed, http response code=" + r1 + ", error=" + httpStateError);
                            }
                            this.g.onFailed(httpStateError);
                            HttpURLConnection httpURLConnection = this.a;
                            if (httpURLConnection == null) {
                                return;
                            }
                            httpURLConnection.disconnect();
                            return;
                        }
                        r1 = this.a.getInputStream();
                        bufferedReader2 = new BufferedReader(new InputStreamReader((InputStream) r1, Keyczar.DEFAULT_ENCODING));
                        try {
                            StringBuffer stringBuffer = new StringBuffer();
                            while (true) {
                                int read = bufferedReader2.read();
                                if (read == -1) {
                                    break;
                                }
                                stringBuffer.append((char) read);
                            }
                            this.c = stringBuffer.toString();
                            if (r1 != 0) {
                                bufferedReader2.close();
                                r1.close();
                            }
                            HttpURLConnection httpURLConnection2 = this.a;
                            if (httpURLConnection2 != null) {
                                httpURLConnection2.disconnect();
                            }
                            this.g.onSuccess(this.c);
                        } catch (Exception e2) {
                            e = e2;
                            if (Logger.debugEnable()) {
                                e.printStackTrace();
                            } else {
                                Logger.logW("HttpClient", e.getMessage());
                            }
                            Log.e("HttpClient", "Catch exception. INNER_ERROR", e);
                            this.g.onFailed(HttpStateError.INNER_ERROR);
                            if (r1 != 0 && bufferedReader2 != null) {
                                bufferedReader2.close();
                                r1.close();
                            }
                            HttpURLConnection httpURLConnection3 = this.a;
                            if (httpURLConnection3 == null) {
                                return;
                            }
                            httpURLConnection3.disconnect();
                        }
                    } catch (Exception e3) {
                        bufferedReader2 = null;
                        e = e3;
                    } catch (Throwable th3) {
                        bufferedReader = null;
                        th = th3;
                        inputStream = r1;
                        if (inputStream != null && bufferedReader != null) {
                            bufferedReader.close();
                            inputStream.close();
                        }
                        HttpURLConnection httpURLConnection4 = this.a;
                        if (httpURLConnection4 != null) {
                            httpURLConnection4.disconnect();
                        }
                        throw th;
                    }
                } catch (Exception e4) {
                    bufferedReader2 = null;
                    e = e4;
                    r1 = 0;
                } catch (Throwable th4) {
                    bufferedReader = null;
                    th = th4;
                    inputStream = null;
                }
            } catch (Exception e5) {
                if (Logger.debugEnable()) {
                    e5.printStackTrace();
                } else {
                    Logger.logW("HttpClient", e5.getMessage());
                }
                Log.e("HttpClient", "Catch connection exception, INNER_ERROR", e5);
                this.g.onFailed(HttpStateError.INNER_ERROR);
            }
        }
    }

    public void setMaxTimeOut(int i) {
        this.d = i;
    }

    public void setReadTimeOut(int i) {
        this.e = i;
    }
}
