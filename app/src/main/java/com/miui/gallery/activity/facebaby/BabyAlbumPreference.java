package com.miui.gallery.activity.facebaby;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.provider.BabyLockWallpaperDataManager;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.glide.GlideRequestManagerHelper;
import java.lang.ref.WeakReference;
import miuix.slidingwidget.widget.SlidingButton;

/* compiled from: BabyLockWallpaperSettingsFragment.java */
/* loaded from: classes.dex */
public class BabyAlbumPreference extends Preference {
    public static RequestOptions sRequestOptions = GlideOptions.microThumbOf().mo972placeholder(R.drawable.default_album_empty_cover_small).mo954error(R.drawable.default_album_empty_cover_small).mo956fallback(R.drawable.default_album_empty_cover_small).mo964lock();
    public BabyLockWallpaperDataManager.BabyAlbumInfo mBabyAlbum;
    public boolean mChecked;
    public CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    /* renamed from: $r8$lambda$36-aH3FReHDXlOLo7esMyiCPXX0 */
    public static /* synthetic */ void m479$r8$lambda$36aH3FReHDXlOLo7esMyiCPXX0(SlidingButton slidingButton, View view) {
        lambda$onBindViewHolder$1(slidingButton, view);
    }

    public static /* synthetic */ void $r8$lambda$GrsRTC1hbBoxsTAonSS3uM711e8(BabyAlbumPreference babyAlbumPreference, CompoundButton compoundButton, boolean z) {
        babyAlbumPreference.lambda$onBindViewHolder$0(compoundButton, z);
    }

    public BabyAlbumPreference(Context context, BabyLockWallpaperDataManager.BabyAlbumInfo babyAlbumInfo, CompoundButton.OnCheckedChangeListener onCheckedChangeListener, boolean z) {
        super(context);
        this.mBabyAlbum = babyAlbumInfo;
        this.mOnCheckedChangeListener = onCheckedChangeListener;
        this.mChecked = z;
        setLayoutResource(R.layout.baby_lock_wallpaper_item);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((TextView) preferenceViewHolder.findViewById(R.id.baby_album_name)).setText(this.mBabyAlbum.name);
        final SlidingButton slidingButton = (SlidingButton) preferenceViewHolder.findViewById(R.id.slide_button);
        slidingButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.miui.gallery.activity.facebaby.BabyAlbumPreference$$ExternalSyntheticLambda1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                BabyAlbumPreference.$r8$lambda$GrsRTC1hbBoxsTAonSS3uM711e8(BabyAlbumPreference.this, compoundButton, z);
            }
        });
        preferenceViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.activity.facebaby.BabyAlbumPreference$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BabyAlbumPreference.m479$r8$lambda$36aH3FReHDXlOLo7esMyiCPXX0(SlidingButton.this, view);
            }
        });
        slidingButton.setChecked(this.mChecked);
        ThreadPool miscPool = ThreadManager.getMiscPool();
        BabyLockWallpaperDataManager.BabyAlbumInfo babyAlbumInfo = this.mBabyAlbum;
        miscPool.submit(new CoverQueryJob(babyAlbumInfo.localId, babyAlbumInfo.isOtherShared), new CoverQueryHandler(preferenceViewHolder.itemView));
    }

    public /* synthetic */ void lambda$onBindViewHolder$0(CompoundButton compoundButton, boolean z) {
        this.mOnCheckedChangeListener.onCheckedChanged(compoundButton, z);
        this.mChecked = z;
    }

    public static /* synthetic */ void lambda$onBindViewHolder$1(SlidingButton slidingButton, View view) {
        if (slidingButton.isEnabled()) {
            slidingButton.toggle();
        }
    }

    /* compiled from: BabyLockWallpaperSettingsFragment.java */
    /* loaded from: classes.dex */
    public static class CoverQueryJob implements ThreadPool.Job<String> {
        public long mAlbumLocalId;
        public boolean mIsOtherShare;

        public CoverQueryJob(long j, boolean z) {
            this.mAlbumLocalId = j;
            this.mIsOtherShare = z;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public String mo1807run(ThreadPool.JobContext jobContext) {
            return FaceManager.queryBabyAlbumCover(String.valueOf(this.mAlbumLocalId), this.mIsOtherShare);
        }
    }

    /* compiled from: BabyLockWallpaperSettingsFragment.java */
    /* loaded from: classes.dex */
    public static class CoverQueryHandler extends FutureHandler<String> {
        public WeakReference<View> mCoverViewRef;

        public CoverQueryHandler(View view) {
            this.mCoverViewRef = new WeakReference<>(view);
        }

        @Override // com.miui.gallery.concurrent.FutureHandler
        public void onPostExecute(Future<String> future) {
            RequestManager safeGet;
            View view = this.mCoverViewRef.get();
            if (view == null || view.getContext() == null || future == null || future.get() == null || (safeGet = GlideRequestManagerHelper.safeGet(view)) == null) {
                return;
            }
            safeGet.mo985asBitmap().mo962load(GalleryModel.of(future.get())).mo946apply((BaseRequestOptions<?>) BabyAlbumPreference.sRequestOptions).into((ImageView) view.findViewById(R.id.cover));
        }
    }
}
