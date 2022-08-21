package com.miui.gallery.ui;

import android.content.Context;
import android.os.Build;
import com.miui.gallery.util.LazyValue;
import com.miui.mishare.app.util.NearbyUtils;
import com.miui.mishare.app.view.MiShareGalleryTransferView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ShareStateRouter {
    public static final LazyValue<Context, Boolean> IS_MISHARE_AVAILABLE = new LazyValue<Context, Boolean>() { // from class: com.miui.gallery.ui.ShareStateRouter.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            return Boolean.valueOf(Build.VERSION.SDK_INT >= 28 && MiShareGalleryTransferView.isServiceAvailable(context));
        }
    };
    public static final LazyValue<Context, Boolean> SHOULD_UPGRADE_NEARBY_SHARE = new LazyValue<Context, Boolean>() { // from class: com.miui.gallery.ui.ShareStateRouter.2
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            return Boolean.valueOf(ShareStateRouter.IS_MISHARE_AVAILABLE.get(context).booleanValue() && NearbyUtils.supportNearby(context));
        }
    };
    public static ShareStateRouter sShareStateRouter;
    public List<ProjectStateObserver> mProjectStateObservers = new ArrayList();
    public List<PrintStateObserver> mPrintStateObservers = new ArrayList();

    /* loaded from: classes2.dex */
    public interface PrintStateObserver {
        void onObservePrintState(boolean z);
    }

    /* loaded from: classes2.dex */
    public interface ProjectStateObserver {
        void onObserveProjectState(int i);
    }

    public static synchronized ShareStateRouter getInstance() {
        ShareStateRouter shareStateRouter;
        synchronized (ShareStateRouter.class) {
            if (sShareStateRouter == null) {
                sShareStateRouter = new ShareStateRouter();
            }
            shareStateRouter = sShareStateRouter;
        }
        return shareStateRouter;
    }

    public void registerProjectStateObserver(ProjectStateObserver projectStateObserver) {
        if (!this.mProjectStateObservers.contains(projectStateObserver)) {
            this.mProjectStateObservers.add(projectStateObserver);
        }
    }

    public void registerPrintStateObserver(PrintStateObserver printStateObserver) {
        if (!this.mPrintStateObservers.contains(printStateObserver)) {
            this.mPrintStateObservers.add(printStateObserver);
        }
    }

    public void removeProjectStateObserver(ProjectStateObserver projectStateObserver) {
        this.mProjectStateObservers.remove(projectStateObserver);
    }

    public void removePrintStateObserver(PrintStateObserver printStateObserver) {
        this.mPrintStateObservers.remove(printStateObserver);
    }

    public void broadcastProjectState(int i) {
        for (ProjectStateObserver projectStateObserver : this.mProjectStateObservers) {
            projectStateObserver.onObserveProjectState(i);
        }
    }

    public void broadcastPrintState(boolean z) {
        for (PrintStateObserver printStateObserver : this.mPrintStateObservers) {
            printStateObserver.onObservePrintState(z);
        }
    }
}
