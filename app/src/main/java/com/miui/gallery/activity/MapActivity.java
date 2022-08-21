package com.miui.gallery.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.map.utils.MapLibraryLoaderHelper;
import com.miui.gallery.map.utils.MapStatHelper;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.MapFragment;
import com.miui.gallery.ui.MapPrivacyPolicyDialogFragment;
import com.miui.gallery.ui.ProgressDialogFragment;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ResourceUtils;
import com.miui.support.cardview.CardView;

/* loaded from: classes.dex */
public class MapActivity extends BaseActivity {
    public boolean mHasMapPolicyAgreed;
    public double[] mInitialLocation;
    public ProgressDialogFragment mLoadingDialog;
    public MapFragment mMapFragment;
    public final LibraryLoaderHelper.OnLibraryLoadListener mMapLibraryLoadListener = new LibraryLoaderHelper.OnLibraryLoadListener() { // from class: com.miui.gallery.activity.MapActivity.1
        {
            MapActivity.this = this;
        }

        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.OnLibraryLoadListener
        public void onLoading() {
            MapActivity.this.showLoadingDialog();
        }

        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.OnLibraryLoadListener
        public void onLoadFinish(boolean z) {
            MapActivity.this.hideLoadingDialog();
            if (z) {
                MapActivity.this.showMap();
            } else {
                MapActivity.this.mo546getActivity().finish();
            }
        }
    };
    public boolean mNeedShowNearbyLocation;
    public String mTitle;

    /* renamed from: $r8$lambda$0RCGkkE4EHz4uL-fODSjSF99PRI */
    public static /* synthetic */ Fragment m469$r8$lambda$0RCGkkE4EHz4uLfODSjSF99PRI(MapActivity mapActivity, String str) {
        return mapActivity.lambda$onCreate$0(str);
    }

    public static /* synthetic */ void $r8$lambda$82jZNdYACPfYsPeUoZnuXZLm7hA(MapActivity mapActivity, View view) {
        mapActivity.lambda$initActionBar$3(view);
    }

    /* renamed from: $r8$lambda$Eem-Qc0ydktC4-wmlUamDynwfVY */
    public static /* synthetic */ Fragment m470$r8$lambda$EemQc0ydktC4wmlUamDynwfVY(MapActivity mapActivity, String str) {
        return mapActivity.lambda$showMap$2(str);
    }

    /* renamed from: $r8$lambda$mZoWwJMCQz57YTZWS-f1phuTuqQ */
    public static /* synthetic */ void m471$r8$lambda$mZoWwJMCQz57YTZWSf1phuTuqQ(MapActivity mapActivity, View view) {
        mapActivity.lambda$initActionBar$4(view);
    }

    /* renamed from: $r8$lambda$q2-rnxduOSQ57tpSw3yTMeu5As0 */
    public static /* synthetic */ void m472$r8$lambda$q2rnxduOSQ57tpSw3yTMeu5As0(MapActivity mapActivity, boolean z) {
        mapActivity.lambda$showMapPrivacyDialog$1(z);
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return R.id.map_container;
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.map_activity);
        getConfiguration();
        MapLibraryLoaderHelper.getInstance().addLoadLibraryListener(this.mMapLibraryLoadListener);
        boolean isPrivacyPolicyShowed = GalleryPreferences.MapAlbum.isPrivacyPolicyShowed();
        this.mHasMapPolicyAgreed = isPrivacyPolicyShowed;
        if (!isPrivacyPolicyShowed) {
            showMapPrivacyDialog();
        } else if (!MapLibraryLoaderHelper.getInstance().checkAbleOrLoaded(this)) {
        } else {
            this.mMapFragment = (MapFragment) startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.activity.MapActivity$$ExternalSyntheticLambda2
                @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
                public final Fragment create(String str) {
                    return MapActivity.m469$r8$lambda$0RCGkkE4EHz4uLfODSjSF99PRI(MapActivity.this, str);
                }
            }, "MapFragment", false, true);
        }
    }

    public /* synthetic */ Fragment lambda$onCreate$0(String str) {
        return MapFragment.getInstance(this.mTitle, this.mNeedShowNearbyLocation, this.mInitialLocation);
    }

    public final void showMapPrivacyDialog() {
        MapPrivacyPolicyDialogFragment mapPrivacyPolicyDialogFragment = new MapPrivacyPolicyDialogFragment();
        mapPrivacyPolicyDialogFragment.setCancelable(false);
        mapPrivacyPolicyDialogFragment.invoke(this, new OnAgreementInvokedListener() { // from class: com.miui.gallery.activity.MapActivity$$ExternalSyntheticLambda4
            @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
            public final void onAgreementInvoked(boolean z) {
                MapActivity.m472$r8$lambda$q2rnxduOSQ57tpSw3yTMeu5As0(MapActivity.this, z);
            }
        });
    }

    public /* synthetic */ void lambda$showMapPrivacyDialog$1(boolean z) {
        if (z) {
            this.mHasMapPolicyAgreed = true;
            GalleryPreferences.MapAlbum.setPrivacyPolicyShowed(true);
            if (!MapLibraryLoaderHelper.getInstance().checkAbleOrLoaded(this)) {
                return;
            }
            showMap();
            return;
        }
        mo546getActivity().finish();
    }

    public final void showLoadingDialog() {
        if (mo546getActivity() == null || !this.mHasMapPolicyAgreed || this.mLoadingDialog != null) {
            return;
        }
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        this.mLoadingDialog = progressDialogFragment;
        progressDialogFragment.setIndeterminate(false);
        this.mLoadingDialog.setMessage(ResourceUtils.getString(R.string.map_common_library_loading_msg));
        this.mLoadingDialog.setCancelable(false);
        this.mLoadingDialog.showAllowingStateLoss(mo546getActivity().getSupportFragmentManager(), "MapActivity");
    }

    public final void hideLoadingDialog() {
        ProgressDialogFragment progressDialogFragment = this.mLoadingDialog;
        if (progressDialogFragment != null) {
            progressDialogFragment.dismissSafely();
        }
    }

    public final void showMap() {
        if (mo546getActivity() == null || mo546getActivity().isFinishing()) {
            return;
        }
        this.mMapFragment = (MapFragment) startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.activity.MapActivity$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str) {
                return MapActivity.m470$r8$lambda$EemQc0ydktC4wmlUamDynwfVY(MapActivity.this, str);
            }
        }, "MapFragment", false, true);
    }

    public /* synthetic */ Fragment lambda$showMap$2(String str) {
        return MapFragment.getInstance(this.mTitle, this.mNeedShowNearbyLocation, this.mInitialLocation);
    }

    public final void getConfiguration() {
        this.mNeedShowNearbyLocation = getIntent().getBooleanExtra("extra_show_nearby", true);
        this.mInitialLocation = getIntent().getDoubleArrayExtra("extra_initial_location");
        this.mTitle = getIntent().getStringExtra("extra_initial_media_title");
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public void initActionBar() {
        super.initActionBar();
        if (this.mActionBar != null) {
            View inflate = getLayoutInflater().inflate(R.layout.action_bar_map_view, (ViewGroup) null);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(-1, -2);
            ((CardView) inflate.findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.activity.MapActivity$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MapActivity.$r8$lambda$82jZNdYACPfYsPeUoZnuXZLm7hA(MapActivity.this, view);
                }
            });
            ((CardView) inflate.findViewById(R.id.more)).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.activity.MapActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MapActivity.m471$r8$lambda$mZoWwJMCQz57YTZWSf1phuTuqQ(MapActivity.this, view);
                }
            });
            this.mActionBar.setDisplayOptions(16, 16);
            setImmersionMenuEnabled(true);
            this.mActionBar.setBackgroundDrawable(null);
            this.mActionBar.setCustomView(inflate, layoutParams);
        }
    }

    public /* synthetic */ void lambda$initActionBar$3(View view) {
        onBackPressed();
    }

    public /* synthetic */ void lambda$initActionBar$4(View view) {
        showImmersionMenu();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.map_privacy) {
            IntentUtil.gotoBDMapPrivacyPolicy(this);
            MapStatHelper.trackViewPrivacyPolicy("map");
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.map_page_menu, menu);
        return true;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        MapFragment mapFragment = this.mMapFragment;
        if (mapFragment != null) {
            mapFragment.onBackPressed();
        }
        super.onBackPressed();
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        MapLibraryLoaderHelper.getInstance().removeLoadLibraryListener(this.mMapLibraryLoadListener);
        super.onDestroy();
    }
}
