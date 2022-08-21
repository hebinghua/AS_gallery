package com.miui.gallery.vlog.template;

import com.miui.gallery.net.resource.CommonResponse;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.base.net.TemplateRequest;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TemplateMenuModel implements IBaseModel {
    public Callback mCallback;
    public ArrayList<TemplateResource> mTemplateResources = new ArrayList<>();
    public CommonResponse.Callback<TemplateResource> mResourceCallback = new CommonResponse.Callback<TemplateResource>() { // from class: com.miui.gallery.vlog.template.TemplateMenuModel.1
        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onSuccess(List<TemplateResource> list) {
            if (BaseMiscUtil.isValid(list)) {
                DefaultLogger.d("TemplateMenuModel", "load data success.");
                TemplateMenuModel.this.mTemplateResources.clear();
                TemplateMenuModel.this.mTemplateResources.add(TemplateResource.getDefaultItem());
                for (int i = 0; i < list.size(); i++) {
                    TemplateResource templateResource = list.get(i);
                    if (!TemplateProfileUtils.isNeedHideHighTemplate() || !templateResource.isHighTemplate()) {
                        TemplateMenuModel.this.mTemplateResources.add(templateResource);
                    }
                }
                if (TemplateMenuModel.this.mCallback == null) {
                    return;
                }
                TemplateMenuModel.this.mCallback.loadDataSuccess(TemplateMenuModel.this.mTemplateResources);
            }
        }

        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onFail() {
            DefaultLogger.e("TemplateMenuModel", "load data fail.");
            if (TemplateMenuModel.this.mCallback != null) {
                TemplateMenuModel.this.mCallback.loadDataFail();
            }
        }
    };
    public TemplateRequest mTemplateRequest = new TemplateRequest();
    public CommonResponse mCommonResponse = new CommonResponse(this.mResourceCallback);

    /* loaded from: classes2.dex */
    public interface Callback {
        void loadDataFail();

        void loadDataSuccess(List<TemplateResource> list);
    }

    public TemplateMenuModel(Callback callback) {
        this.mCallback = callback;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IBaseModel
    public void loadData() {
        DefaultLogger.d("TemplateMenuModel", "start load data.");
        this.mTemplateRequest.execute(this.mCommonResponse);
    }

    public void clear() {
        if (this.mCallback != null) {
            this.mCallback = null;
        }
        CommonResponse commonResponse = this.mCommonResponse;
        if (commonResponse != null) {
            commonResponse.setCallback(null);
        }
        TemplateRequest templateRequest = this.mTemplateRequest;
        if (templateRequest != null) {
            templateRequest.cancel();
            this.mTemplateRequest = null;
        }
    }
}
