package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.googlelens.GoogleLensImpl;
import com.miui.gallery.googlelens.IGoogleLens;
import com.miui.gallery.googlelens.LensAvailabilityCallback;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.view.menu.IMenuItem;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class GoogleLens extends BaseMenuItemDelegate {
    public boolean isLensResume;
    public IGoogleLens mGoogleLens;

    public static /* synthetic */ void $r8$lambda$aO0bMxt87uSy8hCDbGXaXHbWt7k(GoogleLens googleLens, int i) {
        googleLens.lambda$checkPostCaptureAvailability$0(i);
    }

    public static GoogleLens instance(IMenuItem iMenuItem) {
        return new GoogleLens(iMenuItem);
    }

    public GoogleLens(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate
    public void doInitFunction() {
        super.doInitFunction();
        GoogleLensImpl googleLensImpl = new GoogleLensImpl();
        this.mGoogleLens = googleLensImpl;
        googleLensImpl.init(this.mContext.getApplicationContext());
    }

    public final void checkPostCaptureAvailability() {
        this.mGoogleLens.checkPostCaptureAvailability(new LensAvailabilityCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.GoogleLens$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.googlelens.LensAvailabilityCallback
            public final void onPostCaptureAvailabilityStatus(int i) {
                GoogleLens.$r8$lambda$aO0bMxt87uSy8hCDbGXaXHbWt7k(GoogleLens.this, i);
            }
        });
    }

    public /* synthetic */ void lambda$checkPostCaptureAvailability$0(int i) {
        boolean z;
        if (i == 0) {
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_GoogleLens", "Google Lens is available");
            z = true;
        } else {
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_GoogleLens", "Google Lens is unavailable: status->%s", Integer.valueOf(i));
            z = false;
        }
        setSupport(z);
        this.mMenuItemManager.refreshNonResidentData(this, z);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onResume(LifecycleOwner lifecycleOwner) {
        IGoogleLens iGoogleLens = this.mGoogleLens;
        if (iGoogleLens != null) {
            iGoogleLens.onResume();
            this.isLensResume = true;
        }
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onPause(LifecycleOwner lifecycleOwner) {
        IGoogleLens iGoogleLens = this.mGoogleLens;
        if (iGoogleLens != null) {
            iGoogleLens.onPause();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        Bitmap bitmap;
        if (!this.isFunctionInit) {
            return;
        }
        Drawable drawable = this.mDataProvider.getFieldData().mCurrent.itemView.getPhotoView().getDrawable();
        if (!(drawable instanceof BitmapDrawable) || (bitmap = ((BitmapDrawable) drawable).getBitmap()) == null) {
            return;
        }
        if (this.mGoogleLens.launchLensActivityWithBitmap(bitmap)) {
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_GoogleLens", "Launch google lens activity successfully.");
        } else {
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_GoogleLens", "Launch google lens activity failed.");
        }
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        IGoogleLens iGoogleLens = this.mGoogleLens;
        if (iGoogleLens != null) {
            iGoogleLens.release();
            this.mGoogleLens = null;
        }
    }

    public void onFilterFinished(BaseDataItem baseDataItem, ArrayList<IMenuItemDelegate> arrayList, ArrayList<IMenuItemDelegate> arrayList2) {
        if (this.mGoogleLens == null || !this.isLensResume || !this.isFunctionInit) {
            return;
        }
        checkPostCaptureAvailability();
    }
}
