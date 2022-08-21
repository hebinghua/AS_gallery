package com.miui.gallery.ui.photoPage;

import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.model.BaseDataItem;

/* loaded from: classes2.dex */
public class PhotoPageLifeCircleHooker {
    public void pause() {
        PhotoPageManagerMediator.getInstance().dispatchLifeCirclePause();
    }

    public void resume() {
        PhotoPageManagerMediator.getInstance().dispatchLifeCircleResume();
    }

    public void onDestroyView() {
        PhotoPageManagerMediator.getInstance().dispatchLifeCircleOnDestroyView();
    }

    public void settleItem(BaseDataItem baseDataItem) {
        PhotoPageManagerMediator.getInstance().dispatchLifeCircleSettleItem(baseDataItem);
    }

    public void onViewInflated() {
        PhotoPageManagerMediator.getInstance().dispatchLifeCircleOnViewInflated();
    }

    public void onConfigurationChanged(Configuration configuration) {
        PhotoPageManagerMediator.getInstance().dispatchLifeCircleOnConfigurationChanged(configuration);
    }

    public void doExit() {
        PhotoPageManagerMediator.getInstance().dispatchLifeCircleDoExit();
    }

    public void onDestroy() {
        PhotoPageManagerMediator.getInstance().dispatchLifeCircleOnDestroy();
    }

    public void onCreate(Bundle bundle) {
        PhotoPageManagerMediator.getInstance().dispatchLifeCircleOnCreate(bundle);
    }

    public void onStart() {
        PhotoPageManagerMediator.getInstance().dispatchLifeCircleOnStart();
    }

    public void onSaveInstanceState(Bundle bundle) {
        PhotoPageManagerMediator.getInstance().dispatchLifeCircleOnSaveInstanceState(bundle);
    }
}
