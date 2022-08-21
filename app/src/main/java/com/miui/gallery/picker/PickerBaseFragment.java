package com.miui.gallery.picker;

import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.app.screenChange.IScreenChange;

/* loaded from: classes2.dex */
public class PickerBaseFragment extends GalleryFragment {
    /* renamed from: $r8$lambda$PTU-Xl4hhwieyB99kyLUtCa6fhQ */
    public static /* synthetic */ void m1177$r8$lambda$PTUXl4hhwieyB99kyLUtCa6fhQ(PickerBaseFragment pickerBaseFragment, Configuration configuration) {
        pickerBaseFragment.lambda$onCreate$0(configuration);
    }

    /* renamed from: $r8$lambda$mmjUfSOWzNC9og-n0DEUfsWRXrY */
    public static /* synthetic */ void m1178$r8$lambda$mmjUfSOWzNC9ogn0DEUfsWRXrY(PickerBaseFragment pickerBaseFragment, Bundle bundle, Configuration configuration) {
        pickerBaseFragment.lambda$onCreate$1(bundle, configuration);
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addScreenChangeListener(new IScreenChange.OnScreenLayoutSizeChangeListener() { // from class: com.miui.gallery.picker.PickerBaseFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnScreenLayoutSizeChangeListener
            public final void onScreenLayoutSizeChange(Configuration configuration) {
                PickerBaseFragment.m1177$r8$lambda$PTUXl4hhwieyB99kyLUtCa6fhQ(PickerBaseFragment.this, configuration);
            }
        });
        addScreenChangeListener(new IScreenChange.OnRestoreInstanceListener() { // from class: com.miui.gallery.picker.PickerBaseFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnRestoreInstanceListener
            public final void onRestoreInstance(Bundle bundle2, Configuration configuration) {
                PickerBaseFragment.m1178$r8$lambda$mmjUfSOWzNC9ogn0DEUfsWRXrY(PickerBaseFragment.this, bundle2, configuration);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0(Configuration configuration) {
        updateThumbConfig();
    }

    public /* synthetic */ void lambda$onCreate$1(Bundle bundle, Configuration configuration) {
        updateThumbConfig();
    }

    public final void updateThumbConfig() {
        Config$ThumbConfig.get().updateConfig();
    }
}
