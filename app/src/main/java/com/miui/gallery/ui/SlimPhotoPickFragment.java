package com.miui.gallery.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.miui.gallery.R;
import com.miui.gallery.activity.PhotoSlimActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cleaner.slim.SlimController;
import com.miui.gallery.cleaner.slim.SlimScanner;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.SlimPhotoPickFragment;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.util.HashMap;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class SlimPhotoPickFragment extends CleanerPhotoPickFragment {
    public SlimDescriptionDialog mSlimDescriptionDialog;
    public View.OnClickListener mStartSlimListener;

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public int getLayoutSource() {
        return R.layout.slim_photo_pick_layout;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "cleaner_slim_photo_pick";
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordDeleteEvent(int i) {
    }

    public SlimPhotoPickFragment() {
        super(0);
        this.mStartSlimListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.SlimPhotoPickFragment.1
            {
                SlimPhotoPickFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                long[] checkedItemIds = SlimPhotoPickFragment.this.mEditableWrapper.getCheckedItemIds();
                SlimController.getInstance().start(checkedItemIds);
                SlimPhotoPickFragment.this.getActivity().startActivity(new Intent(SlimPhotoPickFragment.this.getActivity(), PhotoSlimActivity.class));
                HashMap hashMap = new HashMap();
                hashMap.put(MiStat.Param.COUNT, SamplingStatHelper.formatValueStage(checkedItemIds.length, CleanerPhotoPickFragment.DELETE_COUNT_STAGE));
                SamplingStatHelper.recordCountEvent("cleaner", "cleaner_slim_used", hashMap);
                TrackController.trackClick("403.27.2.1.11321", AutoTracking.getRef());
                SlimPhotoPickFragment.this.finish();
            }
        };
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordSelectGroupEvent() {
        TrackController.trackClick("403.27.2.1.11320", AutoTracking.getRef());
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordSelectAllEvent() {
        TrackController.trackClick("403.27.2.1.11319", AutoTracking.getRef());
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment, com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        this.mActivity.getAppCompatActionBar().setTitle(R.string.cleaner_slim_title);
        this.mDeleteButton.setOnClickListener(this.mStartSlimListener);
        mo1564getAdapter().setClickToPhotoPageEnable(false);
        return onInflateView;
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d("SlimPhotoPickFragment", "onCreate: ");
        if (GalleryPreferences.PhotoSlim.isFirstUsePhotoSlim()) {
            SlimDescriptionDialog slimDescriptionDialog = new SlimDescriptionDialog(this.mActivity);
            this.mSlimDescriptionDialog = slimDescriptionDialog;
            slimDescriptionDialog.show();
            GalleryPreferences.PhotoSlim.setIsFirstUsePhotoSlim(Boolean.FALSE);
        }
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment, com.miui.gallery.ui.PhotoListFragmentBase, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        SlimDescriptionDialog slimDescriptionDialog = this.mSlimDescriptionDialog;
        if (slimDescriptionDialog != null) {
            slimDescriptionDialog.updateConfiguration(configuration);
        }
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        SlimDescriptionDialog slimDescriptionDialog = this.mSlimDescriptionDialog;
        if (slimDescriptionDialog == null || !slimDescriptionDialog.isShowing()) {
            return;
        }
        this.mSlimDescriptionDialog.dismiss();
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment, com.miui.gallery.ui.PhotoListFragmentBase
    public String getSelection() {
        return String.format("%s AND %s IN (%s)", SlimScanner.SYNCED_SLIM_SCAN_SELECTION, j.c, TextUtils.join(",", this.mScanResultIds));
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordCancelSelectAllEvent() {
        SamplingStatHelper.recordCountEvent("cleaner", "slim_select_all_cancel");
    }

    /* loaded from: classes2.dex */
    public class SlimDescriptionDialog extends AlertDialog {
        public LinearLayout mButtonGroup;
        public TextView mPhotoSlimAfter;
        public TextView mPhotoSlimBefore;
        public LinearLayout mPictureGroup;
        public Button mPositiveBtn;
        public TextView mSubtitle;

        public static /* synthetic */ void $r8$lambda$QX6Bzkl7Vk2OtOfxCN_PoP7CWac(SlimDescriptionDialog slimDescriptionDialog, View view) {
            slimDescriptionDialog.lambda$new$0(view);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SlimDescriptionDialog(Context context) {
            super(context);
            SlimPhotoPickFragment.this = r6;
            View inflate = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.photo_slim_description_dialog, (ViewGroup) null);
            setView(inflate);
            TextView textView = (TextView) inflate.findViewById(R.id.sub_title);
            this.mSubtitle = textView;
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            this.mPictureGroup = (LinearLayout) inflate.findViewById(R.id.slimPictureGroup);
            TextView textView2 = (TextView) inflate.findViewById(R.id.photo_slim_before);
            this.mPhotoSlimBefore = textView2;
            textView2.setText(r6.getString(R.string.slim_description_dialog_slim_before, Float.valueOf(3.07f)));
            TextView textView3 = (TextView) inflate.findViewById(R.id.photo_slim_after);
            this.mPhotoSlimAfter = textView3;
            textView3.setText(r6.getString(R.string.slim_description_dialog_slim_after, Integer.valueOf((int) BDLocation.TypeServerError)));
            Button button = (Button) inflate.findViewById(R.id.button);
            this.mPositiveBtn = button;
            button.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.SlimPhotoPickFragment$SlimDescriptionDialog$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SlimPhotoPickFragment.SlimDescriptionDialog.$r8$lambda$QX6Bzkl7Vk2OtOfxCN_PoP7CWac(SlimPhotoPickFragment.SlimDescriptionDialog.this, view);
                }
            });
            this.mButtonGroup = (LinearLayout) inflate.findViewById(R.id.buttonGroup);
            updateConfiguration(r6.getResources().getConfiguration());
        }

        public /* synthetic */ void lambda$new$0(View view) {
            SlimPhotoPickFragment.this.mSlimDescriptionDialog.dismiss();
        }

        public void updateConfiguration(Configuration configuration) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mSubtitle.getLayoutParams();
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mButtonGroup.getLayoutParams();
            if (configuration.orientation == 2) {
                layoutParams2.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.slim_description_dialog_land_button_margin_top);
                this.mPictureGroup.setVisibility(8);
                layoutParams.leftMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.slim_description_dialog_land_detail_margin_left);
                layoutParams.rightMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.slim_description_dialog_land_detail_margin_right);
            } else {
                this.mPictureGroup.setVisibility(0);
                layoutParams.leftMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.slim_description_dialog_detail_margin_left_right);
                layoutParams.rightMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.slim_description_dialog_detail_margin_left_right);
                layoutParams2.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.slim_description_dialog_button_top_margin);
            }
            this.mSubtitle.setLayoutParams(layoutParams);
            this.mButtonGroup.setLayoutParams(layoutParams2);
        }
    }
}
