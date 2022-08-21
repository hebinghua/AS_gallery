package com.miui.gallery.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.RevisePhotoFragment;
import com.miui.gallery.ui.TextEditFragment;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.MiscUtil;
import miui.gallery.support.actionbar.ActionBarCompat;
import miuix.view.animation.QuarticEaseInOutInterpolator;

/* loaded from: classes.dex */
public class TextEditActivity extends BaseActivity {
    public static LazyValue<Void, Boolean> sIsNoteOld = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.activity.TextEditActivity.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r2) {
            return Boolean.valueOf(MiscUtil.getAppVersionCode("com.miui.notes") < 310);
        }
    };
    public boolean isRevising;
    public ValueAnimator mBottomAreaAnimator;
    public Fragment mFragment;
    public Guideline mGuideline;
    public String mPicPath;

    public static /* synthetic */ void $r8$lambda$K0LGx82TLPFgOt8ecFNGVppYrh8(TextEditActivity textEditActivity, ValueAnimator valueAnimator) {
        textEditActivity.lambda$hideShowReviseFragment$1(valueAnimator);
    }

    /* renamed from: $r8$lambda$S-MExs1IoqP0cwVHaEQmMsLUEoY */
    public static /* synthetic */ Fragment m477$r8$lambda$SMExs1IoqP0cwVHaEQmMsLUEoY(TextEditActivity textEditActivity, String str) {
        return textEditActivity.lambda$onCreate$0(str);
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return R.id.text_edit_top_area_layout;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
        setContentView(R.layout.text_edit_activity);
        this.mGuideline = (Guideline) findViewById(R.id.text_edit_bottom_guide_line);
        setTitle(getString(R.string.ocr_title));
        ActionBarCompat.setExpandState(this, 0);
        ActionBarCompat.setResizable(this, false);
        this.mPicPath = getIntent().getStringExtra("extra_pic_path");
        this.mFragment = startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.activity.TextEditActivity$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str) {
                return TextEditActivity.m477$r8$lambda$SMExs1IoqP0cwVHaEQmMsLUEoY(TextEditActivity.this, str);
            }
        }, "TextEditFragment", false, false);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.add(R.id.text_edit_bottom_area_layout, prepareRevisePhotoFragment(), "RevisePhotoFragment");
        beginTransaction.commitAllowingStateLoss();
        if (bundle != null) {
            this.isRevising = bundle.getBoolean("is_revising");
        }
    }

    public /* synthetic */ Fragment lambda$onCreate$0(String str) {
        return prepareTextEditFragment();
    }

    public final TextEditFragment prepareTextEditFragment() {
        TextEditFragment textEditFragment = new TextEditFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key_editable_string", getIntent().getStringExtra("extra_editable_string"));
        textEditFragment.setArguments(bundle);
        return textEditFragment;
    }

    public final RevisePhotoFragment prepareRevisePhotoFragment() {
        RevisePhotoFragment revisePhotoFragment = new RevisePhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_revise_photo_item", getIntent().getExtras().getSerializable("extra_revise_photo"));
        revisePhotoFragment.setArguments(bundle);
        return revisePhotoFragment;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mFragment.onActivityResult(i, i2, intent);
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem findItem = menu.findItem(R.id.action_save_to_note);
        if (sIsNoteOld.get(null).booleanValue()) {
            findItem.setTitle(R.string.ocr_save_to_note);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ocr_text_edit_bottom_menu, menu);
        if (this.isRevising) {
            hideShowReviseFragment(menu.findItem(R.id.action_revise));
            return true;
        }
        return true;
    }

    public final void hideShowReviseFragment(MenuItem menuItem) {
        if (((ConstraintLayout.LayoutParams) this.mGuideline.getLayoutParams()).guidePercent == 1.0f) {
            this.isRevising = true;
            menuItem.setChecked(true);
            ValueAnimator valueAnimator = this.mBottomAreaAnimator;
            if (valueAnimator != null) {
                valueAnimator.setFloatValues(1.0f, 0.5f);
            } else {
                this.mBottomAreaAnimator = ValueAnimator.ofFloat(1.0f, 0.5f);
            }
        } else {
            this.isRevising = false;
            menuItem.setChecked(false);
            ValueAnimator valueAnimator2 = this.mBottomAreaAnimator;
            if (valueAnimator2 != null) {
                valueAnimator2.setFloatValues(0.5f, 1.0f);
            } else {
                this.mBottomAreaAnimator = ValueAnimator.ofFloat(0.5f, 1.0f);
            }
        }
        this.mBottomAreaAnimator.setDuration(450L);
        this.mBottomAreaAnimator.setInterpolator(new QuarticEaseInOutInterpolator());
        this.mBottomAreaAnimator.removeAllUpdateListeners();
        this.mBottomAreaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.activity.TextEditActivity$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                TextEditActivity.$r8$lambda$K0LGx82TLPFgOt8ecFNGVppYrh8(TextEditActivity.this, valueAnimator3);
            }
        });
        this.mBottomAreaAnimator.start();
    }

    public /* synthetic */ void lambda$hideShowReviseFragment$1(ValueAnimator valueAnimator) {
        this.mGuideline.setGuidelinePercent(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_revise) {
            SamplingStatHelper.recordCountEvent("OCR", "ocr_text_revise");
            TrackController.trackClick("403.43.0.1.11173", AutoTracking.getRef());
            hideShowReviseFragment(menuItem);
            return true;
        } else if (!this.mFragment.onOptionsItemSelected(menuItem)) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            return true;
        }
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("is_revising", this.isRevising);
    }
}
