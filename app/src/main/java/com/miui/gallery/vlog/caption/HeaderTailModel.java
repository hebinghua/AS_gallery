package com.miui.gallery.vlog.caption;

import com.miui.gallery.net.resource.CommonResponse;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.base.net.TitleStyleRequest;
import com.miui.gallery.vlog.entity.HeaderTailData;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class HeaderTailModel implements IBaseModel {
    public Callback mCallback;
    public ArrayList<HeaderTailData> mTitleStyleResources = new ArrayList<>();
    public CommonResponse.Callback<HeaderTailData> mResourceCallback = new CommonResponse.Callback<HeaderTailData>() { // from class: com.miui.gallery.vlog.caption.HeaderTailModel.1
        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onSuccess(List<HeaderTailData> list) {
            if (BaseMiscUtil.isValid(list)) {
                DefaultLogger.d("HeaderTailModel", "load data success.");
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setBgColor(i);
                }
                HeaderTailModel.this.mTitleStyleResources.clear();
                HeaderTailModel.this.mTitleStyleResources.add(HeaderTailData.getDefaultItem());
                HeaderTailModel.this.mTitleStyleResources.add(HeaderTailData.getCustomItem());
                HeaderTailModel.this.mTitleStyleResources.addAll(list);
                if (HeaderTailModel.this.mCallback == null) {
                    return;
                }
                HeaderTailModel.this.mCallback.loadDataSuccess(HeaderTailModel.this.mTitleStyleResources);
            }
        }

        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onFail() {
            DefaultLogger.e("HeaderTailModel", "load data fail.");
            if (HeaderTailModel.this.mCallback != null) {
                HeaderTailModel.this.mCallback.loadDataFail();
            }
        }
    };
    public TitleStyleRequest mTitleStyleRequest = new TitleStyleRequest();
    public CommonResponse mCommonResponse = new CommonResponse(this.mResourceCallback);

    /* loaded from: classes2.dex */
    public interface Callback {
        void loadDataFail();

        void loadDataSuccess(ArrayList<HeaderTailData> arrayList);
    }

    public HeaderTailModel(Callback callback) {
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
        TitleStyleRequest titleStyleRequest = this.mTitleStyleRequest;
        if (titleStyleRequest != null) {
            titleStyleRequest.cancel();
            this.mTitleStyleRequest = null;
        }
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IBaseModel
    public void loadData() {
        this.mTitleStyleRequest.execute(this.mCommonResponse);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
