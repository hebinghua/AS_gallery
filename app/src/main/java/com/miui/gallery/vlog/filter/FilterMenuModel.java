package com.miui.gallery.vlog.filter;

import com.miui.gallery.net.resource.CommonResponse;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.base.net.FilterRequest;
import com.miui.gallery.vlog.entity.FilterData;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterMenuModel implements IBaseModel {
    public Callback mCallback;
    public ArrayList<FilterData> mFilterResources = new ArrayList<>();
    public CommonResponse.Callback<FilterData> mResourceCallback = new CommonResponse.Callback<FilterData>() { // from class: com.miui.gallery.vlog.filter.FilterMenuModel.1
        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onSuccess(List<FilterData> list) {
            if (BaseMiscUtil.isValid(list)) {
                DefaultLogger.d("FilterMenuModel", "load data success.");
                FilterMenuModel.this.mFilterResources.clear();
                FilterMenuModel.this.mFilterResources.add(FilterData.getDefaultItem());
                FilterMenuModel.this.mFilterResources.addAll(list);
                if (FilterMenuModel.this.mCallback == null) {
                    return;
                }
                FilterMenuModel.this.mCallback.loadDataSuccess(FilterMenuModel.this.mFilterResources);
            }
        }

        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onFail() {
            DefaultLogger.e("FilterMenuModel", "load data fail.");
            if (FilterMenuModel.this.mCallback != null) {
                FilterMenuModel.this.mCallback.loadDataFail();
            }
        }
    };
    public FilterRequest mFilterRequest = new FilterRequest();
    public CommonResponse mCommonResponse = new CommonResponse(this.mResourceCallback);

    /* loaded from: classes2.dex */
    public interface Callback {
        void loadDataFail();

        void loadDataSuccess(ArrayList<FilterData> arrayList);
    }

    public FilterMenuModel(Callback callback) {
        this.mCallback = callback;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IBaseModel
    public void loadData() {
        DefaultLogger.d("FilterMenuModel", "start load data.");
        this.mFilterRequest.execute(this.mCommonResponse);
    }

    public void clear() {
        if (this.mCallback != null) {
            this.mCallback = null;
        }
        CommonResponse commonResponse = this.mCommonResponse;
        if (commonResponse != null) {
            commonResponse.setCallback(null);
        }
        FilterRequest filterRequest = this.mFilterRequest;
        if (filterRequest != null) {
            filterRequest.cancel();
            this.mFilterRequest = null;
        }
    }
}
