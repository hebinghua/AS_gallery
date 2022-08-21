package com.miui.gallery.request;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.util.Base64;
import androidx.fragment.app.FragmentActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.JsonObject;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.TextEditActivity;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.net.hardwareauth.DeviceCredentialManager;
import com.miui.gallery.net.hardwareauth.HardwareAuthTokenManager;
import com.miui.gallery.net.hardwareauth.OCRRelatedRequest;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import com.nexstreaming.nexeditorsdk.nexChecker;
import com.xiaomi.miai.api.Response;
import com.xiaomi.miai.api.StatusCode;
import com.xiaomi.miai.api.VisionOCR;
import com.xiaomi.miai.api.common.APIUtils;
import com.xiaomi.stat.d;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLHandshakeException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class OCRRequestHelper {
    public static String TAG = "OCRRequestHelper";
    public WeakReference<FragmentActivity> mActivityRef;
    public BaseDataItem mBaseDataItem;
    public WeakReference<OCRRequestListener> mListenerWeakReference;
    public OCRAsyncTask mOCRRequest;
    public WeakReference<PhotoView> mPhotoViewRef;
    @Deprecated
    public static String[] sSupportOCRDevices = {"cas", "cezanne", "cmi", "umi", "lmi", "lmipro", "vangogh", "gauguin", "gauguinpro", "cannon", "lancelot", "dandelion", "merlin", "atom", "bomb", "cepheus", "crux", "grus", "pyxis", "vela", "tucana", "lotus", "laurus", "onc", "pine", "lavender", "violet", "davinci", "raphael", "olive", "olivelite", "ginkgo", "begonia", "phoenix", "picasso", "apollo"};
    public static final LazyValue<Context, Boolean> SUPPORT_OCR = new LazyValue<Context, Boolean>() { // from class: com.miui.gallery.request.OCRRequestHelper.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Context context) {
            if (Rom.IS_INTERNATIONAL) {
                return Boolean.FALSE;
            }
            if (!Rom.IS_MIUI) {
                return Boolean.FALSE;
            }
            return Boolean.valueOf(DeviceCredentialManager.getSupportCloudCredential(context));
        }
    };

    /* loaded from: classes2.dex */
    public interface OCRRequestListener {
        void onRequestEnd();

        void onRequestStart();
    }

    /* renamed from: $r8$lambda$ownmnoe-KGJhR_PnpS96XB36hK0 */
    public static /* synthetic */ void m1260$r8$lambda$ownmnoeKGJhR_PnpS96XB36hK0(OCRRequestHelper oCRRequestHelper, boolean z, boolean z2) {
        oCRRequestHelper.lambda$showConfirmDialog$0(z, z2);
    }

    public OCRRequestHelper(FragmentActivity fragmentActivity, PhotoView photoView, BaseDataItem baseDataItem, OCRRequestListener oCRRequestListener) {
        this.mPhotoViewRef = new WeakReference<>(photoView);
        this.mActivityRef = new WeakReference<>(fragmentActivity);
        this.mListenerWeakReference = new WeakReference<>(oCRRequestListener);
        this.mBaseDataItem = baseDataItem;
    }

    public void startRequest() {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork() && this.mActivityRef.get() != null) {
            AgreementsUtils.showUserAgreements(this.mActivityRef.get(), new OnAgreementInvokedListener() { // from class: com.miui.gallery.request.OCRRequestHelper.2
                {
                    OCRRequestHelper.this = this;
                }

                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public void onAgreementInvoked(boolean z) {
                    if (z) {
                        OCRRequestHelper.this.startRequestWithCheck();
                    }
                }
            });
        } else {
            startRequestWithCheck();
        }
    }

    public void release() {
        if (this.mListenerWeakReference.get() != null) {
            this.mListenerWeakReference.get().onRequestEnd();
        }
    }

    public final void startRequestWithCheck() {
        if (!BaseNetworkUtils.isNetworkConnected()) {
            showNoNetworkToast();
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            showConfirmDialog();
        } else {
            startRequestWithCheckReal(false);
        }
    }

    public final void showNoNetworkToast() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.ocr_no_network);
    }

    public final void showConfirmDialog() {
        if (this.mActivityRef.get() == null) {
            return;
        }
        NetworkConsider.consider(this.mActivityRef.get(), new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.request.OCRRequestHelper$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                OCRRequestHelper.m1260$r8$lambda$ownmnoeKGJhR_PnpS96XB36hK0(OCRRequestHelper.this, z, z2);
            }
        });
    }

    public /* synthetic */ void lambda$showConfirmDialog$0(boolean z, boolean z2) {
        if (z) {
            startRequestWithCheckReal(false);
        }
    }

    public final void startRequestWithCheckReal(boolean z) {
        OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(z);
        this.mOCRRequest = oCRAsyncTask;
        oCRAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void cancelReuqest() {
        OCRAsyncTask oCRAsyncTask = this.mOCRRequest;
        if (oCRAsyncTask != null) {
            oCRAsyncTask.cancel(true);
        }
    }

    /* loaded from: classes2.dex */
    public class OCRAsyncTask extends AsyncTask<Void, Void, String> {
        public boolean mHasToasted;
        public boolean mIsRetry;
        public boolean mNeedRetry;

        public OCRAsyncTask(boolean z) {
            OCRRequestHelper.this = r1;
            this.mIsRetry = z;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            if (this.mIsRetry || OCRRequestHelper.this.mListenerWeakReference.get() == null) {
                return;
            }
            ((OCRRequestListener) OCRRequestHelper.this.mListenerWeakReference.get()).onRequestStart();
        }

        @Override // android.os.AsyncTask
        public String doInBackground(Void... voidArr) {
            Drawable drawable;
            ByteArrayOutputStream byteArrayOutputStream;
            Bitmap bitmap;
            byte[] byteArray;
            if (OCRRequestHelper.this.mActivityRef.get() == null) {
                return null;
            }
            String authTokenSync = HardwareAuthTokenManager.getAuthTokenSync((Context) OCRRequestHelper.this.mActivityRef.get(), this.mIsRetry, MiscUtil.getAppVersionCode(), "ocr");
            if (authTokenSync == null) {
                DefaultLogger.e(OCRRequestHelper.TAG, "ocr abnormal authentoken is null");
                return null;
            }
            if (OCRRequestHelper.this.mPhotoViewRef.get() != null && (drawable = ((PhotoView) OCRRequestHelper.this.mPhotoViewRef.get()).getDrawable()) != null) {
                if (drawable instanceof LayerDrawable) {
                    LayerDrawable layerDrawable = (LayerDrawable) drawable;
                    drawable = layerDrawable.getDrawable(layerDrawable.getNumberOfLayers() - 1);
                }
                if (drawable instanceof BitmapDrawable) {
                    if (OCRRequestHelper.this.mBaseDataItem.queryIsScreenshot((Context) OCRRequestHelper.this.mActivityRef.get())) {
                        File file = new File(OCRRequestHelper.this.mBaseDataItem.getPathDisplayBetter());
                        long length = file.length();
                        if (length <= 4194304) {
                            if (OCRRequestHelper.this.mBaseDataItem.getWidth() > 8000 || OCRRequestHelper.this.mBaseDataItem.getHeight() > 8000) {
                                toast(R.string.ocr_abnormal_pic_too_long);
                                return null;
                            } else if (length <= 0) {
                                DefaultLogger.e(OCRRequestHelper.TAG, "ocr abnormal size is 0");
                                return null;
                            } else {
                                int i = (int) length;
                                byteArray = new byte[i];
                                try {
                                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                                    bufferedInputStream.read(byteArray, 0, i);
                                    bufferedInputStream.close();
                                    bitmap = null;
                                } catch (IOException unused) {
                                    DefaultLogger.e(OCRRequestHelper.TAG, "ocr abnormal IOException");
                                    toast(R.string.ocr_abnormal);
                                    return null;
                                }
                            }
                        } else {
                            toast(R.string.ocr_abnormal_pic_too_large);
                            return null;
                        }
                    } else {
                        Bitmap bitmap2 = ((BitmapDrawable) drawable).getBitmap();
                        if (bitmap2 == null) {
                            return null;
                        }
                        int byteCount = bitmap2.getByteCount();
                        if (byteCount > 4194304) {
                            int width = bitmap2.getWidth();
                            int height = bitmap2.getHeight();
                            float f = 1080 / width;
                            float f2 = ((float) nexChecker.UHD_HEIGHT) / height;
                            if (f >= f2) {
                                f = f2;
                            }
                            Matrix matrix = new Matrix();
                            matrix.postScale(f, f);
                            bitmap = Bitmap.createBitmap(bitmap2, 0, 0, width, height, matrix, false);
                            byteArrayOutputStream = new ByteArrayOutputStream(bitmap.getByteCount());
                            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, byteArrayOutputStream);
                        } else {
                            byteArrayOutputStream = new ByteArrayOutputStream(byteCount);
                            bitmap2.compress(Bitmap.CompressFormat.WEBP, 100, byteArrayOutputStream);
                            bitmap = null;
                        }
                        byteArray = byteArrayOutputStream.toByteArray();
                    }
                    String encodeToString = Base64.encodeToString(byteArray, 2);
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    try {
                        Object[] executeSync = new OCRRequest(authTokenSync, encodeToString).executeSync();
                        if (executeSync != null && executeSync.length > 0 && (executeSync[0] instanceof VisionOCR.OcrGeneralResponse)) {
                            return OCRRequestHelper.this.processOcrResponse((VisionOCR.OcrGeneralResponse) executeSync[0]);
                        }
                    } catch (RequestError e) {
                        if ((e.getResponseData() instanceof SSLHandshakeException) && !this.mIsRetry) {
                            this.mNeedRetry = true;
                            return null;
                        }
                        Object responseData = e.getResponseData();
                        if (responseData != null && (responseData instanceof VolleyError)) {
                            VolleyError volleyError = (VolleyError) responseData;
                            if (volleyError.networkResponse != null) {
                                processReuqestError(volleyError);
                                DefaultLogger.e(OCRRequestHelper.TAG, "ocr request error %s", Integer.valueOf(volleyError.networkResponse.statusCode));
                            }
                        }
                    }
                } else {
                    toast(R.string.ocr_abnormal_pic_format);
                    return null;
                }
            }
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(String str) {
            if (str != null || this.mIsRetry || !this.mNeedRetry || isCancelled()) {
                if (OCRRequestHelper.this.mListenerWeakReference.get() != null) {
                    ((OCRRequestListener) OCRRequestHelper.this.mListenerWeakReference.get()).onRequestEnd();
                }
                if (isCancelled()) {
                    return;
                }
                if (str == null) {
                    DefaultLogger.e(OCRRequestHelper.TAG, "ocr abnormal string is null");
                    toast(R.string.ocr_abnormal);
                    return;
                } else if (!str.isEmpty()) {
                    if (OCRRequestHelper.this.mActivityRef.get() == null) {
                        return;
                    }
                    Intent intent = new Intent((Context) OCRRequestHelper.this.mActivityRef.get(), TextEditActivity.class);
                    intent.putExtra("extra_editable_string", str);
                    intent.putExtra("extra_revise_photo", OCRRequestHelper.this.mBaseDataItem);
                    ((FragmentActivity) OCRRequestHelper.this.mActivityRef.get()).startActivity(intent);
                    return;
                } else {
                    toast(R.string.ocr_no_text_recognized);
                    return;
                }
            }
            OCRRequestHelper.this.startRequestWithCheckReal(true);
        }

        public final void processReuqestError(VolleyError volleyError) {
            int i;
            NetworkResponse networkResponse = volleyError.networkResponse;
            if (networkResponse == null) {
                DefaultLogger.e(OCRRequestHelper.TAG, "processReuqestError null response");
                return;
            }
            byte[] bArr = networkResponse.data;
            String str = "";
            if (bArr != null) {
                try {
                    Response response = APIUtils.getResponse(new String(bArr, "utf-8"), VisionOCR.OcrGeneralResponse.class);
                    if (response.getStatus() == null) {
                        return;
                    }
                    i = response.getStatus().getCode();
                    if (response.getStatus().getTrace().get() == null) {
                        return;
                    }
                    str = response.getStatus().getTrace().get().getTraceId();
                } catch (UnsupportedEncodingException unused) {
                    i = volleyError.networkResponse.statusCode;
                    DefaultLogger.e(OCRRequestHelper.TAG, "processReuqestError UnsupportedEncodingException");
                }
            } else {
                i = networkResponse.statusCode;
            }
            DefaultLogger.e(OCRRequestHelper.TAG, "processReuqestError %s %s", Integer.valueOf(i), str);
            HashMap hashMap = new HashMap();
            hashMap.put("result", String.valueOf(i));
            hashMap.put("request", str);
            SamplingStatHelper.recordCountEvent("OCR", "ocr_sever_error", hashMap);
            switch (i) {
                case StatusCode.BAD_PARAMETER /* 40010001 */:
                case StatusCode.BAD_FORMAT_CONTENT /* 40010004 */:
                case StatusCode.IMAGE_EMPTY_ERROR /* 40021101 */:
                case StatusCode.OCR_FAIL /* 50021100 */:
                    DefaultLogger.e(OCRRequestHelper.TAG, "ocr abnormal code %s", Integer.valueOf(i));
                    toast(R.string.ocr_abnormal);
                    return;
                case StatusCode.IMAGE_BAD_FORMAT_ERROR /* 40021102 */:
                    toast(R.string.ocr_abnormal_pic_format);
                    return;
                case StatusCode.IMAGE_DETECT_EMPTY_TXT_ERROR /* 50021102 */:
                    toast(R.string.ocr_no_text_recognized);
                    return;
                default:
                    if (String.valueOf(i).startsWith(String.valueOf(401))) {
                        if (this.mIsRetry) {
                            DefaultLogger.e(OCRRequestHelper.TAG, "ocr abnormal code %s", Integer.valueOf(i));
                            toast(R.string.ocr_abnormal);
                            return;
                        }
                        this.mNeedRetry = true;
                        return;
                    } else if (!String.valueOf(i).startsWith(String.valueOf((int) StatusCode.REQUEST_ENTITY_TOO_LARGE))) {
                        DefaultLogger.e(OCRRequestHelper.TAG, "ocr abnormal code %s", Integer.valueOf(i));
                        toast(R.string.ocr_abnormal);
                        return;
                    } else {
                        toast(R.string.ocr_abnormal_pic_too_large);
                        return;
                    }
            }
        }

        public final void toast(int i) {
            if (this.mHasToasted) {
                return;
            }
            this.mHasToasted = true;
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), i);
        }
    }

    public final String processOcrResponse(VisionOCR.OcrGeneralResponse ocrGeneralResponse) {
        Iterator<VisionOCR.Line> it;
        List<VisionOCR.Region> regions = ocrGeneralResponse.getRegions();
        String property = System.getProperty("line.separator");
        String str = "";
        for (VisionOCR.Region region : regions) {
            while (region.getLines().iterator().hasNext()) {
                str = str + it.next().getText() + property;
            }
        }
        return str;
    }

    /* loaded from: classes2.dex */
    public static class OCRRequest extends OCRRelatedRequest {
        public OCRRequest(String str, String str2) {
            super(1, "https://api.open.ai.xiaomi.com/vision/ocr/v1/general");
            addHeader("Content-Encoding", d.aj);
            addHeader("Content-Type", "application/json");
            addHeader("User-Agent", String.format("MiuiGallery/%s", Integer.valueOf(MiscUtil.getAppVersionCode())));
            addHeader("Authorization", String.format("HARDWARE_AUTH %s", str));
            setUseJsonContentType(true);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("image", str2);
            setBodyJson(jsonObject);
        }

        @Override // com.miui.gallery.net.hardwareauth.OCRRelatedRequest
        public boolean checkStatus(JSONObject jSONObject) {
            Response response = APIUtils.getResponse(jSONObject.toString(), VisionOCR.OcrGeneralResponse.class);
            if (response == null || response.getStatus() == null || response.getStatus().getCode() == 200) {
                return true;
            }
            DefaultLogger.e(OCRRequestHelper.TAG, "ocr request error code: %s trace id: %s", Integer.valueOf(response.getStatus().getCode()), response.getStatus().getTrace());
            return false;
        }

        @Override // com.miui.gallery.net.hardwareauth.OCRRelatedRequest
        public void processData(JSONObject jSONObject) {
            VisionOCR.OcrGeneralResponse ocrGeneralResponse = (VisionOCR.OcrGeneralResponse) APIUtils.fromJsonString(jSONObject.toString(), VisionOCR.OcrGeneralResponse.class);
            if (ocrGeneralResponse != null) {
                deliverResponse(ocrGeneralResponse);
            }
        }
    }
}
