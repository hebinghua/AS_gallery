package com.market.sdk.utils;

import android.text.TextUtils;
import ch.qos.logback.classic.spi.CallerData;
import com.xiaomi.stat.b.h;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class Connection {
    public boolean mIsBackground;
    public boolean mNeedBaseParameter;
    public boolean mNeedHosted;
    public boolean mNeedId;
    public boolean mNeedSessionID;
    public Parameter mParameter;
    public JSONObject mResponse;
    public URL mUrl;
    public boolean mUseGet;

    /* loaded from: classes.dex */
    public class ConnectionException extends Exception {
        private static final long serialVersionUID = 1;
        public NetworkError mError;
        public final /* synthetic */ Connection this$0;
    }

    /* loaded from: classes.dex */
    public enum NetworkError {
        OK,
        URL_ERROR,
        NETWORK_ERROR,
        AUTH_ERROR,
        CLIENT_ERROR,
        SERVER_ERROR,
        RESULT_ERROR,
        UNKNOWN_ERROR
    }

    public HttpURLConnection onConnectionCreated(HttpURLConnection httpURLConnection) throws ConnectionException {
        return httpURLConnection;
    }

    public Parameter onQueryCreated(Parameter parameter) throws ConnectionException {
        return parameter;
    }

    public String onURLCreated(String str, Parameter parameter) throws ConnectionException {
        return str;
    }

    public Connection(String str) {
        this(str, false);
    }

    public Connection(String str, boolean z) {
        URL url;
        try {
            url = new URL(str);
        } catch (MalformedURLException e) {
            Log.e("MarketConnection", "URL error: " + e);
            url = null;
        }
        init(url);
        this.mIsBackground = z;
    }

    public static String connect(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        if (TextUtils.isEmpty(str2)) {
            return str;
        }
        if (str.charAt(str.length() - 1) == '/') {
            str = str.substring(0, str.length() - 1);
        }
        if (str2.charAt(0) == '/') {
            str2 = str2.substring(1);
        }
        return str + h.g + str2;
    }

    public final void init(URL url) {
        this.mNeedBaseParameter = true;
        this.mUseGet = false;
        this.mNeedHosted = true;
        this.mNeedId = true;
        this.mNeedSessionID = true;
        if (checkURL(url)) {
            this.mUrl = url;
        }
    }

    public JSONObject getResponse() {
        return this.mResponse;
    }

    public NetworkError requestJSON() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        NetworkError request = request(new MemoryResetableOutputStream(byteArrayOutputStream));
        try {
            try {
                if (request == NetworkError.OK) {
                    this.mResponse = new JSONObject(byteArrayOutputStream.toString());
                } else {
                    Log.e("MarketConnection", "Connection failed : " + request);
                }
                try {
                    byteArrayOutputStream.close();
                } catch (IOException unused) {
                }
                return request;
            } catch (JSONException e) {
                Log.e("MarketConnection", "JSON error: " + e);
                NetworkError networkError = NetworkError.RESULT_ERROR;
                try {
                    byteArrayOutputStream.close();
                } catch (IOException unused2) {
                }
                return networkError;
            }
        } catch (Throwable th) {
            try {
                byteArrayOutputStream.close();
            } catch (IOException unused3) {
            }
            throw th;
        }
    }

    public NetworkError request(ResetableOutputStream resetableOutputStream) {
        if (this.mUrl == null) {
            return NetworkError.URL_ERROR;
        }
        if (!Utils.isConnected(AppGlobal.getContext())) {
            return NetworkError.NETWORK_ERROR;
        }
        if (this.mParameter == null) {
            this.mParameter = new Parameter(this);
        }
        try {
            Parameter onQueryCreated = onQueryCreated(this.mParameter);
            String url = this.mUrl.toString();
            if (this.mUseGet && !onQueryCreated.isEmpty()) {
                String query = this.mUrl.getQuery();
                String url2 = this.mUrl.toString();
                if (TextUtils.isEmpty(query)) {
                    url = url2 + CallerData.NA + onQueryCreated.toString();
                } else {
                    url = url2 + "&" + onQueryCreated.toString();
                }
            }
            try {
                String onURLCreated = onURLCreated(url, onQueryCreated);
                if (Utils.DEBUG) {
                    Log.d("MarketConnection", "connection url: " + onURLCreated);
                }
                String parameter = !this.mUseGet ? onQueryCreated.toString() : "";
                long currentTimeMillis = System.currentTimeMillis();
                NetworkError innerRequest = innerRequest(onURLCreated, parameter, this.mUseGet, false, resetableOutputStream);
                if (Utils.DEBUG) {
                    long currentTimeMillis2 = System.currentTimeMillis();
                    Log.d("MarketConnection", "Time(ms) spent in request: " + (currentTimeMillis2 - currentTimeMillis) + ", " + onURLCreated);
                }
                return innerRequest;
            } catch (ConnectionException e) {
                return e.mError;
            }
        } catch (ConnectionException e2) {
            return e2.mError;
        }
    }

    public final NetworkError innerRequest(String str, String str2, boolean z, boolean z2, ResetableOutputStream resetableOutputStream) {
        HttpURLConnection httpURLConnection;
        Exception e;
        HttpURLConnection httpURLConnection2;
        BufferedInputStream bufferedInputStream;
        Throwable th;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str3 = (String) it.next();
            if (Utils.DEBUG) {
                Log.d("MarketConnection", "hosted connection url: " + str3);
            }
            try {
                URL url = new URL(str3);
                HttpURLConnection httpURLConnection3 = null;
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    try {
                        try {
                            httpURLConnection.setConnectTimeout(10000);
                            if (Utils.isWifiConnected(AppGlobal.getContext())) {
                                httpURLConnection.setReadTimeout(10000);
                            } else {
                                httpURLConnection.setReadTimeout(30000);
                            }
                            if (z) {
                                httpURLConnection.setRequestMethod("GET");
                                httpURLConnection.setDoOutput(false);
                            } else {
                                httpURLConnection.setRequestMethod("POST");
                                httpURLConnection.setDoOutput(true);
                            }
                            try {
                                httpURLConnection = onConnectionCreated(httpURLConnection);
                                httpURLConnection.connect();
                                if (!z && !TextUtils.isEmpty(str2)) {
                                    OutputStream outputStream = httpURLConnection.getOutputStream();
                                    outputStream.write(str2.getBytes());
                                    if (Utils.DEBUG) {
                                        Log.d("MarketConnection", "[post]" + str2);
                                    }
                                    outputStream.close();
                                }
                                NetworkError handleResponseCode = handleResponseCode(httpURLConnection.getResponseCode());
                                if (handleResponseCode == NetworkError.OK && resetableOutputStream != null) {
                                    try {
                                        bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream(), 8192);
                                        try {
                                            try {
                                                byte[] bArr = new byte[1024];
                                                while (true) {
                                                    int read = bufferedInputStream.read(bArr, 0, 1024);
                                                    if (read <= 0) {
                                                        break;
                                                    }
                                                    resetableOutputStream.write(bArr, 0, read);
                                                }
                                                resetableOutputStream.flush();
                                                bufferedInputStream.close();
                                            } catch (Exception e3) {
                                                e2 = e3;
                                                Log.e("MarketConnection", "Connection Exception for " + url.getHost() + " : read file stream error " + e2);
                                                resetableOutputStream.reset();
                                                if (bufferedInputStream != null) {
                                                    bufferedInputStream.close();
                                                }
                                                httpURLConnection.disconnect();
                                            }
                                        } catch (Throwable th2) {
                                            th = th2;
                                            if (bufferedInputStream != null) {
                                                bufferedInputStream.close();
                                            }
                                            throw th;
                                        }
                                    } catch (Exception e4) {
                                        bufferedInputStream = null;
                                        e2 = e4;
                                    } catch (Throwable th3) {
                                        bufferedInputStream = null;
                                        th = th3;
                                    }
                                }
                                httpURLConnection.disconnect();
                                return handleResponseCode;
                            } catch (ConnectionException e5) {
                                NetworkError networkError = e5.mError;
                                httpURLConnection2.disconnect();
                                return networkError;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            httpURLConnection3 = httpURLConnection;
                            if (httpURLConnection3 != null) {
                                httpURLConnection3.disconnect();
                            }
                            throw th;
                        }
                    } catch (Exception e6) {
                        e = e6;
                        Log.e("MarketConnection", "Connection Exception for " + url.getHost() + " :" + e);
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    }
                } catch (Exception e7) {
                    httpURLConnection = null;
                    e = e7;
                } catch (Throwable th5) {
                    th = th5;
                }
            } catch (MalformedURLException e8) {
                Log.e("MarketConnection", " URL error :" + e8);
            }
        }
        return NetworkError.NETWORK_ERROR;
    }

    public boolean checkURL(URL url) {
        if (url == null) {
            return false;
        }
        String protocol = url.getProtocol();
        return TextUtils.equals(protocol, "http") || TextUtils.equals(protocol, "https");
    }

    public final NetworkError handleResponseCode(int i) {
        if (i == 200) {
            return NetworkError.OK;
        }
        Log.e("MarketConnection", "Network Error : " + i);
        return NetworkError.SERVER_ERROR;
    }

    /* loaded from: classes.dex */
    public class Parameter {
        public TreeMap<String, String> params;

        public Parameter(Connection connection) {
            this(true);
        }

        public Parameter(boolean z) {
            this.params = new TreeMap<>();
            if (z) {
                Connection.this.mParameter = this;
            }
        }

        public Parameter add(String str, String str2) {
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                this.params.put(str, str2);
            }
            return this;
        }

        public boolean isEmpty() {
            return this.params.isEmpty();
        }

        public String toString() {
            if (this.params.isEmpty()) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (String str : this.params.keySet()) {
                sb.append(str);
                sb.append("=");
                try {
                    sb.append(URLEncoder.encode(this.params.get(str), Keyczar.DEFAULT_ENCODING));
                } catch (UnsupportedEncodingException unused) {
                }
                sb.append("&");
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
    }

    /* loaded from: classes.dex */
    public abstract class ResetableOutputStream extends OutputStream {
        public OutputStream mOutputStream;

        public abstract void reset();

        public ResetableOutputStream(OutputStream outputStream) {
            if (outputStream == null) {
                throw new IllegalArgumentException("outputstream is null");
            }
            this.mOutputStream = outputStream;
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.mOutputStream.close();
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            this.mOutputStream.flush();
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr) throws IOException {
            this.mOutputStream.write(bArr);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i, int i2) throws IOException {
            this.mOutputStream.write(bArr, i, i2);
        }

        @Override // java.io.OutputStream
        public void write(int i) throws IOException {
            this.mOutputStream.write(i);
        }
    }

    /* loaded from: classes.dex */
    public class MemoryResetableOutputStream extends ResetableOutputStream {
        public MemoryResetableOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
            super(byteArrayOutputStream);
        }

        @Override // com.market.sdk.utils.Connection.ResetableOutputStream
        public void reset() {
            ((ByteArrayOutputStream) this.mOutputStream).reset();
        }
    }
}
