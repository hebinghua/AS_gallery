package com.miui.gallery.vlog.caption;

import com.miui.gallery.net.resource.CommonResponse;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.base.net.CaptionStyleRequest;
import com.miui.gallery.vlog.entity.CaptionStyleData;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class CaptionStyleModel implements IBaseModel {
    public Callback mCallback;
    public ArrayList<CaptionStyleData> mCaptionStyleResources = new ArrayList<>();
    public CommonResponse.Callback<CaptionStyleData> mResourceCallback = new CommonResponse.Callback<CaptionStyleData>() { // from class: com.miui.gallery.vlog.caption.CaptionStyleModel.1
        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onSuccess(List<CaptionStyleData> list) {
            if (BaseMiscUtil.isValid(list)) {
                DefaultLogger.d("CaptionStyleModel", "load data success.");
                CaptionStyleModel.this.mCaptionStyleResources.clear();
                CaptionStyleModel.this.mCaptionStyleResources.add(CaptionStyleData.getDefaultItem());
                list.remove(0);
                CaptionStyleModel.this.mCaptionStyleResources.addAll(list);
                if (CaptionStyleModel.this.mCallback == null) {
                    return;
                }
                CaptionStyleModel.this.mCallback.loadDataSuccess(CaptionStyleModel.this.mCaptionStyleResources);
            }
        }

        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onFail() {
            DefaultLogger.e("CaptionStyleModel", "load data fail.");
            if (CaptionStyleModel.this.mCallback != null) {
                CaptionStyleModel.this.mCallback.loadDataFail();
            }
        }
    };
    public CaptionStyleRequest mCaptionStyleRequest = new CaptionStyleRequest();
    public CommonResponse mCommonResponse = new CommonResponse(this.mResourceCallback);

    /* loaded from: classes2.dex */
    public interface Callback {
        void loadDataFail();

        void loadDataSuccess(List<CaptionStyleData> list);
    }

    public CaptionStyleModel(Callback callback) {
        this.mCallback = callback;
    }

    public void clear() {
        if (this.mCallback != null) {
            this.mCallback = null;
        }
        CommonResponse commonResponse = this.mCommonResponse;
        if (commonResponse != null) {
            commonResponse.setCallback(null);
        }
        CaptionStyleRequest captionStyleRequest = this.mCaptionStyleRequest;
        if (captionStyleRequest != null) {
            captionStyleRequest.cancel();
            this.mCaptionStyleRequest = null;
        }
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IBaseModel
    public void loadData() {
        this.mCaptionStyleRequest.execute(this.mCommonResponse);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
