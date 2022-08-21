package com.miui.gallery.picker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.picker.PickerActivity;
import com.miui.gallery.picker.decor.DefaultDecor;
import com.miui.gallery.picker.helper.Picker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/* loaded from: classes2.dex */
public class PickPeopleActivity extends PickerActivity {
    public PickPeoplePageFragment mFragment;

    @Override // com.miui.gallery.picker.PickerActivity, com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mPicker == null) {
            finish();
            return;
        }
        setContentView(R.layout.picker_people_page_activity);
        boolean booleanExtra = getIntent().getBooleanExtra("pick_people", false);
        PickPeoplePageFragment pickPeoplePageFragment = (PickPeoplePageFragment) getSupportFragmentManager().findFragmentById(R.id.people_page);
        this.mFragment = pickPeoplePageFragment;
        pickPeoplePageFragment.setIsPickPeople(booleanExtra);
        if (booleanExtra) {
            String stringExtra = getIntent().getStringExtra("pick_people_candidate_name");
            if (TextUtils.isEmpty(stringExtra)) {
                stringExtra = getString(R.string.choose_people);
            }
            setTitle(stringExtra);
        }
        String stringExtra2 = getIntent().getStringExtra("album_name");
        if (TextUtils.isEmpty(stringExtra2)) {
            stringExtra2 = getString(R.string.album_name_people);
        }
        setTitle(stringExtra2);
    }

    @Override // com.miui.gallery.picker.PickerActivity
    public Picker onCreatePicker() {
        Intent intent = getIntent();
        int intExtra = intent.getIntExtra("pick-upper-bound", 1);
        int intExtra2 = intent.getIntExtra("pick-lower-bound", 1);
        Picker.MediaType mediaType = Picker.MediaType.values()[intent.getIntExtra("picker_media_type", 0)];
        Set set = (Set) intent.getSerializableExtra("picker_result_set");
        if (getIntent().getBooleanExtra("pick_people", false)) {
            set = new LinkedHashSet();
        }
        Picker.ResultType resultType = Picker.ResultType.values()[intent.getIntExtra("picker_result_type", 0)];
        PickerActivity.SimplePicker simplePicker = new PickerActivity.SimplePicker(this, intExtra, intExtra2, set);
        simplePicker.setMediaType(mediaType);
        simplePicker.setResultType(resultType);
        if (intent.hasExtra("extra_filter_media_type")) {
            simplePicker.setFilterMimeTypes(intent.getStringArrayExtra("extra_filter_media_type"));
        }
        return simplePicker;
    }

    @Override // com.miui.gallery.picker.PickerActivity
    public PickerActivity.Decor onCreateDecor() {
        if (getPicker().getMode() == Picker.Mode.SINGLE) {
            return super.onCreateDecor();
        }
        return new DefaultDecor(this);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        done(4);
    }

    @Override // com.miui.gallery.picker.PickerActivity
    public void onDone(int i) {
        done(i);
    }

    public void done(int i) {
        Intent intent = new Intent();
        intent.putExtra("internal_key_updated_selection", PickerActivity.copyPicker(getPicker()));
        setResult(i, intent);
        finish();
    }

    @Override // com.miui.gallery.picker.PickerBaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != 0) {
            Set<String> set = (Set) intent.getSerializableExtra("internal_key_updated_selection");
            if (set == null) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (String str : this.mPicker) {
                if (!set.contains(str)) {
                    arrayList.add(str);
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.mPicker.remove((String) it.next());
            }
            for (String str2 : set) {
                if (!this.mPicker.contains(str2)) {
                    this.mPicker.pick(str2);
                }
            }
            if (i2 != -1) {
                return;
            }
            this.mPicker.done(-1);
            return;
        }
        Picker picker = this.mPicker;
        if (picker == null) {
            return;
        }
        picker.cancel();
    }
}
