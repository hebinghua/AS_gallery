package com.miui.gallery.util.deviceprovider;

import cn.kuaipan.android.kss.download.DownloadDescriptorFile;
import cn.kuaipan.android.kss.upload.UploadDescriptorFile;
import com.miui.gallery.cloud.GalleryMiCloudServerException;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.util.deviceprovider.MiCloudProviderInterface;
import com.xiaomi.micloudsdk.cloudinfo.utils.CloudInfoUtils;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.micloudsdk.file.MiCloudFileMaster;
import com.xiaomi.micloudsdk.request.CloudHttpClient;
import com.xiaomi.micloudsdk.request.utils.Request;
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
import miui.cloud.MiCloudCompat;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

/* loaded from: classes2.dex */
public class BigMiCloudProvider implements MiCloudProviderInterface {
    public static final MiCloudProviderInterface.GalleryCloudCoder sCloudCoder = new MiCloudProviderInterface.GalleryCloudCoder() { // from class: com.miui.gallery.util.deviceprovider.BigMiCloudProvider.1
    };
    public static final MiCloudProviderInterface.GalleryCloudManager sCloudManager = new MiCloudProviderInterface.GalleryCloudManager() { // from class: com.miui.gallery.util.deviceprovider.BigMiCloudProvider.2
        @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface.GalleryCloudManager
        public String getUserAgent() {
            return CloudInfoUtils.getUserAgent();
        }

        @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface.GalleryCloudManager
        public String getGalleryHost() {
            return MiCloudCompat.GALLERY_HOST;
        }

        @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface.GalleryCloudManager
        public String getGalleryAnonymousHost() {
            return MiCloudCompat.GALLERY_ANONYMOUS_HOST;
        }

        @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface.GalleryCloudManager
        public String getFaceHost() {
            return MiCloudCompat.FACE_HOST;
        }

        @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface.GalleryCloudManager
        public String getSearchHost() {
            return MiCloudCompat.SEARCH_HOST;
        }

        @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface.GalleryCloudManager
        public String getSearchAnonymousHost() {
            return MiCloudCompat.SEARCH_ANONYMOUS_HOST;
        }

        @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface.GalleryCloudManager
        public String getVipStatusHost() {
            return MiCloudCompat.VIP_STATUS_HOST;
        }

        @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface.GalleryCloudManager
        public String getGalleryH5() {
            return MiCloudCompat.GALLERY_H5;
        }
    };

    @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface
    public HttpClient getHttpClient() {
        return CloudHttpClient.newInstance();
    }

    @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface
    public MiCloudProviderInterface.GalleryCloudManager getCloudManager() {
        return sCloudManager;
    }

    @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface
    public void doFileSDKUpload(MiCloudFileMaster<RequestCloudItem> miCloudFileMaster, RequestCloudItem requestCloudItem, UploadDescriptorFile uploadDescriptorFile, MiCloudFileListener miCloudFileListener) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        miCloudFileMaster.upload(requestCloudItem, uploadDescriptorFile, miCloudFileListener);
    }

    @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface
    public void doFileSDKDownload(MiCloudFileMaster<RequestCloudItem> miCloudFileMaster, RequestCloudItem requestCloudItem, DownloadDescriptorFile downloadDescriptorFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        miCloudFileMaster.download((MiCloudFileMaster<RequestCloudItem>) requestCloudItem, downloadDescriptorFile, miCloudFileListener, miCloudTransferStopper);
    }

    @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface
    public String secureGet(String str, Map<String, String> map) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, ClientProtocolException, IOException, GalleryMiCloudServerException {
        try {
            return Request.secureGet(str, map);
        } catch (CloudServerException e) {
            throw new GalleryMiCloudServerException(e);
        }
    }

    @Override // com.miui.gallery.util.deviceprovider.MiCloudProviderInterface
    public String securePost(String str, Map<String, String> map) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, ClientProtocolException, IOException, GalleryMiCloudServerException {
        try {
            return Request.securePost(str, map);
        } catch (CloudServerException e) {
            throw new GalleryMiCloudServerException(e);
        }
    }
}
