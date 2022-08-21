package com.miui.gallery.movie.picker;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.biz.story.data.MediaInfo;
import com.miui.gallery.picker.PickerActivity;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SignatureUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public class MovieStoryPickActivity extends PickerActivity {
    public List<MediaInfo> mMediaInfos;
    public ParseTask mParseTask;
    public Intent mPickIntent;
    public MovieStoryPickFragment mPickStoryFragment;
    public Set<String> mSelectedPhotoSha1s;

    @Override // com.miui.gallery.picker.PickerCompatActivity
    public int getFragmentContainerId() {
        return 16908290;
    }

    @Override // com.miui.gallery.picker.PickerActivity, com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setBackgroundDrawableResource(R.color.story_background_color);
        initActionBar();
        this.mPickIntent = getIntent();
        MovieStoryPickFragment movieStoryPickFragment = (MovieStoryPickFragment) getSupportFragmentManager().findFragmentByTag("StoryMoviePickFragment");
        this.mPickStoryFragment = movieStoryPickFragment;
        if (movieStoryPickFragment == null) {
            MovieStoryPickFragment movieStoryPickFragment2 = new MovieStoryPickFragment();
            this.mPickStoryFragment = movieStoryPickFragment2;
            startFragment(movieStoryPickFragment2, "StoryMoviePickFragment", false, true);
        }
    }

    @Override // com.miui.gallery.picker.PickerActivity
    public void onDone(int i) {
        if (this.mParseTask == null) {
            ParseTask parseTask = new ParseTask();
            this.mParseTask = parseTask;
            parseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            return;
        }
        DefaultLogger.w("MovieStoryPickActivity", "parse task is running, skip");
    }

    @Override // com.miui.gallery.picker.PickerActivity
    public void onCancel() {
        finish();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        onCancel();
    }

    /* loaded from: classes2.dex */
    public class ParseTask extends AsyncTask<Void, Void, Boolean> {
        public ParseTask() {
        }

        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voidArr) {
            boolean z = false;
            Cursor cursor = null;
            try {
                cursor = MovieStoryPickActivity.this.getContentResolver().query(getUri(), StoryMoviePickAdapter.PROJECTION, getSelection(), null, "alias_create_time DESC ");
                if (cursor != null) {
                    int count = cursor.getCount();
                    MovieStoryPickActivity.this.mMediaInfos = new ArrayList(count);
                    while (cursor.moveToNext()) {
                        MovieStoryPickActivity.this.mMediaInfos.add(new MediaInfo(cursor));
                    }
                    z = true;
                }
                return Boolean.valueOf(z);
            } catch (Exception e) {
                DefaultLogger.d("MovieStoryPickActivity", e.getMessage());
                return false;
            } finally {
                BaseMiscUtil.closeSilently(cursor);
            }
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            if (isCancelled()) {
                DefaultLogger.e("MovieStoryPickActivity", "onPostExecute: the task is cancel.");
            } else if (bool.booleanValue()) {
                if (BaseMiscUtil.isValid(MovieStoryPickActivity.this.mMediaInfos)) {
                    try {
                        MovieStoryPickActivity.this.mParseTask = null;
                        ClipData clipData = null;
                        for (int i = 0; i < MovieStoryPickActivity.this.mMediaInfos.size(); i++) {
                            if (MovieStoryPickActivity.this.mMediaInfos.get(i) != null) {
                                Uri translateToContent = GalleryOpenProvider.translateToContent(((MediaInfo) MovieStoryPickActivity.this.mMediaInfos.get(i)).getUri());
                                String sha1 = ((MediaInfo) MovieStoryPickActivity.this.mMediaInfos.get(i)).getSha1();
                                if (clipData == null) {
                                    clipData = new ClipData("data", new String[]{"image/*", "text/uri-list"}, new ClipData.Item(sha1, null, translateToContent));
                                } else {
                                    clipData.addItem(new ClipData.Item(sha1, null, translateToContent));
                                }
                            }
                        }
                        Intent resultIntent = MovieStoryPickActivity.this.getResultIntent();
                        resultIntent.setClipData(clipData);
                        if (SignatureUtils.checkIsPlatformApp(MovieStoryPickActivity.this, resultIntent.getComponent().getPackageName())) {
                            MovieStoryPickActivity.this.setResult(-1, resultIntent);
                        }
                        MovieStoryPickActivity.this.finish();
                        return;
                    } catch (Exception e) {
                        DefaultLogger.d("MovieStoryPickActivity", e.toString());
                        return;
                    }
                }
                DefaultLogger.e("MovieStoryPickActivity", "mMediaInfos is invalid .");
            } else {
                DefaultLogger.e("MovieStoryPickActivity", "onPostExecute: fail.");
            }
        }

        public final String getSelection() {
            if (MovieStoryPickActivity.this.mSelectedPhotoSha1s == null) {
                MovieStoryPickActivity.this.mSelectedPhotoSha1s = new HashSet();
            }
            MovieStoryPickActivity.this.mSelectedPhotoSha1s.clear();
            for (String str : MovieStoryPickActivity.this.mPicker) {
                MovieStoryPickActivity.this.mSelectedPhotoSha1s.add(str);
            }
            return String.format("%s IN ('%s')", "sha1", TextUtils.join("','", MovieStoryPickActivity.this.mSelectedPhotoSha1s));
        }

        public final Uri getUri() {
            return GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(true)).build();
        }
    }

    public Intent getResultIntent() {
        Intent intent = this.mPickIntent;
        return intent != null ? intent : new Intent();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.mPickIntent != null) {
            this.mPickIntent = null;
        }
        ParseTask parseTask = this.mParseTask;
        if (parseTask != null) {
            if (!parseTask.isCancelled()) {
                this.mParseTask.cancel(true);
            }
            this.mParseTask = null;
        }
    }
}
