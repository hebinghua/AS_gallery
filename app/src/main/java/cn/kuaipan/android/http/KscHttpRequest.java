package cn.kuaipan.android.http;

import android.net.Uri;
import android.util.Log;
import cn.kuaipan.android.http.multipart.ByteArrayValuePair;
import cn.kuaipan.android.http.multipart.FilePart;
import cn.kuaipan.android.http.multipart.FileValuePair;
import cn.kuaipan.android.http.multipart.MultipartEntity;
import cn.kuaipan.android.http.multipart.Part;
import cn.kuaipan.android.http.multipart.StringPart;
import com.xiaomi.stat.d;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.AbstractHttpEntity;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class KscHttpRequest {
    public final IKscDecoder mDecoder;
    public final IKscTransferListener mListener;
    public final HttpMethod mMethod;
    public AbstractHttpEntity mPostEntity;
    public final ArrayList<NameValuePair> mPostForm;
    public HttpUriRequest mRequest;
    public boolean mTryGzip;
    public Uri mUri;

    /* loaded from: classes.dex */
    public enum HttpMethod {
        GET,
        POST
    }

    public KscHttpRequest(HttpMethod httpMethod, String str, IKscDecoder iKscDecoder, IKscTransferListener iKscTransferListener) {
        this(httpMethod, Uri.parse(str), null, iKscDecoder, iKscTransferListener);
    }

    public KscHttpRequest(HttpMethod httpMethod, Uri uri, IKscDecoder iKscDecoder, IKscTransferListener iKscTransferListener) {
        this(httpMethod, uri, null, iKscDecoder, iKscTransferListener);
    }

    public KscHttpRequest(HttpMethod httpMethod, Uri uri, AbstractHttpEntity abstractHttpEntity, IKscDecoder iKscDecoder, IKscTransferListener iKscTransferListener) {
        this.mPostForm = new ArrayList<>();
        this.mTryGzip = false;
        this.mMethod = httpMethod;
        this.mUri = uri;
        this.mPostEntity = abstractHttpEntity;
        this.mDecoder = iKscDecoder;
        this.mListener = iKscTransferListener;
    }

    public final void checkRequest() {
        if (this.mRequest == null) {
            return;
        }
        throw new RuntimeException("HttpRequest has been created. All input can't be changed.");
    }

    public static ArrayList<NameValuePair> getMergedPostValue(AbstractHttpEntity abstractHttpEntity, List<NameValuePair> list) {
        ArrayList<NameValuePair> arrayList = new ArrayList<>();
        if (abstractHttpEntity != null) {
            try {
                arrayList.addAll(URLEncodedUtils.parse(abstractHttpEntity));
            } catch (IOException e) {
                Log.e("KscHttpRequest", "Failed parse an user entity.", e);
                throw new RuntimeException("Failed parse an user entity. The user entity should be parseable by URLEncodedUtils.parse(HttpEntity)", e);
            }
        }
        arrayList.addAll(list);
        return arrayList;
    }

    public void setPostEntity(AbstractHttpEntity abstractHttpEntity) {
        checkRequest();
        this.mPostEntity = abstractHttpEntity;
        if (!isFormEntity(abstractHttpEntity)) {
            this.mPostForm.clear();
        }
    }

    public HttpUriRequest getRequest() {
        if (this.mRequest == null) {
            this.mRequest = createHttpRequest();
        }
        return this.mRequest;
    }

    public final HttpUriRequest createHttpRequest() {
        if (!isValidUri(this.mUri)) {
            throw new IllegalArgumentException("Request uri is not valid. uri=" + this.mUri);
        }
        String uri = this.mUri.toString();
        HttpMethod httpMethod = this.mMethod;
        if (httpMethod == null) {
            httpMethod = (this.mPostEntity != null || !this.mPostForm.isEmpty()) ? HttpMethod.POST : HttpMethod.GET;
        }
        HttpPost httpPost = null;
        int i = AnonymousClass1.$SwitchMap$cn$kuaipan$android$http$KscHttpRequest$HttpMethod[httpMethod.ordinal()];
        if (i == 1) {
            httpPost = new HttpGet(uri);
            if (this.mPostEntity != null || !this.mPostForm.isEmpty()) {
                Log.w("KscHttpRequest", "Post data is not empty, but method is GET. All post data is lost.");
            }
        } else if (i == 2) {
            httpPost = new HttpPost(uri);
            if (!this.mPostForm.isEmpty()) {
                this.mPostEntity = makeFormEntity();
            }
            httpPost.setEntity(this.mPostEntity);
        }
        if (this.mTryGzip) {
            httpPost.setHeader("Accept-Encoding", d.aj);
        }
        this.mRequest = httpPost;
        return httpPost;
    }

    /* renamed from: cn.kuaipan.android.http.KscHttpRequest$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$cn$kuaipan$android$http$KscHttpRequest$HttpMethod;

        static {
            int[] iArr = new int[HttpMethod.values().length];
            $SwitchMap$cn$kuaipan$android$http$KscHttpRequest$HttpMethod = iArr;
            try {
                iArr[HttpMethod.GET.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$cn$kuaipan$android$http$KscHttpRequest$HttpMethod[HttpMethod.POST.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public final AbstractHttpEntity makeFormEntity() {
        AbstractHttpEntity abstractHttpEntity = this.mPostEntity;
        ArrayList<NameValuePair> arrayList = this.mPostForm;
        if (arrayList.isEmpty()) {
            return this.mPostEntity;
        }
        boolean z = true;
        boolean z2 = abstractHttpEntity != null && (abstractHttpEntity instanceof MultipartEntity);
        if (!z2) {
            Iterator<NameValuePair> it = arrayList.iterator();
            while (it.hasNext()) {
                NameValuePair next = it.next();
                if (!(next instanceof FileValuePair)) {
                    if (next instanceof ByteArrayValuePair) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        z = z2;
        if (z) {
            if (abstractHttpEntity != null && (abstractHttpEntity instanceof MultipartEntity)) {
                MultipartEntity multipartEntity = (MultipartEntity) abstractHttpEntity;
                multipartEntity.appendPart(toPartArray(arrayList));
                return multipartEntity;
            }
            return new MultipartEntity(toPartArray(getMergedPostValue(abstractHttpEntity, arrayList)));
        }
        try {
            return new UrlEncodedFormEntity(this.mPostForm, Keyczar.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            Log.e("KscHttpRequest", "JVM not support UTF_8?", e);
            throw new RuntimeException("JVM not support UTF_8?", e);
        }
    }

    public final boolean isValidUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    public IKscDecoder getDecoder() {
        return this.mDecoder;
    }

    public static boolean isFormEntity(AbstractHttpEntity abstractHttpEntity) {
        return abstractHttpEntity == null || (abstractHttpEntity instanceof MultipartEntity) || URLEncodedUtils.isEncoded(abstractHttpEntity);
    }

    public static Part[] toPartArray(List<NameValuePair> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int size = list.size();
        Part[] partArr = new Part[size];
        for (int i = 0; i < size; i++) {
            NameValuePair nameValuePair = list.get(i);
            if (nameValuePair instanceof FileValuePair) {
                try {
                    partArr[i] = new FilePart(nameValuePair.getName(), ((FileValuePair) nameValuePair).getFile());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("The file to be sent should be exist. file=" + ((FileValuePair) nameValuePair).getFile(), e);
                }
            } else if (nameValuePair instanceof ByteArrayValuePair) {
                partArr[i] = new FilePart(nameValuePair.getName(), nameValuePair.getValue(), ((ByteArrayValuePair) nameValuePair).getData());
            } else {
                partArr[i] = new StringPart(nameValuePair.getName(), nameValuePair.getValue(), Keyczar.DEFAULT_ENCODING);
            }
        }
        return partArr;
    }

    public IKscTransferListener getListener() {
        return this.mListener;
    }
}
