package com.xiaomi.micloudsdk.request.utils;

import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.micloudsdk.exception.CipherException;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.micloudsdk.stat.GetDownloadFileUrlFailedStatParam;
import com.xiaomi.micloudsdk.stat.MiCloudNetEventStatInjector;
import com.xiaomi.micloudsdk.stat.MiCloudStatManager;
import com.xiaomi.micloudsdk.stat.NetFailedStatParam;
import com.xiaomi.micloudsdk.stat.NetSuccessStatParam;
import com.xiaomi.micloudsdk.utils.AESCoder;
import com.xiaomi.micloudsdk.utils.CryptCoder;
import com.xiaomi.stat.d;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class CloudRequestHelper {
    public static String httpPostRequestWithDecodeData(String str, UrlEncodedFormEntity urlEncodedFormEntity, Header header, List<Header> list, CryptCoder cryptCoder) throws CloudServerException, IOException {
        String httpPostRequest = httpPostRequest(str, urlEncodedFormEntity, urlEncodedFormEntity.getContentType().getValue(), header, list, cryptCoder, 0);
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", httpPostRequest);
        }
        return httpPostRequest;
    }

    public static String httpPostJSONRequestWithDecodeData(String str, StringEntity stringEntity, Header header, List<Header> list, CryptCoder cryptCoder) throws CloudServerException, IOException {
        String httpPostRequest = httpPostRequest(str, stringEntity, "application/json", header, list, cryptCoder, 0);
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", httpPostRequest);
        }
        return httpPostRequest;
    }

    public static String httpPostRequest(String str, HttpEntity httpEntity, String str2, Header header, List<Header> list, CryptCoder cryptCoder, int i) throws IOException, CloudServerException {
        HttpPost httpPost = new HttpPost(str);
        if (httpEntity != null) {
            httpPost.setEntity(httpEntity);
        }
        addHttpRequestHeaders(httpPost, str2, header, list, i);
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", "http post url : " + str);
            StringBuilder sb = new StringBuilder();
            sb.append("http post cookies : ");
            sb.append(header != null ? header.toString() : "null");
            Log.d("Micloud", sb.toString());
            Log.d("Micloud", "http post additionalHeaders : " + list);
        }
        HttpResponse excuteInternal = excuteInternal(httpPost);
        int statusCode = excuteInternal.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            try {
                String decrypt = cryptCoder.decrypt(decodeGZipToString(excuteInternal));
                String checkRedirect = CloudRelocationUtils.checkRedirect(decrypt, i);
                return !TextUtils.isEmpty(checkRedirect) ? httpPostRequest(checkRedirect, httpEntity, str2, header, list, cryptCoder, i + 1) : decrypt;
            } catch (CipherException e) {
                Log.e("Micloud", "MiCloudServerException", e);
                throw new CloudServerException(0, excuteInternal);
            }
        } else if (CloudServerException.isMiCloudServerException(statusCode)) {
            Log.e("Micloud", "MiCloudServerException: " + statusCode + " " + excuteInternal.getStatusLine());
            throw new CloudServerException(statusCode, excuteInternal);
        } else {
            Log.e("Micloud", "Server error: " + statusCode + " " + excuteInternal.getStatusLine());
            throw new IOException("Server error: " + statusCode + " " + excuteInternal.getStatusLine());
        }
    }

    public static String httpGetRequestWithDecodeData(String str, Header header, List<Header> list, CryptCoder cryptCoder) throws CloudServerException, IOException {
        String httpGetRequest = httpGetRequest(str, header, list, cryptCoder, 0);
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", httpGetRequest);
        }
        return httpGetRequest;
    }

    public static String httpGetRequest(String str, Header header, List<Header> list, CryptCoder cryptCoder, int i) throws IOException, CloudServerException {
        HttpGet httpGet = new HttpGet(str);
        addHttpRequestHeaders(httpGet, null, header, list, i);
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", "http get url : " + str);
            StringBuilder sb = new StringBuilder();
            sb.append("http get cookies : ");
            sb.append(header != null ? header.toString() : "null");
            Log.d("Micloud", sb.toString());
            Log.d("Micloud", "http get additionalHeaders : " + list);
        }
        HttpResponse excuteInternal = excuteInternal(httpGet);
        int statusCode = excuteInternal.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            try {
                String decrypt = cryptCoder.decrypt(decodeGZipToString(excuteInternal));
                String checkRedirect = CloudRelocationUtils.checkRedirect(decrypt, i);
                if (!TextUtils.isEmpty(checkRedirect)) {
                    return httpGetRequest(checkRedirect, header, list, cryptCoder, i + 1);
                }
                statGetDownloadFileUrlsFailedEventIfCloudResponseCodeError(str, httpGet.getMethod(), statusCode, decrypt);
                return decrypt;
            } catch (CipherException e) {
                Log.e("Micloud", "MiCloudServerException", e);
                statGetDownloadFileUrlsFailedEvent(str, httpGet.getMethod(), statusCode, e.getClass().getSimpleName(), "");
                throw new CloudServerException(0, excuteInternal);
            }
        }
        statGetDownloadFileUrlsFailedEvent(str, httpGet.getMethod(), statusCode, "", "");
        if (CloudServerException.isMiCloudServerException(statusCode)) {
            Log.e("Micloud", "MiCloudServerException: " + statusCode + " " + excuteInternal.getStatusLine());
            throw new CloudServerException(statusCode, excuteInternal);
        }
        Log.e("Micloud", "Server error: " + statusCode + " " + excuteInternal.getStatusLine());
        throw new IOException("Server error: " + statusCode + " " + excuteInternal.getStatusLine());
    }

    public static void addHttpRequestHeaders(HttpRequestBase httpRequestBase, String str, Header header, List<Header> list, int i) {
        if (!TextUtils.isEmpty(str)) {
            httpRequestBase.setHeader("Content-Type", str);
        }
        if (header != null) {
            httpRequestBase.setHeader(header);
        }
        httpRequestBase.setHeader("Accept", "application/json");
        httpRequestBase.setHeader("Accept-Encoding", d.aj);
        if (i > 0) {
            httpRequestBase.setHeader("X-XIAOMI-REDIRECT-COUNT", i + "");
        }
        httpRequestBase.setHeader("X-XIAOMI-SUPPORT-REDIRECT", "true, https");
        for (Header header2 : list) {
            httpRequestBase.setHeader(header2);
        }
    }

    public static HttpResponse excuteInternal(HttpRequestBase httpRequestBase) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        try {
        } catch (IOException e) {
            e = e;
        }
        try {
            HttpResponse execute = HttpUtils.getHttpClient().execute(httpRequestBase);
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            long j = 0;
            if (execute.getEntity() != null) {
                j = execute.getEntity().getContentLength();
            }
            long j2 = j;
            int statusCode = execute.getStatusLine().getStatusCode();
            MiCloudStatManager.getInstance().addHttpEvent(httpRequestBase.getURI().toString(), currentTimeMillis2, j2, statusCode, null);
            MiCloudNetEventStatInjector.getInstance().addNetSuccessEvent(new NetSuccessStatParam(httpRequestBase.getURI().toString(), currentTimeMillis, currentTimeMillis2, j2, statusCode, 0));
            return execute;
        } catch (IOException e2) {
            e = e2;
            MiCloudStatManager.getInstance().addHttpEvent(httpRequestBase.getURI().toString(), -1L, 0L, -1, e.getClass().getSimpleName());
            MiCloudNetEventStatInjector.getInstance().addNetFailedEvent(new NetFailedStatParam(httpRequestBase.getURI().toString(), currentTimeMillis, System.currentTimeMillis() - currentTimeMillis, e, 0));
            statGetDownloadFileUrlsFailedEvent(httpRequestBase.getURI().toString(), httpRequestBase.getMethod(), -1, e.getClass().getSimpleName(), "");
            throw e;
        }
    }

    public static InputStream decodeGZip(HttpResponse httpResponse) throws IllegalStateException, IOException {
        Header firstHeader = httpResponse.getFirstHeader("Content-Encoding");
        if (firstHeader != null && firstHeader.getValue().equalsIgnoreCase(d.aj)) {
            return new GZIPInputStream(httpResponse.getEntity().getContent());
        }
        return httpResponse.getEntity().getContent();
    }

    public static String decodeGZipToString(HttpResponse httpResponse) throws IllegalStateException, IOException {
        InputStream decodeGZip = decodeGZip(httpResponse);
        try {
            return inputStreamToString(decodeGZip);
        } finally {
            if (decodeGZip != null) {
                decodeGZip.close();
            }
        }
    }

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Keyczar.DEFAULT_ENCODING));
        char[] cArr = new char[2048];
        while (true) {
            int read = bufferedReader.read(cArr, 0, 2048);
            if (read != -1) {
                stringBuffer.append(cArr, 0, read);
            } else {
                return stringBuffer.toString();
            }
        }
    }

    public static boolean isV4Url(String str) {
        return str.indexOf("/v4") != -1;
    }

    public static CryptCoder getCryptCoder(String str, String str2) {
        return isV4Url(str) ? new CloudAESWithIVCoder(str2) : new AESCoder(str2);
    }

    public static String encodeData(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("security is empty.");
        }
        if (TextUtils.isEmpty(str3)) {
            return "";
        }
        try {
            return getCryptCoder(str, str2).encrypt(str3);
        } catch (CipherException e) {
            Log.e("CloudRequestHelper", "encodeData failed:" + str3, e);
            return "";
        }
    }

    public static void statGetDownloadFileUrlsFailedEvent(String str, String str2, int i, String str3, String str4) {
        if (MiCloudNetEventStatInjector.getInstance().isGetDownloadFileRequestUrl(str, str2)) {
            MiCloudNetEventStatInjector.getInstance().addGetDownloadFileUrlsFailedEvent(GetDownloadFileUrlFailedStatParam.createGetDownloadFileUrlFailedStatParam(str, i, str3, str4));
        }
    }

    public static void statGetDownloadFileUrlsFailedEventIfCloudResponseCodeError(String str, String str2, int i, String str3) {
        if (!MiCloudNetEventStatInjector.getInstance().isGetDownloadFileRequestUrl(str, str2) || !HttpUtils.isHttpStatusCodeOKButCloudResponseCodeError(i, str3)) {
            return;
        }
        MiCloudNetEventStatInjector.getInstance().addGetDownloadFileUrlsFailedEvent(GetDownloadFileUrlFailedStatParam.createGetDownloadFileUrlFailedStatParam(str, i, "", str3));
    }
}
