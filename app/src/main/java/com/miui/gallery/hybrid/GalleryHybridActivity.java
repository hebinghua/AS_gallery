package com.miui.gallery.hybrid;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.hybrid.GalleryHybridFragment;
import com.miui.gallery.hybrid.hybridclient.HybridClient;
import com.miui.gallery.hybrid.hybridclient.HybridClientFactory;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.request.HostManager;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.util.PrivacyAgreementUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class GalleryHybridActivity extends BaseActivity implements GalleryHybridFragment.HybridViewEventListener {
    public boolean isFromPhotoEditor;
    public boolean isFromRecommend;
    public boolean mHasLoaded = false;
    public HybridClient mHybridClient;
    public GalleryHybridFragment mHybridFragment;
    public TextView mTitleView;

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean allowUseOnOffline() {
        return false;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        this.isFromRecommend = intent.getBooleanExtra("from_recommend", false);
        this.isFromPhotoEditor = intent.getBooleanExtra("from_photo_editor", false);
        HybridClient createHybridClient = HybridClientFactory.createHybridClient(this, intent);
        this.mHybridClient = createHybridClient;
        if (createHybridClient == null) {
            super.onCreate(bundle);
            finish();
            return;
        }
        if (!OrientationCheckHelper.isSupportOrientationChange()) {
            setRequestedOrientation(1);
        }
        super.onCreate(bundle);
        setContentView(R.layout.hybrid_activity);
        configureActionBar();
        GalleryHybridFragment galleryHybridFragment = (GalleryHybridFragment) getSupportFragmentManager().findFragmentById(R.id.hybrid_fragment);
        this.mHybridFragment = galleryHybridFragment;
        galleryHybridFragment.setHybridViewEventListener(this);
        this.mHybridFragment.setHybridClient(this.mHybridClient);
        if (this.isFromPhotoEditor) {
            this.mHybridFragment.setDisableNavigationBarStrategy(true);
        }
        load();
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public void onCtaChecked(boolean z, boolean z2) {
        if (z) {
            load();
        }
    }

    public final void configureActionBar() {
        View customView;
        this.mActionBar.setDisplayShowCustomEnabled(true);
        this.mActionBar.setDisplayShowHomeEnabled(false);
        if (this.isFromRecommend) {
            this.mActionBar.setCustomView(R.layout.hybrid_action_bar_empty);
            this.mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.recommend_webview_actionbar)));
            customView = this.mActionBar.getCustomView();
        } else {
            this.mActionBar.setDisplayShowTitleEnabled(false);
            this.mActionBar.setHomeButtonEnabled(true);
            this.mActionBar.setCustomView(R.layout.hybrid_action_bar_custom_view);
            customView = this.mActionBar.getCustomView();
            this.mTitleView = (TextView) customView.findViewById(R.id.title);
        }
        View findViewById = customView.findViewById(R.id.close);
        if (this.isFromPhotoEditor) {
            this.mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.photo_editor_webview_actionbar)));
            this.mTitleView.setTextColor(getResources().getColor(R.color.photo_editor_webview_title_color));
            findViewById.setBackgroundResource(R.drawable.hybrid_photo_editor_action_bar_close);
            requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
            getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
            getWindow().setNavigationBarColor(-16777216);
        }
        findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.hybrid.GalleryHybridActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GalleryHybridActivity.this.finish();
            }
        });
    }

    public final void load() {
        if (this.mHasLoaded) {
            DefaultLogger.w("GalleryHybridActivity", "url has loaded!");
        } else if (this.mHybridFragment == null) {
            DefaultLogger.w("GalleryHybridActivity", "Hybrid fragment not attached, couldn't load url now!");
        } else if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.w("GalleryHybridActivity", "CTA not allowed, couldn't connect to network!");
        } else if (checkCloudServiceAgreementIfNeeded(getIntent())) {
            this.mHybridFragment.load();
            this.mHasLoaded = true;
        } else {
            finish();
        }
    }

    public final boolean checkCloudServiceAgreementIfNeeded(Intent intent) {
        if (intent == null || !isCloudServiceRequest(intent) || PrivacyAgreementUtils.isCloudServiceAgreementEnable(GalleryApp.sGetAndroidContext())) {
            return true;
        }
        DefaultLogger.w("GalleryHybridActivity", "cloud service request but cloud privacy denied, start cloud main page.");
        IntentUtil.startCloudMainPage(GalleryApp.sGetAndroidContext());
        return false;
    }

    public final boolean isCloudServiceRequest(Intent intent) {
        String dataString = intent.getDataString();
        String stringExtra = intent.getStringExtra(MapBundleKey.MapObjKey.OBJ_URL);
        String[] strArr = {HostManager.getTrashBinUrl()};
        for (int i = 0; i < 1; i++) {
            String str = strArr[i];
            if (TextUtils.equals(dataString, str) || TextUtils.equals(stringExtra, str)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.miui.gallery.hybrid.GalleryHybridFragment.HybridViewEventListener
    public void onReceivedTitle(String str) {
        TextView textView;
        if (TextUtils.isEmpty(str) || (textView = this.mTitleView) == null) {
            return;
        }
        textView.setText(str);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (!this.mHybridFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        HybridClient hybridClient = this.mHybridClient;
        if (hybridClient != null) {
            return hybridClient.getRuntimePermissions();
        }
        return null;
    }
}
