package com.miui.gallery.editor.photo.app.sky.res;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.miai.api.Response;
import com.xiaomi.miai.api.common.APIUtils;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SkyRandomRequest extends BaseStringRequest<String> {
    public ISkyRandomRequestCallback mISkyRandomRequestCallback;
    public int mSkyMaterialId;

    /* loaded from: classes2.dex */
    public interface ISkyRandomRequestCallback {
        void returnRequestErrorCode(int i);

        void setSkyBgFromCloud(Bitmap bitmap, int i);
    }

    public static String getSunnySkyMaterialName(int i) {
        if (i != 0) {
            if (i == 1) {
                return "qingkong";
            }
            if (i == 2) {
                return "bikong";
            }
            if (i == 3) {
                return "duoyun";
            }
            if (i == 4) {
                return "boyun";
            }
            if (i == 5) {
                return "yunxu";
            }
            if (i == 15) {
                return "cengyun";
            }
            if (i == 16) {
                return "yuyun";
            }
            return null;
        }
        return "qingtian";
    }

    public SkyRandomRequest(int i, int i2, String str, String str2, ISkyRandomRequestCallback iSkyRandomRequestCallback) {
        super(i, str);
        this.mISkyRandomRequestCallback = iSkyRandomRequestCallback;
        this.mSkyMaterialId = i2;
        addHeader("Content-Type", "application/json");
        addHeader("User-Agent", String.format("MiuiGallery/%s", Integer.valueOf(MiscUtil.getAppVersionCode())));
        addHeader("Authorization", String.format("HARDWARE_AUTH %s", str2));
    }

    @Override // com.miui.gallery.editor.photo.app.sky.res.BaseStringRequest
    public byte[] getRequestBody() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("scene", getSunnySkyMaterialName(this.mSkyMaterialId));
            jSONObject.put(MapBundleKey.MapObjKey.OBJ_SL_INDEX, (Object) null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return jSONObject.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    @Override // com.miui.gallery.net.base.VolleyRequest
    public void handleResponse(String str) {
        SkyResourceData skyResourceData = (SkyResourceData) GsonUtils.fromJson(str, (Class<Object>) SkyResourceData.class);
        int code = skyResourceData.getStatus().getCode();
        if (skyResourceData.getData() == null) {
            this.mISkyRandomRequestCallback.returnRequestErrorCode(code);
        } else if (skyResourceData.getData().getImage() == null) {
            this.mISkyRandomRequestCallback.returnRequestErrorCode(code);
        } else {
            byte[] decode = Base64.decode(skyResourceData.getData().getImage(), 0);
            int index = skyResourceData.getData().getIndex();
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            ISkyRandomRequestCallback iSkyRandomRequestCallback = this.mISkyRandomRequestCallback;
            if (iSkyRandomRequestCallback != null) {
                if (decodeByteArray == null) {
                    iSkyRandomRequestCallback.returnRequestErrorCode(code);
                } else {
                    iSkyRandomRequestCallback.setSkyBgFromCloud(decodeByteArray, index);
                }
            }
            DefaultLogger.d("SkyRandomRequest", "sky request success %d", Integer.valueOf(code));
        }
    }

    @Override // com.miui.gallery.net.base.BaseRequest, com.miui.gallery.net.base.ResponseErrorHandler
    public void onRequestError(ErrorCode errorCode, String str, Object obj) {
        int i;
        if (obj instanceof VolleyError) {
            VolleyError volleyError = (VolleyError) obj;
            if (volleyError.networkResponse != null) {
                i = processRequestError(volleyError);
                DefaultLogger.d("SkyRandomRequest", "sky request error %s", Integer.valueOf(volleyError.networkResponse.statusCode));
                DefaultLogger.d("SkyRandomRequest", "sky request error %d", Integer.valueOf(i));
                this.mISkyRandomRequestCallback.returnRequestErrorCode(i);
            }
        }
        i = -1;
        DefaultLogger.d("SkyRandomRequest", "sky request error %d", Integer.valueOf(i));
        this.mISkyRandomRequestCallback.returnRequestErrorCode(i);
    }

    public final int processRequestError(VolleyError volleyError) {
        int i;
        NetworkResponse networkResponse = volleyError.networkResponse;
        byte[] bArr = networkResponse.data;
        if (bArr != null) {
            try {
                Response response = APIUtils.getResponse(new String(bArr, "utf-8"), SkyResourceData.class);
                if (response.getStatus() == null) {
                    return -1;
                }
                i = response.getStatus().getCode();
            } catch (UnsupportedEncodingException unused) {
                i = volleyError.networkResponse.statusCode;
                DefaultLogger.e("SkyRandomRequest", "processRequestError UnsupportedEncodingException");
            }
        } else {
            i = networkResponse.statusCode;
        }
        DefaultLogger.d("SkyRandomRequest", "processRequestError %s %s", Integer.valueOf(i));
        return i;
    }
}
