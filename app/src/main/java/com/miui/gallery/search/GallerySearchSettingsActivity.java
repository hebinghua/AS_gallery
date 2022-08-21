package com.miui.gallery.search;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.SyncUtil;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public class GallerySearchSettingsActivity extends BaseActivity implements View.OnClickListener {
    public TextView mRequestView;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.search_settings_activity);
        this.mRequestView = (TextView) findViewById(R.id.request);
        int openApiSearchStatus = AIAlbumStatusHelper.getOpenApiSearchStatus();
        configByStatus(openApiSearchStatus);
        if (openApiSearchStatus == 2) {
            AIAlbumStatusHelper.requestOpenCloudControlSearch("GallerySearchSettingsActivity");
        }
    }

    public final void configByStatus(int i) {
        if (i == 0) {
            this.mRequestView.setOnClickListener(this);
            this.mRequestView.setEnabled(true);
            this.mRequestView.setText(R.string.search_request_open);
        } else if (i == 1) {
            this.mRequestView.setOnClickListener(null);
            this.mRequestView.setEnabled(false);
            this.mRequestView.setText(R.string.search_request_applied);
        } else if (i != 2) {
        } else {
            this.mRequestView.setOnClickListener(null);
            this.mRequestView.setEnabled(false);
            this.mRequestView.setText(R.string.search_request_requesting);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == null || view.getId() != R.id.request) {
            return;
        }
        if (!SyncUtil.existXiaomiAccount(this)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("cloud_guide_source", GalleryIntent$CloudGuideSource.SEARCH);
            IntentUtil.guideToLoginXiaomiAccount(this, bundle);
            return;
        }
        AIAlbumStatusHelper.setLocalSearchStatus(this, true);
        configByStatus(2);
        SamplingStatHelper.recordCountEvent(MiStat.Event.SEARCH, "search_open_switch");
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        SamplingStatHelper.recordPageStart(this, "search_settings");
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        SamplingStatHelper.recordPageEnd(this, "search_settings");
    }
}
