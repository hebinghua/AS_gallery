package com.miui.gallery.hybrid.hybridclient;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.R;
import com.miui.gallery.activity.ExternalPhotoPageActivity;
import com.miui.gallery.hybrid.GalleryHybridFragment;
import com.miui.gallery.hybrid.hybridclient.GalleryHybridClient;
import com.miui.gallery.hybrid.hybridclient.HybridClient;
import com.miui.gallery.hybrid.hybridclient.wrapper.WebChromeClientWrapper;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.request.HostManager;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class OperationHybridClient extends GalleryHybridClient {
    public int mMaxSelectCount;
    public ValueCallback<Uri[]> mValueCallback;

    @Override // com.miui.gallery.hybrid.hybridclient.GalleryHybridClient, com.miui.gallery.hybrid.hybridclient.HybridClient
    public boolean isSupportPullToRefresh() {
        return false;
    }

    public OperationHybridClient(Context context, String str) {
        super(context, str);
    }

    @Override // com.miui.gallery.hybrid.hybridclient.GalleryHybridClient, com.miui.gallery.hybrid.hybridclient.HybridClient
    public WebChromeClientWrapper getWebChromeClient(WebChromeClient webChromeClient) {
        return new OperationWebChromeClientWrapper(webChromeClient);
    }

    @Override // com.miui.gallery.hybrid.hybridclient.GalleryHybridClient, com.miui.gallery.hybrid.hybridclient.HybridClient
    public List<HybridClient.JsInterfacePair> getJavascriptInterfaces() {
        List<HybridClient.JsInterfacePair> javascriptInterfaces = super.getJavascriptInterfaces();
        if (javascriptInterfaces == null) {
            javascriptInterfaces = new ArrayList<>(1);
        }
        javascriptInterfaces.add(new HybridClient.JsInterfacePair("MiuiGalleryOperationStoryJSBridge", new OperationStoryJSBridge()));
        return javascriptInterfaces;
    }

    @Override // com.miui.gallery.hybrid.hybridclient.GalleryHybridClient, com.miui.gallery.hybrid.hybridclient.HybridClient
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 1) {
            return;
        }
        if (i2 == -1) {
            receiveImages(intent);
        } else {
            receiveImages(null);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void receiveImages(android.content.Intent r6) {
        /*
            r5 = this;
            r0 = 0
            if (r6 == 0) goto L35
            android.content.ClipData r1 = r6.getClipData()
            android.net.Uri r6 = r6.getData()
            r2 = 0
            if (r1 == 0) goto L2d
            int r6 = r1.getItemCount()
            android.net.Uri[] r3 = new android.net.Uri[r6]
            int r4 = r5.mMaxSelectCount
            int r6 = java.lang.Math.min(r6, r4)
        L1a:
            if (r2 >= r6) goto L36
            android.content.ClipData$Item r4 = r1.getItemAt(r2)
            android.net.Uri r4 = r4.getUri()
            android.net.Uri r4 = r5.resolveUri(r4)
            r3[r2] = r4
            int r2 = r2 + 1
            goto L1a
        L2d:
            if (r6 == 0) goto L35
            r1 = 1
            android.net.Uri[] r3 = new android.net.Uri[r1]
            r3[r2] = r6
            goto L36
        L35:
            r3 = r0
        L36:
            int r6 = android.os.Build.VERSION.SDK_INT
            r1 = 21
            if (r6 < r1) goto L45
            android.webkit.ValueCallback<android.net.Uri[]> r6 = r5.mValueCallback
            if (r6 == 0) goto L43
            r6.onReceiveValue(r3)
        L43:
            r5.mValueCallback = r0
        L45:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.hybrid.hybridclient.OperationHybridClient.receiveImages(android.content.Intent):void");
    }

    public final Uri resolveUri(Uri uri) {
        return uri.getAuthority().equals("com.miui.gallery.open") ? Uri.fromFile(new File(uri.getLastPathSegment())) : uri;
    }

    @Override // com.miui.gallery.hybrid.hybridclient.GalleryHybridClient, com.miui.gallery.hybrid.hybridclient.HybridClient
    public Permission[] getRuntimePermissions() {
        return new Permission[]{new Permission("android.permission.WRITE_EXTERNAL_STORAGE", ResourceUtils.getString(R.string.permission_storage_desc), true)};
    }

    /* loaded from: classes2.dex */
    public class OperationWebChromeClientWrapper extends GalleryHybridClient.GalleryWebChromeClientWrapper {
        public OperationWebChromeClientWrapper(WebChromeClient webChromeClient) {
            super(webChromeClient);
        }

        @Override // android.webkit.WebChromeClient
        @TargetApi(21)
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            OperationHybridClient operationHybridClient = OperationHybridClient.this;
            if (operationHybridClient.mContext != null) {
                operationHybridClient.mValueCallback = valueCallback;
                if (fileChooserParams.getMode() == 0) {
                    OperationHybridClient.this.mMaxSelectCount = 1;
                    startPicker(OperationHybridClient.this.mMaxSelectCount);
                } else {
                    OperationHybridClient.this.mMaxSelectCount = 20;
                    getFileChooserMaxImageCount(webView, new GalleryHybridClient.ImageCountGotCallback() { // from class: com.miui.gallery.hybrid.hybridclient.OperationHybridClient.OperationWebChromeClientWrapper.1
                        @Override // com.miui.gallery.hybrid.hybridclient.GalleryHybridClient.ImageCountGotCallback
                        public void onGetMaxImageCount(int i) {
                            if (i > 0) {
                                OperationHybridClient.this.mMaxSelectCount = Math.min(i, 20);
                            }
                            OperationWebChromeClientWrapper operationWebChromeClientWrapper = OperationWebChromeClientWrapper.this;
                            operationWebChromeClientWrapper.startPicker(OperationHybridClient.this.mMaxSelectCount);
                        }
                    });
                }
                return true;
            }
            DefaultLogger.d("OperationHybridClient", "onShowFileChooser: already detached, do nothing");
            return false;
        }

        public void getFileChooserMaxImageCount(WebView webView, final GalleryHybridClient.ImageCountGotCallback imageCountGotCallback) {
            OperationHybridClient.this.callJsMethod(webView, "javascript:getMaxImageCount()", new ValueCallback<String>() { // from class: com.miui.gallery.hybrid.hybridclient.OperationHybridClient.OperationWebChromeClientWrapper.2
                @Override // android.webkit.ValueCallback
                public void onReceiveValue(String str) {
                    int i;
                    DefaultLogger.d("OperationHybridClient", "get max image count from html:%s", str);
                    try {
                        i = Integer.parseInt(str);
                    } catch (NumberFormatException e) {
                        DefaultLogger.e("OperationHybridClient", e);
                        i = 0;
                    }
                    GalleryHybridClient.ImageCountGotCallback imageCountGotCallback2 = imageCountGotCallback;
                    if (imageCountGotCallback2 != null) {
                        imageCountGotCallback2.onGetMaxImageCount(i);
                    }
                }
            });
        }

        public void startPicker(int i) {
            if (OperationHybridClient.this.mWebViewFragment != null) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("pick-upper-bound", i);
                intent.putExtra("pick_close_type", 3);
                intent.setPackage("com.miui.gallery");
                OperationHybridClient.this.mWebViewFragment.startActivityForResult(intent, 1);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class OperationStoryJSBridge {
        public OperationStoryJSBridge() {
        }

        @JavascriptInterface
        public boolean saveImage(String str) {
            GalleryHybridClient.ImageInfo imageInfo;
            String str2;
            OperationHybridClient.this.recordSaveEvent("save_click");
            if (HostManager.isInternalUrl(OperationHybridClient.this.mCurrentUrl) && (imageInfo = (GalleryHybridClient.ImageInfo) GsonUtils.fromJson(str, (Class<Object>) GalleryHybridClient.ImageInfo.class)) != null && (str2 = imageInfo.data) != null) {
                try {
                    byte[] decode = Base64.decode(str2, 0);
                    Bitmap decodeByteArray = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                    if (decodeByteArray == null) {
                        DefaultLogger.d("OperationHybridClient", "save image fail,no bitmap got from web");
                        return false;
                    }
                    DefaultLogger.d("OperationHybridClient", "save image from html,image size: %d X %d", Integer.valueOf(decodeByteArray.getWidth()), Integer.valueOf(decodeByteArray.getHeight()));
                    String format = String.format(Locale.US, "IMG_%s.jpg", DateFormat.format("yyyyMMdd_HHmmss", System.currentTimeMillis()));
                    String pathInPriorStorage = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE + File.separator + format);
                    if (pathInPriorStorage == null) {
                        return false;
                    }
                    if (!OperationHybridClient.this.saveBitmapToOutputStream(pathInPriorStorage, decodeByteArray)) {
                        ToastUtils.makeText(OperationHybridClient.this.mContext, (int) R.string.main_save_error_msg);
                        DefaultLogger.d("OperationHybridClient", "save image fail,bitmap compress error");
                        return false;
                    }
                    OperationHybridClient operationHybridClient = OperationHybridClient.this;
                    operationHybridClient.callJsMethod(operationHybridClient.mWebView, "javascript:onImageSaveResult()", null);
                    ScannerEngine.getInstance().scanFile(OperationHybridClient.this.mContext, pathInPriorStorage, 13);
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(pathInPriorStorage, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("OperationHybridClient", "saveImage"));
                    if (documentFile != null) {
                        StorageSolutionProvider.get().apply(documentFile);
                    }
                    OperationHybridClient.this.recordSaveEvent("save_success");
                    OperationHybridClient.this.gotoPhotoPage(pathInPriorStorage);
                    return true;
                } catch (Throwable th) {
                    DefaultLogger.e("OperationHybridClient", "saveImage() failed %s", th);
                }
            }
            return false;
        }
    }

    public final boolean saveBitmapToOutputStream(String str, Bitmap bitmap) {
        DocumentFile documentFile;
        boolean z = false;
        OutputStream outputStream = null;
        try {
            documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT, FileHandleRecordHelper.appendInvokerTag("OperationHybridClient", "saveBitmapToOutputStream"));
        } finally {
            try {
                return z;
            } finally {
            }
        }
        if (documentFile == null) {
            return false;
        }
        outputStream = StorageSolutionProvider.get().openOutputStream(documentFile);
        z = GalleryUtils.saveBitmapToOutputStream(bitmap, Bitmap.CompressFormat.JPEG, outputStream);
        return z;
    }

    public final void gotoPhotoPage(String str) {
        GalleryHybridFragment galleryHybridFragment = this.mWebViewFragment;
        if (galleryHybridFragment == null || galleryHybridFragment.getActivity() == null) {
            return;
        }
        Intent intent = new Intent(this.mWebViewFragment.getActivity(), ExternalPhotoPageActivity.class);
        intent.setData(Uri.fromFile(new File(str)));
        this.mWebViewFragment.startActivity(intent);
    }

    public void recordSaveEvent(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("save_detail", str);
        SamplingStatHelper.recordCountEvent("assistant", "assistant_operation_card_image_save_success", hashMap);
    }
}
