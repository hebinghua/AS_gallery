package com.miui.gallery.vlog.caption;

import android.content.Context;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.caption.CaptionStyleModel;
import com.miui.gallery.vlog.entity.CaptionStyleData;
import java.util.List;

/* loaded from: classes2.dex */
public class CaptionStylePresenter extends BasePresenter {
    public CaptionStyleContract$ICaptionStyleView mICaptionStyleView;

    public CaptionStylePresenter(Context context, CaptionStyleContract$ICaptionStyleView captionStyleContract$ICaptionStyleView) {
        super(context);
        this.mICaptionStyleView = captionStyleContract$ICaptionStyleView;
        this.mIBaseModel = new CaptionStyleModel(new CaptionStyleModel.Callback() { // from class: com.miui.gallery.vlog.caption.CaptionStylePresenter.1
            @Override // com.miui.gallery.vlog.caption.CaptionStyleModel.Callback
            public void loadDataFail() {
            }

            @Override // com.miui.gallery.vlog.caption.CaptionStyleModel.Callback
            public void loadDataSuccess(List<CaptionStyleData> list) {
                CaptionStylePresenter.this.mICaptionStyleView.loadRecyclerView(list);
            }
        });
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void destroy() {
        IBaseModel iBaseModel = this.mIBaseModel;
        if (iBaseModel instanceof CaptionStyleModel) {
            ((CaptionStyleModel) iBaseModel).setCallback(null);
            ((CaptionStyleModel) this.mIBaseModel).clear();
        }
    }
}
