package com.miui.gallery.util.deviceprovider;

import cn.kuaipan.android.kss.download.DownloadDescriptorFile;
import cn.kuaipan.android.kss.upload.UploadDescriptorFile;
import com.miui.gallery.cloud.GalleryMiCloudServerException;
import com.miui.gallery.cloud.RequestCloudItem;
import com.xiaomi.micloudsdk.file.MiCloudFileMaster;
import com.xiaomi.opensdk.exception.AuthenticationException;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.opensdk.exception.UnretriableException;
import com.xiaomi.opensdk.file.model.MiCloudFileListener;
import com.xiaomi.opensdk.file.model.MiCloudTransferStopper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

/* loaded from: classes2.dex */
public interface MiCloudProviderInterface {

    /* loaded from: classes2.dex */
    public interface GalleryCloudCoder {
    }

    /* loaded from: classes2.dex */
    public interface GalleryCloudManager {
        String getFaceHost();

        String getGalleryAnonymousHost();

        String getGalleryH5();

        String getGalleryHost();

        String getSearchAnonymousHost();

        String getSearchHost();

        String getUserAgent();

        String getVipStatusHost();
    }

    void doFileSDKDownload(MiCloudFileMaster<RequestCloudItem> miCloudFileMaster, RequestCloudItem requestCloudItem, DownloadDescriptorFile downloadDescriptorFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException;

    void doFileSDKUpload(MiCloudFileMaster<RequestCloudItem> miCloudFileMaster, RequestCloudItem requestCloudItem, UploadDescriptorFile uploadDescriptorFile, MiCloudFileListener miCloudFileListener) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException;

    GalleryCloudManager getCloudManager();

    HttpClient getHttpClient();

    String secureGet(String str, Map<String, String> map) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, ClientProtocolException, IOException, GalleryMiCloudServerException;

    String securePost(String str, Map<String, String> map) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, ClientProtocolException, IOException, GalleryMiCloudServerException;
}
