package com.miui.gallery.search.resultpage;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.search.utils.SearchLog;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class SearchResultActivity extends BaseActivity {
    public Fragment mFragment;

    public static /* synthetic */ Fragment $r8$lambda$ef6FiWql1LN0aro3I9M5x6MLVIY(String str, Intent intent, String str2) {
        return lambda$onCreate$0(str, intent, str2);
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return R.id.content;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        setContentView(R.layout.search_result_activity);
        final Intent intent = getIntent();
        if (intent.getData() == null) {
            SearchLog.e("SearchResultActivity", "No extra data fount!");
            finish();
            return;
        }
        Uri data = intent.getData();
        final String str2 = null;
        if (data.getLastPathSegment().equals("result")) {
            str2 = data.getQueryParameter(nexExportFormat.TAG_FORMAT_TYPE);
            str = "client_enter_image_result_page";
        } else {
            str = null;
        }
        if (data.getLastPathSegment().equals("tags") || "tagList".equals(str2)) {
            str = "client_enter_tag_list_page";
            str2 = "tagList";
        } else if (data.getLastPathSegment().equals("locations") || "locationList".equals(str2)) {
            str = "client_enter_location_list_page";
            str2 = "locationList";
        } else if (data.getLastPathSegment().equals("likelyResults") || "likelyImage".equals(str2)) {
            str = "client_enter_likely_image_list_page";
            str2 = "likelyImage";
        }
        if (str2 == null) {
            SearchLog.e("SearchResultActivity", "Invalid action uri, no result type specified!");
            finish();
            return;
        }
        Map buildSearchEventParams = SearchStatUtils.buildSearchEventParams(intent.getExtras());
        if (buildSearchEventParams == null) {
            buildSearchEventParams = new HashMap();
        }
        for (String str3 : data.getQueryParameterNames()) {
            buildSearchEventParams.put(str3, data.getQueryParameter(str3));
        }
        SearchStatUtils.cacheEvent(intent.getStringExtra("from"), str, buildSearchEventParams);
        getIntent().setData(data.buildUpon().appendQueryParameter(nexExportFormat.TAG_FORMAT_TYPE, str2).build());
        this.mFragment = startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.search.resultpage.SearchResultActivity$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str4) {
                return SearchResultActivity.$r8$lambda$ef6FiWql1LN0aro3I9M5x6MLVIY(str2, intent, str4);
            }
        }, "RootFragment", false, false);
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(String str, Intent intent, String str2) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1549236392:
                if (str.equals("tagList")) {
                    c = 0;
                    break;
                }
                break;
            case -878401383:
                if (str.equals("imageList")) {
                    c = 1;
                    break;
                }
                break;
            case -58531341:
                if (str.equals("locationList")) {
                    c = 2;
                    break;
                }
                break;
            case 450823255:
                if (str.equals("likelyImage")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                TrackController.trackClick("403.20.0.1.11272", AutoTracking.getRef());
                return new TagListFragment();
            case 1:
                SearchStatUtils.trackEnterImagePage(intent.getExtras());
                return new SearchImageResultFragment();
            case 2:
                TrackController.trackClick("403.20.0.1.11270", AutoTracking.getRef());
                return new LocationListFragment();
            case 3:
                return new SearchFeedbackLikelyResultFragment();
            default:
                return new SearchResultFragment();
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Fragment fragment = this.mFragment;
        if (fragment == null) {
            return;
        }
        fragment.onActivityResult(i, i2, intent);
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mFragment.onConfigurationChanged(configuration);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        Fragment fragment = this.mFragment;
        if (fragment != null && (fragment instanceof SearchImageResultFragment)) {
            ((SearchImageResultFragment) fragment).onProvideKeyboardShortcuts(list, menu, i);
        }
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        Fragment fragment = this.mFragment;
        if (fragment == null || !(fragment instanceof SearchImageResultFragment) || !((SearchImageResultFragment) fragment).onKeyShortcut(i, keyEvent)) {
            return super.onKeyShortcut(i, keyEvent);
        }
        return true;
    }
}
