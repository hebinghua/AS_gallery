package com.miui.gallery.picker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.picker.PickerActivity;
import com.miui.gallery.picker.decor.DefaultDecor;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.ui.album.picker.ai.PickAIAlbumFragment;
import com.miui.gallery.ui.album.picker.other.PickOtherAlbumFragment;
import com.miui.gallery.util.ResourceUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.c.b;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes2.dex */
public class PickAlbumActivity extends PickerActivity {
    @Override // com.miui.gallery.picker.PickerCompatActivity
    public int getFragmentContainerId() {
        return 16908290;
    }

    @Override // com.miui.gallery.picker.PickerActivity, com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        String string;
        super.onCreate(bundle);
        if (this.mPicker == null) {
            finish();
            return;
        }
        String str = "";
        switch (getIntent().getIntExtra("extra_to_type", -1)) {
            case 1003:
                startFragment(new PickOtherAlbumFragment(), "StoryMoviePickFragment", false, true);
                string = ResourceUtils.getString(R.string.other_album);
                break;
            case 1004:
            default:
                finish();
                string = str;
                break;
            case 1005:
                startFragment(new PickAIAlbumFragment(), "StoryMoviePickFragment", false, true);
                string = ResourceUtils.getString(R.string.album_ai_page_title);
                break;
            case 1006:
                if (getIntent().getData() == null) {
                    finish();
                    return;
                }
                getIntent().setData(getIntent().getData().buildUpon().appendQueryParameter(nexExportFormat.TAG_FORMAT_TYPE, "locationList").build());
                startFragment(new PickLocationListFragment(), "StoryMoviePickFragment", false, true);
                string = ResourceUtils.getString(R.string.album_name_locations);
                break;
            case b.g /* 1007 */:
                if (getIntent().getData() == null) {
                    finish();
                    return;
                }
                getIntent().setData(getIntent().getData().buildUpon().appendQueryParameter(nexExportFormat.TAG_FORMAT_TYPE, "tagList").build());
                startFragment(new PickTagListFragment(), "StoryMoviePickFragment", false, true);
                string = ResourceUtils.getString(R.string.album_name_tags);
                break;
        }
        if (!TextUtils.isEmpty(string)) {
            str = string;
        }
        setTitle(str);
    }

    @Override // com.miui.gallery.picker.PickerActivity
    public Picker onCreatePicker() {
        Intent intent = getIntent();
        int intExtra = intent.getIntExtra("pick-upper-bound", 1);
        int intExtra2 = intent.getIntExtra("pick-lower-bound", 1);
        int intExtra3 = intent.getIntExtra("picker_media_type", 0);
        Picker.ResultType resultType = Picker.ResultType.values()[intent.getIntExtra("picker_result_type", 0)];
        PickerActivity.SimplePicker simplePicker = new PickerActivity.SimplePicker(this, intExtra, intExtra2, (Set) intent.getSerializableExtra("picker_result_set"));
        simplePicker.setMediaType(Picker.MediaType.values()[intExtra3]);
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
        done(2);
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
        this.mPicker.cancel();
    }
}
