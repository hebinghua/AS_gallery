package com.miui.gallery.picker;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.UriUtil;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class PickFaceAlbumActivity extends PickAlbumDetailActivityBase {
    public AsyncTask<Void, Void, Intent> mParseTask = new AsyncTask<Void, Void, Intent>() { // from class: com.miui.gallery.picker.PickFaceAlbumActivity.1
        @Override // android.os.AsyncTask
        public Intent doInBackground(Void... voidArr) {
            return PickFaceAlbumActivity.this.parseResult();
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Intent intent) {
            if (intent == null) {
                PickFaceAlbumActivity.this.finish();
                return;
            }
            intent.putExtra("local_id_of_album", PickFaceAlbumActivity.this.getIntent().getStringExtra("local_id_of_album"));
            PickFaceAlbumActivity.this.setResult(-1, intent);
            PickFaceAlbumActivity.this.finish();
        }
    };

    @Override // com.miui.gallery.picker.PickAlbumDetailActivityBase, com.miui.gallery.picker.PickerActivity, com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mPicker == null) {
            return;
        }
        setContentView(R.layout.picker_face_detail_activity);
        this.mAlbumDetailImpl = (PickFaceAlbumFragment) getSupportFragmentManager().findFragmentById(R.id.album_detail);
        PickFaceAlbumFragment pickFaceAlbumFragment = (PickFaceAlbumFragment) getSupportFragmentManager().findFragmentById(R.id.album_detail);
        this.mISelectAllDecor = pickFaceAlbumFragment;
        pickFaceAlbumFragment.setItemStateListener(this.mItemStateListener);
        String stringExtra = getIntent().getStringExtra("album_name");
        if (TextUtils.isEmpty(stringExtra)) {
            return;
        }
        setTitle(stringExtra);
    }

    @Override // com.miui.gallery.picker.PickAlbumDetailActivityBase, com.miui.gallery.picker.PickerActivity
    public void onDone(int i) {
        if (getIntent().getBooleanExtra("pick_face_direct", false)) {
            if (getIntent().getBooleanExtra("need_pick_face_id", false)) {
                Iterator<String> it = getPicker().iterator();
                if (it.hasNext()) {
                    Intent intent = new Intent();
                    intent.putExtra("picked_face_id", it.next());
                    setResult(i, intent);
                }
                finish();
                return;
            }
            this.mParseTask.execute(new Void[0]);
            return;
        }
        super.onDone(i);
    }

    public final Intent parseResult() {
        String join = TextUtils.join("','", getPicker());
        Cursor query = getContentResolver().query(UriUtil.appendGroupBy(GalleryContract.Media.URI_PICKER, j.c, null), PickerActivity.PICKABLE_PROJECTION, String.format("_id IN ('%s') AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", join), null, String.format("INSTR(\"'%s'\", _id)", join));
        if (query == null) {
            return null;
        }
        try {
            Intent intent = new Intent();
            ArrayList arrayList = new ArrayList();
            while (query.moveToNext()) {
                arrayList.add(Long.valueOf(query.getLong(0)));
            }
            intent.putExtra("pick-result-data", arrayList);
            return intent;
        } finally {
            query.close();
        }
    }
}
