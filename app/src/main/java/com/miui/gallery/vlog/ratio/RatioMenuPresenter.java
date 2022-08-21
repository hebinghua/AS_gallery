package com.miui.gallery.vlog.ratio;

import android.content.Context;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.entity.RatioData;
import java.util.List;

/* loaded from: classes2.dex */
public class RatioMenuPresenter extends BasePresenter {
    public MiVideoRatioManager mRatioManager;

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void destroy() {
    }

    public RatioMenuPresenter(Context context) {
        super(context);
        this.mRatioManager = (MiVideoRatioManager) this.mVlogModel.getSdkManager().getManagerService(10);
    }

    public int findCurrentIndex(List<RatioData> list) {
        int currentAspectRatio = this.mVlogModel.getSdkManager().getCurrentAspectRatio();
        for (int i = 0; i < list.size(); i++) {
            if (currentAspectRatio == list.get(i).getRatio()) {
                return i;
            }
        }
        return 0;
    }
}
